package com.machinecoding.cabmanagement.service;

import com.machinecoding.cabmanagement.dao.CabRepository;
import com.machinecoding.cabmanagement.dao.CabStatusTrackingRepository;
import com.machinecoding.cabmanagement.dao.CityRepository;
import com.machinecoding.cabmanagement.dto.CabDetailsDto;
import com.machinecoding.cabmanagement.dto.CabIdleDto;
import com.machinecoding.cabmanagement.dto.CabSnapshotDto;
import com.machinecoding.cabmanagement.dto.CityDetailsDto;
import com.machinecoding.cabmanagement.entity.CabDetails;
import com.machinecoding.cabmanagement.entity.CabStatusTracking;
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
    private CabStatusTrackingRepository cabStatusTrackingRepository;

    public void onboardCity(CityDetailsDto cityDetailsDto) {
        cityRepository.saveCityDetails(cityDetailsDto);
    }

    public CabDetails registerCab(CabDetailsDto cabDetailsDto) {
        CabDetails cabDetails = cabRepository.saveCabDetails(cabDetailsDto);
        cabStatusTrackingRepository.saveCabStatusTrackingWhileRegister(cabDetails);
        return cabDetails;
    }

    public void bookCab(String cityName) {
        Optional<CityDetails> cityDetailsOptional = cityRepository.getCityByName(cityName);
        if (cityDetailsOptional.isPresent()) {
            Optional<CabStatusTracking> availableCabOptional = cabStatusTrackingRepository.getAvailableCabByLocation(cityDetailsOptional.get().getCityId());
            if (availableCabOptional.isPresent()) {
                cabStatusTrackingRepository.saveCabStatusTrackingWhileCabBooking(availableCabOptional.get());
                cabRepository.changeCabStatus(availableCabOptional.get().getCabDetails().getCabId(), null);
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
            cabStatusTrackingRepository.saveCabStatusTrackingAfterTripEnd(cabId, cityDetailsOptional.get());
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
            cabSnapshotDto.setCabState(cabDetails.getCabStatus().toString());
            cabSnapshotDto.setCityId(cabDetails.getCityDetails() != null ? cabDetails.getCityDetails().getCityId() : null);
            cabSnapshotDtos.add(cabSnapshotDto);
        });
        return cabSnapshotDtos;
    }

    public List<CabIdleDto> getCabTrackingIdleList() {
        Map<Long, Long> cabIdleTimeMap = new HashMap<>();
        List<CabIdleDto> cabIdleDtos = new ArrayList<>();
        List<CabStatusTracking> cabStatusTrackingList = cabStatusTrackingRepository.getAll();
        cabStatusTrackingList.stream().forEach(cabStatusTracking -> {
            Long idleMillis;
            if (cabIdleTimeMap.containsKey(cabStatusTracking.getCabDetails().getCabId())
                    && cabStatusTracking.getCabStatus().equals(Constant.CabStatusEnum.IDLE.getCabStatus())) {
                idleMillis = cabIdleTimeMap.get(cabStatusTracking.getCabDetails().getCabId());
            } else {
                idleMillis = 0L;
            }
            if (cabStatusTracking.getEndTime() != null) {
                idleMillis += ChronoUnit.MILLIS.between(cabStatusTracking.getStartTime(), cabStatusTracking.getEndTime());
            } else {
                idleMillis += ChronoUnit.MILLIS.between(cabStatusTracking.getStartTime(), LocalDateTime.now());
            }
            cabIdleTimeMap.put(cabStatusTracking.getCabDetails().getCabId(), idleMillis);
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
