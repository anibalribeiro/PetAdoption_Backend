package com.ribeiroanibal.adopt

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.ribeiroanibal.adopt.model.User
import com.ribeiroanibal.adopt.rest.dto.LoginRequest
import com.ribeiroanibal.adopt.rest.dto.UserPostDto

import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.junit.Assert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(beanName = "dataSource")
abstract class ITSpecificationCommon extends ApiSpecification {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    def initialUser = new User('user1@email.com', 'pass', '123456')

    protected Map<String, Object> authenticateAndGetDetails() {
        def response = apiPost(
                post(AdoptApplication.API_AUTH + '/signin'),
                new LoginRequest(initialUser.username, initialUser.password)
        )
        objectMapper.convertValue(response, Map)
    }

    protected void jsonEquals(final Object actualObject, final String expectedJson) {
        def actualJson = objectMapper.writeValueAsString(actualObject)
        jsonEquals(actualJson, expectedJson)
    }

    /**
     * Convenience method to check if two json strings are equal.
     * The benefit is that it will show nice "View difference" in the IDE if comparison fails.
     */
    protected void jsonEquals(final String actualJson, final String expectedJson) {
        def actualJsonMap = objectMapper.readValue(actualJson, JsonNode.class)
        def expectedJsonMap = objectMapper.readValue(expectedJson, JsonNode.class)

        //  It will not fail if the field order is different.
        if (!Objects.equals(actualJsonMap, expectedJsonMap)) {
            // If the test fails, it will show the difference nicely formatted.
            def actualUnifiedJson = objectMapper.writeValueAsString(actualJsonMap)
            def expectedUnifiedJson = objectMapper.writeValueAsString(expectedJsonMap)

            // If the test fails, it will show the difference nicely formatted.
            Assert.assertEquals expectedUnifiedJson, actualUnifiedJson
        }
    }

    protected User registerUser() {
        def response = apiPost(
                post(AdoptApplication.API_AUTH + '/signup'),
                new UserPostDto(initialUser.username, initialUser.password, initialUser.phone),
                HttpStatus.CREATED
        )
        objectMapper.convertValue(response, User)
    }

    protected String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj)
        } catch (Exception e) {
            throw new RuntimeException(e)
        }
    }
}
