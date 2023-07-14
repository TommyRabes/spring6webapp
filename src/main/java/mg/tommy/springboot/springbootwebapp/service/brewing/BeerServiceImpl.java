package mg.tommy.springboot.springbootwebapp.service.brewing;

import lombok.RequiredArgsConstructor;
import mg.tommy.springboot.springbootwebapp.domain.embedded.Beer;
import mg.tommy.springboot.springbootwebapp.dto.BeerDto;
import mg.tommy.springboot.springbootwebapp.mapper.BeerMapper;
import mg.tommy.springboot.springbootwebapp.repository.embedded.BeerRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public Iterable<BeerDto> findAll() {
        return beerRepository.findAll()
                .stream()
                .map(beerMapper::toBeerDto)
                .collect(toList());
    }

    @Override
    public Optional<BeerDto> findById(UUID id) {
        return beerRepository.findById(id).map(beerMapper::toBeerDto);
    }

    @Override
    public BeerDto save(BeerDto beer) {
        return beerMapper.toBeerDto(
                beerRepository.save(beerMapper.toBeer(beer).toBuilder()
                        .createdDate(LocalDateTime.now())
                        .updateDate(LocalDateTime.now())
                        .build())
        );
    }

    @Override
    public BeerDto overwriteById(UUID uuid, BeerDto beerDto) {
        Optional<Beer> beerToUpdate = beerRepository.findById(uuid);
        if (beerToUpdate.isEmpty()) {
            return beerMapper.toBeerDto(beerRepository.save(beerMapper.toBeer(beerDto)));
        }
        Beer.BeerBuilder builder = beerToUpdate.get().toBuilder()
                .beerName(beerDto.getBeerName())
                .beerStyle(beerDto.getBeerStyle())
                .price(beerDto.getPrice())
                .quantityOnHand(beerDto.getQuantityOnHand())
                .upc(beerDto.getUpc())
                .updateDate(LocalDateTime.now());
        return beerMapper.toBeerDto(beerRepository.save(builder.build()));
    }

    @Override
    public BeerDto updateById(UUID uuid, BeerDto beerDto) {
        Optional<Beer> beerToUpdate = beerRepository.findById(uuid);
        if (beerToUpdate.isEmpty()) {
            // Ideally throw an exception or return a meaningful response
            return null;
        }
        Beer.BeerBuilder builder = beerToUpdate.get().toBuilder();
        if (StringUtils.hasText(beerDto.getBeerName()))
            builder.beerName(beerDto.getBeerName());
        if (beerDto.getBeerStyle() != null)
            builder.beerStyle(beerDto.getBeerStyle());
        if (beerDto.getPrice() != null)
            builder.price(beerDto.getPrice());
        if (beerDto.getQuantityOnHand() != null)
            builder.quantityOnHand(beerDto.getQuantityOnHand());
        if (StringUtils.hasText(beerDto.getUpc()))
            builder.upc(beerDto.getUpc());
        builder.updateDate(LocalDateTime.now());
        return beerMapper.toBeerDto(beerRepository.save(builder.build()));
    }

    @Override
    public void deleteById(UUID uuid) {
        beerRepository.deleteById(uuid);
    }
}
