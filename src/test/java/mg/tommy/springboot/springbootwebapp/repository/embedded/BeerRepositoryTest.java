package mg.tommy.springboot.springbootwebapp.repository.embedded;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import mg.tommy.springboot.springbootwebapp.bootstrap.CommandLineInitializer;
import mg.tommy.springboot.springbootwebapp.configuration.database.EmbeddedDatabaseConfig;
import mg.tommy.springboot.springbootwebapp.mapper.BeerMapperImpl;
import mg.tommy.springboot.springbootwebapp.mapper.BeerPropertyMapping;
import mg.tommy.springboot.springbootwebapp.model.domain.embedded.Beer;
import mg.tommy.springboot.springbootwebapp.model.domain.embedded.BeerStyle;
import mg.tommy.springboot.springbootwebapp.service.brewing.BeerCsvServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional("embeddedTransactionManager")
@Rollback
@DataJpaTest
@Import({
        EmbeddedDatabaseConfig.class,
        CommandLineInitializer.class,
        BeerCsvServiceImpl.class,
        BeerMapperImpl.class,
        BeerPropertyMapping.class
})
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    public void findAllByBeerNameTest() {
        List<Beer> beers = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%", null).getContent();

        assertThat(beers).hasSize(336);
        for (Beer beer : beers) {
            assertThat(beer.getBeerName()).containsIgnoringCase("IPA");
        }
    }

    @Test
    public void persistBeerTest() {
        Beer savedBeer = beerRepository.saveAndFlush(Beer.builder()
                .beerName("My Beer")
                        .beerStyle(BeerStyle.PILSNER)
                        .upc("0658059479")
                        .quantityOnHand(50)
                        .price(new BigDecimal("21.99"))
                .build());

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }

    @Test
    public void persistBeerWithoutRequiredColumnTest() {
        ConstraintViolationException thrownException = assertThrows(ConstraintViolationException.class, () -> {
            beerRepository.saveAndFlush(Beer.builder().build());
        });

        assertThat(thrownException).matches(beerEntityConstraintViolation(2, 1, 2, 0, 1));
    }

    @Test
    public void persistBeerWithInvalidColumnTest() {
        ConstraintViolationException thrownException = assertThrows(ConstraintViolationException.class, () -> {
            beerRepository.saveAndFlush(Beer.builder()
                    .beerName("b")
                    .beerStyle(BeerStyle.PORTER)
                    .upc("up")
                    .quantityOnHand(-1)
                    .price(new BigDecimal("-1"))
                    .build());
        });

        assertThat(thrownException).matches(beerEntityConstraintViolation(1, 0, 1, 1, 1));
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