package com.tekfilo.sso.user.repository;

import com.tekfilo.sso.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    List<UserEntity> findUserByUserName(String userName);

    @Query("select u from UserEntity u where u.userName = :username")
    List<UserEntity> checkUserExistOrNot(String username);

    @Query("select u from UserEntity u where u.isDeleted = 0 ")
    List<UserEntity> findAllUsers();

    @Query("select u from UserEntity u where email = :email")
    List<UserEntity> findUserByEmail(String email);

    @Transactional
    @Modifying
    @Query("update UserEntity set imagePath = :imagename where userId = :userId")
    void updateImagePath(String imagename, Integer userId);

    @Transactional
    @Modifying
    @Query("update UserEntity set contactNo = :contactNo,firstName=:firstName,lastName=:lastName where userId = :userId")
    void updateUserProfile(String firstName, String lastName, String contactNo, Integer userId);

    @Transactional
    @Modifying
    @Query("update UserEntity set isLocked = :locked where userId = :userId")
    void updateUserStatus(Integer userId, Integer locked);
}
