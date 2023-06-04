package code;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class SpringIT {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private DataSource dataSource;

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @AfterEach
    void cleanDb() {
        new SqlDatabaseCleaner(dataSource).clean();
    }

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
