package com.machinecoding.cabmanagement.dto;

public class CabIdleDto {

    private Long cabId;

    private Long idleMillis;

    public Long getCabId() {
        return cabId;
    }

    public void setCabId(Long cabId) {
        this.cabId = cabId;
    }

    public Long getIdleMillis() {
        return idleMillis;
    }

    public void setIdleMillis(Long idleMillis) {
        this.idleMillis = idleMillis;
    }

    @Override
    public String toString() {
        return "CabIdleDto{" +
                "cabId=" + cabId +
                ", idleMillis=" + idleMillis +
                '}';
    }
}
