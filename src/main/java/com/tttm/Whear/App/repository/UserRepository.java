package com.tttm.Whear.App.repository;

import com.tttm.Whear.App.entity.User;
import com.tttm.Whear.App.enums.StatusGeneral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

  User getUserByEmail(String email);

  User getUserByUserID(String userID);

  User getUserByUsername(String username);

  User getUserByPhone(String phone);

  User getUserByEmailAndPassword(String email, String password);

  @Query(
          value = "select * from users where email = ?1 and status = ?2", nativeQuery = true
  )
  Optional<User> findUserByEmailAndActiveStatus(String email, String statusGeneral);
}
