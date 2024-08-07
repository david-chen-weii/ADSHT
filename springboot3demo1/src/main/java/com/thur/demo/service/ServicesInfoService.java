package com.thur.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thur.demo.model.subscriptions.ServicesInfo;
import com.thur.demo.model.subscriptions.ServicesInfoRepository;
import com.thur.demo.model.subscriptions.SubServices;
import com.thur.demo.model.subscriptions.SubServicesRepository;
import com.thur.demo.model.subscriptions.Subscriptions;
import com.thur.demo.model.subscriptions.SubscriptionsRepository;
import com.thur.demo.model.user.User;
import com.thur.demo.model.user.UserRepository;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class ServicesInfoService {

	
	
	@Autowired
	private ServicesInfoRepository servicesInfoRepo;
<<<<<<< HEAD
	
	@Autowired
	private SubscriptionsRepository subscriptionsRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	//修改
=======

	@Autowired
	private SubscriptionsRepository subscriptionsRepo;

	@Autowired
	private UserRepository userRepo;

	// 修改
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
	public ServicesInfo modify(String json, Integer id) {
		try {
			JSONObject obj = new JSONObject(json);
			// Integer id = obj.isNull("serviceId") ? null : obj.getInt("serviceId");
			String name = obj.isNull("serviceName") ? null : obj.getString("serviceName");
			Integer price = obj.isNull("price") ? null : obj.getInt("price");
			String description = obj.isNull("description") ? null : obj.getString("description");
			String image = obj.isNull("image") ? null : obj.getString("image");
			Optional<ServicesInfo> optional = servicesInfoRepo.findById(id);
			if (optional.isPresent()) {
				ServicesInfo update = optional.get();
				// update.setServiceId(id);
				update.setServiceName(name);
				update.setPrice(price);
				update.setDescription(description);
				update.setImage(image);
				return servicesInfoRepo.save(update);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查詢全部
	public List<ServicesInfo> findAll() {
		return servicesInfoRepo.findAll();
	}

<<<<<<< HEAD
	
	//查詢我建立的訂閱
	public List<ServicesInfo> findCreater(UUID loginUserId) {
		return servicesInfoRepo.findCreater(loginUserId);
	}
	
	//查詢使用者目前訂閱的資料(by id)
=======
	// 查詢我建立的訂閱
	public List<ServicesInfo> findCreater(UUID loginUserId) {
		return servicesInfoRepo.findCreater(loginUserId);
	}

	// 查詢使用者目前訂閱的資料(by id)
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
	public List<ServicesInfo> findMySubsById(UUID id) {
		try {
			return servicesInfoRepo.findMySubs(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
<<<<<<< HEAD
	
	//查詢單筆(by id)
=======

	// 查詢單筆(by id)
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
	public ServicesInfo findById(Integer id) {
		Optional<ServicesInfo> optional = servicesInfoRepo.findById(id);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	// 刪除單筆資料(by id)
	public boolean remove(Integer id) {
		if (id != null) {
			Optional<ServicesInfo> optional = servicesInfoRepo.findById(id);
			if (optional.isPresent()) {
				servicesInfoRepo.deleteById(id);
				return true;
			}
		}
		return false;
	}

<<<<<<< HEAD
	//新增單筆資料
	public ServicesInfo insert(String json,UUID loginUserId) {
=======
	// 新增單筆資料
	public ServicesInfo insert(String json, UUID loginUserId) {
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
		try {
			JSONObject obj = new JSONObject(json);
			String name = obj.isNull("serviceName") ? null : obj.getString("serviceName");
			Integer price = obj.isNull("price") ? null : obj.getInt("price");
			String description = obj.isNull("description") ? null : obj.getString("description");
			String image = obj.isNull("image") ? null : obj.getString("image");
<<<<<<< HEAD
			Integer level = obj.isNull("level") ? null : obj.getInt("level");
			//boolean acitve = obj.isNull("acitve") ? null : obj.getBoolean("acitve");
				ServicesInfo insert = new ServicesInfo();
				insert.setServiceName(name);
				insert.setDescription(description);
				insert.setPrice(price);
				try {
					insert.setImage(image);
				} catch (Exception e) {
					insert.setImage(null);
				}
				ServicesInfo servicesInfo = servicesInfoRepo.save(insert);
				System.out.println(insert.getServiceId());
///
				Subscriptions insert2 = new Subscriptions();
				Optional<User> userOpt = userRepo.findById(loginUserId);
				Optional<ServicesInfo> servOpt = servicesInfoRepo.findById(insert.getServiceId());
				insert2.setUser(userOpt.get());
				insert2.setServicesInfo(servOpt.get());
				insert2.setLevel(level);
				insert2.setActive(false);
				subscriptionsRepo.save(insert2);
				return servicesInfo;
=======
			ServicesInfo insert = new ServicesInfo();
			insert.setServiceName(name);
			insert.setDescription(description);
			insert.setPrice(price);
			try {
				insert.setImage(image);
			} catch (Exception e) {
				insert.setImage(null);
			}
			ServicesInfo servicesInfo = servicesInfoRepo.save(insert);
			System.out.println(insert.getServiceId());
			///
			Subscriptions insert2 = new Subscriptions();
			Optional<User> userOpt = userRepo.findById(loginUserId);
			Optional<ServicesInfo> servOpt = servicesInfoRepo.findById(insert.getServiceId());
			insert2.setUser(userOpt.get());
			insert2.setServicesInfo(servOpt.get());
			insert2.setActive(false);
			subscriptionsRepo.save(insert2);
			return servicesInfo;
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
<<<<<<< HEAD
	
=======

>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
	public List<ServicesInfo> find(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			return servicesInfoRepo.find(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
<<<<<<< HEAD
	
=======

>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
	public long count(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			return servicesInfoRepo.count(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
<<<<<<< HEAD
	
=======

>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
}
