package com.thur.demo.util;

import java.nio.file.Files;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class DefaultIcon {
    @Autowired
    private ResourceLoader resourceLoader;

    public String defaultIcon() {
        try {
            int i = (int) (Math.random() * 2) + 1;

            String imagePath = "classpath:img/defaultIcon/" + i + ".png";
            Resource resource = resourceLoader.getResource(imagePath);
            byte[] imageBytes = Files.readAllBytes(resource.getFile().toPath());
            String base64String = Base64.getEncoder().encodeToString(imageBytes);
            return "data:image/png;base64," + base64String;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String defaultProductIcon() {
        try {
            String imagePath = "classpath:img/defaultIcon/question.png";
            Resource resource = resourceLoader.getResource(imagePath);
            byte[] imageBytes = Files.readAllBytes(resource.getFile().toPath());
            String base64String = Base64.getEncoder().encodeToString(imageBytes);
            return "data:image/png;base64," + base64String;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String defaultShopIcon() {
        try {
            String imagePath = "classpath:img/defaultIcon/question.png";
            Resource resource = resourceLoader.getResource(imagePath);
            byte[] imageBytes = Files.readAllBytes(resource.getFile().toPath());
            String base64String = Base64.getEncoder().encodeToString(imageBytes);
            return "data:image/png;base64," + base64String;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
