package vn.chodientu.controller.market;

import com.google.gson.Gson;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import vn.chodientu.component.imboclient.Url.ImageUrl;
import vn.chodientu.entity.db.AdvBanner;
import vn.chodientu.entity.db.Category;
import vn.chodientu.entity.db.City;
import vn.chodientu.entity.db.HotDealBox;
import vn.chodientu.entity.db.Item;
import vn.chodientu.entity.db.Manufacturer;
import vn.chodientu.entity.db.Model;
import vn.chodientu.entity.db.Shop;
import vn.chodientu.entity.db.User;
import vn.chodientu.entity.db.VipItem;
import vn.chodientu.entity.enu.AdvBannerType;
import vn.chodientu.entity.enu.ImageType;
import vn.chodientu.entity.input.AdvBannerSearch;
import vn.chodientu.entity.input.ItemSearch;
import vn.chodientu.entity.input.ManufacturerSearch;
import vn.chodientu.entity.input.ModelSearch;
import vn.chodientu.entity.input.PropertySearch;
import vn.chodientu.entity.input.VipItemSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.service.AdvBannerService;
import vn.chodientu.service.CategoryService;
import vn.chodientu.service.CityService;
import vn.chodientu.service.FooterKeywordService;
import vn.chodientu.service.HotDealBoxService;
import vn.chodientu.service.ImageService;
import vn.chodientu.service.ItemService;
import vn.chodientu.service.ManufacturerService;
import vn.chodientu.service.ModelService;
import vn.chodientu.service.SellerFollowService;
import vn.chodientu.service.SellerReviewService;
import vn.chodientu.service.ShopService;
import vn.chodientu.service.SuggestService;
import vn.chodientu.service.TagService;
import vn.chodientu.service.UserService;
import vn.chodientu.service.VipItemService;
import vn.chodientu.util.TextUtils;

/**
 * @since Jun 16, 2014
 * @author Phu
 */
@Controller("browseController")
public class BrowseController extends BaseMarket {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private VipItemService vipItemService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private UserService userService;
    @Autowired
    private CityService cityService;
    @Autowired
    private ManufacturerService manufacturerService;
    @Autowired
    private Gson gson;
    @Autowired
    private SellerReviewService sellerReviewService;
    @Autowired
    private HotDealBoxService hotDealBoxService;
    @Autowired
    private AdvBannerService advBannerService;
    @Autowired
    private SellerFollowService sellerFollowService;
    @Autowired
    private FooterKeywordService footerKeywordService;
    @Autowired
    private TagService tagService;
    @Autowired
    private SuggestService suggestService;

