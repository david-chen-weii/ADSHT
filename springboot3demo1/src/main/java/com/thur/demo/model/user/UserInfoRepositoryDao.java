package com.thur.demo.model.user;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepositoryDao {

    List<UserInfo> findWithCustomCriteria(String friend);

}
