package mg.tommy.springboot.springbootwebapp.service.brewing;

import com.opencsv.bean.CsvToBeanBuilder;
import mg.tommy.springboot.springbootwebapp.model.dto.BeerRecord;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class BeerCsvServiceImpl implements BeerCsvService {
    @Override
    public List<BeerRecord> convertCsv(File csvFile) {
        try {
            return new CsvToBeanBuilder<BeerRecord>(new FileReader(csvFile))
                    .withType(BeerRecord.class)
                    .build().parse();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<BeerRecord> convertCsv(InputStream csvInputStream) {
        return new CsvToBeanBuilder<BeerRecord>(new InputStreamReader(csvInputStream))
                .withType(BeerRecord.class)
                .build().parse();
    }
}
