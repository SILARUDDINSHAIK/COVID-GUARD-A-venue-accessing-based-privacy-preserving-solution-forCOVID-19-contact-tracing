package com.example.covidguard.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class VenueRecordDto implements Serializable {
    @SerializedName("uuid")
    private String UUID;
    @SerializedName("startTime")
    private String startTime;
    @SerializedName("endTime")
    private String endTime;

    public VenueRecordDto() {
    }

    public VenueRecordDto(String UUID, String startTime, String endTime) {
        this.UUID = UUID;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "VenueRecordDto{" +
                "venueId='" + UUID + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
