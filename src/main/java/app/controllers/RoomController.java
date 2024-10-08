package app.controllers;

import app.daos.HotelDAO;
import app.daos.RoomDAO;
import app.dtos.RoomDTO;
import app.entities.Hotel;
import app.entities.Message;
import app.entities.Room;
import app.exceptions.ApiException;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RoomController implements Controller {

    private final Logger log = LoggerFactory.getLogger(RoomController.class);
    private final RoomDAO roomDao;
    private HotelDAO hotelDao;

    public RoomController(RoomDAO roomDao, HotelDAO hotelDao) {
        this.roomDao = roomDao;
        this.hotelDao = hotelDao;
    }


    @Override
    public void getById(Context ctx) {

        try {
            // == request ==
            Integer id = Integer.parseInt(ctx.pathParam("id"));

            // == querying ==
            Room room = roomDao.getById(id);

            if (room == null) {
                ctx.res().setStatus(404);
                ctx.json(new Message(404, "Room not found"), Message.class);
                return;
            }

            // == response ==
            RoomDTO roomDto = new RoomDTO(room);
            ctx.res().setStatus(200);
            ctx.json(roomDto, RoomDTO.class);

        } catch (NumberFormatException e) {
            log.error("Invalid room ID format: {}", e.getMessage());
            throw new ApiException(400, "Invalid room ID format");
        } catch (Exception e) {
            log.error("500 {}", e.getMessage());
            throw new ApiException(500, e.getMessage());
        }


    }

    @Override
    public void getAll(Context ctx) {

        try {
            // == querying ==
            List<Room> rooms = roomDao.getAll();

            // == response ==
            List<RoomDTO> roomDtos = RoomDTO.toRoomDtoList(rooms);
            ctx.res().setStatus(200);
            ctx.json(roomDtos, RoomDTO.class);
        } catch (Exception e) {
            log.error("500 {}", e.getMessage());
            throw new ApiException(500, e.getMessage());
        }
    }

    @Override
    public void create(Context ctx) {

        try {
            RoomDTO roomDTO = ctx.bodyAsClass(RoomDTO.class);
            Hotel hotel = hotelDao.getById(roomDTO.getHotelId());

            if (hotel == null) {
                throw new ApiException(400, "Room not found");
            }

            Room room = new Room(roomDTO, hotel);
            room.setHotel(hotel);
            roomDao.create(room);

            ctx.res().setStatus(201);
            ctx.json(room, Room.class);
            ctx.result("Room created");
        } catch (Exception e) {
            log.error("400 {}", e.getMessage());
            throw new ApiException(400, "Bad Request: " + e.getMessage());
        }
    }

    @Override
    public void update(Context ctx) {

    }

    @Override
    public void delete(Context ctx) {

        try {
            // == request ==
            Integer id = Integer.parseInt(ctx.pathParam("id"));

            // == querying ==
            Room room = roomDao.getById(id);

            // == response ==
            roomDao.delete(id);
            ctx.res().setStatus(200);
            ctx.result("Room deleted");
        } catch (Exception e) {
            log.error("400 {}", e.getMessage());
            throw new ApiException(400, e.getMessage());
        }
    }
}


