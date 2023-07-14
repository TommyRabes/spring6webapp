package mg.tommy.springboot.springbootwebapp.service.library;

import lombok.RequiredArgsConstructor;
import mg.tommy.springboot.springbootwebapp.domain.embedded.Customer;
import mg.tommy.springboot.springbootwebapp.dto.CustomerDto;
import mg.tommy.springboot.springbootwebapp.mapper.CustomerMapper;
import mg.tommy.springboot.springbootwebapp.repository.embedded.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Iterable<CustomerDto> findAll() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::toCustomerDto)
                .collect(toList());
    }

    @Override
    public Optional<CustomerDto> findById(UUID id) {
        return customerRepository.findById(id).map(customerMapper::toCustomerDto);
    }

    @Override
    public CustomerDto save(CustomerDto customerDto) {
        return customerMapper.toCustomerDto(
                customerRepository.save(customerMapper.toCustomer(customerDto).toBuilder()
                        .createdDate(LocalDateTime.now())
                        .updateDate(LocalDateTime.now())
                        .build())
        );
    }

    @Override
    public Optional<CustomerDto> overwriteById(UUID uuid, CustomerDto customerDto) {
        Optional<Customer> customerToUpdate = customerRepository.findById(uuid);
        if (customerToUpdate.isEmpty()) {
            return Optional.empty();
        }
        Customer.CustomerBuilder builder = customerToUpdate.get().toBuilder()
                .firstName(customerDto.getFirstName())
                .lastName(customerDto.getLastName())
                .email(customerDto.getEmail())
                .birthdate(customerDto.getBirthdate())
                .updateDate(LocalDateTime.now());
        return Optional.of(customerMapper.toCustomerDto(customerRepository.save(builder.build())));
    }

    @Override
    public Optional<CustomerDto> updateById(UUID uuid, CustomerDto customerDto) {
        AtomicReference<Optional<CustomerDto>> customerDtoReference = new AtomicReference<>();

        customerRepository.findById(uuid)
                .ifPresentOrElse(customerToUpdate -> {
                    Customer.CustomerBuilder builder = customerToUpdate.toBuilder().updateDate(LocalDateTime.now());
                    if (StringUtils.hasText(customerDto.getFirstName())) {
                        builder.firstName(customerDto.getFirstName());
                    }
                    if (StringUtils.hasText(customerDto.getLastName())) {
                        builder.lastName(customerDto.getLastName());
                    }
                    if (StringUtils.hasText(customerDto.getEmail())) {
                        builder.email(customerDto.getEmail());
                    }
                    if (customerDto.getBirthdate() != null) {
                        builder.birthdate(customerDto.getBirthdate());
                    }
                    Customer updatedCustomer = customerRepository.save(builder.build());
                    customerDtoReference.set(Optional.of(customerMapper.toCustomerDto(updatedCustomer)));
                }, () -> customerDtoReference.set(Optional.empty()));

        return customerDtoReference.get();

    }

    @Override
    public boolean deleteById(UUID uuid) {
        if (!customerRepository.existsById(uuid))
            return false;
        customerRepository.deleteById(uuid);
        return true;
    }
}
