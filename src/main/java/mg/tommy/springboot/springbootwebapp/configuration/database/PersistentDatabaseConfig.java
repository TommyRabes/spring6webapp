package mg.tommy.springboot.springbootwebapp.configuration.database;

import mg.tommy.springboot.springbootwebapp.domain.persistent.User;
import mg.tommy.springboot.springbootwebapp.repository.persistent.UserRepository;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Profile("!(Dev | Prod | QA | Uat)")
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackageClasses = UserRepository.class,
        entityManagerFactoryRef = "persistentEntityManagerFactory",
        transactionManagerRef = "persistentTransactionManager"
)
public class PersistentDatabaseConfig {

    @Profile("default")
    @Bean("persistentDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.persistent")
    public DataSourceProperties persistentDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Profile("PG")
    @Bean("persistentDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.postgres")
    public DataSourceProperties postgresDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource persistentDataSource(
            @Qualifier("persistentDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return persistentDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean persistentEntityManagerFactory(
            @Qualifier("persistentDataSource") DataSource dataSource,
            EntityManagerFactoryBuilder builder,
            @Qualifier("persistentJPAProperties") Map<String, String> propertyMap) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = builder
                .dataSource(dataSource)
                .persistenceUnit("Persistent")
                .packages(User.class)
                .build();

        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactory.setJpaPropertyMap(propertyMap);
        entityManagerFactory.setPersistenceProviderClass(HibernatePersistenceProvider.class);

        Objects.requireNonNull(entityManagerFactory.getPersistenceProvider()).generateSchema("Persistent", entityManagerFactory.getJpaPropertyMap());
        return entityManagerFactory;
    }

    @Bean
    public PlatformTransactionManager persistentTransactionManager(
            @Qualifier("persistentEntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactoryBean
    ) {
        return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactoryBean.getObject()));
    }

    @Bean
    public JdbcTemplate persistentJdbcTemplate(
            @Qualifier("persistentDataSource") DataSource persistentDateSource) {
        return new JdbcTemplate(persistentDateSource);
    }

    @Profile("default")
    @Bean("persistentJPAProperties")
    public Map<String, String> mysqlPropertiesMap() {
        // Trying to mimic Spring Boot's default configuration
        Map<String, String> propertiesMap = new HashMap<>();
        // Spring Boot can't guess the appropriate Hibernate Dialect, so I need to specify it explicitly
        propertiesMap.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");

        // Should be 'create' at first and then 'update' for a non-in-memory database like MySQL
        propertiesMap.put("hibernate.hbm2ddl.auto", "create-drop");
        // To implement to custom naming strategy, see https://vladmihalcea.com/hibernate-physical-naming-strategy/
        propertiesMap.put("hibernate.physical_naming_strategy", "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
        propertiesMap.put("hibernate.show_sql", "false");
        propertiesMap.put("hibernate.format_sql", "false");
        propertiesMap.put("hibernate.use_sql_comments", "false");
        propertiesMap.put("hibernate.generate_statistics", "false");
        propertiesMap.put("javax.persistence.sharedCache.mode", "ENABLE_SELECTIVE");
        return propertiesMap;
    }

    @Profile("PG")
    @Bean("persistentJPAProperties")
    public Map<String, String> postgresPropertiesMap() {
        // Trying to mimic Spring Boot's default configuration
        Map<String, String> propertiesMap = new HashMap<>();
        // Spring Boot can't guess the appropriate Hibernate Dialect, so I need to specify it explicitly
        propertiesMap.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

        // Should be 'create' at first and then 'update' for a non-in-memory database like MySQL
        propertiesMap.put("hibernate.hbm2ddl.auto", "create-drop");
        // To implement to custom naming strategy, see https://vladmihalcea.com/hibernate-physical-naming-strategy/
        propertiesMap.put("hibernate.physical_naming_strategy", "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
        propertiesMap.put("hibernate.show_sql", "false");
        propertiesMap.put("hibernate.format_sql", "false");
        propertiesMap.put("hibernate.use_sql_comments", "false");
        propertiesMap.put("hibernate.generate_statistics", "false");
        propertiesMap.put("javax.persistence.sharedCache.mode", "ENABLE_SELECTIVE");
        return propertiesMap;
    }

}
