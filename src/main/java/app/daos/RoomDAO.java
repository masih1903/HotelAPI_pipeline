package app.daos;

import app.entities.Room;
import jakarta.persistence.EntityManagerFactory;

public class RoomDAO extends DAO <Room> {


    public RoomDAO(EntityManagerFactory emf) {
        super(emf, Room.class);
    }
}
