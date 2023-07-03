package com.tekfilo.admin.settings.service;

import com.tekfilo.admin.multitenancy.CompanyContext;
import com.tekfilo.admin.multitenancy.UserContext;
import com.tekfilo.admin.settings.dto.DefaultChargesDto;
import com.tekfilo.admin.settings.entity.DefaultChargesEntity;
import com.tekfilo.admin.settings.repository.DefaultChargesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class SettingService {

    @Autowired
    DefaultChargesRepository defaultChargesRepository;

    public List<DefaultChargesEntity> getDefaultCharges(String invType) {
        return this.defaultChargesRepository.findAllByInvoiceType(invType);
    }

    public DefaultChargesEntity save(DefaultChargesDto defaultChargesDto) {
        return this.defaultChargesRepository.save(convertDefaultChargesDto2Entity(defaultChargesDto));
    }

    private DefaultChargesEntity convertDefaultChargesDto2Entity(DefaultChargesDto defaultChargesDto) {
        DefaultChargesEntity entity = new DefaultChargesEntity();
        entity.setInvType(defaultChargesDto.getInvType());
        entity.setChargeName(defaultChargesDto.getChargeName());
        entity.setPlusMinusFlag(defaultChargesDto.getPlusMinusFlag());
        entity.setInputPctAmountType(defaultChargesDto.getInputPctAmountType() == null ? "PCT" : defaultChargesDto.getInputPctAmountType());
        entity.setInputPctAmountValue(new BigDecimal(0.00));
        entity.setInputAmount(new BigDecimal(0.00));
        entity.setIsPartyPayable(defaultChargesDto.getIsPartyPayable());
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setIsDeleted(0);
        return entity;
    }
}
