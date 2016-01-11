/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.controller.cpservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.chodientu.controller.BaseRest;
import vn.chodientu.entity.db.SellerEmailMarketing;
import vn.chodientu.entity.output.Response;
import vn.chodientu.service.SellerMarketingService;

/**
 *
 * @author PhucTd
 */
@Controller("cpEmailMarketingService")
@RequestMapping("/cpservice/emailmarketing")
public class SellerEmailMarketingController extends BaseRest {

    @Autowired
    private SellerMarketingService sellerMarketing;

    @RequestMapping(value = "/changeactiveemail", method = RequestMethod.GET)
    @ResponseBody
    public Response changeActiveEmail(@RequestParam(value = "id", defaultValue = "") String id) throws Exception {
        if (viewer == null || viewer.getAdministrator() == null) {
            return new Response(false, "Bạn không phải người quản trị!");
        }
        return sellerMarketing.activeEmailMarketing(viewer.getAdministrator(), id);
    }

    @RequestMapping(value = "/preview", method = RequestMethod.POST)
    @ResponseBody
    public Response previewEmail(@RequestBody SellerEmailMarketing email) throws Exception {
        if (viewer == null || viewer.getAdministrator() == null) {
            return new Response(false, "Bạn phải đăng nhập trước !");
        }
        String template = sellerMarketing.getTemplate(email, email.getTemplate());
        return new Response(true, "ok", template);
    }
}
