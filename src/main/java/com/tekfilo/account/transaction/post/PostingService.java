package com.tekfilo.account.transaction.post;

import com.tekfilo.account.multitenancy.CompanyContext;
import com.tekfilo.account.multitenancy.UserContext;
import com.tekfilo.account.transaction.bankpaymentreceipt.entity.BankPaymentReceiptDetailEntity;
import com.tekfilo.account.transaction.bankpaymentreceipt.entity.BankPaymentReceiptMainEntity;
import com.tekfilo.account.transaction.bankpaymentreceipt.repository.BankPaymentReceiptBreakupRepository;
import com.tekfilo.account.transaction.bankpaymentreceipt.repository.BankPaymentReceiptDetailRepository;
import com.tekfilo.account.transaction.bankpaymentreceipt.repository.BankPaymentReceiptMainRepository;
import com.tekfilo.account.transaction.cashpaymentreceipt.entity.CashPaymentReceiptDetailEntity;
import com.tekfilo.account.transaction.cashpaymentreceipt.entity.CashPaymentReceiptMainEntity;
import com.tekfilo.account.transaction.cashpaymentreceipt.repository.CashPaymentReceiptBreakupRepository;
import com.tekfilo.account.transaction.cashpaymentreceipt.repository.CashPaymentReceiptDetailRepository;
import com.tekfilo.account.transaction.cashpaymentreceipt.repository.CashPaymentReceiptMainRepository;
import com.tekfilo.account.transaction.debitcreditnote.entity.DebitCreditNoteDetailEntity;
import com.tekfilo.account.transaction.debitcreditnote.entity.DebitCreditNoteMainEntity;
import com.tekfilo.account.transaction.debitcreditnote.repository.DebitCreditNoteBreakupRepository;
import com.tekfilo.account.transaction.debitcreditnote.repository.DebitCreditNoteDetailRepository;
import com.tekfilo.account.transaction.debitcreditnote.repository.DebitCreditNoteMainRepository;
import com.tekfilo.account.transaction.jv.entity.JVDetailEntity;
import com.tekfilo.account.transaction.jv.entity.JVMainEntity;
import com.tekfilo.account.transaction.jv.repository.JVBreakupRepository;
import com.tekfilo.account.transaction.jv.repository.JVDetailRepository;
import com.tekfilo.account.transaction.jv.repository.JVMainRepository;
import com.tekfilo.account.util.AccountConstants;
import com.tekfilo.account.util.AccountsUtil;
import com.tekfilo.account.util.FilterClauseAppender;
import com.tekfilo.account.vouchertype.VoucherTypeEntity;
import com.tekfilo.account.vouchertype.VoucherTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class PostingService {


    @Autowired
    PostingMainRepository postingMainRepository;

    @Autowired
    PostingDetailRepository postingDetailRepository;

    @Autowired
    CashPaymentReceiptMainRepository cashPaymentReceiptMainRepository;

    @Autowired
    CashPaymentReceiptDetailRepository cashPaymentReceiptDetailRepository;

    @Autowired
    BankPaymentReceiptMainRepository bankPaymentReceiptMainRepository;

    @Autowired
    BankPaymentReceiptDetailRepository bankPaymentReceiptDetailRepository;

    @Autowired
    VoucherTypeRepository voucherTypeRepository;

    @Autowired
    JVMainRepository jvMainRepository;

    @Autowired
    JVDetailRepository jvDetailRepository;

    @Autowired
    JVBreakupRepository jvBreakupRepository;

    @Autowired
    CashPaymentReceiptBreakupRepository cashPaymentReceiptBreakupRepository;

    @Autowired
    BankPaymentReceiptBreakupRepository bankPaymentReceiptBreakupRepository;

    @Autowired
    DebitCreditNoteMainRepository debitCreditNoteMainRepository;

    @Autowired
    DebitCreditNoteDetailRepository debitCreditNoteDetailRepository;

    @Autowired
    DebitCreditNoteBreakupRepository debitCreditNoteBreakupRepository;


    public void unPostAccounting(final String invoiceType, final Integer invoiceId) throws Exception {
        List<PostingMainEntity> entityList = postingMainRepository.findAllByVoucher(invoiceType, invoiceId, CompanyContext.getCurrentCompany());
        if (entityList.size() == 0) {
            this.changeAccountingStatus(invoiceType, invoiceId, AccountConstants.DRAFT);
            return;
        }
        List<Integer> postingMainIdList = entityList.stream().map(PostingMainEntity::getId).collect(Collectors.toList());
        List<PostingDetailEntity> detailEntityList = postingDetailRepository.findAll(AccountsUtil.getInClassFilter("postingId", postingMainIdList));
        postingDetailRepository.deleteAll(detailEntityList);
        postingMainRepository.deleteAll(entityList);
        this.changeAccountingStatus(invoiceType, invoiceId, AccountConstants.DRAFT);
    }

    public void unPostAccounting(PostingDeleteDto postingDeleteDto) throws Exception {
        List<Integer> ids = postingDeleteDto.getVoucherIdList().stream().distinct().collect(Collectors.toList());
        List<String> types = postingDeleteDto.getVoucherTypeList().stream().distinct().collect(Collectors.toList());
        List<PostingMainEntity> entityList = postingMainRepository.findAllByVoucherList(types, ids, CompanyContext.getCurrentCompany());
        if (entityList.size() == 0) {
            return;
        }
        entityList.stream().forEach(entity -> {
            try {
                this.changeAccountingStatus(entity.getInvoiceType(), entity.getId(), AccountConstants.DRAFT);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        List<PostingDetailEntity> detailEntityList = postingDetailRepository.findAll(AccountsUtil.getInClassFilter("postingId",
                entityList.stream().map(PostingMainEntity::getId).collect(Collectors.toList())));
        postingDetailRepository.deleteAll(detailEntityList);
        postingMainRepository.deleteAll(entityList);
    }


    public void postAccounting(final String invoiceType, final Integer invoiceId) throws Exception {
        if (invoiceType == null)
            throw new RuntimeException("Voucher Type must be passed");
        if (invoiceId == null) {
            throw new RuntimeException("Voucher ID must be passed");
        }
        unPostAccounting(invoiceType, invoiceId);
        PostingMainEntity mainEntity = getPostingMainEntity(invoiceType, invoiceId);
        if (mainEntity.getInvId() == null || mainEntity.getIsDeleted() == 1) {
            throw new RuntimeException("Given Voucher not found in Main table, either It's Deleted or does not exists");
        }
        if (mainEntity.getCompanyId().intValue() != CompanyContext.getCurrentCompany().intValue()) {
            throw new RuntimeException("Logged In Company is not matching with Voucher entered company !");
        }
        List<PostingDetailEntity> postingDetailEntityList = getPostingDetailEntityList(invoiceType, invoiceId, mainEntity.getHeaderAccountId());
        final Double debitAmount = postingDetailEntityList.stream().mapToDouble(mp -> mp.getInOutFlag() == 1 ? mp.getAmount() : 0).sum();
        final Double creditAmount = postingDetailEntityList.stream().mapToDouble(mp -> mp.getInOutFlag() == -1 ? mp.getAmount() : 0).sum();
        if (debitAmount.doubleValue() - creditAmount.doubleValue() != 0) {
            throw new RuntimeException("Total Debit Amount and Credit Amount not matching for Posting Accounting entry ! Actual values Debit = " + debitAmount + ";Credit = " + creditAmount);
        }
        PostingMainEntity postingMainEntity = postingMainRepository.save(mainEntity);
        setPostingMain2DetailList(postingMainEntity.getId(), postingDetailEntityList);
        postingDetailRepository.saveAll(postingDetailEntityList);
        changeAccountingStatus(invoiceType, invoiceId, AccountConstants.POSTED);
    }


    @Deprecated
    private VoucherTypeEntity getVoucherTypeList(String invoiceType, Integer currentCompany) throws Exception {
        List<VoucherTypeEntity> voucherTypeEntities = voucherTypeRepository.findVoucherType(invoiceType, CompanyContext.getCurrentCompany());
        if (voucherTypeEntities.size() == 0) {
            throw new RuntimeException("There is no Voucher Type configuration found for Invoice Type " + invoiceType);
        }
        if (voucherTypeEntities.size() > 1) {
            throw new RuntimeException("More than one Voucher Type configuration found for Invoice Type " + invoiceType);
        }
        return voucherTypeEntities.get(0);
    }

    private void setPostingMain2DetailList(Integer postingMainId, List<PostingDetailEntity> postingDetailEntityList) {
        postingDetailEntityList.stream().forEach(e -> {
            e.setPostingId(postingMainId);
        });
    }


    private PostingMainEntity getPostingMainEntity(String invoiceType, Integer invoiceId) throws Exception {
        PostingMainEntity mainEntity = new PostingMainEntity();
        switch (PostingTypes.valueOf(invoiceType.toUpperCase())) {
            case JV:
                JVMainEntity jvMainEntity = jvMainRepository.findById(invoiceId).get();
                mainEntity.setId(null);
                mainEntity.setInvoiceType(jvMainEntity.getInvoiceType());
                mainEntity.setInvId(jvMainEntity.getId());
                mainEntity.setInvId(jvMainEntity.getId());
                mainEntity.setInvoiceDate(jvMainEntity.getInvoiceDate());
                mainEntity.setInvoiceDueDate(jvMainEntity.getInvoiceDueDate());
                mainEntity.setCurrency(jvMainEntity.getCurrency());
                mainEntity.setExchangeRate(jvMainEntity.getExchangeRate());
                mainEntity.setReferenceNo(jvMainEntity.getReferenceNo());
                mainEntity.setInvoiceNo(jvMainEntity.getInvoiceNo());
                mainEntity.setNote1(jvMainEntity.getNote());
                mainEntity.setCompanyId(jvMainEntity.getCompanyId());
                mainEntity.setCreatedBy(UserContext.getLoggedInUser());
                mainEntity.setSequence(0);
                mainEntity.setIsLocked(0);
                mainEntity.setIsDeleted(0);
                break;
            case CP:
            case CR:
            case PCP:
            case PCR:
                CashPaymentReceiptMainEntity cashPaymentReceiptMainEntity = cashPaymentReceiptMainRepository.findById(invoiceId).get();
                mainEntity.setId(null);
                mainEntity.setInvoiceType(cashPaymentReceiptMainEntity.getInvoiceType());
                mainEntity.setInvId(cashPaymentReceiptMainEntity.getId());
                mainEntity.setInvoiceDate(cashPaymentReceiptMainEntity.getInvoiceDate());
                mainEntity.setInvoiceDueDate(cashPaymentReceiptMainEntity.getInvoiceDueDate());
                mainEntity.setCurrency(cashPaymentReceiptMainEntity.getCurrency());
                mainEntity.setExchangeRate(cashPaymentReceiptMainEntity.getExchangeRate());
                mainEntity.setReferenceNo(cashPaymentReceiptMainEntity.getReferenceNo());
                mainEntity.setInvoiceNo(cashPaymentReceiptMainEntity.getInvoiceNo());
                mainEntity.setNote1(cashPaymentReceiptMainEntity.getNote());
                mainEntity.setCompanyId(cashPaymentReceiptMainEntity.getCompanyId());
                mainEntity.setCreatedBy(UserContext.getLoggedInUser());
                mainEntity.setSequence(0);
                mainEntity.setIsLocked(0);
                mainEntity.setIsDeleted(0);
                mainEntity.setHeaderAccountId(cashPaymentReceiptMainEntity.getCashAccountId());
                break;
            case BP:
            case BR:
                BankPaymentReceiptMainEntity bankPaymentReceiptMainEntity = bankPaymentReceiptMainRepository.findById(invoiceId).get();
                mainEntity.setId(null);
                mainEntity.setInvoiceType(bankPaymentReceiptMainEntity.getInvoiceType());
                mainEntity.setInvId(bankPaymentReceiptMainEntity.getId());
                mainEntity.setInvoiceDate(bankPaymentReceiptMainEntity.getInvoiceDate());
                mainEntity.setInvoiceDueDate(bankPaymentReceiptMainEntity.getInvoiceDueDate());
                mainEntity.setCurrency(bankPaymentReceiptMainEntity.getCurrency());
                mainEntity.setExchangeRate(bankPaymentReceiptMainEntity.getExchangeRate());
                mainEntity.setReferenceNo(bankPaymentReceiptMainEntity.getReferenceNo());
                mainEntity.setInvoiceNo(bankPaymentReceiptMainEntity.getInvoiceNo());
                mainEntity.setNote1(bankPaymentReceiptMainEntity.getNote());
                mainEntity.setCompanyId(bankPaymentReceiptMainEntity.getCompanyId());
                mainEntity.setCreatedBy(UserContext.getLoggedInUser());
                mainEntity.setSequence(0);
                mainEntity.setIsLocked(0);
                mainEntity.setIsDeleted(0);
                mainEntity.setHeaderAccountId(bankPaymentReceiptMainEntity.getBankAccountId());
                break;
            case NDR:
            case NCR:
                DebitCreditNoteMainEntity debitCreditNoteMainEntity = this.debitCreditNoteMainRepository.findById(invoiceId).get();
                mainEntity.setId(null);
                mainEntity.setInvoiceType(debitCreditNoteMainEntity.getInvoiceType());
                mainEntity.setInvId(debitCreditNoteMainEntity.getId());
                mainEntity.setInvoiceDate(debitCreditNoteMainEntity.getInvoiceDate());
                mainEntity.setInvoiceDueDate(debitCreditNoteMainEntity.getInvoiceDueDate());
                mainEntity.setCurrency(debitCreditNoteMainEntity.getCurrency());
                mainEntity.setExchangeRate(debitCreditNoteMainEntity.getExchangeRate());
                mainEntity.setReferenceNo(debitCreditNoteMainEntity.getReferenceNo());
                mainEntity.setInvoiceNo(debitCreditNoteMainEntity.getInvoiceNo());
                mainEntity.setNote1(debitCreditNoteMainEntity.getNote());
                mainEntity.setCompanyId(debitCreditNoteMainEntity.getCompanyId());
                mainEntity.setCreatedBy(UserContext.getLoggedInUser());
                mainEntity.setSequence(0);
                mainEntity.setIsLocked(0);
                mainEntity.setIsDeleted(0);
                if (debitCreditNoteMainEntity.getPartyAccountId() == null) {
                    throw new RuntimeException(debitCreditNoteMainEntity.getPartyType().concat(" Account not available for Posting Accounting entries"));
                }
                mainEntity.setHeaderAccountId(debitCreditNoteMainEntity.getPartyId());
                break;
        }
        return mainEntity;
    }

    public List<JVDetailEntity> findAllDetailByMainId(Integer id) {
        List<JVDetailEntity> detailEntities = this.jvDetailRepository.findAllByMainId(id);
        detailEntities.stream().forEach(e -> {
            FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
            List<Integer> ids = new ArrayList<>();
            ids.add(e.getId());
            e.setBreakupList(jvBreakupRepository.findAll(filterClauseAppender.getCustomInClassFilter("jvDetailId", ids)));
        });
        return detailEntities;
    }

    public List<CashPaymentReceiptDetailEntity> findAllCashDetailByMainId(Integer id) {
        List<CashPaymentReceiptDetailEntity> detailEntities = this.cashPaymentReceiptDetailRepository.findAllByMainId(id);
        detailEntities.stream().forEach(e -> {
            FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
            List<Integer> ids = new ArrayList<>();
            ids.add(e.getId());
            e.setBreakupList(cashPaymentReceiptBreakupRepository.findAll(filterClauseAppender.getCustomInClassFilter("invoiceDetailId", ids)));
        });
        return detailEntities;
    }

    public List<BankPaymentReceiptDetailEntity> findAllBankDetailByMainId(Integer id) {
        List<BankPaymentReceiptDetailEntity> detailEntities = this.bankPaymentReceiptDetailRepository.findAllByMainId(id);
        detailEntities.stream().forEach(e -> {
            FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
            List<Integer> ids = new ArrayList<>();
            ids.add(e.getId());
            e.setBreakupList(bankPaymentReceiptBreakupRepository.findAll(filterClauseAppender.getCustomInClassFilter("invoiceDetailId", ids)));
        });
        return detailEntities;
    }

    public List<DebitCreditNoteDetailEntity> findAllDebitCreditNoteDetailByMainId(Integer id) {
        List<DebitCreditNoteDetailEntity> detailEntities = this.debitCreditNoteDetailRepository.findAllByMainId(id);
        detailEntities.stream().forEach(e -> {
            FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
            List<Integer> ids = new ArrayList<>();
            ids.add(e.getId());
            e.setBreakupList(debitCreditNoteBreakupRepository.findAll(filterClauseAppender.getCustomInClassFilter("invoiceDetailId", ids)));
        });
        return detailEntities;
    }

    private List<PostingDetailEntity> getPostingDetailEntityList(String invoiceType, Integer invoiceId, final Integer headerAccountId) throws Exception {
        List<PostingDetailEntity> postingDetailEntityList = new ArrayList<>();
        PostingDetailEntity totalEntity = new PostingDetailEntity();
        switch (PostingTypes.valueOf(invoiceType.toUpperCase())) {
            case JV:
                List<JVDetailEntity> jvDetailEntityList = this.findAllDetailByMainId(invoiceId);
                jvDetailEntityList.stream().forEach(jv -> {
                    if (jv.getAccount().getCostCenterApplicable() == 1 && (jv.getBreakupList() == null || jv.getBreakupList().size() == 0)) {
                        throw new RuntimeException("Account : ".concat(jv.getAccount().getAccountName())
                                .concat(" does not have Breakup details"));
                    }
                });
                jvDetailEntityList.stream().forEach(jv -> {
                    if (jv.getBreakupList() != null) {
                        if (jv.getBreakupList().size() > 0) {
                            final double detailDebitAmount = jv.getDebitAmount() == null ? 0.00 : jv.getDebitAmount().doubleValue();
                            final double detailCreditAmount = jv.getCreditAmount() == null ? 0.00 : jv.getCreditAmount().doubleValue();
                            final double breakupDebitAmount = jv.getBreakupList().stream().mapToDouble(a -> a.getDebitAmount() == null ? 0.00 : a.getDebitAmount()).sum();
                            final double breakupCreditAmount = jv.getBreakupList().stream().mapToDouble(b -> b.getCreditAmount() == null ? 0.00 : b.getCreditAmount()).sum();
                            if (Math.abs(breakupDebitAmount - detailDebitAmount) > 0) {
                                throw new RuntimeException("Account : ".concat(jv.getAccount().getAccountName()).concat("Detail Debit Amount ")
                                        .concat(String.valueOf(detailDebitAmount))
                                        .concat(",Cost Breakup provided ")
                                        .concat(String.valueOf(breakupDebitAmount))
                                        .concat(" not matching"));
                            }
                            if (Math.abs(breakupCreditAmount - detailCreditAmount) > 0) {
                                throw new RuntimeException("Account : ".concat(jv.getAccount().getAccountName()).concat("Detail Credit Amount ")
                                        .concat(String.valueOf(detailCreditAmount))
                                        .concat(",Cost Breakup provided ")
                                        .concat(String.valueOf(breakupCreditAmount))
                                        .concat(" not matching"));
                            }
                        }
                    }
                });
                jvDetailEntityList.stream().forEachOrdered(dr -> {
                    if (dr.getAccount().getCostCenterApplicable() == 1) {
                        setJVDebitCostBreakupDetails(postingDetailEntityList, dr);
                    } else {
                        PostingDetailEntity entity = new PostingDetailEntity();
                        final double debitAmount = dr.getDebitAmount() == null ? 0 : dr.getDebitAmount().doubleValue();
                        entity.setId(null);
                        entity.setPostingId(null);
                        entity.setAccountId(dr.getAccountId());
                        entity.setInOutFlag(debitAmount < 0 ? AccountConstants.OUT : AccountConstants.IN);
                        entity.setDrCr(debitAmount < 0 ? AccountConstants.CREDIT : AccountConstants.DEBIT);
                        entity.setAmount(Math.abs(debitAmount));
                        entity.setSequence(2);
                        entity.setCreatedBy(UserContext.getLoggedInUser());
                        entity.setIsLocked(0);
                        entity.setIsDeleted(0);
                        postingDetailEntityList.add(entity);
                    }
                });

                jvDetailEntityList.stream().forEachOrdered(cr -> {
                    if (cr.getAccount().getCostCenterApplicable() == 1) {
                        setJVCreditCostBreakupDetails(postingDetailEntityList, cr);
                    } else {
                        PostingDetailEntity entity = new PostingDetailEntity();
                        final double creditAmount = cr.getCreditAmount() == null ? 0 : cr.getCreditAmount().doubleValue();
                        entity.setId(null);
                        entity.setPostingId(null);
                        entity.setAccountId(cr.getAccountId());
                        entity.setInOutFlag(creditAmount < 0 ? AccountConstants.IN : AccountConstants.OUT);
                        entity.setDrCr(creditAmount < 0 ? AccountConstants.DEBIT : AccountConstants.CREDIT);
                        entity.setAmount(Math.abs(creditAmount));
                        entity.setSequence(2);
                        entity.setCreatedBy(UserContext.getLoggedInUser());
                        entity.setIsLocked(0);
                        entity.setIsDeleted(0);
                        postingDetailEntityList.add(entity);
                    }
                });
                break;
            case CP:
            case PCP:
            case CR:
            case PCR:
                List<CashPaymentReceiptDetailEntity> cashPaymentReceiptDetailEntityList = this.findAllCashDetailByMainId(invoiceId);
                final Double totalGrossAmount = cashPaymentReceiptDetailEntityList.stream().mapToDouble(e -> Double.valueOf(e.getGrossAmount().toString())).sum();
                totalEntity.setId(null);
                totalEntity.setPostingId(null);
                totalEntity.setAccountId(headerAccountId);
                switch (PostingTypes.valueOf(invoiceType.toUpperCase())) {
                    case CP:
                    case PCP:
                        totalEntity.setInOutFlag(totalGrossAmount < 0 ? AccountConstants.IN : AccountConstants.OUT);
                        totalEntity.setDrCr(totalGrossAmount < 0 ? AccountConstants.DEBIT : AccountConstants.CREDIT);
                        break;
                    case CR:
                    case PCR:
                        totalEntity.setInOutFlag(totalGrossAmount < 0 ? AccountConstants.OUT : AccountConstants.IN);
                        totalEntity.setDrCr(totalGrossAmount < 0 ? AccountConstants.CREDIT : AccountConstants.DEBIT);
                        break;
                }
                totalEntity.setAmount(Math.abs(totalGrossAmount));
                totalEntity.setSequence(1);
                totalEntity.setCreatedBy(UserContext.getLoggedInUser());
                totalEntity.setIsLocked(0);
                totalEntity.setIsDeleted(0);
                postingDetailEntityList.add(totalEntity);
                cashPaymentReceiptDetailEntityList.stream().forEach(cash -> {
                    if (cash.getAccount().getCostCenterApplicable() == 1 && (cash.getBreakupList() == null || cash.getBreakupList().size() == 0)) {
                        throw new RuntimeException("Account : ".concat(cash.getAccount().getAccountName())
                                .concat(" does not have Breakup details"));
                    }
                });
                cashPaymentReceiptDetailEntityList.stream().forEach(cash -> {
                    if (cash.getBreakupList() != null) {
                        if (cash.getBreakupList().size() > 0) {
                            final double detailAmount = cash.getGrossAmount() == null ? 0.00 : cash.getGrossAmount().doubleValue();
                            final double breakupAmount = cash.getBreakupList().stream().mapToDouble(a -> a.getGrossAmount() == null ? 0.00 : a.getGrossAmount()).sum();
                            if (Math.abs(breakupAmount - detailAmount) > 0) {
                                throw new RuntimeException("Account : ".concat(cash.getAccount().getAccountName()).concat("Detail Amount ")
                                        .concat(String.valueOf(detailAmount))
                                        .concat(",Cost Breakup provided ")
                                        .concat(String.valueOf(breakupAmount))
                                        .concat(" not matching"));
                            }
                        }
                    }
                });
                cashPaymentReceiptDetailEntityList.stream().forEachOrdered(e -> {
                    if (e.getAccount().getCostCenterApplicable() == 1) {
                        setCashPaymentReceiptCostBreakup(postingDetailEntityList, e, invoiceType);
                    } else {
                        final double grossAmount = e.getGrossAmount() == null ? 0 : e.getGrossAmount().doubleValue();
                        PostingDetailEntity entity = new PostingDetailEntity();
                        entity.setId(null);
                        entity.setPostingId(null);
                        entity.setAccountId(e.getAccountId());
                        switch (PostingTypes.valueOf(invoiceType.toUpperCase())) {
                            case CP:
                            case PCP:
                                entity.setInOutFlag(grossAmount < 0 ? AccountConstants.OUT : AccountConstants.IN);
                                entity.setDrCr(grossAmount < 0 ? AccountConstants.CREDIT : AccountConstants.DEBIT);
                                break;
                            case CR:
                            case PCR:
                                entity.setInOutFlag(grossAmount < 0 ? AccountConstants.IN : AccountConstants.OUT);
                                entity.setDrCr(grossAmount < 0 ? AccountConstants.DEBIT : AccountConstants.CREDIT);
                                break;
                        }
                        entity.setAmount(Math.abs(grossAmount));
                        entity.setSequence(2);
                        entity.setCreatedBy(UserContext.getLoggedInUser());
                        entity.setIsLocked(0);
                        entity.setIsDeleted(0);
                        postingDetailEntityList.add(entity);
                    }
                });
                break;
            case BP:
            case BR:
                List<BankPaymentReceiptDetailEntity> bankPaymentReceiptDetailEntityList = this.findAllBankDetailByMainId(invoiceId);
                final Double totalBankGrossAmount = bankPaymentReceiptDetailEntityList.stream().mapToDouble(e -> Double.valueOf(e.getGrossAmount().toString())).sum();
                totalEntity.setId(null);
                totalEntity.setPostingId(null);
                totalEntity.setAccountId(headerAccountId);
                switch (PostingTypes.valueOf(invoiceType.toUpperCase())) {
                    case BP:
                        totalEntity.setInOutFlag(totalBankGrossAmount < 0 ? AccountConstants.IN : AccountConstants.OUT);
                        totalEntity.setDrCr(totalBankGrossAmount < 0 ? AccountConstants.DEBIT : AccountConstants.CREDIT);
                        break;
                    case BR:
                        totalEntity.setInOutFlag(totalBankGrossAmount < 0 ? AccountConstants.OUT : AccountConstants.IN);
                        totalEntity.setDrCr(totalBankGrossAmount < 0 ? AccountConstants.CREDIT : AccountConstants.DEBIT);
                        break;
                }
                totalEntity.setAmount(Math.abs(totalBankGrossAmount));
                totalEntity.setSequence(1);
                totalEntity.setCreatedBy(UserContext.getLoggedInUser());
                totalEntity.setIsLocked(0);
                totalEntity.setIsDeleted(0);
                postingDetailEntityList.add(totalEntity);
                bankPaymentReceiptDetailEntityList.stream().forEach(bank -> {
                    if (bank.getAccount().getCostCenterApplicable() == 1 && (bank.getBreakupList() == null || bank.getBreakupList().size() == 0)) {
                        throw new RuntimeException("Account : ".concat(bank.getAccount().getAccountName())
                                .concat(" does not have Breakup details"));
                    }
                });
                bankPaymentReceiptDetailEntityList.stream().forEach(bank -> {
                    if (bank.getBreakupList() != null) {
                        if (bank.getBreakupList().size() > 0) {
                            final double detailAmount = bank.getGrossAmount() == null ? 0.00 : bank.getGrossAmount().doubleValue();
                            final double breakupAmount = bank.getBreakupList().stream().mapToDouble(a -> a.getGrossAmount() == null ? 0.00 : a.getGrossAmount()).sum();
                            if (Math.abs(breakupAmount - detailAmount) > 0) {
                                throw new RuntimeException("Account : ".concat(bank.getAccount().getAccountName()).concat("Detail Amount ")
                                        .concat(String.valueOf(detailAmount))
                                        .concat(",Cost Breakup provided ")
                                        .concat(String.valueOf(breakupAmount))
                                        .concat(" not matching"));
                            }
                        }
                    }
                });
                bankPaymentReceiptDetailEntityList.stream().forEachOrdered(e -> {
                    if (e.getAccount().getCostCenterApplicable() == 1) {
                        setBankPaymentReceiptCostBreakup(postingDetailEntityList, e, invoiceType);
                    } else {
                        final double grossAmount = e.getGrossAmount() == null ? 0 : e.getGrossAmount().doubleValue();
                        PostingDetailEntity entity = new PostingDetailEntity();
                        entity.setId(null);
                        entity.setPostingId(null);
                        entity.setAccountId(e.getAccountId());
                        switch (PostingTypes.valueOf(invoiceType.toUpperCase())) {
                            case BP:
                                entity.setInOutFlag(grossAmount < 0 ? AccountConstants.OUT : AccountConstants.IN);
                                entity.setDrCr(grossAmount < 0 ? AccountConstants.CREDIT : AccountConstants.DEBIT);
                                break;
                            case BR:
                                entity.setInOutFlag(grossAmount < 0 ? AccountConstants.IN : AccountConstants.OUT);
                                entity.setDrCr(grossAmount < 0 ? AccountConstants.DEBIT : AccountConstants.CREDIT);
                                break;
                        }
                        entity.setAmount(Math.abs(grossAmount));
                        entity.setSequence(2);
                        entity.setCreatedBy(UserContext.getLoggedInUser());
                        entity.setIsLocked(0);
                        entity.setIsDeleted(0);
                        postingDetailEntityList.add(entity);
                    }
                });
                break;
            case NDR:
            case NCR:
                List<DebitCreditNoteDetailEntity> debitCreditNoteDetailEntityList = this.findAllDebitCreditNoteDetailByMainId(invoiceId);
                final Double totalDebitCreditNoteGrossAmount = debitCreditNoteDetailEntityList.stream().mapToDouble(e -> Double.valueOf(e.getGrossAmount().toString())).sum();
                totalEntity.setId(null);
                totalEntity.setPostingId(null);
                totalEntity.setAccountId(headerAccountId);
                switch (PostingTypes.valueOf(invoiceType.toUpperCase())) {
                    case NCR:
                        totalEntity.setInOutFlag(totalDebitCreditNoteGrossAmount < 0 ? AccountConstants.IN : AccountConstants.OUT);
                        totalEntity.setDrCr(totalDebitCreditNoteGrossAmount < 0 ? AccountConstants.DEBIT : AccountConstants.CREDIT);
                        break;
                    case NDR:
                        totalEntity.setInOutFlag(totalDebitCreditNoteGrossAmount < 0 ? AccountConstants.OUT : AccountConstants.IN);
                        totalEntity.setDrCr(totalDebitCreditNoteGrossAmount < 0 ? AccountConstants.CREDIT : AccountConstants.DEBIT);
                        break;
                }
                totalEntity.setAmount(Math.abs(totalDebitCreditNoteGrossAmount));
                totalEntity.setSequence(1);
                totalEntity.setCreatedBy(UserContext.getLoggedInUser());
                totalEntity.setIsLocked(0);
                totalEntity.setIsDeleted(0);
                postingDetailEntityList.add(totalEntity);
                debitCreditNoteDetailEntityList.stream().forEach(d -> {
                    if (d.getAccount().getCostCenterApplicable() == 1 && (d.getBreakupList() == null || d.getBreakupList().size() == 0)) {
                        throw new RuntimeException("Account : ".concat(d.getAccount().getAccountName())
                                .concat(" does not have Breakup details"));
                    }
                });
                debitCreditNoteDetailEntityList.stream().forEach(drcr -> {
                    if (drcr.getBreakupList() != null) {
                        if (drcr.getBreakupList().size() > 0) {
                            final double detailAmount = drcr.getGrossAmount() == null ? 0.00 : drcr.getGrossAmount().doubleValue();
                            final double breakupAmount = drcr.getBreakupList().stream().mapToDouble(a -> a.getGrossAmount() == null ? 0.00 : a.getGrossAmount()).sum();
                            if (Math.abs(breakupAmount - detailAmount) > 0) {
                                throw new RuntimeException("Account : ".concat(drcr.getAccount().getAccountName()).concat("Detail Amount ")
                                        .concat(String.valueOf(detailAmount))
                                        .concat(",Cost Breakup provided ")
                                        .concat(String.valueOf(breakupAmount))
                                        .concat(" not matching"));
                            }
                        }
                    }
                });
                debitCreditNoteDetailEntityList.stream().forEachOrdered(e -> {
                    if (e.getAccount().getCostCenterApplicable() == 1) {
                        setDebitCreditNoteCostBreakup(postingDetailEntityList, e, invoiceType);
                    } else {
                        final double debitCreditNoteGrossAmount = e.getGrossAmount() == null ? 0 : e.getGrossAmount().doubleValue();
                        PostingDetailEntity entity = new PostingDetailEntity();
                        entity.setId(null);
                        entity.setPostingId(null);
                        entity.setAccountId(e.getAccountId());
                        switch (PostingTypes.valueOf(invoiceType.toUpperCase())) {
                            case NDR:
                                entity.setInOutFlag(debitCreditNoteGrossAmount > 0 ? AccountConstants.OUT : AccountConstants.IN);
                                entity.setDrCr(debitCreditNoteGrossAmount > 0 ? AccountConstants.CREDIT : AccountConstants.DEBIT);
                                break;
                            case NCR:
                                entity.setInOutFlag(debitCreditNoteGrossAmount > 0 ? AccountConstants.IN : AccountConstants.OUT);
                                entity.setDrCr(debitCreditNoteGrossAmount > 0 ? AccountConstants.DEBIT : AccountConstants.CREDIT);
                                break;
                        }
                        entity.setAmount(Math.abs(debitCreditNoteGrossAmount));
                        entity.setSequence(2);
                        entity.setCreatedBy(UserContext.getLoggedInUser());
                        entity.setIsLocked(0);
                        entity.setIsDeleted(0);
                        postingDetailEntityList.add(entity);
                    }
                });
                break;
        }
        return postingDetailEntityList;
    }

    private void setJVCreditCostBreakupDetails(List<PostingDetailEntity> postingDetailEntityList, JVDetailEntity cr) {
        cr.getBreakupList().stream().forEach(br -> {
            final double creditAmount = br.getCreditAmount() == null ? 0 : br.getCreditAmount().doubleValue();
            PostingDetailEntity entity = new PostingDetailEntity();
            entity.setId(null);
            entity.setPostingId(null);
            entity.setAccountId(cr.getAccountId());
            entity.setSequence(2);
            entity.setCreatedBy(UserContext.getLoggedInUser());
            entity.setIsLocked(0);
            entity.setIsDeleted(0);
            entity.setInOutFlag(creditAmount < 0 ? AccountConstants.IN : AccountConstants.OUT);
            entity.setDrCr(creditAmount < 0 ? AccountConstants.DEBIT : AccountConstants.CREDIT);
            entity.setAmount(Math.abs(creditAmount));
            entity.setCostCategoryId(br.getCostCategoryId());
            entity.setCostCenterId(br.getCostCenterId());
        });
    }

    private void setJVDebitCostBreakupDetails(List<PostingDetailEntity> postingDetailEntityList, JVDetailEntity dr) {
        dr.getBreakupList().stream().forEach(br -> {
            PostingDetailEntity entity = new PostingDetailEntity();
            entity.setId(null);
            entity.setPostingId(null);
            entity.setAccountId(dr.getAccountId());
            entity.setSequence(2);
            entity.setCreatedBy(UserContext.getLoggedInUser());
            entity.setIsLocked(0);
            entity.setIsDeleted(0);
            final double debitAmount = br.getDebitAmount() == null ? 0 : br.getDebitAmount().doubleValue();
            entity.setInOutFlag(debitAmount < 0 ? AccountConstants.OUT : AccountConstants.IN);
            entity.setDrCr(debitAmount < 0 ? AccountConstants.CREDIT : AccountConstants.DEBIT);
            entity.setAmount(Math.abs(debitAmount));
            entity.setCostCategoryId(br.getCostCategoryId());
            entity.setCostCenterId(br.getCostCenterId());
            postingDetailEntityList.add(entity);
        });
    }

    private void setDebitCreditNoteCostBreakup(List<PostingDetailEntity> postingDetailEntityList, DebitCreditNoteDetailEntity debitCreditNote, String invoiceType) {
        debitCreditNote.getBreakupList().stream().forEach(br -> {
            final double debitCreditNoteGrossAmount = br.getGrossAmount() == null ? 0 : br.getGrossAmount().doubleValue();
            PostingDetailEntity entity = new PostingDetailEntity();
            entity.setId(null);
            entity.setPostingId(null);
            entity.setAccountId(debitCreditNote.getAccountId());
            entity.setCostCategoryId(br.getCostCategoryId());
            entity.setCostCenterId(br.getCostCenterId());
            switch (PostingTypes.valueOf(invoiceType.toUpperCase())) {
                case NDR:
                    entity.setInOutFlag(debitCreditNoteGrossAmount > 0 ? AccountConstants.OUT : AccountConstants.IN);
                    entity.setDrCr(debitCreditNoteGrossAmount > 0 ? AccountConstants.CREDIT : AccountConstants.DEBIT);
                    break;
                case NCR:
                    entity.setInOutFlag(debitCreditNoteGrossAmount > 0 ? AccountConstants.IN : AccountConstants.OUT);
                    entity.setDrCr(debitCreditNoteGrossAmount > 0 ? AccountConstants.DEBIT : AccountConstants.CREDIT);
                    break;
            }
            entity.setAmount(Math.abs(debitCreditNoteGrossAmount));
            entity.setSequence(2);
            entity.setCreatedBy(UserContext.getLoggedInUser());
            entity.setIsLocked(0);
            entity.setIsDeleted(0);
            postingDetailEntityList.add(entity);
        });
    }

    private void setCashPaymentReceiptCostBreakup(List<PostingDetailEntity> postingDetailEntityList, CashPaymentReceiptDetailEntity cashDetail, String invoiceType) {
        cashDetail.getBreakupList().stream().forEach(br -> {
            final double brGrossAmount = br.getGrossAmount() == null ? 0 : br.getGrossAmount().doubleValue();
            PostingDetailEntity entity = new PostingDetailEntity();
            entity.setId(null);
            entity.setPostingId(null);
            entity.setAccountId(cashDetail.getAccountId());
            entity.setCostCategoryId(br.getCostCategoryId());
            entity.setCostCenterId(br.getCostCenterId());
            switch (PostingTypes.valueOf(invoiceType.toUpperCase())) {
                case CP:
                case PCP:
                    entity.setInOutFlag(brGrossAmount < 0 ? AccountConstants.OUT : AccountConstants.IN);
                    entity.setDrCr(brGrossAmount < 0 ? AccountConstants.CREDIT : AccountConstants.DEBIT);
                    break;
                case CR:
                case PCR:
                    entity.setInOutFlag(brGrossAmount < 0 ? AccountConstants.IN : AccountConstants.OUT);
                    entity.setDrCr(brGrossAmount < 0 ? AccountConstants.DEBIT : AccountConstants.CREDIT);
                    break;
            }
            entity.setAmount(Math.abs(brGrossAmount));
            entity.setSequence(2);
            entity.setCreatedBy(UserContext.getLoggedInUser());
            entity.setIsLocked(0);
            entity.setIsDeleted(0);
            postingDetailEntityList.add(entity);
        });
    }

    private void setBankPaymentReceiptCostBreakup(List<PostingDetailEntity> postingDetailEntityList, BankPaymentReceiptDetailEntity bankDetail, String invoiceType) {
        bankDetail.getBreakupList().stream().forEach(br -> {
            final double bankGrossAmount = br.getGrossAmount() == null ? 0 : br.getGrossAmount().doubleValue();
            PostingDetailEntity entity = new PostingDetailEntity();
            entity.setId(null);
            entity.setPostingId(null);
            entity.setAccountId(bankDetail.getAccountId());
            entity.setCostCategoryId(br.getCostCategoryId());
            entity.setCostCenterId(br.getCostCenterId());
            switch (PostingTypes.valueOf(invoiceType.toUpperCase())) {
                case BP:
                    entity.setInOutFlag(bankGrossAmount < 0 ? AccountConstants.OUT : AccountConstants.IN);
                    entity.setDrCr(bankGrossAmount < 0 ? AccountConstants.CREDIT : AccountConstants.DEBIT);
                    break;
                case BR:
                    entity.setInOutFlag(bankGrossAmount < 0 ? AccountConstants.IN : AccountConstants.OUT);
                    entity.setDrCr(bankGrossAmount < 0 ? AccountConstants.DEBIT : AccountConstants.CREDIT);
                    break;
            }
            entity.setAmount(Math.abs(bankGrossAmount));
            entity.setSequence(2);
            entity.setCreatedBy(UserContext.getLoggedInUser());
            entity.setIsLocked(0);
            entity.setIsDeleted(0);
            postingDetailEntityList.add(entity);
        });
    }
    private void changeAccountingStatus(String invoiceType, Integer invoiceId, String status) throws Exception {
        switch (PostingTypes.valueOf(invoiceType.toUpperCase())) {
            case JV:
                JVMainEntity jvMainEntity = jvMainRepository.findById(invoiceId).get();
                jvMainEntity.setAccountingStatus(status);
                jvMainRepository.save(jvMainEntity);
                break;
            case CP:
            case CR:
            case PCP:
            case PCR:
                CashPaymentReceiptMainEntity cashPaymentReceiptMainEntity = cashPaymentReceiptMainRepository.findById(invoiceId).get();
                cashPaymentReceiptMainEntity.setAccountingStatus(status);
                cashPaymentReceiptMainRepository.save(cashPaymentReceiptMainEntity);
                break;
            case BP:
            case BR:
                BankPaymentReceiptMainEntity bankPaymentReceiptMainEntity = bankPaymentReceiptMainRepository.findById(invoiceId).get();
                bankPaymentReceiptMainEntity.setAccountingStatus(status);
                this.bankPaymentReceiptMainRepository.save(bankPaymentReceiptMainEntity);
                break;
            case NDR:
            case NCR:
                DebitCreditNoteMainEntity debitCreditNoteMainEntity = this.debitCreditNoteMainRepository.findById(invoiceId).get();
                debitCreditNoteMainEntity.setAccountingStatus(status);
                this.debitCreditNoteMainRepository.save(debitCreditNoteMainEntity);
                break;
        }
    }

    public List<PostingDetailEntity> findAllPostedVoucherListByAccount(Integer accountId) {
        return this.postingDetailRepository.findAllByAccountId(accountId);
    }
}
