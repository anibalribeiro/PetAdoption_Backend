package com.ribeiroanibal.adopt.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

/**
 * Custom object mapper which is supposed to take plain ObjectMapper from the Spring's context
 * and configure it properly.
 * <p>
 * So instead, I am directly autowiring preconfigured instance via {@link #customMapper()} for now.
 */
@Component
public class ObjectMapperCustom implements BeanPostProcessor {

    /**
     * Add extended configuration to the ObjectMapper
     *
     * @return Configured mapper
     * @throws BeansException
     */
    public Object postProcessAfterBaseInitialization(final ObjectMapper mapper) {

        mapper.registerModule(new Jdk8Module()); //support of `Optional` java type

        final JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(Instant.class, new InstantSerializerWithMilliSecondPrecision());
        mapper.registerModule(module);

        mapper.registerModule(new ParameterNamesModule());

        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

        final SimpleModule exceptionModule = new SimpleModule("ExceptionModule");
        exceptionModule.addSerializer(Throwable.class, new ExceptionSerializer());
        mapper.registerModule(exceptionModule);

        return mapper;
    }

    /**
     * Add FAIL_ON_UNKNOWN_PROPERTIES set to false to the config of the prepared ObjectMapper
     * Determines that encountering of unknown properties will not result in a failure
     *
     * @return Configured mapper
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {

        if (!(bean instanceof ObjectMapper)) {
            return bean;
        }

        final ObjectMapper mapper = (ObjectMapper) bean;
        postProcessAfterBaseInitialization(mapper);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return mapper;
    }

    /**
     * Add additional FAIL_ON_UNKNOWN_PROPERTIES set to false to the config of the prepared ObjectMapper
     *
     * @return Configured mapper
     */
    public static ObjectMapper customMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectMapperCustom processor = new ObjectMapperCustom();
        processor.postProcessAfterInitialization(mapper, null);
        return mapper;
    }

    private static class ExceptionSerializer extends JsonSerializer<Throwable> {
        @Override
        public void serialize(final Throwable value,
                              final JsonGenerator jgen,
                              final SerializerProvider provider) throws IOException {
            if (value == null) {
                jgen.writeNull();
            } else {
                final ExceptionSerialized serialized =
                        new ExceptionSerialized(
                                value.getClass().getName(),
                                value.getMessage(),
                                ExceptionUtils.getStackTrace(value));

                jgen.writeObject(serialized);
            }
        }
    }


    private static class ExceptionSerialized {
        private final String clazz;
        private final String message;
        private final String stackTrace;

        public ExceptionSerialized(final String clazz, final String message, final String stackTrace) {
            this.clazz = clazz;
            this.message = message;
            this.stackTrace = stackTrace;
        }

        public String getClazz() {
            return clazz;
        }

        public String getMessage() {
            return message;
        }

        public String getStackTrace() {
            return stackTrace;
        }
    }

}
