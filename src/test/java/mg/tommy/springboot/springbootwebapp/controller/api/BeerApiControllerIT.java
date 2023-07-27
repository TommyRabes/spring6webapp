package mg.tommy.springboot.springbootwebapp.controller.api;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import mg.tommy.springboot.springbootwebapp.model.domain.embedded.Beer;
import mg.tommy.springboot.springbootwebapp.model.domain.embedded.BeerStyle;
import mg.tommy.springboot.springbootwebapp.model.dto.BeerDto;
import mg.tommy.springboot.springbootwebapp.mapper.BeerMapper;
import mg.tommy.springboot.springbootwebapp.repository.embedded.BeerRepository;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;


import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import static mg.tommy.springboot.springbootwebapp.controller.api.BeerApiController.ROOT_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BeerApiControllerIT {

    @Autowired
    BeerApiController beerApiController;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper beerMapper;

    /**
     * No need to specify a specific bean this time ?
     * Either Spring is smart enough to figure out which is the appropriate EntityManager (by spotting the @Transactional via reflection on the overlying test method)
     * or since it's a JPA annotation, all the magic are done by Hibernate which could make sense because Hibernate
     * should be able to figure out which DataSource he should utilize as per the type of the object that is passed to it
     */
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @Test
    @Order(2)
    public void getAllBeersTest() {
        Iterable<BeerDto> dtos = beerApiController.getAllBeers();

        assertThat(dtos).hasSize(2413);
    }

    /**
     * In most cases, jakarta's @Transactional and Spring's @Transactional annotations are interchangeable
     * But in our case, we have 2 different databases i.e 2 different DataSource thus, 2 different PlatformTransactionManager beans
     * So we must use the Spring annotation to specify the transaction manager we want to use for this test
     */
    @Transactional("embeddedTransactionManager")
    @Rollback
    @Test
    @Order(1)
    public void getEmptyBeerListTest() {
        beerRepository.deleteAll();
        Iterable<BeerDto> dtos = beerApiController.getAllBeers();

        assertThat(dtos).hasSize(0);
    }

    @Test
    @Order(3)
    public void getBeerByIdTest() {
        Beer beer = beerRepository.findAll().get(0);

        BeerDto beerDto = beerApiController.getBeerByUUID(beer.getId()).getBody();

        assertThat(beerDto).isNotNull();
    }

    @Test
    @Order(4)
    public void getBeerByIdNotFoundTest() {
        assertThrows(NotFoundException.class, () -> {
            beerApiController.getBeerByUUID(UUID.randomUUID());
        });
    }

    @Test
    public void getBeersByName() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        mockMvc.perform(get(ROOT_PATH)
                        .queryParam("beerName", "IPA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(100)));
    }

    @Test
    public void getBeersByStyle() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        mockMvc.perform(get(ROOT_PATH)
                        .queryParam("beerStyle", "LAGER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(200)));
    }

    @Transactional("embeddedTransactionManager")
    @Rollback
    @Test
    public void saveBeerTest() {
        BeerDto beerDto = BeerDto.builder()
                .beerName("New beer")
                .beerStyle(BeerStyle.SAISON.name())
                .upc("0535421")
                .price(new BigDecimal("25.99"))
                .build();

        ResponseEntity<BeerDto> responseEntity = beerApiController.saveBeer(beerDto);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID generatedUUID = UUID.fromString(locationUUID[4]);
        Optional<Beer> beerOptional = beerRepository.findById(generatedUUID);

        assertThat(beerOptional).isNotEmpty();
        Beer savedBeer = beerOptional.get();
        assertThat(savedBeer.getId()).isEqualTo(generatedUUID);
        assertThat(savedBeer.getVersion()).isNotNull();
        assertThat(savedBeer.getBeerName()).isEqualTo(beerDto.getBeerName());
        assertThat(savedBeer.getBeerStyle().name()).isEqualTo(beerDto.getBeerStyle());
        assertThat(savedBeer.getUpc()).isEqualTo(beerDto.getUpc());
        assertThat(savedBeer.getQuantityOnHand()).isEqualTo(beerDto.getQuantityOnHand());
        assertThat(savedBeer.getPrice()).isEqualTo(beerDto.getPrice());
        assertThat(savedBeer.getCreatedDate()).isNotNull();
        assertThat(savedBeer.getUpdateDate()).isNotNull();
    }

    /**
     * In order not to alter the application's real behavior, propagation is set to NOT_SUPPORTED
     * This way no transaction will be created at this method level
     * preventing from expanding the transaction scope
     * resulting in having a ConstraintViolationException nested within a TransactionSystemException
     * instead of getting directly the ConstraintViolationException bubbled up to the controller
     * here we want to keep the normal behavior of the webapp in case of an exception
     * Actually, the @Transactional and @Rollback may be pointless in this case
     * because flushing occurs earlier and thus, probably commit too (to probe)
     *
     * One caveat is what if no exception get thrown while running the test ? (because we're expecting one)
     * Undesired data will persist in the database without the possibility to rollback
     * It's okay for this test because we're connecting to an in-memory database
     * but in a production-ready application and database (MySQL), we wouldn't want that
     */
    @Transactional(transactionManager = "embeddedTransactionManager", propagation = Propagation.NOT_SUPPORTED)
    @Rollback
    @Test
    public void saveBeerWithoutRequiredFieldsTest() {
        BeerDto beerDto = BeerDto.builder().build();

        TransactionSystemException transactionSystemException = assertThrows(TransactionSystemException.class, () -> {
            beerApiController.saveBeer(beerDto);
        });

        assertThat(transactionSystemException.getCause().getCause()).isInstanceOf(ConstraintViolationException.class);
        assertThat((ConstraintViolationException) transactionSystemException.getCause().getCause()).matches(beerEntityConstraintViolation(2, 1, 2, 0, 1));
    }

    @Transactional(transactionManager = "embeddedTransactionManager", propagation = Propagation.NOT_SUPPORTED)
    @Rollback
    @Test
    public void saveBeerWithInvalidFieldsTest() {
        BeerDto beerDto = BeerDto.builder()
                .beerName("b")
                .beerStyle(BeerStyle.PORTER.name())
                .upc("up")
                .quantityOnHand(-1)
                .price(new BigDecimal("-1"))
                .build();

        TransactionSystemException transactionSystemException = assertThrows(TransactionSystemException.class, () -> {
            beerApiController.saveBeer(beerDto);
        });

        assertThat(transactionSystemException.getCause().getCause()).isInstanceOf(ConstraintViolationException.class);
        assertThat((ConstraintViolationException) transactionSystemException.getCause().getCause()).matches(beerEntityConstraintViolation(1, 0, 1, 1, 1));
    }

    @Transactional("embeddedTransactionManager")
    @Rollback
    @Test
    public void updateBeerByIdTest() {
        final String updatedName = "Updated name";
        Beer beer = beerRepository.findAll().get(0);
        /**
         * Must detach this JPA entity from the current Hibernate session
         * otherwise the underlying update operation will alter this instance as well
         * We want to freeze that instance in order to compare the previous entity state against its new state
         */
        entityManager.detach(beer);

        BeerDto beerDto = beerMapper.toBeerDto(beer).toBuilder()
                .id(null)
                .version(null)
                .beerName(updatedName)
                .createdDate(null)
                .updateDate(null)
                .build();

        ResponseEntity responseEntity = beerApiController.updateById(beer.getId(), beerDto);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Optional<Beer> beerOptional = beerRepository.findById(beer.getId());
        assertThat(beerOptional).isNotEmpty();
        Beer updatedBeer = beerOptional.get();
        assertThat(updatedBeer.getId()).isNotNull();
        assertThat(updatedBeer.getVersion()).isNotNull();
        assertThat(updatedBeer.getBeerName()).isEqualTo(beerDto.getBeerName());
        assertThat(updatedBeer.getBeerStyle().name()).isEqualTo(beerDto.getBeerStyle());
        assertThat(updatedBeer.getUpc()).isEqualTo(beerDto.getUpc());
        assertThat(updatedBeer.getPrice()).isEqualTo(beerDto.getPrice());
        assertThat(updatedBeer.getQuantityOnHand()).isEqualTo(beerDto.getQuantityOnHand());
        assertThat(updatedBeer.getCreatedDate()).isEqualTo(beer.getCreatedDate());
        assertThat(updatedBeer.getUpdateDate()).isNotNull();
        assertThat(updatedBeer.getUpdateDate()).isNotEqualTo(beer.getUpdateDate());
    }

    @Transactional(transactionManager = "embeddedTransactionManager", propagation = Propagation.NOT_SUPPORTED)
    @Rollback
    @Test
    public void updateBeerByIdWithoutRequiredFieldsTest() {
        final String updatedName = "Updated name";
        Beer beer = beerRepository.findAll().get(0);
        entityManager.detach(beer);

        BeerDto beerDto = BeerDto.builder().beerName(updatedName).build();

        TransactionSystemException transactionSystemException = assertThrows(TransactionSystemException.class, () -> {
            beerApiController.updateById(beer.getId(), beerDto);
        });

        assertThat(transactionSystemException.getCause().getCause()).isInstanceOf(ConstraintViolationException.class);
        assertThat((ConstraintViolationException) transactionSystemException.getCause().getCause()).matches(beerEntityConstraintViolation(0, 1, 2, 0, 1));
    }

    @Transactional(transactionManager = "embeddedTransactionManager", propagation = Propagation.NOT_SUPPORTED)
    @Rollback
    @Test
    public void updateBeerByIdWithInvalidFieldsTest() {
        Beer beer = beerRepository.findAll().get(0);
        entityManager.detach(beer);

        BeerDto beerDto = BeerDto.builder()
                .beerName("n")
                .beerStyle(BeerStyle.LAGER.name())
                .upc("0123456789".repeat(11))
                .quantityOnHand(-1)
                .price(new BigDecimal("-1"))
                .build();

        TransactionSystemException transactionSystemException = assertThrows(TransactionSystemException.class, () -> {
            beerApiController.updateById(beer.getId(), beerDto);
        });

        assertThat(transactionSystemException.getCause().getCause()).isInstanceOf(ConstraintViolationException.class);
        assertThat((ConstraintViolationException) transactionSystemException.getCause().getCause()).matches(beerEntityConstraintViolation(1, 0, 1, 1, 1));
    }

    @Transactional("embeddedTransactionManager")
    @Rollback
    @Test
    public void updateBeerByIdNotFoundTest() {
        assertThrows(NotFoundException.class, () -> {
            beerApiController.updateById(UUID.randomUUID(), mock(BeerDto.class));
        });
    }

    @Transactional("embeddedTransactionManager")
    @Rollback
    @Test
    public void deleteByIdTest() {
        Beer beer = beerRepository.findAll().get(0);
        ResponseEntity responseEntity = beerApiController.deleteById(beer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Optional<Beer> foundBeer = beerRepository.findById(beer.getId());
        assertThat(foundBeer).isEmpty();
    }

    @Transactional("embeddedTransactionManager")
    @Rollback
    @Test
    public void deleteByIdNotFoundTest() {
        assertThrows(NotFoundException.class, () -> {
            beerApiController.deleteById(UUID.randomUUID());
        });
    }

    @Transactional("embeddedTransactionManager")
    @Rollback
    @Test
    public void patchByIdTest() {
        Beer beerToPatch = beerRepository.findAll().get(0);
        entityManager.detach(beerToPatch);

        BeerDto beerForPatch = BeerDto.builder()
                .beerName("New beer name")
                .build();

        ResponseEntity responseEntity = beerApiController.patchById(beerToPatch.getId(), beerForPatch);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Beer patchedBeer = beerRepository.findById(beerToPatch.getId()).get();

        assertThat(patchedBeer.getId()).isEqualTo(beerToPatch.getId());
        assertThat(patchedBeer.getVersion()).isEqualTo(beerToPatch.getVersion());
        assertThat(patchedBeer.getBeerName()).isEqualTo(beerForPatch.getBeerName());
        assertThat(patchedBeer.getBeerStyle()).isEqualTo(beerToPatch.getBeerStyle());
        assertThat(patchedBeer.getUpc()).isEqualTo(beerToPatch.getUpc());
        assertThat(patchedBeer.getPrice()).isEqualTo(beerToPatch.getPrice());
        assertThat(patchedBeer.getQuantityOnHand()).isEqualTo(beerToPatch.getQuantityOnHand());
        assertThat(patchedBeer.getCreatedDate()).isEqualTo(beerToPatch.getCreatedDate());
        assertThat(patchedBeer.getUpdateDate()).isNotEqualTo(beerToPatch.getUpdateDate());
    }

    @Transactional(transactionManager = "embeddedTransactionManager", propagation = Propagation.NOT_SUPPORTED)
    @Rollback
    @Test
    public void patchByIdWithInvalidFieldsTest() {
        Beer beerToPatch = beerRepository.findAll().get(0);
        entityManager.detach(beerToPatch);

        BeerDto beerForPatch = BeerDto.builder()
                .beerName("n")
                .quantityOnHand(-1)
                .build();

        TransactionSystemException transactionSystemException = assertThrows(TransactionSystemException.class, () -> {
            beerApiController.patchById(beerToPatch.getId(), beerForPatch);
        });

        assertThat(transactionSystemException.getCause().getCause()).isInstanceOf(ConstraintViolationException.class);
        assertThat((ConstraintViolationException) transactionSystemException.getCause().getCause()).matches(beerEntityConstraintViolation(1, 0, 0, 1, 0));
    }

    @Transactional("embeddedTransactionManager")
    @Rollback
    @Test
    public void patchByIdNotFoundTest() {
        assertThrows(NotFoundException.class, () -> {
            beerApiController.patchById(UUID.randomUUID(), mock(BeerDto.class));
        });
    }

    private Predicate<ConstraintViolationException> beerEntityConstraintViolation(int beerName, int beerStyle, int upc, int quantityOnHand, int price) {
        return (exception) -> {
            Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
            return violations.size() == beerName + beerStyle + upc + quantityOnHand + price &&
                    violations.stream().filter(violation -> "beerName".equals(violation.getPropertyPath().toString())).count() == beerName &&
                    violations.stream().filter(violation -> "beerStyle".equals(violation.getPropertyPath().toString())).count() == beerStyle &&
                    violations.stream().filter(violation -> "upc".equals(violation.getPropertyPath().toString())).count() == upc &&
                    violations.stream().filter(violation -> "quantityOnHand".equals(violation.getPropertyPath().toString())).count() == quantityOnHand &&
                    violations.stream().filter(violation -> "price".equals(violation.getPropertyPath().toString())).count() == price;
        };
    }
}