package vn.chodientu.controller.market;

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
import vn.chodientu.entity.db.Category;
import vn.chodientu.entity.db.Landing;
import vn.chodientu.entity.db.LandingCategory;
import vn.chodientu.entity.db.LandingItem;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.service.CategoryService;
import vn.chodientu.service.LandingService;
import vn.chodientu.util.TextUtils;

/**
 * @since Jul 14, 2014
 * @author Account
 */
@Controller("landingController")
public class LandingController extends BaseMarket {

    @Autowired
    private LandingService landingService;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping({"/landing/{lid}/{name}.html"})
    public String browse(@PathVariable("lid") String lid,
            HttpServletResponse response, ModelMap model, HttpServletRequest request) {

        Landing landing = landingService.getLanding(lid);
        if (landing == null) {
            return "market.404";
        }
        String uri = "/landing/" + lid + "/" + TextUtils.createAlias(landing.getName()) + ".html";
        if (!request.getRequestURI().equals(uri)) {
            String q = request.getQueryString();
            if (q != null && !q.equals("")) {
                q = "?" + q;
            } else {
                q = "";
            }
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.setHeader("Location", baseUrl + uri + q);
            return "market.landing";
        }
        model.put("canonical", "/landing/" + lid + "/" + TextUtils.createAlias(landing.getName()) + ".html");
        model.put("title", "Xem chương trình " + landing.getName() + " với ưu đãi đặc biệt");
        model.put("description", "Xem chương trình ưu đãi đặc biệt:" + landing.getName() + " tại chodientu.vn - Giá rẻ, nhiều khuyến mại, thanh toán online, CoD, giao hàng toàn quốc, bảo vệ người mua.");

        List<LandingCategory> categories = landingService.getCategories(lid);
        List<String> cateIds = new ArrayList<>();
        String categoryPath = "";
        for (LandingCategory landingCategory : categories) {
            cateIds.add(landingCategory.getId());
            categoryPath = "/" + categoryPath + landingCategory.getName();
        }
        List<LandingItem> landingItems = landingService.getItemByCategories(cateIds);
        model.put("keywords", "giảm giá, ưu đãi, khuyến mại, hotdeal, " + landing.getName() + "/" + categoryPath);
        model.put("landing", landing);
        model.put("lcategories", categories);
        model.put("landingItems", landingItems);
        model.put("remarketing", "dynx_itemid: \"\",dynx_pagetype: \"other\",dynx_totalvalue:\"\"");
        model.put("clientScript","landing.init()");
        return "market.landing";
    }

    @RequestMapping({"/landing/category/{cid}/{name}.html"})
    public String browse(@PathVariable("cid") String cid, @RequestParam(value = "page", defaultValue = "0") int page,
            HttpServletResponse response, ModelMap model, HttpServletRequest request) {

        LandingCategory landingCategory = landingService.getCategory(cid);
        if (landingCategory == null) {
            return "market.404";
        }
        String uri = "/landing/category/" + cid + "/" + TextUtils.createAlias(landingCategory.getName()) + ".html";
        if (!request.getRequestURI().equals(uri)) {
            String q = request.getQueryString();
            if (q != null && !q.equals("")) {
                q = "?" + q;
            } else {
                q = "";
            }
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.setHeader("Location", baseUrl + uri + q);
            return "market.landing";
        }
        String pageS = "";
        if (page > 0) {
            pageS = "?page=" + page;
        } else {
            pageS = "";
        }
        List<Category> listCat = new ArrayList<>();
        String categoryPath = "";
        String currCate = "";
        try {
            Category tempCate = categoryService.get(cid);
            if (tempCate != null) {
                currCate = tempCate.getName();

                while (!"".equals(tempCate.getParentId()) && tempCate.getParentId() != null) {
                    listCat.add(tempCate);
                    tempCate = categoryService.get(tempCate.getParentId());
                }
            }

        } catch (Exception e) {
        }
        for (int i = 0; i < listCat.size(); i++) {
            Category tempCate = listCat.get(i);
            categoryPath += tempCate.getName() + "/";
        }
//        Cate
        model.put("canonical", "/landing/category/" + cid + "/" + TextUtils.createAlias(landingCategory.getName()) + ".html" + pageS);
        model.put("title", "Xem danh mục " + currCate + " tại chương trình " + landingCategory.getName() + " với ưu đãi đặc biệt");
        model.put("description", "Xem danh mục " + currCate + " tại chương trình ưu đãi đặc biệt:" + landingCategory.getName() + " tại chodientu.vn - Giá rẻ, nhiều khuyến mại, thanh toán online, CoD, giao hàng toàn quốc, bảo vệ người mua.");
        model.put("keywords", " giảm giá, ưu đãi, khuyến mại, hotdeal," + landingCategory.getName() + "/" + categoryPath);
        List<LandingCategory> categories = landingService.getCategories(landingCategory.getLandingId());
        if (page > 0) {
            page = page - 1;
        }
        DataPage<LandingItem> landingItems = landingService.getItemByCategory(cid, new PageRequest(page, 30), 0);
        Landing landing = landingService.getLanding(landingCategory.getLandingId());
        

        model.put("landing", landing);
        model.put("landingCategory", landingCategory);
        model.put("lcategories", categories);
        model.put("landingItems", landingItems);
        model.put("remarketing", "dynx_itemid: \"\",dynx_pagetype: \"other\",dynx_totalvalue:\"\"");
        model.put("clientScript","landing.init()");
        return "market.landing.category";
    }
}
