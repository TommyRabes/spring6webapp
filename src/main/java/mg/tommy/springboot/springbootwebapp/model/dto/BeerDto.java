package mg.tommy.springboot.springbootwebapp.model.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import mg.tommy.springboot.springbootwebapp.model.domain.embedded.BeerStyle;
import mg.tommy.springboot.springbootwebapp.model.dto.constraint.group.BeerGroup;
import mg.tommy.springboot.springbootwebapp.model.dto.constraint.group.PartialBeerGroup;
import mg.tommy.springboot.springbootwebapp.model.dto.constraint.validator.ValueOfEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
@Data
public class BeerDto {
    private UUID id;
    private Integer version;

    @Size(min = 3, max = 255, groups = {BeerGroup.class, PartialBeerGroup.class})
    @NotBlank(groups = BeerGroup.class)
    @NotNull(groups = BeerGroup.class)
    private String beerName;

    @ValueOfEnum(enumClass = BeerStyle.class, groups = {BeerGroup.class, PartialBeerGroup.class})
    @NotNull(groups = BeerGroup.class)
    private String beerStyle;

    @Size(min = 3, max = 100, groups = {BeerGroup.class, PartialBeerGroup.class})
    @NotBlank(groups = BeerGroup.class)
    @NotNull(groups = BeerGroup.class)
    private String upc;

    @PositiveOrZero(groups = {BeerGroup.class, PartialBeerGroup.class})
    @NotNull(groups = BeerGroup.class)
    private BigDecimal price;

    @PositiveOrZero(groups = {BeerGroup.class, PartialBeerGroup.class})
    private Integer quantityOnHand;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
