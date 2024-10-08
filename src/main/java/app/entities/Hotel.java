package app.entities;

import app.dtos.HotelDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String address;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER) // Use fetch = FetchType.EAGER to load all rooms when loading a hotel
    //@Transient
    private List<Room> rooms = new ArrayList<>();

    public Hotel(Integer id, String name, String address, List<Room> rooms) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.rooms = rooms;
    }

    public Hotel(String name, String address, List<Room> rooms) {
        this.name = name;
        this.address = address;
        this.rooms = rooms;
    }

    public Hotel(HotelDTO hotelDTO) {
        this.id = hotelDTO.getId();
        this.name = hotelDTO.getName();
        this.address = hotelDTO.getAddress();
        this.rooms = hotelDTO.getRooms()
                .stream()
                .map(roomDTO -> new Room(roomDTO, this))
                .collect(Collectors.toList());
    }
}
