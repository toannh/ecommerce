/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import vn.chodientu.entity.enu.EmailOutboxType;
import vn.chodientu.entity.output.Response;
import vn.chodientu.service.EmailService;

/**
 *
 * @author Fang Anh
 */
@ControllerAdvice
public class CustomeException {

    @Autowired
    private EmailService emailService;
    @Autowired
    private LoadConfig config;

    @ExceptionHandler(Exception.class)
//    @ResponseBody
    public ModelAndView myError(Exception exception) throws Exception {
        System.out.println("----Caught IOException----");

        StringWriter errors = new StringWriter();
        exception.printStackTrace(new PrintWriter(errors));
        String err = errors.toString();
        Map<String, Object> data = new HashMap();
        data.put("username", "Admin");
        data.put("message", err);
        emailService.send(EmailOutboxType.ERROR,
                "development@peacesoft.net",
                "[Exception] "+config.getUrlSite(),
                "message", data);
//        return new Response(false, exception.getMessage());
//        return exception.getMessage();
        ModelAndView mav = new ModelAndView("/500");
		mav.addObject("name", exception.getClass().getSimpleName());
		mav.addObject("message", exception.getMessage());

		return mav;
//                return "market.500";
    }
}
