package mg.tommy.springboot.springbootwebapp.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.tommy.springboot.springbootwebapp.dto.CustomerDto;
import mg.tommy.springboot.springbootwebapp.service.library.CustomerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(CustomerApiController.ROOT_PATH)
public class CustomerApiController {
    public static final String ROOT_PATH = "/api/v1/customers";
    
    private final CustomerService customerService;

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<CustomerDto> listAllCustomers() {
        return customerService.findAll();
    }

    @GetMapping("{uuid}")
    public ResponseEntity<CustomerDto> getCustomerByUUID(@PathVariable("uuid") UUID uuid) {
        return ResponseEntity.ok()
                .body(customerService.findById(uuid)
                        .orElseThrow(() -> new NotFoundException("Customer with uuid : " + uuid + " not found"))
                );
    }

    @PostMapping
    public ResponseEntity<CustomerDto> saveCustomer(@RequestBody CustomerDto customerDto) {
        CustomerDto savedCustomer = customerService.save(customerDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", "/api/v1/customers/" + savedCustomer.getId().toString());
        return new ResponseEntity<>(savedCustomer, httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping("{uuid}")
    public ResponseEntity updateCustomer(@PathVariable("uuid") UUID uuid, @RequestBody CustomerDto customerDto) {
        customerService.overwriteById(uuid, customerDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("{uuid}")
    public ResponseEntity patchCustomer(@PathVariable("uuid") UUID uuid, @RequestBody CustomerDto customerDto) {
        CustomerDto patchedCustomer = customerService.updateById(uuid, customerDto);
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
