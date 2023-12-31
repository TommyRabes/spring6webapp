package mg.tommy.springboot.springbootwebapp.service.brewing;

import mg.tommy.springboot.springbootwebapp.model.dto.BeerRecord;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface BeerCsvService {
    List<BeerRecord> convertCsv(File csvFile);
    List<BeerRecord> convertCsv(InputStream csvInputStream);
}
