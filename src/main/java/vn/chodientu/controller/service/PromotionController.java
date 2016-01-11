package vn.chodientu.controller.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.chodientu.component.Validator;
import vn.chodientu.controller.BaseRest;
import vn.chodientu.entity.db.Item;
import vn.chodientu.entity.db.Promotion;
import vn.chodientu.entity.db.PromotionCategory;
import vn.chodientu.entity.db.PromotionItem;
import vn.chodientu.entity.enu.PromotionTarget;
import vn.chodientu.entity.enu.PromotionType;
import vn.chodientu.entity.input.ItemSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.entity.output.Response;
import vn.chodientu.service.ItemService;
import vn.chodientu.service.PromotionService;
import vn.chodientu.util.UrlUtils;

@Controller("servicePromotion")
@RequestMapping("/promotion")
public class PromotionController extends BaseRest {

    @Autowired
    private PromotionService promotionService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private Validator validator;

    @RequestMapping(value = "/addcategorypromotion", method = RequestMethod.POST)
    @ResponseBody

    public Response addPromotionCategory(@RequestBody Promotion promotion) {
        Map<String, String> errors = validator.validate(promotion);
        if (viewer == null || viewer.getUser() == null) {
            return new Response(false, "Bạn phải đăng nhập để tạo khuyến mại!");
        }

        if (promotion.getName() == null || promotion.getName().equals("")) {
            errors.put("promotionName", "Bạn chưa nhập tên chương trình khuyến mại");
        } else if (promotion.getName().length() <= 0 || promotion.getName().length() > 125) {
            errors.put("promotionName", "Tên chương trình khuyến mại phải từ 1 đến 125 ký tự!");
        }
        long time = System.currentTimeMillis();
        if (promotion.getStartTime() == 0) {
            promotion.setStartTime(time);
        }
        if (promotion.getStartTime() < time) {
            errors.put("startTime", "Thời gian bắt đầu khuyến mại phải lớn hơn thời gian hiện tại");
        }
        if (promotion.getEndTime() == 0) {
            promotion.setEndTime(promotion.getStartTime() + 604800000);
        }
        if (promotion.getEndTime() <= promotion.getStartTime()) {
            errors.put("endTime", "Thời gian kết thúc phải lớn hơn thời gian bắt đầu");
        }
        if (promotion.getMinOrderPrice() < 0) {
            errors.put("minOrderPrice", "Giá trị nhỏ nhất của hóa đơn không được là số âm!");
        }
        if (promotion.getTarget() == PromotionTarget.CATEGORY || promotion.getTarget() == PromotionTarget.SHOP_CATEGORY) {
            List<PromotionCategory> categories = promotion.getCategories();
            if (categories == null || categories.isEmpty()) {
                errors.put("categories", "Phải có ít nhất 1 danh mục có giảm giá!");
            }
            ItemSearch itemSearch = new ItemSearch();
            itemSearch.setPageIndex(0);
            itemSearch.setPageSize(4);
            itemSearch.setStatus(1);
            itemSearch.setSellerId(viewer.getUser().getId());
            itemSearch.setCategoryIds(new ArrayList<String>());
            int i = 0;
            for (PromotionCategory ct : categories) {
                if (ct.getCategoryId() != null && !ct.getCategoryId().equals("")) {
                    itemSearch.setCategoryIds(new ArrayList<String>());
                    itemSearch.getCategoryIds().add(ct.getCategoryId());
                    DataPage<Item> search = itemService.search(itemSearch);
                    List<Item> items = search.getData();
                    if (items != null && !items.isEmpty()) {
                        i += items.size();
                    }
                } else {
                    itemSearch.setShopCategoryId(ct.getShopCategoryId());
                    DataPage<Item> search = itemService.search(itemSearch);
                    List<Item> items = search.getData();
                    if (items != null && !items.isEmpty()) {
                        i += items.size();
                    }
                }

                if (ct.getDiscountPercent() > 50) {
                    errors.put("categories", "Khuyến mại cho danh mục không được lớn hơn 50% !");
                }
                if (ct.getDiscountPrice() != 0 && ct.getDiscountPercent() != 0) {
                    errors.put("categories", "Danh mục chỉ được nhận 1 loại giảm giá theo phần trăm hoặc giá!");
                }
            }
            if (i < 4) {
                errors.put("categories", "Phải có ít nhất 4 sản phẩm được giảm giá");
            }
        }
        if (promotion.getTarget() == PromotionTarget.ITEM) {
            List<PromotionItem> items = promotion.getItems();
            if (promotion.getType() == PromotionType.DISCOUND) {
                if (items == null || items.isEmpty() || items.size() < 4) {
                    errors.put("categories", "Phải có ít nhất 4 sản phẩm được giảm giá");
                }
                for (PromotionItem it : items) {
                    if (it.getDiscountPercent() != 0 && it.getDiscountPrice() != 0) {
                        errors.put("categories", "Sản phẩm chỉ được nhận 1 loại giảm giá theo phần trăm hoặc giá");
                    }
                    if (it.getDiscountPercent() > 50) {
                        errors.put("categories", "Khuyến mại cho sản phẩm không được lớn hơn 50% !");
                    }
                    if (it.getDiscountPrice() >= it.getOldPrice()) {
                        errors.put("categories", "Giảm giá cho sản phẩm: " + it.getItemId() + " phải nhỏ hơn giá bán hiện tại!");
                    }
                    if (it.getDiscountPrice() > 0 && it.getOldPrice() > 0) {
                        if (it.getOldPrice() > (it.getOldPrice() - it.getDiscountPrice()) * 2) {
                            errors.put("categories", "Giảm giá cho sản phẩm: " + it.getItemId() + " không được vượt quá 50% so với giá hiện tại!");
                        }
                    }
                }
            } else {
                if (items == null || items.isEmpty()) {
                    errors.put("categories", "Phải có ít nhất 1 sản phẩm có quà tặng");
                }
            }
        }
        if (errors.isEmpty()) {
            promotion.setSellerId(viewer.getUser().getId());
            if (promotion.getId() == null || promotion.getId().equals("")) {
                return promotionService.add(promotion);
            } else {
                return promotionService.update(promotion);
            }
        }
        return new Response(false, "Thất bại!", errors);
    }

