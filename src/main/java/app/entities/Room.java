package app.entities;

import app.dtos.RoomDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Hotel hotel;

    private String number;
    private double price;

    public Room(Integer id, Hotel hotel, String number, double price) {
        this.id = id;
        this.hotel = hotel;
        this.number = number;
        this.price = price;
    }

    public Room(Hotel hotel, String number, double price) {
        this.hotel = hotel;
        this.number = number;
        this.price = price;
    }

    public Room(RoomDTO roomDTO, Hotel hotel) {
        this.id = roomDTO.getId();
        this.hotel = hotel;
        this.number = roomDTO.getNumber();
        this.price = roomDTO.getPrice();
    }
}
