package vn.chodientu.controller.market;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.chodientu.entity.db.BigLanding;
import vn.chodientu.entity.db.BigLandingCategory;
import vn.chodientu.entity.db.BigLandingItem;
import vn.chodientu.entity.enu.ImageType;
import vn.chodientu.service.BigLandingService;
import vn.chodientu.service.ImageService;
import vn.chodientu.util.UrlUtils;

/**
 * @since Jul 14, 2014
 * @author Account
 */
@Controller("biglandingController")
public class BigLandingController extends BaseMarket {

    @Autowired
    private BigLandingService bigLandingService;
    @Autowired
    private ImageService imageService;

    @RequestMapping({"/biglanding/{id}/{name}.html"})
    public String browse(
            @PathVariable("id") String id, @PathVariable("name") String name,
            HttpServletResponse response, HttpServletRequest request,
            ModelMap model) {
        BigLanding landing = null;
        try {
             landing = bigLandingService.getBigLanding(id);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.setHeader("Location", baseUrl + "/index.html");
            return "market.index";
            //return "redirect:" + baseUrl + "/index.html";
        }

        if (landing == null) {
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.setHeader("Location", baseUrl + "/index.html");
            return "market.index";
            //return "redirect:" + baseUrl + "/index.html";
        }

        model.put("canonical", "/biglanding/" + id + "/" + landing.getName() + "/.html");
        model.put("title", "Xem chương trình " + landing.getName() + " với ưu đãi đặc biệt");
        String landingId = landing.getId();
        List<BigLandingCategory> bigLandingCates = bigLandingService.getCategories(landingId, true);
        for (BigLandingCategory bigLandingCate : bigLandingCates) {
            List<BigLandingCategory> categoryChild = bigLandingService.getCategoriesChild(bigLandingCate.getId());
            List<BigLandingItem> bigLandingItems = bigLandingService.getFeaturedItemByCategories(bigLandingCate.getId());
            bigLandingCate.setBigLandingItem(bigLandingItems);
            bigLandingCate.setCategorySubs(categoryChild);
        }
        List<String> imgLogo = imageService.get(ImageType.LANDING_LOGO_BANNER, landingId);
        if (imgLogo != null && !imgLogo.isEmpty()) {
            landing.setLogoBanner(imageService.getUrl(imgLogo.get(0)).compress(100).getUrl(landing.getName()));
        }
        List<String> imgHeartBanner = imageService.get(ImageType.LANDING_HEART_BANNER, landingId);
        if (imgHeartBanner != null && !imgHeartBanner.isEmpty()) {
            landing.setHeartBanner(imageService.getUrl(imgHeartBanner.get(0)).compress(100).getUrl(landing.getName()));
        }
        List<String> imgCenterBanner = imageService.get(ImageType.LANDING_CENTER_BANNER, landingId);
        if (imgCenterBanner != null && !imgCenterBanner.isEmpty()) {
            landing.setCenterBanner(imageService.getUrl(imgCenterBanner.get(0)).compress(100).getUrl(landing.getName()));
        }

        //Lấy danh sách khuyến mãi ra trang chủ biglanding
//        List<BigLandingSeller> listBigLandingPromotion = bigLandingService.getListBigLandingPromotion(landingId, 1);
//        BigLandingSeller bigLandingSeller = new BigLandingSeller();
//        int blPromotionItemCount = 0;
//        List<Item> blPromotionItem = new ArrayList<>();
//        if (listBigLandingPromotion != null && !listBigLandingPromotion.isEmpty()) {
//            bigLandingSeller.setPromotionName(listBigLandingPromotion.get(0).getPromotionName());
//            bigLandingSeller.setPromotionId(listBigLandingPromotion.get(0).getPromotionId());
//            bigLandingSeller.setAlias(listBigLandingPromotion.get(0).getAlias());
//            blPromotionItemCount = listBigLandingPromotion.get(0).getItems().size();
//            blPromotionItem = listBigLandingPromotion.get(0).getItems();
//        }
        //model.put("listBigLandingPromotion", listBigLandingPromotion);
        //model.put("bigLandingSeller", bigLandingSeller);
        //model.put("blPromotionItemCount", blPromotionItemCount);
        //model.put("blPromotionItem", blPromotionItem);
        model.put("bigLandingCates", bigLandingCates);
        model.put("bigLanding", landing);
        model.put("remarketing", "dynx_itemid: \"\",dynx_pagetype: \"other\",dynx_totalvalue:\"\"");
        model.put("clientScript", "biglanding.init()");
        return "biglanding.index";
    }

}
