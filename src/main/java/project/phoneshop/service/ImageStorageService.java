package project.phoneshop.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Component
@Service
public interface ImageStorageService {
    boolean isImageFile(MultipartFile file);

    String saveAvatar(MultipartFile file, String fileName);

    String saveLogo(MultipartFile file, String fileName);

    String saveProductImg(MultipartFile file, String fileName);

    String saveShippingImg(MultipartFile file, String fileName);

    String saveImgProduct(MultipartFile file, String fileName);

    void destroyProductImg(UUID fileName);

    String saveProductRatingImg(MultipartFile file, String fileName);
    List<String> saveProductRatingList(MultipartFile[] files, int id);
}
