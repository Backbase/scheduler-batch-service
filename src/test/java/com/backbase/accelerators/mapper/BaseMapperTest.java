package com.backbase.accelerators.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class BaseMapperTest {

    private final BaseMapper mapper = new BaseMapper() {};

    @Test
    public void toJsonStringTest() {
        assertNull(mapper.toJsonString(null));

        Map<String, String> emptyMap = Collections.emptyMap();
        assertEquals("{}", mapper.toJsonString(emptyMap));

        Map<String, String> testMap = new HashMap<>();
        testMap.put("key1", "value1");
        testMap.put("key2", "value2");
        String jsonString = mapper.toJsonString(testMap);
        assertEquals("{\"key1\":\"value1\",\"key2\":\"value2\"}", jsonString);
    }

    @Test
    public void toJsonStringExceptionTest() {
        Map<String, String> invalidMap = new HashMap<>();
        invalidMap.put("validKey", "validValue");
        invalidMap.put(null, "valueWithNullKey");
        invalidMap.put("keyWithNullValue", null);
        assertThrows(Exception.class, () -> mapper.toJsonString(invalidMap));
    }

    @Test
    public void toMapTest() {
        assertEquals(Collections.emptyMap(), mapper.toMap(null));

        assertEquals(Collections.emptyMap(), mapper.toMap("{}"));

        String jsonString = "{\"key1\":\"value1\",\"key2\":\"value2\"}";
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("key1", "value1");
        expectedMap.put("key2", "value2");
        assertEquals(expectedMap, mapper.toMap(jsonString));
    }

    @Test
    public void toMapExceptionTest() {
        String invalidJsonString = "invalid";
        assertThrows(Exception.class, () -> mapper.toMap(invalidJsonString));
    }
}