package mg.tommy.springboot.springbootwebapp.configuration.profile;

import mg.tommy.springboot.springbootwebapp.controller.sandbox.DataSourceController;
import mg.tommy.springboot.springbootwebapp.service.sandbox.DataSourceService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("sandbox")
@Configuration
@ComponentScan(
        basePackageClasses = {DataSourceController.class, DataSourceService.class}
)
public class SandboxProfile {
}
