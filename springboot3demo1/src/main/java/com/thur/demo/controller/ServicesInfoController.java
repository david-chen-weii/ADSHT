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

import com.thur.demo.service.ServicesInfoService;


@RequestMapping("/ajax/pages/servicesInfo")
@RestController //REST controller是透過REST，以JSON格式與前端溝通。
@CrossOrigin
public class ServicesInfoController {

	@Autowired
	private ServicesInfoService servInfoServ;

	//新增
	@PostMapping("/add")
	public String create(@RequestBody String infoBody, @RequestHeader("session-id") UUID loginUserId) {
		JSONObject responseJson = new JSONObject();
		JSONObject obj = new JSONObject(infoBody);
		String servName = obj.isNull("serviceName") ? null : obj.getString("serviceName");
        if(servName == null) {
        	responseJson.put("success", false);
        	responseJson.put("message", "servName是必要欄位");
        } else {
                ServicesInfo servInfo = servInfoServ.insert(infoBody,loginUserId);
                if(servInfo==null) {
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
            if(servInfoServ.findById(id) == null) {
            	responseJson.put("success", false);
            	responseJson.put("message", "Id不存在");
            } else {
                if(!servInfoServ.remove(id)) {
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
            if(servInfoServ.findById(id) == null) {
            	responseJson.put("success", false);
                responseJson.put("message", "Id不存在");
            } else {
            	ServicesInfo servInfo = servInfoServ.modify(body,id);
                if(servInfo == null) {
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
        List<ServicesInfo> servInfos = servInfoServ.findAll();
        if(servInfos!=null && !servInfos.isEmpty()) {
            for(ServicesInfo servInfo : servInfos) {
                JSONObject item = new JSONObject()
                        .put("ServiceId", servInfo.getServiceId())
                        .put("ServiceName", servInfo.getServiceName())
                        .put("Price", servInfo.getPrice())
                        .put("Description", servInfo.getDescription())
                        .put("Image", servInfo.getImage())
                        .put("CreatedAt", servInfo.getCreatedAt())
                        .put("UpdateAt", servInfo.getUpdateAt());
                array = array.put(item);
            }
        }
        //long count = servInfoServ.count(body);
        responseJson.put("ServList", array);
        responseJson.put("count", array.length());
        return responseJson.toString();
    }
    

    //查詢已建立的訂閱
    @PostMapping("/findMyCreate")
    public String findMyCreate( @RequestHeader("session-id") UUID loginUserId) {
        JSONObject responseJson = new JSONObject();
        JSONArray array = new JSONArray();
        List<ServicesInfo> servInfos = servInfoServ.findCreater(loginUserId);
        if(servInfos!=null && !servInfos.isEmpty()) {
            for(ServicesInfo servInfo : servInfos) {
                JSONObject item = new JSONObject()
                        .put("ServiceId", servInfo.getServiceId())
                        .put("ServiceName", servInfo.getServiceName())
                        .put("Price", servInfo.getPrice())
                        .put("Description", servInfo.getDescription())
                        .put("Image", servInfo.getImage())
                        .put("CreatedAt", servInfo.getCreatedAt())
                        .put("UpdateAt", servInfo.getUpdateAt());
                array = array.put(item);
            }
        }
        //long count = servInfoServ.count(body);
        responseJson.put("count", array.length());
        responseJson.put("ServList", array);
        return responseJson.toString();
    }
    
    
  //查詢已建立的訂閱
    @PostMapping("/findMySubsById")
    public String findMySubsById( @RequestHeader("session-id") UUID loginUserId) {
        JSONObject responseJson = new JSONObject();
        JSONArray array = new JSONArray();
        List<ServicesInfo> servInfos = servInfoServ.findMySubsById(loginUserId);
        if(servInfos!=null && !servInfos.isEmpty()) {
            for(ServicesInfo servInfo : servInfos) {
                JSONObject item = new JSONObject()
                        .put("ServiceId", servInfo.getServiceId())
                        .put("ServiceName", servInfo.getServiceName())
                        .put("Price", servInfo.getPrice())
                        .put("Description", servInfo.getDescription())
                        .put("Image", servInfo.getImage())
                        .put("CreatedAt", servInfo.getCreatedAt())
                        .put("UpdateAt", servInfo.getUpdateAt());
                array = array.put(item);
            }
        }
        //long count = servInfoServ.count(body);
        responseJson.put("count", array.length());
        responseJson.put("ServList", array);
        return responseJson.toString();
    }
    
    //查詢全部(排序)
    @PostMapping("/find")
    public String find(@RequestBody String body) {
        JSONObject responseJson = new JSONObject();
        JSONArray array = new JSONArray();
        List<ServicesInfo> servInfos = servInfoServ.find(body);
        if(servInfos!=null && !servInfos.isEmpty()) {
            for(ServicesInfo servInfo : servInfos) {
                JSONObject item = new JSONObject()
                        .put("ServiceId", servInfo.getServiceId())
                        .put("ServiceName", servInfo.getServiceName())
                        .put("Price", servInfo.getPrice())
                        .put("Description", servInfo.getDescription())
                        .put("Image", servInfo.getImage())
                        .put("CreatedAt", servInfo.getCreatedAt())
                        .put("UpdateAt", servInfo.getUpdateAt());
                array = array.put(item);
            }
        }
        long count = servInfoServ.count(body);
        responseJson.put("ServList", array);
        responseJson.put("count", count);
        return responseJson.toString();
    }
    
//    //查詢全部(使用者已訂閱)
//    @PostMapping("/findUserSubs")
//    public String findUserSubs(@RequestHeader("session-id") UUID loginUserId) {
//        JSONObject responseJson = new JSONObject();
//        JSONArray array = new JSONArray();
//        List<ServicesInfo> servInfos = subServicesServ.findByUsersId();
//        if(servInfos!=null && !servInfos.isEmpty()) {
//            for(ServicesInfo servInfo : servInfos) {
//                JSONObject item = new JSONObject()
////                        .put("ServiceName", servInfo.getServiceName())
//                        .put("Price", servInfo.getPrice())
//                        .put("Description", servInfo.getDescription());
//                array = array.put(item);
//            }
//        }
//        responseJson.put("ServList", array);
//        responseJson.put("count", array.length());
//        return responseJson.toString();
//    }
    
    //查詢單筆
    @GetMapping("/findOne/{pk}")
    public String findById(@PathVariable(name = "pk") Integer id) {
        JSONObject responseJson = new JSONObject();
        JSONArray array = new JSONArray();
        ServicesInfo servInfo = servInfoServ.findById(id);
        if(servInfo != null) {
            JSONObject item = new JSONObject()
                    .put("ServiceId", servInfo.getServiceId())
                    .put("ServiceName", servInfo.getServiceName())
                    .put("Price", servInfo.getPrice())
                    .put("Description", servInfo.getDescription())
                    .put("Image", servInfo.getImage())
                    .put("CreatedAt", servInfo.getCreatedAt())
                    .put("UpdateAt", servInfo.getUpdateAt());
            array = array.put(item);
        }
        responseJson.put("list", array);
        return responseJson.toString();
    }
    
}
