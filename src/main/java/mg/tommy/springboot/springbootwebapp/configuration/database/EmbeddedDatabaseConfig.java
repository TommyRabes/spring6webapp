package mg.tommy.springboot.springbootwebapp.configuration.database;

import mg.tommy.springboot.springbootwebapp.configuration.database.property.HibernateJpaProperties;
import mg.tommy.springboot.springbootwebapp.configuration.database.property.JpaSchemaProperties;
import mg.tommy.springboot.springbootwebapp.model.domain.embedded.Post;
import mg.tommy.springboot.springbootwebapp.repository.embedded.PostRepository;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackageClasses = PostRepository.class,
        entityManagerFactoryRef = "embeddedEntityManagerFactory",
        transactionManagerRef = "embeddedTransactionManager"
)
public class EmbeddedDatabaseConfig {

    @Profile("H2")
    @Bean("embeddedDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.embedded")
    public DataSourceProperties embeddedDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Profile("MSSQL")
    @Bean("embeddedDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.mssql")
    public DataSourceProperties mssqlDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource embeddedDataSource(
            @Qualifier("embeddedDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean embeddedEntityManagerFactory(
            @Qualifier("embeddedDataSource") DataSource embeddedDataSource,
            EntityManagerFactoryBuilder builder,
            // It's odd but when we inject a Map with Object types as value type
            // we get a Map with a single key (the qualifier) mapped to the desired bean
            @Qualifier("embeddedJPAPropertyMap") Map<String, Map<String, Object>> propertyMap
    ) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = builder
                .dataSource(embeddedDataSource)
                .persistenceUnit("Embedded")
                .packages(Post.class)
                .build();

        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactory.setJpaPropertyMap(propertyMap.get("embeddedJPAPropertyMap"));
        entityManagerFactory.setPersistenceProviderClass(HibernatePersistenceProvider.class);

        Objects.requireNonNull(entityManagerFactory.getPersistenceProvider()).generateSchema("Embedded", entityManagerFactory.getJpaPropertyMap());
        return entityManagerFactory;
    }

    @Bean
    public PlatformTransactionManager embeddedTransactionManager(
            @Qualifier("embeddedEntityManagerFactory") LocalContainerEntityManagerFactoryBean embeddedEntityManagerFactory
    ) {
        return new JpaTransactionManager(Objects.requireNonNull(embeddedEntityManagerFactory.getObject()));
    }

    @Bean
    public JdbcTemplate embeddedJdbcTemplate(
            @Qualifier("embeddedDataSource") DataSource embeddedDateSource) {
        return new JdbcTemplate(embeddedDateSource);
    }

    @Profile("H2")
    @Bean("embeddedHibernateProperties")
    @ConfigurationProperties(prefix = "spring.jpa.h2.hibernate")
    public HibernateJpaProperties h2HibernateJpaProperties() {
        return new HibernateJpaProperties();
    }

    @Profile("H2")
    @Bean("embeddedJpaProperties")
    @ConfigurationProperties(prefix = "spring.jpa.h2.properties")
    public JpaSchemaProperties h2JpaSchemaProperties() {
        return new JpaSchemaProperties();
    }

    @Profile("MSSQL")
    @Bean("embeddedHibernateProperties")
    @ConfigurationProperties(prefix = "spring.jpa.mssql.hibernate")
    public HibernateJpaProperties mssqlHibernateJpaProperties() {
        return new HibernateJpaProperties();
    }

    @Profile("MSSQL")
    @Bean("embeddedJpaProperties")
    @ConfigurationProperties(prefix = "spring.jpa.mssql.properties")
    public JpaSchemaProperties mssqlJpaSchemaProperties() {
        return new JpaSchemaProperties();
    }

    @Bean
    public Map<String, Object> embeddedJPAPropertyMap(HibernateJpaProperties embeddedHibernateProperties, JpaSchemaProperties embeddedJpaProperties) {
        Map<String, Object> jpaPropertyMap = embeddedHibernateProperties.jpaPropertyMap();
        jpaPropertyMap.put("javax.persistence.sharedCache.mode", "ENABLE_SELECTIVE");
        jpaPropertyMap.putAll(embeddedJpaProperties.jpaPropertyMap());

        return jpaPropertyMap;
    }

}
