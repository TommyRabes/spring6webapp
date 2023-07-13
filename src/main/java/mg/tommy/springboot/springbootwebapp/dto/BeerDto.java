package mg.tommy.springboot.springbootwebapp.dto;

import lombok.Builder;
import lombok.Data;
import mg.tommy.springboot.springbootwebapp.domain.embedded.BeerStyle;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
@Data
public class BeerDto {
    private UUID id;
    private Integer version;
    private String beerName;
    private BeerStyle beerStyle;
    private String upc;
    private BigDecimal price;
    private Integer quantityOnHand;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
