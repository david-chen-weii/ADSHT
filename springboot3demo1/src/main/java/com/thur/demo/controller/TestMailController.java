package com.thur.demo.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thur.demo.model.user.User;
import com.thur.demo.service.UserService;
import com.thur.demo.util.JsonWebTokenUtility;

@RestController
@CrossOrigin
@RequestMapping("/ajax/mail")
public class TestMailController {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    UserService userService;
    @Autowired
    JsonWebTokenUtility tokenUtility;
    @Value("${mypath}")
    private String path;

    @PostMapping("/forget")
    public String test(@RequestBody String request) {
        JSONObject response = new JSONObject();
        SimpleMailMessage message = new SimpleMailMessage();
        JSONObject body = new JSONObject(request);

        User user = userService.forgetPassword(body.getString("email"));
        if (user != null) {
            String token = tokenUtility.createEncryptedToken(user.toString(), null);
            System.out.println("token : " + token);
            message.setTo(user.getUserAccount());
            message.setSubject("Reset Password");
            message.setText(path + token);
            mailSender.send(message);
            // String a = tokenUtility.validateEncryptedToken(token);
            // System.out.println("a : " + a);
            response.put("success", "請於5分鐘內更改密碼");
            return response.toString();
        }
        response.put("error", "請輸入有效的E-mail:");
        return response.toString();
    }

}
