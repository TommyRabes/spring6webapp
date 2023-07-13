package mg.tommy.springboot.springbootwebapp.mapper;

import mg.tommy.springboot.springbootwebapp.domain.embedded.Beer;
import mg.tommy.springboot.springbootwebapp.dto.BeerDto;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer toBeer(BeerDto dto);

    BeerDto toBeerDto(Beer beer);
}
