/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.entity.enu;

import java.io.Serializable;
/**
 *
 * @author Phu
 */
public enum EmailOutboxType implements Serializable {

    VERIFY, RESETPASSWORD, NEWPASSWORD, SHOP_CONTACT, EMAIL_MARKETING,
    CREATE_ORDER_SELLER, CREATE_ORDER_BUYER, PAYMENT_ORDER_SELLER, PAYMENT_ORDER_BUYER, WELCOME,
    AUCTION_BUYER, AUCTION_SELLER, WARNING_OUTOFSTOCK, WARNING_WRONG_PRICE, TOPUP_CARD_TEL, TOPUP_TEL, CASH_PENALTY,
    COUPON_ORDER_SELLER, MESSAGE_TO_BYUER, GUEST_CHECKOUT,
    AUTO_1,//Sau khi đăng ký làm thành viên CDT
    AUTO_2,//Sau khi NB đăng bán thành công lần đầu và tài khoản sau khi đăng bán vẫn chưa tích hợp NL và SC
    AUTO_3,//Ngay sau đăng bán thành công lần đầu và chưa phải là TK Shop
    AUTO_4,//Ngay sau đăng bán thành công lần đầu
    AUTO_5,//Ngay sau đăng bán thành công lần đầu
    AUTO_6,//Ngay sau đăng bán thành công lần đầu và chưa sử dụng chức năng Khuyến mại
    AUTO_7,//Sau khi người bán tích hợp NL và SC
    AUTO_8,//Sau khi người bán mở Shop
    AUTO_9,//Sau khi nạp xèng 14 ngày mà NB ko uptin
    AUTO_10,//Sau khi nạp xèng 21 ngày mà NB ko mua tin VIP
    AUTO_11,//Sau khi nạp xèng 28 ngày mà NB ko kích hoạt DS KH
    AUTO_12,//Sau khi nạp xèng 35 ngày mà NB ko gửi SMS marketing
    AUTO_13,//Sau khi nạp xèng 42 ngày mà NB ko gửi email marketing
    AUTO_14,//Người bán có đăng nhập trong vòng 12 tháng
    EMAIL_REPORT_SELLER,
    ERROR
}
