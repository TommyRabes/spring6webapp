package mg.tommy.springboot.springbootwebapp.mapper;

import mg.tommy.springboot.springbootwebapp.model.domain.embedded.Beer;
import mg.tommy.springboot.springbootwebapp.model.domain.embedded.BeerStyle;
import mg.tommy.springboot.springbootwebapp.model.dto.BeerDto;
import mg.tommy.springboot.springbootwebapp.model.dto.BeerRecord;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.UUID;

@Mapper(uses = BeerPropertyMapping.class)
public interface BeerMapper {

    Beer toBeer(BeerDto dto);


    @Mapping(target = "id", ignore = true)
    @Mapping(source = "beer", target = "beerName", qualifiedByName = { "BeerPropertyMapping", "AbbreviateBeerName" })
    @Mapping(source = "style", target = "beerStyle", qualifiedByName = { "BeerPropertyMapping", "ConvertBeerStyle" })
    @Mapping(source = "row", target = "upc", qualifiedByName = { "BeerPropertyMapping", "UpcFormat" })
    @Mapping(source = "ounces", target = "price")
    @Mapping(source = "count", target = "quantityOnHand")
    Beer toBeer(BeerRecord record);

    BeerDto toBeerDto(Beer beer);

    default BigDecimal toPrice(Float ounces) {
        return new BigDecimal(ounces * (1. + Math.random() / 2.));
    }

}
