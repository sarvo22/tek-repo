package com.tekfilo.admin.dropdown;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface IDropDownService {

    List<DropDownDto> dropDownList(String key);

    List<DropDownNumberDto> dropDownNumberList(String key);

    List<DropDownDto> dropDownList(String key, Integer companyId);

    List<DropDownDto> dropDownList(String key, Map<String, Object> parameters);
}
