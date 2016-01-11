/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.controller.cpservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.chodientu.controller.BaseRest;
import vn.chodientu.entity.form.ParameterKeyForm;
import vn.chodientu.entity.output.Response;
import vn.chodientu.service.ParameterKeyService;

/**
 *
 * @author TaiND
 */
@Controller("cpParameterKeyService")
@RequestMapping(value = "/cpservice/parameterkey")
public class ParameterKeyController extends BaseRest {

    @Autowired
    private ParameterKeyService parameterKeyService;
    
    @ResponseBody
    @RequestMapping(value = "/listparameterkey", method = RequestMethod.GET)
    public Response list() {
        return new Response(true, null, parameterKeyService.list());
    }
    
    @RequestMapping(value = "/addparameterkey", method = RequestMethod.POST)
    @ResponseBody
    public Response add(@RequestBody ParameterKeyForm parameterKeyForm) throws Exception {
        return parameterKeyService.add(parameterKeyForm);
    }
    
    @RequestMapping(value = "/editparameterkey", method = RequestMethod.POST)
    @ResponseBody
    public Response edit(@RequestBody ParameterKeyForm parameterKeyForm) throws Exception {
        return parameterKeyService.edit(parameterKeyForm);
    }

    @RequestMapping(value = "/deleteparameterkey", method = RequestMethod.GET)
    @ResponseBody
    public Response delete(@RequestParam(value = "keyConvention", defaultValue = "") String id) {
        return parameterKeyService.delete(id);
    }
    
    @RequestMapping(value = "/getparameterkeybyid", method = RequestMethod.GET)
    @ResponseBody
    public Response getParameterKeyById(@RequestParam(value = "keyConvention", defaultValue = "") String id) {
        return parameterKeyService.getParameterKeybyId(id);
    }
}
