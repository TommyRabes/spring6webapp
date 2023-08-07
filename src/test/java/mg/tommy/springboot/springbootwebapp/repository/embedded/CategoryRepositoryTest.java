package mg.tommy.springboot.springbootwebapp.repository.embedded;

import mg.tommy.springboot.springbootwebapp.bootstrap.CommandLineInitializer;
import mg.tommy.springboot.springbootwebapp.configuration.database.EmbeddedDatabaseConfig;
import mg.tommy.springboot.springbootwebapp.mapper.BeerMapperImpl;
import mg.tommy.springboot.springbootwebapp.mapper.BeerPropertyMapping;
import mg.tommy.springboot.springbootwebapp.model.domain.embedded.Beer;
import mg.tommy.springboot.springbootwebapp.model.domain.embedded.Category;
import mg.tommy.springboot.springbootwebapp.service.brewing.BeerCsvServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({
        EmbeddedDatabaseConfig.class,
        CommandLineInitializer.class,
        BeerCsvServiceImpl.class,
        BeerMapperImpl.class,
        BeerPropertyMapping.class
})
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    BeerRepository beerRepository;

    Beer testBeer;

    @BeforeEach
    void setUp() {
        testBeer = beerRepository.findAll().get(0);
    }

    @Test
    public void saveCategoryTest() {
        Category savedCategory = categoryRepository.saveAndFlush(Category.builder()
                .description("Ales")
                .beers(Set.of(testBeer))
                .build());

        // testBeer.addCategory(savedCategory);
        Beer savedBeer = beerRepository.findById(testBeer.getId()).get();

        assertThat(savedBeer.getCategories()).hasSize(1);
        assertThat(savedCategory.getBeers()).hasSize(1);
    }
}