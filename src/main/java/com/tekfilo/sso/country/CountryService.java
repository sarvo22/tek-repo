package com.tekfilo.sso.country;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CountryService {

    @Autowired
    CountryRepository countryRepository;

    public List<CountryEntity> findAllCountries() {
        return countryRepository.findAll();
    }
}
