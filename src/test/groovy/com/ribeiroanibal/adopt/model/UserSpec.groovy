package com.ribeiroanibal.adopt.model

import com.ribeiroanibal.adopt.util.ObjectMapperCustom
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import spock.lang.Specification

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import static net.javacrumbs.jsonunit.core.util.ResourceUtils.resource
import static spock.util.matcher.HamcrestSupport.that

class UserSpec extends Specification {
    def USER_JSON = 'json/user.json'
    def objectMapper = ObjectMapperCustom.customMapper()
    def user = new User('username', 'passowrd', '123456')

    def "should serialize"() {
        expect:
        that objectMapper.writeValueAsString(user), jsonEquals(resource(USER_JSON))
    }

    def "should deserialize"() {
        when:
        def deserialized = objectMapper.readValue(resource(USER_JSON), User)

        then:
        with(deserialized) {
            username == 'username'
            password == 'passowrd'
            phone == '123456'
        }
    }

    def "should verify equals"() {
        expect:
        EqualsVerifier.forClass(User)
                .suppress(Warning.NONFINAL_FIELDS, Warning.STRICT_INHERITANCE)
                .verify()
    }
}
