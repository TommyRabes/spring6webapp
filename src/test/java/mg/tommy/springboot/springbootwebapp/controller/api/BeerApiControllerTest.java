package mg.tommy.springboot.springbootwebapp.controller.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mg.tommy.springboot.springbootwebapp.domain.embedded.Beer;
import mg.tommy.springboot.springbootwebapp.domain.embedded.BeerStyle;
import mg.tommy.springboot.springbootwebapp.service.brewing.BeerService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mockito;
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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static mg.tommy.springboot.springbootwebapp.controller.api.BeerApiController.ROOT_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.core.Is.is;

@WebMvcTest(
        controllers = { BeerApiController.class },
        excludeAutoConfiguration = { SecurityAutoConfiguration.class }
)
// For some reason, this annotation is necessary despite specifying the controller being tested in @WebMvcTest
@Import(BeerApiController.class)
class BeerApiControllerTest {
    private static final Beer GALAXY = Beer.builder()
            .id(UUID.randomUUID())
            .version(1)
            .beerName("Galaxy Cat")
            .beerStyle(BeerStyle.PALE_ALE)
            .upc("808643")
            .price(new BigDecimal("20.50"))
            .quantityOnHand(140)
            .createdDate(LocalDateTime.now())
            .updateDate(LocalDateTime.now())
            .build();
    private static final Beer SUNSHINE = Beer.builder()
            .id(UUID.randomUUID())
            .version(1)
            .beerName("Sunshine City")
            .beerStyle(BeerStyle.IPA)
            .upc("03270857")
            .price(new BigDecimal("16.99"))
            .quantityOnHand(250)
            .createdDate(LocalDateTime.now())
            .updateDate(LocalDateTime.now())
            .build();

    private static final Beer MANGO = Beer.builder()
            .id(UUID.randomUUID())
            .version(1)
            .beerName("Mango Bobs")
            .beerStyle(BeerStyle.ALE)
            .upc("0378257")
            .price(new BigDecimal("18.99"))
            .quantityOnHand(300)
            .createdDate(LocalDateTime.now())
            .updateDate(LocalDateTime.now())
            .build();

    private static final String UUID_PATH = ROOT_PATH + "/{uuid}";

    @Autowired
    MockMvc mockMvc;

    /**
     * Spring ships its own Jackson ObjetMapper bean
     * with some sensible defaults set beforehand
     */
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BeerService beerService;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<Beer> beerArgumentCaptor;

    @Test
    public void getAllBeerTest() throws Exception {
        given(beerService.findAll()).willReturn(Lists.list(GALAXY, SUNSHINE));

        mockMvc.perform(get(ROOT_PATH).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    public void getBeerByIdTest() throws Exception {
        given(beerService.findById(GALAXY.getId())).willReturn(Optional.of(GALAXY));

        mockMvc.perform(get(UUID_PATH, GALAXY.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(GALAXY.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(GALAXY.getBeerName())))
                .andExpect(jsonPath("$.beerStyle", is(GALAXY.getBeerStyle().name())))
                .andExpect(jsonPath("$.price", is(GALAXY.getPrice().doubleValue())));
    }

    @Test
    public void saveBeerTest() throws Exception {
        // ObjectMapper objectMapper = new ObjectMapper();
        /**
         * Scan classpath to add "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" module
         * in order to support Java 8 date/time APIs (still getting a weird datetime format)
         * Prefer auto wiring Spring's ObjectMapper bean which is already well-configured
         */
        // objectMapper.findAndRegisterModules();

        Beer beer = MANGO.toBuilder()
                .id(null)
                .version(1)
                .createdDate(null)
                .updateDate(null)
                .build();

        given(beerService.save(any(Beer.class))).willReturn(MANGO);

        mockMvc.perform(post(ROOT_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "/api/v1/beers/" + MANGO.getId()));
    }

    @Test
    public void updateByIdTest() throws Exception {
        Beer beer = MANGO.toBuilder()
                .id(null)
                .version(null)
                .beerName("Mango Bobs - TRB")
                .beerStyle(BeerStyle.IPA)
                .upc("485125")
                .quantityOnHand(160)
                .price(new BigDecimal("19.99"))
                .build();

        mockMvc.perform(put(UUID_PATH, MANGO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isNoContent());

        verify(beerService).overwriteById(any(UUID.class), any(Beer.class));
    }

    @Test
    public void deleteByIdTest() throws Exception {
        mockMvc.perform(delete(UUID_PATH, MANGO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(beerService).deleteById(uuidArgumentCaptor.capture());

        assertThat(MANGO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    public void patchByIdTest() throws Exception {
        /**
         * The body doesn't have to be an instance of Beer, as long as the resulting JSON meets our expectation
         */
        Map<String, String> beerMap = new HashMap<>();
        beerMap.put("beerName", "Mango Bobs - TRB");

        given(beerService.updateById(any(UUID.class), any(Beer.class))).willReturn(mock(Beer.class));

        mockMvc.perform(patch(UUID_PATH, MANGO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isNoContent());

        verify(beerService).updateById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(MANGO.getId());
        assertThat(beerArgumentCaptor.getValue().getBeerName()).isEqualTo(beerMap.get("beerName"));
    }

}