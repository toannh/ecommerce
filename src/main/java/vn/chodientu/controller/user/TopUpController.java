package vn.chodientu.controller.user;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import vn.chodientu.entity.db.TopUp;
import vn.chodientu.entity.input.TopUpSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.service.TopUpService;

@Controller
@RequestMapping("/user")
public class TopUpController extends BaseUser {

    @Autowired
    private TopUpService topUpService;

    @RequestMapping("/topup-by-cash.html")
    public String topUp(ModelMap map, HttpSession session, @RequestParam(value = "page", defaultValue = "1") int page) {
        if (viewer == null || viewer.getUser() == null) {
            return "redirect:/user/signin.html?ref=" + baseUrl + "/user/topup-by-cash.html";
        }

        TopUpSearch topUpSearch = new TopUpSearch();
        if (session.getAttribute("topUpSearch") != null && page != 0) {
            topUpSearch = (TopUpSearch) session.getAttribute("topUpSearch");
        } else {
            session.setAttribute("topUpSearch", topUpSearch);
        }
        topUpSearch.setUserId(viewer.getUser().getId());
        topUpSearch.setPageIndex(page - 1);
        topUpSearch.setPageSize(100);

        DataPage<TopUp> dataPage = topUpService.search(topUpSearch);

        map.put("dataPage", dataPage);
        map.put("clientScript", "topup.init();");
        return "user.topup";
    }

}
