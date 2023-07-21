package mg.tommy.springboot.springbootwebapp.configuration.database;

import mg.tommy.springboot.springbootwebapp.configuration.database.property.HibernateJpaProperties;
import mg.tommy.springboot.springbootwebapp.configuration.database.property.JpaSchemaProperties;
import mg.tommy.springboot.springbootwebapp.model.domain.persistent.User;
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
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackageClasses = UserRepository.class,
        entityManagerFactoryRef = "persistentEntityManagerFactory",
        transactionManagerRef = "persistentTransactionManager"
)
public class PersistentDatabaseConfig {

    @Profile("MySQL")
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
            @Qualifier("persistentJPAPropertyMap") Map<String, Map<String, Object>> propertyMap) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = builder
                .dataSource(dataSource)
                .persistenceUnit("Persistent")
                .packages(User.class)
                .build();

        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactory.setJpaPropertyMap(propertyMap.get("persistentJPAPropertyMap"));
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

    @Profile("PG")
    @Bean("persistentHibernateProperties")
    @ConfigurationProperties(prefix = "spring.jpa.pg.hibernate")
    public HibernateJpaProperties pgHibernateJpaProperties() {
        return new HibernateJpaProperties();
    }

    @Profile("PG")
    @Bean("persistentJpaProperties")
    @ConfigurationProperties(prefix = "spring.jpa.pg.properties")
    public JpaSchemaProperties pgJpaSchemaProperties() {
        return new JpaSchemaProperties();
    }

    @Profile("MySQL")
    @Bean("persistentHibernateProperties")
    @ConfigurationProperties(prefix = "spring.jpa.mysql.hibernate")
    public HibernateJpaProperties mysqlHibernateJpaProperties() {
        return new HibernateJpaProperties();
    }

    @Profile("MySQL")
    @Bean("persistentJpaProperties")
    @ConfigurationProperties(prefix = "spring.jpa.mysql.properties")
    public JpaSchemaProperties mysqlJpaSchemaProperties() {
        return new JpaSchemaProperties();
    }

    @Bean
    public Map<String, Object> persistentJPAPropertyMap(HibernateJpaProperties persistentHibernateProperties, JpaSchemaProperties persistentJpaProperties) {
        Map<String, Object> jpaPropertyMap = persistentHibernateProperties.jpaPropertyMap();
        jpaPropertyMap.put("javax.persistence.sharedCache.mode", "ENABLE_SELECTIVE");
        jpaPropertyMap.putAll(persistentJpaProperties.jpaPropertyMap());

        return jpaPropertyMap;
    }

}
