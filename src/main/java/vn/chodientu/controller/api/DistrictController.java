package vn.chodientu.controller.api;

import com.google.gson.Gson;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.chodientu.entity.api.Request;
import vn.chodientu.entity.db.District;
import vn.chodientu.entity.db.SellerApi;
import vn.chodientu.entity.db.User;
import vn.chodientu.entity.output.Response;
import vn.chodientu.service.ApiHistoryService;
import vn.chodientu.service.DistrictService;

@Controller("districtApiController")
@RequestMapping(value = "/api/district")
public class DistrictController extends BaseApi {

    @Autowired
    private DistrictService districtService;
    @Autowired
    private Gson gson;
    @Autowired
    private ApiHistoryService apiHistoryService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Response list(@RequestBody Request data, ModelMap model) {
        Response response;
        User user = null;
        try {
            validate(data, model);
            user = (User) model.get("user");
            SellerApi sellerApi = (SellerApi) model.get("sellerApi");
            List<District> districts = districtService.list();

            response = new Response(true, "Danh sách quận huyện", districts);
        } catch (Exception e) {
            response = new Response(false, e.getMessage());
        }
        apiHistoryService.create(data, user, response);
        return response;
    }

}
