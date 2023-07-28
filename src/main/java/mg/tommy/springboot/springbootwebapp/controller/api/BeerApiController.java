package mg.tommy.springboot.springbootwebapp.controller.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.tommy.springboot.springbootwebapp.model.domain.embedded.BeerStyle;
import mg.tommy.springboot.springbootwebapp.model.dto.BeerDto;
import mg.tommy.springboot.springbootwebapp.model.dto.constraint.group.BeerGroup;
import mg.tommy.springboot.springbootwebapp.model.dto.constraint.group.PartialBeerGroup;
import mg.tommy.springboot.springbootwebapp.service.brewing.BeerService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(BeerApiController.ROOT_PATH)
public class BeerApiController {
    public static final String ROOT_PATH = "/api/v1/beers";

    private final BeerService beerService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<BeerDto> findBeers(
            @RequestParam(required = false) String beerName,
            @RequestParam(required = false) BeerStyle beerStyle,
            @RequestParam(defaultValue = "false") boolean showInventory,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize) {
        return beerService.find(beerName, beerStyle, showInventory, pageNumber, pageSize);
    }

    @GetMapping("{uuid}")
    public ResponseEntity<BeerDto> getBeerByUUID(@PathVariable("uuid") UUID uuid) {
        return ResponseEntity.ok()
                .body(beerService.findById(uuid)
                        .orElseThrow(() -> new NotFoundException("Beer with uuid : " + uuid + " not found"))
                );
    }

    @PostMapping
    public ResponseEntity<BeerDto> saveBeer(@Validated(BeerGroup.class) @RequestBody BeerDto beerDto) {
        BeerDto savedBeer = beerService.save(beerDto);
        return ResponseEntity
                .created(URI.create("/api/v1/beers/" + savedBeer.getId().toString()))
                .body(savedBeer);
    }

    @PutMapping("{uuid}")
    public ResponseEntity updateById(@PathVariable("uuid") UUID uuid,
                                     @Validated(BeerGroup.class) @RequestBody BeerDto beerDto) {
        Optional<BeerDto> updatedBeer = beerService.overwriteById(uuid, beerDto);
        if (updatedBeer.isEmpty()) {
            throw new NotFoundException("Beer of id: " + uuid + " not found");
        }
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{uuid}")
    public ResponseEntity patchById(@PathVariable("uuid") UUID uuid,
                                    @Validated(PartialBeerGroup.class) @RequestBody BeerDto beerDto) {
        Optional<BeerDto> updatedBeer = beerService.updateById(uuid, beerDto);
        if (updatedBeer.isEmpty()) {
            throw new NotFoundException("Beer of id: " + uuid + " not found");
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{uuid}")
    public ResponseEntity deleteById(@PathVariable("uuid") UUID uuid) {
        if (!beerService.deleteById(uuid)) {
            throw new NotFoundException("Beer of id: " + uuid + " not found");
        }
        return ResponseEntity.noContent().build();
    }

}
