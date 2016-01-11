/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.util;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author CANH
 */
public class ErrorUtils {

    public static Map<String, String> error = new HashMap<>();
    public static Map<String, String> alert = new HashMap<>();

    static {
        error.put("ERR_CRAWL_001", "SourceLink cannot be blank");
        error.put("ERR_CRAWL_002", "Images not validate Array format in Json");
        error.put("ERR_CRAWL_003", "SellPrice must be an Integer");
        error.put("ERR_CRAWL_004", "StartPrice must be an Integer");
        error.put("ERR_CRAWL_005", "CategoryId cannot be blank");
        error.put("ERR_CRAWL_006", "Keyword is not validate");
        error.put("ERR_CRAWL_007", "Seller is not exist");
        error.put("ERR_CRAWL_008", "Auction item can not edit!");
    }

    static {
        alert.put("ALERT_CRAWL_001", "Name is empty");
        alert.put("ALERT_CRAWL_002", "Detail is empty");
        alert.put("ALERT_CRAWL_003", "SellerId is empty");
        alert.put("ALERT_CRAWL_004", "Sell price less than 1.000 VND.");
        alert.put("ALERT_CRAWL_005", "CategoryId is empty");
    }

    public static String getErrorMessage(String errorCode) {
        if (errorCode != null && !errorCode.equals("")) {
            if (error.containsKey(errorCode)) {
                return error.get(errorCode.toUpperCase().trim());
            } else {
                return "ERROR_CODE_NOT_DEFINE";
            }
        } else {
            return "ERROR_CODE IS EMPTY";
        }
    }

    public static String getAlertMessage(String alertCode) {
        if (alertCode != null && !alertCode.equals("")) {
            if (alert.containsKey(alertCode)) {
                return alert.get(alertCode.toUpperCase().trim());
            } else {
                return "ALERT_CODE_NOT_DEFINE";
            }
        } else {
            return "ALERT_CODE IS EMPTY";
        }
    }

}
