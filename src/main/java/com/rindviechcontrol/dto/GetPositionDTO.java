package com.rindviechcontrol.dto;

import java.time.ZonedDateTime;

public class GetPositionDTO {

    public String deviceId;

    public double latitude;
    public double longitude;

    public ZonedDateTime time = ZonedDateTime.now();

    public GetPositionDTO(String deviceId, double latitude, double longitude, ZonedDateTime time) {
        this.deviceId = deviceId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
    }
}
