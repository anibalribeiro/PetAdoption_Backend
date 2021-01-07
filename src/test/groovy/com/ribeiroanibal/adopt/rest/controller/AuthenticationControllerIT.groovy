package com.ribeiroanibal.adopt.rest.controller

import com.ribeiroanibal.adopt.AdoptApplication
import com.ribeiroanibal.adopt.ITSpecificationCommon
import com.ribeiroanibal.adopt.model.enums.PetCategoryEnum
import com.ribeiroanibal.adopt.repository.PetRepository
import com.ribeiroanibal.adopt.repository.UserRepository
import com.ribeiroanibal.adopt.rest.dto.LoginRequest
import com.ribeiroanibal.adopt.rest.dto.PetPostDto
import com.ribeiroanibal.adopt.rest.dto.UserPostDto
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AuthenticationControllerIT extends ITSpecificationCommon {

    @Autowired
    UserRepository userRepository

    @Autowired
    PetRepository petRepository

    def "token should contain username"() {
        given:
        def user = registerUser()

        expect:
        authenticateAndDecodeToken().sub == user.username

        cleanup:
        userRepository.deleteAll()
    }

    def "unauthenticated request on a unprotected endpoint should pass"() {
        expect:
        mockMvc.perform(get(AdoptApplication.API_PETS)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
    }

    def "should fail to authenticate with incorrect credentials"() {
        when:
        def response = mockMvc.perform(post(AdoptApplication.API_AUTH + '/signin')
                .content(asJsonString(new LoginRequest('incorrectUsername', 'incorrectPassword')))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().response
        response = new JsonSlurper().parseText(response.contentAsString)

        then:
        response.errorId == 'ERROR.ACCESS.BAD_CREDENTIALS'
        response.messageParams == ['Bad credentials']
    }

    def "request with proper permission should pass"() {
        given:
        def user = registerUser()
        def jwtDetails = authenticateAndGetDetails()
        def accessToken = 'Bearer ' + jwtDetails.get("token").toString()

        expect:
        mockMvc.perform(
                post(AdoptApplication.API_PETS)
                        .content(asJsonString(new PetPostDto('pet1',
                                0.1f,
                                PetCategoryEnum.DOG,
                                'desc',
                                user.id,
                                'photo')))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER, accessToken)
        ).andExpect(status().is2xxSuccessful())

        cleanup:
        petRepository.deleteAll()
        userRepository.deleteAll()
    }

    def "should not allow to register same user twice"() {
        when:
        mockMvc.perform(
                post(AdoptApplication.API_AUTH + '/signup')
                        .content(asJsonString(new UserPostDto('user1', 'pass1', '123456')))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful())

        then:
        userRepository.count() == 1

        when:
        def response = mockMvc.perform(
                post(AdoptApplication.API_AUTH + '/signup')
                        .content(asJsonString(new UserPostDto('user1', 'pass1', '123456')))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response
        response = new JsonSlurper().parseText(response.contentAsString)

        then:
        response.errorId == 'ERROR.ENTITY.EXISTS'
        response.messageParams == ['user', 'user1']

        cleanup:
        userRepository.deleteAll()
    }

    private Map<String, Object> authenticateAndDecodeToken() {
        def jwtToken = authenticateAndGetDetails().token.toString()
        def parts = jwtToken.split("\\."); // split out the "parts" (header, payload and signature)

        def payloadJson = new String(Base64.urlDecoder.decode(parts[1]))
        new JsonSlurper().parseText(payloadJson)
    }


}
