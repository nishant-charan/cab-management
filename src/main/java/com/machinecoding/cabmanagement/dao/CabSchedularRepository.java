package com.machinecoding.cabmanagement.dao;

import com.machinecoding.cabmanagement.entity.CabDetails;
import com.machinecoding.cabmanagement.entity.CabSchedular;
import com.machinecoding.cabmanagement.entity.CityDetails;
import com.machinecoding.cabmanagement.util.Constant;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
public class CabSchedularRepository {

    private List<CabSchedular> cabSchedularList = new ArrayList<>();

    public Optional<CabSchedular> getCabSchedularDetailsByStatus(Long cabId, String status) {
         return cabSchedularList.stream().filter(cabSchedular ->
                cabSchedular.getCabDetails().getCabId().equals(cabId)
                        && cabSchedular.getCabStatus().equals(status))
                .findAny();
    }

    public void saveCabSchedularWhileRegister(CabDetails cabDetails) {
        CabSchedular cabSchedular = new CabSchedular();
        cabSchedular.setCabDetails(cabDetails);
        cabSchedular.setCabStatus(Constant.CabStatusEnum.IDLE.getCabStatus());
        cabSchedular.setStartTime(LocalDateTime.now());
        cabSchedular.setCityDetails(cabDetails.getCityDetails());
        cabSchedularList.add(cabSchedular);
    }

    public void saveCabSchedularWhileCabBooking(CabSchedular cabSchedular) {
        cabSchedular.setEndTime(LocalDateTime.now());

        CabSchedular newCabSchedular = new CabSchedular();
        newCabSchedular.setCabDetails(cabSchedular.getCabDetails());
        newCabSchedular.setCabStatus(Constant.CabStatusEnum.IN_TRANSIT.getCabStatus());
        newCabSchedular.setStartTime(LocalDateTime.now());
        cabSchedularList.add(newCabSchedular);
    }

    public void saveCabSchedularAfterTripEnd(Long cabId, CityDetails destinationCityDetails) {
        Optional<CabSchedular> cabSchedularOptional = getCabSchedularDetailsByStatus(cabId, Constant.CabStatusEnum.IN_TRANSIT.getCabStatus());
        if (cabSchedularOptional.isPresent()) {
            CabSchedular cabSchedular = cabSchedularOptional.get();
            cabSchedular.setEndTime(LocalDateTime.now());
        }
        CabSchedular newCabSchedular = new CabSchedular();
        newCabSchedular.setCabDetails(cabSchedularOptional.get().getCabDetails());
        newCabSchedular.setCabStatus(Constant.CabStatusEnum.IDLE.getCabStatus());
        newCabSchedular.setStartTime(LocalDateTime.now());
        newCabSchedular.setCityDetails(destinationCityDetails);
        cabSchedularList.add(newCabSchedular);
    }

    public Optional<CabSchedular> getAvailableCabByLocation(Long cityId) {
        return cabSchedularList.stream().filter(cabSchedular -> cabSchedular.getCityDetails() != null
                                && cabSchedular.getCityDetails().getCityId().equals(cityId)
                                && cabSchedular.getCabStatus().equals(Constant.CabStatusEnum.IDLE.getCabStatus())
                                && cabSchedular.getEndTime() == null)
                .max(Comparator.comparing(CabSchedular::getStartTime));
    }

    public List<CabSchedular> getAll() {
        return cabSchedularList;
    }
}
