package com.thur.demo.model.subscriptions;

import java.util.List;

import org.json.JSONObject;

public interface ServicesInfoDAO {
	public abstract List<ServicesInfo> find(JSONObject obj);
	public abstract long count(JSONObject obj);
}
