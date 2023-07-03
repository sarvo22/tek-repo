package com.tekfilo.inventory.upload;

import com.tekfilo.inventory.invoice.memopurchaseinvoice.entity.MemoPurchaseInvoiceMainEntity;
import com.tekfilo.inventory.invoice.memopurchaseinvoice.repository.MemoPurchaseInvoiceMainRepository;
import com.tekfilo.inventory.invoice.memopurchasereturninvoice.entity.MemoPurchaseReturnInvoiceMainEntity;
import com.tekfilo.inventory.invoice.memopurchasereturninvoice.repository.MemoPurchaseReturnInvoiceMainRepository;
import com.tekfilo.inventory.invoice.memosalesinvoice.entity.MemoSalesInvoiceMainEntity;
import com.tekfilo.inventory.invoice.memosalesinvoice.repository.MemoSalesInvoiceMainRepository;
import com.tekfilo.inventory.invoice.memosalesreturninvoice.entity.MemoSalesReturnInvoiceMainEntity;
import com.tekfilo.inventory.invoice.memosalesreturninvoice.repository.MemoSalesReturnInvoiceMainRepository;
import com.tekfilo.inventory.invoice.purchaseinvoice.entity.PurchaseInvoiceMainEntity;
import com.tekfilo.inventory.invoice.purchaseinvoice.repository.PurchaseInvoiceMainRepository;
import com.tekfilo.inventory.invoice.purchasereturninvoice.entity.PurchaseReturnInvoiceMainEntity;
import com.tekfilo.inventory.invoice.purchasereturninvoice.repository.PurchaseReturnInvoiceMainRepository;
import com.tekfilo.inventory.invoice.salesinvoice.entity.SalesInvoiceMainEntity;
import com.tekfilo.inventory.invoice.salesinvoice.repository.SalesInvoiceMainRepository;
import com.tekfilo.inventory.invoice.salesreturninvoice.entity.SalesReturnInvoiceMainEntity;
import com.tekfilo.inventory.invoice.salesreturninvoice.repository.SalesReturnInvoiceMainRepository;
import com.tekfilo.inventory.item.ItemEntity;
import com.tekfilo.inventory.item.ItemRepository;
import com.tekfilo.inventory.item.invoice.purchase.entity.ItemPurchaseInvoiceMainEntity;
import com.tekfilo.inventory.item.invoice.purchase.repository.ItemPurchaseInvoiceMainRepository;
import com.tekfilo.inventory.item.invoice.purchasereturn.entity.ItemPurchaseReturnInvoiceMainEntity;
import com.tekfilo.inventory.item.invoice.purchasereturn.repository.ItemPurchaseReturnInvoiceMainRepository;
import com.tekfilo.inventory.item.invoice.salesinvoice.entity.ItemSalesInvoiceMainEntity;
import com.tekfilo.inventory.item.invoice.salesinvoice.repository.ItemSalesInvoiceMainRepository;
import com.tekfilo.inventory.item.invoice.salesreturn.entity.ItemSalesReturnInvoiceMainEntity;
import com.tekfilo.inventory.item.invoice.salesreturn.repository.ItemSalesReturnInvoiceMainRepository;
import com.tekfilo.inventory.product.ProductEntity;
import com.tekfilo.inventory.product.ProductRepository;
import com.tekfilo.inventory.settlement.paymentpaid.entity.PaymentPaidMainEntity;
import com.tekfilo.inventory.settlement.paymentpaid.repository.PaymentPaidMainRepository;
import com.tekfilo.inventory.settlement.paymentreceived.entity.PaymentReceivedMainEntity;
import com.tekfilo.inventory.settlement.paymentreceived.repository.PaymentReceivedMainRepository;
import com.tekfilo.inventory.util.UploadTypes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
public class UploadService {

    private static final CharSequence DELIMIER = ",";
    @Autowired
    PurchaseInvoiceMainRepository purchaseInvoiceMainRepository;

    @Autowired
    MemoPurchaseInvoiceMainRepository memoPurchaseInvoiceMainRepository;

    @Autowired
    MemoSalesInvoiceMainRepository memoSalesInvoiceMainRepository;

    @Autowired
    MemoSalesReturnInvoiceMainRepository memoSalesReturnInvoiceMainRepository;

    @Autowired
    MemoPurchaseReturnInvoiceMainRepository memoPurchaseReturnInvoiceMainRepository;
    @Autowired
    PurchaseReturnInvoiceMainRepository purchaseReturnInvoiceMainRepository;

