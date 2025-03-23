package com.example.aviationroutes.dto;

public class SegmentDto {
    private String origin;
    private String destination;
    private String transportationType;

    public SegmentDto(){
    }

    public SegmentDto(String origin, String destination, String transportationType){
        this.origin = origin;
        this.destination = destination;
        this.transportationType = transportationType;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTransportationType() {
        return transportationType;
    }

    public void setTransportationType(String transportationType) {
        this.transportationType = transportationType;
    }
}
