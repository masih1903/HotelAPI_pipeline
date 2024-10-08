package app.daos;

import app.dtos.RoomDTO;
import app.entities.Hotel;
import app.entities.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.stream.Collectors;

public abstract class DAO<T> implements IDAO<T> {

    private EntityManagerFactory emf;
    private Class<T> entityClass;

    public DAO(EntityManagerFactory emf, Class<T> entityClass) {
        this.emf = emf;
        this.entityClass = entityClass;
    }


    @Override
    public T getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(entityClass, id);

        }
    }

    @Override
    public List<T> getAll() {
        try(EntityManager em = emf.createEntityManager()) {
            TypedQuery<T> query = em.createQuery("SELECT t FROM " + entityClass.getSimpleName() + " t", entityClass);
            return query.getResultList();
        }
    }

    @Override
    public void create(T t) {

        try (EntityManager em = emf.createEntityManager()) {

            em.getTransaction().begin();
            em.persist(t);
            em.getTransaction().commit();
        }
    }

    @Override
    public void update(T t) {

        try (EntityManager em = emf.createEntityManager()) {

            em.getTransaction().begin();
            em.merge(t);
            em.getTransaction().commit();
        }
    }

    @Override
    public void delete(Integer id) {

        try (EntityManager em = emf.createEntityManager()) {

            em.getTransaction().begin();
            T t = em.find(entityClass, id);
            em.remove(t);
            em.getTransaction().commit();
        }
    }

    public List<Hotel> getAllHotelsWithrooms()
    {
        try (EntityManager em = emf.createEntityManager())
        {
            return em.createQuery("SELECT h FROM Hotel h LEFT JOIN FETCH h.rooms", Hotel.class).getResultList();
        }
    }

    public List<RoomDTO> getAllRoomsBySpecificHotel(Integer hotelId)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            List<Room> rooms = em.createQuery("SELECT r FROM Room r WHERE r.hotel.id = :hotelId", Room.class)
                    .setParameter("hotelId", hotelId)
                    .getResultList();

            // Convert Room entities to RoomDTOs
            return rooms.stream()
                    .map(RoomDTO::new)
                    .collect(Collectors.toList());
        }
    }
}