    @Autowired
    SalesInvoiceMainRepository salesInvoiceMainRepository;

    @Autowired
    SalesReturnInvoiceMainRepository salesReturnInvoiceMainRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    PaymentPaidMainRepository paymentPaidMainRepository;

    @Autowired
    PaymentReceivedMainRepository paymentReceivedMainRepository;


    @Autowired
    ItemPurchaseInvoiceMainRepository itemPurchaseInvoiceMainRepository;

    @Autowired
    ItemPurchaseReturnInvoiceMainRepository itemPurchaseReturnInvoiceMainRepository;

    @Autowired
    ItemSalesInvoiceMainRepository itemSalesInvoiceMainRepository;

    @Autowired
    ItemSalesReturnInvoiceMainRepository itemSalesReturnInvoiceMainRepository;

    @Autowired
    ItemRepository itemRepository;


    public void updateUploadedDocumentUrls(DocumentDto documentDto) throws Exception {
        String documentUrl = "";
        switch (UploadTypes.valueOf(documentDto.getUploadModule().toUpperCase())) {
            case RM_PURCHASE:
                PurchaseInvoiceMainEntity purchaseInvoiceMainEntity = this.purchaseInvoiceMainRepository.findById(documentDto.getInvoiceId()).orElse(new PurchaseInvoiceMainEntity());
                documentUrl = purchaseInvoiceMainEntity.getDocumentUrl();
                this.purchaseInvoiceMainRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case RM_PURCHASE_RETURN:
                PurchaseReturnInvoiceMainEntity purchaseReturnInvoiceMainEntity = this.purchaseReturnInvoiceMainRepository.findById(documentDto.getInvoiceId()).orElse(new PurchaseReturnInvoiceMainEntity());
                documentUrl = purchaseReturnInvoiceMainEntity.getDocumentUrl();
                this.purchaseReturnInvoiceMainRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case RM_SALES:
                SalesInvoiceMainEntity salesInvoiceMainEntity = this.salesInvoiceMainRepository.findById(documentDto.getInvoiceId()).orElse(new SalesInvoiceMainEntity());
                documentUrl = salesInvoiceMainEntity.getDocumentUrl();
                this.salesInvoiceMainRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case RM_SALES_RETURN:
                SalesReturnInvoiceMainEntity salesReturnInvoiceMainEntity = this.salesReturnInvoiceMainRepository.findById(documentDto.getInvoiceId()).orElse(new SalesReturnInvoiceMainEntity());
                documentUrl = salesReturnInvoiceMainEntity.getDocumentUrl();
                this.salesReturnInvoiceMainRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case RM_MEMO_PURCHASE:
                MemoPurchaseInvoiceMainEntity memoPurchaseInvoiceMainEntity = this.memoPurchaseInvoiceMainRepository.findById(documentDto.getInvoiceId()).orElse(new MemoPurchaseInvoiceMainEntity());
                documentUrl = memoPurchaseInvoiceMainEntity.getDocumentUrl();
                this.memoPurchaseInvoiceMainRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case RM_MEMO_SALES:
                MemoSalesInvoiceMainEntity memoSalesInvoiceMainEntity = this.memoSalesInvoiceMainRepository.findById(documentDto.getInvoiceId()).orElse(new MemoSalesInvoiceMainEntity());
                documentUrl = memoSalesInvoiceMainEntity.getDocumentUrl();
                this.memoSalesInvoiceMainRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case RM_MEMO_SALES_RETURN:
                MemoSalesReturnInvoiceMainEntity memoSalesReturnInvoiceMainEntity = this.memoSalesReturnInvoiceMainRepository.findById(documentDto.getInvoiceId()).orElse(new MemoSalesReturnInvoiceMainEntity());
                documentUrl = memoSalesReturnInvoiceMainEntity.getDocumentUrl();
                this.memoSalesReturnInvoiceMainRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case RM_MEMO_PURCHASE_RETURN:
                MemoPurchaseReturnInvoiceMainEntity memoPurchaseReturnInvoiceMainEntity = this.memoPurchaseReturnInvoiceMainRepository.findById(documentDto.getInvoiceId()).orElse(new MemoPurchaseReturnInvoiceMainEntity());
                documentUrl = memoPurchaseReturnInvoiceMainEntity.getDocumentUrl();
                this.memoPurchaseReturnInvoiceMainRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case ITEM_PURCHASE:
                ItemPurchaseInvoiceMainEntity itemPurchaseInvoiceMainEntity = this.itemPurchaseInvoiceMainRepository.findById(documentDto.getInvoiceId()).orElse(new ItemPurchaseInvoiceMainEntity());
                documentUrl = itemPurchaseInvoiceMainEntity.getDocumentUrl();
                this.itemPurchaseInvoiceMainRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case ITEM_PURCHASE_RETURN:
                ItemPurchaseReturnInvoiceMainEntity itemPurchaseReturnInvoiceMainEntity = this.itemPurchaseReturnInvoiceMainRepository.findById(documentDto.getInvoiceId()).orElse(new ItemPurchaseReturnInvoiceMainEntity());
                documentUrl = itemPurchaseReturnInvoiceMainEntity.getDocumentUrl();
                this.itemPurchaseReturnInvoiceMainRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case ITEM_SALES:
                ItemSalesInvoiceMainEntity itemSaleseInvoiceMainEntity = this.itemSalesInvoiceMainRepository.findById(documentDto.getInvoiceId()).orElse(new ItemSalesInvoiceMainEntity());
                documentUrl = itemSaleseInvoiceMainEntity.getDocumentUrl();
                this.itemSalesInvoiceMainRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case ITEM_SALES_RETURN:
                ItemSalesReturnInvoiceMainEntity itemSalesReturnInvoiceMainEntity = this.itemSalesReturnInvoiceMainRepository.findById(documentDto.getInvoiceId()).orElse(new ItemSalesReturnInvoiceMainEntity());
                documentUrl = itemSalesReturnInvoiceMainEntity.getDocumentUrl();
                this.itemSalesReturnInvoiceMainRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case DIAMOND_IMAGE:
            case GEMSTONE_IMAGE:
            case METAL_IMAGE:
                if (Optional.ofNullable(documentDto.getDocumentUrls()).isPresent()) {
                    if (documentDto.getDocumentUrls().length() > 0) {
                        String[] picturePaths = documentDto.getDocumentUrls().split(",");
                        if (picturePaths != null) {
                            int length = picturePaths.length;
                            if (length > 0) {
                                if (length == 1)
                                    this.productRepository.updatepicture1Path(documentDto.getInvoiceId(), picturePaths[0]);
                                if (length == 2)
                                    this.productRepository.updatepicture2Path(documentDto.getInvoiceId(), picturePaths[1]);
                                if (length == 3)
                                    this.productRepository.updatepicture3Path(documentDto.getInvoiceId(), picturePaths[2]);
                                if (length == 4)
                                    this.productRepository.updatepicture4Path(documentDto.getInvoiceId(), picturePaths[3]);
                            }
                        }
                    }
                }
                break;
            case DIAMOND:
            case GEMSTONE:
            case METAL:
                ProductEntity productEntity = this.productRepository.findById(documentDto.getInvoiceId()).orElse(new ProductEntity());
                documentUrl = productEntity.getDocumentUrls();
                this.productRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case ITEM_IMAGE:
                ItemEntity itemEntity = this.itemRepository.findById(documentDto.getInvoiceId()).orElse(new ItemEntity());
                documentUrl = itemEntity.getImageUrl();
                this.itemRepository.updatePicturePath(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case PAYMENT_PAID:
                PaymentPaidMainEntity paymentPaidMainEntity = this.paymentPaidMainRepository.findById(documentDto.getInvoiceId()).orElse(new PaymentPaidMainEntity());
                documentUrl = paymentPaidMainEntity.getDocumentUrl();
                this.paymentPaidMainRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case PAYMENT_RECEIVED:
                PaymentReceivedMainEntity paymentReceivedMainEntity = this.paymentReceivedMainRepository.findById(documentDto.getInvoiceId()).orElse(new PaymentReceivedMainEntity());
                documentUrl = paymentReceivedMainEntity.getDocumentUrl();
                this.paymentReceivedMainRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;


            default:
                break;
        }
    }

    private String getDocumentUrl(String dbDocumentUrl, String newDocumentUrls) {
        if (dbDocumentUrl == null) {
            return newDocumentUrls;
        } else if (dbDocumentUrl == "null") {
            return newDocumentUrls;
        } else {
            String documentUrl =  String.join(DELIMIER, dbDocumentUrl, newDocumentUrls);
            return documentUrl.startsWith(",") ? documentUrl.replaceFirst(",","") : documentUrl;
        }
    }
}
