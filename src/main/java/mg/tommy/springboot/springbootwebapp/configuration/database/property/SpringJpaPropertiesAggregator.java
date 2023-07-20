package mg.tommy.springboot.springbootwebapp.configuration.database.property;

import java.util.Map;

public abstract class SpringJpaPropertiesAggregator {
    public abstract Map<String, Object> jpaPropertyMap();

    protected abstract String getKey(String key);
}
