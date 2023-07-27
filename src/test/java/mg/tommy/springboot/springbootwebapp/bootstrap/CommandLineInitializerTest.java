package mg.tommy.springboot.springbootwebapp.bootstrap;

import mg.tommy.springboot.springbootwebapp.configuration.database.EmbeddedDatabaseConfig;
import mg.tommy.springboot.springbootwebapp.mapper.BeerMapperImpl;
import mg.tommy.springboot.springbootwebapp.mapper.BeerPropertyMapping;
import mg.tommy.springboot.springbootwebapp.model.domain.embedded.Beer;
import mg.tommy.springboot.springbootwebapp.model.domain.embedded.Customer;
import mg.tommy.springboot.springbootwebapp.repository.embedded.BeerRepository;
import mg.tommy.springboot.springbootwebapp.repository.embedded.CustomerRepository;
import mg.tommy.springboot.springbootwebapp.service.brewing.BeerCsvService;
import mg.tommy.springboot.springbootwebapp.service.brewing.BeerCsvServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        EmbeddedDatabaseConfig.class,
        CommandLineInitializer.class,
        BeerCsvServiceImpl.class,
        BeerMapperImpl.class,
        BeerPropertyMapping.class
})
@Transactional("embeddedTransactionManager")
@Rollback
class CommandLineInitializerTest {

    @Autowired
    BeerRepository beerRepository;
    @Autowired
    CustomerRepository customerRepository;

    @Test
    public void beerBootstrapTest() {
        List<Beer> beers = beerRepository.findAll();

        assertThat(beers).hasSize(2413);
        for (Beer beer : beers) {
            assertThat(beer.getId()).isNotNull();
            assertThat(beer.getVersion()).isNotNull();
        }
    }


    @Test
    public void customerBootstrapTest() {
        List<Customer> customers = customerRepository.findAll();

        assertThat(customers).hasSize(3);
        assertThat(customers.get(0).getId()).isNotNull();
        assertThat(customers.get(0).getVersion()).isNotNull();
        assertThat(customers.get(1).getId()).isNotNull();
        assertThat(customers.get(1).getVersion()).isNotNull();
        assertThat(customers.get(2).getId()).isNotNull();
        assertThat(customers.get(2).getVersion()).isNotNull();
    }

}