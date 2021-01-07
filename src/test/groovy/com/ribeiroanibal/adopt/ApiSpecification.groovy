package com.ribeiroanibal.adopt

import com.google.common.io.CharStreams
import groovy.json.JsonSlurper
import net.javacrumbs.jsonunit.core.util.ResourceUtils
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import spock.lang.Ignore
import spock.lang.Specification

import static java.nio.charset.StandardCharsets.UTF_8
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

/**
 * Helper class with common utilities for REST API tests.
 *
 * Extend it in the actual test. Make sure your test class defines objectMapper and mockMvc fields.
 */
@Ignore
abstract class ApiSpecification extends Specification {

    private static final Set EXPECTED_JSON_HEADERS = [
            'application/json',
            'application/json;charset=UTF-8'
    ] as Set

    static String urlEncode(final String value) {
        URLEncoder.encode(value, UTF_8.name())
    }

    static Object withoutVariableFields(final Object content) {
        content.tap {
            createdDate = '${json-unit.ignore}'
            updatedDate = '${json-unit.ignore}'
            id = '${json-unit.ignore}'
        }
    }

    static Object pageableWithoutVariableFields(final Object content) {
        content.data.each {
            it.createdDate = '${json-unit.ignore}'
            it.updatedDate = '${json-unit.ignore}'
            it.id = '${json-unit.ignore}'
        }
        return content
    }

    /**
     * Load specified resource
     *
     * @param resourceName path to resource, should be available in the classpath
     * @return the contents
     */
    static String resource(String resourceName) {
        CharStreams.toString(ResourceUtils.resource(resourceName))
    }

    // @formatter:off
    /**
     * Send simple GET request, with expectation that it must be successful.
     *
     * @param uri template of URI, e.g. /my/controller/{userId}
     * @param uriArgs variables to use for "expanding" the URI template
     * @return parsed response contents
     */
    protected Object apiGet(String uri, Object... uriArgs) {
        apiGet(get(uri, uriArgs))
    }

    /**
     * Use when custom expected status is needed.
     * {@code apiGet(get("/my/controller/{userId}", USER_ID), HttpStatus.NOT_FOUND)}
     *
     * @param requestBuilder {@code get(uri, args)}
     * @param expectedStatus {@code HttpStatus.NOT_FOUND}
     * @return parsed response contents
     */
    protected Object apiGet(MockHttpServletRequestBuilder requestBuilder,
                            HttpStatus expectedStatus = HttpStatus.OK) {
        def response = mockMvc.perform(requestBuilder.characterEncoding(UTF_8.name()))
                .andReturn().response
        assert response.status == expectedStatus.value()
        assert EXPECTED_JSON_HEADERS.contains(response.contentType)

        return new JsonSlurper().parseText(new String(response.getContentAsByteArray(), UTF_8.name()))
    }

    /**
     * Send data to API endpoint and return response contents.
     * {@code apiSubmit(put("/my/controller/{userId}", USER_ID), modifiedUser, HttpStatus.FORBIDDEN)}
     *
     * @param requestBuilder {@code put(uri, args)}
     * @param dataToSend request content for sending
     * @param expectedStatus {@code HttpStatus.NOT_FOUND}
     * @return parsed response contents
     */
    protected Object apiSubmit(MockHttpServletRequestBuilder requestBuilder,
                               Object dataToSend,
                               HttpStatus expectedStatus = HttpStatus.OK) {
        String contentType = MediaType.APPLICATION_JSON.toString()
        def response = mockMvc.perform(requestBuilder
                .content(objectMapper.writeValueAsString(dataToSend))
                .characterEncoding(UTF_8.name())
                .contentType(contentType))
                .andReturn().response
        assert response.status == expectedStatus.value()

        if (expectedStatus.isError() && response.contentType == null) {
             // In case there is not JSON content type header, the response may not have a body at all.
            return null
        }

        assert EXPECTED_JSON_HEADERS.contains(response.contentType)
        return new JsonSlurper().parseText(response.contentAsString)
    }

    /**
     * Send POST request
     * {@code apiPost(put("/my/controller/{userId}", USER_ID), modifiedUser, HttpStatus.FORBIDDEN)}
     *
     * @param requestBuilder {@code put(uri, args)}
     * @param dataToSend request content for sending
     * @param expectedStatus {@code HttpStatus.NOT_FOUND}
     * @return parsed response contents
     */
    protected Object apiPost(MockHttpServletRequestBuilder requestBuilder,
                             Object dataToSend,
                             HttpStatus expectedStatus = HttpStatus.OK) {
        apiSubmit(requestBuilder, dataToSend, expectedStatus)
    }

    /**
     * Send PUT request
     * {@code apiPut(put("/my/controller/{userId}", USER_ID), modifiedUser, HttpStatus.FORBIDDEN)}
     *
     * @param requestBuilder {@code put(uri, args)}
     * @param dataToSend request content for sending
     * @param expectedStatus {@code HttpStatus.NOT_FOUND}
     * @return parsed response contents
     */
    protected Object apiPut(MockHttpServletRequestBuilder requestBuilder,
                            Object dataToSend,
                            HttpStatus expectedStatus = HttpStatus.OK) {
        apiSubmit(requestBuilder, dataToSend, expectedStatus)
    }
    // @formatter:on

    protected abstract MockMvc getMockMvc();
}
