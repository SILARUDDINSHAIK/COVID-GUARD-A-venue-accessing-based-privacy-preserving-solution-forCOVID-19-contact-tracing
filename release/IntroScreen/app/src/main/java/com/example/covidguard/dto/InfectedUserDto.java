package com.example.covidguard.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InfectedUserDto implements Serializable {
    @SerializedName("authCode")
    private String permissionCode;
    @SerializedName("reports")
    private List<VenueRecordDto> venueRecords = new ArrayList<>();

    public InfectedUserDto(String permissionCode, List<VenueRecordDto> venueRecords) {
        this.permissionCode = permissionCode;
        this.venueRecords = venueRecords;
    }

    public InfectedUserDto() {
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public List<VenueRecordDto> getVenueRecords() {
        return venueRecords;
    }

    public void setVenueRecords(List<VenueRecordDto> venueRecords) {
        this.venueRecords = venueRecords;
    }
}
