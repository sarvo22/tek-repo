package com.tekfilo.sso.registration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

/**
 * Repository for signup process and verification process
 *
 * @author Sivaraj
 */
@Repository
public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Integer> {

    @Transactional
    @Modifying
    @Query("UPDATE RegistrationEntity SET isTokenVerified = 1, tokenVerifiedDate = :date WHERE signupId = :signupId")
    Integer updateTokenStatus(Integer signupId, Timestamp date);

    @Query("select e from RegistrationEntity e where e.email = :email")
    List<RegistrationEntity> checkEmailExistOrNot(String email);
}
