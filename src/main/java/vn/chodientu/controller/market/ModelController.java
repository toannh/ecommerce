package vn.chodientu.controller.market;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import vn.chodientu.component.imboclient.Url.ImageUrl;
import vn.chodientu.entity.db.Category;
import vn.chodientu.entity.db.City;
import vn.chodientu.entity.db.Item;
import vn.chodientu.entity.db.Manufacturer;
import vn.chodientu.entity.db.Model;
import vn.chodientu.entity.db.ModelSeo;
import vn.chodientu.entity.input.ItemSearch;
import vn.chodientu.entity.input.ModelSearch;
import vn.chodientu.entity.input.PropertySearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.service.CategoryService;
import vn.chodientu.service.CityService;
import vn.chodientu.service.ImageService;
import vn.chodientu.service.ItemService;
import vn.chodientu.service.ManufacturerService;
import vn.chodientu.service.ModelReviewService;
import vn.chodientu.service.ModelService;
import vn.chodientu.util.TextUtils;

/**
 * @since Jun 16, 2014
 * @author Phu
 */
@Controller("modelController")
public class ModelController extends BaseMarket {
    
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private CityService cityService;
    @Autowired
    private ManufacturerService manufacturerService;
    @Autowired
    private ModelReviewService modelReviewService;
    @Autowired
    private Gson gson;
    
