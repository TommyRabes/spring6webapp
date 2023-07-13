package mg.tommy.springboot.springbootwebapp.controller.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.tommy.springboot.springbootwebapp.domain.embedded.Customer;
import mg.tommy.springboot.springbootwebapp.service.library.CustomerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(CustomerApiController.ROOT_PATH)
public class CustomerApiController {
    public static final String ROOT_PATH = "/api/v1/customers";
    
    private final CustomerService customerService;

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Customer> listAllCustomers() {
        return customerService.findAll();
    }

    @GetMapping("{uuid}")
    public ResponseEntity<Customer> getCustomerByUUID(@PathVariable("uuid") UUID uuid) {
        Optional<Customer> customer = customerService.findById(uuid);
        if (customer.isEmpty()) {
            throw new NotFoundException("Customer with uuid : " + uuid + " not found");
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
        customerService.overwriteById(uuid, customer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("{uuid}")
    public ResponseEntity patchCustomer(@PathVariable("uuid") UUID uuid, @RequestBody Customer customer) {
        Customer patchedCustomer = customerService.updateById(uuid, customer);
        if (patchedCustomer == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{uuid}")
    public ResponseEntity deleteCustomer(@PathVariable("uuid") UUID uuid) {
        customerService.deleteById(uuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
