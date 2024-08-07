package com.thur.demo.controller;

import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thur.demo.model.subscriptions.ServicesInfo;
import com.thur.demo.model.subscriptions.SubServices;
import com.thur.demo.service.SubServicesService;

@RequestMapping("/ajax/pages/SubServices")
@RestController //REST controller是透過REST，以JSON格式與前端溝通。
@CrossOrigin
public class SubServicesController {


	@Autowired
	private SubServicesService subServicesServ;
	

	//新增
	@PostMapping("/add")
	public String create(@RequestBody String subsBody, @RequestHeader("session-id") UUID loginUserId) {
		System.out.println("UUID:" + loginUserId);
		JSONObject responseJson = new JSONObject();
		JSONObject obj = new JSONObject(subsBody);
		Integer servInfoId = obj.isNull("servInfoId") ? null : obj.getInt("servInfoId");
        if(servInfoId == null) {
        	responseJson.put("success", false);
        	responseJson.put("message", "servInfoId是必要欄位");
        } else {
        	SubServices subServices = subServicesServ.insert(subsBody ,loginUserId);
                if(subServices==null) {
                	responseJson.put("success", false);
                    responseJson.put("message", "重複訂閱/無此服務");
                } else {
                	responseJson.put("success", true);
                	responseJson.put("message", "訂閱成功");
                }
            }
        return responseJson.toString();
	}
	
	
	//刪除
    @DeleteMapping("/del/{id}")
    public String remove(@PathVariable Integer id, @RequestHeader("session-id") UUID loginUserId) {
        JSONObject responseJson = new JSONObject();
        if(id==null) {
        	responseJson.put("success", false);
        	responseJson.put("message", "Id是必要欄位");
        } else {
                if(!subServicesServ.remove(id, loginUserId)) {
                	responseJson.put("success", false);
                	responseJson.put("message", "取消訂閱失敗");
                } else {
                	responseJson.put("success", true);
                	responseJson.put("message", "你已經取消訂閱");
                }
        }
        return responseJson.toString();
    }

}
