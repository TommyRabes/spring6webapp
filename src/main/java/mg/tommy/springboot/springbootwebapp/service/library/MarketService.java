package mg.tommy.springboot.springbootwebapp.service.library;

import mg.tommy.springboot.springbootwebapp.domain.embedded.Client;
import mg.tommy.springboot.springbootwebapp.domain.embedded.Order;
import mg.tommy.springboot.springbootwebapp.domain.embedded.Product;
import mg.tommy.springboot.springbootwebapp.repository.embedded.MarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MarketService {

    private final MarketRepository marketJsonRepository;
    private final MarketRepository marketDatabaseRepository;

    @Autowired
    public MarketService(
            @Qualifier("marketJsonRepository") MarketRepository marketJsonRepository,
            @Qualifier("marketDatabaseRepository") MarketRepository marketDatabaseRepository) {
        this.marketJsonRepository = marketJsonRepository;
        this.marketDatabaseRepository = marketDatabaseRepository;
    }

    public List<Client> getClients() {
        return Stream.concat(
                marketJsonRepository.getClients().stream(),
                marketDatabaseRepository.getClients().stream())
                .collect(Collectors.toList());
    }

    public List<Product> getProducts() {
        return Stream.concat(
                marketJsonRepository.getProducts().stream(),
                marketDatabaseRepository.getProducts().stream())
                .collect(Collectors.toList());
    }

    public List<Order> getOrders() {
        return Stream.concat(
                marketJsonRepository.getOrders().stream(),
                marketDatabaseRepository.getOrders().stream())
                .collect(Collectors.toList());
    }
}
