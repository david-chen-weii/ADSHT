package com.thur.demo.controller;

import java.io.FileOutputStream;
import java.util.Base64;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/ajax/image")
public class imageController {
    @PostMapping("/base64")
    public String postMethodName(@RequestBody String request) {
        JSONObject response = new JSONObject();
        JSONObject body = new JSONObject(request);
        String base64Image = body.getString("image");

        if (base64Image != null) {
            try {
                byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                try (FileOutputStream fos = new FileOutputStream("uploaded_image.png")) {
                    fos.write(imageBytes);
                    return "Image uploaded successfully";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Error saving image: " + e.getMessage();
            }
        } else {
            return "No image found";
        }

    }

}
