package com.upo.springtest.service;

import com.upo.springtest.model.Car;
import com.upo.springtest.model.CarModel;
import com.upo.springtest.model.Image;
import com.upo.springtest.repository.ImageRepository;
import com.upo.springtest.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ImageService {
    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public List<Image> getAllImages(){
        return imageRepository.findAll();
    }

    public Image addImage(Image image){
        return imageRepository.save(image);
    }


    public void uploadImages(MultipartFile[] images, CarModel carModel) throws IOException {

        for(MultipartFile img: images){

            if (img != null && !img.isEmpty()){
                String name = String.valueOf(new Date().getTime()) + ".png";
                Image im = new Image();
                im.setLocation(name);
                im.setCarModel(carModel);
                Image uploadImg = addImage(im);

                Path uploadPath = Paths.get("./model-images/" + carModel.getId());
                if(!Files.exists(uploadPath)){
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(name);
                Files.copy(img.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            }

        }

    }





}
