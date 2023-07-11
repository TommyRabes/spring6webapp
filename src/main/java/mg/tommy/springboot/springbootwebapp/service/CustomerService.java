package mg.tommy.springboot.springbootwebapp.service;

import mg.tommy.springboot.springbootwebapp.domain.embedded.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    Iterable<Customer> findAll();
    Optional<Customer> findById(UUID id);

    Customer save(Customer customer);

    Customer updateById(UUID uuid, Customer customer);

    void deleteById(UUID uuid);
}
