package mg.tommy.springboot.springbootwebapp.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import mg.tommy.springboot.springbootwebapp.domain.embedded.Customer;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.empty;
import static mg.tommy.springboot.springbootwebapp.controller.api.CustomerApiController.ROOT_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
    private static final Customer BAILEY = Customer.builder()
            .id(UUID.randomUUID())
            .version(1)
            .firstName("Bailey")
            .lastName("Summer")
            .email("bsum@gmail.com")
            .birthdate(LocalDate.of(2001, 12, 29))
            .build();

    private static final Customer BOOKER = Customer.builder()
            .id(UUID.randomUUID())
            .version(1)
            .firstName("Booker")
            .lastName("Reynold")
            .email("reybook@gmail.com")
            .birthdate(LocalDate.of(1997, 4, 13))
            .build();

    private static final Customer SHERLEY = Customer.builder()
            .id(UUID.randomUUID())
            .version(1)
            .firstName("Sherley")
            .lastName("Sirius")
            .email("sirisherley@gmail.com")
            .birthdate(LocalDate.of(1994, 8, 8))
            .build();

    private static final String UUID_PATH = ROOT_PATH + "/{uuid}";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CustomerService customerService;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<Customer> customerArgumentCaptor;

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
        Customer customer = SHERLEY.toBuilder()
                .id(null)
                .version(null)
                .build();

        given(customerService.save(any(Customer.class))).willReturn(SHERLEY);

        mockMvc.perform(post(ROOT_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", ROOT_PATH + "/" + SHERLEY.getId()))
                .andExpect(jsonPath("$.id", is(SHERLEY.getId().toString())))
                .andExpect(jsonPath("$.firstName", is(SHERLEY.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(SHERLEY.getLastName())))
                .andExpect(jsonPath("$.email", is(SHERLEY.getEmail())))
                .andExpect(jsonPath("$.birthdate", is(SHERLEY.getBirthdate().toString())));
    }

    @Test
    public void updateCustomerTest() throws Exception {
        Customer customer = SHERLEY.toBuilder()
                .id(null)
                .firstName("Shayan")
                .lastName("Lauren")
                .email("shayanlauren@gmail.com")
                .createdDate(null)
                .updateDate(null)
                .build();

        mockMvc.perform(put(UUID_PATH, SHERLEY.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isNoContent());

        verify(customerService).overwriteById(eq(SHERLEY.getId()), eq(customer));
    }

    @Test
    public void deleteCustomerTest() throws Exception {
        mockMvc.perform(delete(UUID_PATH, SHERLEY.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerService).deleteById(uuidArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(SHERLEY.getId());
    }

    @Test
    public void patchCustomerTest() throws Exception {
        Map<String, String> customerMap = new HashMap<>();
        customerMap.put("firstName", "Updated First name");
        Customer customer = Customer.builder().firstName(customerMap.get("firstName")).build();

        given(customerService.updateById(any(UUID.class), any(Customer.class))).willReturn(mock(Customer.class));

        mockMvc.perform(patch(UUID_PATH, SHERLEY.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerMap)))
                .andExpect(status().isNoContent());

        verify(customerService).updateById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(SHERLEY.getId());
        assertThat(customerArgumentCaptor.getValue()).isEqualTo(customer);
    }
}