package com.nnte.ac_backend.controller.autoCode;

import com.fasterxml.jackson.databind.JsonNode;
import com.nnte.ac_business.component.autoCode.AutoCodeComponent;
import com.nnte.ac_business.mapper.confdb.ProjectMain;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.utils.MapUtil;
import com.nnte.framework.utils.NumberUtil;
import com.nnte.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping(value = "/api/autocode/project")
public class ProjectMainController {

    @Autowired
    private AutoCodeComponent autoCodeComponent;

    @RequestMapping(value = "/queryProjectList")
    @ResponseBody
    public Object queryProjectList(@RequestBody JsonNode json){
        Map<String,Object> param = new HashMap<>();
        param.put("pageNo", NumberUtil.getDefaultInteger(json.get("current")));
        param.put("limit", NumberUtil.getDefaultInteger(json.get("pageSize")));
        Map ret= autoCodeComponent.queryProjectListWithPage(param);
        BaseNnte.setRetTrue(ret,ErrorCodeLib.OPE_SUCCESS);
        return ret;
    }
    @RequestMapping(value = "/saveAutoCodeProject")
    @ResponseBody
    public Object saveAutoCodeProject(@RequestBody  Map<String, Object> param){
        try {
            if (param == null || param.size() <= 0)
                throw new Exception(ErrorCodeLib.INVALID_SUBMIT_PARAMS);
            ProjectMain dto = new ProjectMain();
            MapUtil.copyFromSrcMap(param,dto);
            if (dto.getSubClass()==null)
                dto.setSubClass("");
            Map<String,Object> ret = autoCodeComponent.saveProject(dto);
            return ret;
        }catch (Exception e){
            Map errRet = BaseNnte.newMapRetObj();
            BaseNnte.setRetFalse(errRet,1001,e.getMessage());
            return errRet;
        }
    }
    /**
     * 查询连接的表的列表
     * */
    @RequestMapping(value = "/queryConnTableList")
    @ResponseBody
    public Object queryConnTableList(@RequestBody  Map<String, Object> param){
        try {
            if (param == null || param.size() <= 0)
                throw new Exception(ErrorCodeLib.INVALID_SUBMIT_PARAMS);
            Integer projectCode = NumberUtil.getDefaultInteger(param.get("projectCode"));
            Map<String,Object> ret = autoCodeComponent.queryDBSrcTableNames(projectCode);
            return ret;
        }catch (Exception e){
            Map errRet = BaseNnte.newMapRetObj();
            BaseNnte.setRetFalse(errRet,1001,e.getMessage());
            return errRet;
        }
    }

    /**
     * 执行依据表名称创建代码文件
     * */
    @RequestMapping(value = "/execGenFile")
    @ResponseBody
    public Object execGenFile(@RequestBody  Map<String, Object> param){
        try {
            if (param == null || param.size() <= 0)
                throw new Exception(ErrorCodeLib.INVALID_SUBMIT_PARAMS);
            Integer projectCode = NumberUtil.getDefaultInteger(param.get("projectCode"));
            String subClass = StringUtils.defaultString(param.get("subClass"));
            String tables = StringUtils.defaultString(param.get("tables"));
            if (projectCode==null||projectCode<=0 ||
                    StringUtils.isEmpty(subClass)||StringUtils.isEmpty(tables))
                throw new Exception("参数不完整");
            Map<String,Object> ret = autoCodeComponent.makeAutoCode(projectCode,subClass,tables.split(","));
            return ret;
        }catch (Exception e){
            Map errRet = BaseNnte.newMapRetObj();
            BaseNnte.setRetFalse(errRet,1001,e.getMessage());
            return errRet;
        }
    }
}
