package vn.chodientu.controller.market;

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
import vn.chodientu.entity.db.LandingNew;
import vn.chodientu.entity.db.LandingNewItem;
import vn.chodientu.entity.db.LandingNewSlide;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.service.CategoryService;
import vn.chodientu.service.LandingNewService;
import vn.chodientu.service.LandingService;
import vn.chodientu.util.TextUtils;

/**
 * @since Jul 14, 2014
 * @author Account
 */
@Controller("landingNewController")
public class LandingNewController extends BaseMarket {

    @Autowired
    private LandingService landingService;
    @Autowired
    private LandingNewService landingNewService;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping({"/landingnew/{lid}/{name}.html"})
    public String browse(@PathVariable("lid") String lid, @RequestParam(value = "page", defaultValue = "0") int page,
            HttpServletResponse response, ModelMap model, HttpServletRequest request) throws Exception {

        LandingNew landingNew = landingNewService.getLandingNew(lid);
        if (landingNew == null) {
            return "market.404";
        }
        String uri = "/landingnew/" + lid + "/" + TextUtils.createAlias(landingNew.getName()) + ".html";
        if (!request.getRequestURI().equals(uri)) {
            String q = request.getQueryString();
            if (q != null && !q.equals("")) {
                q = "?" + q;
            } else {
                q = "";
            }
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.setHeader("Location", baseUrl + uri + q);
            return "market.landingnew";
        }
        if (page > 0) {
            page = page - 1;
        }
        List<LandingNewSlide> landingNewSlides = landingNewService.listLandingNewSlide(lid);
        DataPage<LandingNewItem> listLandingNewItems = landingNewService.listLandingNewItemPage(lid, new PageRequest(page, 50));
        model.put("canonical", "/landingnew/" + lid + "/" + TextUtils.createAlias(landingNew.getName()) + ".html");
        model.put("title", "Xem chương trình " + landingNew.getName() + " với ưu đãi đặc biệt");
        model.put("description", "Xem chương trình ưu đãi đặc biệt:" + landingNew.getName() + " tại chodientu.vn - Giá rẻ, nhiều khuyến mại, thanh toán online, CoD, giao hàng toàn quốc, bảo vệ người mua.");

        model.put("keywords", "giảm giá, ưu đãi, khuyến mại, hotdeal, " + landingNew.getName());
        model.put("landingNewCDT", landingNew);
        model.put("landingNewSlide", landingNewSlides);
        model.put("listLandingNewItem", listLandingNewItems);
        model.put("remarketing", "dynx_itemid: \"\",dynx_pagetype: \"other\",dynx_totalvalue:\"\"");
        model.put("clientScript", "landingnew.init()");
        return "market.landingnew";
    }

}