    @ResponseBody
    @RequestMapping(value = "/stoppromotion", method = RequestMethod.GET)
    public Response stopPromotion(@RequestParam(value = "promId", defaultValue = "") String promId) throws Exception {
        if (viewer == null || viewer.getUser() == null) {
            return new Response(false, "Bạn phải đăng nhập để sửa khuyến mại");
        }
        if (promId.equals("")) {
            return new Response(false, "Dữ liệu không hợp lệ, hãy kiểm tra lại");
        }
        return promotionService.stopPromotion(promId, viewer.getUser().getId());
    }

    @ResponseBody
    @RequestMapping(value = "/getdetailpromotion", method = RequestMethod.GET)
    public Response getDetailPromotion(@RequestParam(value = "promId", defaultValue = "") String promId,
            @RequestParam(value = "page", defaultValue = "0") int page) {
        if (viewer == null || viewer.getUser() == null) {
            return new Response(false, "Bạn phải đăng nhập để xem chi tiết khuyến mại");
        }
        if (promId.equals("")) {
            return new Response(false, "Dữ liệu không hợp lệ, hãy kiểm tra lại");
        }
        List<String> ids = new ArrayList<>();
        if (page > 0) {
            page = page - 1;
        }
        DataPage<PromotionItem> dataPage = promotionService.getPromotionItem(promId, new PageRequest(page, 15));
        if (dataPage.getDataCount() > 0 && dataPage.getData() != null) {
            for (PromotionItem pi : dataPage.getData()) {
                ids.add(pi.getItemId());
            }
        }
        List<Item> list = itemService.list(ids);
        Map map = new HashMap<String, Object>();
        map.put("dataPage", dataPage);
        map.put("items", list);

        return new Response(true, "ok", map);
    }

    @RequestMapping(value = "/checkip-cdt", method = RequestMethod.GET)
    @ResponseBody
    public Response checkIpCDT(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        if (UrlUtils.checkIPInCDT(ipAddress)) {
            return new Response(false, "IP là của NV chodientu.vn");
        }
        return new Response(true, "OK");
    }

}
