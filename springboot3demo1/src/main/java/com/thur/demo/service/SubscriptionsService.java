package com.thur.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thur.demo.model.subscriptions.ServicesInfo;
import com.thur.demo.model.subscriptions.ServicesInfoRepository;
import com.thur.demo.model.subscriptions.Subscriptions;
import com.thur.demo.model.subscriptions.SubscriptionsRepository;
import com.thur.demo.model.user.User;

import com.thur.demo.model.user.UserRepository;


@Service
public class SubscriptionsService {
	
	@Autowired
	private SubscriptionsRepository subscriptionsRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ServicesInfoRepository serInfoRepo;

	//新增單筆資料
	public Subscriptions insert(String json, UUID loginUserId) {
		try {
			JSONObject obj = new JSONObject(json);
			boolean active = obj.isNull("active") ? null : obj.getBoolean("active");
			Integer servInfoId = obj.isNull("servInfoId") ? null : obj.getInt("servInfoId");
			Integer level = obj.isNull("level") ? null : obj.getInt("level");
			Optional<User> userOpt = userRepo.findById(loginUserId);
			Optional<ServicesInfo> servOpt = serInfoRepo.findById(servInfoId);
			Subscriptions insert = new Subscriptions();
				insert.setUser(userOpt.get());
				insert.setServicesInfo(servOpt.get());
				insert.setActive(active);
				insert.setLevel(level);
				return subscriptionsRepo.save(insert);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	//刪除單筆資料(by id)
	public boolean remove(Integer id) {
		if(id!=null) {
			Optional<Subscriptions> optional = subscriptionsRepo.findById(id);
			if(optional.isPresent()) {
				subscriptionsRepo.deleteById(id);
				return true;
			}
		}
		return false;
	}
	
	//修改
	public Subscriptions modify(String json, Integer id) {
		try {
			JSONObject obj = new JSONObject(json);
			//Integer id = obj.isNull("subscriptionId") ? null : obj.getInt("subscriptionId");
			boolean isActive = obj.isNull("isActive") ? null : obj.getBoolean("isActive");
			Integer level = obj.isNull("level") ? null : obj.getInt("level");
			Optional<Subscriptions> optional = subscriptionsRepo.findById(id);
			if(optional.isPresent()) {
				Subscriptions update = optional.get();
				//update.setServiceId(id);
				update.setActive(isActive);
				update.setLevel(level);
				return subscriptionsRepo.save(update);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	//查詢全部
	public List<Subscriptions> findAll() {
		return subscriptionsRepo.findAll();
	}

	//查詢單筆(by id)
	public Subscriptions findById(Integer id) {
		Optional<Subscriptions> optional = subscriptionsRepo.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	
	
}
