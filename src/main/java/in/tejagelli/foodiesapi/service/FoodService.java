package in.tejagelli.foodiesapi.service;

import in.tejagelli.foodiesapi.io.FoodRequest;
import in.tejagelli.foodiesapi.io.FoodResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FoodService {

    String uploadFile(MultipartFile file);
    FoodResponse addFood(FoodRequest request, MultipartFile file);
}
