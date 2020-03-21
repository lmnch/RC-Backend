package com.rindviechcontrol;

import com.rindviechcontrol.dto.MapTileDTO;
import com.rindviechcontrol.dto.PositionStateDTO;
import com.rindviechcontrol.entity.MapTile;
import com.rindviechcontrol.entity.Position;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;
import java.util.List;
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

}