    @RequestMapping({"/mua-ban/{cid}/{name}.html"})
    public String browse(@PathVariable("cid") String cid, @RequestParam(value = "filter", required = false) String filter,
            @PathVariable("name") String name,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1", required = false) String pageStr,
            HttpServletResponse response,
            ModelMap model, HttpServletRequest request) throws UnsupportedEncodingException {
        int page = 1;
        if (!TextUtils.isLongNumber(pageStr)) {
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.setHeader("Location", baseUrl + "/mua-ban/" + cid + "/" + name + ".html");
            return "market.browse";
        } else {
            page = Integer.parseInt(pageStr);
        }
        if (request.getParameterValues("manu_id") != null || request.getParameterValues("sell_zone") != null
                || request.getParameterValues("seller") != null
                || request.getParameterValues("optionNBDB") != null
                || request.getParameterValues("portal") != null
                || request.getParameterValues("click_source") != null
                || request.getParameterValues("shipping") != null
                || request.getParameterValues("zone_id") != null
                || request.getParameterValues("condition") != null) {
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.setHeader("Location", baseUrl + "/mua-ban/" + cid + "/" + name + ".html");
            return "market.browse";
//            return "redirect:/mua-ban/" + cid + "/" + name + ".html";
        }
        if (filter != null && !filter.trim().equals("")) {
            try {
                String json = StringUtils.newStringUtf8(Base64.decodeBase64(filter));
                if (gson.fromJson(json, ItemSearch.class) == null) {
                    response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
                    response.setHeader("Location", baseUrl + "/mua-ban/" + cid + "/" + name + ".html");
                    return "market.browse";
                }
            } catch (Exception ex) {
                response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
                response.setHeader("Location", baseUrl + "/mua-ban/" + cid + "/" + name + ".html");
                return "market.browse";
            }
        }
        Category category = null;
        try {
            category = categoryService.get(cid);
        } catch (Exception ex) {
            try {
                return "redirect:/index.html";
            } catch (Exception e) {
                model.put("title", "Thông báo");
                model.put("message", ex.getMessage());
                model.put("clientScript", "textUtils.redirect('/index.html', 10000);");
                return "user.msg";
            }
        }
        String uri = "/mua-ban/" + cid + "/" + TextUtils.createAlias(category.getName()) + ".html";
        if (!request.getRequestURI().equals(uri)) {
            String q = request.getQueryString();
            if (q != null && !q.equals("")) {
                q = "?" + q;
            } else {
                q = "";
            }
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.setHeader("Location", baseUrl + uri + q);
            return "market.browse";
//            return "redirect:" + uri + q;
        }
        String pageS = "";
        String keywordS = "";
        if (keyword != null && !keyword.equals("")) {
            tagService.add(keyword);
            suggestService.create(keyword, cid);
            keywordS = "?keyword=" + keyword;
            if (page > 1) {
                pageS = "&page=" + page;
            }
        } else {
            if (page > 1) {
                pageS = "?page=" + page;
            }
        }
        model.put("canonical", "/mua-ban/" + cid + "/" + TextUtils.createAlias(category.getName()) + ".html" + keywordS + pageS);
        List<Category> listCat = new ArrayList<>();
        try {
            Category tempCate = categoryService.get(cid);
            while (!"".equals(tempCate.getParentId()) && tempCate.getParentId() != null) {
                listCat.add(tempCate);
                tempCate = categoryService.get(tempCate.getParentId());
            }

        } catch (Exception e) {
        }
        String categoryPath = "";
        for (int i = 0; i < listCat.size(); i++) {
            Category tempCate = listCat.get(i);
            categoryPath += tempCate.getName() + "/";
        }
        if (category.getContent() != null && !category.getContent().equals("")) {
            model.put("content", category.getContent());
        }

        String titleSEO = "";
        String descriptionSEO = "";
        if (page > 1) {
            if (category.getTitle() != null && !category.getTitle().equals("")) {
                titleSEO = category.getTitle();
            } else {
                titleSEO = category.getName() + " giá tốt từ các shop uy tín | ChoDienTu.vn - trang " + page;
                titleSEO = (keyword != null && !keyword.equals("") ? "Xem sản phẩm " + keyword + " tại danh mục " + category.getName() + " liên tục cập nhật, giá rẻ ,trang " + page + "" : "Xem sản phẩm " + category.getName() + " liên tục cập nhật, giá rẻ, trang " + page + "");
            }
            if (category.getDescription() != null && !category.getDescription().equals("")) {
                descriptionSEO = category.getDescription();
            } else {
                descriptionSEO = category.getName() + " chất lượng, giá tốt, giao hàng nhanh toàn quốc tại ChoDienTu.vn - Trang " + page + "";
            }
            model.put("title", titleSEO);
            model.put("description", descriptionSEO);
        } else {
            if (category.getTitle() != null && !category.getTitle().equals("")) {
                titleSEO = category.getTitle();
            } else {
                titleSEO = category.getName() + " giá tốt từ các shop uy tín | ChoDienTu.vn";
            }
            if (category.getDescription() != null && !category.getDescription().equals("")) {
                descriptionSEO = category.getDescription();
            } else {
                descriptionSEO = (keyword != null && !keyword.equals("") ? "Tìm kiếm sản phẩm " + keyword + ", " : "") + "Mua bán sản phẩm " + category.getName() + " chất lượng, giá tốt, giao hàng nhanh toàn quốc tại ChoDienTu.vn";
            }
            model.put("title", titleSEO);
            model.put("description", descriptionSEO);
            model.put("ogImage", staticUrl + "/market/images/logo.png");
        }

        model.put("keywords", categoryPath);
        ItemSearch itemSearch = new ItemSearch();
        List<Shop> shops;
        List<String> shopsId = new ArrayList<>();
        if (filter != null && !filter.trim().equals("")) {
            String json = StringUtils.newStringUtf8(Base64.decodeBase64(filter));
            itemSearch = gson.fromJson(json, ItemSearch.class);
        }
        itemSearch.setCategoryIds(new ArrayList<String>());
        itemSearch.getCategoryIds().add(cid);
        DataPage<VipItem> vipPage = new DataPage<>();
        List<Item> items = new ArrayList<>();
        if (page > 1) {
            itemSearch.setPageIndex(page - 1);
        } else {
            itemSearch.setPageIndex(0);
        }

        try {
            VipItemSearch vipSearch = new VipItemSearch();
            vipSearch.setCategoryId(category.getId());
            vipSearch.setActive(1);
            vipSearch.setPageIndex(0);
            vipSearch.setPageSize(4);
            vipPage = vipItemService.search(vipSearch, true);
            List<String> vipIds = new ArrayList<>();
            for (VipItem i : vipPage.getData()) {
                if (!vipIds.contains(i)) {
                    vipIds.add(i.getItemId());
                }
            }

            items = itemService.list(vipIds);
            if (items != null && !items.isEmpty()) {
                for (int i = 0; i < items.size(); i++) {
                    if (!items.get(i).isActive() || !items.get(i).isApproved() || !items.get(i).isCompleted()) {
                        items.remove(i);
                        i--;
                    }
                }
                for (Item item : items) {
                    List<String> images = new ArrayList<>();
                    if (item.getImages() != null && !item.getImages().isEmpty()) {
                        for (String img : item.getImages()) {
                            ImageUrl url = imageService.getUrl(img).thumbnail(213, 213, "inset").compress(100);
                            if (url != null && url.getUrl() != null) {
                                images.add(url.getUrl());
                            }
                        }
                    }
                    item.setImages(images);
                    if (!shopsId.contains(item.getSellerId())) {
                        shopsId.add(item.getSellerId());
                    }
                }
            }
        } catch (Exception e) {
        }

        model.put("vips", items);
        model.put("vipPages", vipPage);
        itemSearch.setCategoryIds(new ArrayList<String>());
        itemSearch.getCategoryIds().add(cid);
        if (keyword != null && !keyword.trim().equals("")) {
            itemSearch.setKeyword(TextUtils.removeSpecialCharacters(keyword));
        }

        itemSearch.setStatus(1);

        itemSearch.setPageSize(60);
        DataPage<Item> itemPage = itemService.search(itemSearch);
        if ((itemPage.getData() == null || itemPage.getData().isEmpty()) && page > 1) {
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.setHeader("Location", baseUrl + "/mua-ban/" + cid + "/" + name + ".html");
            return "market.browse";
        }
        for (Item item : itemPage.getData()) {
            if (item.getSellerId() != null && !"".equals(item.getSellerId()) && !shopsId.contains(item.getSellerId())) {
                shopsId.add(item.getSellerId());
            }
//            // kiem tra shop bi khoa hay khong
//            if (shopService.getByAlias(item.getShopName()) == null) {
//                itemPage.getData().remove(item);
//            }
        }
        shops = shopService.getShops(shopsId);

        for (Item item : itemPage.getData()) {
            List<String> images = new ArrayList<>();
            if (item.getImages() != null && !item.getImages().isEmpty()) {
                for (String img : item.getImages()) {
                    ImageUrl url = imageService.getUrl(img).thumbnail(213, 213, "inset").compress(100);
                    if (url != null) {
                        images.add(url.getUrl(item.getName()));
                    }
                }
            }
            item.setImages(images);
        }
        List<Category> listCategories = new ArrayList<>();
        if (category.isLeaf()) {
            listCategories = categoryService.getChilds(category.getParentId());
        } else {
            listCategories = categoryService.getChilds(category.getId());
        }
        List<Category> listParentCategories = categoryService.getCategories(category.getPath());
        String cateCheckId = category.getPath().get(0);
        if (cateCheckId != null) {
            if (cateCheckId.equals("2924")) {
                String script = "";
                script += "<script type=\"text/javascript\" src=\"//static.criteo.net/js/ld/ld.js\" async=\"true\"></script>";
                script += "<script type=\"text/javascript\">";
                script += "window.criteo_q = window.criteo_q || [];";
                script += "window.criteo_q.push(";
                script += "{ event: \"setAccount\", account: 17454 },";
                script += "{ event: \"setHashedEmail\", email: \"e1651950b85ec9e71e54ce7388ad5584\" },";
                script += "{ event: \"setSiteType\", type: \"d\" },";
                script += "{ event: \"viewList\", item: [\"275191880866\", \"90759982754\", \"354171114185\"] }";
                script += ");";
                script += "</script>";
                model.put("criteo", script);
            }
        }
        if (itemSearch.getManufacturerIds() == null) {
            itemSearch.setManufacturerIds(new ArrayList<String>());
        }
        if (itemSearch.getModelIds() == null) {
            itemSearch.setModelIds(new ArrayList<String>());
        }
        if (itemSearch.getCityIds() == null) {
            itemSearch.setCityIds(new ArrayList<String>());
        }
        if (itemSearch.getProperties() == null) {
            itemSearch.setProperties(new ArrayList<PropertySearch>());
        }
        User user = new User();
        if (itemSearch.getSellerId() != null && !itemSearch.getSellerId().equals("")) {
            try {
                user = userService.get(itemSearch.getSellerId());
                if (user.getAvatar() != null && !user.getAvatar().equals("")) {
                    ImageUrl url = imageService.getUrl(user.getAvatar()).compress(100);
                    if (url != null) {
                        user.setAvatar(url.getUrl("avatar"));
                    }
                }

            } catch (Exception ex) {
            }
        }
        model.put("seller", user);
        model.put("sellerReview", sellerReviewService.report(user.getId()));
        model.put("sellerFollowCount", sellerFollowService.countByItem(user.getId()));
        AdvBannerSearch bannerSearch = new AdvBannerSearch();
        bannerSearch.setActive(1);
        bannerSearch.setPageSize(1);
        bannerSearch.setPageIndex(0);
        bannerSearch.setPosition(AdvBannerType.BROWSE_TOP);
        DataPage<AdvBanner> search = advBannerService.search(bannerSearch);
        if (search != null && search.getData() != null && search.getData().size() > 0) {
            model.put("topBanner", search.getData().get(0));
        } else {
            model.put("topBanner", null);
        }
        model.put("contentBanner", null);
        for (Category cate : listParentCategories) {
            if (cate.getLevel() == 1) {
                bannerSearch.setCategoryId(cate.getId());
                bannerSearch.setPosition(AdvBannerType.BROWSE_CONTENT);
                DataPage<AdvBanner> content = advBannerService.search(bannerSearch);
                if (content != null && content.getData() != null && content.getData().size() > 0) {
                    model.put("contentBanner", content.getData().get(0));
                }
                break;
            }
        }
        if (itemPage.getDataCount() <= 0 || itemPage.getData() == null || itemPage.getData().isEmpty()) {
            List<HotDealBox> hotDealBox = hotDealBoxService.list();
            List<String> itemIds = new ArrayList<>();
            for (HotDealBox box : hotDealBox) {
                itemIds.add(box.getItemId());
            }
            List<Item> list = new ArrayList<>();
            if (itemIds != null && itemIds.size() > 0) {
                list = itemService.list(itemIds);
            }
            for (Item item : list) {
                HotDealBox byId = hotDealBoxService.getById(item.getId());
                if (byId != null) {
                    item.setName(byId.getTitle());
                }
                List<String> get = imageService.get(ImageType.HOT_DEAL_BOX, item.getId());
                if (get != null && get.size() > 0) {
                    List<String> images = new ArrayList<>();
                    for (String img : get) {
                        String url = imageService.getUrl(img).compress(100).getUrl(item.getName());
                        images.add(url);
                    }
                    item.setImages(images);
                } else {
                    item.setImages(null);
                }
            }
            model.put("hotDealBox", list);
        }
        ManufacturerSearch manufacturerSearch = new ManufacturerSearch();
        manufacturerSearch.setPageIndex(0);
        manufacturerSearch.setPageSize(24);
        manufacturerSearch.setCategoryId(cid);
        DataPage<Manufacturer> munufData = manufacturerService.search(manufacturerSearch);
        model.put("munufData", munufData);

        List<City> cities = cityService.list();
        model.put("parentCategories", listParentCategories);
        model.put("listCategories", listCategories);
        model.put("category", category);
        model.put("itemPage", itemPage);
        model.put("cities", cities);
        model.put("shops", shops);
        model.put("ref", "browse");
        model.put("itemSearch", itemSearch);
        model.put("clientScript", "var vipPageCount = " + (vipPage.getDataCount() == 0 ? 0 : vipPage.getPageCount()) + ";var cities = " + gson.toJson(cities) + ";browse.init({category:" + gson.toJson(category) + ",itemSearch:" + gson.toJson(itemSearch) + ",filter:1});");
        try {
            /**
             * thanhvv put cho google adv
             */
            model.put("adPage", "cat" + category.getPath().get(0));
        } catch (Exception e) {
        }
        model.put("remarketing", "dynx_itemid: \"\",dynx_pagetype: \"other\",dynx_totalvalue:\"\"");
        return "market.browse";
    }

