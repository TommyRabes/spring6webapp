package mg.tommy.springboot.springbootwebapp;

import mg.tommy.springboot.springbootwebapp.configuration.profile.FullProfile;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableConfigurationProperties
@SpringBootApplication(
		exclude = {SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class},
		scanBasePackageClasses = {FullProfile.class}
)
@EnableScheduling
public class SpringBootWebappApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebappApplication.class, args);
	}

}
