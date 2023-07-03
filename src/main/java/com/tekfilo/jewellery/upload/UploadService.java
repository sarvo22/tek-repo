package com.tekfilo.jewellery.upload;

import com.tekfilo.jewellery.design.entity.DesignEntity;
import com.tekfilo.jewellery.design.repository.DesignRepository;
import com.tekfilo.jewellery.goodsinward.entity.GoodsInwardMainEntity;
import com.tekfilo.jewellery.goodsinward.repository.GoodsInwardMainRepository;
import com.tekfilo.jewellery.goodsoutward.entity.GoodsOutwardMainEntity;
import com.tekfilo.jewellery.goodsoutward.repository.GoodsOutwardMainRepository;
import com.tekfilo.jewellery.jewinvoice.memopurchaseinvoice.entity.MemoPurchaseInvoiceMainEntity;
import com.tekfilo.jewellery.jewinvoice.memopurchaseinvoice.repository.MemoPurchaseInvoiceMainRepository;
import com.tekfilo.jewellery.jewinvoice.memopurchasereturninvoice.entity.MemoPurchaseReturnInvoiceMainEntity;
import com.tekfilo.jewellery.jewinvoice.memopurchasereturninvoice.repository.MemoPurchaseReturnInvoiceMainRepository;
import com.tekfilo.jewellery.jewinvoice.memosalesinvoice.entity.MemoSalesInvoiceMainEntity;
import com.tekfilo.jewellery.jewinvoice.memosalesinvoice.repository.MemoSalesInvoiceMainRepository;
import com.tekfilo.jewellery.jewinvoice.memosalesreturninvoice.entity.MemoSalesReturnInvoiceMainEntity;
import com.tekfilo.jewellery.jewinvoice.memosalesreturninvoice.repository.MemoSalesReturnInvoiceMainRepository;
import com.tekfilo.jewellery.jewinvoice.purchaseinvoice.entity.PurchaseInvoiceMainEntity;
import com.tekfilo.jewellery.jewinvoice.purchaseinvoice.repository.PurchaseInvoiceMainRepository;
import com.tekfilo.jewellery.jewinvoice.purchasereturninvoice.entity.PurchaseReturnInvoiceMainEntity;
import com.tekfilo.jewellery.jewinvoice.purchasereturninvoice.repository.PurchaseReturnInvoiceMainRepository;
import com.tekfilo.jewellery.jewinvoice.salesinvoice.entity.SalesInvoiceMainEntity;
import com.tekfilo.jewellery.jewinvoice.salesinvoice.repository.SalesInvoiceMainRepository;
import com.tekfilo.jewellery.jewinvoice.salesreturninvoice.entity.SalesReturnInvoiceMainEntity;
import com.tekfilo.jewellery.jewinvoice.salesreturninvoice.repository.SalesReturnInvoiceMainRepository;
import com.tekfilo.jewellery.product.entity.ProductEntity;
import com.tekfilo.jewellery.product.repository.ProductRepository;
import com.tekfilo.jewellery.sku.entity.SkuEntity;
import com.tekfilo.jewellery.sku.repository.SkuRepository;
import com.tekfilo.jewellery.util.UploadTypes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class UploadService {

    private static final CharSequence DELIMIER = ",";

    @Autowired
    PurchaseInvoiceMainRepository purchaseInvoiceMainRepository;

    @Autowired
    PurchaseReturnInvoiceMainRepository purchaseReturnInvoiceMainRepository;

    @Autowired
    MemoPurchaseInvoiceMainRepository memoPurchaseInvoiceMainRepository;

    @Autowired
    MemoPurchaseReturnInvoiceMainRepository memoPurchaseReturnInvoiceMainRepository;

    @Autowired
    SalesInvoiceMainRepository salesInvoiceMainRepository;

    @Autowired
    SalesReturnInvoiceMainRepository salesReturnInvoiceMainRepository;

    @Autowired
    MemoSalesInvoiceMainRepository memoSalesInvoiceMainRepository;

    @Autowired
    MemoSalesReturnInvoiceMainRepository memoSalesReturnInvoiceMainRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    GoodsInwardMainRepository goodsInwardMainRepository;

    @Autowired
    GoodsOutwardMainRepository goodsOutwardMainRepository;

    @Autowired
    DesignRepository designRepository;


    @Autowired
    SkuRepository skuRepository;

    public void updateUploadedDocumentUrls(DocumentDto documentDto) throws Exception {
        String documentUrl = "";
        switch (UploadTypes.valueOf(documentDto.getUploadModule().toUpperCase())) {
            case FG_PURCHASE:
            case JEW_PURCHASE:
                PurchaseInvoiceMainEntity purchaseInvoiceMainEntity = this.purchaseInvoiceMainRepository.findById(documentDto.getInvoiceId()).orElse(new PurchaseInvoiceMainEntity());
                documentUrl = purchaseInvoiceMainEntity.getDocumentUrl();
                this.purchaseInvoiceMainRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case FG_PURCHASE_RETURN:
            case JEW_PURCHASE_RETURN:
                PurchaseReturnInvoiceMainEntity purchaseReturnInvoiceMainEntity = this.purchaseReturnInvoiceMainRepository.findById(documentDto.getInvoiceId()).orElse(new PurchaseReturnInvoiceMainEntity());
                documentUrl = purchaseReturnInvoiceMainEntity.getDocumentUrl();
                this.purchaseReturnInvoiceMainRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case FG_SALES:
            case JEW_SALES:
                SalesInvoiceMainEntity salesInvoiceMainEntity = this.salesInvoiceMainRepository.findById(documentDto.getInvoiceId()).orElse(new SalesInvoiceMainEntity());
                documentUrl = salesInvoiceMainEntity.getDocumentUrl();
                this.salesInvoiceMainRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case FG_SALES_RETURN:
            case JEW_SALES_RETURN:
                SalesReturnInvoiceMainEntity salesReturnInvoiceMainEntity = this.salesReturnInvoiceMainRepository.findById(documentDto.getInvoiceId()).orElse(new SalesReturnInvoiceMainEntity());
                documentUrl = salesReturnInvoiceMainEntity.getDocumentUrl();
                this.salesReturnInvoiceMainRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case FG_MEMO_PURCHASE:
            case JEW_MEMO_PURCHASE:
                MemoPurchaseInvoiceMainEntity memoPurchaseInvoiceMainEntity = this.memoPurchaseInvoiceMainRepository.findById(documentDto.getInvoiceId()).orElse(new MemoPurchaseInvoiceMainEntity());
                documentUrl = memoPurchaseInvoiceMainEntity.getDocumentUrl();
                this.memoPurchaseInvoiceMainRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case FG_MEMO_SALES:
            case JEW_MEMO_SALES:
                MemoSalesInvoiceMainEntity memoSalesInvoiceMainEntity = this.memoSalesInvoiceMainRepository.findById(documentDto.getInvoiceId()).orElse(new MemoSalesInvoiceMainEntity());
                documentUrl = memoSalesInvoiceMainEntity.getDocumentUrl();
                this.memoSalesInvoiceMainRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case FG_MEMO_SALES_RETURN:
            case JEW_MEMO_SALES_RETURN:
                MemoSalesReturnInvoiceMainEntity memoSalesReturnInvoiceMainEntity = this.memoSalesReturnInvoiceMainRepository.findById(documentDto.getInvoiceId()).orElse(new MemoSalesReturnInvoiceMainEntity());
                documentUrl = memoSalesReturnInvoiceMainEntity.getDocumentUrl();
                this.memoSalesReturnInvoiceMainRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case FG_MEMO_PURCHASE_RETURN:
            case JEW_MEMO_PURCHASE_RETURN:
                MemoPurchaseReturnInvoiceMainEntity memoPurchaseReturnInvoiceMainEntity = this.memoPurchaseReturnInvoiceMainRepository.findById(documentDto.getInvoiceId()).orElse(new MemoPurchaseReturnInvoiceMainEntity());
                documentUrl = memoPurchaseReturnInvoiceMainEntity.getDocumentUrl();
                this.memoPurchaseReturnInvoiceMainRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case JEWELLERY:
            case JEWELLERY_IMAGE:
                ProductEntity productEntity = this.productRepository.findById(documentDto.getInvoiceId()).orElse(new ProductEntity());
                documentUrl = productEntity.getPicturePath();
                this.productRepository.updatePicturePath(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case JEW_INWARD:
                GoodsInwardMainEntity goodsInwardMainEntity = this.goodsInwardMainRepository.findById(documentDto.getInvoiceId()).orElse(new GoodsInwardMainEntity());
                documentUrl = goodsInwardMainEntity.getDocumentUrl();
                this.goodsInwardMainRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case JEW_OUTWARD:
                GoodsOutwardMainEntity goodsOutwardMainEntity = this.goodsOutwardMainRepository.findById(documentDto.getInvoiceId()).orElse(new GoodsOutwardMainEntity());
                documentUrl = goodsOutwardMainEntity.getDocumentUrl();
                this.goodsOutwardMainRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case DESIGN_IMAGE:
                DesignEntity designEntity = this.designRepository.findById(documentDto.getInvoiceId()).orElse(new DesignEntity());
                documentUrl = designEntity.getImageUrl();
                this.designRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case SKU:
            case SKU_IMAGE:
                SkuEntity skuEntity = this.skuRepository.findById(documentDto.getInvoiceId()).orElse(new SkuEntity());
                documentUrl = skuEntity.getImageUrl();
                this.skuRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
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
