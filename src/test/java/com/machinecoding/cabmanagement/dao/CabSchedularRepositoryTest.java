package com.machinecoding.cabmanagement.dao;

import com.machinecoding.cabmanagement.dto.CabDetailsDto;
import com.machinecoding.cabmanagement.dto.CityDetailsDto;
import com.machinecoding.cabmanagement.entity.CabDetails;
import com.machinecoding.cabmanagement.entity.CabSchedular;
import com.machinecoding.cabmanagement.entity.CityDetails;
import com.machinecoding.cabmanagement.util.Constant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class CabSchedularRepositoryTest {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CabRepository cabRepository;

    @Autowired
    private CabSchedularRepository cabSchedularRepository;

    @Test
    @DisplayName("Test to save cab schedular while register")
    public void saveCabSchedularWhileRegisterTest() {
        CityDetailsDto cityDetailsDto = new CityDetailsDto();
        cityDetailsDto.setCityName("Mumbai");
        cityRepository.saveCityDetails(cityDetailsDto);

        CabDetailsDto cabDetailsDto = new CabDetailsDto();
        cabDetailsDto.setRegNumber("MH14KH1223");
        cabDetailsDto.setCityName("Mumbai");
        CabDetails cabDetails = cabRepository.saveCabDetails(cabDetailsDto);

        cabSchedularRepository.saveCabSchedularWhileRegister(cabDetails);
        Optional<CabSchedular> cabSchedularOptional = cabSchedularRepository.getCabSchedularDetailsByStatus(cabDetails.getCabId(), Constant.CabStatusEnum.IDLE.getCabStatus());
        assertTrue(cabSchedularOptional.isPresent(), "Cab schedular not saved");
    }

    @Test
    @DisplayName("Test to save cab schedular while cab booking")
    public void saveCabSchedularWhileCabBookingTest() {
        CityDetailsDto cityDetailsDto = new CityDetailsDto();
        cityDetailsDto.setCityName("Mumbai");
        cityRepository.saveCityDetails(cityDetailsDto);

        CabDetailsDto cabDetailsDto = new CabDetailsDto();
        cabDetailsDto.setRegNumber("MH14KH1223");
        cabDetailsDto.setCityName("Mumbai");
        CabDetails cabDetails = cabRepository.saveCabDetails(cabDetailsDto);
        cabSchedularRepository.saveCabSchedularWhileRegister(cabDetails);

        Optional<CabSchedular> cabSchedularOptional = cabSchedularRepository.getCabSchedularDetailsByStatus(cabDetails.getCabId(), Constant.CabStatusEnum.IDLE.getCabStatus());
        cabSchedularRepository.saveCabSchedularWhileCabBooking(cabSchedularOptional.get());
        Optional<CabSchedular> cabSchedularOptional1 = cabSchedularRepository.getCabSchedularDetailsByStatus(cabDetails.getCabId(), Constant.CabStatusEnum.IN_TRANSIT.getCabStatus());
        assertTrue(cabSchedularOptional1.isPresent(), "Cab schedular is not in transit");
    }

    @Test
    @DisplayName("Test to save cab schedular after trip")
    public void saveCabSchedularAfterTripEndTest() {
        CityDetailsDto cityDetailsDto = new CityDetailsDto();
        cityDetailsDto.setCityName("Mumbai");
        CityDetails cityDetails = cityRepository.saveCityDetails(cityDetailsDto);

        CabDetailsDto cabDetailsDto = new CabDetailsDto();
        cabDetailsDto.setRegNumber("MH14KH1223");
        cabDetailsDto.setCityName("Mumbai");
        CabDetails cabDetails = cabRepository.saveCabDetails(cabDetailsDto);
        cabSchedularRepository.saveCabSchedularWhileRegister(cabDetails);

        Optional<CabSchedular> cabSchedularOptional = cabSchedularRepository.getCabSchedularDetailsByStatus(cabDetails.getCabId(), Constant.CabStatusEnum.IDLE.getCabStatus());
        cabSchedularRepository.saveCabSchedularWhileCabBooking(cabSchedularOptional.get());

        cabSchedularRepository.saveCabSchedularAfterTripEnd(cabDetails.getCabId(), cityDetails);
        Optional<CabSchedular> cabSchedularOptional1 = cabSchedularRepository.getCabSchedularDetailsByStatus(cabDetails.getCabId(), Constant.CabStatusEnum.IDLE.getCabStatus());
        assertTrue(cabSchedularOptional1.isPresent(), "Cab schedular is back to idle");
    }

    @Test
    @DisplayName("Test to get available cab by location")
    public void getAvailableCabByLocationTest() {
        CityDetailsDto cityDetailsDto = new CityDetailsDto();
        cityDetailsDto.setCityName("Patna");
        CityDetails cityDetails = cityRepository.saveCityDetails(cityDetailsDto);

        CabDetailsDto cabDetailsDto = new CabDetailsDto();
        cabDetailsDto.setRegNumber("MH14KH1223");
        cabDetailsDto.setCityName("Patna");
        CabDetails cabDetails = cabRepository.saveCabDetails(cabDetailsDto);
        cabSchedularRepository.saveCabSchedularWhileRegister(cabDetails);

        Optional<CabSchedular> cabSchedularOptional = cabSchedularRepository.getAvailableCabByLocation(cityDetails.getCityId());
        assertTrue(cabSchedularOptional.isPresent(), "Cab is not available");
    }
}
