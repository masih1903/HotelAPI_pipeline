package app.controllers;

import app.daos.HotelDAO;
import app.dtos.HotelDTO;
import app.dtos.RoomDTO;
import app.entities.Hotel;
import app.entities.Message;
import app.exceptions.ApiException;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class HotelController implements Controller {

    private final Logger log = LoggerFactory.getLogger(HotelController.class);
    private final HotelDAO hotelDao;

    public HotelController(HotelDAO hotelDao) {
        this.hotelDao = hotelDao;
    }

    @Override
    public void getById(Context ctx) {
        try {
            // == request ==
            Integer id = Integer.parseInt(ctx.pathParam("id"));

            // == querying ==
            Hotel hotel = hotelDao.getById(id);

            if (hotel == null) {
                ctx.res().setStatus(404);
                ctx.json(new Message(404, "Hotel not found"), Message.class);
                return;
            }

            // == response ==
            HotelDTO hotelDto = new HotelDTO(hotel);
            ctx.res().setStatus(200);
            ctx.json(hotelDto, HotelDTO.class);

        } catch (NumberFormatException e) {
            log.error("Invalid hotel ID format: {}", e.getMessage());
            throw new ApiException(400, "Invalid hotel ID format");
        } catch (Exception e) {
            log.error("500 {}", e.getMessage());
            throw new ApiException(500, e.getMessage());
        }
    }

    @Override
    public void getAll(Context ctx) {

        try {
            // == querying ==
            List<Hotel> hotels = hotelDao.getAll();

            // == response ==
            List<HotelDTO> hotelDtos = HotelDTO.toHotelDtoList(hotels);
            ctx.res().setStatus(200);
            ctx.json(hotelDtos, HotelDTO.class);
        } catch (Exception e) {
            log.error("500 {}", e.getMessage());
            throw new ApiException(500, e.getMessage());
        }


    }

    @Override
    public void create(Context ctx) {

        try {
            // == request ==
            HotelDTO hotelDto = ctx.bodyAsClass(HotelDTO.class);

            // == converting DTO to Entity ==
            Hotel newHotel = new Hotel(hotelDto);

            // == persisting the hotel and associated rooms ==
            hotelDao.create(newHotel);

            // == response ==
            ctx.res().setStatus(201);
            ctx.result("Hotel created");
        } catch (Exception e) {

            log.error("400 {}", e.getMessage());

            throw new ApiException(400, e.getMessage());
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
            Hotel hotel = hotelDao.getById(id);

            // == response ==
            hotelDao.delete(id);
            ctx.res().setStatus(200);
            ctx.result("Hotel deleted");
        } catch (Exception e) {
            log.error("400 {}", e.getMessage());
            throw new ApiException(400, e.getMessage());
        }

    }

    //Can only use this method without @Transient in the Hotel class
    public void getAllHotelsWithRooms(Context ctx) {
        try {
            // == querying ==
            List<Hotel> hotels = hotelDao.getAllHotelsWithrooms();

            // == response ==
            List<HotelDTO> hotelDtos = HotelDTO.toHotelDtoList(hotels);
            ctx.res().setStatus(200);
            ctx.json(hotelDtos, HotelDTO.class);
        }catch (Exception e) {
            log.error("500 {}", e.getMessage());
            throw new ApiException(500, e.getMessage());
        }

    }

    public void getAllRoomsBySpecificHotel(Context ctx)
    {
        try
        {
            // == request ==
            Integer id = Integer.parseInt(ctx.pathParam("id"));

            // == querying ==
            List<RoomDTO> rooms = hotelDao.getAllRoomsBySpecificHotel(id);

            // == response ==
            ctx.res().setStatus(200);
            ctx.json(rooms);
        } catch (NumberFormatException e)
        {
            log.error("400 {} ", e.getMessage());
            throw new ApiException(400, e.getMessage());
        } catch (Exception e)
        {
            log.error("500 {} ", e.getMessage());
            throw new ApiException(500, e.getMessage());
        }
    }

}
