package com.machinecoding.cabmanagement.service;

import com.machinecoding.cabmanagement.dao.CabRepository;
import com.machinecoding.cabmanagement.dao.CabSchedularRepository;
import com.machinecoding.cabmanagement.dao.CityRepository;
import com.machinecoding.cabmanagement.dto.CabDetailsDto;
import com.machinecoding.cabmanagement.dto.CabIdleDto;
import com.machinecoding.cabmanagement.dto.CabSnapshotDto;
import com.machinecoding.cabmanagement.dto.CityDetailsDto;
import com.machinecoding.cabmanagement.entity.CabDetails;
import com.machinecoding.cabmanagement.entity.CabSchedular;
import com.machinecoding.cabmanagement.entity.CityDetails;
import com.machinecoding.cabmanagement.util.Constant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookingServiceTest {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CabRepository cabRepository;

    @Autowired
    private CabSchedularRepository cabSchedularRepository;

    @Autowired
    private BookingService bookingService;

    @Test
    @DisplayName("Test to onboard city")
    public void onboardCityTest() {
        CityDetailsDto cityDetailsDto = new CityDetailsDto();
        cityDetailsDto.setCityName("Pune");
        bookingService.onboardCity(cityDetailsDto);

        Optional<CityDetails> cityDetailsOptional = cityRepository.getCityByName(cityDetailsDto.getCityName());

        assertTrue(cityDetailsOptional.isPresent(), "City is not onboard");
    }

    @Test
    @DisplayName("Test to register cab")
    public void registerCab() {
        CityDetailsDto cityDetailsDto = new CityDetailsDto();
        cityDetailsDto.setCityName("Mumbai");
        cityRepository.saveCityDetails(cityDetailsDto);

        CabDetailsDto cabDetailsDto = new CabDetailsDto();
        cabDetailsDto.setRegNumber("MH14KH1223");
        cabDetailsDto.setCityName("Mumbai");
        CabDetails cabDetails = bookingService.registerCab(cabDetailsDto);

        Optional<CabSchedular>  cabSchedularOptional = cabSchedularRepository.getCabSchedularDetailsByStatus(cabDetails.getCabId(), cabDetails.getCabStatus());

        assertTrue(cabSchedularOptional.isPresent(), "Cab is registered");
    }

    @Test
    @DisplayName("Test to book cab when successful")
    public void bookCabTest1() {
        CityDetailsDto cityDetailsDto = new CityDetailsDto();
        cityDetailsDto.setCityName("Mumbai");
        cityRepository.saveCityDetails(cityDetailsDto);

        CabDetailsDto cabDetailsDto = new CabDetailsDto();
        cabDetailsDto.setRegNumber("MH14KH1223");
        cabDetailsDto.setCityName("Mumbai");
        CabDetails cabDetails = bookingService.registerCab(cabDetailsDto);

        bookingService.bookCab("Mumbai");
        Optional<CabSchedular>  cabSchedularOptional = cabSchedularRepository.getCabSchedularDetailsByStatus(cabDetails.getCabId(),
                Constant.CabStatusEnum.IN_TRANSIT.getCabStatus());
        assertTrue(cabSchedularOptional.isPresent(), "Cab not booked successfully");
    }

    @Test
    @DisplayName("Test to book cab when failed due to invalid location")
    public void bookCabTest2() {
        CityDetailsDto cityDetailsDto = new CityDetailsDto();
        cityDetailsDto.setCityName("Pune");
        cityRepository.saveCityDetails(cityDetailsDto);

        RuntimeException thrown = Assertions
                .assertThrows(RuntimeException.class, () -> {
                    bookingService.bookCab("Ranchi");
                }, "RuntimeException error was expected");
        assertEquals("Invalid location", thrown.getMessage());
    }

    @Test
    @DisplayName("Test to book cab when failed due to no available cab")
    public void bookCabTest3() {
        CityDetailsDto cityDetailsDto = new CityDetailsDto();
        cityDetailsDto.setCityName("Goa");
        cityRepository.saveCityDetails(cityDetailsDto);

        RuntimeException thrown = Assertions
                .assertThrows(RuntimeException.class, () -> {
                    bookingService.bookCab("Goa");
                }, "RuntimeException error was expected");
        assertEquals("No cab available at Goa", thrown.getMessage());
    }

    @Test
    @DisplayName("Test to end trip when successful")
    public void endTripTest1() {
        CityDetailsDto cityDetailsDto = new CityDetailsDto();
        cityDetailsDto.setCityName("Mumbai");
        cityRepository.saveCityDetails(cityDetailsDto);

        CabDetailsDto cabDetailsDto = new CabDetailsDto();
        cabDetailsDto.setRegNumber("MH14KH1223");
        cabDetailsDto.setCityName("Mumbai");
        CabDetails cabDetails = bookingService.registerCab(cabDetailsDto);
        bookingService.bookCab("Mumbai");

        bookingService.endTrip(cabDetails.getCabId(), "Mumbai");
        Optional<CabSchedular>  cabSchedularOptional = cabSchedularRepository.getCabSchedularDetailsByStatus(cabDetails.getCabId(),
                Constant.CabStatusEnum.IDLE.getCabStatus());
        assertTrue(cabSchedularOptional.isPresent(), "Trip does not end");
    }

    @Test
    @DisplayName("Test to end trip when failed")
    public void endTripTest2() {
        CityDetailsDto cityDetailsDto = new CityDetailsDto();
        cityDetailsDto.setCityName("Mumbai");
        cityRepository.saveCityDetails(cityDetailsDto);

        CabDetailsDto cabDetailsDto = new CabDetailsDto();
        cabDetailsDto.setRegNumber("MH14KH1223");
        cabDetailsDto.setCityName("Mumbai");
        CabDetails cabDetails = bookingService.registerCab(cabDetailsDto);
        bookingService.bookCab("Mumbai");

        RuntimeException thrown = Assertions
                .assertThrows(RuntimeException.class, () -> {
                    bookingService.endTrip(cabDetails.getCabId(), "Goa");
                }, "RuntimeException error was expected");
        assertEquals("Invalid location", thrown.getMessage());
    }
}
