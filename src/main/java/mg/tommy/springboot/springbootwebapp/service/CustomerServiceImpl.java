package mg.tommy.springboot.springbootwebapp.service;

import lombok.AllArgsConstructor;
import mg.tommy.springboot.springbootwebapp.domain.embedded.Customer;
import mg.tommy.springboot.springbootwebapp.repository.embedded.CustomerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public Iterable<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        return customerRepository.findById(id);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer.toBuilder()
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build());
    }

    @Override
    public Customer updateById(UUID uuid, Customer customer) {
        Optional<Customer> customerToUpdate = customerRepository.findById(uuid);
        if (customerToUpdate.isEmpty()) {
            return null;
        }
        Customer.CustomerBuilder builder = customerToUpdate.get().toBuilder();
        if (customer.getFirstName() != null) {
            builder.firstName(customer.getFirstName());
        }
        if (customer.getLastName() != null) {
            builder.lastName(customer.getLastName());
        }
        if (customer.getEmail() != null) {
            builder.email(customer.getEmail());
        }
        if (customer.getBirthdate() != null) {
            builder.birthdate(customer.getBirthdate());
        }
        builder.updateDate(LocalDateTime.now());
        return customerRepository.save(builder.build());
    }

    @Override
    public void deleteById(UUID uuid) {
        customerRepository.deleteById(uuid);
    }
}
