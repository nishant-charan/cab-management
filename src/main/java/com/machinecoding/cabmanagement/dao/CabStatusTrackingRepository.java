package com.machinecoding.cabmanagement.dao;

import com.machinecoding.cabmanagement.entity.CabDetails;
import com.machinecoding.cabmanagement.entity.CabStatusTracking;
import com.machinecoding.cabmanagement.entity.CityDetails;
import com.machinecoding.cabmanagement.util.Constant;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
public class CabStatusTrackingRepository {

    private List<CabStatusTracking> cabStatusTrackingList = new ArrayList<>();

    public Optional<CabStatusTracking> getCabStatusTrackingByStatus(Long cabId, Enum status) {
         return cabStatusTrackingList.stream().filter(cabStatusTracking ->
                cabStatusTracking.getCabDetails().getCabId().equals(cabId)
                        && cabStatusTracking.getCabStatus().equals(status))
                .findAny();
    }

    public void saveCabStatusTrackingWhileRegister(CabDetails cabDetails) {
        CabStatusTracking cabStatusTracking = new CabStatusTracking();
        cabStatusTracking.setCabDetails(cabDetails);
        cabStatusTracking.setCabStatus(Constant.CabStatusEnum.IDLE);
        cabStatusTracking.setStartTime(LocalDateTime.now());
        cabStatusTracking.setCityDetails(cabDetails.getCityDetails());
        cabStatusTrackingList.add(cabStatusTracking);
    }

    public void saveCabStatusTrackingWhileCabBooking(CabStatusTracking cabStatusTracking) {
        cabStatusTracking.setEndTime(LocalDateTime.now());

        CabStatusTracking newCabStatusTracking = new CabStatusTracking();
        newCabStatusTracking.setCabDetails(cabStatusTracking.getCabDetails());
        newCabStatusTracking.setCabStatus(Constant.CabStatusEnum.ON_TRIP);
        newCabStatusTracking.setStartTime(LocalDateTime.now());
        cabStatusTrackingList.add(newCabStatusTracking);
    }

    public void saveCabStatusTrackingAfterTripEnd(Long cabId, CityDetails destinationCityDetails) {
        Optional<CabStatusTracking> cabStatusTrackingOptional = getCabStatusTrackingByStatus(cabId, Constant.CabStatusEnum.ON_TRIP);
        if (cabStatusTrackingOptional.isPresent()) {
            CabStatusTracking cabStatusTracking = cabStatusTrackingOptional.get();
            cabStatusTracking.setEndTime(LocalDateTime.now());
        }
        CabStatusTracking newCabStatusTracking = new CabStatusTracking();
        newCabStatusTracking.setCabDetails(cabStatusTrackingOptional.get().getCabDetails());
        newCabStatusTracking.setCabStatus(Constant.CabStatusEnum.IDLE);
        newCabStatusTracking.setStartTime(LocalDateTime.now());
        newCabStatusTracking.setCityDetails(destinationCityDetails);
        cabStatusTrackingList.add(newCabStatusTracking);
    }

    public Optional<CabStatusTracking> getAvailableCabByLocation(Long cityId) {
        return cabStatusTrackingList.stream().filter(cabStatusTracking -> cabStatusTracking.getCityDetails() != null
                                && cabStatusTracking.getCityDetails().getCityId().equals(cityId)
                                && cabStatusTracking.getCabStatus().equals(Constant.CabStatusEnum.IDLE)
                                && cabStatusTracking.getEndTime() == null)
                .max(Comparator.comparing(CabStatusTracking::getStartTime));
    }

    public List<CabStatusTracking> getAll() {
        return cabStatusTrackingList;
    }
}
