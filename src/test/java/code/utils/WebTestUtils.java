package code.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;

public abstract class WebTestUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public static <T> T fromJson(String json, Class<T> type) throws JsonProcessingException {
        return objectMapper.readValue(json, type);
    }

    public static <T> T fromResultActions(ResultActions actions, Class<T> type) throws UnsupportedEncodingException, JsonProcessingException {
        var stringContent = actions
                .andReturn()
                .getResponse()
                .getContentAsString();
        return fromJson(stringContent, type);
    }
}
