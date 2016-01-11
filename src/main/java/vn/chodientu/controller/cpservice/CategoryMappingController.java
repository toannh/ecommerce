/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.controller.cpservice;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.chodientu.controller.BaseRest;
import vn.chodientu.entity.db.Category;
import vn.chodientu.entity.db.CategoryMapping;
import vn.chodientu.entity.output.Response;
import vn.chodientu.entity.web.Viewer;
import vn.chodientu.repository.CategoryMappingRespository;
import vn.chodientu.repository.CategoryRepository;
import vn.chodientu.service.CategoryMappingService;

/**
 *
 * @author CANH
 */
@Controller("cpCategoryMappingService")
@RequestMapping(value = "/cpservice/mapcate")
public class CategoryMappingController extends BaseRest {

    @Autowired
    CategoryMappingRespository categoryMappingRespository;
    @Autowired
    CategoryMappingService categoryMappingService;
    @Autowired
    CategoryRepository categoryRepository;

    @RequestMapping(value = "/submitAdd", method = RequestMethod.POST)
    @ResponseBody
    public Response add(@RequestBody Map<String, String> listParam) throws Exception {
        String oriCate = listParam.get("oriCate");
        String destCate = listParam.get("destCate");
        boolean check = Boolean.parseBoolean(listParam.get("newprop"));
        String newPropName = listParam.get("newPropName");
        int active = Integer.parseInt(listParam.get("active"));
        if (oriCate == null || oriCate.equals("")) {
            return new Response(false, "Bạn chưa chọn danh mục nguồn!");
        }
        if (destCate == null || destCate.equals("")) {
            return new Response(false, "Bạn chưa chọn danh mục đích!");
        }
        if (check) {
            if (newPropName == null || newPropName.equals("")) {
                return new Response(false, "Bạn chưa điền tên thuộc tính mới!");
            }
        }
        Category oriCategory = categoryRepository.find(oriCate);
        Category destCategory = categoryRepository.find(destCate);
        if (oriCategory == null || destCategory == null) {
            return new Response(false, "Danh mục nguồn / danh mục đích không tồn tại!");
        } else {
            if (!(oriCategory.isLeaf() && destCategory.isLeaf())) {
                return new Response(false, "Danh mục nguồn và danh mục đích phải là cấp lá!");
            }
        }
        String adminId = viewer.getAdministrator().getId();
        return categoryMappingService.mapCate(adminId, oriCate, destCate, active == 1, check, newPropName);
    }

    @RequestMapping(value = "/changeActive", method = RequestMethod.POST)
    @ResponseBody
    public Response changeActive(@RequestBody Map<String, String> listParam) {
        String cateMappId = listParam.get("id");
//        boolean active = Boolean.parseBoolean(listParam.get("active"));
        if (cateMappId == null) {
            return new Response(false, "Chưa chọn lệnh map danh mục tương ứng");
        }
        return categoryMappingService.changeActive(cateMappId);
    }

}
