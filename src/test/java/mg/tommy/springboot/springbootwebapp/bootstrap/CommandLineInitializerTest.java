package mg.tommy.springboot.springbootwebapp.bootstrap;

import mg.tommy.springboot.springbootwebapp.domain.embedded.Beer;
import mg.tommy.springboot.springbootwebapp.domain.embedded.Customer;
import mg.tommy.springboot.springbootwebapp.repository.embedded.BeerRepository;
import mg.tommy.springboot.springbootwebapp.repository.embedded.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(CommandLineInitializer.class)
class CommandLineInitializerTest {

    @Autowired
    BeerRepository beerRepository;
    @Autowired
    CustomerRepository customerRepository;

    @Test
    public void beerBootstrapTest() {
        List<Beer> beers = beerRepository.findAll();

        assertThat(beers).hasSize(3);
        assertThat(beers.get(0).getId()).isNotNull();
        assertThat(beers.get(0).getVersion()).isNotNull();
        assertThat(beers.get(1).getId()).isNotNull();
        assertThat(beers.get(1).getVersion()).isNotNull();
        assertThat(beers.get(2).getId()).isNotNull();
        assertThat(beers.get(2).getVersion()).isNotNull();
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