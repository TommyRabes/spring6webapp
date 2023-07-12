package mg.tommy.springboot.springbootwebapp.configuration.profile;

import mg.tommy.springboot.springbootwebapp.SpringBootWebappApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("Full")
@Configuration
@ComponentScan(basePackageClasses = SpringBootWebappApplication.class)
public class FullProfile {
}
