package vn.chodientu.controller.cp;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import vn.chodientu.entity.db.AdministratorRole;
import vn.chodientu.entity.db.Model;
import vn.chodientu.entity.db.ModelSeo;
import vn.chodientu.entity.db.Shop;
import vn.chodientu.entity.input.ModelSeoSearch;
import vn.chodientu.entity.input.ShopSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.service.CategoryService;
import vn.chodientu.service.ManufacturerService;
import vn.chodientu.service.ModelService;

@Controller("modelSeo")
public class ModelSeoController extends BaseCp {

    @Autowired
    private ManufacturerService manufacturerService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private Gson gson;

    /**
     * Danh sách tất cả các model
     *
     * @param map
     * @param session
     * @param page
     * @return
     */
    @RequestMapping(value = "/cp/modelseo", method = RequestMethod.GET)
    public String list(ModelMap map,
            HttpSession session,
            @RequestParam(value = "page", defaultValue = "0") int page) throws Exception {
        ModelSeoSearch search = new ModelSeoSearch();
        if (session.getAttribute("modelSeoSearch") != null && page != 0) {
            search = (ModelSeoSearch) session.getAttribute("modelSeoSearch");
        } else {
            session.setAttribute("modelSeoSearch", search);
        }
        if (page > 0) {
            search.setPageIndex(page - 1);
        } else {
            search.setPageIndex(0);
        }
        search.setPageSize(20);

        List<String> modelIds = new ArrayList<>();
        DataPage<ModelSeo> dataPage = modelService.searchModelSeo(search);
        if (dataPage.getData() != null && !dataPage.getData().isEmpty()) {
            for (ModelSeo modelSeo : dataPage.getData()) {
                if (!modelIds.contains(modelSeo.getModelId())) {
                    modelIds.add(modelSeo.getModelId());
                }
            }
            List<Model> models = modelService.getModels(modelIds);
            for (ModelSeo modelSeo : dataPage.getData()) {
                for (Model model : models) {
                    if (model.getId().equals(modelSeo.getModelId())) {
                        modelSeo.setName(model.getName());
                    }
                }
            }
        }

        map.put("dataPage", dataPage);
        map.put("modelSeoSearch", search);
        map.put("clientScript", "modelseo.init();");
        return "cp.modelseo";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String search(ModelMap map, HttpSession session, @ModelAttribute ModelSeoSearch modelSeoSearch) throws Exception {
        session.setAttribute("modelSeoSearch", modelSeoSearch);
        modelSeoSearch.setPageIndex(0);
        modelSeoSearch.setPageSize(20);
        List<String> modelIds = new ArrayList<>();
        DataPage<ModelSeo> dataPage = modelService.searchModelSeo(modelSeoSearch);
        if (dataPage.getData() != null && !dataPage.getData().isEmpty()) {
            for (ModelSeo modelSeo : dataPage.getData()) {
                if (!modelIds.contains(modelSeo.getModelId())) {
                    modelIds.add(modelSeo.getModelId());
                }
            }
            List<Model> models = modelService.getModels(modelIds);
            for (ModelSeo modelSeo : dataPage.getData()) {
                for (Model model : models) {
                    if (model.getId().equals(modelSeo.getModelId())) {
                        modelSeo.setName(model.getName());
                    }
                }
            }
        }

        map.put("dataPage", dataPage);
        map.put("modelSeoSearch", modelSeoSearch);
        map.put("clientScript", "modelseo.init();");
        return "cp.modelseo";
    }

}
