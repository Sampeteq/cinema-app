package code.utils;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class SpringIT {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private DataSource dataSource;

    @AfterEach
    void cleanDb() {
        new DbCleaner(dataSource).clean();
    }
}
