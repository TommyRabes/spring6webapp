package mg.tommy.springboot.springbootwebapp.controller.api;

import mg.tommy.springboot.springbootwebapp.domain.embedded.Customer;
import mg.tommy.springboot.springbootwebapp.dto.CustomerDto;
import mg.tommy.springboot.springbootwebapp.repository.embedded.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CustomerApiControllerIT {

    @Autowired
    CustomerApiController customerApiController;

    @Autowired
    CustomerRepository customerRepository;

    @Test
    public void listAllCustomersTest() {
        Iterable<CustomerDto> customers = customerApiController.listAllCustomers();

        assertThat(customers).hasSize(3);
    }

    @Transactional("embeddedTransactionManager")
    @Rollback
    @Test
    public void listEmptyCustomerListTest() {
        customerRepository.deleteAll();
        Iterable<CustomerDto> customers = customerApiController.listAllCustomers();

        assertThat(customers).isEmpty();
    }

    @Test
    public void getCustomerByUUIDTest() {
        Customer customer = customerRepository.findAll().get(0);

        CustomerDto customerDto = customerApiController.getCustomerByUUID(customer.getId()).getBody();

        assertThat(customerDto).isNotNull();
        assertThat(customerDto.getId()).isEqualTo(customer.getId());
    }

    @Test
    public void getCustomerByUUIDNotFoundTest() {
        assertThrows(NotFoundException.class, () -> {
            customerApiController.getCustomerByUUID(UUID.randomUUID());
        });
    }

}