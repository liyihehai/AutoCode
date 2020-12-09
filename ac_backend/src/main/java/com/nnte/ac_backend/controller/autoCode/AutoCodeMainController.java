package com.nnte.ac_backend.controller.autoCode;

import com.fasterxml.jackson.databind.JsonNode;
import com.nnte.ac_business.component.autoCode.AutoCodeComponent;
import com.nnte.ac_business.mapper.confdb.ProjectMain;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.base.DataLibrary;
import com.nnte.framework.entity.KeyValue;
import com.nnte.framework.utils.MapUtil;
import com.nnte.framework.utils.NumberUtil;
import com.nnte.framework.utils.StringUtils;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping(value = "/autoCode")
public class AutoCodeMainController {
    private static Log logger = LogFactory.getLog(AutoCodeMainController.class);
    @Autowired
    private AutoCodeComponent autoCodeComponent;

    @RequestMapping(value = "index")
    public ModelAndView index(ModelAndView modelAndView){
        modelAndView.setViewName("front/autoCode/index");
        return modelAndView;
    }

    @RequestMapping(value = "mainPage")
    public ModelAndView mainPage(ModelAndView modelAndView){
        modelAndView.setViewName("front/autoCode/mainPage");
        return modelAndView;
    }

    @RequestMapping(value = "defaultMethodIndex")
    public ModelAndView defaultMethodIndex(ModelAndView modelAndView){
        modelAndView.setViewName("front/autoCode/defaultMethodIndex");
        return modelAndView;
    }

    @RequestMapping(value = "projectMgrIndex")
    public ModelAndView projectMgrIndex(ModelAndView modelAndView){
        modelAndView.setViewName("front/autoCode/projectMgrIndex");
        Map<String,Object> map=new HashMap<>();
        Map<String,Object> ret=autoCodeComponent.queryProjectList();
        if (BaseNnte.getRetSuc(ret))
        {
            List<ProjectMain> list=(List<ProjectMain>)ret.get("projectList");
            map.put("projectList",ret.get("projectList"));
            if (list!=null && list.size()>0)
            {
                ProjectMain pm = list.get(0);
                Map<String,Object> retQuery=autoCodeComponent.queryDBSrcTableNames(pm.getProjectCode());
                if (BaseNnte.getRetSuc(retQuery)){
                    map.put("tableList",retQuery.get("tableList"));
                }
                if (StringUtils.isNotEmpty(pm.getSubClass()))
                {
                    String sc=StringUtils.trim(pm.getSubClass());
                    String[] scs=sc.split(",");
                    List<String> scList = Arrays.asList(scs);
                    map.put("scList",scList);
                }
            }
            List<KeyValue> DBDriversList= DataLibrary.getLibKeyValueList("LibDBDrivers");
            map.put("DBDriversList",DBDriversList);
        }
        //--------------------------------
        modelAndView.addObject("map", map);
        return modelAndView;
    }

    //Ajax操作：查询数据源的所有表名称
    @RequestMapping(value = "queryDBSrcTableNames")
    @ResponseBody
    public Map<String,Object> queryDBSrcTableNames(@RequestBody JSONObject jsonParam){
        Map<String,Object> ret = BaseNnte.newMapRetObj();
        if (jsonParam==null)
        {
            BaseNnte.setRetFalse(ret,1002,"参数为空");
            return ret;
        }
        Integer projectCode = NumberUtil.getDefaultInteger(jsonParam.get("projectCode"));
        ret=autoCodeComponent.queryDBSrcTableNames(projectCode);
        return ret;
    }

    //Ajax操作：保存项目更改，包括新增及更改
    @RequestMapping(value = "onSaveProject")
    @ResponseBody
    public Map<String,Object> onSaveProject(@RequestBody Map<String,Object> mapProject){
        try {
            Map<String, Object> map = new HashMap<>();
            ProjectMain dto = new ProjectMain();
            MapUtil.copyFromSrcMap(mapProject, dto);
            Map<String, Object> ret = autoCodeComponent.saveProject( dto);
            return ret;
        }catch (Exception e){
            Map<String, Object> ret=BaseNnte.newMapRetObj();
            BaseNnte.setRetFalse(ret,1001,e.getMessage());
            return ret;
        }
    }

    //Ajax操作：查询单个项目信息
    @RequestMapping(value = "queryProjectInfo")
    @ResponseBody
    public Map<String,Object> queryProjectInfo(Integer projectCode) {
        Map<String,Object> ret=autoCodeComponent.querySingleProject(projectCode);
        return ret;
    }

    @RequestMapping(value = "deleteProject")
    @ResponseBody
    public Map<String,Object> deleteProject(Integer projectCode) {
        Map<String,Object> ret=autoCodeComponent.delSingleProject(projectCode);
        return ret;
    }

    //Ajax操作：产生自动代码
    @RequestMapping(value = "makeAutoCode")
    @ResponseBody
    public Map<String,Object> makeAutoCode(@RequestBody JSONObject jsonProject){
        Integer projectCode= NumberUtil.getDefaultInteger(jsonProject.get("projectCode"));
        String subClass=StringUtils.defaultString(jsonProject.get("subClass"));
        String tables=StringUtils.defaultString(jsonProject.get("tables"));
        String[] tablenames=tables.split(",");
        Map<String,Object> ret=autoCodeComponent.makeAutoCode(projectCode,subClass,tablenames);
        return ret;
    }

    //Ajax操作：数据库连接测试
    @RequestMapping(value = "connTest")
    @ResponseBody
    public Map<String,Object> connTest(@RequestBody JSONObject jsonProject){
        ProjectMain project= (ProjectMain)JSONObject.toBean(jsonProject,ProjectMain.class);
        Map<String,Object> ret=autoCodeComponent.connTest(project);
        return ret;
    }

}
