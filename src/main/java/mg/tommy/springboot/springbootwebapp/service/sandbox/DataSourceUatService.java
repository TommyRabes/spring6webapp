package mg.tommy.springboot.springbootwebapp.service.sandbox;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("Uat")
@Service
public class DataSourceUatService implements DataSourceService {

    @Override
    public String getDataSource() {
        return "Uat Data Source";
    }
}
