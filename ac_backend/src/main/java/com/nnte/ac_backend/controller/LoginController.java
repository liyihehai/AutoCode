package com.nnte.ac_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping(value = "/api/login")
public class LoginController {

    @RequestMapping(value = "/account")
    @ResponseBody
    public Object account(@RequestBody JsonNode data){
        if (data!=null)
            System.out.println(data.toString());
        Map<String,Object> retObj=new HashMap<>();
        retObj.put("status","ok");
        return retObj;
    }
}
