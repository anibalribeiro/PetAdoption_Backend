package com.ribeiroanibal.adopt.rest.controller

import com.ribeiroanibal.adopt.AdoptApplication
import com.ribeiroanibal.adopt.ITSpecificationCommon
import com.ribeiroanibal.adopt.model.Pet
import com.ribeiroanibal.adopt.model.enums.PetCategoryEnum
import com.ribeiroanibal.adopt.repository.PetRepository
import com.ribeiroanibal.adopt.repository.UserRepository
import com.ribeiroanibal.adopt.rest.dto.PetPostDto
import com.ribeiroanibal.adopt.rest.dto.PetPutDto
import com.ribeiroanibal.adopt.service.PetService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.lang.Unroll

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put

class PetControllerIT extends ITSpecificationCommon {

    def LIST_PETS_JSON = 'json/pets.json'
    def CREATED_PET_JSON = 'json/createdPet.json'

    @Autowired
    PetService petService

    @Autowired
    PetRepository petRepository

    @Autowired
    UserRepository userRepository

    def "should create, get, list and update an pet"() {
        given:
        def user = registerUser(initialUser.username, initialUser.password, initialUser.phone)
        def pet = new Pet('Frantisek',
                1.5,
                PetCategoryEnum.DOG,
                'nice dog',
                user,
                'http://localhost/files/photo2.jpg',
                true)
        def created = petService.addEntity(pet)
        def jwtDetails = authenticateAndGetDetails(initialUser.username, initialUser.password)
        def accessToken = 'Bearer ' + jwtDetails.get("token").toString()

        when: "POST request to create an pet"
        def response = apiPost(
                post(AdoptApplication.API_PETS).contentType(MediaType.APPLICATION_JSON).header(AUTHORIZATION_HEADER,
                        accessToken),
                new PetPostDto('Rex',
                        0.3,
                        PetCategoryEnum.DOG,
                        'descrip',
                        user.id,
                        'http://localhost/files/photo.jpg'),
                HttpStatus.CREATED
        )

        then:
        jsonEquals withoutVariableFields(response), resource(CREATED_PET_JSON)

        when: "should load single entity pet by ID"
        response = apiGet(AdoptApplication.API_PETS_ITEM, created.id)

        then:
        objectMapper.convertValue(created, Map) == response

        when: "should list pets"
        response = apiGet(AdoptApplication.API_PETS)

        then:
        jsonEquals pageableWithoutVariableFields(response), resource(LIST_PETS_JSON)

        when: "should mark one pet as adopted"
        response = apiPut(
                put(AdoptApplication.API_PETS_ITEM, created.id).contentType(MediaType.APPLICATION_JSON).header(
                        AUTHORIZATION_HEADER,
                        accessToken),
                new PetPutDto(0L,
                        'Frantisek',
                        1.5,
                        PetCategoryEnum.DOG,
                        'nice dog',
                        user.id,
                        'http://localhost/files/photo2.jpg',
                        false)
        )

        then:
        response.version == 1
        response.active == false

        when: "should try to load entity by ID non existing and throw exception"
        response = apiGet(get(AdoptApplication.API_PETS_ITEM, 9999), HttpStatus.NOT_FOUND)

        then:
        response.errorId == 'ERROR.ENTITY.NOT_FOUND.BY_ID'
        response.messageParams == ['pet', 9999]

        cleanup:
        userRepository.deleteAll()
        petRepository.deleteAll()
    }

    def "should test permissions for updating and removing a pet"() {
        given:
        def user = registerUser(initialUser.username, initialUser.password, initialUser.phone)
        def pet = new Pet('Frantisek',
                1.5,
                PetCategoryEnum.DOG,
                'nice dog',
                user,
                'http://localhost/files/photo2.jpg',
                true)
        def created = petService.addEntity(pet)

        when: "add another user and try to modify the previous added pet from a different user"
        def newUser = registerUser('newUser', 'pass', '123')
        def jwtDetails = authenticateAndGetDetails(newUser.username, 'pass')
        def accessToken = 'Bearer ' + jwtDetails.get("token").toString()
        def response = apiPut(
                put(AdoptApplication.API_PETS_ITEM, created.id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER, accessToken),
                new PetPutDto(0L,
                        'Frantisek',
                        1.5,
                        PetCategoryEnum.DOG,
                        'nice dog',
                        user.id,
                        'http://localhost/files/photo2.jpg',
                        false),
                HttpStatus.FORBIDDEN
        )

        then:
        response.errorId == 'ERROR.ACCESS.PERMISSION'
        response.messageParams == []

        cleanup:
        userRepository.deleteAll()
        petRepository.deleteAll()
    }

    @Unroll
    def "#url should return #expected"() {
        given:
        def user = registerUser(initialUser.username, initialUser.password, initialUser.phone)
        petService.addEntity(new Pet('Pet 1', 1.4, PetCategoryEnum.DOG, 'desc pet 1', user, '1', true))
        petService.addEntity(new Pet('Pet 2', 2.5, PetCategoryEnum.CAT, 'desc pet 2', user, '2', true))
        petService.addEntity(new Pet('Pet 3', 0.2, PetCategoryEnum.FISH, 'desc pet 3', user, '3', true))
        petService.addEntity(new Pet('Pet 4', 1, PetCategoryEnum.FISH, 'desc pet 4', user, '4', false))

        when:
        def content = apiGet(api(url))

        then:
        content.data?.name == expected

        cleanup:
        userRepository.deleteAll()
        petRepository.deleteAll()

        where:
        url                | expected
        '/'                | ['Pet 1', 'Pet 2', 'Pet 3']
        '?page=0&size=2'   | ['Pet 1', 'Pet 2']
        '?page=1&size=1'   | ['Pet 2']
        '?page=0&size=1'   | ['Pet 1']
        '?sort=age'        | ['Pet 3', 'Pet 1', 'Pet 2']
        '?sort=age,desc'   | ['Pet 2', 'Pet 1', 'Pet 3']
        '?sort=photo,desc' | ['Pet 3', 'Pet 2', 'Pet 1']
    }

    private static String api(String url) {
        return (AdoptApplication.API_PETS + "/" + url).replace('//', '/')
    }

}
