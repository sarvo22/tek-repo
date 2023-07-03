package com.tekfilo.inventory.bin;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.multitenancy.CompanyContext;
import com.tekfilo.inventory.multitenancy.UserContext;
import com.tekfilo.inventory.util.FilterClauseAppender;
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
public class BinService {

    @Autowired
    BinRepository binRepository;

    public Integer save(BinDto binDto) throws Exception {
        return binRepository.save(convertToEntity(binDto)).getId();
    }
    public int checkNameExists(String binName) {
        return this.binRepository.checkNameExists(binName.toLowerCase());
    }

    private BinEntity convertToEntity(BinDto binDto) {
        BinEntity entity = new BinEntity();
        BeanUtils.copyProperties(binDto, entity);
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setIsDeleted(0);
        return entity;
    }

    public Page<BinEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.binRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);

    }

    public BinEntity findById(Integer binId) {
        return this.binRepository.findById(binId).get();
    }

    public void remove(BinEntity entity) {
        this.binRepository.save(entity);
    }


    public List<BinEntity> findAllEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.binRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    public void removeAll(List<BinEntity> entities) throws Exception {
        this.binRepository.saveAll(entities);
    }

    public void lock(List<BinEntity> entities) throws Exception {
        this.binRepository.saveAll(entities);
    }

    public void unlock(List<BinEntity> entities) throws Exception {
        this.binRepository.saveAll(entities);
    }
}
