package yummypizza.core.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {

    private final String UPLOADS_DIR = getClass().getClassLoader().getResource("static/uploads").toString();

    public String uploadImage(MultipartFile image) {
        if (!image.isEmpty()) {
            String originalImageName = image.getOriginalFilename();
            String newImageName = UUID.randomUUID() + originalImageName.substring(originalImageName.lastIndexOf("."));
            try {
                Path fileNameAndPath = Paths.get(UPLOADS_DIR.substring(6), newImageName);
                Files.write(fileNameAndPath, image.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return newImageName;
        } else {
            return null;
        }

    }

}
