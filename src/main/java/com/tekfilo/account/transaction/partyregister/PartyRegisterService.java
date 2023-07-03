package com.tekfilo.account.transaction.partyregister;

import com.tekfilo.account.multitenancy.CompanyContext;
import com.tekfilo.account.multitenancy.UserContext;
import com.tekfilo.account.transaction.common.PartyRegisterDto;
import com.tekfilo.account.util.AccountConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class PartyRegisterService {

    @Autowired
    PartyRegisterMainRepository partyRegisterMainRepository;


    public PartyRegisterMainEntity createPartyAccountRegister(PartyRegisterDto partyRegisterDto) throws Exception {
        PartyRegisterMainEntity entity = getPartyRegisterEntity(partyRegisterDto);
        return this.partyRegisterMainRepository.save(entity);
    }

    private PartyRegisterMainEntity getPartyRegisterEntity(PartyRegisterDto partyRegisterDto) {
        PartyRegisterMainEntity entity = new PartyRegisterMainEntity();
        entity.setInvoiceId(partyRegisterDto.getInvoiceId());
        entity.setInvoiceType(partyRegisterDto.getInvoiceType());
        entity.setInvoiceTypeId(partyRegisterDto.getInvoiceType().concat("-").concat(partyRegisterDto.getInvoiceId().toString()));
        if(partyRegisterDto.getInvoiceNo().contains("-")){
            entity.setInvoiceNo(partyRegisterDto.getInvoiceNo().split("-")[1]);
        }else{
            entity.setInvoiceNo(partyRegisterDto.getInvoiceNo());
        }
        entity.setInvoiceDate(partyRegisterDto.getInvoiceDate());
        entity.setInvoiceDueDate(partyRegisterDto.getInvoiceDueDate());
        entity.setReferenceNo(partyRegisterDto.getReferenceNo());
        entity.setCurrency(partyRegisterDto.getCurrency());
        entity.setExchangeRate(partyRegisterDto.getExchangeRate());
        entity.setInvoiceDueMonths(partyRegisterDto.getInvoiceDueMonths() == null ? 0 : partyRegisterDto.getInvoiceDueMonths());
        entity.setInvoiceDueDays(partyRegisterDto.getInvoiceDueDays() == null ? 0 : partyRegisterDto.getInvoiceDueDays());
        entity.setPaymentTypeId(partyRegisterDto.getPaymentTypeId());
        entity.setPartyType(partyRegisterDto.getPartyType());
        entity.setPartyId(partyRegisterDto.getPartyId());
        entity.setPartyTypeId(partyRegisterDto.getPartyType().concat("-").concat(partyRegisterDto.getPartyId().toString()));
        entity.setStatus(partyRegisterDto.getStatus() == null ? AccountConstants.UNPAID : partyRegisterDto.getStatus());
        entity.setInOutFlag(partyRegisterDto.getInOutFlag());
        entity.setInvoiceAmount(partyRegisterDto.getInvoiceAmount() == null ? 0 : partyRegisterDto.getInvoiceAmount());
        entity.setSettlementAmount(partyRegisterDto.getSettlementAmount() == null ? 0 : partyRegisterDto.getSettlementAmount());
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }
    public List<PartyRegisterMainEntity> findAllCustomerPendingList(Integer customerId, String currency) {
        return this.partyRegisterMainRepository.findallCustomerPendingList(customerId, currency);
    }

    public List<PartyRegisterMainEntity> findAllSupplierPendingList(Integer supplierId, String currency) {
        return this.partyRegisterMainRepository.findallSupplierPendingList(supplierId, currency);
    }

    public List<PartyRegisterMainEntity> findAllCustomerPendingList(Integer customerId) {
        return this.partyRegisterMainRepository.findallCustomerPendingList(customerId);
    }

    public List<PartyRegisterMainEntity> findAllSupplierPendingList(Integer supplierId) {
        return this.partyRegisterMainRepository.findallSupplierPendingList(supplierId);
    }
}
