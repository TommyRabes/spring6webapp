package mg.tommy.springboot.springbootwebapp.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import mg.tommy.springboot.springbootwebapp.domain.embedded.Beer;
import mg.tommy.springboot.springbootwebapp.domain.embedded.BeerStyle;
import mg.tommy.springboot.springbootwebapp.dto.BeerDto;
import mg.tommy.springboot.springbootwebapp.service.brewing.BeerService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.empty;
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
/**
 * For some reason, this annotation is necessary despite specifying the controller being tested in @WebMvcTest
 * Moreover, @ControllerAdvice-annotated classes don't get picked up in a unit test context
 * as that error handling technique is based on Servlet container error mapping
 * while Spring MockMvc is container-less
 * Nonetheless it seems to work fine when we explicitly add the class through @Import annotation
 * Does it mean MockMvc is somehow able to mimic Spring Boot's behaviour (at least the error handling part) ?
 */
@Import({ BeerApiController.class, ControllerExceptionHandler.class })
class BeerApiControllerTest {
    private static final BeerDto GALAXY = BeerDto.builder()
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
    private static final BeerDto SUNSHINE = BeerDto.builder()
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

    private static final BeerDto MANGO = BeerDto.builder()
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
    ArgumentCaptor<BeerDto> beerArgumentCaptor;

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
    public void getBeerByIdNotFoundTest() throws Exception {
        given(beerService.findById(any(UUID.class))).willReturn(empty());

        mockMvc.perform(get(UUID_PATH, UUID.randomUUID()))
                .andExpect(status().isNotFound());
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

        BeerDto beerDto = MANGO.toBuilder()
                .id(null)
                .version(null)
                .createdDate(null)
                .updateDate(null)
                .build();

        given(beerService.save(any(BeerDto.class))).willReturn(MANGO);

        mockMvc.perform(post(ROOT_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "/api/v1/beers/" + MANGO.getId()));
    }

    @Test
    public void updateByIdTest() throws Exception {
        BeerDto beerDto = MANGO.toBuilder()
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
                        .content(objectMapper.writeValueAsString(beerDto)))
                .andExpect(status().isNoContent());

        verify(beerService).overwriteById(any(UUID.class), any(BeerDto.class));
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

        given(beerService.updateById(any(UUID.class), any(BeerDto.class))).willReturn(mock(BeerDto.class));

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