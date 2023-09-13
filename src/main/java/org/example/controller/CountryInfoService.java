package org.example.controller;

import com.google.gson.Gson;
import org.example.model.Countries;
import org.example.model.Country;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class CountryInfoService {

    // Retrieve country data from the REST Countries API
    public Countries retrieveCountriesData() {
        try {
            // Filter in URL that way, saving memory for the things going to fetch/store
            URL url = new URL("https://restcountries.com/v3.1/all?fields=name,population,borders,region,cioc,area");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                response.append("{ \"countryList\":");
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                response.append("}");

                return new Gson().fromJson(response.toString(), Countries.class);
            } else {
                System.err.println("Failed to retrieve country data. HTTP Error: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Find the country with the most bordering countries from a different region
    public Country findCountryWithMostBorderingDifferentRegion(List<Country> countryList, String region) {

        Country asianCountryWithMostBorderingDifferentRegion = null;
        int maxBorderingDifferentRegionCount = 0;

        for (Country country: countryList) {

            if (country.getRegion().equalsIgnoreCase(region)) {
                List<String> borders = country.getBorders();
                int borderingDifferentRegionCount = 0;

                for (String borderingCountryCode: borders) {
                    Country borderingCountry = findCountryByCode(countryList, borderingCountryCode);

                    if (borderingCountry != null && !borderingCountry.getRegion().equalsIgnoreCase(region)) {
                        borderingDifferentRegionCount++;
                    }
                }

                if (borderingDifferentRegionCount > maxBorderingDifferentRegionCount) {
                    maxBorderingDifferentRegionCount = borderingDifferentRegionCount;
                    asianCountryWithMostBorderingDifferentRegion = country;
                }
            }
        }

        return asianCountryWithMostBorderingDifferentRegion;
    }

    // Find a country by its cioc
    public Country findCountryByCode(List<Country> countryList, String countryCode) {

        return countryList.stream()
                .filter(country -> country.getCioc().equalsIgnoreCase(countryCode))
                .findAny()
                .orElse(null);
    }

    // Sort countries by population density in descending order
    public List<Country> sortCountriesByPopulationDensity(List<Country> countryList) {

        countryList.sort((c1, c2) -> {
            double density1 = c1.getPopulation() / c1.getArea();
            double density2 = c2.getPopulation() / c2.getArea();
            return Double.compare(density2, density1);
        });

        return countryList;
    }
}
