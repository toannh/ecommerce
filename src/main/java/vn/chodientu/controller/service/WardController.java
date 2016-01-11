package vn.chodientu.controller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.chodientu.controller.BaseRest;
import vn.chodientu.entity.db.Tag;
import vn.chodientu.entity.input.TagSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.entity.output.Response;
import vn.chodientu.service.TagService;
import vn.chodientu.service.WardService;

/**
 *
 * @author thanhvv
 */
@Controller("serviceWard")
@RequestMapping("/ward")
public class WardController extends BaseRest {

    @Autowired
    private WardService wardService;

    @RequestMapping(value = "/loadwardbydistrict", method = RequestMethod.GET)
    @ResponseBody
    public Response loadwardbydistrict(@RequestParam(value = "districtId", defaultValue = "") String districtId) {
        try {
            return new Response(true,"success",wardService.getAllWardByDistrict(districtId));
        } catch (Exception ex) {
            return new Response(false, ex.getMessage());
        }
    }

}
