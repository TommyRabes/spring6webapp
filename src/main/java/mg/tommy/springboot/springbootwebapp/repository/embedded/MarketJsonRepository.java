package mg.tommy.springboot.springbootwebapp.repository.embedded;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import mg.tommy.springboot.springbootwebapp.domain.embedded.Client;
import mg.tommy.springboot.springbootwebapp.domain.embedded.Order;
import mg.tommy.springboot.springbootwebapp.domain.embedded.Product;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository("marketJsonRepository")
public class MarketJsonRepository implements MarketRepository {

    private final String STATIC_RESOURCES_PATH = "src/main/resources/static/repository";
    private final JsonFactory jsonFactory = new JsonFactory(new ObjectMapper());

    @Override
    public List<Client> getClients() {
        return getValues(new TypeReference<List<Client>>() {}, "clients.json");
    }

    @Override
    public List<Product> getProducts() {
        return getValues(new TypeReference<List<Product>>() {}, "products.json");
    }

    @Override
    public List<Order> getOrders() {
        return getValues(new TypeReference<List<Order>>() {}, "orders.json");
    }

    public <T> List<T> getValues(TypeReference<List<T>> typeReference, String repositoryFile) {
        try {
            Path filePath = Paths.get(STATIC_RESOURCES_PATH, repositoryFile);
            JsonParser parser = jsonFactory.createParser(filePath.toFile());
            return parser.readValueAs(typeReference);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<T>();
        }
    }

    /**
     * Cannot utilize this method as Jackson parser will return LinkedHashMap as a result of readValueAs
     * Perhaps as a result of type erasure, the TypeReference doesn't have any clue of the exact type of T
     */
    @Deprecated
    private <T> List<T> getValues(Class<T> type, String repositoryFile) {
        try {
            Path filePath = Paths.get(STATIC_RESOURCES_PATH, repositoryFile);
            JsonParser parser = jsonFactory.createParser(filePath.toFile());
            return parser.readValueAs(new TypeReference<List<T>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
