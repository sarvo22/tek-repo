package com.tekfilo.sso.parameter;

import com.tekfilo.sso.base.FilterClause;
import com.tekfilo.sso.util.FilterClauseAppender;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ParameterService {

    @Autowired
    ParameterRepository parameterRepository;

    public List<ParameterEntity> getBusinessTypeList() {
        return parameterRepository.getParameterList("BUSINESS_TYPE");
    }

    public List<ParameterEntity> getFiscalYearList() {
        return parameterRepository.getParameterList("FISCAL_YEAR");
    }

    public List<ParameterEntity> getTimeZoneList() {
        return parameterRepository.getParameterList("TIMEZONE");
    }

    public List<ParameterEntity> getDateTimeFormat() {
        return parameterRepository.getParameterList("DATE_TIME_FORMAT");
    }

    public List<ParameterEntity> getDateTimeDivider() {
        return parameterRepository.getParameterList("DATE_TIME_FORMAT_DIVIDER");
    }

    public List<ParameterEntity> getCompanyRegisterTypes() {
        return parameterRepository.getParameterList("COMPANY_REGISTER_TYPES");
    }

    public List<ParameterEntity> getCompanyTaxTypes() {
        return parameterRepository.getParameterList("COMPANY_TAX_TYPES");
    }

    public Page<ParameterEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.parameterRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }


    public ParameterEntity save(ParameterDto parameterDto) throws Exception {
        return parameterRepository.save(convertToEntity(parameterDto));
    }

    private ParameterEntity convertToEntity(ParameterDto parameterDto) {
        ParameterEntity entity = new ParameterEntity();
        BeanUtils.copyProperties(parameterDto, entity);
        entity.setSequence(parameterDto.getSequence() == null ? 0 : parameterDto.getSequence());
        entity.setIsLocked(parameterDto.getIsLocked() == null ? 0 : parameterDto.getIsLocked());
        entity.setIsDeleted(parameterDto.getIsDeleted() == null ? 0 : parameterDto.getIsDeleted());
        return entity;
    }


    public void modify(ParameterDto parameterDto) throws Exception {
        parameterRepository.save(convertToEntity(parameterDto));
    }


    public ParameterEntity findById(Integer id) {
        return parameterRepository.findById(id).orElse(null);
    }


    public void remove(ParameterEntity entity) throws Exception {
        parameterRepository.save(entity);
    }


    public void removeAll(List<ParameterEntity> entities) throws Exception {
        this.parameterRepository.saveAll(entities);
    }


    public void lock(List<ParameterEntity> entities) throws Exception {
        this.parameterRepository.saveAll(entities);
    }


    public void unlock(List<ParameterEntity> entities) throws Exception {
        this.parameterRepository.saveAll(entities);
    }
}
