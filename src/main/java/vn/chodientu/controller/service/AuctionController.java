/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.controller.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.chodientu.controller.BaseRest;
import vn.chodientu.entity.db.Bid;
import vn.chodientu.entity.db.Item;
import vn.chodientu.entity.db.User;
import vn.chodientu.entity.output.Response;
import vn.chodientu.service.AuctionService;
import vn.chodientu.service.ItemService;
import vn.chodientu.service.UserAuctionService;
import vn.chodientu.service.UserService;

/**
 *
 * @author ThuNguyen
 */
@Controller("serviceAuction")
@RequestMapping("/auction")
public class AuctionController extends BaseRest {

    @Autowired
    private AuctionService auctionService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserAuctionService userAuctionService;
    @Autowired
    private ItemService itemService;

    @RequestMapping(value = "/bid", method = RequestMethod.GET)
    @ResponseBody
    public Response bid(@RequestParam(value = "itemId", defaultValue = "") String itemId,
            @RequestParam(value = "price", defaultValue = "0") long price,
            @RequestParam(value = "auto", defaultValue = "false") boolean auto) {
        try {
            return auctionService.bid(itemId, price, auto);
        } catch (Exception ex) {
            return new Response(false, ex.getMessage());
        }
    }

    @RequestMapping(value = "/bidhistory", method = RequestMethod.GET)
    @ResponseBody
    public Response bid(@RequestParam(value = "itemId", defaultValue = "") String itemId) {
        try {
            List<Bid> bidHistory = auctionService.bidHistory(itemId);
            List<String> userIds = new ArrayList<>();
            for (Bid bid : bidHistory) {
                userIds.add(bid.getBiderId());
            }
            List<User> userByIds = userService.getUserByIds(userIds);
            List<User> users = new ArrayList<User>();

            for (User userById : userByIds) {
                User user = new User();
                user.setId(userById.getId());
                String name = "";
                if (userById.getUsername() != null && !userById.getUsername().equals("")) {
                    name = userById.getUsername();
                } else if (userById.getName() != null && !userById.getName().equals("")) {
                    name = userById.getName();
                } else {
                    name = userById.getEmail();
                }
                Item item = itemService.get(itemId);
                if (item != null) {
                    if (item.getHighestBider() != null && item.getEndTime() < System.currentTimeMillis()) {
                        if (viewer.getUser() != null && viewer.getUser().getId().equals(item.getSellerId())) {
                            user.setName(name);
                            user.setEmail(userById.getEmail());
                            user.setPhone(userById.getPhone());
                            user.setActive(userById.isActive());
                        } else {
                            user.setName(name.substring(0, 1) + "*****" + name.substring(name.length() - 1, name.length()));
                        }
                    } else {
                        user.setName(name.substring(0, 1) + "*****" + name.substring(name.length() - 1, name.length()));
                    }
                }

                users.add(user);
            }

            Map<String, Object> map = new HashMap<>();
            map.put("bidHistory", bidHistory);
            map.put("bider", users);
            map.put("userCount", userAuctionService.countByItem(itemId));
            return new Response(true, "ok", map);
        } catch (Exception ex) {
            return new Response(false, ex.getMessage());
        }
    }

}
