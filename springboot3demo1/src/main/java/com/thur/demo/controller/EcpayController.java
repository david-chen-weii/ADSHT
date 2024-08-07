package com.thur.demo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.view.RedirectView;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@RestController
@RequestMapping("/")
public class EcpayController {

    @Value("${ecpay.hashkey}")
    private String hashKey;

    @Value("{ecpay.hashiv}")
    private String hashIv;

    @PostMapping("/return")
    public ResponseEntity<String> handleReturn(@RequestBody Map<String, String> requestData) {
        String checkMacValue = requestData.get("CheckMacValue");
        requestData.remove("CheckMacValue");

        String generatedCheckMacValue = generateCheckMacValue(requestData);

        System.out.println("111111111111111111111111111111111111111");
        System.out.println("Generated CheckMacValue: " + generatedCheckMacValue);
        System.out.println("Received CheckMacValue: " + checkMacValue);

        if (checkMacValue.equals(generatedCheckMacValue)) {
            // 处理支付结果
            System.out.println("Payment Result: " + requestData);
            return ResponseEntity.ok("OK");
        } else {
            return ResponseEntity.status(400).body("Invalid CheckMacValue");
        }
    }

    @GetMapping("/clientReturn")
    public RedirectView clientReturn() {
        return new RedirectView("http://localhost:8080/thank-you");
    }

    @GetMapping("/thank-you")
    public String thankYou() {
        return "Thank you for your purchase!";
    }

    private String generateCheckMacValue(Map<String, String> data) {
        List<String> sortedKeys = new ArrayList<>(data.keySet());
        Collections.sort(sortedKeys);

        StringBuilder queryString = new StringBuilder("HashKey=" + hashKey);
        for (String key : sortedKeys) {
            String value = data.get(key);
            if (value != null && !value.isEmpty()) {
                queryString.append("&").append(key).append("=").append(value);
            }
        }
        queryString.append("&HashIV=").append(hashIv);

        String encodedQuery = encodeURIComponent(queryString.toString().toLowerCase());
        return DigestUtils.md5Hex(encodedQuery).toUpperCase();
    }

    private String encodeURIComponent(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8")
                    .replace("+", "%20")
                    .replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
