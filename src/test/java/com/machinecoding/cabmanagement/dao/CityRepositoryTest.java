package com.machinecoding.cabmanagement.dao;

import com.machinecoding.cabmanagement.dto.CityDetailsDto;
import com.machinecoding.cabmanagement.entity.CityDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CityRepositoryTest {

    @Autowired
    private CityRepository cityRepository;

    @Test
    @DisplayName("Test for save city details")
    public void saveCityDetailsTest() {
        CityDetailsDto cityDetailsDto = new CityDetailsDto();
        cityDetailsDto.setCityName("Pune");
        CityDetails savedDetails = cityRepository.saveCityDetails(cityDetailsDto);

        assertNotNull(savedDetails.getCityId(), "City Details Not Saved");
    }

    @Test
    @DisplayName("Test for get city by name success")
    public void getCityByNameTest1() {
        CityDetailsDto cityDetailsDto = new CityDetailsDto();
        cityDetailsDto.setCityName("Pune");
        cityRepository.saveCityDetails(cityDetailsDto);

        Optional<CityDetails> cityDetailsOptional = cityRepository.getCityByName(cityDetailsDto.getCityName());
        assertTrue(cityDetailsOptional.isPresent(), "City Not Present");
    }

    @Test
    @DisplayName("Test for get city by name not success")
    public void getCityByNameTest2() {
        Optional<CityDetails> cityDetailsOptional = cityRepository.getCityByName("Goa");
        assertFalse(cityDetailsOptional.isPresent(), "City is Present");
    }
}
