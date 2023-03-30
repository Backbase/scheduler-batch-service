package com.backbase.accelerators.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.mapstruct.Named;

import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Objects.isNull;

public interface BaseMapper {

    @SneakyThrows
    @Named("toJsonString")
    default String toJsonString(Map<String, String> additions) {
        if (isNull(additions)) {
            return null;
        }

        return new ObjectMapper().writeValueAsString(additions);
    }

    @SneakyThrows
    @Named("toMap")
    default Map<String, String> toMap(String additionsJsonString) {
        if (isNull(additionsJsonString)) {
            return emptyMap();
        }

        return new ObjectMapper().readValue(additionsJsonString, Map.class);
    }
}
