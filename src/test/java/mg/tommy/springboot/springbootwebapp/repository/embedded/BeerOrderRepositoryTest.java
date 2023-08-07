package mg.tommy.springboot.springbootwebapp.repository.embedded;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import mg.tommy.springboot.springbootwebapp.bootstrap.CommandLineInitializer;
import mg.tommy.springboot.springbootwebapp.configuration.database.EmbeddedDatabaseConfig;
import mg.tommy.springboot.springbootwebapp.mapper.BeerMapperImpl;
import mg.tommy.springboot.springbootwebapp.mapper.BeerPropertyMapping;
import mg.tommy.springboot.springbootwebapp.model.domain.embedded.Beer;
import mg.tommy.springboot.springbootwebapp.model.domain.embedded.BeerOrder;
import mg.tommy.springboot.springbootwebapp.model.domain.embedded.BeerOrderShipment;
import mg.tommy.springboot.springbootwebapp.model.domain.embedded.Customer;
import mg.tommy.springboot.springbootwebapp.service.brewing.BeerCsvServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        EmbeddedDatabaseConfig.class,
        CommandLineInitializer.class,
        BeerCsvServiceImpl.class,
        BeerMapperImpl.class,
        BeerPropertyMapping.class
})
class BeerOrderRepositoryTest {

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    BeerOrderShipmentRepository beerOrderShipmentRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerRepository beerRepository;

    @PersistenceContext
    EntityManager entityManager;

    Customer testCustomer;
    Beer testBeer;

    @BeforeEach
    public void setUp() {
        testCustomer = customerRepository.findAll().get(0);
        testBeer = beerRepository.findAll().get(0);
    }

    @Test
    public void countBeerOrderTest() {
        BeerOrder beerOrder = BeerOrder.builder()
                .customerRef("Test order")
                .customer(testCustomer)
                .beerOrderShipment(BeerOrderShipment.builder()
                        .trackingNumber("6814735")
                        .build())
                .build();

        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);
        entityManager.flush();

        BeerOrderShipment savedBeerOrderShipment = beerOrderShipmentRepository.findById(savedBeerOrder.getBeerOrderShipment().getId()).orElse(null);

        assertThat(savedBeerOrder.getCustomerRef()).isEqualTo(beerOrder.getCustomerRef());
        assertThat(savedBeerOrder.getCustomer()).isEqualTo(testCustomer);
        assertThat(savedBeerOrder.getCustomer().getBeerOrders()).isNotEmpty();
        assertThat(savedBeerOrder.getBeerOrderShipment()).isNotNull();
        assertThat(savedBeerOrderShipment).isNotNull();
        assertThat(savedBeerOrderShipment.getBeerOrder()).isNotNull();
    }

}