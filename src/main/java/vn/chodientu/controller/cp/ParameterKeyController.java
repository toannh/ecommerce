/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.controller.cp;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import vn.chodientu.entity.db.ParameterKey;
import vn.chodientu.entity.input.ParameterKeySearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.service.ParameterKeyService;

@Controller("cpParameterKey")
@RequestMapping(value = "/cp/parameterkey")
public class ParameterKeyController extends BaseCp {

    @Autowired
    private ParameterKeyService parameterKeyService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(ModelMap model, HttpSession session, @RequestParam(value = "page", defaultValue = "1") int page) {
        ParameterKeySearch parameterSearch = new ParameterKeySearch();
        parameterSearch.setPageIndex(page - 1);
        parameterSearch.setPageSize(10);
        DataPage<ParameterKey> search = parameterKeyService.search(parameterSearch);
//        List<ParameterKey> parameterkey = parameterKeyService.list();
        model.put("parameter", search);
        model.put("clientScript", "parameter.init();");
        return "cp.parameter";
    }
}
