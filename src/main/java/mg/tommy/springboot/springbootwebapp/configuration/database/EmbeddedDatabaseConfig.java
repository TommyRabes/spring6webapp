package mg.tommy.springboot.springbootwebapp.configuration.database;

import mg.tommy.springboot.springbootwebapp.domain.embedded.Post;
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
import java.util.HashMap;
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

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.embedded")
    public DataSourceProperties embeddedDataSourceProperties() {
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
            EntityManagerFactoryBuilder builder
    ) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = builder
                .dataSource(embeddedDataSource)
                .persistenceUnit("Embedded")
                .packages(Post.class)
                .build();

        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactory.setJpaPropertyMap(propertiesMap());
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

    private Map<String, String> propertiesMap() {
        // Trying to mimic Spring Boot's default configuration
        Map<String, String> propertiesMap = new HashMap<>();
        // propertiesMap.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
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
