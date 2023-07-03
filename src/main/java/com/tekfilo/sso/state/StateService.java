package com.tekfilo.sso.state;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StateService {

    @Autowired
    StateRepository stateRepository;

    public List<StateEntity> findAllStates(Integer countryId) {
        return stateRepository.findStatesByCountryId(countryId);
    }
}
