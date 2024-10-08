package app.routes;

import app.config.HibernateConfig;
import app.controllers.HotelController;
import app.daos.HotelDAO;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class HotelRoute {

    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private final HotelDAO hotelDao = new HotelDAO(emf);
    private final HotelController hotelController = new HotelController(hotelDao);


    public EndpointGroup getHotelRoutes() {
        return () ->
        {
            get("/{id}", hotelController::getById);
            get("/", hotelController::getAll);
            post("/", hotelController::create);
            put("/{id}", hotelController::update);
            delete("/{id}", hotelController::delete);
            get("/rooms/{id}", hotelController::getAllRoomsBySpecificHotel);

            // Shows all hotels with rooms but can only be used without @Transient in the Hotel Class
            //get("/", hotelController::getAllHotelsWithRooms);

        };
    }
}
