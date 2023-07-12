package mg.tommy.springboot.springbootwebapp.service.sandbox;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile({ "Dev", "default" })
@Service
public class DataSourceDevService implements DataSourceService {
    @Override
    public String getDataSource() {
        return "Dev Data Source";
    }
}
