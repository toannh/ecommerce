package vn.chodientu.controller.cp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import vn.chodientu.entity.db.LandingNew;
import vn.chodientu.entity.db.LandingNewSlide;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.service.LandingNewService;

@Controller("cpLandingNew")
@RequestMapping("/cp/landingnew")
public class LandingNewController extends BaseCp {

    @Autowired
    private LandingNewService landingNewService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(ModelMap map, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "landingid", defaultValue = "") String landingid) throws Exception {
        if (page > 0) {
            page = page - 1;
        }
        if (landingid == null || landingid.equals("")) {
            DataPage<LandingNew> dataPage = landingNewService.getAll(new PageRequest(page, 50));
            map.put("dataPage", dataPage);
            map.put("clientScript", "landingnew.init();");
            return "cp.landingnew.list";
        } else {
            DataPage<LandingNewSlide> dataPage = landingNewService.getLandingNewSlide(new PageRequest(page, 50),landingid);
            map.put("landingid", landingid);
            map.put("dataPage", dataPage);
            map.put("clientScript", "landingnew.init();");
            return "cp.landingnewslide.list";
        }

    }

}
