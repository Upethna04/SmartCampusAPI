/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resource;

import com.smartcampus.exception.RoomNotEmptyException;
import com.smartcampus.model.Room;
import com.smartcampus.store.DataStore;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    private final DataStore store = DataStore.getInstance();

    @GET
    public Response getAllRooms() {
        Collection<Room> rooms = store.getRooms().values();
        return Response.ok(rooms).build();
    }

    @POST
    public Response createRoom(Room room) {
        if (room.getId() == null || room.getId().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Room ID is required");
            return Response.status(400).entity(error).build();
        }
        store.getRooms().put(room.getId(), room);
        return Response.status(Response.Status.CREATED).entity(room).build();
    }

    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId) {
        Map<String, Room> roomMap = store.getRooms();
        Room room = roomMap.get(roomId);
        if (room == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Room not found: " + roomId);
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
        return Response.ok(room).build();
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Map<String, Room> roomMap = store.getRooms();
        Room room = roomMap.get(roomId);
        if (room == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Room not found: " + roomId);
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(
                "Room " + roomId + " still has sensors assigned. Remove sensors first.");
        }
        store.getRooms().remove(roomId);
        return Response.noContent().build();
    }
}