package app.daos;

import app.config.HibernateConfig;
import app.dtos.RoomDTO;
import app.entities.Hotel;
import app.entities.Room;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DAOTest {

    static EntityManagerFactory emf;
    static HotelDAO hotelDAO;
    static Hotel h1, h2, h3;



    @BeforeAll
    static void setUpAll() {

        HibernateConfig.setTest(true);
        emf = HibernateConfig.getEntityManagerFactory();
        hotelDAO = new HotelDAO(emf);
    }

    @BeforeEach

    void setUp() {

        h1 = new Hotel("Hotel 1", "Address 1", List.of(new Room(h1, "100", 100.00)));
        h2 = new Hotel("Hotel 2", "Address 2", List.of(new Room(h2, "200", 200.00)));
        h3 = new Hotel("Hotel 3", "Address 3", List.of(new Room(h3, "300", 300.00)));

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

    @Test
    void getById() {

        Hotel hotel = hotelDAO.getById(1);
        assertEquals(h1.getId(), hotel.getId());

    }

    @Test
    void getAll() {

        List<Hotel> hotels = hotelDAO.getAll();
        assertEquals(3, hotels.size());
    }

    @Test
    void create() {

            Hotel hotel = new Hotel("Hotel 4", "Address 4", List.of(new Room(h1, "400", 400.00)));
            hotelDAO.create(hotel);
            assertEquals(4, hotel.getId());
    }

    @Test
    void update() {

        String newName = "Hotel 5";
        Hotel hotel = hotelDAO.getById(1);
        hotel.setName(newName);
        hotelDAO.update(hotel);
        assertEquals(newName, hotelDAO.getById(1).getName());
    }


    @Test
    void delete() {

        hotelDAO.delete(1);
        assertNull(hotelDAO.getById(1));
    }

    @Disabled
    @Test
    void getAllRoomsBySpecificHotel() {

        List<RoomDTO> rooms = hotelDAO.getAllRoomsBySpecificHotel(1);
        assertEquals(1, rooms.size());
    }
}