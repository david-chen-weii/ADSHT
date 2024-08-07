package com.thur.demo.model.subscriptions;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.thur.demo.model.post.Post;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
@Repository
public class ServicesInfoDAOImpl implements ServicesInfoDAO {

    @PersistenceContext
    private EntityManager entityManager;

	@Override
	public long count(JSONObject obj) {
//		Integer id = obj.isNull("id") ? null : obj.getInt("id");
//		String name = obj.isNull("name") ? null : obj.getString("name");
//		Double startPrice = obj.isNull("startPrice") ? null : obj.getDouble("startPrice");
//		Double endPrice = obj.isNull("endPrice") ? null : obj.getDouble("endPrice");
//		String startMake = obj.isNull("startMake") ? null : obj.getString("startMake");
//		String endMake = obj.isNull("endMake") ? null : obj.getString("endMake");
//		Integer startExpire = obj.isNull("startExpire") ? null : obj.getInt("startExpire");
//		Integer endExpire = obj.isNull("endExpire") ? null : obj.getInt("endExpire");

		CriteriaBuilder criterBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criterBuilder.createQuery(Long.class);

//		from
		Root<ServicesInfo> table = criteriaQuery.from(ServicesInfo.class);

//		select count(*)
		criteriaQuery = criteriaQuery.select(criterBuilder.count(table));

//		where
		List<Predicate> predicates = new ArrayList<>();
//		if(id!=null) {
//			predicates.add(criterBuilder.equal(table.get("id"), id));
//		}
//		if(name!=null && name.length()!=0) {
//			predicates.add(criterBuilder.like(table.get("name"), "%"+name+"%"));
//		}
//		if(startPrice!=null) {
//			predicates.add(criterBuilder.greaterThan(table.get("price"), startPrice));
//		}
//		if(endPrice!=null) {
//			predicates.add(criterBuilder.lessThan(table.get("price"), endPrice));
//		}
//		if(startMake!=null && startMake.length()!=0) {
//			java.util.Date date = DatetimeConverter.parse(startMake, "yyyy-MM-dd");
//			predicates.add(criterBuilder.greaterThan(table.get("make"), date));
//		}
//		if(endMake!=null && endMake.length()!=0) {
//			java.util.Date date = DatetimeConverter.parse(endMake, "yyyy-MM-dd");
//			predicates.add(criterBuilder.lessThan(table.get("make"), date));
//		}
//		if(startExpire!=null) {
//			predicates.add(criterBuilder.greaterThan(table.get("expire"), startExpire));
//		}
//		if(endExpire!=null) {
//			predicates.add(criterBuilder.lessThan(table.get("expire"), endExpire));
//		}
		criteriaQuery = criteriaQuery.where(predicates.toArray(new Predicate[0]));
		
		TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery);
		Long result = typedQuery.getSingleResult();
		if(result!=null) {
			return result;			
		} else {
			return 0;
		}
	}

	@Override
	public List<ServicesInfo> find(JSONObject obj) {
		int start = obj.isNull("start") ? 0 : obj.getInt("start");
		int max = obj.isNull("max") ? 5 : obj.getInt("max");
		boolean dir = obj.isNull("dir") ? false : obj.getBoolean("dir");
		String order = obj.isNull("order") ? "id" : obj.getString("order");
		
		CriteriaBuilder criterBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ServicesInfo> criteriaQuery = criterBuilder.createQuery(ServicesInfo.class);

//		from
		Root<ServicesInfo> table = criteriaQuery.from(ServicesInfo.class);

//		where
		List<Predicate> predicates = new ArrayList<>();
		criteriaQuery = criteriaQuery.where(predicates.toArray(new Predicate[0]));
		
//		order by
		if(dir) {
			criteriaQuery = criteriaQuery.orderBy(criterBuilder.desc(table.get(order)));
		} else {
			criteriaQuery = criteriaQuery.orderBy(criterBuilder.asc(table.get(order)));
		}
		
		TypedQuery<ServicesInfo> typedQuery = entityManager.createQuery(criteriaQuery)
				.setFirstResult(start)
				.setMaxResults(max);
		List<ServicesInfo> result = typedQuery.getResultList();
		if(result!=null && !result.isEmpty()) {
			return result;
		} else {
			return null;
		}
	}
    
}
