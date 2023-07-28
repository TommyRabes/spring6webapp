package mg.tommy.springboot.springbootwebapp.controller.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mg.tommy.springboot.springbootwebapp.model.domain.embedded.BeerStyle;
import mg.tommy.springboot.springbootwebapp.model.dto.BeerDto;
import mg.tommy.springboot.springbootwebapp.service.brewing.BeerService;
import net.minidev.json.JSONObject;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.empty;
import static mg.tommy.springboot.springbootwebapp.controller.api.BeerApiController.ROOT_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;
import static org.hamcrest.core.StringContains.containsStringIgnoringCase;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
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
            .beerStyle(BeerStyle.PALE_ALE.name())
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
            .beerStyle(BeerStyle.IPA.name())
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
            .beerStyle(BeerStyle.ALE.name())
            .upc("0378257")
            .price(new BigDecimal("18.99"))
            .quantityOnHand(300)
            .createdDate(LocalDateTime.now())
            .updateDate(LocalDateTime.now())
            .build();

    private static final String UUID_PATH = ROOT_PATH + "/{uuid}";

    public static final JSONObject STR_INVALID_BEER_MAP = new JSONObject(5)
            .appendField("beerName", "be")
            .appendField("beerStyle", "NONE")
            .appendField("upc", "up")
            .appendField("quantityOnHand", 5.5)
            .appendField("price", 15.99);

    public static final JSONObject VALUES_OUT_OF_BOUND_BEER_MAP = new JSONObject(5)
            .appendField("beerName", "too many characters".repeat(20))
            .appendField("beerStyle", BeerStyle.LAGER.name())
            .appendField("upc", "very long upc value".repeat(20))
            .appendField("quantityOnHand", -1)
            .appendField("price", "-1");

    public static final JSONObject TYPE_MISMATCH_BEER_MAP = new JSONObject(5)
            .appendField("beerName", "be")
            .appendField("beerStyle", "NONE")
            .appendField("upc", "up")
            .appendField("quantityOnHand", "string value")
            .appendField("price", 15.99);

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
    public void findAllBeerTest() throws Exception {
        given(beerService.find(null, null, false, null, null)).willReturn(new PageImpl<>(Lists.list(GALAXY, SUNSHINE)));

        mockMvc.perform(get(ROOT_PATH).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()", is(2)));
    }

    @Test
    public void getBeerByIdTest() throws Exception {
        given(beerService.findById(GALAXY.getId())).willReturn(Optional.of(GALAXY));

        mockMvc.perform(get(UUID_PATH, GALAXY.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(GALAXY.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(GALAXY.getBeerName())))
                .andExpect(jsonPath("$.beerStyle", is(GALAXY.getBeerStyle())))
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

        mockMvc.perform(saveBeerRequest(beerDto))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "/api/v1/beers/" + MANGO.getId()));
    }

    @Test
    public void saveBeerWithoutRequiredFieldsTest() throws Exception {
        BeerDto beerDto = BeerDto.builder().build();

        given(beerService.save(any(BeerDto.class))).willReturn(SUNSHINE);

        mockMvc.perform(saveBeerRequest(beerDto))
                .andExpect(status().isBadRequest())
                .andExpectAll(fieldsErrors(2, 1, 2, 0, 1));

        verifyNoInteractions(beerService);
    }

    @Test
    public void saveBeerWithInvalidFieldsTest() throws Exception {
        given(beerService.save(any(BeerDto.class))).willReturn(SUNSHINE);

        mockMvc.perform(saveBeerRequest(STR_INVALID_BEER_MAP))
                .andExpect(status().isBadRequest())
                .andExpectAll(fieldsErrors(1, 1, 1, 0, 0));

        mockMvc.perform(saveBeerRequest(VALUES_OUT_OF_BOUND_BEER_MAP))
                .andExpect(status().isBadRequest())
                .andExpectAll(fieldsErrors(1, 0, 1, 1, 1));

        verifyNoInteractions(beerService);
    }

    /**
     * <a href="https://github.com/unbroken-dome/jackson-bean-validation">Check out Jackson bean validation to perform bean validation upon deserialization</a>
     * @throws Exception
     */
    @Test
    public void saveBeerWithTypeMismatchTest() throws Exception {
        given(beerService.save(any(BeerDto.class))).willReturn(SUNSHINE);

        mockMvc.perform(saveBeerRequest(TYPE_MISMATCH_BEER_MAP))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("TypeMismatching")))
                .andExpect(jsonPath("$.message", containsStringIgnoringCase("conversion failed")));
    }

    @Test
    public void updateByIdTest() throws Exception {
        BeerDto beerDto = MANGO.toBuilder()
                .id(null)
                .version(null)
                .beerName("Mango Bobs - TRB")
                .beerStyle(BeerStyle.IPA.name())
                .upc("485125")
                .quantityOnHand(160)
                .price(new BigDecimal("19.99"))
                .build();

        given(beerService.overwriteById(eq(MANGO.getId()), eq(beerDto))).willReturn(Optional.of(mock(BeerDto.class)));

        mockMvc.perform(updateBeerRequest(MANGO.getId(), beerDto))
                .andExpect(status().isNoContent());

        verify(beerService).overwriteById(any(UUID.class), any(BeerDto.class));
    }

    @Test
    public void updateByIdWithoutRequiredFieldsTest() throws Exception {
        given(beerService.overwriteById(any(UUID.class), any(BeerDto.class))).willReturn(Optional.of(mock(BeerDto.class)));

        BeerDto beerDto = BeerDto.builder().build();

        mockMvc.perform(updateBeerRequest(MANGO.getId(), beerDto))
                .andExpect(status().isBadRequest())
                .andExpectAll(fieldsErrors(2, 1, 2, 0, 1));

        verifyNoInteractions(beerService);
    }

    @Test
    public void updateByIdWithInvalidFieldsTest() throws Exception {
        given(beerService.overwriteById(any(UUID.class), any(BeerDto.class))).willReturn(Optional.of(mock(BeerDto.class)));

        mockMvc.perform(updateBeerRequest(MANGO.getId(), STR_INVALID_BEER_MAP))
                .andExpect(status().isBadRequest())
                .andExpectAll(fieldsErrors(1, 1, 1, 0, 0));

        mockMvc.perform(updateBeerRequest(MANGO.getId(), VALUES_OUT_OF_BOUND_BEER_MAP))
                .andExpect(status().isBadRequest())
                .andExpectAll(fieldsErrors(1, 0, 1, 1, 1));

        verifyNoInteractions(beerService);
    }

    @Test
    public void updateByIdWithTypeMismatchTest() throws Exception {
        given(beerService.overwriteById(any(UUID.class), any(BeerDto.class))).willReturn(Optional.of(mock(BeerDto.class)));

        mockMvc.perform(updateBeerRequest(MANGO.getId(), TYPE_MISMATCH_BEER_MAP))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("TypeMismatching")))
                .andExpect(jsonPath("$.message", containsStringIgnoringCase("conversion failed")));

        verifyNoInteractions(beerService);
    }

    @Test
    public void updateByIdNotFoundTest() throws Exception {
        BeerDto beerDto = MANGO.toBuilder()
                .id(null)
                .version(null)
                .beerName("Mango Bobs - TRB")
                .beerStyle(BeerStyle.IPA.name())
                .upc("485125")
                .quantityOnHand(160)
                .price(new BigDecimal("19.99"))
                .build();

        mockMvc.perform(updateBeerRequest(UUID.randomUUID(), beerDto))
                .andExpect(status().isNotFound());

        verify(beerService).overwriteById(any(UUID.class), any(BeerDto.class));
    }

    @Test
    public void deleteByIdTest() throws Exception {
        given(beerService.deleteById(any(UUID.class))).willReturn(true);

        mockMvc.perform(delete(UUID_PATH, MANGO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(beerService).deleteById(uuidArgumentCaptor.capture());

        assertThat(MANGO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    public void deleteByIdNotFoundTest() throws Exception {
        given(beerService.deleteById(any(UUID.class))).willReturn(false);

        mockMvc.perform(delete(UUID_PATH, MANGO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                /*
                 * In hindsight, a real unit test should just test whether the method throw an exception
                 * Here, the NOT_FOUND status is returned by a @ControllerAdvice annotated class (not really 'unity')
                 */
                .andExpect(status().isNotFound());

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(beerService).deleteById(uuidArgumentCaptor.capture());

        assertThat(MANGO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    public void patchByIdTest() throws Exception {
        /**
         * The body doesn't have to be an instance of Beer, as long as the resulting JSON meets our expectation
         */
        Map<String, String> beerMap = Map.of("beerName", "Mango Bobs - TRB");

        given(beerService.updateById(any(UUID.class), any(BeerDto.class))).willReturn(Optional.of(mock(BeerDto.class)));

        mockMvc.perform(patchBeerRequest(MANGO.getId(), beerMap))
                .andExpect(status().isNoContent());

        verify(beerService).updateById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(MANGO.getId());
        assertThat(beerArgumentCaptor.getValue().getBeerName()).isEqualTo(beerMap.get("beerName"));
    }
    @Test
    public void patchByIdWithInvalidFieldsTest() throws Exception {
        given(beerService.updateById(any(UUID.class), any(BeerDto.class))).willReturn(Optional.of(mock(BeerDto.class)));

        mockMvc.perform(patchBeerRequest(MANGO.getId(), STR_INVALID_BEER_MAP))
                .andExpect(status().isBadRequest())
                .andExpectAll(fieldsErrors(1, 1, 1, 0, 0));

        mockMvc.perform(patchBeerRequest(MANGO.getId(), VALUES_OUT_OF_BOUND_BEER_MAP))
                .andExpect(status().isBadRequest())
                .andExpectAll(fieldsErrors(1, 0, 1, 1, 1));

        verifyNoInteractions(beerService);
    }

    @Test
    public void patchByIdNotFoundTest() throws Exception {
        Map<String, String> beerMap = new HashMap<>();
        beerMap.put("beerName", "Mango Bobs - TRB");

        given(beerService.updateById(any(UUID.class), any(BeerDto.class))).willReturn(Optional.empty());

        mockMvc.perform(patchBeerRequest(MANGO.getId(), beerMap))
                .andExpect(status().isNotFound());

        verify(beerService).updateById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(MANGO.getId());
        assertThat(beerArgumentCaptor.getValue().getBeerName()).isEqualTo(beerMap.get("beerName"));
    }

    private RequestBuilder saveBeerRequest(Object requestBody) throws JsonProcessingException {
        return post(ROOT_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody));
    }

    private MockHttpServletRequestBuilder updateBeerRequest(UUID uuid, Object requestBody) throws JsonProcessingException {
        return put(UUID_PATH, uuid)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody));
    }

    private MockHttpServletRequestBuilder patchBeerRequest(UUID uuid, Object requestBody) throws JsonProcessingException {
        return patch(UUID_PATH, uuid)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody));
    }

    private ResultMatcher[] fieldsErrors(int beerName, int beerStyle, int upc, int quantityOnHand, int price) {
        return new ResultMatcher[] {
                jsonPath("$.length()", is(beerName + beerStyle + upc + quantityOnHand + price)),
                jsonPath("$..beerName", iterableWithSize(beerName)),
                jsonPath("$..beerStyle", iterableWithSize(beerStyle)),
                jsonPath("$..upc", iterableWithSize(upc)),
                jsonPath("$..quantityOnHand", iterableWithSize(quantityOnHand)),
                jsonPath("$..price", iterableWithSize(price))
            };
    }

}