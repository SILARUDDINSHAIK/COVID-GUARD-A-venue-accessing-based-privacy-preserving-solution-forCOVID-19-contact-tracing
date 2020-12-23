package com.example.covidguard.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class InfectionReport implements Serializable {
    @SerializedName("reportId")
    private Long reportId;
    @SerializedName("venueId")
    private String venueId;
    @SerializedName("startTime")
    private Date startTime;
    @SerializedName("endTime")
    private Date endTime;
    @SerializedName("lastUpdated")
    private Date lastUpdated;


    public InfectionReport() {
    }

    public InfectionReport(Long reportId, String venueId, Date startTime, Date endTime, Date lastUpdated) {
        this.reportId = reportId;
        this.venueId = venueId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lastUpdated = lastUpdated;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "InfectionReport{" +
                "reportId=" + reportId +
                ", venueId='" + venueId + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
