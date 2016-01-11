package vn.chodientu.controller.market;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.chodientu.entity.db.BigLandingCategory;
import vn.chodientu.entity.db.BigLanding;
import vn.chodientu.entity.db.BigLandingItem;
import vn.chodientu.entity.enu.ImageType;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.service.BigLandingService;
import vn.chodientu.service.ImageService;
import vn.chodientu.util.TextUtils;

/**
 *
 * @author Anhpp
 */
@Controller("biglandingbrowseController")
public class BigLandingBrowseController extends BaseMarket {

    @Autowired
    private BigLandingService landingService;
    @Autowired
    private ImageService imageService;

    @RequestMapping({"/biglanding/{id}/{cid}/{name}.html"})
    public String browse(@PathVariable("id") String id, @PathVariable("cid") String cid, @RequestParam(value = "page", defaultValue = "0") int page,
            HttpServletResponse response,
            ModelMap model, HttpServletRequest request) throws UnsupportedEncodingException, Exception {
        BigLanding landing = landingService.getBigLanding(id);

        if (landing == null) {
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.setHeader("Location", baseUrl + "/index.html");
            return "market.index";
            //return "redirect:" + baseUrl + "/index.html";
        }
        List<String> imgLogo = imageService.get(ImageType.LANDING_LOGO_BANNER, landing.getId());
        if (imgLogo != null && !imgLogo.isEmpty()) {
            landing.setLogoBanner(imageService.getUrl(imgLogo.get(0)).compress(100).getUrl(landing.getName()));
        }
        List<String> imgHeartBanner = imageService.get(ImageType.LANDING_HEART_BANNER, landing.getId());
        if (imgHeartBanner != null && !imgHeartBanner.isEmpty()) {
            landing.setHeartBanner(imageService.getUrl(imgHeartBanner.get(0)).compress(100).getUrl(landing.getName()));
        }
        List<String> imgCenterBanner = imageService.get(ImageType.LANDING_CENTER_BANNER, landing.getId());
        if (imgCenterBanner != null && !imgCenterBanner.isEmpty()) {
            landing.setCenterBanner(imageService.getUrl(imgCenterBanner.get(0)).compress(100).getUrl(landing.getName()));
        }
        model.put("canonical", "/biglanding/" + landing.getId() + "/" + cid + "/" + TextUtils.createAlias(landing.getName()) + ".html");
        model.put("title", "Xem chương trình " + landing.getName() + " với ưu đãi đặc biệt");
        model.put("description", "Xem chương trình ưu đãi đặc biệt:" + landing.getName() + " tại chodientu.vn - Giá rẻ, nhiều khuyến mại, thanh toán online, CoD, giao hàng toàn quốc, bảo vệ người mua.");
        BigLandingCategory bigLandingCategory = landingService.getBigLandingCategory(cid);
        String pageS = "";
        if (page > 0) {
            pageS = "?page=" + page;
        } else {
            pageS = "";
        }
        if (page > 0) {
            page = page - 1;
        }
        if (bigLandingCategory != null) {
            model.put("bigLandingCategory", bigLandingCategory);
            String parentId = bigLandingCategory.getParentId();
            if (parentId != null && parentId != "") {
                BigLandingCategory categoryParent = landingService.getBigLandingCategory(parentId);
                List<BigLandingCategory> categoryAllChild = landingService.getCategoriesChild(parentId);
                model.put("categoryParent", categoryParent);
                model.put("categoryChild", bigLandingCategory);
                model.put("categoryAllChild", categoryAllChild);
                List<String> cIds = new ArrayList<>();
                cIds.add(cid);
                DataPage<BigLandingItem> dataPage = landingService.getAllItemByCategory(cIds, new PageRequest(page, 40),0);
                model.put("bigLandingItems", dataPage);
            } else {
                List<BigLandingCategory> categoryChild = landingService.getCategoriesChild(cid);
                List<String> allId = new ArrayList<>();
                for (BigLandingCategory blc : categoryChild) {

                    String idC = blc.getId();
                    allId.add(idC);
                    List<BigLandingItem> bigLandingItems = landingService.getTopItemByCategories(idC);
                    blc.setBigLandingItem(bigLandingItems);
                }
                DataPage<BigLandingItem> dataPage = landingService.getAllItemByCategory(allId, new PageRequest(page, 40), 0);
                model.put("bigLandingItems", dataPage);
                model.put("categoryChild", bigLandingCategory);
                model.put("categoryChildList", categoryChild);
                model.put("categoryAllChild", categoryChild);
                model.put("categoryParent", bigLandingCategory);

            }
        } else {
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.setHeader("Location", baseUrl + "/biglanding/" + id + "/" + TextUtils.createAlias(landing.getName()) + ".html");
            return "biglanding.index";
            //return "redirect:" + baseUrl + "/biglanding/"+id+"/"+TextUtils.createAlias(landing.getName()) +".html";
        }
        model.put("clientScript", "biglanding.init()");
        model.put("bigLanding", landing);
        model.put("remarketing", "dynx_itemid: \"\",dynx_pagetype: \"other\",dynx_totalvalue:\"\"");
        return "biglanding.browse";
    }

}
