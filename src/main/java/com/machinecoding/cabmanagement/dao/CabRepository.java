package com.machinecoding.cabmanagement.dao;

import com.machinecoding.cabmanagement.dto.CabDetailsDto;
import com.machinecoding.cabmanagement.entity.CabDetails;
import com.machinecoding.cabmanagement.entity.CityDetails;
import com.machinecoding.cabmanagement.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CabRepository {

    private Long cabId = 0L;

    private List<CabDetails> cabDetailsList = new ArrayList<>();

    @Autowired
    private CityRepository cityRepository;

    public Optional<CabDetails> findById(Long cabId) {
        return cabDetailsList.stream().filter(cabDetails -> cabDetails.getCabId().equals(cabId)).findAny();
    }

    public void changeCabStatus(Long cabId, CityDetails cityDetails) {
        Optional<CabDetails> cabDetailsOptional = cabDetailsList.stream().filter(cabDetails -> cabDetails.getCabId().equals(cabId)).findAny();
        if (cabDetailsOptional.isPresent()) {
            CabDetails cabDetails = cabDetailsOptional.get();
            if (cityDetails == null) {
                cabDetails.setCabStatus(Constant.CabStatusEnum.IN_TRANSIT.getCabStatus());
            } else {
                cabDetails.setCabStatus(Constant.CabStatusEnum.IDLE.getCabStatus());
                cabDetails.setCityDetails(cityDetails);
            }
        }
    }

    public CabDetails saveCabDetails(CabDetailsDto cabDetailsDto) {
        cabId++;
        CabDetails cabDetails = new CabDetails();
        cabDetails.setCabId(cabId);
        cabDetails.setCabRegNumber(cabDetailsDto.getRegNumber());
        cabDetails.setCabStatus(Constant.CabStatusEnum.IDLE.getCabStatus());
        Optional<CityDetails> cityDetailsOptional = cityRepository.getCityByName(cabDetailsDto.getCityName());
        if (!cityDetailsOptional.isPresent()) {
            throw new RuntimeException("Invalid city");
        } else {
            cabDetails.setCityDetails(cityDetailsOptional.get());
        }
        cabDetailsList.add(cabDetails);
        return cabDetails;
    }

    public List<CabDetails> getAll() {
        return cabDetailsList;
    }
}
