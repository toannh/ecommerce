package vn.chodientu.controller.shop;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpServerErrorException;
import vn.chodientu.entity.db.Category;
import vn.chodientu.entity.db.Item;
import vn.chodientu.entity.db.Promotion;
import vn.chodientu.entity.db.Seller;
import vn.chodientu.entity.db.Shop;
import vn.chodientu.entity.db.ShopCategory;
import vn.chodientu.entity.enu.ImageType;
import vn.chodientu.entity.input.ItemSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.service.CategoryService;
import vn.chodientu.service.ImageService;
import vn.chodientu.service.ItemService;
import vn.chodientu.service.PromotionService;
import vn.chodientu.service.SellerService;
import vn.chodientu.service.ShopCategoryService;
import vn.chodientu.service.ShopService;

/**
 * @since Jun 2, 2014
 * @author Phu
 */
@Controller("shopBrowse")
public class BrowseController extends BaseShop {

    @Autowired
    private ItemService itemService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private PromotionService promotionService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private Gson gson;
    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "/{alias}/browse")
    public String index(@PathVariable String alias, ModelMap model,
            @RequestParam(value = "cid", required = false) String cid,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "promotionId", required = false) String promotionId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "order", defaultValue = "0") int order,
            HttpServletResponse response) {
        alias = alias.toLowerCase();
        ShopCategory shopCategory = null;
        ItemSearch itemSearch = new ItemSearch();
        if (cid != null && !cid.trim().equals("")) {
            itemSearch.setShopCategoryId(cid);
            shopCategory = shopCategoryService.get(cid);
            if (shopCategory == null) {
                try {
                    shopCategory = new ShopCategory();
                    Category category = categoryService.get(cid);
                    shopCategory.setName(category.getName());
                    shopCategory.setId(category.getId());
                    List<String> cids = new ArrayList<>();
                    cids.add(category.getId());
                    itemSearch.setCategoryIds(cids);
                    itemSearch.setShopCategoryId(null);
                } catch (Exception ex) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return "market.404";
                    //throw new HttpServerErrorException(HttpStatus.NOT_FOUND, "Danh mục không tồn tại");
                }
            }
        }
        Shop shopCheck = shopService.getByAlias(alias);
        if (shopCheck == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setHeader("Location", baseUrl + "/404.html");
            return "market.404";
        }
        Seller seller = sellerService.getById(shopCheck.getUserId());
        if (!seller.isNlIntegrated() || !seller.isScIntegrated()) {
            if (viewer.getUser() == null || !viewer.getUser().getId().equals(shopCheck.getUserId())) {
                String url = baseUrl + "/" + alias + "/index.html";
                response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
                response.setHeader("Location", url);
                return "shop.index";
                //return "redirect:" + url;
            }
        }
        initMap(alias, model, response);

        Shop shop = (Shop) model.get("shop");

        if (page > 1) {
            page = page - 1;
        } else {
            page = 0;
        }
        itemSearch.setPageIndex(page);
        itemSearch.setPageSize(40);
        itemSearch.setStatus(1);
        itemSearch.setOrderBy(order);
        itemSearch.setSellerId(shop.getUserId());

        if (keyword != null && !keyword.trim().equals("")) {
            itemSearch.setKeyword(keyword);
        }
        if (promotionId != null && !promotionId.trim().equals("")) {
            itemSearch.setPromotionId(promotionId);
        }

        DataPage<Item> search = itemService.search(itemSearch);
        for (Item item : search.getData()) {
            List<String> images = new ArrayList<>();
            if (item != null && item.getImages() != null && !item.getImages().isEmpty()) {
                for (String img : item.getImages()) {
                    images.add(imageService.getUrl(img).thumbnail(210, 280, "inset").getUrl(item.getName()));
                }
                item.setImages(images);
            }
        }
        String clientScript = model.get("clientScript").toString();
        clientScript += "browse.init({itemSearch:" + gson.toJson(itemSearch) + "});";
        model.put("shopCategory", shopCategory);
        model.put("itemPage", search);
        model.put("itemSearch", itemSearch);
        model.put("clientScript", clientScript);
        if (promotionId != null && !promotionId.equals("")) {
            Promotion promotion = promotionService.getPromotion(promotionId);
            if (promotion.getName().toLowerCase().contains("chương trình")) {
                model.put("title", "Xem " + promotion.getName() + " tại " + shop.getTitle() + "");
                model.put("description", "Xem " + promotion.getName() + " tại " + shop.getTitle() + " chodientu.vn - Giá rẻ, nhiều khuyến mại, thanh toán online, CoD, giao hàng toàn quốc, bảo vệ người mua.");
            } else {
                model.put("title", "Xem chương trình " + promotion.getName() + " tại " + shop.getTitle() + "");
                model.put("description", "Xem chương trình " + promotion.getName() + " tại " + shop.getTitle() + " chodientu.vn - Giá rẻ, nhiều khuyến mại, thanh toán online, CoD, giao hàng toàn quốc, bảo vệ người mua.");
            }

            model.put("keywords", "giảm giá, ưu đãi, khuyến mại, hotdeal," + promotion.getName());
            String keywordS = "";
            String pageS = "";
            String cidS = "";
            String promotionIdS = "";
            page = page + 1;
            if (page > 1) {
                pageS = "&page=" + page;
            }
            if (cid != null && !cid.equals("")) {
                cidS = "?cid=" + cid;
            } else {
                if (page > 1) {
                    pageS = "?page=" + page;
                }
            }
            if (promotionId != null && !promotionId.equals("")) {
                promotionIdS = "?promotionId=" + promotionId;
            } else {
                if (page > 1) {
                    pageS = "?page=" + page;
                }
            }
            if (keyword != null && !keyword.equals("")) {
                keywordS = "?keyword=" + keyword;
            } else {
                if (page > 1) {
                    pageS = "?page=" + page;
                }
            }
            model.put("promotion", promotion);
            model.put("canonical", "/" + shop.getAlias() + "/browse.html" + cidS + keywordS + promotionIdS + pageS);
            long totalVisit = promotion.getTotalVisit() + 1;
            promotion.setTotalVisit(totalVisit);
            promotionService.save(promotion);
        } else {
            if (shopCategory == null) {
                shopCategory = new ShopCategory();
                shopCategory.setName("toàn bộ sản phẩm");
            }
            model.put("title", "Sản phẩm " + shopCategory.getName() + " của shop " + shop.getTitle() + " | ChoDienTu.vn" + (page > 0 ? ",trang " + (page + 1) + "" : ""));
            model.put("description", "Mua bán các sản phẩm  " + shopCategory.getName() + " của shop " + shop.getTitle() + " trên ChoDienTu.vn - Liên doanh eBay uy tín số 1" + (page > 0 ? " Trang " + (page + 1) + "." : "."));
            List<String> listImg = imageService.get(ImageType.SHOP_LOGO, shop.getUserId());
            if (listImg != null && !listImg.isEmpty()) {
                model.put("ogImage", imageService.getUrl(listImg.get(0)).compress(100).getUrl(shop.getAlias()));
            }
            String keywordS = "";
            String pageS = "";
            String cidS = "";
            page = page + 1;
            if (page > 1) {
                pageS = "&page=" + page;
            }
            if (cid != null && !cid.equals("")) {
                cidS = "?cid=" + cid;
            } else {
                if (page > 1) {
                    pageS = "?page=" + page;
                }
            }
            if (keyword != null && !keyword.equals("")) {
                keywordS = "?keyword=" + keyword;
            } else {
                if (page > 1) {
                    pageS = "?page=" + page;
                }
            }

            model.put("canonical", "/" + shop.getAlias() + "/browse.html" + cidS + keywordS + pageS);
        }
        return "shop.browse";
    }

    @RequestMapping(value = "/{alias}/promotion")
    public String promotion(@PathVariable String alias, ModelMap model,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "order", defaultValue = "0") int order,
            HttpServletResponse response) {
        alias = alias.toLowerCase();
        ShopCategory shopCategory = null;
        ItemSearch itemSearch = new ItemSearch();

        Shop shopCheck = shopService.getByAlias(alias);
        if (shopCheck == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setHeader("Location", baseUrl + "/404.html");
            return "market.404";
        }
        Seller seller = sellerService.getById(shopCheck.getUserId());
        if (!seller.isNlIntegrated() || !seller.isScIntegrated()) {
            if (viewer.getUser() == null || !viewer.getUser().getId().equals(shopCheck.getUserId())) {
                String url = baseUrl + "/" + alias + "/index.html";
                response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
                response.setHeader("Location", url);
                return "shop.index";
                //return "redirect:" + url;
            }
        }
        initMap(alias, model, response);

        Shop shop = (Shop) model.get("shop");

        if (page > 1) {
            page = page - 1;
        } else {
            page = 0;
        }
        itemSearch.setPageIndex(page);
        itemSearch.setPageSize(40);
        itemSearch.setStatus(1);
        itemSearch.setOrderBy(order);
        itemSearch.setSellerId(shop.getUserId());

        DataPage<Item> search = itemService.search(itemSearch);
        for (Item item : search.getData()) {
            List<String> images = new ArrayList<>();
            if (item != null && item.getImages() != null && !item.getImages().isEmpty()) {
                for (String img : item.getImages()) {
                    images.add(imageService.getUrl(img).thumbnail(210, 280, "inset").getUrl(item.getName()));
                }
                item.setImages(images);
            }
        }
        String clientScript = model.get("clientScript").toString();
        clientScript += "browse.init({itemSearch:" + gson.toJson(itemSearch) + "});";
        model.put("shopCategory", shopCategory);
        model.put("itemPage", search);
        model.put("itemSearch", itemSearch);
        model.put("clientScript", clientScript);

        return "shop.promotion";
    }
}
