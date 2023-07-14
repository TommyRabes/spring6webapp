package mg.tommy.springboot.springbootwebapp.controller.api;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import mg.tommy.springboot.springbootwebapp.domain.embedded.Customer;
import mg.tommy.springboot.springbootwebapp.dto.CustomerDto;
import mg.tommy.springboot.springbootwebapp.repository.embedded.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

@SpringBootTest
class CustomerApiControllerIT {

    @Autowired
    CustomerApiController customerApiController;

    @Autowired
    CustomerRepository customerRepository;

    @PersistenceContext
    EntityManager entityManager;

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

    @Transactional("embeddedTransactionManager")
    @Rollback
    @Test
    public void saveCustomerTest() {
        CustomerDto customerToSave = CustomerDto.builder()
                .firstName("Gilbert")
                .lastName("Poitier")
                .email("poilbert@gmail.com")
                .birthdate(LocalDate.of(1960, 5, 27))
                .build();

        ResponseEntity<CustomerDto> responseEntity = customerApiController.saveCustomer(customerToSave);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String generatedUUID = responseEntity.getHeaders().getLocation().getPath().split("/")[4];
        CustomerDto savedCustomer = responseEntity.getBody();

        assertThat(savedCustomer.getId()).isNotNull();
        assertThat(UUID.fromString(generatedUUID)).isEqualTo(savedCustomer.getId());
        assertThat(savedCustomer.getVersion()).isNotNull();
        assertThat(savedCustomer.getFirstName()).isEqualTo(customerToSave.getFirstName());
        assertThat(savedCustomer.getLastName()).isEqualTo(customerToSave.getLastName());
        assertThat(savedCustomer.getEmail()).isEqualTo(customerToSave.getEmail());
        assertThat(savedCustomer.getBirthdate()).isEqualTo(customerToSave.getBirthdate());
        assertThat(savedCustomer.getCreatedDate()).isNotNull();
        assertThat(savedCustomer.getUpdateDate()).isNotNull();
    }

    @Transactional("embeddedTransactionManager")
    @Rollback
    @Test
    public void updateCustomerTest() {
        final Customer customerToUdpate = customerRepository.findAll().get(0);
        entityManager.detach(customerToUdpate);

        CustomerDto customerForUpdateDto = CustomerDto.builder()
                .lastName("New last name")
                .build();

        ResponseEntity responseEntity = customerApiController.updateCustomer(customerToUdpate.getId(), customerForUpdateDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Customer updatedCustomer = customerRepository.findById(customerToUdpate.getId()).get();
        assertThat(updatedCustomer.getId()).isEqualTo(customerToUdpate.getId());
        assertThat(updatedCustomer.getVersion()).isNotNull();
        assertThat(updatedCustomer.getVersion()).isEqualTo(customerToUdpate.getVersion());
        assertThat(updatedCustomer.getFirstName()).isNull();
        assertThat(updatedCustomer.getLastName()).isEqualTo(customerForUpdateDto.getLastName());
        assertThat(updatedCustomer.getEmail()).isNull();
        assertThat(updatedCustomer.getBirthdate()).isNull();
        assertThat(updatedCustomer.getCreatedDate()).isNotNull();
        assertThat(updatedCustomer.getUpdateDate()).isNotNull();
        assertThat(updatedCustomer.getUpdateDate()).isNotEqualTo(customerToUdpate.getUpdateDate());
    }

    @Transactional("embeddedTransactionManager")
    @Rollback
    @Test
    public void updateCustomerNotFoundTest() {
        assertThrows(NotFoundException.class, () -> {
            customerApiController.updateCustomer(UUID.randomUUID(), mock(CustomerDto.class));
        });
    }

    @Transactional("embeddedTransactionManager")
    @Rollback
    @Test
    public void deleteCustomerTest() {
        final Long initialCustomerCount = customerRepository.count();
        final UUID uuidToDelete = customerRepository.findAll().get(0).getId();

        ResponseEntity responseEntity = customerApiController.deleteCustomer(uuidToDelete);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(customerRepository.count()).isEqualTo(initialCustomerCount - 1);

        Optional<Customer> deletedCustomer = customerRepository.findById(uuidToDelete);
        assertThat(deletedCustomer).isEmpty();
    }

    @Transactional("embeddedTransactionManager")
    @Rollback
    @Test
    public void deleteCustomerNotFoundTest() {
        assertThrows(NotFoundException.class, () -> {
            customerApiController.deleteCustomer(UUID.randomUUID());
        });
    }


    @Transactional("embeddedTransactionManager")
    @Rollback
    @Test
    public void patchCustomerTest() {
        final Customer customerToPatch = customerRepository.findAll().get(0);
        entityManager.detach(customerToPatch);

        CustomerDto customerForPatchDto = CustomerDto.builder()
                .lastName("New last name")
                .build();

        ResponseEntity responseEntity = customerApiController.patchCustomer(customerToPatch.getId(), customerForPatchDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Customer updatedCustomer = customerRepository.findById(customerToPatch.getId()).get();
        assertThat(updatedCustomer.getId()).isEqualTo(customerToPatch.getId());
        assertThat(updatedCustomer.getVersion()).isEqualTo(customerToPatch.getVersion());
        assertThat(updatedCustomer.getFirstName()).isEqualTo(customerToPatch.getFirstName());
        assertThat(updatedCustomer.getLastName()).isEqualTo(customerForPatchDto.getLastName());
        assertThat(updatedCustomer.getEmail()).isEqualTo(customerToPatch.getEmail());
        assertThat(updatedCustomer.getBirthdate()).isEqualTo(customerToPatch.getBirthdate());
        assertThat(updatedCustomer.getCreatedDate()).isEqualTo(customerToPatch.getCreatedDate());
        assertThat(updatedCustomer.getUpdateDate()).isNotNull();
        assertThat(updatedCustomer.getUpdateDate()).isNotEqualTo(customerToPatch.getUpdateDate());
    }


    @Transactional("embeddedTransactionManager")
    @Rollback
    @Test
    public void patchCustomerNotFoundTest() {
        assertThrows(NotFoundException.class, () -> {
            customerApiController.patchCustomer(UUID.randomUUID(), mock(CustomerDto.class));
        });
    }
}