package mg.tommy.springboot.springbootwebapp.repository.embedded;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import mg.tommy.springboot.springbootwebapp.bootstrap.CommandLineInitializer;
import mg.tommy.springboot.springbootwebapp.configuration.database.EmbeddedDatabaseConfig;
import mg.tommy.springboot.springbootwebapp.domain.embedded.Beer;
import mg.tommy.springboot.springbootwebapp.domain.embedded.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional("embeddedTransactionManager")
@Rollback
@DataJpaTest
@Import({ EmbeddedDatabaseConfig.class })
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    public void persistCustomerTest() {
        Customer customerToSave = Customer.builder()
                .firstName("My first name")
                .lastName("My last name")
                .email("my.email@domain.com")
                .birthdate(LocalDate.of(2000, 2, 15))
                .build();

        Customer customer = customerRepository.saveAndFlush(customerToSave);

        assertThat(customer).isNotNull();
        assertThat(customer.getId()).isNotNull();
        assertThat(customer.getVersion()).isNotNull();
        assertThat(customer.getFirstName()).isEqualTo(customerToSave.getFirstName());
        assertThat(customer.getLastName()).isEqualTo(customerToSave.getLastName());
        assertThat(customer.getEmail()).isEqualTo(customerToSave.getEmail());
        assertThat(customer.getBirthdate()).isEqualTo(customerToSave.getBirthdate());
        assertThat(customer.getCreatedDate()).isEqualTo(customerToSave.getCreatedDate());
        assertThat(customer.getUpdateDate()).isEqualTo(customerToSave.getUpdateDate());
    }

    @Test
    public void persistCustomerWithoutRequiredColumnsTest() {
        ConstraintViolationException thrownException = assertThrows(ConstraintViolationException.class, () -> {
            customerRepository.saveAndFlush(Customer.builder().build());
        });

        assertThat(thrownException).matches(customerEntityConstraintViolation(2, 2, 2, 1));
    }

    @Test
    public void persistCustomerWithInvalidColumnsTest() {
        ConstraintViolationException thrownException = assertThrows(ConstraintViolationException.class, () -> {
            customerRepository.saveAndFlush(Customer.builder()
                            .firstName("fi")
                            .lastName("too long last name".repeat(20))
                            .email("invalidemail")
                            .birthdate(LocalDate.now())
                    .build());
        });

        assertThat(thrownException).matches(customerEntityConstraintViolation(1, 1, 1, 1));
    }

    private Predicate<ConstraintViolationException> customerEntityConstraintViolation(int firstName, int lastName, int email, int birthdate) {
        return (exception) -> {
            Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
            return violations.size() == firstName + lastName + email + birthdate &&
                    violations.stream().filter(violation -> "firstName".equals(violation.getPropertyPath().toString())).count() == firstName &&
                    violations.stream().filter(violation -> "lastName".equals(violation.getPropertyPath().toString())).count() == lastName &&
                    violations.stream().filter(violation -> "email".equals(violation.getPropertyPath().toString())).count() == email &&
                    violations.stream().filter(violation -> "birthdate".equals(violation.getPropertyPath().toString())).count() == birthdate;

        };
    }

}