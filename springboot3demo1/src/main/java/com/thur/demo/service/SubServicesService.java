package com.thur.demo.service;

<<<<<<< HEAD

=======
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
import java.util.Optional;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thur.demo.model.subscriptions.ServicesInfo;
import com.thur.demo.model.subscriptions.SubServices;
import com.thur.demo.model.subscriptions.SubServicesId;
import com.thur.demo.model.subscriptions.SubServicesRepository;
import com.thur.demo.model.subscriptions.Subscriptions;
import com.thur.demo.model.user.User;

@Service
public class SubServicesService {
<<<<<<< HEAD
	
	
	
	@Autowired
	private SubServicesRepository subServRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ServicesInfoService servicesInfoService;
	
	
=======

	@Autowired
	private SubServicesRepository subServRepo;

	@Autowired
	private UserService userService;

	@Autowired
	private ServicesInfoService servicesInfoService;

>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
	@Transactional
	public SubServices insert(String json, UUID userId) {
		try {
			JSONObject obj = new JSONObject(json);
			Integer serviceId = obj.isNull("servInfoId") ? null : obj.getInt("servInfoId");
			SubServices exist = subServRepo.findByUsersIdAndServiceId(userId, serviceId);
<<<<<<< HEAD
			if(exist != null) {
				return null;
			}
			Optional<User> user =  userService.findById(userId);
			ServicesInfo servicesInfo = servicesInfoService.findById(serviceId);
			if(servicesInfo == null) {
=======
			if (exist != null) {
				return null;
			}
			Optional<User> user = userService.findById(userId);
			ServicesInfo servicesInfo = servicesInfoService.findById(serviceId);
			if (servicesInfo == null) {
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
				return null;
			}
			// id 物件
			SubServicesId subServicesId = new SubServicesId();
			subServicesId.setUserId(userId);
			subServicesId.setServiceId(serviceId);
<<<<<<< HEAD
			
=======

>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
			// Cart 物件
			SubServices subServices = new SubServices();
			subServices.setSubServicesId(subServicesId);
			subServices.setUser(user.get());
			subServices.setServicesInfo(servicesInfo);
<<<<<<< HEAD
			subServices.setServiceName(servicesInfo.getServiceName());	
=======
			subServices.setServiceName(servicesInfo.getServiceName());
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
			return subServRepo.save(subServices);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

<<<<<<< HEAD
	
	//刪除單筆資料(by id)
	public boolean remove(Integer serviceId, UUID userId) {
		if(serviceId!=null) {
			SubServices subServices = subServRepo.findByUsersIdAndServiceId(userId, serviceId);
			if(subServices != null) {
=======
	// 刪除單筆資料(by id)
	public boolean remove(Integer serviceId, UUID userId) {
		if (serviceId != null) {
			SubServices subServices = subServRepo.findByUsersIdAndServiceId(userId, serviceId);
			if (subServices != null) {
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
				subServRepo.deleteById(subServices.getSubServicesId());
				return true;
			}
		}
		return false;
	}

<<<<<<< HEAD
	
=======
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
}
