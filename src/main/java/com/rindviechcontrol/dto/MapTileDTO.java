package com.rindviechcontrol.dto;

public class MapTileDTO {
public static int affected = 0;
    public int x,y;
    public int danger;


    public MapTileDTO(int x, int y, int danger) {
        this.x = x;
        this.y = y;
        this.danger = danger;
        if(danger>0){
            affected++;
        }
    }
}
