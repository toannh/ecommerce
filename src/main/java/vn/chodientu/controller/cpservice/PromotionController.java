/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.controller.cpservice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.chodientu.controller.BaseRest;
import vn.chodientu.entity.db.Lading;
import vn.chodientu.entity.db.PromotionCare;
import vn.chodientu.entity.output.Response;
import vn.chodientu.service.PromotionService;

/**
 *
 * @author Phuongdt
 */
@Controller("cpPromotionService")
@RequestMapping(value = "/cpservice/promotion")
public class PromotionController extends BaseRest {
    
    @Autowired
    private PromotionService promotionService;
    @Autowired
    private Gson gson;
    
    @RequestMapping(value = "/addcare", method = RequestMethod.POST)
    @ResponseBody
    public Response addcare(@RequestBody PromotionCare promotionCare) throws Exception {
        return promotionService.addCare(promotionCare);
    }
    
    @RequestMapping(value = "/editcare", method = RequestMethod.POST)
    @ResponseBody
    public Response editcare(@RequestBody PromotionCare promotionCare) throws Exception {
        return promotionService.editCare(promotionCare);
    }
    
    @RequestMapping(value = "/getcare", method = RequestMethod.GET)
    @ResponseBody
    public Response getcare(@RequestParam String id) throws Exception {
        return promotionService.getCare(id);
    }
    
    @RequestMapping(value = "/getlistcare", method = RequestMethod.POST)
    @ResponseBody
    public Response getcares(@RequestBody List<String> promotionIds) throws Exception {
     
        Map<String, Boolean> listCare = new HashMap<>();
        if (promotionIds != null && !promotionIds.isEmpty()) {
            listCare = promotionService.listCare(promotionIds, viewer.getAdministrator().getEmail());
        }
        return new Response(true, null, listCare);
        
    }
    
    @RequestMapping(value = "/getlistcarebypromotionid", method = RequestMethod.GET)
    @ResponseBody
    public Response getlistcarebypromotionid(@RequestParam String id) throws Exception {
        return promotionService.listCares(id);
    }
    
    @RequestMapping(value = "/getcountlistcare", method = RequestMethod.POST)
    @ResponseBody
    public Response getcountlistcare(@RequestBody List<String> promotionIds) throws Exception {
     
        Map<String, Integer> listCare = new HashMap<>();
        if (promotionIds != null && !promotionIds.isEmpty()) {
            listCare = promotionService.listCareCount(promotionIds);
        }
        return new Response(true, null, listCare);
        
    }
    
}