    @RequestMapping({"/model/{mid}/{name}.html"})
    public String detail(@PathVariable("mid") String mid, @PathVariable("name") String name, @RequestParam(value = "search", required = false) String search,
            ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
        if (request.getParameterValues("category_id") != null
                || request.getParameterValues("shipping") != null
                || request.getParameterValues("manuf") != null
                || request.getParameterValues("promotion") != null
                || request.getParameterValues("condition") != null
                || request.getParameterValues("order") != null
                || request.getParameterValues("type") != null) {
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.setHeader("Location", baseUrl + "/model/" + mid + "/" + name + ".html");
            return "market.model.detail";
//            return "redirect:/mua-ban/" + cid + "/" + name + ".html";
        }
        if (mid == null || mid.trim().equals("")) {
            return "index.404";
        }
        Model model = new Model();
        Category category = null;
        Manufacturer manufacturer = null;
        try {
            model = modelService.getModel(mid);
            if (model == null) {
                return "index.404";
            }
            String uri = "/model/" + model.getId() + "/" + TextUtils.createAlias(model.getName()) + ".html";
            if (!request.getRequestURI().equals(uri)) {
                String q = request.getQueryString();
                if (q != null && !q.equals("")) {
                    q = "?" + q;
                } else {
                    q = "";
                }
                response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
                response.setHeader("Location", baseUrl + uri + q);
                return "market.model.detail";
                
            }
            manufacturer = manufacturerService.getManufacturer(model.getManufacturerId());
            if (manufacturer != null) {
                model.setManufacturerName(manufacturer.getName());
            }
            List<String> images = new ArrayList<>();
            if (model.getImages() != null && !model.getImages().isEmpty()) {
                for (String img : model.getImages()) {
                    ImageUrl url = imageService.getUrl(img).compress(100);
                    if (url != null && url.getUrl() != null) {
                        images.add(url.getUrl("manufacturer"));
                    }
                }
            }
            model.setImages(images);
            model.setProperties(modelService.getProperties(model.getId()));
            
            category = categoryService.get(model.getCategoryId());
        } catch (Exception ex) {
//            throw new HttpServerErrorException(HttpStatus.NOT_FOUND, ex.getMessage());
            if (name.length() > 10) {
                name = name.substring(0, 10);
            }
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.setHeader("Location", baseUrl + "/s/" + name + ".html");
            return "market.browse.search";
            //return "redirect:" + baseUrl + "/s/" + name + ".html";
        }
        ModelSearch modelSearch = new ModelSearch();
        modelSearch.setManufacturerIds(new ArrayList<String>());
        modelSearch.setProperties(new ArrayList<PropertySearch>());
        modelSearch.setModelId(mid);
        
        List<String> ids = new ArrayList<String>();
        ids.add(mid);
        
        ItemSearch itemSearchHints = new ItemSearch();
        itemSearchHints.setPageIndex(0);
        itemSearchHints.setPageSize(3);
        itemSearchHints.setStatus(1);
        itemSearchHints.setCod(true);
        itemSearchHints.setOnlinePayment(true);
        itemSearchHints.setModelIds(ids);
        DataPage<Item> searchHints = itemService.search(itemSearchHints);
        List<Item> itemHints = searchHints.getData();
        
        ItemSearch itSearch = new ItemSearch();
        itSearch.setManufacturerIds(new ArrayList<String>());
        itSearch.setModelIds(new ArrayList<String>());
        itSearch.setCityIds(new ArrayList<String>());
        itSearch.setProperties(new ArrayList<PropertySearch>());
        
        List<City> list = cityService.list();
        modelMap.put("category", category);
        modelMap.put("cateProperties", categoryService.getProperties(category.getId()));
        modelMap.put("catePropertyValues", categoryService.getPropertyValuesWithCategoryId(category.getId()));
        modelMap.put("parentCategories", categoryService.getCategories(category.getPath()));
        modelMap.put("itemHints", itemHints);
        modelMap.put("model", model);
        modelMap.put("modelSearch", modelSearch);
        modelMap.put("itemSearch", itSearch);
        modelMap.put("review", modelReviewService.checkReview(viewer.getUser() != null ? viewer.getUser().getId() : "", mid));
        modelMap.put("cities", list);
        
        modelMap.put("canonical", "/model/" + model.getId() + "/" + TextUtils.createAlias(model.getName()) + ".html");
        StringBuilder des = new StringBuilder();
        StringBuilder keywords = new StringBuilder();
        des.append(model.getName()).append(" - tại danh mục ");
        try {
            List<Category> ancestors = categoryService.getAncestors(category.getId());
            for (Category c : ancestors) {
                if (c.getParentId() == null || c.getParentId().equals("")) {
                    des.append(c.getName()).append("/");
                    keywords.append(c.getName()).append("/");
                } else {
                    des.append(c.getName()).append(", ");
                    keywords.append(c.getName()).append(", ");
                }
            }
        } catch (Exception ex) {
        }
        des.append("... mọi thứ tại ChợĐiệnTử eBay Việt Nam - Thanh toán online, CoD, giao hàng toàn quốc");
        ModelSeo modelSeo = modelService.getModelSeoOK(model.getId());
        if(modelSeo!=null){
            model.setTitle(modelSeo.getTitle());
            model.setDescription(modelSeo.getDescription());
             modelMap.put("modelSeo", modelSeo);
        }
        if (model.getTitle() == null || model.getTitle().equals("")) {
            modelMap.put("title", model.getName() + " | ChoDienTu.vn");
        } else {
            modelMap.put("title", model.getTitle());
        }
        if (model.getDescription() == null || model.getDescription().equals("")) {
            if (model.getOldMinPrice() > 0 && model.getOldMaxPrice() > 0) {
                String oldMinPrice = String.valueOf(model.getOldMinPrice());
                double priceMin = Double.parseDouble(oldMinPrice);
                String oldMaxPrice = String.valueOf(model.getOldMaxPrice());
                double priceMax = Double.parseDouble(oldMaxPrice);
                modelMap.put("description", model.getName() + " - giá từ " + TextUtils.numberFormat(priceMin) + " đến " + TextUtils.numberFormat(priceMax) + " tại " + model.getItemCount() + " nơi bán tại ChoDienTu.vn");
            } else {
                modelMap.put("description", des);
            }
        } else {
            modelMap.put("description", model.getDescription());
        }
        
        modelMap.put("keywords", keywords);
        if (model.getImages() != null && !model.getImages().isEmpty()) {
            modelMap.put("ogImage", model.getImages().get(0));
        }
        modelMap.put("remarketing", "dynx_itemid: \"\",dynx_pagetype: \"other\",dynx_totalvalue:\"\"");
        modelMap.put("clientScript", "var cities=" + gson.toJson(list) + ";var itemSearch=" + gson.toJson(itSearch) + ";model.init({model:" + gson.toJson(model) + "});");
        return "market.model.detail";
    }
}
