package com.example.aviationroutes.dto;

public class RouteDto {

    private String description;
    private SegmentDto beforeFlightTransfer;
    private SegmentDto flight;
    private SegmentDto afterFlightTransfer;

    public RouteDto(){
    }

    public RouteDto(String description, SegmentDto beforeFlightTransfer, SegmentDto flight, SegmentDto afterFlightTransfer) {
        this.description = description;
        this.beforeFlightTransfer = beforeFlightTransfer;
        this.flight = flight;
        this.afterFlightTransfer = afterFlightTransfer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SegmentDto getBeforeFlightTransfer() {
        return beforeFlightTransfer;
    }

    public void setBeforeFlightTransfer(SegmentDto beforeFlightTransfer) {
        this.beforeFlightTransfer = beforeFlightTransfer;
    }

    public SegmentDto getFlight() {
        return flight;
    }

    public void setFlight(SegmentDto flight) {
        this.flight = flight;
    }

    public SegmentDto getAfterFlightTransfer() {
        return afterFlightTransfer;
    }

    public void setAfterFlightTransfer(SegmentDto afterFlightTransfer) {
        this.afterFlightTransfer = afterFlightTransfer;
    }
}
