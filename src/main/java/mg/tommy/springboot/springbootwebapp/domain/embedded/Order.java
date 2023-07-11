package mg.tommy.springboot.springbootwebapp.domain.embedded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private Client client;
    private Product product;
    private Integer quantity;
    private BigDecimal totalPrice;
    private String status;
}
