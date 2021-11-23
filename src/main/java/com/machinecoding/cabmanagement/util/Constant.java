package com.machinecoding.cabmanagement.util;

public final class Constant {

    public enum CabStatusEnum {
        IDLE("IDLE"),
        IN_TRANSIT("IN_TRANSIT");

        private String cabStatus;

        CabStatusEnum(String cabStatus) {
            this.cabStatus = cabStatus;
        }

        public String getCabStatus() {
            return cabStatus;
        }
    }
}
