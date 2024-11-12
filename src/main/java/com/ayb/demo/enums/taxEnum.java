package com.ayb.demo.enums;

public enum taxEnum {

    TR(0.50), US(0.40), DE(0.30), FR(0.20), UK(0.35);

    private final double rate; 

    taxEnum(double rate) {
        this.rate = rate;
    }

    public double getRate() {
        return rate;
    }
}
