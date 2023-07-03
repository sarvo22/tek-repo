package com.tekfilo.inventory.item.invoice.salesreturn.repository;

import com.tekfilo.inventory.item.invoice.salesreturn.entity.ItemSalesReturnInvoiceChargesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ItemSalesReturnInvoiceChargesRepository extends JpaRepository<ItemSalesReturnInvoiceChargesEntity, Integer> {

    @Query("select c from ItemSalesReturnInvoiceChargesEntity c where c.isDeleted = 0 and c.invId = :invId")
    List<ItemSalesReturnInvoiceChargesEntity> findAllCharges(Integer invId);

    @Modifying
    @Transactional
    @Query("update ItemSalesReturnInvoiceChargesEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteChargesById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM ItemSalesReturnInvoiceChargesEntity where isDeleted = 0 and invId in (:mainIds)")
    List<ItemSalesReturnInvoiceChargesEntity> findAllChargesMainByIds(List<Integer> mainIds);
}
