package app.daos;

import app.entities.Hotel;
import jakarta.persistence.EntityManagerFactory;

public class HotelDAO extends DAO <Hotel> {

    private EntityManagerFactory emf;

    public HotelDAO(EntityManagerFactory emf) {
        super(emf, Hotel.class);
    }

}
