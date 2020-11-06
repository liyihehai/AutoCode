package com.nnte.ac_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.nnte.framework.base.BaseNnte;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@CrossOrigin
@Controller
@RequestMapping(value = "/api")

public class UserController {

    @Data
    private static class TmpItem{
        private Integer key;
        private Boolean disabled=false;
        private String href;
        private String avatar;
        private String name;
        private String owner;
        private String desc;
        private Integer callNo=0;
        private Integer status=0;
        private Date updatedAt;
        private Date createdAt;
        private Integer progress=0;
    }
    @Data
    private static class Result{
        private List<TmpItem> data;
        private Integer total;
        private boolean success=true;
        private Integer pageSize=10;
        private Integer current=1;
    }

    @Data
    private static class MenuItem {
        MenuItem(String name,String path,String icon){
            this.name=name;
            this.path=path;
            this.icon=icon;
        }
        private List<MenuItem> children=new ArrayList<>();
        private String name;
        private String path;
        private String icon;
    }

    @RequestMapping(value = "/notices", method = { RequestMethod.GET })
    @ResponseBody
    public Object notices(HttpServletRequest request){
        if (request!=null)
            System.out.println("getRequest");
        Map<String,Object> retObj=new HashMap<>();
        retObj.put("status","ok");

        return retObj;
    }

    @RequestMapping(value = "/currentUser", method = { RequestMethod.GET })
    @ResponseBody
    public Object currentUser(HttpServletRequest request){
        if (request!=null)
            System.out.println("getRequest");
        Map<String,Object> retObj=new HashMap<>();
        retObj.put("name","liyi");
        retObj.put("avatar","avatar");
        retObj.put("userid","1001");
        retObj.put("notice","");
        retObj.put("email","");
        retObj.put("signature","");
        retObj.put("title","");
        retObj.put("group","");
        retObj.put("tags","");
        retObj.put("notifyCount",0);
        retObj.put("unreadCount",0);
        retObj.put("country","CN");
        retObj.put("geographic","");
        retObj.put("address","");
        retObj.put("phone","");
        return retObj;
    }


    @RequestMapping(value = "/rule", method = { RequestMethod.GET })
    @ResponseBody
    public Object rule(HttpServletRequest request){
        if (request!=null)
            System.out.println(request.getParameterMap());
        List<TmpItem> list = new ArrayList<>();
        for(Integer i=0;i<10;i++){
            TmpItem item = new TmpItem();
            item.setKey(i+1);
            item.setName("name"+i.toString());
            item.setCreatedAt(new Date());
            item.setUpdatedAt(new Date());
            list.add(item);
        }
        Result result=new Result();
        result.setData(list);
        result.setTotal(20);
        return result;
    }

    @RequestMapping(value = "/queryUserMenu")
    @ResponseBody
    public Object queryUserMenu(@RequestBody JsonNode json){
        Map ret=BaseNnte.newMapRetObj();
        List<MenuItem> menus = new ArrayList<>();
        MenuItem menu1 = new MenuItem("自动代码","/autocode","icon-deleteteam");
        menu1.getChildren().add(new MenuItem("项目管理","/autocode/project","icon-deleteteam"));
        menu1.getChildren().add(new MenuItem("ProTable","/list/table-list","icon-deleteteam"));
        menu1.getChildren().add(new MenuItem("连接管理","/autocode/connect","icon-deleteteam"));
        menu1.getChildren().get(2).getChildren().add(new MenuItem("连接列表","/autocode/connect/conlist","icon-deleteteam"));
        menus.add(menu1);
        ret.put("data",menus);
        BaseNnte.setRetTrue(ret,"");
        return ret;
    }
}
