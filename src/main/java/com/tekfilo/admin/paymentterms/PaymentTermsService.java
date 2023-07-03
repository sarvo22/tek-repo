package com.tekfilo.admin.paymentterms;

import com.tekfilo.admin.base.FilterClause;
import com.tekfilo.admin.multitenancy.CompanyContext;
import com.tekfilo.admin.multitenancy.UserContext;
import com.tekfilo.admin.util.FilterClauseAppender;
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
public class PaymentTermsService implements IPaymentTermsService {

    @Autowired
    PaymentTermsRepository paymentTermsRepository;

    public PaymentTermsService(PaymentTermsRepository paymentTermsRepository) {
        this.paymentTermsRepository = paymentTermsRepository;
    }

    @Override
    public Page<PaymentTermsEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection,
                                            List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return paymentTermsRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public PaymentTermsEntity save(PaymentTermsDto paymentTermsDto) throws Exception {
        return paymentTermsRepository.save(convertToEntity(paymentTermsDto));
    }

    private PaymentTermsEntity convertToEntity(PaymentTermsDto paymentTermsDto) {
        PaymentTermsEntity entity = new PaymentTermsEntity();
        BeanUtils.copyProperties(paymentTermsDto, entity);
        entity.setSequence(paymentTermsDto.getSequence() == null ? 0 : paymentTermsDto.getSequence());
        entity.setIsLocked(paymentTermsDto.getIsLocked() == null ? 0 : paymentTermsDto.getIsLocked());
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(paymentTermsDto.getIsDeleted() == null ? 0 : paymentTermsDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modify(PaymentTermsDto paymentTermsDto) throws Exception {
        paymentTermsRepository.save(convertToEntity(paymentTermsDto));
    }

    @Override
    public PaymentTermsEntity findById(Integer id) {
        return paymentTermsRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(PaymentTermsEntity entity) throws Exception {
        paymentTermsRepository.save(entity);
    }


    @Override
    public List<PaymentTermsEntity> findAllEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.paymentTermsRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    @Override
    public void removeAll(List<PaymentTermsEntity> entities) throws Exception {
        this.paymentTermsRepository.saveAll(entities);
    }

    @Override
    public void lock(List<PaymentTermsEntity> entities) throws Exception {
        this.paymentTermsRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<PaymentTermsEntity> entities) throws Exception {
        this.paymentTermsRepository.saveAll(entities);
    }
}
