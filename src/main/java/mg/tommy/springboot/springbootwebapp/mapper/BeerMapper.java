package mg.tommy.springboot.springbootwebapp.mapper;

import mg.tommy.springboot.springbootwebapp.model.domain.embedded.Beer;
import mg.tommy.springboot.springbootwebapp.model.dto.BeerDto;
import mg.tommy.springboot.springbootwebapp.model.dto.BeerRecord;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper
public interface BeerMapper {

    Beer toBeer(BeerDto dto);

    Beer toBeer(BeerRecord record);

    BeerDto toBeerDto(Beer beer);

    default UUID map(Integer id) {
        return UUID.randomUUID();
    }
}
