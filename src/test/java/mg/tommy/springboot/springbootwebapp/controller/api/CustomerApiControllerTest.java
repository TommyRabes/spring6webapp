package mg.tommy.springboot.springbootwebapp.controller.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mg.tommy.springboot.springbootwebapp.model.dto.CustomerDto;
import mg.tommy.springboot.springbootwebapp.service.library.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.empty;
import static mg.tommy.springboot.springbootwebapp.controller.api.CustomerApiController.ROOT_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = CustomerApiController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class
        }
)
@Import({ CustomerApiController.class, ControllerExceptionHandler.class })
class CustomerApiControllerTest {
    private static final CustomerDto BAILEY = CustomerDto.builder()
            .id(UUID.randomUUID())
            .version(1)
            .firstName("Bailey")
            .lastName("Summer")
            .email("bsum@gmail.com")
            .birthdate(LocalDate.of(2001, 12, 29))
            .build();

    private static final CustomerDto BOOKER = CustomerDto.builder()
            .id(UUID.randomUUID())
            .version(1)
            .firstName("Booker")
            .lastName("Reynold")
            .email("reybook@gmail.com")
            .birthdate(LocalDate.of(1997, 4, 13))
            .build();

    private static final CustomerDto SHERLEY = CustomerDto.builder()
            .id(UUID.randomUUID())
            .version(1)
            .firstName("Sherley")
            .lastName("Sirius")
            .email("sirisherley@gmail.com")
            .birthdate(LocalDate.of(1994, 8, 8))
            .build();

    private static final String UUID_PATH = ROOT_PATH + "/{uuid}";

    public static final CustomerDto INVALID_CUSTOMER = CustomerDto.builder()
            .firstName("to")
            .lastName("too large name".repeat(20))
            .email("notanemailvalue")
            .birthdate(LocalDate.now().minusYears(18).plus(1, ChronoUnit.DAYS))
            .build();

    public static final Map<String, String> UNDESERIALIZABLE_CUSTOMER = Map.of(
            "firstName", "to",
            "lastName", "too large name".repeat(20),
            "email", "notanemailvalue",
            "birthdate", "notaparseabledate"
    );

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CustomerService customerService;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<CustomerDto> customerArgumentCaptor;

    @BeforeEach
    public void arrange() {
        given(customerService.findAll()).willReturn(list(BAILEY, BOOKER));
        given(customerService.findById(BAILEY.getId())).willReturn(Optional.of(BAILEY));
    }

