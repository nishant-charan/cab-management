package com.machinecoding.cabmanagement.entity;

public class CabDetails {

    private Long cabId;

    private String cabRegNumber;

    private String cabStatus;

    private CityDetails cityDetails;

    public Long getCabId() {
        return cabId;
    }

    public void setCabId(Long cabId) {
        this.cabId = cabId;
    }

    public String getCabRegNumber() {
        return cabRegNumber;
    }

    public void setCabRegNumber(String cabRegNumber) {
        this.cabRegNumber = cabRegNumber;
    }

    public String getCabStatus() {
        return cabStatus;
    }

    public void setCabStatus(String cabStatus) {
        this.cabStatus = cabStatus;
    }

    public CityDetails getCityDetails() {
        return cityDetails;
    }

    public void setCityDetails(CityDetails cityDetails) {
        this.cityDetails = cityDetails;
    }

    @Override
    public String toString() {
        return "CabDetails{" +
                "cabId=" + cabId +
                ", cabRegNumber='" + cabRegNumber + '\'' +
                ", cabStatus='" + cabStatus + '\'' +
                ", cityDetails=" + cityDetails +
                '}';
    }
}
