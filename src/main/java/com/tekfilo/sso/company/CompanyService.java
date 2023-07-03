package com.tekfilo.sso.company;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    public CompanyEntity findById(Integer companyId) {
        return Optional.of(companyRepository.findById(companyId)).get().orElse(null);
    }

    public void saveCompany(CompanyDto companyDto) throws Exception {
        companyRepository.save(convertToEntity(companyDto));
    }

    private CompanyEntity convertToEntity(CompanyDto companyDto) {
        CompanyEntity entity = new CompanyEntity();
        BeanUtils.copyProperties(companyDto, entity);
        entity.setCreatedBy(companyDto.getOperateBy());
        entity.setModifiedBy(companyDto.getOperateBy());
        entity.setIsDeleted(companyDto.getIsDeleted() == null ? 0 : companyDto.getIsDeleted());
        return entity;
    }

}
