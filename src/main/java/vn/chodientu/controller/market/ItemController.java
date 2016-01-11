package vn.chodientu.controller.market;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.chodientu.component.imboclient.Url.ImageUrl;
import vn.chodientu.entity.db.AdministratorRole;
import vn.chodientu.entity.db.Category;
import vn.chodientu.entity.db.CategoryProperty;
import vn.chodientu.entity.db.CategoryPropertyValue;
import vn.chodientu.entity.db.City;
import vn.chodientu.entity.db.Coupon;
import vn.chodientu.entity.db.District;
import vn.chodientu.entity.db.Item;
import vn.chodientu.entity.db.ItemDetail;
import vn.chodientu.entity.db.Manufacturer;
import vn.chodientu.entity.db.Model;
import vn.chodientu.entity.db.Promotion;
import vn.chodientu.entity.db.Seller;
import vn.chodientu.entity.db.SellerSupportFee;
import vn.chodientu.entity.db.Shop;
import vn.chodientu.entity.db.User;
import vn.chodientu.entity.enu.FeeType;
import vn.chodientu.entity.enu.ImageType;
import vn.chodientu.entity.enu.PromotionType;
import vn.chodientu.entity.input.ItemSearch;
import vn.chodientu.entity.input.PropertySearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.entity.output.Response;
import vn.chodientu.service.AdministratorService;
import vn.chodientu.service.CategoryService;
import vn.chodientu.service.CityService;
import vn.chodientu.service.CouponService;
import vn.chodientu.service.DistrictService;
import vn.chodientu.service.ImageService;
import vn.chodientu.service.ItemFollowService;
import vn.chodientu.service.ItemService;
import vn.chodientu.service.ManufacturerService;
import vn.chodientu.service.ModelService;
import vn.chodientu.service.PromotionService;
import vn.chodientu.service.SellerService;
import vn.chodientu.service.SellerSupportFeeService;
import vn.chodientu.service.ShopCategoryService;
import vn.chodientu.service.ShopContactService;
import vn.chodientu.service.ShopService;
import vn.chodientu.service.UserService;
import vn.chodientu.util.TextUtils;
import vn.chodientu.util.UrlUtils;

@Controller("itemdetailController")
public class ItemController extends BaseMarket {

    @Autowired
    private ItemService itemService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ManufacturerService manufacturerService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private PromotionService promotionService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private UserService userService;
    @Autowired
    private ShopContactService shopContactService;
    @Autowired
    private CityService cityService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private Gson json;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private ItemFollowService itemFollowService;
    @Autowired
    private SellerSupportFeeService sellerSupportFeeService;
    @Autowired
    private AdministratorService administratorService;

