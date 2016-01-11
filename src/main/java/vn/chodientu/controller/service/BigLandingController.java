/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.controller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.chodientu.controller.BaseRest;
import vn.chodientu.entity.db.BigLandingSeller;
import vn.chodientu.entity.output.Response;
import vn.chodientu.service.BigLandingService;

@Controller("serviceBigLanding")
@RequestMapping("/biglanding")
public class BigLandingController extends BaseRest {

    @Autowired
    private BigLandingService bigLandingService;

    @RequestMapping(value = "/getbiglandingpromotion", method = RequestMethod.GET)
    @ResponseBody
    public Response addEmail(@RequestParam String promotionId) {
        BigLandingSeller bigLandingPromotion = bigLandingService.getBigLandingPromotion(promotionId);
        if (bigLandingPromotion == null) {
            return new Response(false, "Không tồn tại chương trình khuyến mãi");
        }
        return new Response(true, "Thông tin BigLanding Promotion", bigLandingPromotion);
    }

}
