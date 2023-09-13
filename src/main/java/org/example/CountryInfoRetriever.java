package org.example;

import com.google.gson.Gson;
import org.example.controller.CountryInfoService;
import org.example.model.Countries;
import org.example.model.Country;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class CountryInfoRetriever {

    public static void main(String[] args) {

        CountryInfoService countryInfoService = new CountryInfoService();

        // Retrieve the list of countries from the REST Countries API
        Countries countries = countryInfoService.retrieveCountriesData();

        if (countries != null) {
            // Task 1: Sort countries by population density in descending order
            List<Country> sortedCountriesByDensity = countryInfoService.sortCountriesByPopulationDensity(countries.getCountryList());
            System.out.println("Task 1: Sorted list of countries by population density (descending order):");
            for (Country country : sortedCountriesByDensity) {
                String name = country.getName().getCommon();
                double density = country.getPopulation() / country.getArea();
                System.out.println(name + " - Population Density: " + density);
            }

            // Task 2: Find the Asian country with the most bordering countries from a different region
            Country asianCountry = countryInfoService.findAsianCountryWithMostBorderingDifferentRegion(countries.getCountryList());
            System.out.println("\nTask 2: Asian country with the most bordering countries from a different region:");
            if (asianCountry != null) {
                System.out.println("Country Name: " + asianCountry.getName().getCommon());
                System.out.println("Number of Bordering Countries from Different Region: " + asianCountry.getBorders().size());
            } else {
                System.out.println("No Asian country found with bordering countries from a different region.");
            }
        }
    }






}


