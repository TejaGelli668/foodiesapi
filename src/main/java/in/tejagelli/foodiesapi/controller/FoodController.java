package in.tejagelli.foodiesapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.tejagelli.foodiesapi.io.FoodRequest;
import in.tejagelli.foodiesapi.io.FoodResponse;
import in.tejagelli.foodiesapi.service.FoodService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/foods")
@AllArgsConstructor
public class FoodController {

    private final FoodService foodService;
    @PostMapping
    public FoodResponse addFood(@RequestPart("food") String foodString,
                                @RequestPart("file")MultipartFile file){
        ObjectMapper objectMapper = new ObjectMapper();
        FoodRequest request = null;
        try{
            //Converting into JSON Object
            request =objectMapper.readValue(foodString,FoodRequest.class);

        }catch(JsonProcessingException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON format");

        }
        FoodResponse response = foodService.addFood(request, file);
        return response;
    }
}
