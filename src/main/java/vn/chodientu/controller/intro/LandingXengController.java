/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.controller.intro;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Phuongdt
 */
@Controller("landingXengController")
public class LandingXengController {

    @RequestMapping(value = "/kiem-xeng.html")
    public String introLandingxeng() {
        return "intro.landingxeng";
    }
}
