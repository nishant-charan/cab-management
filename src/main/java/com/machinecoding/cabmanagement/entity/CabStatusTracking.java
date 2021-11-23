package com.machinecoding.cabmanagement.entity;

import java.time.LocalDateTime;

public class CabStatusTracking {

    private CabDetails cabDetails;

    private Enum cabStatus;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private CityDetails cityDetails;

    public CabDetails getCabDetails() {
        return cabDetails;
    }

    public void setCabDetails(CabDetails cabDetails) {
        this.cabDetails = cabDetails;
    }

    public Enum getCabStatus() {
        return cabStatus;
    }

    public void setCabStatus(Enum cabStatus) {
        this.cabStatus = cabStatus;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public CityDetails getCityDetails() {
        return cityDetails;
    }

    public void setCityDetails(CityDetails cityDetails) {
        this.cityDetails = cityDetails;
    }

    @Override
    public String toString() {
        return "CabStatusTracking{" +
                "cabDetails=" + cabDetails.getCabId() +
                ", cabStatus='" + cabStatus + '\'' +
                ", cityDetails=" + cityDetails +
                '}';
    }
}
