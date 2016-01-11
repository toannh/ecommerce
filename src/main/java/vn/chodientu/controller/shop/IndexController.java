package vn.chodientu.controller.shop;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpServerErrorException;
import vn.chodientu.entity.db.Item;
import vn.chodientu.entity.db.Shop;
import vn.chodientu.entity.db.ShopBanner;
import vn.chodientu.entity.db.ShopHomeItem;
import vn.chodientu.entity.enu.ImageType;
import vn.chodientu.entity.enu.ShopBannerType;
import vn.chodientu.entity.input.ItemSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.service.ImageService;
import vn.chodientu.service.ItemService;
import vn.chodientu.service.ShopBannerService;
import vn.chodientu.service.ShopHomeItemService;
import vn.chodientu.service.ShopService;

/**
 * @since Jun 2, 2014
 * @author Phu
 */
@Controller("shopIndex")
public class IndexController extends BaseShop {

    @Autowired
    private ShopService shopService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private Gson gson;
    @Autowired
    private ShopBannerService shopBannerService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ShopHomeItemService shopHomeItemService;
    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "/{alias}/index")
    public String index(@PathVariable String alias, ModelMap model,
            HttpServletResponse response) {
        String q = request.getQueryString();
        alias = alias.toLowerCase();
        Shop shopCheck = shopService.getByAlias(alias);
        if (shopCheck == null) {
            if (q != null && !q.equals("")) {
                if (q.contains("keyword")) {
                    q = q.substring(q.indexOf("=") + 1, q.length());
                }
                //String url = baseUrl + "/s/" + q + ".html";
                response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
                response.setHeader("Location", baseUrl + "/s/" + q + ".html");
                return "market.browse.search";
                //  return "redirect:" + url;

            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.setHeader("Location", baseUrl + "/404.html");
                return "market.404";
            }

        }
        try {
            initMap(alias, model, response);
        } catch (Exception ex) {
            if (((HttpServerErrorException) ex).getStatusCode().equals(HttpStatus.NOT_IMPLEMENTED)) {
                if (((HttpServerErrorException) ex).getStatusText().contains("Bạn chưa cấu hình tích hợp Nganluong hoặc Shipchung cho shop")) {

                    model.put("clientScript", "browse.redirect('/index.html', 6000);");
//                    return "redirect:" + baseUrl + "/index.html";
                }
            } else if (((HttpServerErrorException) ex).getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.setHeader("Location", baseUrl + "/index.html");
                return "market.index";
            }
//            
        }
        Shop shop = shopService.getByAlias(alias);
        //Lấy heart banner ra trang chủ shop 
        List<ShopBanner> shopHeartBanner = new ArrayList<>();
        List<ShopBanner> shopLeftBanner = new ArrayList<>();
        List<ShopBanner> shopBanners = shopBannerService.getAll(shop.getUserId(), 1);
        List<String> idsShopBanner = new ArrayList<>();
        if (shopBanners != null && shopBanners.size() > 0) {
            for (ShopBanner shopBanner : shopBanners) {
                if (shopBanner.getEmbedCode() == null) {
                    idsShopBanner.add(shopBanner.getId());
                }
            }
        }
        if (idsShopBanner != null && idsShopBanner.size() > 0) {
            Map<String, List<String>> get = imageService.get(ImageType.SHOP_BANNER, idsShopBanner);
            if (get != null && get.size() > 0) {
                for (Map.Entry<String, List<String>> entry : get.entrySet()) {
                    String imageTagerId = entry.getKey();
                    List<String> img = entry.getValue();
                    for (ShopBanner banner : shopBanners) {
                        if (banner.getId().equals(imageTagerId) && img != null && img.size() > 0) {
                            banner.setImage(imageService.getUrl(img.get(0)).compress(100).getUrl(banner.getTitle()));
                        }
                    }
                }
            }
            for (ShopBanner shopBanner : shopBanners) {
                if (shopBanner.getType() == ShopBannerType.HEART) {
                    shopHeartBanner.add(shopBanner);
                }
                if (shopBanner.getType() == ShopBannerType.ADV_LEFT) {
                    shopLeftBanner.add(shopBanner);
                }
            }
        }

        //Lấy sản phẩm mới nhất ra trang chủ shop
        List<String> itemNewIds = new ArrayList<>();
        ItemSearch itemSearch = new ItemSearch();
        itemSearch.setPageIndex(0);
        itemSearch.setPageSize(40);
        itemSearch.setStatus(1);
        itemSearch.setOrderBy(3);
        itemSearch.setSellerId(shop.getUserId());
        DataPage<Item> search = itemService.search(itemSearch);

        List<Item> listItemsNew = search.getData();
        //List<Item> listItemsNew = itemService.getBSellerLimit(shop.getUserId());
        if (listItemsNew != null && listItemsNew.size() > 0) {
            for (Item item : listItemsNew) {
                itemNewIds.add(item.getId());
            }
        }
        if (itemNewIds != null && itemNewIds.size() > 0) {
            Map<String, List<String>> get = imageService.get(ImageType.ITEM, itemNewIds);
            if (get != null && get.size() > 0) {
                for (Map.Entry<String, List<String>> entry : get.entrySet()) {
                    String imageTagerId = entry.getKey();
                    List<String> img = entry.getValue();
                    for (Item item : listItemsNew) {
                        if (item.getId().equals(imageTagerId) && img != null && img.size() > 0) {
                            String url = imageService.getUrl(img.get(0)).thumbnail(210, 280, "inset").getUrl(item.getName());
                            List<String> urls = new ArrayList<>();
                            urls.add(url);
                            item.setImages(urls);
                        }
                    }
                }
            }
        }
        //Lấy sản phẩm xem nhiều nhất ra trang chủ shop
        List<String> itemViewCountIds = new ArrayList<>();
        ItemSearch itemSearch1 = new ItemSearch();
        itemSearch1.setPageIndex(0);
        itemSearch1.setPageSize(4);
        itemSearch1.setStatus(1);
        itemSearch1.setOrderBy(5);
        itemSearch1.setSellerId(shop.getUserId());
        DataPage<Item> dataPage = itemService.search(itemSearch1);

        List<Item> listItemsViewCount = dataPage.getData();
        //List<Item> listItemsNew = itemService.getBSellerLimit(shop.getUserId());
        if (listItemsViewCount != null && listItemsViewCount.size() > 0) {
            for (Item item : listItemsViewCount) {
                itemViewCountIds.add(item.getId());
            }
        }
        if (listItemsViewCount != null && listItemsViewCount.size() > 0) {
            Map<String, List<String>> get = imageService.get(ImageType.ITEM, itemViewCountIds);
            if (get != null && get.size() > 0) {
                for (Map.Entry<String, List<String>> entry : get.entrySet()) {
                    String imageTagerId = entry.getKey();
                    List<String> img = entry.getValue();
                    for (Item item : listItemsViewCount) {
                        if (item.getId().equals(imageTagerId) && img != null && img.size() > 0) {
                            String url = imageService.getUrl(img.get(0)).thumbnail(210, 280, "inset").getUrl(item.getName());
                            List<String> urls = new ArrayList<>();
                            urls.add(url);
                            item.setImages(urls);
                        }
                    }
                }
            }
        }
        //lấy sản phẩm nổi bật ra trang chủ
        List<ShopHomeItem> homeItems = shopHomeItemService.getAll(shop.getUserId());
        List<String> itemIdNB = new ArrayList<>();
        if (homeItems != null && !homeItems.isEmpty()) {
            for (ShopHomeItem homeItem : homeItems) {
                for (String homeId : homeItem.getItemIds()) {
                    if (!itemIdNB.contains(homeId)) {
                        itemIdNB.add(homeId);
                    }
                }
            }
        }
        List<Item> itemsNB = itemService.list(itemIdNB);
        // Check : loai bo San pham het han, chua duyet, het hang
        java.util.Iterator<Item> listItems = itemsNB.iterator();
        long datetime = System.currentTimeMillis();
        while (listItems.hasNext()) {
            Item item = listItems.next();
            if (item.getQuantity() <= 0 || item.getEndTime() < datetime || !item.isApproved() || item.getStartTime() > datetime || !item.isActive()) {
                listItems.remove();
            }

        }
        if (itemsNB != null && !itemsNB.isEmpty()) {
            Map<String, List<String>> get = imageService.get(ImageType.ITEM, itemIdNB);
            if (get != null && get.size() > 0) {
                for (Map.Entry<String, List<String>> entry : get.entrySet()) {
                    String imageTagerId = entry.getKey();
                    List<String> img = entry.getValue();
                    for (Item item : itemsNB) {
                        if (item.getId().equals(imageTagerId) && img != null && img.size() > 0) {
                            String url = imageService.getUrl(img.get(0)).thumbnail(210, 280, "inset").getUrl(item.getName());
                            List<String> urls = new ArrayList<>();
                            urls.add(url);
                            item.setImages(urls);
                        }
                    }
                }
            }
        }

        model.put("listHomeItemsNB", homeItems);
        model.put("listItemsNB", itemsNB);
        model.put("listItemsIdNB", itemIdNB);
        model.put("listItemsViewCounts", listItemsViewCount);
        model.put("listItemsNew", listItemsNew);
        model.put("shopHeartBanner", shopHeartBanner);
        model.put("shopBannerLefts", shopLeftBanner);
        return "shop.index";
    }
}
