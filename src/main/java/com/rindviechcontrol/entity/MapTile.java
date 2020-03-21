package com.rindviechcontrol.entity;

public class MapTile {

    public static final double TILE_SIZE = 5;
    public static final double DANGER_DISTANCE = 5;

    private int dangerState;

    private int x, y;

    private double relLat, relLong;
    private double distance;

    public MapTile(int x, int y, double relationLat, double relationLong) {
        this.dangerState = 0;
        this.x = x;
        this.y = y;
        this.relLat = relationLat;
        this.relLong = relationLong;
    }

    public int addDangerSource(double dangerLat, double dangerLong) {
        double earthRadiusKm = 6371;

        double dLat = degreesToRadians(dangerLat - this.relLat);
        double dLong = degreesToRadians(dangerLong - this.relLong);

        double dY = earthRadiusKm * Math.tan(dLat) * 1000 / TILE_SIZE;
        double dX = earthRadiusKm * Math.tan(dLong) * 1000 / TILE_SIZE;

        // check distance
        if ((dX - this.x) * (dX - this.x) <= DANGER_DISTANCE && (dY - this.y) * (dY - this.y) <= DANGER_DISTANCE) {
            this.dangerState++;
        }

        return this.dangerState;
    }

    private static double degreesToRadians(double degrees) {
        return degrees * Math.PI / 180;
    }

    public int getDangerState() {
        return dangerState;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
