/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.controller.intro;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.chodientu.controller.user.BaseUser;

/**
 *
 * @author Phuongdt
 */
@Controller("LandingIphone6Controller")
public class LandingIphone6Controller extends BaseUser {

    @RequestMapping(value = "/thang-12-ky-dieu-dau-gia-tu-thien-iphone6plus.html")
    public String introLandingxeng(ModelMap model) {
         model.put("clientScript", "$('body').addClass('bg-white');");
        return "intro.landingIphone6";
    }
}
