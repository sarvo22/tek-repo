package com.tekfilo.admin.paymentterms;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

public class PaymentTermsServiceTest {

    PaymentTermsEntity db = new PaymentTermsEntity();
    PaymentTermsDto dto = new PaymentTermsDto();

    List<PaymentTermsEntity> dbEntityList = new ArrayList<>();

    private PaymentTermsRepository paymentTermsRepository = Mockito.mock(PaymentTermsRepository.class);
    PaymentTermsService paymentTermsService = new PaymentTermsService(paymentTermsRepository);

    @Before
    public void setUp() {

        db.setPaymentTypeName("Test Payment");
        db.setCompanyId(1000);
        db.setIsDeleted(0);
        db.setId(1001);

        dto.setId(1000);
        dto.setPaymentTypeName("Test Payment");
        dto.setIsDeleted(0);

        for (int i = 0; i < 5; i++) {
            PaymentTermsEntity entity = new PaymentTermsEntity();
            entity.setPaymentTypeName("Tesst" + i);
            entity.setIsLocked(0);
            entity.setCompanyId(1000);
            entity.setIsDeleted(0);
            dbEntityList.add(entity);
        }


    }


    @Test
    public void findAllPaymentList() {

    }

    @Test
    public void removeData() {
        Mockito.when(paymentTermsRepository.save(any(PaymentTermsEntity.class))).thenReturn(db);
        try {
            paymentTermsService.remove(db);
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void modifyData() {
        Mockito.when(paymentTermsRepository.save(any(PaymentTermsEntity.class))).thenReturn(db);
        try {
            paymentTermsService.modify(dto);
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }


    @Test
    public void save() {
        // Dummy database creation

        Mockito.when(paymentTermsRepository.save(any(PaymentTermsEntity.class))).thenReturn(db);
        try {
            PaymentTermsEntity expected = paymentTermsService.save(dto);
            org.assertj.core.api.Assertions.assertThat(expected.getPaymentTypeName()).isEqualTo(db.getPaymentTypeName());
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }


}