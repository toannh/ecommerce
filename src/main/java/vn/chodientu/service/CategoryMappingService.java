/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import vn.chodientu.entity.db.Category;
import vn.chodientu.entity.db.CategoryMapping;
import vn.chodientu.entity.db.CategoryProperty;
import vn.chodientu.entity.db.CategoryPropertyValue;
import vn.chodientu.entity.db.Item;
import vn.chodientu.entity.db.ItemProperty;
import vn.chodientu.entity.enu.CategoryMappingType;
import vn.chodientu.entity.enu.PropertyOperator;
import vn.chodientu.entity.enu.PropertyType;
import vn.chodientu.entity.input.CategoryMappingSearch;
import vn.chodientu.entity.input.ItemSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.entity.output.Response;
import vn.chodientu.repository.CategoryMappingRespository;
import vn.chodientu.repository.CategoryPropertyRepository;
import vn.chodientu.repository.CategoryPropertyValueRepository;
import vn.chodientu.repository.ItemPropertyRepository;

/**
 *
 * @author CANH
 */
@Service
public class CategoryMappingService {

    @Autowired
    private CategoryMappingRespository categoryMappingRespository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryPropertyRepository categoryPropertyRepository;
    @Autowired
    private CategoryPropertyValueRepository categoryPropertyValueRepository;
    @Autowired
    private AdministratorService adminService;

    public Response mapCate(String userId, String orgiCateId, String destCateId, boolean active, boolean checkNew, String newPropName) throws Exception {
        Category orgiCate;
        Category destCate;
        CategoryMapping cateMapping = categoryMappingRespository.findByOrigCate(orgiCateId);
        if (cateMapping != null) {
            return new Response(false, "Không thể tạo thêm lệnh map danh mục cho Cate nguồn này. <\br>"
                    + "Cate nguồn này đang đợi lệnh MAPPING khác tới Danh mục : " + cateMapping.getDestCategoryId(), cateMapping);
        } else {
            cateMapping = new CategoryMapping();
            cateMapping.setActive(active);
            cateMapping.setUserId(userId);
            cateMapping.setCreateTime(System.currentTimeMillis());
            cateMapping.setOriginCategoryId(orgiCateId);
            cateMapping.setDestCategoryId(destCateId);
            cateMapping.setRunning(CategoryMappingType.EVERRUN);
        }
        try {
            orgiCate = categoryService.get(orgiCateId);
        } catch (Exception ex) {
            return new Response(false, "Không tìm thấy cate Nguồn", orgiCateId);
        }
        try {
            destCate = categoryService.get(destCateId);
        } catch (Exception ex) {
            return new Response(false, "Không tìm thấy cate Đích", destCateId);
        }
        //
        if (checkNew) {
            HashMap<String, String> newMapProp = new HashMap<>();
            CategoryProperty newCateProp = categoryPropertyRepository.find(destCateId, newPropName);
            if (newCateProp == null) {
                newCateProp = new CategoryProperty();
                newCateProp.setCategoryId(destCateId);
                newCateProp.setName(newPropName);
                newCateProp.setType(PropertyType.INPUT);
                newCateProp.setFilter(true);
                newCateProp.setOperator(PropertyOperator.EQ);
                categoryPropertyRepository.save(newCateProp);
                newCateProp = categoryPropertyRepository.find(newCateProp.getCategoryId(), newCateProp.getName());
                //
                CategoryPropertyValue newCatePropVal = new CategoryPropertyValue();
                newCatePropVal.setName(orgiCate.getName());
                newCatePropVal.setCategoryId(destCateId);
                newCatePropVal.setCategoryPropertyId(newCateProp.getId());
                newCatePropVal.setFilter(true);
                newCatePropVal.setValue(1);
                newCatePropVal.setPosition(0);
                categoryPropertyValueRepository.save(newCatePropVal);
                newCatePropVal = categoryPropertyValueRepository.find(newCatePropVal.getCategoryId(), newCatePropVal.getCategoryPropertyId(), newCatePropVal.getName());
                //
                newMapProp.put(newCateProp.getId(), newCatePropVal.getId());
            } else {
                CategoryPropertyValue newCatePropVal = categoryPropertyValueRepository.find(destCateId, newCateProp.getId(), destCate.getName());
                if (newCatePropVal == null) {
                    newCatePropVal = new CategoryPropertyValue();
                    newCatePropVal.setName(orgiCate.getName());
                    newCatePropVal.setCategoryId(destCateId);
                    newCatePropVal.setCategoryPropertyId(newCateProp.getId());
                    newCatePropVal.setFilter(true);
                    newCatePropVal.setValue(1);
                    newCatePropVal.setPosition(0);
                    categoryPropertyValueRepository.save(newCatePropVal);
                }
                newCatePropVal = categoryPropertyValueRepository.find(newCatePropVal.getCategoryId(), newCatePropVal.getCategoryPropertyId(), newCatePropVal.getName());
                newMapProp.put(newCateProp.getId(), newCatePropVal.getId());
            }
            cateMapping.setNewProperty(newMapProp);
        }
        //
        List<CategoryProperty> listOrgiCateProp = categoryPropertyRepository.getByCategory(orgiCateId);
        List<CategoryProperty> listDestCateProp = categoryPropertyRepository.getByCategory(destCateId);
        List<CategoryPropertyValue> listOrgiCatePropValues = categoryPropertyValueRepository.getByCategory(orgiCateId);
        List<CategoryPropertyValue> listDestCatePropValues = categoryPropertyValueRepository.getByCategory(destCateId);
        //
        Map<String, String> listSameCateProp = new HashMap<>();
        Map<String, String> listPropertiesValues = new HashMap<>();
        // get Same cate (by name)
        for (CategoryProperty tempOrCateProp : listOrgiCateProp) {
            for (CategoryProperty tempDesCateProp : listDestCateProp) {
                if (tempOrCateProp.getName().toLowerCase().trim().equals(tempDesCateProp.getName().toLowerCase().trim())) {
                    listSameCateProp.put(tempOrCateProp.getId(), tempDesCateProp.getId());
                }
            }
        }
        //get mapp property value (same property cate and property value)
        for (CategoryPropertyValue tempOrCatePropVal : listOrgiCatePropValues) {
            String destSameCatePropId = listSameCateProp.get(tempOrCatePropVal.getCategoryPropertyId());

            if (destSameCatePropId != null && !destSameCatePropId.equals("")) {
                boolean checkValEquals = false;
                for (CategoryPropertyValue tempDesCatePropVal : listDestCatePropValues) {
                    // filter same categoryProperty
                    if (tempDesCatePropVal.getCategoryPropertyId().equals(destSameCatePropId)) {
                        String orgVal = tempOrCatePropVal.getName().toLowerCase().trim();
                        String destVal = tempDesCatePropVal.getName().toLowerCase().trim();
                        // if same value
                        if (orgVal.equals(destVal)) {
                            listPropertiesValues.put(tempOrCatePropVal.getId(), tempDesCatePropVal.getId());
                            checkValEquals = true;
                            break;
                        }
                    }
                }
                if (!checkValEquals) {
                    listPropertiesValues.put(tempOrCatePropVal.getId(), tempOrCatePropVal.getId());
                }
            }
            // update cateid for cateproperty and catepropertyVal   
        }
        cateMapping.setSameProperties(listSameCateProp);
        cateMapping.setListPropertiesValues(listPropertiesValues);
        categoryMappingRespository.save(cateMapping);
        return new Response(true, "Lệnh mapping danh mục đã được xác nhận", cateMapping);
    }

