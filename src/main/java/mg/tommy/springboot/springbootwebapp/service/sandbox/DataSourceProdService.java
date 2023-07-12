package mg.tommy.springboot.springbootwebapp.service.sandbox;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("Prod")
@Service
public class DataSourceProdService implements DataSourceService {
    @Override
    public String getDataSource() {
        return "Prod Data Source";
    }
}
