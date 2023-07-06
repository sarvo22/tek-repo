package com.tekfilo.sso.state;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends JpaRepository<StateEntity, String> {

    @Query("select state from StateEntity state where state.isDeleted = 0 and state.countryId = :countryId")
    List<StateEntity> findStatesByCountryId(Integer countryId);
}
