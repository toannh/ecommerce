package vn.chodientu.controller.cp;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import vn.chodientu.entity.db.BigLanding;
import vn.chodientu.entity.db.BigLandingCategory;
import vn.chodientu.entity.db.LandingCategory;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.service.BigLandingService;

@Controller("cpBigLanding")
@RequestMapping("/cp/biglanding")
public class BigLandingController extends BaseCp {

    @Autowired
    private BigLandingService bigLandingService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(ModelMap map, @RequestParam(value = "biglandingid", required = false) String biglandingid, @RequestParam(value = "biglandingcate", required = false) String biglandingcate) throws Exception {
        if ((biglandingcate != null && !biglandingcate.equals("")) && (biglandingid != null && !biglandingid.equals(""))) {
            List<BigLandingCategory> categories = bigLandingService.getCategoriesByParent(biglandingcate);
            BigLandingCategory category = bigLandingService.getCategory(biglandingcate);
            BigLanding bigLanding = bigLandingService.getBigLanding(biglandingid);
            map.put("landing", bigLanding);
            map.put("listCategories", categories);
            map.put("categorie", category);
            map.put("clientScript", "biglanding.init();");
            return "cp.biglanding.categorysub";
        } else if (biglandingid != null && !biglandingid.equals("")) {
            List<BigLandingCategory> categories = bigLandingService.getCategories(biglandingid, false);
            BigLanding bigLanding = bigLandingService.getBigLanding(biglandingid);
            map.put("landing", bigLanding);
            map.put("listCategories", categories);
            map.put("biglandingid", biglandingid);
            map.put("clientScript", "biglanding.init();");
            return "cp.biglanding.category";
        } else {
            DataPage<BigLanding> dataPage = bigLandingService.getAll(new PageRequest(0, 50));
            map.put("dataPage", dataPage);
            map.put("clientScript", "biglanding.init();");
            return "cp.biglanding.list";
        }

    }

}
