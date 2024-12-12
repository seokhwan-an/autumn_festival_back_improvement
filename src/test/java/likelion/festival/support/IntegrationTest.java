package likelion.festival.support;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IntegrationTest {

    @Autowired
    private DataCleaner dataCleaner;

    @AfterEach
    public void cleanUp() {
        dataCleaner.clear();
    }
}
