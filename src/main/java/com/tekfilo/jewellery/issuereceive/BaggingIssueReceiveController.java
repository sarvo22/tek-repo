package com.tekfilo.jewellery.issuereceive;

import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.issuereceive.dto.BaggingIssueReceiveDetailDto;
import com.tekfilo.jewellery.issuereceive.dto.BaggingIssueReceiveMainDto;
import com.tekfilo.jewellery.issuereceive.entity.*;
import com.tekfilo.jewellery.issuereceive.service.BaggingIssueReceiveService;
import com.tekfilo.jewellery.multitenancy.UserContext;
import com.tekfilo.jewellery.util.JewResponse;
import com.tekfilo.jewellery.util.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/jew/bagging")
public class BaggingIssueReceiveController {

    @Autowired
    BaggingIssueReceiveService baggingIssueReceiveService;

    @PostMapping("/issue/respective/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<BaggingIssueRespectiveMainEntity>> findAllBaggingIssueRespectiveMain(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClauses) {
        return new ResponseEntity<Page<BaggingIssueRespectiveMainEntity>>
                (baggingIssueReceiveService.findAllBaggingIssueRespectiveMain(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClauses), HttpStatus.OK);
    }

    @GetMapping("/issue/respective/main/findbyid/{id}")
    public ResponseEntity<BaggingIssueRespectiveMainEntity> findBaggingIssueRespectiveById(@PathVariable("id") Integer id) {
        return new ResponseEntity<BaggingIssueRespectiveMainEntity>(baggingIssueReceiveService.findBaggingIssueRespectiveById(id), HttpStatus.OK);
    }

    @GetMapping("/issue/respective/detailbymainid/{invId}")
    public ResponseEntity<List<BaggingIssueRespectiveDetailEntity>> findBaggingIssueRespectiveDetailByMainId(@PathVariable("invId") Integer invId) {
        return new ResponseEntity<List<BaggingIssueRespectiveDetailEntity>>(baggingIssueReceiveService.findBaggingIssueRespectiveDetailByMainId(invId), HttpStatus.OK);
    }

