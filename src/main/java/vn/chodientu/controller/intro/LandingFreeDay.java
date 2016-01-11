/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.controller.intro;

import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.chodientu.controller.user.BaseUser;

/**
 *
 * @author Phuongdt
 */
@Controller("LandingFreeDayController")
public class LandingFreeDay extends BaseUser {

    @RequestMapping(value = "/ngay-mien-phi.html")
    public String introLandingFreeDay(ModelMap model,HttpServletResponse httpResponse) {
        httpResponse.addHeader("Cache-Control", "max-age=60");
        model.put("clientScript", "biglanding.ladingfreeday();");
        model.put("canonical", "/ngay-mien-phi.html");
        model.put("title", "Xem chương trình ngày miễn phí giảm giá tới 50% tại Chợ Điện Tử");
        model.put("description", "Xem chương trình ngày miễn phí hơn 100.000 sản phẩm, giảm giá tới 50% tại Chợ Điện Tử - Miễn 100% phí vận chuyển, thanh toán khi nhận hàng, bảo vệ người mua.");
        model.put("keywords", "Ngày miễn phí, bùng nổ mua sắm, ngày khuyến mãi, mua sắm giảm giá 50%");
        return "intro.landingfreeday";
    }
}
