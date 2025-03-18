package in.tejagelli.foodiesapi.io;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//Created new request object to store food details
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodRequest {
    private String name;
    private String description;
    private double price;
    private String category;
}
