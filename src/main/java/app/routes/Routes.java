package app.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {

    private HotelRoute hotelRoutes = new HotelRoute();
    private RoomRoute roomRoutes = new RoomRoute();

    public EndpointGroup getApiRoutes() {
        return () ->
        {
            path("/hotels", hotelRoutes.getHotelRoutes());
            path("/rooms", roomRoutes.getRoomRoutes());

        };
    }
}
