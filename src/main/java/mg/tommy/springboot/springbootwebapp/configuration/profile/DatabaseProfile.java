package mg.tommy.springboot.springbootwebapp.configuration.profile;

import mg.tommy.springboot.springbootwebapp.configuration.database.EmbeddedDatabaseConfig;
import mg.tommy.springboot.springbootwebapp.controller.api.BeerApiController;
import mg.tommy.springboot.springbootwebapp.controller.page.PageController;
import mg.tommy.springboot.springbootwebapp.service.brewing.BeerService;
import mg.tommy.springboot.springbootwebapp.service.library.BookService;
import mg.tommy.springboot.springbootwebapp.service.traveling.PlanService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("Database")
@Configuration
@ComponentScan(
        basePackageClasses = {
                EmbeddedDatabaseConfig.class,
                BeerApiController.class,
                PageController.class,
                BeerService.class,
                BookService.class,
                PlanService.class,
        }
)
public class DatabaseProfile {
}
