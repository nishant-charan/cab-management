package com.machinecoding.cabmanagement.dto;

public class CabSnapshotDto {

    private Long cabId;

    private String cabState;

    private Long cityId;

    public Long getCabId() {
        return cabId;
    }

    public void setCabId(Long cabId) {
        this.cabId = cabId;
    }

    public String getCabState() {
        return cabState;
    }

    public void setCabState(String cabState) {
        this.cabState = cabState;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    @Override
    public String toString() {
        return "CabSnapshotDto{" +
                "cabId=" + cabId +
                ", cabState='" + cabState + '\'' +
                ", cityId=" + cityId +
                '}';
    }
}
