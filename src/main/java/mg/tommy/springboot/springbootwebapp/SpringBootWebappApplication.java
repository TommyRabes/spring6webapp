package mg.tommy.springboot.springbootwebapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableConfigurationProperties
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class})
@EnableScheduling
public class SpringBootWebappApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootWebappApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebappApplication.class, args);
	}

}
