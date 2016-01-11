/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vn.chodientu.component;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.brickred.socialauth.SocialAuthConfig;
import org.brickred.socialauth.SocialAuthManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import vn.chodientu.service.ParameterKeyService;

/**
 *
 * @author Fang Anh
 */
@Service
public class LoadConfig {
 @Autowired
 private ParameterKeyService parameterKeyService;
 @Cacheable(value = "buffercache", key = "'configSocail'")
public SocialAuthManager getSocail(){
        SocialAuthManager socialAuthManager=new SocialAuthManager();
        try {
            SocialAuthConfig socialAuthConfig=SocialAuthConfig.getDefault();
            Properties pAuthConfig = new Properties();
            pAuthConfig.setProperty(parameterKeyService.getValue("sn.google.key", true), parameterKeyService.getValue("sn.google.key", false));
            pAuthConfig.setProperty(parameterKeyService.getValue("sn.google.secret", true), parameterKeyService.getValue("sn.google.secret", false));
            pAuthConfig.setProperty(parameterKeyService.getValue("sn.fb.key", true), parameterKeyService.getValue("sn.fb.key", true));
            pAuthConfig.setProperty(parameterKeyService.getValue("sn.fb.secret", true), parameterKeyService.getValue("sn.fb.secret", true));
            pAuthConfig.setProperty(parameterKeyService.getValue("sn.yahoo.key", true), parameterKeyService.getValue("sn.yahoo.key", false));
            pAuthConfig.setProperty(parameterKeyService.getValue("sn.yahoo.secret", true), parameterKeyService.getValue("sn.yahoo.secret", false));
            socialAuthConfig.setApplicationProperties(pAuthConfig);
            socialAuthConfig.load();
            socialAuthManager.setSocialAuthConfig(socialAuthConfig);
        } catch (Exception ex) {
            Logger.getLogger(LoadConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        return socialAuthManager;
    }
@Cacheable(value = "buffercache", key = "'urlFacebook'")
public String getUrlFacebookApp(){
        try {
            return parameterKeyService.getValue("app.fb.url", false);
            
        } catch (Exception ex) {
            Logger.getLogger(LoadConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
@Cacheable(value = "buffercache", key = "'urlSite'")
public String getUrlSite(){
        try {
            return parameterKeyService.getValue("cdt.url.home_path", false);
            
        } catch (Exception ex) {
            Logger.getLogger(LoadConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
}
