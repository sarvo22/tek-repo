package com.tekfilo.inventory.item;

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
public class ItemService {

    @Autowired
    ItemRepository itemRepository;

    public Page<ItemEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.itemRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }


    public ItemEntity save(ItemDto itemDto) throws Exception {
        return itemRepository.save(convertToEntity(itemDto));
    }

    private ItemEntity convertToEntity(ItemDto itemDto) {
        ItemEntity entity = new ItemEntity();
        BeanUtils.copyProperties(itemDto, entity);
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setIsDeleted(0);
        return entity;
    }

    public void modify(ItemDto itemDto) throws Exception {
        itemRepository.save(convertToEntity(itemDto));
    }

    public ItemEntity findById(Integer id) {
        return itemRepository.findById(id).orElse(null);
    }

    public void remove(ItemEntity entity) {
        itemRepository.save(entity);
    }


    public List<ItemEntity> findAllEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.itemRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    public void removeAll(List<ItemEntity> entities) throws Exception {
        this.itemRepository.saveAll(entities);
    }

    public void lock(List<ItemEntity> entities) throws Exception {
        this.itemRepository.saveAll(entities);
    }

    public void unlock(List<ItemEntity> entities) throws Exception {
        this.itemRepository.saveAll(entities);
    }

    public List<ItemEntity> getItemList(String searchKey) {
        List<ItemEntity> itemEntityList = this.itemRepository.findByItemName(
                (searchKey == null ? "%%" : searchKey.toLowerCase()), CompanyContext.getCurrentCompany());
        return (itemEntityList);
    }

}
