package com.machinecoding.cabmanagement.dao;

import com.machinecoding.cabmanagement.dto.CityDetailsDto;
import com.machinecoding.cabmanagement.entity.CityDetails;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CityRepository {

    private Long cityId = 0L;

    private List<CityDetails> cityList = new ArrayList<>();

    public CityDetails saveCityDetails(CityDetailsDto cityDetailsDto) {
        cityId++;
        CityDetails cityDetails = new CityDetails();
        cityDetails.setCityName(cityDetailsDto.getCityName());
        cityDetails.setCityId(cityId);
        cityList.add(cityDetails);
        return cityDetails;
    }

    public Optional<CityDetails> getCityByName(String cityName) {
        return cityList.stream()
                .filter(cityDetails -> cityDetails.getCityName().equals(cityName)).findAny();
    }

    public List<CityDetails> getAll() {
        return cityList;
    }
}
