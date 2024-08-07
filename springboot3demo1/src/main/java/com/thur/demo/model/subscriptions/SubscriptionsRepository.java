package com.thur.demo.model.subscriptions;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubscriptionsRepository extends JpaRepository<Subscriptions, Integer>{

}