    private void viewItemHistory(String cookie, String itemId, HttpServletResponse response) {
        List<String> ids = new ArrayList<>();;
        try {
            ids = json.fromJson(cookie, new TypeToken<List<String>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
        }
        if (ids == null) {
            ids = new ArrayList<>();
        }
        if (!ids.contains(itemId)) {
            ids.add(itemId);
            Cookie c = new Cookie("itemviews", json.toJson(ids.toArray(new String[0])));
            c.setPath("/");
            c.setMaxAge(365 * 24 * 3600);
            response.addCookie(c);
        }
    }

    @RequestMapping({"/san-pham/{id}/{name}.html"})
    public String detail(@PathVariable("id") String id, @PathVariable("name") String name,
            @CookieValue(value = "itemviews", defaultValue = "") String itemviews,
            HttpServletResponse response, HttpServletRequest request,
            ModelMap model) throws Exception {

        if (id == null || id.trim().equals("")) {
            return "index.404";
        }
        Item itemCheckExist = itemService.getSearch(id);
        String imageId = null;
        if (itemCheckExist == null) {
            if (name.length() > 10) {
                name = name.substring(0, 10);
            }
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.setHeader("Location", baseUrl + "/s/" + name + ".html");
            return "market.browse.search";
            //return "redirect:" + baseUrl + "/s/" + name + ".html";
        }

        Item item = itemService.get(id);
        try {
            String itemUrl = UrlUtils.item(item.getId(), item.getName());
            if (!request.getRequestURI().equals(itemUrl)) {
                String q = request.getQueryString();
                if (q != null && !q.equals("")) {
                    q = "?" + q;
                } else {
                    q = "";
                }
                try {
//                    response.sendError(301);
                } catch (Exception e) {
                }
                response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
                response.setHeader("Location", baseUrl + itemUrl + q);
                return "market.item.detail";
            }

            this.viewItemHistory(itemviews, id, response);
            itemService.viewcount(item);
            //Lấy thương hiệu của sản phẩm nếu có
            if (item.getManufacturerId() != null && !item.getManufacturerId().equals("")) {
                Manufacturer manufacturer = manufacturerService.getManufacturer(item.getManufacturerId());
                if (manufacturer != null) {
                    item.setManufacturerName(manufacturer.getName());
                }
            }
            //Lấy ảnh sản phẩm
            List<String> images = new ArrayList<>();
            if (item.getImages() != null && !item.getImages().isEmpty()) {
                imageId = item.getImages().get(0);
                for (String img : item.getImages()) {
                    ImageUrl url = imageService.getUrl(img);
                    if (url != null) {
                        images.add(url.compress(100).getUrl(item.getName()));
                    }
                }
            }
            item.setImages(images);
            // Lấy danh mục,path danh mục của sản phẩm
            List<Category> listCat = new ArrayList<>();
            try {
                listCat = categoryService.get(item.getCategoryPath());
            } catch (Exception e) {
            }
            Category category = null;
            try {
                category = categoryService.get(item.getCategoryId());
            } catch (Exception e) {
            }
            //Lấy detail sản phẩm
            ItemDetail itemDetail = null;
            try {
                itemDetail = itemService.getDetail(item.getId());
                String removeUrl = itemService.removeUrl(itemDetail.getDetail());
                itemDetail.setDetail(removeUrl);
            } catch (Exception e) {
            }
            //Lấy user
            User user = new User();
            City ct = null;
            District dt = null;
            Response userResp = userService.getById(item.getSellerId());
            if (userResp.isSuccess()) {
                List<String> getImgUser = null;
                user = (User) userResp.getData();
                getImgUser = imageService.get(ImageType.AVATAR, user.getId());
                if (getImgUser != null && !getImgUser.isEmpty()) {
                    user.setAvatar(imageService.getUrl(getImgUser.get(0)).thumbnail(42, 42, "outbound").compress(100).getUrl("AVATAR"));
                }
            }
            //Lấy shop
            Shop shop = shopService.getShop(item.getSellerId());
            if (shop != null) {
                shopService.viewCount(shop.getAlias());
                List<String> getImgShop = null;
                Response city = cityService.getCitybyId(shop.getCityId());
                ct = (City) city.getData();
                dt = districtService.get(shop.getDistrictId());
                shop.setViewCount(shop.getViewCount() + 1);
                getImgShop = imageService.get(ImageType.SHOP_LOGO, shop.getUserId());
                if (getImgShop != null && !getImgShop.isEmpty()) {
                    shop.setLogo(imageService.getUrl(getImgShop.get(0)).thumbnail(42, 42, "outbound").compress(100).getUrl(shop.getAlias()));
                }
            } else {
                Response city = cityService.getCitybyId(user.getCityId());
                if (city.isSuccess()) {
                    ct = (City) city.getData();
                    dt = districtService.get(user.getDistrictId());
                }
            }

            ItemSearch its = new ItemSearch();
            its.setPageSize(10);
            its.setStatus(1);
            Model md = null;
            try {
                md = modelService.getModel(item.getModelId());
                model.put("model", md);
                List<String> mList = new ArrayList<>();
                mList.add(item.getModelId());
                its.setCategoryIds(new ArrayList<String>());
                its.getCategoryIds().add(item.getCategoryId());
                its.setModelIds(mList);
                its.setOrderBy(2);
            } catch (Exception e) {
                its.setCategoryIds(new ArrayList<String>());
                its.getCategoryIds().add(item.getCategoryId());
                its.setPriceFrom((int) (item.getSellPrice() - (item.getSellPrice() / 10)));
                its.setPriceTo((int) (item.getSellPrice() + (item.getSellPrice() / 10)));
                its.setOrderBy(0);
            }
            try {
                DataPage<Item> itAdv = itemService.search(its);
                for (Item it : itAdv.getData()) {
                    List<String> img = new ArrayList<>();
                    if (it != null && it.getImages() != null && !it.getImages().isEmpty()) {
                        for (String ig : it.getImages()) {
                            img.add(imageService.getUrl(ig).thumbnail(50, 50, "outbound").compress(100).getUrl(it.getName()));
                        }
                        it.setImages(img);
                    }
                }
                model.put("itAdv", itAdv);
            } catch (Exception e) {
            }
            //Lấy sản phẩm tương tự ,cùng khoảng giá nếu sản phẩm hết hạn đăng bán hoặc hết hàng
            ItemSearch searchSame = new ItemSearch();
            searchSame.setStatus(1);
            searchSame.setCategoryIds(new ArrayList<String>());
            searchSame.getCategoryIds().add(item.getCategoryId());
            searchSame.setPriceFrom((int) (item.getSellPrice() - (item.getSellPrice() / 10)));
            searchSame.setPriceTo((int) (item.getSellPrice() + (item.getSellPrice() / 10)));
            searchSame.setModelIds(new ArrayList<String>());
            searchSame.setManufacturerIds(new ArrayList<String>());
            searchSame.setCityIds(new ArrayList<String>());
            searchSame.setProperties(new ArrayList<PropertySearch>());

            //Lấy coupon đang chạy của người bán trong khoảng thời gian người mua xem tin bán
            Coupon coupon = couponService.checkExistCouponByTime(item.getSellerId(), System.currentTimeMillis());
            //Lấy bất kì một chiến dịch đang chạy của người bán
            List<Promotion> listPromotion = promotionService.getPromotionBySellerIsRunning(item.getSellerId(), PromotionType.DISCOUND, 1);
            Promotion promotion = null;
            List<Item> itemPromotion = null;
            if (listPromotion != null && !listPromotion.isEmpty()) {
                promotion = listPromotion.get(0);
                ItemSearch search = new ItemSearch();
                search.setPageIndex(0);
                search.setPageSize(100);
                search.setStatus(1);
                search.setSellerId(promotion.getSellerId());
                search.setPromotionId(promotion.getId());
                DataPage<Item> itemPro = itemService.search(search);
                for (Item it : itemPro.getData()) {
                    List<String> img = new ArrayList<>();
                    if (it.getImages() != null && !it.getImages().isEmpty()) {
                        for (String ig : it.getImages()) {
                            img.add(imageService.getUrl(ig).compress(100).getUrl(it.getName()));
                        }
                    }
                    it.setImages(img);
                }
                itemPromotion = itemPro.getData();
            }
            //Lấy người bán
            Seller seller = sellerService.getById(item.getSellerId());

            ItemSearch itSearch = new ItemSearch();
            itSearch.setManufacturerIds(new ArrayList<String>());
            itSearch.setModelIds(new ArrayList<String>());
            itSearch.setCityIds(new ArrayList<String>());
            itSearch.setProperties(new ArrayList<PropertySearch>());
            String allCate = "";
            String keyword = "";
            if (listCat != null) {
                if (listCat.size() > 0) {
                    if (listCat.size() == 1) {
                        allCate += listCat.get(0).getName();
                        keyword = allCate;
                    } else if (listCat.size() == 2) {
                        allCate += listCat.get(0).getName() + ",";
                        allCate += listCat.get(1).getName() + ",";
                        keyword = allCate;
                    } else if (listCat.size() == 3) {
                        allCate += listCat.get(0).getName() + ",";
                        allCate += listCat.get(1).getName() + ",";
                        keyword += listCat.get(0).getName() + ",";
                        keyword += listCat.get(1).getName() + ",";
                        keyword += listCat.get(2).getName() + ",";
                    }
                }
            }
            String sellPriceItem = TextUtils.sellPrice(item.getSellPrice(), item.isDiscount(), item.getDiscountPrice(), item.getDiscountPercent());
            if (item.getEndTime() < System.currentTimeMillis()) {
                model.put("title", item.getName() + "- Sản phẩm " + item.getId() + " đã hết hạn");
                model.put("description", item.getName() + " giá " + sellPriceItem + "đ tại " + shop.getAlias() + " - " + category.getName() + " | ChoDienTu.vn");
            } else {
                model.put("title", item.getName() + " " + item.getId() + " - " + category.getName() + " | ChoDienTu.vn");
                model.put("description", item.getName() + " giá " + sellPriceItem + "đ tại " + (shop != null ? shop.getAlias() : "") + " - " + category.getName() + " | ChoDienTu.vn");
            }
            model.put("keywords", keyword);
            model.put("refresh", "true");
            /**
             * Thẻ chống trùng lặp
             */
            model.put("canonical", UrlUtils.item(item.getId(), item.getName()));
            try {
                ImageUrl compress = imageService.getUrl(imageId).thumbnail(400, 400, "outbound").compress(100);
                model.put("ogImage", compress.getUrl(item.getName()));
            } catch (Exception e) {
            }

            //lấy quyền admin
            List<AdministratorRole> roles = null;
            if (viewer.getAdministrator() != null) {
                try {
                    roles = administratorService.getRoles(viewer.getAdministrator().getId());
                } catch (Exception e) {
                }
            }
            // Người bán hỗ trợ thanh toán vận chuyển

            String sellPrice = TextUtils.sellPrice(item.getSellPrice(), item.isDiscount(), item.getDiscountPrice(), item.getDiscountPercent());
            String strPercent = sellPrice.replaceAll("\\.", "");
            SellerSupportFee sellerSupportFee = sellerSupportFeeService.getTopByOrderPrice(seller.getUserId(), Double.parseDouble(strPercent), FeeType.ONLINEPAYMENT);
            if (sellerSupportFee != null) {
                int discountPercent = sellerSupportFee.getDiscountPercent();
                if (discountPercent >= 2) {
                    //long sellerDiscountPayment = (Long.parseLong(strPercent) * discountPercent) / 100;
                    item.setSellerDiscountPayment(discountPercent);
                    item.setMinOrderPricePayment(sellerSupportFee.getMinOrderPrice());
                }
            }
            SellerSupportFee sellerSupportFeeCoD = sellerSupportFeeService.getTopByOrderPrice(seller.getUserId(), Double.parseDouble(strPercent), FeeType.COD);
            if (sellerSupportFeeCoD != null) {
                long discountShip = sellerSupportFeeCoD.getDiscountPrice();
                if (discountShip > 0) {
                    item.setSellerDiscountShipment(discountShip);
                    item.setMinOrderPriceShipment(sellerSupportFeeCoD.getMinOrderPrice());
                }

            }

            item.setProperties(itemService.getProperties(item.getId()));
            model.put("ct", ct);
            model.put("district", dt);
            model.put("promotion", promotion);
            model.put("itemPromotion", itemPromotion);
            model.put("coupon", coupon);
            model.put("contact", shopContactService.getContactById(item.getSellerId()));
            model.put("cities", cityService.list());
            model.put("shop", shop);
            model.put("seller", seller);
            model.put("user", user);
            User highest = null;
            try {
                highest = userService.get(item.getHighestBider());

                if (highest != null) {
                    String highestName = "";
                    if (highest.getUsername() != null && !highest.getUsername().equals("")) {
                        highestName = highest.getUsername();
                    } else if (highest.getName() != null && !highest.getName().equals("")) {
                        highestName = highest.getName();
                    } else {
                        highestName = highest.getEmail();
                    }
                    if (item.getEndTime() > System.currentTimeMillis()) {
                        if (viewer.getUser() != null && viewer.getUser().getId().equals(highest.getId())) {
                            highest.setName("Bạn đang đấu giá thắng");
                        } else {
                            highest.setName(highestName.substring(0, 1) + "*****" + highestName.substring(highestName.length() - 1, highestName.length()));
                        }
                    } else {
                        if (viewer.getUser() != null && viewer.getUser().getId().equals(highest.getId())) {
                            highest.setName("Bạn đã đấu giá thắng");
                        } else {
                            highest.setName(highestName);
                        }
                    }
                }
            } catch (Exception e) {
            }
            model.put("highestBider", highest);
            model.put("shopcategory", shopCategoryService.getChilds(null, seller.getUserId()));
            model.put("item", item);
            model.put("itemSearch", itSearch);
            model.put("itemDetail", itemDetail);
            model.put("category", category);
            model.put("countFollowItem", itemFollowService.countByItem(id));
            List<CategoryProperty> cateProperties = categoryService.getProperties(item.getCategoryId());
            List<CategoryPropertyValue> catePropertyValues = categoryService.getPropertyValuesWithCategoryId(item.getCategoryId());
            model.put("cateProperties", cateProperties);
            model.put("catePropertyValues", catePropertyValues);
            model.put("cat", listCat);
            String clientScript = "";
            clientScript += "var cities = " + json.toJson(cityService.list()) + ";";
            clientScript += "var adminRole = " + json.toJson(roles) + ";";
            clientScript += "var viewer = " + json.toJson(viewer.getUser()) + ";";
            clientScript += "var proprt = " + json.toJson(item.getProperties()) + ";";
            clientScript += "var cateProperties = " + json.toJson(cateProperties) + ";";
            clientScript += "var catePropertyValues = " + json.toJson(catePropertyValues) + ";";
            clientScript += "item.init({category:" + json.toJson(category) + ",itemSearch:" + json.toJson(itSearch) + ",item:" + json.toJson(item) + ",itemPromotion:" + json.toJson(promotionService.getPromotion(item.getPromotionId())) + "});";
            model.put("clientScript", clientScript);
            String cateCheckId = item.getCategoryPath().get(0);
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
                    script += "{ event: \"viewItem\", item: \"" + item.getId() + "\" }";
                    script += ");";
                    script += "</script>";
                    model.put("criteo", script);
                }
                model.put("remarketing", item.getCategoryId());
            }
        } catch (Exception ex) {
        }

        try {
            /**
             * thanhvv put cho google adv
             */
            model.put("adPage", "item" + item.getCategoryPath().get(0));
        } catch (Exception e) {
        }
        String price = TextUtils.sellPrice(item.getSellPrice(), item.isDiscount(), item.getDiscountPrice(), item.getDiscountPercent());
        price = price.replaceAll("\\.", "");
        model.put("remarketing", "dynx_itemid: \"" + item.getId() + "\",dynx_pagetype: \"offerdetail\",dynx_totalvalue: \"" + price + "\"");
        return "market.item.detail";
    }
}
