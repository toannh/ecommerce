/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.controller.cpservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.chodientu.controller.BaseRest;
import vn.chodientu.entity.db.Administrator;
import vn.chodientu.entity.db.Category;
import vn.chodientu.entity.db.CategoryProperty;
import vn.chodientu.entity.db.CategoryPropertyValue;
import vn.chodientu.entity.db.Model;
import vn.chodientu.entity.db.ModelSeo;
import vn.chodientu.entity.form.ModelRequestForm;
import vn.chodientu.entity.output.Response;
import vn.chodientu.repository.CategoryRepository;
import vn.chodientu.service.AdministratorService;
import vn.chodientu.service.CategoryService;
import vn.chodientu.service.ModelService;

/**
 *
 * @author thunt
 */
@Controller("cpModelService")
@RequestMapping(value = "/cpservice/model")
public class ModelController extends BaseRest {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelService modelService;
    @Autowired
    private AdministratorService administratorService;

    /**
     * load category
     *
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "/loadcategory", method = RequestMethod.GET)
    @ResponseBody
    public Response loadCategory(@RequestParam(value = "id", defaultValue = "0") String categoryId) {
        if (!categoryId.equals("0") && !categoryRepository.exists(categoryId)) {
            return new Response(false, "Danh mục không tồn tại");
        }
        if (categoryId.equals("0")) {
            categoryId = null;
        }
        List<Category> childs = categoryService.getChilds(categoryId);
        return new Response(true, "Danh sách danh mục", childs);
    }

    /**
     * Lấy danh sách cate theo catepath
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getcatepath", method = RequestMethod.GET)
    @ResponseBody
    public Response getCatePath(@RequestParam(value = "id", defaultValue = "") String id) throws Exception {
        Category category = categoryService.get(id);
        if (category == null) {
            return new Response(false, "Danh mục không tồn tại");
        }
        List<Category> catePath = new ArrayList<>();
        for (String cateId : category.getPath()) {
            catePath.add(categoryService.get(cateId));
        }
        return new Response(true, "Danh sách danh mục theo Path", catePath);
    }

    /**
     * Thay đổi trạng thái
     *
     * @param id
     * @return
     * @throws java.lang.Exception
     */
    @RequestMapping(value = "/changestatus", method = RequestMethod.GET)
    @ResponseBody
    public Response changeStatus(@RequestParam(value = "id", defaultValue = "") String id) throws Exception {
        return modelService.changeStatus(id);
    }

    /**
     * Yêu cầu sửa
     *
     * @param form
     * @return
     * @throws java.lang.Exception
     */
    @RequestMapping(value = "/disapproved", method = RequestMethod.POST)
    @ResponseBody
    public Response disapproved(@RequestBody ModelRequestForm form) throws Exception {
        String id = "test";
        if (viewer != null && viewer.getAdministrator() != null) {
            id = viewer.getAdministrator().getId();
        }
        Administrator admin = administratorService.findByEmail(form.getAsignedEmail());
        if (admin == null) {
            return new Response(false, "Địa chỉ email người yêu cầu chỉnh sửa không tồn tại", new HashMap<String, String>());
        }
        if (!admin.isActive()) {
            return new Response(false, "Địa chỉ email người yêu cầu chỉnh sửa đang bị khóa", new HashMap<String, String>());
        }
        modelService.disapprove(form.getId(), id, admin.getId(), form.getMessage());
        return new Response(true, "Đã yêu cầu chỉnh sủa");
    }

    /**
     * Index toàn bộ model
     *
     * @return
     */
    @RequestMapping(value = "/index")
    @ResponseBody
    public Response index() {
        modelService.index();
        return new Response(true, "Index thành công");
    }

    /**
     * Xóa toàn bộ index của model
     *
     * @return
     */
    @RequestMapping(value = "/unindex")
    @ResponseBody
    public Response unindex() {
        modelService.preIndex();
        return new Response(true, "Index thành công");
    }

    /**
     * Cập nhật giá sp vào model tương ứng
     *
     * @return
     */
    @RequestMapping(value = "/updateprice")
    @ResponseBody
    public Response updatePrice() {
        modelService.updatePrice();
        return new Response(true, "Index thành công");
    }

    /**
     * Thay đổi cân nặng
     *
     * @param id
     * @return
     * @throws java.lang.Exception
     */
    @RequestMapping(value = "/changeweight", method = RequestMethod.GET)
    @ResponseBody
    public Response changeWeight(@RequestParam(value = "id", defaultValue = "") String id, @RequestParam(value = "weight", defaultValue = "") int weight) throws Exception {
        return modelService.changeWeight(id, weight);
    }

    /**
     * Service Sửa thông tin danh mục hỗ trợ SEO
     *
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateseo", method = RequestMethod.POST)
    @ResponseBody
    public Response updateSEO(@RequestBody ModelSeo model) throws Exception {
        return modelService.updateSEO(model);
    }
    @RequestMapping(value = "/getcatepropertyvalues", method = RequestMethod.GET)
    @ResponseBody
    public Response getCatePropertyValues(@RequestParam(value = "id", defaultValue = "") String id) throws Exception {
        List<CategoryPropertyValue> propertyValuesWithCategoryId = categoryService.getPropertyValuesWithCategoryId(id);
        Map<String, List> map = new HashMap();
        map.put("cateProperties", categoryService.getProperties(id));
        map.put("propertyValuesWithCategory", propertyValuesWithCategoryId);
        return new Response(true, "Thông tin giá trị thuộc tính", map);
        
    }
    
    /**
     * Service Sửa thông tin danh mục hỗ trợ SEO - người duyệt sửa
     *
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updatereviewseo", method = RequestMethod.POST)
    @ResponseBody
    public Response updateReviewSeo(@RequestBody ModelSeo model) throws Exception {
        return modelService.updateReviewSeo(model);
    }
    /***
     * Đánh giá duyệt model seo
     * @param model
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/reviewmodelseo", method = RequestMethod.POST)
    @ResponseBody
    public Response reviewModelSeo(@RequestBody ModelSeo model) throws Exception {
        return new Response(true, "Đánh giá thành công", modelService.reviewModelSeo(model));
    }
    
    @RequestMapping(value = "/getmodelseo", method = RequestMethod.GET)
    @ResponseBody
    public Response getmodelseo(@RequestParam(value = "id", defaultValue = "") String id) throws Exception {
        return new Response(true, "info model seo", modelService.getModelSeo(id));
    }
}
