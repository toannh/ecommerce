/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.controller.cp;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import vn.chodientu.entity.db.ItemCrawlLog;
import vn.chodientu.entity.input.ItemCrawlLogSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.service.ItemCrawlLogService;

/**
 *
 * @author CANH
 */
@Controller("serviceItemCrawlLog")
@RequestMapping("/cp/itemcrawllog")
public class ItemCrawlLogController extends BaseCp {

    @Autowired
    private ItemCrawlLogService crawlLogService;

    @RequestMapping(method = RequestMethod.POST)
    public String postItemCrawlLog(@ModelAttribute ItemCrawlLogSearch crawlLogSearch,
            ModelMap modelMap, HttpSession session,
            @RequestParam(value = "page", defaultValue = "1") int page) {
        // call mapcate service
        session.setAttribute("crawlLogSearch", crawlLogSearch);
        crawlLogSearch.setPageSize(30);
        crawlLogSearch.setPageIndex(page - 1);
        DataPage<ItemCrawlLog> data = crawlLogService.search(crawlLogSearch);
        modelMap.put("dataPage", data);
        modelMap.put("crawlLogSearch", crawlLogSearch);
        modelMap.put("clientScript", "crawllog.init();");
        return "cp.itemcrawllog.list";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getItemCrawlLog(ModelMap modelMap, HttpSession session, @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "itemId", required = false) String itemId,
            @RequestParam(value = "sellerId", required = false) String sellerId,
            @RequestParam(value = "timeFrom", required = false,defaultValue = "0") long timeFrom,
            @RequestParam(value = "timeTo", required = false, defaultValue = "0") long timeTo,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "imageStatus", required = false) String imageStatus,
            @RequestParam(value = "status", required = false,defaultValue = "-1") int status,
            @RequestParam(value = "ref", required = false, defaultValue = "false") boolean ref) {

        ItemCrawlLogSearch search = new ItemCrawlLogSearch();
        if (ref) {
            if (itemId != null && !itemId.equals("")) {
                search.setItemId(itemId);
            }
            if (sellerId != null && !sellerId.equals("")) {
                search.setSellerId(sellerId);
            }
            if (timeFrom > 0) {
                search.setTimeFrom(timeFrom);
            }
            if (timeTo > 0) {
                search.setTimeTo(timeTo);
            }
            if (type != null && !type.equals("")) {
                search.setType(type);
            }
            if (imageStatus != null && !imageStatus.equals("")) {
                search.setImageStatus(imageStatus);
            }
            if (status > 0) {
                search.setStatus(status);
            }
            session.setAttribute("crawlLogSearch", search);
        } else {
            if (session.getAttribute("crawlLogSearch") != null && page != 0) {
                search = (ItemCrawlLogSearch) session.getAttribute("crawlLogSearch");
            } else {
                session.setAttribute("crawlLogSearch", search);
            }
        }
        search.setPageIndex(page - 1);
        search.setPageSize(30);
        DataPage<ItemCrawlLog> data = crawlLogService.search(search);
        modelMap.put("dataPage", data);
        modelMap.put("crawlLogSearch", search);
        modelMap.put("clientScript", "crawllog.init();");
        return "cp.itemcrawllog.list";
    }

}
