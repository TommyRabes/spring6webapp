package mg.tommy.springboot.springbootwebapp.bootstrap;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import mg.tommy.springboot.springbootwebapp.domain.embedded.Beer;
import mg.tommy.springboot.springbootwebapp.domain.embedded.BeerStyle;
import mg.tommy.springboot.springbootwebapp.domain.embedded.Customer;
import mg.tommy.springboot.springbootwebapp.repository.embedded.BeerRepository;
import mg.tommy.springboot.springbootwebapp.repository.embedded.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@AllArgsConstructor
@Component
public class CommandLineInitializer implements CommandLineRunner {
    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Command line runner executed after application startup!");
        insertBeerData();
        insertCustomerData();
    }

    private void insertBeerData() {
        beerRepository.save(Beer.builder()
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("06546127")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(250)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build());

        beerRepository.save(Beer.builder()
                .version(1)
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("168673")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(190)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build());

        beerRepository.save(Beer.builder()
                .version(1)
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("98726454")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(150)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build());

    }

    private void insertCustomerData() {
        customerRepository.save(Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@gmail.com")
                .birthdate(LocalDate.of(1987, 2, 13))
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build());

        customerRepository.save(Customer.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@gmail.com")
                .birthdate(LocalDate.of(1995, 12, 5))
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build());

        customerRepository.save(Customer.builder()
                .firstName("Ross")
                .lastName("Stinger")
                .email("ross.stinger@gmail.com")
                .birthdate(LocalDate.of(1990, 7, 30))
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build());

    }
}
