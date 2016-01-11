/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.controller.cp;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import vn.chodientu.entity.db.Category;
import vn.chodientu.entity.db.City;
import vn.chodientu.entity.db.Item;
import vn.chodientu.entity.db.User;
import vn.chodientu.entity.enu.ItemSource;
import vn.chodientu.entity.input.ItemSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.service.CategoryService;
import vn.chodientu.service.CityService;
import vn.chodientu.service.ImageService;
import vn.chodientu.service.ItemService;
import vn.chodientu.service.ManufacturerService;
import vn.chodientu.service.ModelService;
import vn.chodientu.service.ShopCategoryService;
import vn.chodientu.service.UserService;

/**
 *
 * @author ThuNguyen
 */
@Controller("reviewItem")
@RequestMapping(value = {"/cp"})
public class ReviewItemController extends BaseCp {

    @Autowired
    private ItemService itemService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private ManufacturerService manufacturerService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private CityService cityService;
    @Autowired
    private UserService userService;
    @Autowired
    private Gson gson;

    /**
     * Danh sÃ¡ch sáº£n pháº©m cáº§n review
     *
     * @param modelMap
     * @param session
     * @param page
     * @return
     */
    @RequestMapping(value = {"/reviewitem"}, method = RequestMethod.GET)
    public String reviewList(
            ModelMap modelMap,
            HttpSession session,
            @RequestParam(value = "page", defaultValue = "0") int page) {

        ItemSearch itemSearch = new ItemSearch();
        itemSearch.setStatus(4);
        List<String> cateIds = new ArrayList<>();
        List<String> shopCateIds = new ArrayList<>();
        List<String> manufIds = new ArrayList<>();
        List<String> modelIds = new ArrayList<>();
        if (itemSearch.getCategoryIds() == null) {
            itemSearch.setCategoryIds(new ArrayList<String>());
        }
        itemSearch.setSource(ItemSource.SELLER);
        long createTime=1388509200 * 1000L;
        itemSearch.setCreateTimeFrom(createTime);
        itemSearch.setCreateTimeTo(System.currentTimeMillis());
        if (session.getAttribute("itemSearch") != null && page != 0) {
            itemSearch = (ItemSearch) session.getAttribute("itemSearch");
        } else {
            session.setAttribute("itemSearch", itemSearch);
        }
        Map<String, Object> parentCategorys = new HashMap<>();
        getAncestors(itemSearch, parentCategorys);

        if (page > 0) {
            itemSearch.setPageIndex(page - 1);
        } else {
            itemSearch.setPageIndex(0);
        }
        itemSearch.setPageSize(50);

        DataPage<Item> itemPage = itemService.searchMongo(itemSearch);
        for (Item item : itemPage.getData()) {
            if (item.getCategoryId() != null && !item.getCategoryId().equals("") && !cateIds.contains(item.getCategoryId())) {
                cateIds.add(item.getCategoryId());
            }
            if (item.getShopCategoryId()!= null && !item.getShopCategoryId().equals("") && !shopCateIds.contains(item.getShopCategoryId())) {
                shopCateIds.add(item.getShopCategoryId());
            }
            if (item.getManufacturerId() != null && !item.getManufacturerId().equals("") && !manufIds.contains(item.getManufacturerId())) {
                manufIds.add(item.getManufacturerId());
            }
            if (item.getModelId() != null && !item.getModelId().equals("") && !modelIds.contains(item.getModelId())) {
                modelIds.add(item.getModelId());
            }

        }
        for (Item item : itemPage.getData()) {
            List<String> images = new ArrayList<>();
            if (item != null && item.getImages() != null && !item.getImages().isEmpty()) {
                for (String img : item.getImages()) {
                    images.add(imageService.getUrl(img).thumbnail(200, 200, "outbound").getUrl(item.getName()));
                }
                item.setImages(images);
            }
            if (item.getSellerName() == null || item.getSellerName().equals("")) {
                try {
                    User seller = userService.get(item.getSellerId());
                    item.setSellerName(seller.getEmail());
                } catch (Exception ex) {
                }
            }
        }
        List<City> cities = cityService.list();
        modelMap.put("itemSearch", itemSearch);
        modelMap.put("itemPage", itemPage);
        modelMap.put("cities", cities);
        modelMap.put("itemCates", categoryService.getCategories(cateIds));
        modelMap.put("itemShopCates", shopCategoryService.get(shopCateIds));
        modelMap.put("itemManuf", manufacturerService.getManufacturers(manufIds));
        modelMap.put("itemModels", modelService.getModels(modelIds));
        modelMap.put("clientScript", "category = " + gson.toJson(categoryService.getChilds(null)) + ",cities=" + gson.toJson(cities) + ";reviewitem.init({parentCategorys:" + gson.toJson(parentCategorys) + ", categoryId:'" + (itemSearch.getCategoryIds().isEmpty() ? "" : itemSearch.getCategoryIds().get(0)) + "'});");
        return "cp.item.review";
    }

