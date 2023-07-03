package com.tekfilo.inventory.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<ProductEntity, Integer>, JpaSpecificationExecutor {

    @Query("select product from ProductEntity product where product.isDeleted = 0")
    Page<ProductEntity> findAllProducts(Pageable pageable);

    @Query("select p from ProductEntity p where p.isDeleted = 0 and p.companyId  = :companyId and lower(p.productNo) LIKE :searchKey% ")
    List<ProductEntity> findByProductName(String searchKey, Integer companyId);

    @Modifying
    @Transactional
    @Query("update ProductEntity m set m.picture1Path = :documentUrls where m.id = :id")
    void updatepicture1Path(Integer id, String documentUrls);

    @Modifying
    @Transactional
    @Query("update ProductEntity m set m.picture2Path = :documentUrls where m.id = :id")
    void updatepicture2Path(Integer id, String documentUrls);

    @Modifying
    @Transactional
    @Query("update ProductEntity m set m.picture3Path = :documentUrls where m.id = :id")
    void updatepicture3Path(Integer id, String documentUrls);

    @Modifying
    @Transactional
    @Query("update ProductEntity m set m.picture4Path = :documentUrls where m.id = :id")
    void updatepicture4Path(Integer id, String documentUrls);


    @Modifying
    @Transactional
    @Query("update ProductEntity m set m.documentUrls = :documentUrls where m.id = :id")
    void updateDocumentUrl(Integer id, String documentUrls);

    @Query("select count(1) from ProductEntity where lower(productNo) = :productNo")
    int checkLotCodeExists(String productNo);
}
