package mg.tommy.springboot.springbootwebapp.controller;

import mg.tommy.springboot.springbootwebapp.service.DataSourceService;
import org.springframework.stereotype.Controller;

@Controller
public class DataSourceController {

    private final DataSourceService dataSourceService;

    public DataSourceController(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    public String getDataSource() {
        return dataSourceService.getDataSource();
    }
}
