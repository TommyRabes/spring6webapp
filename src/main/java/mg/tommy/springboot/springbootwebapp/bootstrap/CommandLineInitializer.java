package mg.tommy.springboot.springbootwebapp.bootstrap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.tommy.springboot.springbootwebapp.mapper.BeerMapper;
import mg.tommy.springboot.springbootwebapp.model.domain.embedded.*;
import mg.tommy.springboot.springbootwebapp.model.dto.BeerRecord;
import mg.tommy.springboot.springbootwebapp.repository.embedded.*;
import mg.tommy.springboot.springbootwebapp.service.brewing.BeerCsvService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Profile({"Full", "Database"})
@Scope("singleton")
@Component
public class CommandLineInitializer implements CommandLineRunner {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;
    private final BeerCsvService beerCsvService;
    private final BeerMapper beerMapper;

    @Transactional("embeddedTransactionManager")
    @Override
    public void run(String... args) throws Exception {
        log.info("Command line runner executed after application startup!");
        insertBookData();
        insertBeerData();
        insertBeerCsvData();
        insertCustomerData();
    }

    private void insertBeerData() {
        if (beerRepository.count() > 1000) return;

        beerRepository.save(Beer.builder()
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("06546127")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(250)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build());

        beerRepository.save(Beer.builder()
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("168673")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(190)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build());

        beerRepository.save(Beer.builder()
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("98726454")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(150)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build());

    }

    private void insertBeerCsvData() throws FileNotFoundException {
        if (beerRepository.count() > 1000) return;

        File csvFile = ResourceUtils.getFile("classpath:static/repository/csv/beers.csv");
        List<BeerRecord> beerRecords = beerCsvService.convertCsv(csvFile);
        List<Beer> beers = beerRecords.stream().map(beerMapper::toBeer).collect(toList());
        beerRepository.saveAll(beers);
    }

    private void insertCustomerData() {
        if (customerRepository.count() > 2) return;

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

    private void insertBookData() {
        if (bookRepository.count() > 0) return;

        Author eric = new Author();
        eric.setFirstName("Eric");
        eric.setLastName("Evans");
        eric.setEmail("eevans@gmail.com");

        Book ddd = new Book();
        ddd.setTitle("Domain Driven Design");
        ddd.setIsbn("123456");
        ddd.setPublishDate(LocalDate.of(2007, 11, 10));

        Author ericSaved = authorRepository.save(eric);
        Book dddSaved = bookRepository.save(ddd);

        Author rod = new Author();
        rod.setFirstName("Rod");
        rod.setLastName("Johnson");
        rod.setEmail("rjohnson@gmail.com");

        Book noEJB = new Book();
        noEJB.setTitle("J2EE Development without EJB");
        noEJB.setIsbn("564238");
        noEJB.setPublishDate(LocalDate.of(2003, 5, 22));

        Author rodSaved = authorRepository.save(rod);
        Book noEJBSaved = bookRepository.save(noEJB);

        // Comment out this java relationship setup
        // We only need to set the relationship on Book entity
        // and save book entities to update the join table accordingly
        // since in our case, Book is the owner of the many-to-many relationship between Author and Book
        // ericSaved.getBooks().add(dddSaved);
        // rodSaved.getBooks().add(noEJBSaved);
        dddSaved.getAuthors().add(ericSaved);
        noEJBSaved.getAuthors().add(rodSaved);

        Publisher tom = new Publisher();
        tom.setFirstName("Tom");
        tom.setLastName("Cook");
        tom.setEmail("tomcook@gmail.com");
        tom.setAddress("93 NORTH 9TH STREET, BROOKLYN NY 11211");
        tom.setCity("Brooklyn");
        tom.setState("NY");
        tom.setZip("11211");

        Publisher tomSaved = publisherRepository.save(tom);

        dddSaved.setPublisher(tomSaved);
        noEJBSaved.setPublisher(tomSaved);

        // authorRepository.save(ericSaved);
        // authorRepository.save(rodSaved);
        bookRepository.save(dddSaved);
        bookRepository.save(noEJBSaved);

        log.info("In Bootstrap");
        log.info("Author count: " + authorRepository.count());
        log.info("Books count: " + bookRepository.count());
        log.info("Publishers count: " + publisherRepository.count());
    }
}
