package com.ribeiroanibal.adopt.model

import com.ribeiroanibal.adopt.model.enums.PetCategoryEnum
import com.ribeiroanibal.adopt.util.ObjectMapperCustom
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import spock.lang.Specification

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import static net.javacrumbs.jsonunit.core.util.ResourceUtils.resource
import static spock.util.matcher.HamcrestSupport.that

class PetSpec extends Specification {
    def PET_JSON = 'json/pet.json'
    def objectMapper = ObjectMapperCustom.customMapper()
    def pet = new Pet('Rex',
            0.3f,
            PetCategoryEnum.DOG,
            'descrip',
            new User('user1@email.com', 'pass', '123'),
            'http://localhost/files/photo.jpg',
            true)

    def "should serialize"() {
        expect:
        that objectMapper.writeValueAsString(pet), jsonEquals(resource(PET_JSON))
    }

    def "should deserialize"() {
        when:
        def deserialized = objectMapper.readValue(resource(PET_JSON), Pet)

        then:
        with(deserialized) {
            name == 'Rex'
            age == 0.3f
            description == 'descrip'
            photo == 'http://localhost/files/photo.jpg'
        }
    }

    def "should verify equals"() {
        expect:
        EqualsVerifier.forClass(Pet)
                .suppress(Warning.NONFINAL_FIELDS, Warning.STRICT_INHERITANCE)
                .verify()
    }
}
