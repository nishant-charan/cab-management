package com.machinecoding.cabmanagement.util;

public final class Constant {

    public enum CabStatusEnum {
        IDLE("IDLE"),
        ON_TRIP("ON_TRIP");

        private String cabStatus;

        CabStatusEnum(String cabStatus) {
            this.cabStatus = cabStatus;
        }

        public String getCabStatus() {
            return cabStatus;
        }
    }
}
