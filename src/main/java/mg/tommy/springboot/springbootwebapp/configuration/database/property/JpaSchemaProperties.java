package mg.tommy.springboot.springbootwebapp.configuration.database.property;

import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@ToString
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
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.hasText(databaseAction))
            map.put(getKey("database.action"), databaseAction);
        if (StringUtils.hasText(scriptsAction))
            map.put(getKey("scripts.action"), scriptsAction);
        if (StringUtils.hasText(scriptsCreateSource))
            map.put(getKey("scripts.create-source"), scriptsCreateSource);
        if (StringUtils.hasText(scriptsDropTarget))
            map.put(getKey("scripts.drop-target"), scriptsDropTarget);
        if (StringUtils.hasText(scriptsCreateTarget))
            map.put(getKey("scripts.create-target"), scriptsCreateTarget);
        return map;
    }

    @Override
    protected String getKey(String key) {
        return KEY_PREFIX + "." + key;
    }
}
