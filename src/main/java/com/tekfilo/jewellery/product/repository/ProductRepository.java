package com.tekfilo.jewellery.product.repository;

import com.tekfilo.jewellery.product.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
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
    @Query("update ProductEntity m set m.picturePath = :imageUrls where m.id = :id")
    void updatePicturePath(Integer id, String imageUrls);

    @Query("FROM ProductEntity as e WHERE e.productNo IN (:names) and companyId = :companyId")
    List<ProductEntity> findByJewNos(@Param("names") List<String> names, Integer companyId);
}
