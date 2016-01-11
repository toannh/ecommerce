/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.controller.cp;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.chodientu.controller.BaseRest;
import vn.chodientu.entity.db.CategoryMapping;
import vn.chodientu.entity.input.CategoryMappingSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.entity.output.Response;
import vn.chodientu.service.CategoryMappingService;

/**
 *
 * @author CANH
 */
@Controller("serviceCategoryMapping")
@RequestMapping("/cp")
public class CategoryMappingController extends BaseCp {

    @Autowired
    private CategoryMappingService categoryMappingService;

    @RequestMapping(value = "/mapcate", method = RequestMethod.POST)
    public String postMapCate(@ModelAttribute CategoryMappingSearch mapCateSearch, ModelMap modelMap) {
        // call mapcate service
        CategoryMappingSearch categoryMappingSearch = mapCateSearch;
        categoryMappingSearch.setPageSize(30);
        DataPage<CategoryMapping> data = categoryMappingService.search(categoryMappingSearch);
        modelMap.put("dataPage", data);
        modelMap.put("categoryMappingSearch", categoryMappingSearch);
        return "cp.mapcate.list";
//        return new Response(true, "Lệnh map danh mục đã được xác nhận.", null);
    }

    @RequestMapping(value = "/mapcate")
    public String getMapCate(ModelMap modelMap) {
        // call mapcate service
        CategoryMappingSearch categoryMappingSearch = new CategoryMappingSearch();
        categoryMappingSearch.setPageSize(30);
        DataPage<CategoryMapping> data = categoryMappingService.search(categoryMappingSearch);
        modelMap.put("dataPage", data);
        modelMap.put("categoryMappingSearch", categoryMappingSearch);
        modelMap.put("clientScript", "mapcate.init();");
        return "cp.mapcate.list";
    }

}
