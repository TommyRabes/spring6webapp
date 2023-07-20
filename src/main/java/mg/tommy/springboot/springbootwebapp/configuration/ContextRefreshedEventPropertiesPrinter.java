package mg.tommy.springboot.springbootwebapp.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static java.util.function.Predicate.not;

@Slf4j
@Component
public class ContextRefreshedEventPropertiesPrinter {

    @EventListener
    public void handleContextRefreshed(ContextRefreshedEvent event) {
        ConfigurableEnvironment env = (ConfigurableEnvironment) event.getApplicationContext().getEnvironment();

        env.getPropertySources()
                .stream()
                .filter(ps -> ps instanceof MapPropertySource && (ps.getName().contains("application.properties") || ps.getName().contains("application.yml")))
                .map(ps -> ((MapPropertySource) ps).getSource().keySet())
                .flatMap(Collection::stream)
                .filter(not("spring.mail.password"::equals))
                .distinct()
                .sorted()
                .forEach(key -> log.info("{}={}", key, env.getProperty(key)));
    }
}
