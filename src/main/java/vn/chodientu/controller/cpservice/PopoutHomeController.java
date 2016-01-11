/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.controller.cpservice;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.chodientu.controller.BaseRest;
import vn.chodientu.entity.db.PopoutHome;
import vn.chodientu.entity.enu.ImageType;
import vn.chodientu.entity.form.PopoutHomeForm;
import vn.chodientu.entity.output.Response;
import vn.chodientu.service.ImageService;
import vn.chodientu.service.NewsService;
import vn.chodientu.service.PopoutHomeService;

/**
 *
 * @author Phuongdt
 */
@Controller("cpPopoutHomeService")
@RequestMapping(value = "/cpservice/popouthome")
public class PopoutHomeController extends BaseRest {

    @Autowired
    private ImageService imageService;
    @Autowired
    private PopoutHomeService popoutHomeService;

    /**
     * Thêm tin tức Popout home
     *
     * @param popoutHomeForm
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Response add(@ModelAttribute PopoutHomeForm popoutHomeForm) throws Exception {
        return popoutHomeService.add(popoutHomeForm);
    }

    /**
     * Lấy thông tin Popout home theo ID
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public Response get(@RequestParam String id) throws Exception {
        try {
            Response get = popoutHomeService.get(id); 
            PopoutHome data = (PopoutHome) get.getData();
            List<String> img = imageService.get(ImageType.POPOUT_HOME, data.getId());
            if (img != null && !img.isEmpty()) {
                data.setImage(imageService.getUrl(img.get(0)).compress(100).getUrl(data.getTitle()));
            }
            return new Response(true, null, data);
        } catch (Exception e) {
            return new Response(false, e.getMessage());
        }

    }

    /**
     * Sửa thông tin Popout home
     *
     * @param popoutHomeForm
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public Response edit(@ModelAttribute PopoutHomeForm popoutHomeForm) throws Exception {
        return popoutHomeService.edit(popoutHomeForm);

    }

    /**
     * Sửa trạng thái Popout home
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/editstatus", method = RequestMethod.GET)
    @ResponseBody
    public Response editstatus(@RequestParam String id) throws Exception {
        return popoutHomeService.changeActive(id);

    }

    /**
     * Del Popout home
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/del", method = RequestMethod.GET)
    @ResponseBody
    public Response del(@RequestParam String id) throws Exception {
        return popoutHomeService.del(id);

    }
    
    

}
