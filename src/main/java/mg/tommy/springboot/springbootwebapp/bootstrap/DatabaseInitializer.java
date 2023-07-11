package mg.tommy.springboot.springbootwebapp.bootstrap;

import jakarta.annotation.PostConstruct;
import mg.tommy.springboot.springbootwebapp.domain.embedded.*;
import mg.tommy.springboot.springbootwebapp.domain.persistent.Role;
import mg.tommy.springboot.springbootwebapp.domain.persistent.User;
import mg.tommy.springboot.springbootwebapp.repository.embedded.*;
import mg.tommy.springboot.springbootwebapp.repository.persistent.RoleRepository;
import mg.tommy.springboot.springbootwebapp.repository.persistent.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Scope("singleton")
public class DatabaseInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseInitializer.class);

    private final AuthorRepository authorRepository;
    private final PostRepository postRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final TravelerRepository travelerRepository;
    private final LocationRepository locationRepository;
    private final FlightRepository flightRepository;
    private final VisitRepository visitRepository;
    private final PlanRepository planRepository;
    private final ItineraryRepository itineraryRepository;

    @Autowired
    public DatabaseInitializer(
            AuthorRepository authorRepository,
            PostRepository postRepository,
            RoleRepository roleRepository,
            UserRepository userRepository,
            TravelerRepository travelerRepository,
            LocationRepository locationRepository,
            FlightRepository flightRepository,
            VisitRepository visitRepository,
            PlanRepository planRepository,
            ItineraryRepository itineraryRepository) {
        this.authorRepository = authorRepository;
        this.postRepository = postRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.travelerRepository = travelerRepository;
        this.locationRepository = locationRepository;
        this.flightRepository = flightRepository;
        this.visitRepository = visitRepository;
        this.planRepository = planRepository;
        this.itineraryRepository = itineraryRepository;
    }

    @PostConstruct
    void feed() {
        LOGGER.info("Database feed method called...");
        feedUsers();
        feedPosts();
        feedPlans();
    }

    private void feedUsers() {
        LOGGER.info("Creating users and roles");
        Role guestRole = Role.builder().role("GUEST").build();
        Role readerRole = Role.builder().role("READER").build();
        Role clientRole = Role.builder().role("CLIENT").build();
        Role adminRole = Role.builder().role("ADMIN").build();

        guestRole = roleRepository.save(guestRole);
        readerRole = roleRepository.save(readerRole);
        clientRole = roleRepository.save(clientRole);
        adminRole = roleRepository.save(adminRole);

        final User reader = User.builder()
                .email("reader@gmail.com")
                .password("{bcrypt}$2a$10$pB1fvMDW9HA6L4Y4ULAzy.9j1VRQZ3BXnXuXspH.FtanemnJXoxC2")
                .roles(Set.of(guestRole, readerRole))
                .fullName("App Reader")
                .build();
        final User client = User.builder()
                .email("client@gmail.com")
                .password("{bcrypt}$2a$10$UWlGnleelzPZ2J0gJfBLNuvtQp6ty3SMwnnvltncES.exxIArIzfu")
                .roles(Set.of(guestRole, clientRole))
                .fullName("App Client")
                .build();
        final User admin = User.builder()
                .email("admin@gmail.com")
                .password("{pbkdf2@SpringSecurity_v5_8}5070e01244772466c4b842cc367df8164deaa379b73d04bd36f524b94a9540e4b4014e9d3c6f2d8d52f936fb0c5b6823")
                .roles(Set.of(adminRole))
                .fullName("App Admin")
                .build();

        userRepository.save(reader);
        userRepository.save(client);
        userRepository.save(admin);
    }

    private void feedPosts() {
        LOGGER.info("Feeding posts data...");
        Author author = new Author("Tommy", "Rabesalama");
        author.setEmail("rabesalama.tommy@gmail.com");
        author = authorRepository.save(author);

        Post post = new Post("Blog post title");
        post.setSlug("blog-post-title");
        post.setPostedOn(Date.from(LocalDate.of(2023, 01, 30).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        post.setAuthor(author);
        post.setBody("Blog post body");
        post.setTeaser(post.getBody());
        post.setActive(false);
        post.setKeywords(Set.of("Tommy", "Dummy", "Blog", "Short", "Unpopular"));
        postRepository.save(post);
    }

    private void feedPlans() {
        LOGGER.info("Feeding plans data...");

        Traveler t1 = new Traveler("John Doe", "Male", toDate(LocalDate.of(1990, 6, 23)));
        Traveler t2 = new Traveler("Jane Smith", "Female", toDate(LocalDate.of(1985, 9, 20)));
        Traveler t3 = new Traveler("Alex Johnson", "Male", toDate(LocalDate.of(1995, 2, 10)));
        Traveler t4 = new Traveler("Sarah Thompson", "Female", toDate(LocalDate.of(1992, 11, 28)));
        Traveler t5 = new Traveler("Michael Brown", "Male", toDate(LocalDate.of(1988, 7, 3)));

        travelerRepository.save(t1);
        travelerRepository.save(t2);
        travelerRepository.save(t3);
        travelerRepository.save(t4);
        travelerRepository.save(t5);

        Location l0 = new Location("Antananarivo, Madagascar", "Starting point.", new Coordinates(0.0, 0.0, 4.));
        Location l1 = new Location("Paris, France", "Known as the \"City of Love,\" Paris offers iconic attractions such as the Eiffel Tower, Louvre Museum, Notre-Dame Cathedral, and charming neighborhoods like Montmartre.", new Coordinates(48.8566, 2.3522, 4.));
        Location l2 = new Location("Rome, Italy", "With its rich history and ancient ruins like the Colosseum and Roman Forum, Rome is a must-visit destination. The Vatican City, home to St. Peter's Basilica and the Sistine Chapel, is also located in Rome.", new Coordinates(41.9028, 12.4964, 4.));
        Location l3 = new Location("Tokyo, Japan", "A vibrant city that seamlessly blends tradition and modernity. Tokyo offers a mix of high-tech attractions, beautiful gardens, historic temples, and delicious cuisine.", new Coordinates(35.6895, 139.6917, 4.));
        Location l4 = new Location("New York City, USA", "Known as \"The Big Apple,\" New York City is a bustling metropolis with famous landmarks like Times Square, Statue of Liberty, Central Park, and world-class museums like the Metropolitan Museum of Art.", new Coordinates(40.7128, -74.0060, 4.));
        Location l5 = new Location("Cape Town, South Africa", "Located at the tip of Africa, Cape Town boasts stunning natural beauty with Table Mountain, beautiful beaches, and the Cape Winelands. It also offers a diverse cultural scene and vibrant food markets.", new Coordinates(-33.9249, 18.4241, 4.));
        Location l6 = new Location("Sydney, Australia", "A vibrant coastal city with iconic landmarks like the Sydney Opera House and Sydney Harbour Bridge. Sydney offers beautiful beaches, a thriving food scene, and the opportunity to explore nearby natural wonders like the Blue Mountains.", new Coordinates(-33.8688, 151.2093, 4.));
        Location l7 = new Location("Cusco, Peru", "The gateway to Machu Picchu, Cusco is a historic city with a blend of Incan and Spanish colonial architecture. It offers a rich cultural experience, with traditional markets, museums, and nearby archaeological sites.", new Coordinates(-13.5319, -71.9675, 4.));
        Location l8 = new Location("Barcelona, Spain", "Known for its unique architecture, including the famous works of Antoni Gaudí like the Sagrada Familia and Park Güell, Barcelona also offers beautiful beaches, vibrant street life, and delicious cuisine.", new Coordinates(41.3851, 2.1734, 4.));
        Location l9 = new Location("Cairo, Egypt", "Cairo is known for its iconic ancient Egyptian sites, including the Great Pyramids of Giza and the Sphinx, along with its vibrant Islamic and Coptic heritage, bustling markets, and the renowned Egyptian Museum.", new Coordinates(30.0444, 31.2357, 4.));

        locationRepository.saveAll(Arrays.asList(l0, l1, l2, l3, l4, l5, l6, l7, l8, l9));

        Itinerary i1 = new Itinerary("European Adventure", Arrays.asList());
        Itinerary i2 = new Itinerary("Eastern Delights", Arrays.asList());
        Itinerary i3 = new Itinerary("Ancient Wonders", Arrays.asList());
        Itinerary i4 = new Itinerary("Transatlantic Adventure", Arrays.asList());

        itineraryRepository.saveAll(Arrays.asList(i1, i2, i3, i4));

        Flight f1 = new Flight(BigDecimal.valueOf(5000.), toDate(LocalDateTime.of(2023, 1, 20, 22, 30)), toDate(LocalDateTime.of(2023, 1, 21, 2, 45)), l0, l1);
        Flight f2 = new Flight(BigDecimal.valueOf(1299.99), toDate(LocalDateTime.of(2023, 1, 24, 2, 45)), toDate(LocalDateTime.of(2023, 1, 24, 4, 0)), l1, l2);
        Visit v1 = new Visit(Arrays.asList("Explore iconic landmarks like the Eiffel Tower, Louvre Museum, and Notre-Dame Cathedral.", "Take a stroll along the Seine River and enjoy the charming neighborhoods."), f1.getArrivalDate(), f2.getDepartureDate(), f1, f2, i1);

        Flight f3 = new Flight(BigDecimal.valueOf(649.99), toDate(LocalDateTime.of(2023, 1, 27, 12, 30)), toDate(LocalDateTime.of(2023, 1, 27, 19, 30)), l2, l8);
        Visit v2 = new Visit(Arrays.asList("Visit ancient sites such as the Colosseum, Roman Forum, and Pantheon.", "Explore the Vatican City and its world-renowned attractions like St. Peter's Basilica and the Sistine Chapel."), f2.getArrivalDate(), f3.getDepartureDate(), f2, f3, i1);

        Flight f30 = new Flight(BigDecimal.valueOf(2500), toDate(LocalDateTime.of(2023, 1, 30, 23, 30)), toDate(LocalDateTime.of(2023, 1, 31, 3, 30)), l8, l0);
        Visit v3 = new Visit(Arrays.asList("Experience the unique architecture of Antoni Gaudí, including the Sagrada Familia and Park Güell.", "Enjoy the vibrant street life, visit the Gothic Quarter, and savor delicious Spanish cuisine."), f3.getArrivalDate(), f30.getDepartureDate(), f3, f30, i1);

        Flight f4 = new Flight(BigDecimal.valueOf(1500.), toDate(LocalDateTime.of(2023, 2, 13, 7, 15)), toDate(LocalDateTime.of(2023, 2, 13, 16, 0)), l0, l3);
        Flight f5 = new Flight(BigDecimal.valueOf(789.99), toDate(LocalDateTime.of(2023, 2, 16, 19, 55)), toDate(LocalDateTime.of(2023, 2, 16, 22, 35)), l3, l6);
        Visit v4 = new Visit(Arrays.asList("Immerse yourself in the bustling city life, visit famous neighborhoods like Shibuya and Shinjuku.", "Explore historic sites such as the Meiji Shrine and Tokyo Imperial Palace."), f4.getArrivalDate(), f5.getDepartureDate(), f4, f5, i2);

        Flight f6 = new Flight(BigDecimal.valueOf(350.), toDate(LocalDateTime.of(2023, 2, 20, 0, 30)), toDate(LocalDateTime.of(2023, 2, 20, 3, 40)), l6, l5);
        Visit v5 = new Visit(Arrays.asList("Discover iconic landmarks like the Sydney Opera House and Sydney Harbour Bridge.", "Relax at stunning beaches like Bondi Beach and take a coastal walk."), f5.getArrivalDate(), f6.getDepartureDate(), f5, f6, i2);

        Flight f60 = new Flight(BigDecimal.valueOf(1650.), toDate(LocalDateTime.of(2023, 2, 23, 2, 25)), toDate(LocalDateTime.of(2023, 2, 23, 6, 5)), l5, l0);
        Visit v6 = new Visit(Arrays.asList("Ascend Table Mountain for breathtaking views of the city and coastline.", "Explore the vibrant Victoria & Alfred Waterfront, visit Robben Island, and take a wine tour in the Cape Winelands."), f5.getArrivalDate(), f6.getDepartureDate(), f6, f60, i2);

        Flight f7 = new Flight(BigDecimal.valueOf(3000.), toDate(LocalDateTime.of(2023, 1, 25, 20, 20)), toDate(LocalDateTime.of(2023, 1, 25, 22, 40)), l0, l2);
        Flight f8 = new Flight(BigDecimal.valueOf(1750.), toDate(LocalDateTime.of(2023, 1, 28, 20, 30)), toDate(LocalDateTime.of(2023, 1, 28, 21, 55)), l2, l7);
        Visit v7 = new Visit(Arrays.asList("Explore the ancient ruins of the Colosseum, Roman Forum, and Palatine Hill.", "Marvel at Renaissance art in the Vatican Museums and admire Michelangelo's masterpiece in the Sistine Chapel."), f7.getArrivalDate(), f8.getDepartureDate(), f7, f8, i3);

        Flight f9 = new Flight(BigDecimal.valueOf(500.), toDate(LocalDateTime.of(2023, 2, 1, 1, 10)), toDate(LocalDateTime.of(2023, 2, 1, 3, 0)), l7, l9);
        Visit v8 = new Visit(Arrays.asList("Discover the Inca heritage by visiting the historic center of Cusco.", "Take a train or hike to the breathtaking Machu Picchu, a UNESCO World Heritage Site."), f8.getArrivalDate(), f9.getDepartureDate(), f8, f9, i3);

        Flight f90 = new Flight(BigDecimal.valueOf(2899.99), toDate(LocalDateTime.of(2023, 2, 4, 13, 0)), toDate(LocalDateTime.of(2023, 2, 4, 17, 30)), l9, l0);
        Visit v9 = new Visit(Arrays.asList("Explore the Giza Plateau and witness the Great Pyramids and Sphinx.", "Visit the Egyptian Museum to see ancient artifacts, including the treasures of Tutankhamun."), f9.getArrivalDate(), f90.getDepartureDate(), f9, f90, i3);

        Flight f10 = new Flight(BigDecimal.valueOf(2959.50), toDate(LocalDateTime.of(2023, 2, 2, 10, 40)), toDate(LocalDateTime.of(2023, 2, 2, 14, 50)), l0, l1);
        Flight f11 = new Flight(BigDecimal.valueOf(6320.69), toDate(LocalDateTime.of(2023, 2, 5, 23, 20)), toDate(LocalDateTime.of(2023, 2, 6, 6, 30)), l1, l4);
        Visit v10 = new Visit(Arrays.asList("Explore iconic landmarks like the Eiffel Tower, Louvre Museum, and Notre-Dame Cathedral.", "Take a stroll along the Seine River and enjoy the charming neighborhoods."), f10.getArrivalDate(), f11.getDepartureDate(), f10, f11, i4);

        Flight f12 = new Flight(BigDecimal.valueOf(1370.), toDate(LocalDateTime.of(2023, 2, 9, 4, 0)), toDate(LocalDateTime.of(2023, 2, 9, 8, 0)), l4, l3);
        Visit v11 = new Visit(Arrays.asList("Visit famous attractions such as Times Square, Central Park, and the Statue of Liberty.", "Explore renowned museums like the Metropolitan Museum of Art and the Museum of Modern Art."), f11.getArrivalDate(), f12.getDepartureDate(), f11, f12, i4);

        Flight f120 = new Flight(BigDecimal.valueOf(4544.99), toDate(LocalDateTime.of(2023, 2, 12, 5, 0)), toDate(LocalDateTime.of(2023, 2, 12, 14, 40)), l3, l0);
        Visit v12 = new Visit(Arrays.asList("Immerse yourself in the bustling city life, visit famous neighborhoods like Shibuya and Shinjuku.", "Explore historic sites such as the Meiji Shrine and Tokyo Imperial Palace."), f12.getArrivalDate(), f120.getDepartureDate(), f12, f120, i4);

        flightRepository.saveAll(Arrays.asList(f1, f2, f3, f30, f4, f5, f6, f60, f7, f8, f9, f90, f10, f11, f12, f120));
        visitRepository.saveAll(Arrays.asList(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12));

        Plan p1 = new Plan(i1.getName(), i1, Stream.of(t1, t2, t3).collect(Collectors.toSet()));
        Plan p2 = new Plan(i2.getName(), i2, Stream.of(t1, t2, t3).collect(Collectors.toSet()));
        Plan p3 = new Plan(i3.getName(), i3, Stream.of(t4, t5).collect(Collectors.toSet()));
        Plan p4 = new Plan(i4.getName(), i4, Stream.of(t4, t5).collect(Collectors.toSet()));

        planRepository.saveAll(Arrays.asList(p1, p2, p3, p4));
    }

    private Date toDate(LocalDate localDate) {
        return toDate(localDate.atTime(0, 0));
    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}
