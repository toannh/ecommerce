/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.controller.cp;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import vn.chodientu.entity.db.ItemCrawlLog;
import vn.chodientu.entity.db.SellerCrawlLog;
import vn.chodientu.entity.input.ItemCrawlLogSearch;
import vn.chodientu.entity.input.SellerCrawlLogSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.service.ItemCrawlLogService;
import vn.chodientu.service.SellerCrawlLogService;

/**
 *
 * @author CANH
 */
@Controller("serviceSellerCrawlLog")
@RequestMapping("/cp/sellercrawllog")
public class SellerCrawlLogController extends BaseCp {

    @Autowired
    private ItemCrawlLogService crawlLogService;
    @Autowired
    private SellerCrawlLogService sellerCrawlLogService;

    @RequestMapping(method = RequestMethod.POST)
    public String postSellerCrawlLog(@ModelAttribute SellerCrawlLogSearch sellerCrlLog,
            ModelMap modelMap, HttpSession session,
            @RequestParam(value = "page", defaultValue = "1") int page) {
        // call mapcate service
        session.setAttribute("sellerCrawlLogSearch", sellerCrlLog);
        sellerCrlLog.setPageSize(30);
        sellerCrlLog.setPageIndex(page - 1);
        DataPage<SellerCrawlLog> data = sellerCrawlLogService.search(sellerCrlLog);
        SellerCrawlLog totalLog = getTotal(data);
        modelMap.put("dataPage", data);
        modelMap.put("totalData", totalLog);
        modelMap.put("sellerCrawlLogSearch", sellerCrlLog);
        modelMap.put("clientScript", "sellercrawl.init();");
        return "cp.sellercrawllog.list";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getSellerCrawlLog(ModelMap modelMap, HttpSession session, @RequestParam(value = "page", defaultValue = "1") int page) {
        SellerCrawlLogSearch sellerCrlLog = new SellerCrawlLogSearch();
        if (session.getAttribute("sellerCrawlLogSearch") != null && page != 0) {
            sellerCrlLog = (SellerCrawlLogSearch) session.getAttribute("sellerCrawlLogSearch");
        } else {
            session.setAttribute("sellerCrawlLogSearch", sellerCrlLog);
        }
        sellerCrlLog.setPageIndex(page - 1);
        sellerCrlLog.setPageSize(30);
//        search.setStatus(1);
        DataPage<SellerCrawlLog> data = sellerCrawlLogService.search(sellerCrlLog);
        SellerCrawlLog totalLog = getTotal(data);
        modelMap.put("dataPage", data);
        modelMap.put("sellerCrawlLogSearch", sellerCrlLog);
        modelMap.put("totalData", totalLog);
        modelMap.put("clientScript", "sellercrawl.init();");
        return "cp.sellercrawllog.list";
    }
    
    public SellerCrawlLog getTotal(DataPage<SellerCrawlLog> page) {
        SellerCrawlLog total = new SellerCrawlLog();
        List<SellerCrawlLog> listSellerCrllg = page.getData();
        long totalRequest = 0;
        long totalErr = 0;
        long totalSucs = 0;
        long totalAdd = 0;
        long totalEdit = 0;
        long totalItemok =0;
        long totalItemNoCon = 0;
        long totalItemOKRealT = 0;
        long totalItemMissRealT = 0;
        for(SellerCrawlLog sellerCrllg : listSellerCrllg){
            totalRequest += sellerCrllg.getTotalRequest();
            totalErr += sellerCrllg.getErrorRequest();
            totalSucs += sellerCrllg.getSuccessRequest();
            totalAdd += sellerCrllg.getAddRequest();
            totalEdit += sellerCrllg.getEditRequest();
            totalItemok += sellerCrllg.getEnoughCondition();
            totalItemNoCon += sellerCrllg.getNoCondition();
            totalItemOKRealT += sellerCrllg.getItemOK();
            totalItemMissRealT += sellerCrllg.getItemMiss();
        }
        total.setTotalRequest(totalRequest);
        total.setErrorRequest(totalErr);
        total.setSuccessRequest(totalSucs);
        total.setAddRequest(totalAdd);
        total.setEditRequest(totalEdit);
        total.setNoCondition(totalItemNoCon);
        total.setEnoughCondition(totalItemok);
        total.setItemOK(totalItemOKRealT);
        total.setItemMiss(totalItemMissRealT);
        return total;
                
    }

}
