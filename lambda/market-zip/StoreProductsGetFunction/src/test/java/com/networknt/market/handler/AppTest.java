
package com.networknt.market.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * This is a generated stub for unit test cases. It contains some useful methods that will assist
 * developers to write test cases in BusinessHandlerTest. Please don't modify this class as it
 * might be overwritten in the next generation.
 *
 * @author Steve Hu
 */
public class AppTest {
    ObjectMapper objectMapper = new ObjectMapper();

    public void setEnv(String key, String value) {
        try {
            Map<String, String> env = System.getenv();
            Class<?> cl = env.getClass();
            Field field = cl.getDeclaredField("m");
            field.setAccessible(true);
            Map<String, String> writableEnv = (Map<String, String>) field.get(env);
            writableEnv.put(key, value);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to set environment variable", e);
        }
    }
}
