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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class BookingService {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CabRepository cabRepository;

    @Autowired
    private CabSchedularRepository cabSchedularRepository;

    public void onboardCity(CityDetailsDto cityDetailsDto) {
        cityRepository.saveCityDetails(cityDetailsDto);
    }

    public CabDetails registerCab(CabDetailsDto cabDetailsDto) {
        CabDetails cabDetails = cabRepository.saveCabDetails(cabDetailsDto);
        cabSchedularRepository.saveCabSchedularWhileRegister(cabDetails);
        return cabDetails;
    }

    public void bookCab(String cityName) {
        Optional<CityDetails> cityDetailsOptional = cityRepository.getCityByName(cityName);
        if (cityDetailsOptional.isPresent()) {
            Optional<CabSchedular> availableCabSchedular = cabSchedularRepository.getAvailableCabByLocation(cityDetailsOptional.get().getCityId());
            if (availableCabSchedular.isPresent()) {
                cabSchedularRepository.saveCabSchedularWhileCabBooking(availableCabSchedular.get());
                cabRepository.changeCabStatus(availableCabSchedular.get().getCabDetails().getCabId(), null);
            } else {
                throw new RuntimeException("No cab available at " + cityName);
            }
        } else {
            throw new RuntimeException("Invalid location");
        }
    }

    public void endTrip(Long cabId, String cityName) {
        Optional<CityDetails> cityDetailsOptional = cityRepository.getCityByName(cityName);
        if (cityDetailsOptional.isPresent()) {
            cabSchedularRepository.saveCabSchedularAfterTripEnd(cabId, cityDetailsOptional.get());
            cabRepository.changeCabStatus(cabId, cityDetailsOptional.get());
        } else {
            throw new RuntimeException("Invalid location");
        }
    }

    public List<CityDetails> getCityList() {
        return cityRepository.getAll();
    }

    public List<CabSnapshotDto> getCabSnapshot() {
        List<CabSnapshotDto> cabSnapshotDtos = new ArrayList<>();
        List<CabDetails> cabDetailList = cabRepository.getAll();
        cabDetailList.stream().forEach(cabDetails -> {
            CabSnapshotDto cabSnapshotDto = new CabSnapshotDto();
            cabSnapshotDto.setCabId(cabDetails.getCabId());
            cabSnapshotDto.setCabState(cabDetails.getCabStatus());
            cabSnapshotDto.setCityId(cabDetails.getCityDetails() != null ? cabDetails.getCityDetails().getCityId() : null);
            cabSnapshotDtos.add(cabSnapshotDto);
        });
        return cabSnapshotDtos;
    }

    public List<CabIdleDto> getCabSchedularIdleList() {
        Map<Long, Long> cabIdleTimeMap = new HashMap<>();
        List<CabIdleDto> cabIdleDtos = new ArrayList<>();
        List<CabSchedular> cabSchedularList = cabSchedularRepository.getAll();
        cabSchedularList.stream().forEach(cabSchedular -> {
            Long idleMillis;
            if (cabIdleTimeMap.containsKey(cabSchedular.getCabDetails().getCabId())
                    && cabSchedular.getCabStatus().equals(Constant.CabStatusEnum.IDLE.getCabStatus())) {
                idleMillis = cabIdleTimeMap.get(cabSchedular.getCabDetails().getCabId());
            } else {
                idleMillis = 0L;
            }
            if (cabSchedular.getEndTime() != null) {
                idleMillis += ChronoUnit.MILLIS.between(cabSchedular.getStartTime(), cabSchedular.getEndTime());
            } else {
                idleMillis += ChronoUnit.MILLIS.between(cabSchedular.getStartTime(), LocalDateTime.now());
            }
            cabIdleTimeMap.put(cabSchedular.getCabDetails().getCabId(), idleMillis);
        });
        cabIdleTimeMap.forEach((k, v) -> {
            CabIdleDto cabIdleDto = new CabIdleDto();
            cabIdleDto.setCabId(k);
            cabIdleDto.setIdleMillis(v);
            cabIdleDtos.add(cabIdleDto);
        });
        return cabIdleDtos;
    }
}
