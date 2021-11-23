package com.machinecoding.cabmanagement.dao;

import com.machinecoding.cabmanagement.dto.CabDetailsDto;
import com.machinecoding.cabmanagement.dto.CityDetailsDto;
import com.machinecoding.cabmanagement.entity.CabDetails;
import com.machinecoding.cabmanagement.entity.CabStatusTracking;
import com.machinecoding.cabmanagement.entity.CityDetails;
import com.machinecoding.cabmanagement.util.Constant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class CabStatusTrackingRepositoryTest {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CabRepository cabRepository;

    @Autowired
    private CabStatusTrackingRepository cabStatusTrackingRepository;

    @Test
    @DisplayName("Test to save cab status tracking while register")
    public void saveCabStatusTrackingWhileRegisterTest() {
        CityDetailsDto cityDetailsDto = new CityDetailsDto();
        cityDetailsDto.setCityName("Mumbai");
        cityRepository.saveCityDetails(cityDetailsDto);

        CabDetailsDto cabDetailsDto = new CabDetailsDto();
        cabDetailsDto.setRegNumber("MH14KH1223");
        cabDetailsDto.setCityName("Mumbai");
        CabDetails cabDetails = cabRepository.saveCabDetails(cabDetailsDto);

        cabStatusTrackingRepository.saveCabStatusTrackingWhileRegister(cabDetails);
        Optional<CabStatusTracking> cabStatusTrackingOptional = cabStatusTrackingRepository.getCabStatusTrackingByStatus(cabDetails.getCabId(), Constant.CabStatusEnum.IDLE);
        assertTrue(cabStatusTrackingOptional.isPresent(), "Cab status not saved");
    }

    @Test
    @DisplayName("Test to save cab status tracking while cab booking")
    public void saveCabStatusTrackingWhileCabBookingTest() {
        CityDetailsDto cityDetailsDto = new CityDetailsDto();
        cityDetailsDto.setCityName("Mumbai");
        cityRepository.saveCityDetails(cityDetailsDto);

        CabDetailsDto cabDetailsDto = new CabDetailsDto();
        cabDetailsDto.setRegNumber("MH14KH1223");
        cabDetailsDto.setCityName("Mumbai");
        CabDetails cabDetails = cabRepository.saveCabDetails(cabDetailsDto);
        cabStatusTrackingRepository.saveCabStatusTrackingWhileRegister(cabDetails);

        Optional<CabStatusTracking> cabStatusTrackingOptional = cabStatusTrackingRepository.getCabStatusTrackingByStatus(cabDetails.getCabId(), Constant.CabStatusEnum.IDLE);
        cabStatusTrackingRepository.saveCabStatusTrackingWhileCabBooking(cabStatusTrackingOptional.get());
        Optional<CabStatusTracking> cabStatusTrackingOptional1 = cabStatusTrackingRepository.getCabStatusTrackingByStatus(cabDetails.getCabId(), Constant.CabStatusEnum.ON_TRIP);
        assertTrue(cabStatusTrackingOptional1.isPresent(), "Cab status is not in transit");
    }

    @Test
    @DisplayName("Test to save cab status tracking after trip")
    public void saveCabStatusTrackingAfterTripEndTest() {
        CityDetailsDto cityDetailsDto = new CityDetailsDto();
        cityDetailsDto.setCityName("Mumbai");
        CityDetails cityDetails = cityRepository.saveCityDetails(cityDetailsDto);

        CabDetailsDto cabDetailsDto = new CabDetailsDto();
        cabDetailsDto.setRegNumber("MH14KH1223");
        cabDetailsDto.setCityName("Mumbai");
        CabDetails cabDetails = cabRepository.saveCabDetails(cabDetailsDto);
        cabStatusTrackingRepository.saveCabStatusTrackingWhileRegister(cabDetails);

        Optional<CabStatusTracking> cabStatusTrackingOptional = cabStatusTrackingRepository.getCabStatusTrackingByStatus(cabDetails.getCabId(), Constant.CabStatusEnum.IDLE);
        cabStatusTrackingRepository.saveCabStatusTrackingWhileCabBooking(cabStatusTrackingOptional.get());

        cabStatusTrackingRepository.saveCabStatusTrackingAfterTripEnd(cabDetails.getCabId(), cityDetails);
        Optional<CabStatusTracking> cabStatusTrackingOptional1 = cabStatusTrackingRepository.getCabStatusTrackingByStatus(cabDetails.getCabId(), Constant.CabStatusEnum.IDLE);
        assertTrue(cabStatusTrackingOptional1.isPresent(), "Cab status is back to idle");
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
        cabStatusTrackingRepository.saveCabStatusTrackingWhileRegister(cabDetails);

        Optional<CabStatusTracking> cabStatusTrackingOptional = cabStatusTrackingRepository.getAvailableCabByLocation(cityDetails.getCityId());
        assertTrue(cabStatusTrackingOptional.isPresent(), "Cab is not available");
    }
}
