package mg.tommy.springboot.springbootwebapp.configuration.database.property;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Setter
@ConfigurationProperties(prefix = "spring.jpa.hibernate")
public class HibernateJpaProperties extends SpringJpaPropertiesAggregator {
    private static final String KEY_PREFIX = "hibernate";

    private String dialect;
    private String defaultSchema;
    private String hbm2ddlAuto;
    private String physicalNamingStrategy;
    private boolean showSql;
    private boolean formatSql;
    private boolean useSqlComments;
    private boolean generateStatistics;

    @Override
    public Map<String, Object> jpaPropertyMap() {
        return Map.of(
                getKey("dialect"), dialect,
                getKey("default_schema"), defaultSchema,
                getKey("hbm2ddl.auto"), hbm2ddlAuto,
                getKey("physical_naming_strategy"), physicalNamingStrategy,
                getKey("show_sql"), showSql,
                getKey("format_sql"), formatSql,
                getKey("use_sql_comments"), useSqlComments,
                getKey("generate_statistics"), generateStatistics
        );
    }

    @Override
    protected String getKey(String key) {
        return KEY_PREFIX + "." + key;
    }

}
