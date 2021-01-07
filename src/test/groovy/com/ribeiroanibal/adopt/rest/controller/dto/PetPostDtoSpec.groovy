package com.ribeiroanibal.adopt.rest.controller.dto

import com.ribeiroanibal.adopt.model.enums.PetCategoryEnum
import com.ribeiroanibal.adopt.rest.dto.PetPostDto
import com.ribeiroanibal.adopt.util.ObjectMapperCustom
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import spock.lang.Specification

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import static net.javacrumbs.jsonunit.core.util.ResourceUtils.resource
import static spock.util.matcher.HamcrestSupport.that

class PetPostDtoSpec extends Specification {
    def PET_POST_JSON = 'json/petPostDto.json'
    def objectMapper = ObjectMapperCustom.customMapper()
    def petPostDto = new PetPostDto('Rex', 0.3f, PetCategoryEnum.DOG, 'descrip', 1L, 'http://localhost/files/photo.jpg')

    def "should serialize"() {
        expect:
        that objectMapper.writeValueAsString(petPostDto), jsonEquals(resource(PET_POST_JSON))
    }

    def "should deserialize"() {
        when:
        def deserialized = objectMapper.readValue(resource(PET_POST_JSON), PetPostDto)

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
        EqualsVerifier.forClass(PetPostDto)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify()
    }
}
