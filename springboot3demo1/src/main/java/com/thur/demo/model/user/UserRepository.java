package com.thur.demo.model.user;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, UUID> {

	User findByuserAccount(String userAccount);

	@Query("Select u from User u WHERE u.Id = :id")
	User findByUserUUID(String id);
}