    public DataPage<CategoryMapping> search(CategoryMappingSearch cateMapSearch) {
        String userId = cateMapSearch.getUserId();
        String oriCate = cateMapSearch.getOriginCategoryId();
        String destCate = cateMapSearch.getDestCategoryId();
        int state = cateMapSearch.getRunning();
        int status = cateMapSearch.getActive();
        int pageIndex = cateMapSearch.getPageIndex();
        int pageSize = cateMapSearch.getPageSize();

        Criteria cri = new Criteria();
        if (userId != null && !userId.equals("")) {
            cri.and("userId").is(userId);
        }
        if (oriCate != null && !oriCate.equals("")) {
            cri.and("originCategoryId").is(oriCate);
        }
        if (destCate != null && !destCate.equals("")) {
            cri.and("destCategoryId").is(destCate);
        }
        switch (state) {
            case 1: //everrun
                cri.and("running").is(CategoryMappingType.EVERRUN);
                break;
            case 2: //running
                cri.and("running").is(CategoryMappingType.RUNNING);
                break;
            case 3: //fail
                cri.and("running").is(CategoryMappingType.FAIL);
                break;
            case 4: //success
                cri.and("running").is(CategoryMappingType.SUCCESS);
                break;
            default: //success
                break;
        }
        switch (status) {
            case 0: //everrun
                cri.and("active").is(false);
                break;
            case 1: //running
                cri.and("active").is(true);
                break;
            default: //success
                break;
        }
        if (pageIndex <= 0) {
            pageIndex = 0;
        }
        if (pageSize <= 0) {
            pageSize = 50;
        }
        Query query = new Query(cri);
        query.skip(pageIndex * pageSize).limit(pageSize);
        query.with(new Sort(Sort.Direction.DESC, "createTime"));
        //
        DataPage<CategoryMapping> dataPage = new DataPage<>();
        dataPage.setData(categoryMappingRespository.find(query));
        dataPage.setPageIndex(pageIndex);
        dataPage.setPageSize(pageSize);
        dataPage.setDataCount(categoryMappingRespository.count(query));
        dataPage.setPageCount(dataPage.getDataCount() / pageSize);
        try {

            for (CategoryMapping catemapp : dataPage.getData()) {
                catemapp.setOriCate(categoryService.get(catemapp.getOriginCategoryId()));
                catemapp.setDestCate(categoryService.get(catemapp.getDestCategoryId()));
                catemapp.setUser(adminService.getAdministrator(catemapp.getUserId()));
                catemapp.setListSameCateProp(categoryPropertyRepository.get((String[]) catemapp.getSameProperties().keySet().toArray()));
            }
        } catch (Exception ex) {
            return dataPage;
        }
        return dataPage;

    }

    public Response changeActive(String id) {
        CategoryMapping cateMapp = categoryMappingRespository.find(id);
        if (cateMapp == null) {
            return new Response(true, "Lệnh map danh mục không tồn tại", id);
        }
        cateMapp.setActive(!cateMapp.isActive());
        categoryMappingRespository.save(cateMapp);
        return new Response(true, cateMapp.isActive() ? "Kích hoạt thành công." : "Hủy kích hoạt thành công.", cateMapp);
    }

}
