package com.machinecoding.cabmanagement.dao;

import com.machinecoding.cabmanagement.dto.CabDetailsDto;
import com.machinecoding.cabmanagement.dto.CityDetailsDto;
import com.machinecoding.cabmanagement.entity.CabDetails;
import com.machinecoding.cabmanagement.entity.CityDetails;
import com.machinecoding.cabmanagement.util.Constant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CabRepositoryTest {

    @Autowired
    private CabRepository cabRepository;

    @Autowired
    private CityRepository cityRepository;

    @Test
    @DisplayName("Test to save cab details")
    public void saveCabDetailsTest1() {
        CityDetailsDto cityDetailsDto = new CityDetailsDto();
        cityDetailsDto.setCityName("Pune");
        cityRepository.saveCityDetails(cityDetailsDto);

        CabDetailsDto cabDetailsDto = new CabDetailsDto();
        cabDetailsDto.setRegNumber("MH12KH1023");
        cabDetailsDto.setCityName("Pune");
        CabDetails savedDetails = cabRepository.saveCabDetails(cabDetailsDto);

        assertNotNull(savedDetails.getCabId(), "Cab Details not saved");
    }

    @Test
    @DisplayName("Test to save cab details when exception is thrown")
    public void saveCabDetailsTest2() {
        CabDetailsDto cabDetailsDto = new CabDetailsDto();
        cabDetailsDto.setRegNumber("MH12KH1023");
        cabDetailsDto.setCityName("Kanpur");
        RuntimeException thrown = Assertions
                .assertThrows(RuntimeException.class, () -> {
                    cabRepository.saveCabDetails(cabDetailsDto);
                }, "RuntimeException error was expected");

        assertEquals("Invalid city", thrown.getMessage());
    }

    @Test
    @DisplayName("Test to change cab status when booking is done")
    public void changeCabStatusTest1() {
        CityDetailsDto cityDetailsDto = new CityDetailsDto();
        cityDetailsDto.setCityName("Mumbai");
        cityRepository.saveCityDetails(cityDetailsDto);

        CabDetailsDto cabDetailsDto = new CabDetailsDto();
        cabDetailsDto.setRegNumber("MH14KH1223");
        cabDetailsDto.setCityName("Mumbai");
        CabDetails savedDetails = cabRepository.saveCabDetails(cabDetailsDto);

        cabRepository.changeCabStatus(savedDetails.getCabId(), null);

        Optional<CabDetails> cabDetailsOptional = cabRepository.findById(savedDetails.getCabId());
        assertTrue(cabDetailsOptional.isPresent());
        assertEquals(Constant.CabStatusEnum.ON_TRIP, cabDetailsOptional.get().getCabStatus());
    }

    @Test
    @DisplayName("Test to change cab status when trip is complete")
    public void changeCabStatusTest2() {
        CityDetailsDto cityDetailsDto = new CityDetailsDto();
        cityDetailsDto.setCityName("Pune");
        CityDetails cityDetails = cityRepository.saveCityDetails(cityDetailsDto);

        CabDetailsDto cabDetailsDto = new CabDetailsDto();
        cabDetailsDto.setRegNumber("MH12KH1023");
        cabDetailsDto.setCityName("Pune");
        CabDetails savedDetails = cabRepository.saveCabDetails(cabDetailsDto);

        cabRepository.changeCabStatus(savedDetails.getCabId(), cityDetails);

        Optional<CabDetails> cabDetailsOptional = cabRepository.findById(savedDetails.getCabId());
        assertTrue(cabDetailsOptional.isPresent());
        assertEquals(Constant.CabStatusEnum.IDLE, cabDetailsOptional.get().getCabStatus());
    }

    @Test
    @DisplayName("Find by id success")
    public void findByIdTest1() {
        CityDetailsDto cityDetailsDto = new CityDetailsDto();
        cityDetailsDto.setCityName("Pune");
        cityRepository.saveCityDetails(cityDetailsDto);

        CabDetailsDto cabDetailsDto = new CabDetailsDto();
        cabDetailsDto.setRegNumber("MH12KH1023");
        cabDetailsDto.setCityName("Pune");
        CabDetails savedDetails = cabRepository.saveCabDetails(cabDetailsDto);

        Optional<CabDetails> cabDetailsOptional = cabRepository.findById(savedDetails.getCabId());
        assertTrue(cabDetailsOptional.isPresent(), "Cab details not found");
    }

    @Test
    @DisplayName("Find by id not success")
    public void findByIdTest2() {
        Optional<CabDetails> cabDetailsOptional = cabRepository.findById(100L);
        assertFalse(cabDetailsOptional.isPresent(), "Cab details found");
    }
}
