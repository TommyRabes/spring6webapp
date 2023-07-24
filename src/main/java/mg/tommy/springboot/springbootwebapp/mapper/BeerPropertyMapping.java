package mg.tommy.springboot.springbootwebapp.mapper;

import mg.tommy.springboot.springbootwebapp.model.domain.embedded.BeerStyle;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@Named("BeerPropertyMapping")
public class BeerPropertyMapping {

    @Named("AbbreviateBeerName")
    public String abbreviateBeerName(String beerName) {
        return StringUtils.abbreviate(beerName, 255);
    }

    @Named("UpcFormat")
    public String upcFormat(Integer row) {
        return "0".repeat(6 - row.toString().length()) + row;
    }

    /**
     * Although the type mapping is quite straightforward, we need to provide a qualifier
     * for MapStruct to know it has to invoke the Beer.BeerBuilder#beerStyle(BeerStyle) method
     * not the Beer.BeerBuilder#beerStyle(String)
     */
    @Named("ConvertBeerStyle")
    public BeerStyle map(String style) {
        String lowerCaseStyle = style.toLowerCase();
        if (lowerCaseStyle.contains("wheat"))
            return BeerStyle.WHEAT;
        else if (lowerCaseStyle.contains("saison"))
            return BeerStyle.SAISON;
        else if (lowerCaseStyle.contains("gose"))
            return BeerStyle.GOSE;
        else if (lowerCaseStyle.contains("stout"))
            return BeerStyle.STOUT;
        else if (lowerCaseStyle.contains("lager"))
            return BeerStyle.LAGER;
        else if (lowerCaseStyle.contains("porter"))
            return BeerStyle.PORTER;
        else if (lowerCaseStyle.contains("ipa"))
            return BeerStyle.IPA;
        else if (lowerCaseStyle.contains("pale"))
            return BeerStyle.PALE_ALE;
        else if (lowerCaseStyle.contains("ale"))
            return BeerStyle.ALE;
        else
            return BeerStyle.PILSNER;
    }

}
