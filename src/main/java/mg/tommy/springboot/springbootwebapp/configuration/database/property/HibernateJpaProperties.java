package mg.tommy.springboot.springbootwebapp.configuration.database.property;

import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@ToString
@Setter
@ConfigurationProperties(prefix = "spring.jpa.hibernate")
public class HibernateJpaProperties extends SpringJpaPropertiesAggregator {
    private static final String KEY_PREFIX = "hibernate";

    private String dialect;
    private String defaultSchema;
    private String hbm2ddlAuto;
    private String physicalNamingStrategy;
    private boolean showSql = false;
    private boolean formatSql = false;
    private boolean useSqlComments = false;
    private boolean generateStatistics = false;

    @Override
    public Map<String, Object> jpaPropertyMap() {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.hasText(dialect))
            map.put(getKey("dialect"), dialect);
        if (StringUtils.hasText(defaultSchema))
            map.put(getKey("default_schema"), defaultSchema);
        if (StringUtils.hasText(hbm2ddlAuto))
            map.put(getKey("hbm2ddl.auto"), hbm2ddlAuto);
        if (StringUtils.hasText(physicalNamingStrategy))
            map.put(getKey("physical_naming_strategy"), physicalNamingStrategy);
        map.put("show_sql", showSql);
        map.put("format_sql", formatSql);
        map.put("use_sql_comments", useSqlComments);
        map.put("generate_statistics", generateStatistics);
        return map;
    }

    @Override
    protected String getKey(String key) {
        return KEY_PREFIX + "." + key;
    }

}
