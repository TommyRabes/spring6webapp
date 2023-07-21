package mg.tommy.springboot.springbootwebapp.repository.embedded;

import mg.tommy.springboot.springbootwebapp.model.domain.embedded.Client;
import mg.tommy.springboot.springbootwebapp.model.domain.embedded.Order;
import mg.tommy.springboot.springbootwebapp.model.domain.embedded.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Repository("marketDatabaseRepository")
public class MarketDatabaseRepository implements MarketRepository {

    private static final String CLIENT_COLUMNS = "\"Client\".id AS client_id, \"Client\".name AS client_name, \"Client\".email AS client_email, \"Client\".birthdate AS client_birthdate";
    private static final String PRODUCT_COLUMNS = "PRODUCT.id AS product_id, PRODUCT.name AS product_name, PRODUCT.price AS product_price, PRODUCT.quantity AS product_quantity";
    private static final String ORDER_COLUMNS = "\"Order\".id AS order_id, \"Order\".client_id AS order_client_id, \"Order\".product_id AS order_product_id, \"Order\".quantity AS order_quantity, \"Order\".total_price AS order_total_price, \"Order\".status AS order_status";

    private static final RowMapper<Client> USER_MAPPER = (ResultSet rs, int rowNum) -> {
            Client client = new Client();
            client.setId(rs.getLong("client_id"));
            client.setName(rs.getString("client_name"));
            client.setEmail(rs.getString("client_email"));
            client.setBirthdate(rs.getDate("client_birthdate"));
            return client;
        };

    private static final RowMapper<Product> PRODUCT_MAPPER = (ResultSet rs, int rowNum) -> {
            Product product = new Product();
            product.setId(rs.getLong("product_id"));
            product.setName(rs.getString("product_name"));
            product.setPrice(rs.getBigDecimal("product_price"));
            product.setQuantity(rs.getInt("product_quantity"));
            return product;
        };

    private static final RowMapper<Order> ORDER_MAPPER = (ResultSet rs, int rowNum) -> {
            Order order = new Order();
            order.setId(rs.getLong("order_id"));
            order.setQuantity(rs.getInt("order_quantity"));
            order.setTotalPrice(rs.getBigDecimal("order_total_price"));
            order.setProduct(PRODUCT_MAPPER.mapRow(rs, rowNum));
            order.setClient(USER_MAPPER.mapRow(rs, rowNum));
            return order;
        };

    private final JdbcTemplate jdbc;

    @Autowired
    public MarketDatabaseRepository(@Qualifier("embeddedJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }

    @Override
    public List<Client> getClients() {
        String query = "SELECT " +
                CLIENT_COLUMNS +
                " FROM \"Client\"";
        return jdbc.query(query, USER_MAPPER);
    }

    @Override
    public List<Product> getProducts() {
        String query = "SELECT " +
                PRODUCT_COLUMNS +
                " FROM Product";
        return jdbc.query(query, PRODUCT_MAPPER);
    }

    @Override
    public List<Order> getOrders() {
        String query = "SELECT " +
                ORDER_COLUMNS + ", " +
                PRODUCT_COLUMNS + ", " +
                CLIENT_COLUMNS +
                " FROM \"Order\" JOIN Product ON \"Order\".product_id = Product.id" +
                " JOIN \"Client\" ON \"Order\".client_id = \"Client\".id";
        return jdbc.query(query, ORDER_MAPPER);
    }
}
