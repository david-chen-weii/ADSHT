package com.thur.demo.controller;

import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thur.demo.model.subscriptions.ServicesInfo;
import com.thur.demo.model.subscriptions.Subscriptions;

import com.thur.demo.service.SubscriptionsService;


@RequestMapping("/ajax/pages/Subscriptions")
@RestController //REST controller是透過REST，以JSON格式與前端溝通。
@CrossOrigin
public class SubscriptionsController {

	
	@Autowired
	private SubscriptionsService subscriptionsServ;
	
	//新增
	@PostMapping("/add")
	public String create(@RequestBody String subsBody, @RequestHeader UUID loginUserId) {
		System.out.println("UUID:" + loginUserId);
		JSONObject responseJson = new JSONObject();
		JSONObject obj = new JSONObject(subsBody);
		Integer servInfoId = obj.isNull("servInfoId") ? null : obj.getInt("servInfoId");
        if(servInfoId == null) {
        	responseJson.put("success", false);
        	responseJson.put("message", "servInfoId是必要欄位");
        } else {
        	Subscriptions subscriptions = subscriptionsServ.insert(subsBody,loginUserId);
                if(subscriptions==null) {
                	responseJson.put("success", false);
                    responseJson.put("message", "新增失敗");
                } else {
                	responseJson.put("success", true);
                	responseJson.put("message", "新增成功");
                }
            }
        return responseJson.toString();
	}
	
	
	//刪除
    @DeleteMapping("/del/{id}")
    public String remove(@PathVariable Integer id) {
        JSONObject responseJson = new JSONObject();
        if(id==null) {
        	responseJson.put("success", false);
        	responseJson.put("message", "Id是必要欄位");
        } else {
            if(subscriptionsServ.findById(id) == null) {
            	responseJson.put("success", false);
            	responseJson.put("message", "Id不存在");
            } else {
                if(!subscriptionsServ.remove(id)) {
                	responseJson.put("success", false);
                	responseJson.put("message", "刪除失敗");
                } else {
                	responseJson.put("success", true);
                	responseJson.put("message", "刪除成功");
                }
            }
        }
        return responseJson.toString();
    }
    
    //修改
    @PutMapping("/update/{id}")
    public String modify(@PathVariable Integer id, @RequestBody String body) {
        JSONObject responseJson = new JSONObject();
        if(id==null) {
        	responseJson.put("success", false);
        	responseJson.put("message", "Id是必要欄位");
        } else {
            if(subscriptionsServ.findById(id) == null) {
            	responseJson.put("success", false);
                responseJson.put("message", "Id不存在");
            } else {
            	Subscriptions subscriptions = subscriptionsServ.modify(body,id);
                if(subscriptions == null) {
                	responseJson.put("success", false);
                	responseJson.put("message", "修改失敗");
                } else {
                	responseJson.put("success", true);
                	responseJson.put("message", "修改成功");
                }
            }
        }
        return responseJson.toString();
    }
    
    
    //查詢全部
    @PostMapping("/findAll")
    public String findAll() {
        JSONObject responseJson = new JSONObject();
        JSONArray array = new JSONArray();
        List<Subscriptions> subscriptions = subscriptionsServ.findAll();
        if(subscriptions!=null && !subscriptions.isEmpty()) {
            for(Subscriptions subscription : subscriptions) {
                JSONObject item = new JSONObject()
                        .put("SubscriptionId", subscription.getSubscriptionId())
                        .put("userAccount", subscription.getUser().getUserAccount())
                        .put("serviceId", subscription.getServicesInfo().getServiceId())
                        .put("isActive", subscription.isActive())
                        .put("CreatedAt", subscription.getCreatedAt())
                        .put("UpdateAt", subscription.getUpdateAt());
                array = array.put(item);
            }
        }
        //long count = servInfoServ.count(body);
        responseJson.put("SubscriptionsList", array);
        responseJson.put("count", array.length());
        return responseJson.toString();
    }
    
    //查詢單筆
    @GetMapping("/findOne/{pk}")
    public String findById(@PathVariable(name = "pk") Integer id) {
        JSONObject responseJson = new JSONObject();
        JSONArray array = new JSONArray();
        Subscriptions subscriptions = subscriptionsServ.findById(id);
        if(subscriptions != null) {
            JSONObject item = new JSONObject()
                    .put("SubscriptionId", subscriptions.getSubscriptionId())
                    .put("userAccount", subscriptions.getUser().getUserAccount())
                    .put("serviceId", subscriptions.getServicesInfo().getServiceId())
                    .put("isActive", subscriptions.isActive())
                    .put("CreatedAt", subscriptions.getCreatedAt())
                    .put("UpdateAt", subscriptions.getUpdateAt());
            array = array.put(item);
        }
        responseJson.put("list", array);
        return responseJson.toString();
    }
	
}
