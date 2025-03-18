package in.tejagelli.foodiesapi.service;

import in.tejagelli.foodiesapi.entity.FoodEntity;
import in.tejagelli.foodiesapi.io.FoodRequest;
import in.tejagelli.foodiesapi.io.FoodResponse;
import in.tejagelli.foodiesapi.repository.FoodRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectAclResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.UUID;

@Service

public class FoodServiceImpl implements FoodService {
    @Autowired
    private  S3Client s3Client;

    @Autowired
    private  FoodRepository foodRepository;
    @Value("${aws.s3.bucketname}")
    private String bucketName;
    @Override
    public String uploadFile(MultipartFile file) {
        //Injecting S3 client
        String filenaameExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        //Generating unique string we used UUID
        String key = UUID.randomUUID().toString()+"."+filenaameExtension;
        try{
            //sending the image to bucket
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .acl("public-read")
                    .contentType(file.getContentType())
                    .build();
            //adding to S3 Bucket
            PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            if(response.sdkHttpResponse().isSuccessful()){
                return "https://" + bucketName + ".s3.amazonaws.com/" + key;

            }else{
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"File upload failed");
            }

        }catch(IOException ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"An error occured while uploading the file");
        }
    }

    @Override
    public FoodResponse addFood(FoodRequest request, MultipartFile file) {
        //Converting request object into entity object
        FoodEntity newFoodEntity = convertToEntity(request);
        String imageUrl = uploadFile(file);
        newFoodEntity.setImageUrl(imageUrl);
        newFoodEntity = foodRepository.save(newFoodEntity);
        return convertToResponse(newFoodEntity);
    }
    private FoodEntity convertToEntity(FoodRequest request){
        return FoodEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .build();
    }

    private FoodResponse convertToResponse(FoodEntity entity){
        return FoodResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .category(entity.getCategory())
                .price(entity.getPrice())
                .imageUrl(entity.getImageUrl())
                .build();
    }
}
