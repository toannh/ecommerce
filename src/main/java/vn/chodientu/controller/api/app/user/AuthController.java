package vn.chodientu.controller.api.app.user;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.apache.commons.lang.RandomStringUtils;
import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.Permission;
import org.brickred.socialauth.SocialAuthManager;
import org.brickred.socialauth.util.SocialAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import vn.chodientu.component.LoadConfig;
import vn.chodientu.controller.api.BaseApi;
import vn.chodientu.controller.user.*;
import vn.chodientu.entity.api.Request;
import vn.chodientu.entity.db.Cash;
import vn.chodientu.entity.db.Shop;
import vn.chodientu.entity.db.User;
import vn.chodientu.entity.enu.Gender;
import vn.chodientu.entity.enu.ImageType;
import vn.chodientu.entity.enu.SellerHistoryType;
import vn.chodientu.entity.form.SignupForm;
import vn.chodientu.entity.form.UserLoginForm;
import vn.chodientu.entity.output.Response;
import vn.chodientu.service.CashService;
import vn.chodientu.service.EmailService;
import vn.chodientu.service.ImageService;
import vn.chodientu.service.LocationService;
import vn.chodientu.service.SellerHistoryService;
import vn.chodientu.service.ShopService;
import vn.chodientu.service.UserService;

/**
 * @since May 15, 2014
 * @author Phu
 */
@Controller("userAppController")
@RequestMapping(value = "/app/user")
public class AuthController extends BaseApi {

    @Autowired
    private UserService userService;
    @Autowired
    private LocationService locationService;
//    @Autowired
    private SocialAuthManager socialAuthManager;
    @Autowired
    private ImageService imageService;
    @Autowired
    private CashService cashService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private SellerHistoryService sellerHistoryService;
    @Autowired
    private LoadConfig config;
    @Autowired
    private Gson gson;
    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    @ResponseBody
    public Response signin(@RequestBody UserLoginForm loginForm,HttpServletResponse response) throws Exception {
        //response.setHeader("Access-Control-Allow-Origin", "*");
        try {
            
            if(!validateApi()){
                return new Response(false, "Bạn không có quyền truy cập!", null);
            }
            //UserLoginForm loginForm = gson.fromJson(data.getParams(),UserLoginForm.class);
            if(loginForm ==null){
                return new Response(false, "Thông tin chưa chính xác");
            }
            return userService.signin(loginForm.getUsername(), loginForm.getPassword());
        } catch (Exception e) {
            return new Response(false, "Đăng nhập ChợĐiệnTử thất bại!", null);
        }
    }
    @RequestMapping(value = "/signinsocial", method = RequestMethod.GET)
    public Response signinSocial(HttpServletResponse response) throws Exception {
        //response.addHeader("Access-Control-Allow-Origin", "*");
        if(!validateApi()){
                return new Response(false, "Bạn không có quyền truy cập!", null);
            }
        SignupForm signupForm = new SignupForm();
            try {
                socialAuthManager= config.getSocail();
                AuthProvider provider = socialAuthManager.connect(SocialAuthUtil.getRequestParametersMap(request));
                String email = provider.getUserProfile().getEmail();
                User user = userService.ssoSignin(email);
                if (user != null) {
                    if (!user.isActive()) {
                        return new Response(false, "Tài khoản " + user.getEmail() + " của bạn đang bị khóa, hãy liên hệ với ban quản trị để biết thêm chi tiết.", user);
                    } else if (!user.isEmailVerified()) {
                        return new Response(false, "Tài khoản " + user.getEmail() + " của bạn chưa được xác nhận email", user);
                    } else {
                        return new Response(true, "Đăng nhập vào ChợĐiệnTử thành công!", user);
                    }
                } else {
                    signupForm.setEmail(email);
                    signupForm.setName(provider.getUserProfile().getFullName());
                    signupForm.setAddress(provider.getUserProfile().getLocation());
                    signupForm.setPassword(RandomStringUtils.randomAlphanumeric(8));
                    signupForm.setConfirm(true);
                    signupForm.setGender(provider.getUserProfile().getGender().equals("male") ? Gender.MALE : provider.getUserProfile().getGender().equals("female") ? Gender.FEMALE : Gender.MALE);

                    Response signupSso = userService.signupSso(signupForm);
                    if (signupSso.isSuccess()) {
//                        viewer.setUser((User) signupSso.getData());
//                        imageService.addTmp(viewer.getUser().getId(), ImageType.AVATAR, provider.getUserProfile().getProfileImageURL());
//                        viewer.getUser().setAvatar(provider.getUserProfile().getProfileImageURL());
                        return new Response(true, "Đăng nhập vào ChợĐiệnTử thành công!", user);
                    }
                    for (String id : socialAuthManager.getConnectedProvidersIds()) {
                        socialAuthManager.disconnectProvider(id);
                    }
                }
            } catch (Exception e) {
            return new Response(false, "Đăng nhập ChợĐiệnTử thất bại!", null);
            }
        return new Response(false, "Đăng nhập ChợĐiệnTử thất bại!", null);
    }
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public Response signup(@RequestBody SignupForm signupForm,HttpServletResponse response) throws Exception {
        //response.addHeader("Access-Control-Allow-Origin", "*");
        if(!validateApi()){
                return new Response(false, "Bạn không có quyền truy cập!", null);
            }
        try {
            //SignupForm signupForm=gson.fromJson(data.getParams(),SignupForm.class);
        return userService.signup(signupForm); 
        } catch (Exception e) {
            return new Response(false, "Đăng ký ChợĐiệnTử thất bại!", null);
        }
    }
    @RequestMapping(value = "/verify", method = RequestMethod.GET)
    public Response verify(@RequestParam("code") String code,HttpServletResponse response) throws Exception {
        //response.addHeader("Access-Control-Allow-Origin", "*");
        if(!validateApi()){
                return new Response(false, "Bạn không có quyền truy cập!", null);
            }
        try {
            userService.verify(code);
            return new Response(true, "Kích hoạt tài khoản thành công!", null);
        } catch (Exception ex) {
            return new Response(false, "Kích hoạt tài khoản không thành công!", null);
        }
    }
    @RequestMapping(value = "/requestverify", method = RequestMethod.POST)
    public Response requestVerify(@RequestParam("email") String email,@RequestParam("password") String password,@RequestParam("captcha") String captcha,HttpServletResponse response) throws Exception {
        //response.addHeader("Access-Control-Allow-Origin", "*");
        if(!validateApi()){
                return new Response(false, "Bạn không có quyền truy cập!", null);
            }
        try {
            Response byEmail = userService.getByEmail(email);
            User user = (User) byEmail.getData();
            if (!user.isEmailVerified()) {
                userService.requestVerify(email, password, captcha);
                return new Response(true, "Yêu cầu gửi lại mail kích hoạt thành công!", null);
            } else {
                return new Response(false, "Có lỗi trong quá trình kích hoạt !", null);
            }
        } catch (Exception ex) {
            return new Response(false, "Có lỗi trong quá trình kích hoạt !", ex.getMessage());
        }
    }
    
}
