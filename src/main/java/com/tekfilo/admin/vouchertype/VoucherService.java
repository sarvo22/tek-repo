package com.tekfilo.admin.vouchertype;

import com.tekfilo.admin.base.FilterClause;
import com.tekfilo.admin.multitenancy.CompanyContext;
import com.tekfilo.admin.multitenancy.UserContext;
import com.tekfilo.admin.util.FilterClauseAppender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class VoucherService {

    @Autowired
    VoucherRepository voucherRepository;

    @Autowired
    AutoNumberGeneratorRepository autoNumberGeneratorRepository;

    public Page<VoucherTypeEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {

        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        Page<VoucherTypeEntity> pagedList = voucherRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
        pagedList.stream().forEach(e -> {
            e.setAutoNumberGenerator(this.getAutoNumberEntity(e));
        });
        return pagedList;
    }

    public VoucherTypeEntity findById(String vt) {
        VoucherTypeEntity voucherTypeEntity = this.voucherRepository.findById(vt).get();
        voucherTypeEntity.setAutoNumberGenerator(getAutoNumberEntity(voucherTypeEntity));
        return voucherTypeEntity;
    }

    private AutoNumberGeneratorEntity getAutoNumberEntity(VoucherTypeEntity entity) {
        List<AutoNumberGeneratorEntity> autoNumberGeneratorEntities = this.autoNumberGeneratorRepository.findByVoucherType(entity.getVoucherType(), entity.getCompanyId());
        if (autoNumberGeneratorEntities.size() > 0) {
            return autoNumberGeneratorEntities.get(0);
        }
        return new AutoNumberGeneratorEntity();
    }

    public VoucherTypeEntity save(VoucherTypeDto voucherTypeDto) throws Exception {
        VoucherTypeEntity entity = this.voucherRepository.save(convertDto2Entity(voucherTypeDto));
        if (Optional.ofNullable(entity.getVoucherType()).isPresent()) {
            this.autoNumberGeneratorRepository.save(convert2AutoNumberEntity(entity.getVoucherType(), voucherTypeDto.getVoucherStartNo()));
        } else {
            throw new RuntimeException("Unable to get Voucher Type for configuration Voucher Series");
        }
        return entity;
    }

    public int voucherTypeExist(VoucherTypeDto voucherTypeDto) {
        return this.voucherRepository.checkVoucherTypeExist(voucherTypeDto.getVoucherType(),
                CompanyContext.getCurrentCompany());
    }

    private AutoNumberGeneratorEntity convert2AutoNumberEntity(String voucherType, Integer autoNumber) {
        AutoNumberGeneratorEntity autoNumberGeneratorEntity = new AutoNumberGeneratorEntity();

        autoNumberGeneratorEntity.setId(null);
        autoNumberGeneratorEntity.setVoucherType(voucherType);
        autoNumberGeneratorEntity.setVoucherNumber(String.valueOf(autoNumber));
        autoNumberGeneratorEntity.setNextNumber(autoNumber + 1);
        autoNumberGeneratorEntity.setCurrentNumber(autoNumber);
        autoNumberGeneratorEntity.setCompanyId(CompanyContext.getCurrentCompany());
        autoNumberGeneratorEntity.setSequence(0);
        autoNumberGeneratorEntity.setIsLocked(0);
        autoNumberGeneratorEntity.setCreatedBy(UserContext.getLoggedInUser());
        autoNumberGeneratorEntity.setIsDeleted(0);
        return autoNumberGeneratorEntity;
    }

    private VoucherTypeEntity convertDto2Entity(VoucherTypeDto voucherTypeDto) {
        VoucherTypeEntity voucherType = new VoucherTypeEntity();
        voucherType.setVoucherType(voucherTypeDto.getVoucherType());
        voucherType.setVoucherName(voucherTypeDto.getVoucherName());
        voucherType.setVoucherGroup(voucherTypeDto.getVoucherGroup());
        voucherType.setCompanyId(CompanyContext.getCurrentCompany());
        voucherType.setIsLocked(0);
        voucherType.setSortSequence(0);
        voucherType.setCreatedBy(UserContext.getLoggedInUser());
        voucherType.setIsDeleted(0);
        return voucherType;
    }

    public String getNextNumber(String voucherType) {
        List<AutoNumberGeneratorEntity> entityList = autoNumberGeneratorRepository.findByVoucherType(
                voucherType, CompanyContext.getCurrentCompany()
        );
        if (entityList.size() > 0) {
            return String.format("%06d", entityList.get(0).getNextNumber());
        }
        return "XXXXXX";

    }
}
