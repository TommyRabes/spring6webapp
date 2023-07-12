package mg.tommy.springboot.springbootwebapp.controller.api;

import mg.tommy.springboot.springbootwebapp.domain.embedded.Beer;
import mg.tommy.springboot.springbootwebapp.domain.embedded.BeerStyle;
import mg.tommy.springboot.springbootwebapp.service.brewing.BeerService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = { BeerApiController.class },
        excludeAutoConfiguration = { SecurityAutoConfiguration.class }
)
// For some reason, this annotation is necessary despite specifying the controller being tested in @WebMvcTest
@Import(BeerApiController.class)
class BeerApiControllerTest {
    private static final Beer GALAXY = Beer.builder()
            .beerName("Galaxy Cat")
            .beerStyle(BeerStyle.PALE_ALE)
            .upc("808643")
            .price(new BigDecimal("20.50"))
            .quantityOnHand(140)
            .build();
    private static final Beer SUNSHINE = Beer.builder()
            .beerName("Sunshine City")
            .beerStyle(BeerStyle.IPA)
            .upc("03270857")
            .price(new BigDecimal("16.99"))
            .quantityOnHand(250)
            .build();

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BeerService beerService;

    @Test
    public void getAllBeerTest() throws Exception {
        given(beerService.findAll()).willReturn(Lists.list(GALAXY, SUNSHINE));

        mockMvc.perform(get("/api/v1/beers").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}