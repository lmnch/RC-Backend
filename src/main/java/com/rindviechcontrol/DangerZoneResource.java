package com.rindviechcontrol;

import com.rindviechcontrol.dto.MapTileDTO;
import com.rindviechcontrol.dto.PositionStateDTO;
import com.rindviechcontrol.entity.MapTile;
import com.rindviechcontrol.entity.Position;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Path("dangerzone")
public class DangerZoneResource {

    private static final double DELTA_SEARCH_LAT = 0.05;
    private static final double DELTA_SEARCH_LONG = 0.05;

    private static final double SCAN_AREA_RADIUS = 1000;


    PositionService store = new PositionService();


    @Path("map")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<List<MapTileDTO>> getMap(@QueryParam("latitude") double latitude, @QueryParam("longitude") double longitude) {
        List<Position> dangerThings = store.getPositionsInRect(latitude - DELTA_SEARCH_LAT, latitude + DELTA_SEARCH_LAT, longitude - DELTA_SEARCH_LONG, longitude + DELTA_SEARCH_LONG);
        dangerThings = this.filterPositions(dangerThings);

        List<List<MapTile>> map = new LinkedList<>();
        int mapSize = (int) (2 * SCAN_AREA_RADIUS / MapTile.TILE_SIZE);
        for (int y = 0; y < mapSize; y++) {
            List<MapTile> row = new LinkedList<>();
            map.add(row);
            for (int x = 0; x < mapSize; x++) {
                MapTile tile = new MapTile(x-mapSize/2, y-mapSize/2, latitude, longitude);

                // Add danger value stuff
                for (Position danger :
                        dangerThings) {
                    tile.addDangerSource(danger.latitude, danger.longitude);
                }
                row.add(tile);
            }
        }

        // Create MapTile DTOs
        List<List<MapTileDTO>> res = map.stream().map((row) -> row.stream().map((tile) -> new MapTileDTO(tile.getX(), tile.getY(), tile.getDangerState())).collect(Collectors.toList())).collect(Collectors.toList());
        return res;
    }

    @Path("state")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public PositionStateDTO getState(@QueryParam("latitude") double latitude, @QueryParam("longitude") double longitude) {
        List<Position> dangerThings = store.getPositionsInRect(latitude - DELTA_SEARCH_LAT, latitude + DELTA_SEARCH_LAT, longitude - DELTA_SEARCH_LONG, longitude + DELTA_SEARCH_LONG);

        MapTile mt = new MapTile(0,0, latitude, longitude);
        for (Position danger:dangerThings
             ) {
            mt.addDangerSource(danger.latitude, danger.longitude);
        }

        return new PositionStateDTO(mt.getDangerState());
    }

    private List<Position> filterPositions(List<Position> position){
        List<Position> list =  new LinkedList<>();
        position.stream().sorted(Comparator.comparing(a -> a.time)).forEach((e)->{
            if(!list.stream().map((p)->p.deviceId).collect(Collectors.toList()).contains(e.deviceId)){
                list.add(e);
            }
        });
        return list;
    }

    private Random random = new Random();

    @Path("dummy")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<List<MapTileDTO>> getDummy(){
        double latitude=50.154798;
        double longitude =10.695795;
        MapTileDTO.affected=0;
        double drad=50.156398-latitude;

        int count = random.nextInt(30)+10;

        List<Position> dangerThings= new LinkedList<>();
        for (int i = 0; i < count; i++) {
            double dlat = random.nextInt((int)(drad*100000*2))/100000.0-drad;
            double dlong = random.nextInt((int)(drad*100000*2))/100000.0-drad;
            System.out.println(dlat+ "  "+dlong);

            dangerThings.add( new Position("test"+random.nextInt(), latitude+dlat    , longitude+dlong));
        }
        System.out.println(dangerThings);
        dangerThings = this.filterPositions(dangerThings);

        int affectedCount = 0;

        List<List<MapTile>> map = new LinkedList<>();
        int mapSize = (int) (2 * SCAN_AREA_RADIUS / MapTile.TILE_SIZE);
        for (int y = 0; y < mapSize; y++) {
            List<MapTile> row = new LinkedList<>();
            map.add(row);
            for (int x = 0; x < mapSize; x++) {
                MapTile tile = new MapTile(x-mapSize/2, y-mapSize/2, latitude, longitude);

                // Add danger value stuff
                for (Position danger :
                        dangerThings) {
                    tile.addDangerSource(danger.latitude, danger.longitude);
                }
                if(tile.getDangerState()>0){
                    affectedCount++;
                }
                row.add(tile);
            }
        }
        System.out.println("affected tiles: "+affectedCount);

        // Create MapTile DTOs
        List<List<MapTileDTO>> res = map.stream().map((row) -> row.stream().map((tile) -> new MapTileDTO(tile.getX(), tile.getY(), tile.getDangerState())).collect(Collectors.toList())).collect(Collectors.toList());

        System.out.println(MapTileDTO.affected);

        return res;
    }

}
