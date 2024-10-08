package app.routes;

import app.config.HibernateConfig;
import app.controllers.RoomController;
import app.daos.HotelDAO;
import app.daos.RoomDAO;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class RoomRoute {

    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private final RoomDAO roomDao = new RoomDAO(emf);
    private final HotelDAO hotelDao = new HotelDAO(emf);
    private final RoomController roomController = new RoomController(roomDao, hotelDao);


    public EndpointGroup getRoomRoutes() {
        return () ->
        {
            get("/{id}", roomController::getById);
            get("/", roomController::getAll);
            post("/", roomController::create);
            put("/{id}", roomController::update);
            delete("/{id}", roomController::delete);

        };
    }
}
