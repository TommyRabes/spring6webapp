package mg.tommy.springboot.springbootwebapp.service;

import mg.tommy.springboot.springbootwebapp.model.dto.BeerRecord;
import mg.tommy.springboot.springbootwebapp.service.brewing.BeerCsvService;
import mg.tommy.springboot.springbootwebapp.service.brewing.BeerCsvServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BeerCsvServiceTest {
    private final BeerCsvService beerCsvService = new BeerCsvServiceImpl();

    @Test
    public void convertCsvTest() throws FileNotFoundException {
        File csvFile = ResourceUtils.getFile("classpath:static/repository/csv/beers.csv");

        List<BeerRecord> beerRecords = beerCsvService.convertCsv(csvFile);

        assertThat(beerRecords).isNotNull();
        assertThat(beerRecords).hasSize(2410);
    }
}
