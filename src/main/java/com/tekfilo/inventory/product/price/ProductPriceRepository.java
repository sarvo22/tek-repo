package com.tekfilo.inventory.product.price;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPriceEntity, Integer> {

    @Query("select p from ProductPriceEntity p where isDeleted = 0 and productId = :productId")
    List<ProductPriceEntity> findProductPriceByProduct(Integer productId);
}
