package com.ribeiroanibal.adopt.rest.controller.dto


import com.ribeiroanibal.adopt.rest.dto.UserPostDto
import com.ribeiroanibal.adopt.util.ObjectMapperCustom
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import spock.lang.Specification

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import static net.javacrumbs.jsonunit.core.util.ResourceUtils.resource
import static spock.util.matcher.HamcrestSupport.that

class UserPostDtoSpec extends Specification {
    def USER_POST_JSON = 'json/userPostDto.json'
    def objectMapper = ObjectMapperCustom.customMapper()
    def userPostDto = new UserPostDto('username', 'passowrd', '123456')

    def "should serialize"() {
        expect:
        that objectMapper.writeValueAsString(userPostDto), jsonEquals(resource(USER_POST_JSON))
    }

    def "should deserialize"() {
        when:
        def deserialized = objectMapper.readValue(resource(USER_POST_JSON), UserPostDto)

        then:
        with(deserialized) {
            username == 'username'
            password == 'passowrd'
            phone == '123456'
        }
    }

    def "should verify equals"() {
        expect:
        EqualsVerifier.forClass(UserPostDto)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify()
    }
}
