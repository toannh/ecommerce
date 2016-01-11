package vn.chodientu.controller.service;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.chodientu.controller.user.BaseUser;
import vn.chodientu.entity.api.Request;
import vn.chodientu.entity.output.Response;
import vn.chodientu.service.FacebookService;
import vn.chodientu.service.ImageService;
import vn.chodientu.service.SellerService;

/**
 * @since May 20, 2014
 * @author Phu
 */
@Controller("serviceFacebook")
@RequestMapping("/facebook")
public class FacebookController extends BaseUser {

    @Autowired
    private FacebookService facebookService;
    @Autowired
    private Gson json;

    @RequestMapping(value = "/geturlfacebook", method = RequestMethod.GET)
    @ResponseBody
    public Response geturlfacebook() throws Exception {
        
        return facebookService.getUrl(viewer.getUser().getId());
    }
       

}
