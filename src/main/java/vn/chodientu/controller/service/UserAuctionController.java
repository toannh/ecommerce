/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.controller.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.chodientu.controller.BaseRest;
import vn.chodientu.entity.db.Message;
import vn.chodientu.entity.output.Response;
import vn.chodientu.service.ItemService;
import vn.chodientu.service.MessageService;
import vn.chodientu.service.UserAuctionService;

/**
 *
 * @author ThuNguyen
 */
@Controller("serviceUserAuction")
@RequestMapping("/userauction")
public class UserAuctionController extends BaseRest {

    @Autowired
    private UserAuctionService userAuctionService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private ItemService itemService;

    @RequestMapping(value = "/savenote", method = RequestMethod.GET)
    @ResponseBody
    public Response saveNote(@RequestParam(value = "id", defaultValue = "") String id,
            @RequestParam(value = "note", defaultValue = "") String note) {
        try {
            return userAuctionService.note(id, note);
        } catch (Exception ex) {
            return new Response(false, ex.getMessage());
        }
    }
    
    
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public Response del(@RequestBody List<String> userItemIds) {
        try {
            userAuctionService.delete(userItemIds);
            return new Response(true, "ok");
        } catch (Exception ex) {
            return new Response(false, ex.getMessage());
        }
    }
    
    @RequestMapping(value = "/getItemAuction", method = RequestMethod.GET)
    @ResponseBody
    public Response getItemAuction(@RequestParam(value = "id", defaultValue = "") String id) {
        try {
            return new Response(true, "Thông tin order", itemService.get(id));
        } catch (Exception e) {
            return new Response(false, e.getMessage());
        }
    }
    
    @RequestMapping(value = "/getmessagenb", method = RequestMethod.POST)
    @ResponseBody
    public Response sendMessageNB(@RequestBody Message message) throws Exception {
        try {
            return messageService.send(message.getFromEmail(), message.getSubject(), message.getContent(), message.getItemId(), null);
        } catch (Exception e) {
            return new Response(false, null);
        }
    }
}