    @PostMapping("/issue/respective/main/save")
    public ResponseEntity<JewResponse> saveBaggingIssueRespectiveMain(@RequestBody BaggingIssueReceiveMainDto issueReceiveMainDto) {
        JewResponse response = new JewResponse();
        try {
            BaggingIssueRespectiveMainEntity entity = baggingIssueReceiveService.saveIssueRespectiveMain(issueReceiveMainDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage("Saved Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/issue/respective/main/modify")
    public ResponseEntity<JewResponse> updateBaggingIssueRespectiveMain(@RequestBody BaggingIssueReceiveMainDto issueReceiveMainDto) {
        JewResponse response = new JewResponse();
        try {
            BaggingIssueRespectiveMainEntity entity = baggingIssueReceiveService.saveIssueRespectiveMain(issueReceiveMainDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Modified Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/issue/respective/main/remove/{id}")
    public ResponseEntity<JewResponse> removeBaggingIssueRespectiveMain(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            List<BaggingIssueRespectiveDetailEntity> detailEntities = baggingIssueReceiveService.findBaggingIssueRespectiveDetailByMainId(id);
            detailEntities.stream().forEach(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(UserContext.getLoggedInUser());
            });
            BaggingIssueRespectiveMainEntity entity = baggingIssueReceiveService.findBaggingIssueRespectiveById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            baggingIssueReceiveService.removeIssueRespectiveMain(entity, detailEntities);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Removed Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying " + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }


    @PostMapping("/issue/respective/detail/save")
    public ResponseEntity<JewResponse> saveBaggingIssueRespectiveDetail(@RequestBody BaggingIssueReceiveDetailDto baggingIssueReceiveDetailDto) {
        JewResponse response = new JewResponse();
        try {
            BaggingIssueRespectiveDetailEntity entity = baggingIssueReceiveService.saveIssueRespectiveDetail(baggingIssueReceiveDetailDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage("Saved Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while save {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/issue/respective/detail/modify")
    public ResponseEntity<JewResponse> updateBaggingIssueRespectiveDetail(@RequestBody BaggingIssueReceiveDetailDto baggingIssueReceiveDetailDto) {
        JewResponse response = new JewResponse();
        try {
            BaggingIssueRespectiveDetailEntity entity = baggingIssueReceiveService.saveIssueRespectiveDetail(baggingIssueReceiveDetailDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Modified Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/issue/respective/detail/remove/{id}")
    public ResponseEntity<JewResponse> removeBaggingIssueRespectiveDetail(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            BaggingIssueRespectiveDetailEntity entity = baggingIssueReceiveService.findBaggingIssueRespectiveDetailById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            baggingIssueReceiveService.removeBaggingIssueRespectiveDetail(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("remove_100");
            response.setMessage("Removed Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying " + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/issue/respective/main/removeall")
    public ResponseEntity<JewResponse> removeAllBaggingIssueRespective(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<BaggingIssueRespectiveDetailEntity> detailEntities = baggingIssueReceiveService.findBaggingIssueRespectiveDetailByMainIdList(ids);
            detailEntities.stream().forEach(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(UserContext.getLoggedInUser());
            });

            List<BaggingIssueRespectiveMainEntity> baggingIssueRespectiveMainEntities = baggingIssueReceiveService.findAllBaggingIssueRespectiveMainByIds(ids);
            baggingIssueRespectiveMainEntities.stream().forEachOrdered(c -> {
                c.setIsDeleted(1);
                c.setModifiedBy(UserContext.getLoggedInUser());
            });
            baggingIssueReceiveService.removeAllBaggingIssueRespectiveMain(baggingIssueRespectiveMainEntities, detailEntities);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/issue/respective/lock")
    public ResponseEntity<JewResponse> lockBaggingIssueRespective(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<BaggingIssueRespectiveMainEntity> entities = baggingIssueReceiveService.findAllBaggingIssueRespectiveMainByIds(ids);
            entities.stream().forEachOrdered(c -> {
                c.setIsLocked(1);
                c.setModifiedBy(UserContext.getLoggedInUser());
            });
            baggingIssueReceiveService.lockBaggingIssueRespective(entities);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("lock_100");
            response.setMessage(MessageConstants.RECORD_LOCKED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/issue/respective/unlock")
    public ResponseEntity<JewResponse> unlockBaggingIssueRespective(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<BaggingIssueRespectiveMainEntity> entities = baggingIssueReceiveService.findAllBaggingIssueRespectiveMainByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            baggingIssueReceiveService.unlockBaggingIssueRespective(entities);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("lock_100");
            response.setMessage(MessageConstants.RECORD_UNLOCKED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }


    //Irrespective Issue entry without jewllery no
    @PostMapping("/issue/irrespective/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<BaggingIssueIrrespectiveMainEntity>> findAllBaggingIssueIrrespectiveMain(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClauses) {
        return new ResponseEntity<Page<BaggingIssueIrrespectiveMainEntity>>
                (baggingIssueReceiveService.findAllBaggingIssueIrRespectiveMain(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClauses), HttpStatus.OK);
    }

    @GetMapping("/issue/irrespective/main/findbyid/{id}")
    public ResponseEntity<BaggingIssueIrrespectiveMainEntity> findBaggingIssueIrrespectiveById(@PathVariable("id") Integer id) {
        return new ResponseEntity<BaggingIssueIrrespectiveMainEntity>(baggingIssueReceiveService.findBaggingIssueIrRespectiveById(id), HttpStatus.OK);
    }

    @GetMapping("/issue/irrespective/detailbymainid/{invId}")
    public ResponseEntity<List<BaggingIssueIrrespectiveDetailEntity>> findBaggingIssueIrrespectiveDetailByMainId(@PathVariable("invId") Integer invId) {
        return new ResponseEntity<List<BaggingIssueIrrespectiveDetailEntity>>(baggingIssueReceiveService.findBaggingIssueIrRespectiveDetailByMainId(invId), HttpStatus.OK);
    }

    @PostMapping("/issue/irrespective/main/save")
    public ResponseEntity<JewResponse> saveBaggingIssueIrrespectiveMain(@RequestBody BaggingIssueReceiveMainDto issueReceiveMainDto) {
        JewResponse response = new JewResponse();
        try {
            BaggingIssueIrrespectiveMainEntity entity = baggingIssueReceiveService.saveIssueIrRespectiveMain(issueReceiveMainDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage("Saved Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/issue/irrespective/main/modify")
    public ResponseEntity<JewResponse> updateIssueIrrespectiveMain(@RequestBody BaggingIssueReceiveMainDto issueReceiveMainDto) {
        JewResponse response = new JewResponse();
        try {
            BaggingIssueIrrespectiveMainEntity entity = baggingIssueReceiveService.saveIssueIrRespectiveMain(issueReceiveMainDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Modified Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/issue/irrespective/main/remove/{id}")
    public ResponseEntity<JewResponse> removeBaggingIssueIrrespectiveMain(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            List<BaggingIssueIrrespectiveDetailEntity> detailEntities = baggingIssueReceiveService.findBaggingIssueIrRespectiveDetailByMainId(id);
            detailEntities.stream().forEach(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(UserContext.getLoggedInUser());
            });
            BaggingIssueIrrespectiveMainEntity entity = baggingIssueReceiveService.findBaggingIssueIrRespectiveById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            baggingIssueReceiveService.removeIssueIrRespectiveMain(entity, detailEntities);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Removed Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying " + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }


    @PostMapping("/issue/irrespective/detail/save")
    public ResponseEntity<JewResponse> saveBaggingIssueIrrespectiveDetail(@RequestBody BaggingIssueReceiveDetailDto baggingIssueReceiveDetailDto) {
        JewResponse response = new JewResponse();
        try {
            BaggingIssueIrrespectiveDetailEntity entity = baggingIssueReceiveService.saveIssueIrRespectiveDetail(baggingIssueReceiveDetailDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage("Saved Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while save {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/issue/irrespective/detail/modify")
    public ResponseEntity<JewResponse> updateBaggingIssueIrrespectiveDetail(@RequestBody BaggingIssueReceiveDetailDto baggingIssueReceiveDetailDto) {
        JewResponse response = new JewResponse();
        try {
            BaggingIssueIrrespectiveDetailEntity entity = baggingIssueReceiveService.saveIssueIrRespectiveDetail(baggingIssueReceiveDetailDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Modified Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/issue/irrespective/detail/remove/{id}")
    public ResponseEntity<JewResponse> removeBaggingIssueIrrespectiveDetail(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            BaggingIssueIrrespectiveDetailEntity entity = baggingIssueReceiveService.findBaggingIssueIrRespectiveDetailById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            baggingIssueReceiveService.removeBaggingIssueIrRespectiveDetail(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("remove_100");
            response.setMessage("Removed Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying " + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/issue/irrespective/main/removeall")
    public ResponseEntity<JewResponse> removeAllBaggingIssueIrrespective(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<BaggingIssueIrrespectiveDetailEntity> detailEntities = baggingIssueReceiveService.findBaggingIssueIrRespectiveDetailByMainIdList(ids);
            detailEntities.stream().forEach(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(UserContext.getLoggedInUser());
            });

            List<BaggingIssueIrrespectiveMainEntity> baggingIssueIrRespectiveMainEntities = baggingIssueReceiveService.findAllBaggingIssueIrRespectiveMainByIds(ids);
            baggingIssueIrRespectiveMainEntities.stream().forEachOrdered(c -> {
                c.setIsDeleted(1);
                c.setModifiedBy(UserContext.getLoggedInUser());
            });
            baggingIssueReceiveService.removeAllBaggingIssueIrRespectiveMain(baggingIssueIrRespectiveMainEntities, detailEntities);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/issue/irrespective/lock")
    public ResponseEntity<JewResponse> lockBaggingIssueIrrespective(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<BaggingIssueIrrespectiveMainEntity> entities = baggingIssueReceiveService.findAllBaggingIssueIrRespectiveMainByIds(ids);
            entities.stream().forEachOrdered(c -> {
                c.setIsLocked(1);
                c.setModifiedBy(UserContext.getLoggedInUser());
            });
            baggingIssueReceiveService.lockBaggingIssueIrRespective(entities);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("lock_100");
            response.setMessage(MessageConstants.RECORD_LOCKED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/issue/irrespective/unlock")
    public ResponseEntity<JewResponse> unlockBaggingIssueIrrespective(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<BaggingIssueIrrespectiveMainEntity> entities = baggingIssueReceiveService.findAllBaggingIssueIrRespectiveMainByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            baggingIssueReceiveService.unlockBaggingIssueIrRespective(entities);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("lock_100");
            response.setMessage(MessageConstants.RECORD_UNLOCKED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    // Return Respective and Ir-Respective
    @PostMapping("/return/respective/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<BaggingReturnRespectiveMainEntity>> findAllBaggingReturnRespectiveMain(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClauses) {
        return new ResponseEntity<Page<BaggingReturnRespectiveMainEntity>>
                (baggingIssueReceiveService.findAllBaggingReturnRespectiveMain(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClauses), HttpStatus.OK);
    }

    @GetMapping("/return/respective/main/findbyid/{id}")
    public ResponseEntity<BaggingReturnRespectiveMainEntity> findBaggingReturnRespectiveById(@PathVariable("id") Integer id) {
        return new ResponseEntity<BaggingReturnRespectiveMainEntity>(baggingIssueReceiveService.findBaggingReturnRespectiveById(id), HttpStatus.OK);
    }

    @GetMapping("/return/respective/detailbymainid/{invId}")
    public ResponseEntity<List<BaggingReturnRespectiveDetailEntity>> findBaggingReturnRespectiveDetailByMainId(@PathVariable("invId") Integer invId) {
        return new ResponseEntity<List<BaggingReturnRespectiveDetailEntity>>(baggingIssueReceiveService.findBaggingReturnRespectiveDetailByMainId(invId), HttpStatus.OK);
    }

    @PostMapping("/return/respective/main/save")
    public ResponseEntity<JewResponse> save(@RequestBody BaggingIssueReceiveMainDto returnReceiveMainDto) {
        JewResponse response = new JewResponse();
        try {
            BaggingReturnRespectiveMainEntity entity = baggingIssueReceiveService.saveReturnRespectiveMain(returnReceiveMainDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage("Saved Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/return/respective/main/modify")
    public ResponseEntity<JewResponse> update(@RequestBody BaggingIssueReceiveMainDto returnReceiveMainDto) {
        JewResponse response = new JewResponse();
        try {
            BaggingReturnRespectiveMainEntity entity = baggingIssueReceiveService.saveReturnRespectiveMain(returnReceiveMainDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Modified Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/return/respective/main/remove/{id}")
    public ResponseEntity<JewResponse> remove(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            List<BaggingReturnRespectiveDetailEntity> detailEntities = baggingIssueReceiveService.findBaggingReturnRespectiveDetailByMainId(id);
            detailEntities.stream().forEach(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(UserContext.getLoggedInUser());
            });
            BaggingReturnRespectiveMainEntity entity = baggingIssueReceiveService.findBaggingReturnRespectiveById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            baggingIssueReceiveService.removeReturnRespectiveMain(entity, detailEntities);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Removed Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying " + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }


    @PostMapping("/return/respective/detail/save")
    public ResponseEntity<JewResponse> saveDetail(@RequestBody BaggingIssueReceiveDetailDto baggingIssueReceiveDetailDto) {
        JewResponse response = new JewResponse();
        try {
            BaggingReturnRespectiveDetailEntity entity = baggingIssueReceiveService.saveReturnRespectiveDetail(baggingIssueReceiveDetailDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage("Saved Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while save {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/return/respective/detail/modify")
    public ResponseEntity<JewResponse> updateDetail(@RequestBody BaggingIssueReceiveDetailDto baggingIssueReceiveDetailDto) {
        JewResponse response = new JewResponse();
        try {
            BaggingReturnRespectiveDetailEntity entity = baggingIssueReceiveService.saveReturnRespectiveDetail(baggingIssueReceiveDetailDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Modified Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/return/respective/detail/remove/{id}")
    public ResponseEntity<JewResponse> removeDetail(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            BaggingReturnRespectiveDetailEntity entity = baggingIssueReceiveService.findBaggingReturnRespectiveDetailById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            baggingIssueReceiveService.removeBaggingReturnRespectiveDetail(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("remove_100");
            response.setMessage("Removed Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying " + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/return/respective/main/removeall")
    public ResponseEntity<JewResponse> remove(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<BaggingReturnRespectiveDetailEntity> detailEntities = baggingIssueReceiveService.findBaggingReturnRespectiveDetailByMainIdList(ids);
            detailEntities.stream().forEach(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(UserContext.getLoggedInUser());
            });

            List<BaggingReturnRespectiveMainEntity> baggingReturnRespectiveMainEntities = baggingIssueReceiveService.findAllBaggingReturnRespectiveMainByIds(ids);
            baggingReturnRespectiveMainEntities.stream().forEachOrdered(c -> {
                c.setIsDeleted(1);
                c.setModifiedBy(UserContext.getLoggedInUser());
            });
            baggingIssueReceiveService.removeAllBaggingReturnRespectiveMain(baggingReturnRespectiveMainEntities, detailEntities);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/return/respective/lock")
    public ResponseEntity<JewResponse> lock(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<BaggingReturnRespectiveMainEntity> entities = baggingIssueReceiveService.findAllBaggingReturnRespectiveMainByIds(ids);
            entities.stream().forEachOrdered(c -> {
                c.setIsLocked(1);
                c.setModifiedBy(UserContext.getLoggedInUser());
            });
            baggingIssueReceiveService.lockBaggingReturnRespective(entities);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("lock_100");
            response.setMessage(MessageConstants.RECORD_LOCKED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/return/respective/unlock")
    public ResponseEntity<JewResponse> unlock(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<BaggingReturnRespectiveMainEntity> entities = baggingIssueReceiveService.findAllBaggingReturnRespectiveMainByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            baggingIssueReceiveService.unlockBaggingReturnRespective(entities);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("lock_100");
            response.setMessage(MessageConstants.RECORD_UNLOCKED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }


    //Irrespective Return entry without jewllery no
    @PostMapping("/return/irrespective/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<BaggingReturnIrrespectiveMainEntity>> findAllBaggingReturnIrrespectiveMain(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClauses) {
        return new ResponseEntity<Page<BaggingReturnIrrespectiveMainEntity>>
                (baggingIssueReceiveService.findAllBaggingReturnIrRespectiveMain(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClauses), HttpStatus.OK);
    }

    @GetMapping("/return/irrespective/main/findbyid/{id}")
    public ResponseEntity<BaggingReturnIrrespectiveMainEntity> findBaggingReturnIrrespectiveById(@PathVariable("id") Integer id) {
        return new ResponseEntity<BaggingReturnIrrespectiveMainEntity>(baggingIssueReceiveService.findBaggingReturnIrRespectiveById(id), HttpStatus.OK);
    }

    @GetMapping("/return/irrespective/detailbymainid/{invId}")
    public ResponseEntity<List<BaggingReturnIrrespectiveDetailEntity>> findBaggingReturnIrrespectiveDetailByMainId(@PathVariable("invId") Integer invId) {
        return new ResponseEntity<List<BaggingReturnIrrespectiveDetailEntity>>(baggingIssueReceiveService.findBaggingReturnIrRespectiveDetailByMainId(invId), HttpStatus.OK);
    }

    @PostMapping("/return/irrespective/main/save")
    public ResponseEntity<JewResponse> saveBaggingReturnIrrespectiveMain(@RequestBody BaggingIssueReceiveMainDto returnReceiveMainDto) {
        JewResponse response = new JewResponse();
        try {
            BaggingReturnIrrespectiveMainEntity entity = baggingIssueReceiveService.saveReturnIrRespectiveMain(returnReceiveMainDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage("Saved Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/return/irrespective/main/modify")
    public ResponseEntity<JewResponse> updateReturnIrrespectiveMain(@RequestBody BaggingIssueReceiveMainDto returnReceiveMainDto) {
        JewResponse response = new JewResponse();
        try {
            BaggingReturnIrrespectiveMainEntity entity = baggingIssueReceiveService.saveReturnIrRespectiveMain(returnReceiveMainDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Modified Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/return/irrespective/main/remove/{id}")
    public ResponseEntity<JewResponse> removeBaggingReturnIrrespectiveMain(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            List<BaggingReturnIrrespectiveDetailEntity> detailEntities = baggingIssueReceiveService.findBaggingReturnIrRespectiveDetailByMainId(id);
            detailEntities.stream().forEach(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(UserContext.getLoggedInUser());
            });
            BaggingReturnIrrespectiveMainEntity entity = baggingIssueReceiveService.findBaggingReturnIrRespectiveById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            baggingIssueReceiveService.removeReturnIrRespectiveMain(entity, detailEntities);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Removed Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying " + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }


    @PostMapping("/return/irrespective/detail/save")
    public ResponseEntity<JewResponse> saveBaggingReturnIrrespectiveDetail(@RequestBody BaggingIssueReceiveDetailDto baggingIssueReceiveDetailDto) {
        JewResponse response = new JewResponse();
        try {
            BaggingReturnIrrespectiveDetailEntity entity = baggingIssueReceiveService.saveReturnIrRespectiveDetail(baggingIssueReceiveDetailDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage("Saved Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while save {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/return/irrespective/detail/modify")
    public ResponseEntity<JewResponse> updateBaggingReturnIrrespectiveDetail(@RequestBody BaggingIssueReceiveDetailDto baggingIssueReceiveDetailDto) {
        JewResponse response = new JewResponse();
        try {
            BaggingReturnIrrespectiveDetailEntity entity = baggingIssueReceiveService.saveReturnIrRespectiveDetail(baggingIssueReceiveDetailDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Modified Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/return/irrespective/detail/remove/{id}")
    public ResponseEntity<JewResponse> removeBaggingReturnIrrespectiveDetail(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            BaggingReturnIrrespectiveDetailEntity entity = baggingIssueReceiveService.findBaggingReturnIrRespectiveDetailById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            baggingIssueReceiveService.removeBaggingReturnIrRespectiveDetail(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("remove_100");
            response.setMessage("Removed Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying " + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/return/irrespective/main/removeall")
    public ResponseEntity<JewResponse> removeAllBaggingReturnIrrespective(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<BaggingReturnIrrespectiveDetailEntity> detailEntities = baggingIssueReceiveService.findBaggingReturnIrRespectiveDetailByMainIdList(ids);
            detailEntities.stream().forEach(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(UserContext.getLoggedInUser());
            });

            List<BaggingReturnIrrespectiveMainEntity> baggingReturnIrRespectiveMainEntities = baggingIssueReceiveService.findAllBaggingReturnIrRespectiveMainByIds(ids);
            baggingReturnIrRespectiveMainEntities.stream().forEachOrdered(c -> {
                c.setIsDeleted(1);
                c.setModifiedBy(UserContext.getLoggedInUser());
            });
            baggingIssueReceiveService.removeAllBaggingReturnIrRespectiveMain(baggingReturnIrRespectiveMainEntities, detailEntities);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }
    
    @PutMapping("/return/irrespective/lock")
    public ResponseEntity<JewResponse> lockBaggingReturnIrrespective(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<BaggingReturnIrrespectiveMainEntity> entities = baggingIssueReceiveService.findAllBaggingReturnIrRespectiveMainByIds(ids);
            entities.stream().forEachOrdered(c -> {
                c.setIsLocked(1);
                c.setModifiedBy(UserContext.getLoggedInUser());
            });
            baggingIssueReceiveService.lockBaggingReturnIrRespective(entities);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("lock_100");
            response.setMessage(MessageConstants.RECORD_LOCKED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/return/irrespective/unlock")
    public ResponseEntity<JewResponse> unlockBaggingReturnIrrespective(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<BaggingReturnIrrespectiveMainEntity> entities = baggingIssueReceiveService.findAllBaggingReturnIrRespectiveMainByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            baggingIssueReceiveService.unlockBaggingReturnIrRespective(entities);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("lock_100");
            response.setMessage(MessageConstants.RECORD_UNLOCKED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }
}
