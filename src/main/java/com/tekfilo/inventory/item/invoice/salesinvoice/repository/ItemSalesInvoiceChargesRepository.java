package com.tekfilo.inventory.item.invoice.salesinvoice.repository;

import com.tekfilo.inventory.item.invoice.salesinvoice.entity.ItemSalesInvoiceChargesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ItemSalesInvoiceChargesRepository extends JpaRepository<ItemSalesInvoiceChargesEntity, Integer> {

    @Query("select c from ItemSalesInvoiceChargesEntity c where c.isDeleted = 0 and c.invId = :invId")
    List<ItemSalesInvoiceChargesEntity> findAllCharges(Integer invId);

    @Modifying
    @Transactional
    @Query("update ItemSalesInvoiceChargesEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteChargesById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM ItemSalesInvoiceChargesEntity where isDeleted = 0 and invId in (:mainIds)")
    List<ItemSalesInvoiceChargesEntity> findAllChargesMainByIds(List<Integer> mainIds);
}
