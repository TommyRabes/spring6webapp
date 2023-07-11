package mg.tommy.springboot.springbootwebapp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles({"Dev", "PG"})
@SpringBootTest
class DataSourceDevControllerTest {

    @Autowired
    private DataSourceController controller;

    @Test
    void getDataSource() {
        assertEquals("Dev Data Source", controller.getDataSource());
    }
}