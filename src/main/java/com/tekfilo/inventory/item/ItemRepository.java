package com.tekfilo.inventory.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Integer>, JpaSpecificationExecutor {

    @Query("select p from ItemEntity p where p.isDeleted = 0 and p.companyId  = :companyId and lower(p.itemName) LIKE :searchKey% ")
    List<ItemEntity> findByItemName(String searchKey, Integer companyId);

    @Modifying
    @Transactional
    @Query("update ItemEntity m set m.imageUrl = :documentUrls where m.id = :id")
    void updatePicturePath(Integer id, String documentUrls);
}
