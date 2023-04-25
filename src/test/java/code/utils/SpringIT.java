package code.utils;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class SpringIT {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected DbCleaner dbCleaner;

    @AfterEach
    void cleanDb() {
        dbCleaner.clean();
    }
}
