package mg.tommy.springboot.springbootwebapp.controller.api;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import mg.tommy.springboot.springbootwebapp.domain.embedded.Beer;
import mg.tommy.springboot.springbootwebapp.dto.BeerDto;
import mg.tommy.springboot.springbootwebapp.mapper.BeerMapper;
import mg.tommy.springboot.springbootwebapp.repository.embedded.BeerRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

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

    @Test
    @Order(2)
    public void getAllBeersTest() {
        Iterable<BeerDto> dtos = beerApiController.getAllBeers();

        assertThat(dtos).hasSize(3);
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

    @Transactional("embeddedTransactionManager")
    @Rollback
    @Test
    public void saveBeerTest() {
        BeerDto beerDto = BeerDto.builder()
                .beerName("New beer")
                .price(new BigDecimal("25.99"))
                .build();

        ResponseEntity<BeerDto> responseEntity = beerApiController.saveBeer(beerDto);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        String generatedUUID = locationUUID[4];
        Optional<Beer> beerOptional = beerRepository.findById(UUID.fromString(generatedUUID));

        assertThat(beerOptional).isNotEmpty();
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

    @Transactional("embeddedTransactionManager")
    @Rollback
    @Test
    public void patchByIdNotFoundTest() {
        assertThrows(NotFoundException.class, () -> {
            beerApiController.patchById(UUID.randomUUID(), mock(BeerDto.class));
        });
    }
}