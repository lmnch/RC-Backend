package com.rindviechcontrol;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.rindviechcontrol.dto.AddPositionDTO;
import com.rindviechcontrol.dto.GetPositionDTO;
import com.rindviechcontrol.entity.Position;

@Path("sender")
public class SenderResource {

    PositionService store = new PositionService();

    @GET
    @Path("position")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<GetPositionDTO> getPositions() {
        List<Position> pos = store.getPositionsInRect(0, 0, 50000, 5000000);
        return pos.stream().map((element) -> new GetPositionDTO(element.deviceId, element.longitude, element.latitude, element.time)).collect(Collectors.toList());
    }

    @GET
    @Path("pong")
    public String ping() {
        return "pong";
    }

    @POST
    @Path("postpos")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void addPosition(AddPositionDTO addPos) {
        System.out.println("Add pos");
        store.addPosition(new Position(addPos.deviceId, addPos.latitude, addPos.longitude));
    }

}
