package app.dtos;

import app.entities.Hotel;
import app.entities.Room;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RoomDTO {

    private Integer id;
    private Integer hotelId;
    private String number;
    private double price;

    public RoomDTO(Integer id, Integer hotelId, String number, double price) {
        this.id = id;
        this.hotelId = hotelId;
        this.number = number;
        this.price = price;
    }

    public RoomDTO(Room room){
        this.id = room.getId();
        this.hotelId = room.getHotel().getId();
        this.number = room.getNumber();
        this.price = room.getPrice();

    }

    public static List<RoomDTO> toRoomDtoList(List<Room> rooms) {
        return rooms.stream().map(RoomDTO::new).toList();
    }

}
