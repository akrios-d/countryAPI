package org.example;

import com.google.gson.Gson;
import org.assertj.core.api.Assertions;
import org.example.controller.CountryInfoService;
import org.example.model.Countries;
import org.example.model.Country;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static junit.framework.TestCase.assertTrue;


@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class CountryInfoRetrieverTest {

    Countries countries;
    CountryInfoService countryInfoService;

    @Before
    public void setup() {
        countryInfoService = new CountryInfoService();
        countries = new Gson().fromJson(ConstantsUtil.countryList, Countries.class);
    }

    @Test
    public void sortCountriesByPopulationDensity() {

        List<Country> countryList = countryInfoService.sortCountriesByPopulationDensity(countries.getCountryList());

        Country previous = countryList.get(0);
        countryList.remove(0);

        for (Country element : countryList) {

            double densityPrevious = previous.getPopulation() / previous.getArea();
            double densityElement = element.getPopulation() / element.getArea();
            assertTrue(densityPrevious > densityElement);
            previous = element;
        }
    }


    @Test
    public void findAsianCountryWithMostBorderingDifferentRegion() {

        Countries countriesAfrica = new Gson().fromJson(ConstantsUtil.africaCountryList, Countries.class);

        countries.getCountryList().addAll(countriesAfrica.getCountryList());

        Country response = countryInfoService.findCountryWithMostBorderingDifferentRegion(countries.getCountryList(),"Asia");

        Assertions.assertThat(response.getBorders()).hasSize(3);
        Assertions.assertThat(response.getName().getCommon()).isEqualTo("Palestine");
    }

    @Test
    public void findCountryByCode() {

        Country response = countryInfoService.findCountryByCode(countries.getCountryList(),"KSA");

        Assertions.assertThat(response.getName().getCommon()).isEqualTo("Saudi Arabia");
        Assertions.assertThat(response.getName().getOfficial()).isEqualTo("Kingdom of Saudi Arabia");
        Assertions.assertThat(response.getCioc()).isEqualTo("KSA");
        Assertions.assertThat(response.getRegion()).isEqualTo("Asia");
        Assertions.assertThat(response.getBorders()).hasSize(7);
    }

    @Test
    public void findCountryByCodeFailWithNull() {

        Country response = countryInfoService.findCountryByCode(countries.getCountryList(),"PAR");

        Assertions.assertThat(response).isNull();
    }

}