    @Test
    void listAllCustomersTest() throws Exception {
        final String baileyFilter = "$[?(@.id == '" + BAILEY.getId() + "')]";
        final String bookerFilter = "$[?(@.id == '" + BOOKER.getId() + "')]";

        mockMvc.perform(MockMvcRequestBuilders.get(ROOT_PATH).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)))

                // Stunned by this assertion ?
                // We actually can't use such syntax like '$[?(<expr>)][0].firstName' to pick the first item from the filtered array
                // as it's not a feature specified in the JsonPath specification
                // See: https://github.com/json-path/JsonPath/issues/272
                .andExpect(jsonPath(baileyFilter + ".firstName", contains(BAILEY.getFirstName())))
                .andExpect(jsonPath(baileyFilter + ".lastName", contains(BAILEY.getLastName())))
                .andExpect(jsonPath(baileyFilter + ".email", contains(BAILEY.getEmail())))
                .andExpect(jsonPath(baileyFilter + ".birthdate", contains(BAILEY.getBirthdate().toString())))

                .andExpect(jsonPath(bookerFilter + ".firstName", contains(BOOKER.getFirstName())))
                .andExpect(jsonPath(bookerFilter + ".lastName", contains(BOOKER.getLastName())))
                .andExpect(jsonPath(bookerFilter + ".email", contains(BOOKER.getEmail())))
                .andExpect(jsonPath(bookerFilter + ".birthdate", contains(BOOKER.getBirthdate().toString())));
    }

    @Test
    void getCustomerByUUIDTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(UUID_PATH, BAILEY.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(BAILEY.getId().toString())))
                .andExpect(jsonPath("$.firstName", is(BAILEY.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(BAILEY.getLastName())))
                .andExpect(jsonPath("$.email", is(BAILEY.getEmail())))
                .andExpect(jsonPath("$.birthdate", is(BAILEY.getBirthdate().toString())));
    }

    @Test
    public void getCustomerUUIDNotFoundTest() throws Exception {
        given(customerService.findById(any(UUID.class))).willReturn(empty());

        mockMvc.perform(get(UUID_PATH, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void saveCustomerTest() throws Exception {
        CustomerDto customerDto = SHERLEY.toBuilder()
                .id(null)
                .version(null)
                .build();

        given(customerService.save(any(CustomerDto.class))).willReturn(SHERLEY);

        mockMvc.perform(saveCustomerRequest(customerDto))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", ROOT_PATH + "/" + SHERLEY.getId()))
                .andExpect(jsonPath("$.id", is(SHERLEY.getId().toString())))
                .andExpect(jsonPath("$.firstName", is(SHERLEY.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(SHERLEY.getLastName())))
                .andExpect(jsonPath("$.email", is(SHERLEY.getEmail())))
                .andExpect(jsonPath("$.birthdate", is(SHERLEY.getBirthdate().toString())));
    }

    @Test
    public void saveCustomerWithoutRequiredFieldsTest() throws Exception {
        CustomerDto customerDto = CustomerDto.builder().build();

        given(customerService.save(any(CustomerDto.class))).willReturn(SHERLEY);

        mockMvc.perform(saveCustomerRequest(customerDto))
                .andExpect(status().isBadRequest())
                .andExpectAll(fieldErrors(2, 2, 2, 1));

        verifyNoInteractions(customerService);
    }

    @Test
    public void saveCustomerWithInvalidFieldsTest() throws Exception {
        given(customerService.save(any(CustomerDto.class))).willReturn(SHERLEY);

        mockMvc.perform(saveCustomerRequest(INVALID_CUSTOMER))
                .andExpect(status().isBadRequest())
                .andExpectAll(fieldErrors(1, 1, 1, 1));

        mockMvc.perform(saveCustomerRequest(INVALID_CUSTOMER.toBuilder().birthdate(LocalDate.now().minusYears(18)).build()))
                .andExpect(status().isBadRequest())
                .andExpectAll(fieldErrors(1, 1, 1, 0));

        verifyNoInteractions(customerService);
    }

    @Test
    public void saveCustomerWithTypeMismatchTest() throws Exception {
        given(customerService.save(any(CustomerDto.class))).willReturn(SHERLEY);

        mockMvc.perform(saveCustomerRequest(UNDESERIALIZABLE_CUSTOMER))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("TypeMismatching")))
                .andExpect(jsonPath("$.message", containsString("conversion failed")));

        verifyNoInteractions(customerService);
    }

    @Test
    public void updateCustomerTest() throws Exception {
        CustomerDto customerDto = SHERLEY.toBuilder()
                .id(null)
                .firstName("Shayan")
                .lastName("Lauren")
                .email("shayanlauren@gmail.com")
                .createdDate(null)
                .updateDate(null)
                .build();

        given(customerService.overwriteById(eq(SHERLEY.getId()), eq(customerDto))).willReturn(Optional.of(mock(CustomerDto.class)));

        mockMvc.perform(updateCustomerRequest(SHERLEY.getId(), customerDto))
                .andExpect(status().isNoContent());

        verify(customerService).overwriteById(eq(SHERLEY.getId()), eq(customerDto));
    }

    @Test
    public void updateCustomerWithoutRequiredFieldsTest() throws Exception {
        given(customerService.overwriteById(any(UUID.class), any(CustomerDto.class))).willReturn(Optional.of(mock(CustomerDto.class)));

        CustomerDto customerDto = CustomerDto.builder().build();

        mockMvc.perform(updateCustomerRequest(SHERLEY.getId(), customerDto))
                .andExpect(status().isBadRequest())
                .andExpectAll(fieldErrors(2, 2, 2, 1));

        verifyNoInteractions(customerService);
    }

    @Test
    public void updateCustomerWithInvalidFieldsTest() throws Exception {
        given(customerService.overwriteById(any(UUID.class), any(CustomerDto.class))).willReturn(Optional.of(mock(CustomerDto.class)));

        mockMvc.perform(updateCustomerRequest(SHERLEY.getId(), INVALID_CUSTOMER))
                .andExpect(status().isBadRequest())
                .andExpectAll(fieldErrors(1, 1, 1, 1));

        verifyNoInteractions(customerService);
    }

    @Test
    public void updateCustomerWithTypeMismatchTest() throws Exception {
        given(customerService.overwriteById(any(UUID.class), any(CustomerDto.class))).willReturn(Optional.of(mock(CustomerDto.class)));

        mockMvc.perform(updateCustomerRequest(SHERLEY.getId(), UNDESERIALIZABLE_CUSTOMER))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("TypeMismatching")))
                .andExpect(jsonPath("$.message", containsString("conversion failed")));

        verifyNoInteractions(customerService);
    }

    @Test
    public void updateCustomerNotFoundTest() throws Exception {
        CustomerDto customerDto = SHERLEY.toBuilder()
                .id(null)
                .firstName("Shayan")
                .lastName("Lauren")
                .email("shayanlauren@gmail.com")
                .createdDate(null)
                .updateDate(null)
                .build();

        given(customerService.overwriteById(any(UUID.class), eq(customerDto))).willReturn(Optional.empty());

        mockMvc.perform(updateCustomerRequest(UUID.randomUUID(), customerDto))
                .andExpect(status().isNotFound());

        verify(customerService).overwriteById(any(UUID.class), eq(customerDto));
    }

    @Test
    public void deleteCustomerTest() throws Exception {
        given(customerService.deleteById(any(UUID.class))).willReturn(true);

        mockMvc.perform(delete(UUID_PATH, SHERLEY.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerService).deleteById(uuidArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(SHERLEY.getId());
    }

    @Test
    public void deleteCustomerNotFoundTest() throws Exception {
        given(customerService.deleteById(any(UUID.class))).willReturn(false);

        mockMvc.perform(delete(UUID_PATH, SHERLEY.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(customerService).deleteById(uuidArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(SHERLEY.getId());
    }

    @Test
    public void patchCustomerTest() throws Exception {
        Map<String, String> customerMap = new HashMap<>();
        customerMap.put("firstName", "Updated First name");
        CustomerDto customerDto = CustomerDto.builder().firstName(customerMap.get("firstName")).build();

        given(customerService.updateById(any(UUID.class), any(CustomerDto.class))).willReturn(Optional.of(mock(CustomerDto.class)));

        mockMvc.perform(patchCustomerRequest(SHERLEY.getId(), customerMap))
                .andExpect(status().isNoContent());

        verify(customerService).updateById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(SHERLEY.getId());
        assertThat(customerArgumentCaptor.getValue()).isEqualTo(customerDto);
    }

    @Test
    public void patchCustomerWithInvalidFieldsTest() throws Exception {
        given(customerService.updateById(any(UUID.class), any(CustomerDto.class))).willReturn(Optional.of(mock(CustomerDto.class)));

        mockMvc.perform(patchCustomerRequest(SHERLEY.getId(), INVALID_CUSTOMER))
                .andExpect(status().isBadRequest())
                .andExpectAll(fieldErrors(1, 1, 1, 1));

        verifyNoInteractions(customerService);
    }

    @Test
    public void patchCustomerWithTypeMismatchTest() throws Exception {
        given(customerService.updateById(any(UUID.class), any(CustomerDto.class))).willReturn(Optional.of(mock(CustomerDto.class)));

        mockMvc.perform(patchCustomerRequest(SHERLEY.getId(), UNDESERIALIZABLE_CUSTOMER))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("TypeMismatching")))
                .andExpect(jsonPath("$.message", containsString("conversion failed")));

        verifyNoInteractions(customerService);
    }

    @Test
    public void patchCustomerNotFoundTest() throws Exception {
        Map<String, String> customerMap = new HashMap<>();
        customerMap.put("firstName", "Updated First name");
        CustomerDto customerDto = CustomerDto.builder().firstName(customerMap.get("firstName")).build();

        given(customerService.updateById(any(UUID.class), any(CustomerDto.class))).willReturn(Optional.empty());

        mockMvc.perform(patchCustomerRequest(SHERLEY.getId(), customerMap))
                .andExpect(status().isNotFound());

        verify(customerService).updateById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(SHERLEY.getId());
        assertThat(customerArgumentCaptor.getValue()).isEqualTo(customerDto);
    }

    private RequestBuilder saveCustomerRequest(Object requestBody) throws JsonProcessingException {
        return post(ROOT_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody));
    }

    private RequestBuilder updateCustomerRequest(UUID uuid, Object requestBody) throws JsonProcessingException {
        return put(UUID_PATH, uuid)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody));
    }

    private RequestBuilder patchCustomerRequest(UUID uuid, Object requestBody) throws JsonProcessingException {
        return patch(UUID_PATH, uuid)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody));
    }

    private ResultMatcher[] fieldErrors(int firstName, int lastName, int email, int birthdate) {
        return new ResultMatcher[] {
                jsonPath("$.length()", is(firstName + lastName + email + birthdate)),
                jsonPath("$..firstName", iterableWithSize(firstName)),
                jsonPath("$..lastName", iterableWithSize(lastName)),
                jsonPath("$..email", iterableWithSize(email)),
                jsonPath("$..birthdate", iterableWithSize(birthdate))
            };
    }
}