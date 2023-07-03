package com.tekfilo.account.accmaster;

import com.tekfilo.account.base.FilterClause;
import com.tekfilo.account.multitenancy.CompanyContext;
import com.tekfilo.account.multitenancy.UserContext;
import com.tekfilo.account.transaction.bankpaymentreceipt.dto.BankPaymentReceiptBreakupDto;
import com.tekfilo.account.transaction.bankpaymentreceipt.dto.BankPaymentReceiptDetailDto;
import com.tekfilo.account.transaction.bankpaymentreceipt.dto.BankPaymentReceiptMainDto;
import com.tekfilo.account.transaction.bankpaymentreceipt.dto.BankRequestPayload;
import com.tekfilo.account.transaction.bankpaymentreceipt.entity.BankPaymentReceiptDetailEntity;
import com.tekfilo.account.transaction.bankpaymentreceipt.entity.BankPaymentReceiptMainEntity;
import com.tekfilo.account.transaction.clone.ClonePayload;
import com.tekfilo.account.util.AccountConstants;
import com.tekfilo.account.util.FilterClauseAppender;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AccMasterService implements IAccMasterService {

    @Autowired
    AccMasterRepository accMasterRepository;

    @Override
    public Page<AccMasterEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClauses) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.accMasterRepository.findAll(filterClauseAppender.getFilterClause(filterClauses), pageable);
    }

    @Override
    public AccMasterEntity save(AccMasterDto accMasterDto) throws Exception {
        return accMasterRepository.save(convertToEntity(accMasterDto));
    }

    private AccMasterEntity convertToEntity(AccMasterDto accMasterDto) {
        AccMasterEntity entity = new AccMasterEntity();
        BeanUtils.copyProperties(accMasterDto, entity);
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setSequence(accMasterDto.getSequence() == null ? 0 : accMasterDto.getSequence());
        entity.setIsMasterAccount(accMasterDto.getIsMasterAccount() == null ? 0 : accMasterDto.getIsMasterAccount());
        entity.setCostCenterApplicable(accMasterDto.getCostCenterApplicable() == null ? 0 : accMasterDto.getCostCenterApplicable());
        entity.setIsLocked(0);
        entity.setIsDeleted(0);
        return entity;
    }

    @Override
    public void modify(AccMasterDto accMasterDto) throws Exception {
        accMasterRepository.save(convertToEntity(accMasterDto));
    }

    @Override
    public AccMasterEntity findById(Integer id) {
        return accMasterRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(AccMasterEntity entity) throws Exception {
        accMasterRepository.save(entity);
    }

    @Override
    public List<AccMasterEntity> getAccountList(String searchKey) {
        List<AccMasterEntity> accMasterEntityList = this.accMasterRepository.findByAccountName(
                (searchKey == null ? "%%" : searchKey.toLowerCase()), CompanyContext.getCurrentCompany());
        return (accMasterEntityList);
    }

    private List<AccMasterDto> convert2AccountDto(List<AccMasterEntity> accMasterEntityList) {
        List<AccMasterDto> dtoList = new ArrayList<>();
        accMasterEntityList.stream().forEach(e -> {
            AccMasterDto dto = new AccMasterDto();
            dto.setId(e.getId());
            dto.setAccountCode(e.getAccountCode());
            dto.setAccountName(e.getAccountName());
            dto.setAccountNo(e.getAccountNo());
            dto.setIsLocked(e.getIsLocked());
            dtoList.add(dto);
        });
        return dtoList;
    }

    @Override
    public List<AccMasterEntity> findAllEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.accMasterRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    @Override
    public void removeAll(List<AccMasterEntity> entities) throws Exception {
        this.accMasterRepository.saveAll(entities);
    }

    @Override
    public void lock(List<AccMasterEntity> entities) throws Exception {
        this.accMasterRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<AccMasterEntity> entities) throws Exception {
        this.accMasterRepository.saveAll(entities);
    }

    @Override
    public void cloneAccountMaster(ClonePayload clonePayload) throws Exception {
        List<AccMasterEntity> accountMasterList = new ArrayList<>();
        if (clonePayload.getIds() != null) {
            if (clonePayload.getIds().size() > 0) {
                clonePayload.getIds().stream().forEach(id -> {
                    AccMasterDto dto = new AccMasterDto();
                    AccMasterEntity entity = this.accMasterRepository.findById(id).get();
                    BeanUtils.copyProperties(entity, dto);
                    dto.setId(null);
                    dto.setAccountCode("Clone_" + dto.getAccountCode());
                    accountMasterList.add(this.convertToEntity(dto));
                });
            }
        }
        accMasterRepository.saveAll(accountMasterList);
    }


}
