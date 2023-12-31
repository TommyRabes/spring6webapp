package mg.tommy.springboot.springbootwebapp.repository.embedded;

import mg.tommy.springboot.springbootwebapp.model.domain.embedded.Client;
import mg.tommy.springboot.springbootwebapp.model.domain.embedded.Order;
import mg.tommy.springboot.springbootwebapp.model.domain.embedded.Product;

import java.util.List;

public interface MarketRepository {

    public List<Client> getClients();

    public List<Product> getProducts();

    public List<Order> getOrders();
}
