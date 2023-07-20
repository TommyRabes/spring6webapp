package mg.tommy.springboot.springbootwebapp.configuration.database.property;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Setter
@ConfigurationProperties(prefix = "spring.jpa.properties")
public class JpaSchemaProperties extends SpringJpaPropertiesAggregator {
    private static final String KEY_PREFIX = "jakarta.persistence.schema-generation";

    private String databaseAction;
    private String scriptsAction;
    private String scriptsCreateSource;
    private String scriptsDropTarget;
    private String scriptsCreateTarget;

    @Override
    public Map<String, Object> jpaPropertyMap() {
        return Map.of(
                getKey("database.action"), databaseAction,
                getKey("scripts.action"), scriptsAction,
                getKey("scripts.create-source"), scriptsCreateSource,
                getKey("scripts.drop-target"), scriptsDropTarget,
                getKey("scripts.create-target"), scriptsCreateTarget
        );
    }

    @Override
    protected String getKey(String key) {
        return KEY_PREFIX + "." + key;
    }
}