    /**
     * TÃ¬m kiáº¿m táº¥t cáº£ sáº£n pháº©m
     *
     * @param itemSearch
     * @param session
     * @param modelMap
     * @return
     */
    @RequestMapping(value = {"/reviewitem"}, method = RequestMethod.POST)
    public String reviewSearch(@ModelAttribute("itemSearch") ItemSearch itemSearch,
            HttpSession session,
            ModelMap modelMap) {
        Map<String, Object> parentCategorys = new HashMap<>();
        List<String> cateIds = new ArrayList<>();
        List<String> shopCateIds = new ArrayList<>();
        List<String> manufIds = new ArrayList<>();
        List<String> modelIds = new ArrayList<>();

        itemSearch.setPageIndex(0);
        itemSearch.setPageSize(50);
        if (itemSearch.getCategoryId() != null && !itemSearch.getCategoryId().equals("")) {
            itemSearch.setCategoryIds(new ArrayList<String>());
            itemSearch.getCategoryIds().add(itemSearch.getCategoryId());
        }
        getAncestors(itemSearch, parentCategorys);
        session.setAttribute("itemSearch", itemSearch);
        DataPage<Item> itemPage = itemService.searchMongo(itemSearch);
        for (Item item : itemPage.getData()) {
            if (item.getCategoryId() != null && !item.getCategoryId().equals("") && !cateIds.contains(item.getCategoryId())) {
                cateIds.add(item.getCategoryId());
            }
            if (item.getShopCategoryId()!= null && !item.getShopCategoryId().equals("") && !shopCateIds.contains(item.getShopCategoryId())) {
                shopCateIds.add(item.getShopCategoryId());
            }
            if (item.getManufacturerId() != null && !item.getManufacturerId().equals("") && !manufIds.contains(item.getManufacturerId())) {
                manufIds.add(item.getManufacturerId());
            }
            if (item.getModelId() != null && !item.getModelId().equals("") && !modelIds.contains(item.getModelId())) {
                modelIds.add(item.getModelId());
            }

        }
        for (Item item : itemPage.getData()) {
            List<String> images = new ArrayList<>();
            if (item != null && item.getImages() != null && !item.getImages().isEmpty()) {
                for (String img : item.getImages()) {
                    images.add(imageService.getUrl(img).thumbnail(200, 200, "outbound").getUrl(item.getName()));
                }
                item.setImages(images);
            }
            if (item.getSellerName() == null || item.getSellerName().equals("")) {
                try {
                    User seller = userService.get(item.getSellerId());
                    item.setSellerName(seller.getEmail());
                } catch (Exception ex) {
                }
            }
        }
        if (itemSearch.getCategoryIds() == null) {
            itemSearch.setCategoryIds(new ArrayList<String>());
        }
        List<City> cities = cityService.list();
        modelMap.put("itemSearch", itemSearch);
        modelMap.put("itemPage", itemPage);
        modelMap.put("cities", cities);
        modelMap.put("itemCates", categoryService.getCategories(cateIds));
        modelMap.put("itemShopCates", shopCategoryService.get(shopCateIds));
        modelMap.put("itemManuf", manufacturerService.getManufacturers(manufIds));
        modelMap.put("itemModels", modelService.getModels(modelIds));
        modelMap.put("clientScript", "category = " + gson.toJson(categoryService.getChilds(null)) + ",cities=" + gson.toJson(cities) + ";reviewitem.init({parentCategorys:" + gson.toJson(parentCategorys) + ", categoryId:'" + (itemSearch.getCategoryIds().isEmpty() ? "" : itemSearch.getCategoryIds().get(0)) + "'});");
        return "cp.item.review";
    }

    public void getAncestors(ItemSearch itemSearch, Map<String, Object> parentCategorys) {
        List<Object> cats = new ArrayList<>();
        if (itemSearch.getCategoryIds() == null || itemSearch.getCategoryIds().isEmpty()) {
            parentCategorys.put("cats", cats);
        } else {
            try {
                List<Category> ancestors = categoryService.getAncestors(itemSearch.getCategoryIds().get(0));
                for (Category cat : ancestors) {
                    cats.add(categoryService.getChilds(cat.getId()));
                }
                parentCategorys.put("cats", cats);
                parentCategorys.put("ancestors", ancestors);
            } catch (Exception ex) {
            }
        }
    }
}
