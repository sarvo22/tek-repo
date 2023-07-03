package com.tekfilo.admin.dropdown;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@Service("iDropDownService")
@PropertySource("classpath:sql.properties")
@EnableCaching
public class DropDownRepository implements IDropDownService {

    @Autowired
    Environment environment;
    @Autowired
    private EntityManager entityManager;

    @Override
    //@Cacheable(value = "dropdown", key = "#key")
    public List<DropDownDto> dropDownList(String key) {
        List<DropDownDto> dropDownDtos = new ArrayList<>();
        Query query = entityManager.createNativeQuery(getDropDownQuery(key));
        List<Object[]> resultList = query.getResultList();
        resultList.stream().forEachOrdered(obj -> {
            dropDownDtos.add(new DropDownDto(obj[0].toString(), obj[1].toString()));
        });
        return dropDownDtos;
    }


    @Override
    public List<DropDownNumberDto> dropDownNumberList(String key) {
        List<DropDownNumberDto> dropDownDtos = new ArrayList<>();
        Query query = entityManager.createNativeQuery(getDropDownQuery(key));
        List<Object[]> resultList = query.getResultList();
        resultList.stream().forEachOrdered(obj -> {
            dropDownDtos.add(new DropDownNumberDto(Integer.parseInt(obj[0].toString()), obj[1].toString()));
        });
        return dropDownDtos;
    }

    @Override
    public List<DropDownDto> dropDownList(String key, Integer companyId) {
        List<DropDownDto> dropDownDtos = new ArrayList<>();
        Query query = entityManager.createNativeQuery(getDropDownQuery(key));
        query.setParameter("1", companyId);
        List<Object[]> resultList = query.getResultList();
        resultList.stream().forEachOrdered(obj -> {
            dropDownDtos.add(new DropDownDto(obj[0].toString(), obj[1].toString()));
        });
        return dropDownDtos;
    }


    public List<DropDownDto> dropDownList(String key, Map<String, Object> parameters) {
        List<DropDownDto> dropDownDtos = new ArrayList<>();
        Query query = entityManager.createNativeQuery(getDropDownQuery(key));
        parameters.entrySet().stream().forEachOrdered(kv -> {
            query.setParameter(kv.getKey(), kv.getValue());
        });
        List<Object[]> resultList = query.getResultList();
        resultList.stream().forEachOrdered(obj -> {
            dropDownDtos.add(new DropDownDto(obj[0].toString(), obj[1].toString()));
        });
        return dropDownDtos;
    }


    private String getDropDownQuery(String key) {
        return environment.getProperty(key.toUpperCase());
    }
}
