package mg.tommy.springboot.springbootwebapp.controller.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.tommy.springboot.springbootwebapp.domain.embedded.Customer;
import mg.tommy.springboot.springbootwebapp.service.CustomerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerApiController {
    private final CustomerService customerService;

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Customer> listAllCustomers() {
        return customerService.findAll();
    }

    @GetMapping("{uuid}")
    public ResponseEntity<Customer> getCustomerByUUID(@PathVariable("uuid") UUID uuid) {
        Optional<Customer> customer = customerService.findById(uuid);
        if (customer.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(customer.get());
    }

    @PostMapping
    public ResponseEntity<Customer> saveCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.save(customer);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", "/api/v1/customers/" + savedCustomer.getId().toString());
        return new ResponseEntity<>(savedCustomer, httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping("{uuid}")
    public ResponseEntity updateCustomer(@PathVariable("uuid") UUID uuid, @RequestBody Customer customer) {
        Customer updatedCustomer = customerService.updateById(uuid, customer);
        if (updatedCustomer == null) {
            customerService.save(customer);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{uuid}")
    public ResponseEntity deleteCustomer(@PathVariable("uuid") UUID uuid) {
        customerService.deleteById(uuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