    @RequestMapping({"/mua-ban/model/{cid}/{name}.html"})
    public String modelBrowse(@PathVariable("cid") String cid, @RequestParam(value = "filter", required = false) String filter,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            HttpServletResponse response,
            ModelMap modelMap, HttpServletRequest request
    ) {

        Category category;
        try {
            category = categoryService.get(cid);
        } catch (Exception ex) {
            modelMap.put("title", "Thông báo");
            modelMap.put("message", ex.getMessage());
            modelMap.put("clientScript", "textUtils.redirect('', 10000);");
            return "user.msg";
        }
        String categoryPath = "";
        List<Category> listCat = new ArrayList<>();
        try {
            Category tempCate = categoryService.get(cid);
            if (tempCate != null) {
                categoryPath += tempCate.getName();

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
        modelMap.put("keywords", categoryPath);
        String uri = "/mua-ban/model/" + cid + "/" + TextUtils.createAlias(category.getName()) + ".html";
        if (!request.getRequestURI().equals(uri)) {
            String q = request.getQueryString();
            if (q != null && !q.equals("")) {
                q = "?" + q;
            } else {
                q = "";
            }
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.setHeader("Location", baseUrl + uri + q);
            return "market.browse";
        }
        String pageS = "";
        String keywordS = "";
        if (keyword != null && !keyword.equals("")) {
            tagService.add(keyword);
            suggestService.create(keyword, cid);
            keywordS = "?keyword=" + keyword;
            if (page > 1) {
                pageS = "&page=" + page;
            }
        } else {
            if (page > 1) {
                pageS = "?page=" + page;
            }
        }
        modelMap.put("canonical", "/mua-ban/model/" + cid + "/" + TextUtils.createAlias(category.getName()) + ".html" + keywordS + pageS);
        if (keyword == null || keyword.equals("")) {
            modelMap.put("title", "Xem catalog hàng tại " + category.getName() + " liên tục cập nhật, giá rẻ" + (page > 1 ? ",trang" + page + "" : "."));
            modelMap.put("description", "Mua bán thông tin sản phẩm " + category.getName() + "  tại chodientu.vn liên tục cập nhật - Giá rẻ, nhiều khuyến mại, thanh toán online, CoD, giao hàng toàn quốc, bảo vệ người mua." + (page > 1 ? " | Trang " + page + "" : ""));
        } else {
            modelMap.put("title", "Xem,tìm kiếm catalog hàng tại " + category.getName() + " liên tục cập nhật, giá rẻ" + (page > 1 ? ",trang" + page + "" : "."));
            modelMap.put("description", "Tìm kiếm sản phẩm " + category.getName() + "  tại chodientu.vn -Giá rẻ, nhiều khuyến mại, thanh toán online, CoD, giao hàng toàn quốc, bảo vệ người mua." + (page > 1 ? " | Trang " + page + "" : ""));
        }
        modelMap.put("refresh", "true");
        ModelSearch modelSearch = new ModelSearch();
        List<String> shopsId = new ArrayList<>();
        if (filter != null && !filter.trim().equals("")) {
            String json = StringUtils.newStringUtf8(Base64.decodeBase64(filter));
            modelSearch = gson.fromJson(json, ModelSearch.class);
        }
        modelSearch.setCategoryId(cid);
        DataPage<VipItem> vipPage = new DataPage<>();
        List<Item> items = new ArrayList<>();
        if (page > 1) {
            modelSearch.setPageIndex(page - 1);
        } else {
            modelSearch.setPageIndex(0);
        }
        try {
            VipItemSearch vipSearch = new VipItemSearch();
            vipSearch.setCategoryId(category.getId());
            vipSearch.setActive(1);
            vipSearch.setPageIndex(0);
            vipSearch.setPageSize(4);
            vipPage = vipItemService.search(vipSearch, true);
            List<String> vipIds = new ArrayList<>();
            for (VipItem i : vipPage.getData()) {
                if (!vipIds.contains(i.getItemId())) {
                    vipIds.add(i.getItemId());
                }
            }

            items = itemService.list(vipIds);
            if (items != null && !items.isEmpty()) {
                for (int i = 0; i < items.size(); i++) {
                    if (!items.get(i).isActive() || !items.get(i).isApproved() || !items.get(i).isCompleted()) {
                        items.remove(i);
                        i--;
                    }
                }
                for (Item item : items) {
                    List<String> images = new ArrayList<>();
                    if (item.getImages() != null && !item.getImages().isEmpty()) {
                        for (String img : item.getImages()) {
                            ImageUrl url = imageService.getUrl(img).thumbnail(213, 213, "inset").compress(100);
                            if (url != null && url.getUrl() != null) {
                                images.add(url.getUrl(item.getName()));
                            }
                        }
                    }
                    item.setImages(images);
                    if (!shopsId.contains(item.getSellerId())) {
                        shopsId.add(item.getSellerId());
                    }
                }
            }

        } catch (Exception e) {
        }
        modelMap.put("vips", items);
        modelMap.put("vipPages", vipPage);
        modelSearch.setCategoryId(cid);
        if (keyword != null && !keyword.trim().equals("")) {
            modelSearch.setKeyword(TextUtils.removeSpecialCharacters(keyword));
        }

        modelSearch.setStatus(1);

        modelSearch.setPageSize(60);

        List<String> cateIds = new ArrayList<String>();
        DataPage<Model> modelPage = modelService.search(modelSearch);

        for (Model model : modelPage.getData()) {
            List<String> images = new ArrayList<>();
            if (model.getImages() != null && !model.getImages().isEmpty()) {
                for (String img : model.getImages()) {
                    ImageUrl url = imageService.getUrl(img).thumbnail(213, 213, "inset").compress(100);
                    if (url != null && url.getUrl() != null) {
                        images.add(url.getUrl(model.getName()));
                    }
                }
            }
            model.setImages(images);
        }

        for (Model model : modelPage.getData()) {
            if (model.getCategoryId() != null && !model.getCategoryId().equals("") && !cateIds.contains(model.getCategoryId())) {
                cateIds.add(model.getCategoryId());
            }
            model.setProperties(modelService.getProperties(model.getId()));
        }

        List<Category> listCategories = new ArrayList<>();
        if (category.isLeaf()) {
            listCategories = categoryService.getChilds(category.getParentId());
        } else {
            listCategories = categoryService.getChilds(category.getId());
        }
        List<Category> listParentCategories = categoryService.getCategories(category.getPath());

        if (modelSearch.getManufacturerIds() == null) {
            modelSearch.setManufacturerIds(new ArrayList<String>());
        }
        if (modelSearch.getProperties() == null) {
            modelSearch.setProperties(new ArrayList<PropertySearch>());
        }

        AdvBannerSearch bannerSearch = new AdvBannerSearch();
        bannerSearch.setActive(1);
        bannerSearch.setPageSize(1);
        bannerSearch.setPageIndex(0);
        bannerSearch.setPosition(AdvBannerType.BROWSE_TOP);
        DataPage<AdvBanner> search = advBannerService.search(bannerSearch);
        if (search != null && search.getData() != null && search.getData().size() > 0) {
            modelMap.put("topBanner", search.getData().get(0));
        } else {
            modelMap.put("topBanner", null);
        }
        modelMap.put("contentBanner", null);
        for (Category cate : listParentCategories) {
            if (cate.getLevel() == 1) {
                bannerSearch.setCategoryId(cate.getId());
                bannerSearch.setPosition(AdvBannerType.BROWSE_CONTENT);
                DataPage<AdvBanner> content = advBannerService.search(bannerSearch);
                if (content != null && content.getData() != null && content.getData().size() > 0) {
                    modelMap.put("contentBanner", content.getData().get(0));
                }
                break;
            }
        }

        ManufacturerSearch manufacturerSearch = new ManufacturerSearch();
        manufacturerSearch.setPageIndex(0);
        manufacturerSearch.setPageSize(24);
        manufacturerSearch.setCategoryId(cid);
        DataPage<Manufacturer> munufData = manufacturerService.search(manufacturerSearch);
        modelMap.put("munufData", munufData);

        ItemSearch itSearch = new ItemSearch();
        itSearch.setManufacturerIds(new ArrayList<String>());
        itSearch.setModelIds(new ArrayList<String>());
        itSearch.setCityIds(new ArrayList<String>());
        itSearch.setProperties(new ArrayList<PropertySearch>());

        List<City> cities = cityService.list();
        modelMap.put("parentCategories", listParentCategories);
        modelMap.put("categoryProperties", categoryService.getProperties(cateIds));
        modelMap.put("categoryPropertyValues", categoryService.getPropertyValues(cateIds));
        modelMap.put("listCategories", listCategories);
        modelMap.put("category", category);
        modelMap.put("keyword", keyword);
        modelMap.put("modelPage", modelPage);
        modelMap.put("cities", cities);
        modelMap.put("shops", shopService.getShops(shopsId));
        modelMap.put("modelSearch", modelSearch);
        modelMap.put("clientScript", "var vipPageCount = " + vipPage.getPageCount() + ";var itemSearch=" + gson.toJson(itSearch) + ";var cities = " + gson.toJson(cities) + ";browse.initModel({category:" + gson.toJson(category) + ",modelSearch:" + gson.toJson(modelSearch) + "}); browse.initCompare();");
        try {
            /**
             * thanhvv put cho google adv
             */
            modelMap.put("adPage", "cat" + category.getPath().get(0));
        } catch (Exception e) {
        }
        String cateCheckId = category.getPath().get(0);
        if (cateCheckId != null) {
            if (cateCheckId.equals("2924")) {
                String script = "";
                script += "<script type=\"text/javascript\" src=\"//static.criteo.net/js/ld/ld.js\" async=\"true\"></script>";
                script += "<script type=\"text/javascript\">";
                script += "window.criteo_q = window.criteo_q || [];";
                script += "window.criteo_q.push(";
                script += "{ event: \"setAccount\", account: 17454 },";
                script += "{ event: \"setHashedEmail\", email: \"e1651950b85ec9e71e54ce7388ad5584\" },";
                script += "{ event: \"setSiteType\", type: \"d\" },";
                script += "{ event: \"viewList\", item: [\"275191880866\", \"90759982754\", \"354171114185\"] }";
                script += ");";
                script += "</script>";
                modelMap.put("criteo", script);
            }
            modelMap.put("remarketing", "dynx_itemid: \"\",dynx_pagetype: \"other\",dynx_totalvalue:\"\"");
        }
        return "market.browse.model";
    }

    @RequestMapping({"/tim-kiem.html"})
    public String search(@RequestParam(value = "filter", required = false) String filter,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            HttpServletResponse response,
            ModelMap model
    ) {
        String pageS = "";
        String keywordS = "";
        if (keyword != null && !keyword.equals("")) {
            keywordS = "?keyword=" + keyword;
            if (page > 1) {
                pageS = "&page=" + page;
            }
        } else {
            if (page > 1) {
                pageS = "?page=" + page;
            }
        }
        model.put("canonical", "/tim-kiem.html" + keywordS + pageS);
        model.put("ref", "search");
        model.put("keywordcdt", keyword);
        return search(keyword, model, filter, page, true, response);
    }

    @RequestMapping({"/s/{kw}.html"})
    public String searchKey(@PathVariable(value = "kw") String kw,
            @RequestParam(value = "filter", required = false) String filter,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            HttpServletResponse response,
            ModelMap model, HttpServletRequest request
    ) {
        if (!kw.equals(kw.toLowerCase())) {
            kw = kw.toLowerCase();
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.setHeader("Location", baseUrl + "/s/" + kw + ".html");
//            return baseUrl + "/s/" + kw + ".html";
            return "market.browse";
        }
        if (kw == null || kw.equals("")) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setHeader("Location", baseUrl + "/404.html");
            return "market.404";
        }
        kw = kw.toLowerCase();
        String k = "";
        if (kw != null && !kw.trim().equals("")) {
            k = ((keyword == null || keyword.equals("")) ? "" : (keyword + " ")) + kw;
        }

        String pageS = "";
        if (page > 1) {
            pageS = "?page=" + page;
        } else {
            pageS = "";
        }
        model.put("canonical", "/s/" + kw + ".html" + pageS);
        model.put("ref", "search");
        model.put("keywordcdt", kw);
        model.put("remarketing", "search");

        return search(k, model, filter, page, false, response);
    }

    private String search(String keyword, ModelMap model, String filter, int page, boolean check, HttpServletResponse response) {
        if (keyword == null || keyword.equals("")) {
            model.put("title", "Xem hàng hóa liên tục cập nhật, giá rẻ");
            model.put("description", "Mua bán sản phẩm hàng hóa tại chodientu.vn - Giá rẻ, nhiều khuyến mại, thanh toán online, CoD, giao hàng toàn quốc, bảo vệ người mua.");
        } else {
            model.put("title", "" + keyword.replaceAll("\\+", " ") + " - Sản phẩm " + keyword.replaceAll("\\+", " ") + ", thông tin, nơi bán " + keyword.replaceAll("\\+", " ") + " | ChoDienTu.vn" + (page > 1 ? ",trang" + page + "" : ""));
            model.put("description", "" + keyword.replaceAll("\\+", " ") + " - Sản phẩm " + keyword.replaceAll("\\+", " ") + ", thông tin, nơi bán " + keyword.replaceAll("\\+", " ") + " tại ChoDienTu.vn" + (page > 1 ? ",trang" + page + "" : ""));
            model.put("keywords", keyword + "giá rẻ" + (page > 1 ? "| Trang" + page + "" : ""));
        }

        ItemSearch itemSearch = new ItemSearch();
        List<Shop> shops;
        List<String> shopsId = new ArrayList<>();
        if (filter != null && !filter.trim().equals("")) {
            String json = StringUtils.newStringUtf8(Base64.decodeBase64(filter));
            itemSearch = gson.fromJson(json, ItemSearch.class);
        }
        if (keyword != null && !keyword.trim().equals("")) {
            //keyword search
            tagService.add(keyword);
            suggestService.create(keyword, null);
            itemSearch.setKeyword(TextUtils.removeSpecialCharactersSearch(keyword));

        }

        itemSearch.setStatus(1);

        itemSearch.setPageSize(60);
        if (page > 1) {
            itemSearch.setPageIndex(page - 1);
        } else {
            itemSearch.setPageIndex(0);
        }
        DataPage<Item> itemPage = itemService.search(itemSearch);

        for (Item item : itemPage.getData()) {
            if (item.getSellerId() != null && !"".equals(item.getSellerId()) && !shopsId.contains(item.getSellerId())) {
                shopsId.add(item.getSellerId());
            }
        }
        shops = shopService.getShops(shopsId);

        for (Item item : itemPage.getData()) {
            List<String> images = new ArrayList<>();
            if (item.getImages() != null && !item.getImages().isEmpty()) {
                for (String img : item.getImages()) {
                    ImageUrl url = imageService.getUrl(img).thumbnail(213, 213, "inset").compress(100);
                    if (url != null && url.getUrl() != null) {
                        images.add(url.getUrl(item.getName()));
                    }
                }
            }
            item.setImages(images);
        }

        List<Category> listCategories = categoryService.getChilds(null);

        if (itemSearch.getManufacturerIds() == null) {
            itemSearch.setManufacturerIds(new ArrayList<String>());
        }
        if (itemSearch.getModelIds() == null) {
            itemSearch.setModelIds(new ArrayList<String>());
        }
        if (itemSearch.getCityIds() == null) {
            itemSearch.setCityIds(new ArrayList<String>());
        }
        if (itemSearch.getProperties() == null) {
            itemSearch.setProperties(new ArrayList<PropertySearch>());
        }
        User user = new User();
        if (itemSearch.getSellerId() != null && !itemSearch.getSellerId().equals("")) {
            try {
                user = userService.get(itemSearch.getSellerId());
                if (user.getAvatar() != null && !user.getAvatar().equals("")) {
                    ImageUrl url = imageService.getUrl(user.getAvatar()).compress(100);
                    if (url != null) {
                        user.setAvatar(url.getUrl("avatar"));
                    }
                }

            } catch (Exception ex) {
            }
        }

        if (itemPage.getDataCount() <= 0 || itemPage.getData() == null || itemPage.getData().isEmpty()) {
            List<HotDealBox> hotDealBox = hotDealBoxService.list();
            List<String> itemIds = new ArrayList<>();
            for (HotDealBox box : hotDealBox) {
                itemIds.add(box.getItemId());
            }
            List<Item> list = new ArrayList<>();
            if (itemIds != null && itemIds.size() > 0) {
                list = itemService.list(itemIds);
            }
            for (Item item : list) {
                HotDealBox byId = hotDealBoxService.getById(item.getId());
                if (byId != null) {
                    item.setName(byId.getTitle());
                }
                List<String> get = imageService.get(ImageType.HOT_DEAL_BOX, item.getId());
                if (get != null && get.size() > 0) {
                    List<String> images = new ArrayList<>();
                    for (String img : get) {
                        String url = imageService.getUrl(img).compress(100).getUrl(item.getName());
                        images.add(url);
                    }
                    item.setImages(images);
                } else {
                    item.setImages(null);
                }
            }
            model.put("hotDealBox", list);
        }
        if (itemPage.getDataCount() <= 0 && 1 == 2) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setHeader("Location", baseUrl + "/404.html");
            return "market.404";
        }

        AdvBannerSearch bannerSearch = new AdvBannerSearch();
        bannerSearch.setActive(1);
        bannerSearch.setPageSize(1);
        bannerSearch.setPageIndex(0);
        bannerSearch.setPosition(AdvBannerType.BROWSE_TOP);
        DataPage<AdvBanner> search = advBannerService.search(bannerSearch);
        if (search != null && search.getData() != null && search.getData().size() > 0) {
            model.put("topBanner", search.getData().get(0));
        } else {
            model.put("topBanner", null);
        }
        try {
            double totalPoint = sellerReviewService.calReputationPoints(user.getId());
            model.put("totalPoint", Math.round(totalPoint));
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.put("seller", user);
        model.put("sellerReview", sellerReviewService.report(user.getId()));
        model.put("sellerFollowCount", sellerFollowService.countByItem(user.getId()));
        List<City> cities = cityService.list();
        model.put("listCategories", listCategories);
        model.put("itemPage", itemPage);
        model.put("cities", cities);
        model.put("shops", shops);
//        SuggestSearch suggestSearch = new SuggestSearch();
//        if (keyword != null && !keyword.equals("")) {
//            suggestSearch.setKeyword(keyword);
//        }
//        model.put("keywordSuggest", suggestService.suggests(suggestSearch).getData());
        model.put("itemSearch", itemSearch);
        model.put("clientScript", "var cities = " + gson.toJson(cities) + ";browse.init({category:" + gson.toJson(new Category()) + ",itemSearch:" + gson.toJson(itemSearch) + ",filter:" + (((filter == null || filter.equals("")) && check) ? 0 : 1) + "});");
        model.put("remarketing", "dynx_itemid: \"\",dynx_pagetype: \"searchresults\",dynx_totalvalue:\"\"");
        return "market.browse.search";
    }

    @RequestMapping({"tim-kiem-model"})
    public String searchModel(@RequestParam(value = "filter", required = false) String filter,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            HttpServletResponse response,
            ModelMap model, HttpServletRequest request) {
        String pageS = "";
        if (page > 1) {
            pageS = "?page=" + page;
        } else {
            pageS = "";
        }
        String uri = "/tim-kiem-model.html";
        if (!request.getRequestURI().equals(uri)) {
            String q = request.getQueryString();
            if (q != null && !q.equals("")) {
                q = "?" + q;
            } else {
                q = "";
            }
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.setHeader("Location", baseUrl + uri + q);
            return "market.browse";
        }
        model.put("canonical", "/tim-kiem-model.html?keyword=" + keyword + pageS);
        ModelSearch modelSearch = new ModelSearch();
        if (filter != null && !filter.trim().equals("")) {
            String json = StringUtils.newStringUtf8(Base64.decodeBase64(filter));
            modelSearch = gson.fromJson(json, ModelSearch.class);
        }
        if (keyword != null && !keyword.trim().equals("")) {
            modelSearch.setKeyword(TextUtils.removeSpecialCharacters(keyword));
        }

        modelSearch.setStatus(1);

        modelSearch.setPageSize(60);
        if (page > 1) {
            modelSearch.setPageIndex(page - 1);
        } else {
            modelSearch.setPageIndex(0);
        }
        DataPage<Model> modelPage = modelService.search(modelSearch);

        for (Model item : modelPage.getData()) {
            List<String> images = new ArrayList<>();
            if (item.getImages() != null && !item.getImages().isEmpty()) {
                for (String img : item.getImages()) {
                    ImageUrl url = imageService.getUrl(img).thumbnail(213, 213, "inset").compress(100);
                    if (url != null && url.getUrl() != null) {
                        images.add(url.getUrl(item.getName()));
                    }
                }
            }
            item.setImages(images);
        }
        List<String> cateIds = new ArrayList<>();
        for (Model m : modelPage.getData()) {
            if (m.getCategoryId() != null && !m.getCategoryId().equals("") && !cateIds.contains(m.getCategoryId())) {
                cateIds.add(m.getCategoryId());
            }
            m.setProperties(modelService.getProperties(m.getId()));
        }
        List<Category> listCategories = categoryService.getChilds(null);

        if (modelSearch.getManufacturerIds() == null) {
            modelSearch.setManufacturerIds(new ArrayList<String>());
        }

        if (modelSearch.getProperties() == null) {
            modelSearch.setProperties(new ArrayList<PropertySearch>());
        }

        AdvBannerSearch bannerSearch = new AdvBannerSearch();
        bannerSearch.setActive(1);
        bannerSearch.setPageSize(1);
        bannerSearch.setPageIndex(0);
        bannerSearch.setPosition(AdvBannerType.BROWSE_TOP);
        DataPage<AdvBanner> search = advBannerService.search(bannerSearch);
        if (search != null && search.getData() != null && search.getData().size() > 0) {
            model.put("topBanner", search.getData().get(0));
        } else {
            model.put("topBanner", null);
        }

        ItemSearch itSearch = new ItemSearch();
        itSearch.setManufacturerIds(new ArrayList<String>());
        itSearch.setModelIds(new ArrayList<String>());
        itSearch.setCityIds(new ArrayList<String>());
        itSearch.setProperties(new ArrayList<PropertySearch>());

        List<City> cities = cityService.list();
        model.put("listCategories", listCategories);
        model.put("categoryProperties", categoryService.getProperties(cateIds));
        model.put("categoryPropertyValues", categoryService.getPropertyValues(cateIds));
        model.put("modelPage", modelPage);
        model.put("modelSearch", modelSearch);
        model.put("clientScript", "var cities = " + gson.toJson(cities) + ";var itemSearch=" + gson.toJson(itSearch) + ";browse.initModel({category:" + gson.toJson(new Category()) + ",modelSearch:" + gson.toJson(modelSearch) + "}); browse.initCompare();");
        model.put("remarketing", "dynx_itemid: \"\",dynx_pagetype: \"searchresults\",dynx_totalvalue:\"\"");
        return "market.browse.modelsearch";
    }

    @RequestMapping({"/danh-muc-san-pham"})
    public String categories(ModelMap model) {
        model.put("title", "Danh mục sản phẩm");
        model.put("canonical", "/danh-muc-san-pham.html");
        List<Category> cates = categoryService.getByLevelDisplay(1);
        List<Category> cates2 = categoryService.getByLevelDisplay(2);
        List<Category> cates3 = categoryService.getByLevelDisplay(3);
        for (Category c2 : cates2) {
            List<Category> cate = new ArrayList<>();
            for (Category c3 : cates3) {
                if (c2.getId().equals(c3.getParentId())) {
                    cate.add(c3);
                }
            }
            c2.setCategoris(cate);
        }

        for (Category c2 : cates) {
            List<Category> cate = new ArrayList<>();
            for (Category c3 : cates2) {
                if (c2.getId().equals(c3.getParentId())) {
                    cate.add(c3);
                }
            }
            c2.setCategoris(cate);
        }
        model.put("remarketing", "dynx_itemid: \"\",dynx_pagetype: \"other\",dynx_totalvalue:\"\"");
        model.put("categories", cates);
        model.put("clientScript", "browse.initCategories();");
        return "market.categories";
    }

}
