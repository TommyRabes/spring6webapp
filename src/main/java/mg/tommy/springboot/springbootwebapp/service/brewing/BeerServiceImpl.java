package mg.tommy.springboot.springbootwebapp.service.brewing;

import lombok.RequiredArgsConstructor;
import mg.tommy.springboot.springbootwebapp.domain.embedded.Beer;
import mg.tommy.springboot.springbootwebapp.domain.embedded.BeerStyle;
import mg.tommy.springboot.springbootwebapp.dto.BeerDto;
import mg.tommy.springboot.springbootwebapp.mapper.BeerMapper;
import mg.tommy.springboot.springbootwebapp.repository.embedded.BeerRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

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
    public Optional<BeerDto> overwriteById(UUID uuid, BeerDto beerDto) {
        AtomicReference<Optional<BeerDto>> atomicReference = new AtomicReference<>();
        beerRepository.findById(uuid)
                .ifPresentOrElse(beerToUpdate -> {
                    Beer updatedBeer = beerRepository.save(beerToUpdate.toBuilder()
                            .beerName(beerDto.getBeerName())
                            .beerStyle(BeerStyle.valueOf(beerDto.getBeerStyle()))
                            .price(beerDto.getPrice())
                            .quantityOnHand(beerDto.getQuantityOnHand())
                            .upc(beerDto.getUpc())
                            .updateDate(LocalDateTime.now())
                            .build());
                    atomicReference.set(Optional.of(beerMapper.toBeerDto(updatedBeer)));
                }, () -> {
                    atomicReference.set(Optional.empty());
                });
        return atomicReference.get();
    }

    @Override
    public Optional<BeerDto> updateById(UUID uuid, BeerDto beerDto) {
        Optional<Beer> beerToUpdate = beerRepository.findById(uuid);
        if (beerToUpdate.isEmpty()) {
            return Optional.empty();
        }
        Beer.BeerBuilder builder = beerToUpdate.get().toBuilder();
        if (StringUtils.hasText(beerDto.getBeerName()))
            builder.beerName(beerDto.getBeerName());
        if (beerDto.getBeerStyle() != null)
            builder.beerStyle(BeerStyle.valueOf(beerDto.getBeerStyle()));
        if (beerDto.getPrice() != null)
            builder.price(beerDto.getPrice());
        if (beerDto.getQuantityOnHand() != null)
            builder.quantityOnHand(beerDto.getQuantityOnHand());
        if (StringUtils.hasText(beerDto.getUpc()))
            builder.upc(beerDto.getUpc());
        builder.updateDate(LocalDateTime.now());
        return Optional.ofNullable(beerMapper.toBeerDto(beerRepository.save(builder.build())));
    }

    @Override
    public boolean deleteById(UUID uuid) {
        if (!beerRepository.existsById(uuid))
            return false;

        beerRepository.deleteById(uuid);
        return true;
    }
}
