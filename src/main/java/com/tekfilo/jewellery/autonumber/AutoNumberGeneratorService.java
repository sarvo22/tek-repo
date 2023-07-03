package com.tekfilo.jewellery.autonumber;

import com.tekfilo.jewellery.multitenancy.CompanyContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AutoNumberGeneratorService {

    @Autowired
    AutoNumberGeneratorRepository autoNumberGeneratorRepository;

    public String getNextNumber(String voucherType) throws Exception {
        List<AutoNumberGeneratorEntity> entityList = autoNumberGeneratorRepository.getNextNumber(voucherType, CompanyContext.getCurrentCompany());
        if (entityList.size() == 0)
            throw new RuntimeException("Unable generate Next Number for " + voucherType);
        String nextNumber = String.format("%05d", entityList.get(0).getNextNumber());
        updateNextNumber(entityList.get(0));
        return nextNumber;
    }

    private void updateNextNumber(AutoNumberGeneratorEntity autoNumberGeneratorEntity) throws Exception {
        Integer nextNumber = autoNumberGeneratorEntity.getNextNumber();
        Integer currentNumber = autoNumberGeneratorEntity.getCurrentNumber();
        autoNumberGeneratorEntity.setNextNumber(nextNumber + 1);
        autoNumberGeneratorEntity.setCurrentNumber(nextNumber);
        autoNumberGeneratorRepository.save(autoNumberGeneratorEntity);
    }

    public String getNextDisplayNumber(String invoiceType) {
        List<AutoNumberGeneratorEntity> entityList = autoNumberGeneratorRepository.getNextDisplayNumber(invoiceType, CompanyContext.getCurrentCompany());
        if (entityList.size() == 0) {
            return "";
        }
        String nextNumber = String.format("%05d", entityList.get(0).getNextNumber());
        return nextNumber;

    }
}
