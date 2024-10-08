package app.routes;

import app.config.AppConfig;
import app.config.HibernateConfig;
import app.daos.HotelDAO;
import app.entities.Hotel;
import app.entities.Room;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class HotelRouteTest {

    static Javalin app;
    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfigTest();
    private static String BASE_URL = "http://localhost:7000/api";
    private static HotelDAO hotelDAO = new HotelDAO(emf);

    private static Hotel h1, h2, h3;
    private static List<Hotel> hotels;


    @BeforeAll
    static void init() {
        HibernateConfig.setTest(true);
        AppConfig.startServer();

    }

    @BeforeEach
    void setUp() {

        h1 = new Hotel("Hotel 1", "Address 1", List.of());
        h2 = new Hotel("Hotel 2", "Address 2", List.of());
        h3 = new Hotel("Hotel 3", "Address 3", List.of());

        Room r1 = new Room(h1, "100", 100.00);
        Room r2 = new Room(h2, "200", 200.00);
        Room r3 = new Room(h3, "300", 300.00);

        h1.setRooms(List.of(r1));
        h2.setRooms(List.of(r2));
        h3.setRooms(List.of(r3));

        hotelDAO.create(h1);
        hotelDAO.create(h2);
        hotelDAO.create(h3);


    }

    @AfterEach
    void tearDown() {

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Room").executeUpdate();
            em.createQuery("DELETE FROM Hotel").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE room_id_seq RESTART WITH 1").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE hotel_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void closeDown() {
        AppConfig.stopServer(app);
    }

    @Test
    void testGetHotel() {
        Hotel hotel =
                given()
                .when()
                .get(BASE_URL + "/hotels/" + h1.getId())
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(Hotel.class);

        assertThat(hotel, equalTo(h1));
    }
}