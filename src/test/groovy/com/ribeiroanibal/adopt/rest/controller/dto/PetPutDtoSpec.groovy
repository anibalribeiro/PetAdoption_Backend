package com.ribeiroanibal.adopt.rest.controller.dto

import com.ribeiroanibal.adopt.model.enums.PetCategoryEnum
import com.ribeiroanibal.adopt.rest.dto.PetPutDto
import com.ribeiroanibal.adopt.util.ObjectMapperCustom
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import spock.lang.Specification

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import static net.javacrumbs.jsonunit.core.util.ResourceUtils.resource
import static spock.util.matcher.HamcrestSupport.that

class PetPutDtoSpec extends Specification {
    def PET_PUT_JSON = 'json/petPutDto.json'
    def objectMapper = ObjectMapperCustom.customMapper()
    def petPutDto = new PetPutDto(0L,
            'Rex',
            0.3f,
            PetCategoryEnum.DOG,
            'descrip',
            1L,
            'http://localhost/files/photo.jpg',
            true)

    def "should serialize"() {
        expect:
        that objectMapper.writeValueAsString(petPutDto), jsonEquals(resource(PET_PUT_JSON))
    }

    def "should deserialize"() {
        when:
        def deserialized = objectMapper.readValue(resource(PET_PUT_JSON), PetPutDto)

        then:
        with(deserialized) {
            name == 'Rex'
            age == 0.3f
            description == 'descrip'
            photo == 'http://localhost/files/photo.jpg'
            version == 0
            active
        }
    }

    def "should verify equals"() {
        expect:
        EqualsVerifier.forClass(PetPutDto)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify()
    }
}
