package vn.chodientu.controller.cp;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import vn.chodientu.entity.db.PopoutHome;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.service.CityService;
import vn.chodientu.service.DistrictService;
import vn.chodientu.service.ImageService;
import vn.chodientu.service.PopoutHomeService;

@Controller("cpPopoutHome")
@RequestMapping("/cp/popouthome")
public class PopoutHomeController extends BaseCp {

    @Autowired
    private PopoutHomeService popoutHomeService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private CityService cityService;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private Gson gson;

    @RequestMapping(method = RequestMethod.GET)
    public String list(ModelMap map, @RequestParam(value = "page", defaultValue = "0") int page) throws Exception {
        if (page > 0) {
            page = page - 1;
        }
        DataPage<PopoutHome> dataPage = popoutHomeService.getAll(new PageRequest(page, 30));
        map.put("dataPage", dataPage);
        map.put("clientScript", "popouthome.init();"); 
        return "cp.popouthome";
    }

}
