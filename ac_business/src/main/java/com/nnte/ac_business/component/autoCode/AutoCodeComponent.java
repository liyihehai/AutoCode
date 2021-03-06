package com.nnte.ac_business.component.autoCode;

import com.nnte.ac_business.config.WorkDBConfig;
import com.nnte.ac_business.entity.ErrorCodeLib;
import com.nnte.ac_business.mapper.confdb.ProjectMain;
import com.nnte.ac_business.mapper.confdb.ProjectMainService;
import com.nnte.basebusi.annotation.DBSrcTranc;
import com.nnte.framework.annotation.WorkDBAspect;
import com.nnte.framework.base.*;
import com.nnte.framework.utils.FileUtil;
import com.nnte.framework.utils.FreeMarkertUtil;
import com.nnte.framework.utils.StringUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@WorkDBAspect

public class AutoCodeComponent {
    @Autowired
    private ProjectMainService projectMainService;
    @Autowired
    private ServletContext sc;
    //查询项目列表
    public Map<String,Object> queryProjectList(){
        Map<String,Object> ret = BaseNnte.newMapRetObj();
        ProjectMain dto = new ProjectMain();
        List<ProjectMain> list= projectMainService.findModelList(dto);
        if (list!=null)
        {
            BaseNnte.setRetTrue(ret,"");
            ret.put("projectList",list);
        }
        else
            BaseNnte.setRetFalse(ret,9999,"查询执行异常");
        return ret;
    }

    //查询项目列表
    public Map<String,Object> queryProjectListWithPage(Map<String,Object> paramMap){
        Map<String,Object> ret = BaseNnte.newMapRetObj();
        try {
            List<ProjectMain> list = projectMainService.queryProjectWithPage(paramMap);
            ret.put("total", paramMap.get("totalCount"));
            ret.put("list", list);
            BaseNnte.setRetTrue(ret, ErrorCodeLib.OPE_SUCCESS );
        }catch (Exception e) {
            BaseNnte.setRetFalse(ret, 9999, "查询执行异常");
        }
        return ret;
    }

    //按项目编号查询指定的单个项目
    public Map<String,Object> querySingleProject(Integer projectCode){
        Map<String,Object> ret = BaseNnte.newMapRetObj();
        ProjectMain projectMain= projectMainService.findModelByKey(projectCode);
        if (projectMain!=null)
        {
            BaseNnte.setRetTrue(ret,ErrorCodeLib.OPE_SUCCESS);
            ret.put("projectMain",projectMain);
        }
        else
            BaseNnte.setRetFalse(ret,9999,"查询执行异常");
        return ret;
    }

    //检查项目数据源是否能够正常连接
    public boolean testProjectDBsrc(ProjectMain project){
        if (project==null || StringUtils.isEmpty(project.getConnDriverName())||
                StringUtils.isEmpty(project.getConnUrl()))
            return false;
        try {
            Connection conn= DynamicDatabaseSourceHolder.initSampleDBSrc(project.getConnDriverName(),project.getConnUrl(),
                    project.getConnUsername(),project.getConnPassword());
            if (conn!=null && !conn.isClosed()){
                conn.close();
                return true;
            }
        }catch (SQLException e){
        }
        return false;
    }

    //新增或更改项目(简化版)
    public Map<String,Object> saveProjectSample(ProjectMain saveProject){
        Map<String,Object> paramMap = new HashMap<>();
        JSONObject jsonProject = new JSONObject();
        jsonProject.put("projectCode",saveProject.getProjectCode());
        jsonProject.put("projectName",saveProject.getProjectName());
        jsonProject.put("RootPackage",saveProject.getRootPackage());
        jsonProject.put("subClass",saveProject.getSubClass());
        jsonProject.put("rootDir",saveProject.getRootDir());
        jsonProject.put("connDriverName",saveProject.getConnDriverName());
        jsonProject.put("connUrl",saveProject.getConnUrl());
        jsonProject.put("connUserName",saveProject.getConnUsername());
        jsonProject.put("connPassword",saveProject.getConnPassword());
        return saveProject(saveProject);
    }

    //新增或更改项目
    @DBSrcTranc(value = WorkDBConfig.DB_NAME,autocommit = false)
    public Map<String,Object> saveProject(ProjectMain pm){
        Map<String,Object> ret = BaseNnte.newMapRetObj();
        try {
            BaseService.ConnDaoSession session = BaseService.getThreadLocalSession();
            if (session==null){
                BaseNnte.setRetFalse(ret,9998,"数据连接错误");
                return ret;
            }
            if (pm==null || pm.getProjectCode()==null || pm.getProjectCode()<=0)
            {
                BaseNnte.setRetFalse(ret,1002,"对象转换失败或项目编号错误");
                return ret;
            }
            ProjectMain modifyPm=projectMainService.findModelByKey(session,pm.getProjectCode());
            Integer count = 0;
            if (modifyPm==null)
                count = projectMainService.addModel(session,pm);
            else
                count = projectMainService.updateModel(session,pm);
            if (count==null || !count.equals(1)){
                BaseNnte.setRetFalse(ret,1002,"对象保存失败");
                return ret;
            }
            modifyPm=projectMainService.findModelByKey(session,pm.getProjectCode());
            BaseNnte.setRetTrue(ret, "保存项目成功");
            ret.put("projectMain",modifyPm);
        }catch (Exception e){
            e.printStackTrace();
            BaseNnte.setRetFalse(ret,1002,"保存异常:"+e.getMessage());
        }
        return ret;
    }

    private Connection testGetConnection(ProjectMain project){
        //com.mysql.cj.jdbc.Driver
        String className=project.getConnDriverName();
        //jdbc:mysql://139.196.177.32:3306/qjb?autoReconnect=true&autoReconnectForPools=true&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
        String driverName=project.getConnUrl();
        //qjb
        String userName=project.getConnUsername();
        //QJBdb_2016
        String passWord=project.getConnPassword();
        return  DynamicDatabaseSourceHolder.initSampleDBSrc(className,
                driverName,userName,passWord);
    }
    //查询指定数据库的所有表名称
    public Map<String,Object> queryDBSrcTableNames(Integer projectCode){
        Map<String,Object> ret = BaseNnte.newMapRetObj();
        ProjectMain project= projectMainService.findModelByKey(projectCode);
        if (project==null)
        {
            BaseNnte.setRetFalse(ret,1002,"取得项目失败");
            return ret;
        }
        Connection conn= testGetConnection(project);
        try {
            if (conn != null && !conn.isClosed())
            {
                List<String> tableList=DynamicDatabaseSourceHolder.getAllTables(conn,project.getConnDriverName());
                if (tableList!=null && tableList.size()>0) {
                    BaseNnte.setRetTrue(ret,"取得表名称成功");
                    ret.put("tableList", tableList);
                    ret.put("projectMain",project);
                }
                else{
                    ret.put("code", 1002);
                    ret.put("msg", "取表名失败");
                }
                conn.isClosed();
            }
            else
            {
                ret.put("code", 1002);
                ret.put("msg", "连接失败");
            }
        }catch (java.sql.SQLException e){
            e.printStackTrace();
            ret.put("code", 9999);
            ret.put("msg", "系统异常："+e.getMessage());
        }
        return ret;
    }

    //删除项目
    @DBSrcTranc(value = WorkDBConfig.DB_NAME)
    public Map<String,Object> delSingleProject(Integer projectCode){
        Map<String,Object> ret = BaseNnte.newMapRetObj();
        BaseService.ConnDaoSession session = BaseService.getThreadLocalSession();
        ProjectMain project= projectMainService.findModelByKey(session,projectCode);
        if (project==null)
        {
            BaseNnte.setRetFalse(ret,1002,"取得项目失败");
            return ret;
        }
        Integer count=projectMainService.deleteModel(session,projectCode);
        if (count!=null && count.equals(1)){
            BaseNnte.setRetTrue(ret,"删除项目成功");
            ret.put("count",count);
            ret.put("projectMain",project);
        }
        return ret;
    }

    public String getClassName(String tableName) {
        StringBuffer afterTransferName = new StringBuffer("");
        String[] beforeName = tableName.split("_");
        for (int i = 0; i < beforeName.length; i++) {
            afterTransferName.append(StringUtils.changetoUpper(beforeName[i]));
        }
        return afterTransferName.toString();
    }

    public Map<String,Object> connTest(ProjectMain project){
        Map<String,Object> ret = BaseNnte.newMapRetObj();
        Connection conn= testGetConnection(project);
        if (conn!=null && !DynamicDatabaseSourceHolder.isConnClosed(conn))
        {
            BaseNnte.setRetTrue(ret,"测试链接成功");
            DynamicDatabaseSourceHolder.CloseDBConnection(conn);
        }else
            BaseNnte.setRetFalse(ret,1002,"测试链接失败");
        return ret;
    }

    public Map<String,Object> makeAutoCode(Integer projectCode,
                                           String subClass,String[] tables){
        Map<String,Object> ret = BaseNnte.newMapRetObj();

        ProjectMain project= projectMainService.findModelByKey(projectCode);
        if (project==null)
        {
            BaseNnte.setRetFalse(ret,1002,"取得项目失败");
            return ret;
        }
        if (tables.length<=0){
            BaseNnte.setRetFalse(ret,1002,"没有指定目标表");
            return ret;
        }
        //检查并创建文件根路径
        String packRootPath=StringUtils.defaultString(project.getRootPackage()).trim();
        String rootDir=StringUtils.defaultString(project.getRootDir()).trim();
        rootDir= FileUtil.toUNIXpath(rootDir);
        String fpath;
        if ("/".equals(rootDir.indexOf(rootDir.length()-1)))
            fpath=StringUtils.left(rootDir,rootDir.length()-1);
        else
            fpath=rootDir;
        if (!FileUtil.isPathExists(fpath))
            FileUtil.makeDirectory(fpath);
        if (!FileUtil.isPathExists(fpath)){
            BaseNnte.setRetFalse(ret,1002,"不能创建文件根目录："+rootDir);
            return ret;
        }
        //合成包路径
        String packpath=project.getRootPackage();
        if (StringUtils.isNotEmpty(subClass)){
            String subpath=subClass.replaceAll("\\.","/");
            fpath+="/"+subpath;
            packpath+="."+subClass;
        }
        if (!FileUtil.isPathExists(fpath))
            FileUtil.makeDirectory(fpath);
        if (!FileUtil.isPathExists(fpath)){
            BaseNnte.setRetFalse(ret,1002,"不能创建文件目录："+fpath);
            return ret;
        }
        Connection conn=DynamicDatabaseSourceHolder.initSampleDBSrc(project.getConnDriverName(),project.getConnUrl(),
                project.getConnUsername(),project.getConnPassword());
        if (conn==null || DynamicDatabaseSourceHolder.isConnClosed(conn)){
            BaseNnte.setRetFalse(ret,1002,"建立项目数据库连接失败");
            return ret;
        }
        int sucCount=0;
        List<String> makeInfoList = new ArrayList<>();
        for(String table:tables){
            Map<String,Object> retMake=makeAutoCodeFromDBTable(conn,project,fpath,packRootPath,
                    packpath,table);
            List<String> miList=(List<String>)retMake.get("makeInfoList");
            if (miList!=null)
                makeInfoList.addAll(miList);
            else
                makeInfoList.add(retMake.get("msg").toString());
            if (BaseNnte.getRetSuc(retMake))
                sucCount++;
            else
                break;
        }
        DynamicDatabaseSourceHolder.CloseDBConnection(conn);
        BaseNnte.setRetTrue(ret,"创建"+sucCount+"张数据表自动代码成功");
        ret.put("sucCount",sucCount);
        ret.put("makeInfoList",makeInfoList);
        return ret;
    }
    private String genLimitContentByDriver(String driver){
        String cLimit = "limit #{start},#{limit}";
        if (StringUtils.isEmpty(driver))
            return cLimit;
        String d=driver.toLowerCase();
        if (d.indexOf(".postgresql.")>=0)
            cLimit = "LIMIT #{limit} OFFSET #{start}";
        return cLimit;
    }
    //依据指定的表名称创建自动代码
    private Map<String,Object> makeAutoCodeFromDBTable(Connection conn,ProjectMain project,
                                                       String filePath,String packRootPath,
                                                       String packPath,String table){
        Map<String,Object> ret = BaseNnte.newMapRetObj();
        ret.put("msg","创建数据表"+table+"自动代码异常");
        List<DBSchemaColum> colList=DynamicDatabaseSourceHolder.getTableColumns(conn,
                project.getConnDriverName(),table);
        if (colList==null || colList.size()<=0){
            BaseNnte.setRetFalse(ret,1002,"取得数据表"+table+"字段信息失败");
            return ret;
        }
        String existDate=null;
        for(DBSchemaColum col:colList){
            if (col.getDataType().equalsIgnoreCase("date"))
                existDate="Date";
        }

        //取得表的主键字段
        List<DBSchemaColum> keyColList=DynamicDatabaseSourceHolder.queryKeyColumns(project.getConnDriverName(),colList);
        if (keyColList.size()<=0){
            BaseNnte.setRetFalse(ret,1002,"数据表"+table+"字段没有主键，不能生成代码");
            return ret;
        }
        if (keyColList.size()>1){
            BaseNnte.setRetFalse(ret,1002,"数据表"+table+"暂不支持复合主键，不能生成代码");
            return ret;
        }
        //类前缀
        String classPri=getClassName(table);
        Map<String,Object> tmpmap = new HashMap<>();
        tmpmap.put("packRootPath",packRootPath);
        tmpmap.put("packPath",packPath);
        tmpmap.put("colList",colList);
        tmpmap.put("keyCol",keyColList.get(0));
        tmpmap.put("classPri",classPri);
        tmpmap.put("tableName",table);
        tmpmap.put("limitContent",genLimitContentByDriver(project.getConnDriverName()));
        if (existDate!=null)
            tmpmap.put("existDate",existDate);
        List<String> makeInfoList = new ArrayList<>();
        Map<String,Object> makeRet=makeCodeFile(tmpmap,"model",filePath,classPri,"","java");
        makeInfoList.add(makeRet.get("msg").toString());
        if (!BaseNnte.getRetSuc(makeRet))
        {
            ret.put("makeInfoList",makeInfoList);
            return ret;
        }
        makeRet=makeCodeFile(tmpmap,"dao",filePath,classPri,"Dao","java");
        makeInfoList.add(makeRet.get("msg").toString());
        if (!BaseNnte.getRetSuc(makeRet))
        {
            ret.put("makeInfoList",makeInfoList);
            return ret;
        }
        makeRet=makeCodeFile(tmpmap,"service",filePath,classPri,"Service","java");
        makeInfoList.add(makeRet.get("msg").toString());
        if (!BaseNnte.getRetSuc(makeRet))
        {
            ret.put("makeInfoList",makeInfoList);
            return ret;
        }
        makeRet=makeCodeFile(tmpmap,"daoxml",filePath,classPri,"Dao","xml");
        makeInfoList.add(makeRet.get("msg").toString());
        if (!BaseNnte.getRetSuc(makeRet))
        {
            ret.put("makeInfoList",makeInfoList);
            return ret;
        }
        BaseNnte.setRetTrue(ret,"创建表"+table+"自动代码成功");
        ret.put("makeInfoList",makeInfoList);
        return ret;
    }

    private Map<String,Object> makeCodeFile(Map<String,Object> tmpmap,String fileClass,String filePath,
                                String classPri,String fnAppend,String fileType){
        Map<String,Object> ret = BaseNnte.newMapRetObj();
        String fn=classPri+fnAppend+"."+fileType;
        String sContent= FreeMarkertUtil.getFreemarkerFtl(null,sc,FreeMarkertUtil.pathType.cls,
                tmpmap,"/templates/front/autoCode/codeModel/"+fileClass+".ftl");
        if (StringUtils.isNotEmpty(sContent)){
            //生成代码文件
            String pfn=filePath+"/"+ fn;
            FileUtil.WriteStringToFile(pfn,sContent);
            if (FileUtil.isFileExist(pfn))
            {
                BaseNnte.setRetTrue(ret,fileClass+" file success:"+fn);
                return ret;
            }
        }
        BaseNnte.setRetFalse(ret,1002,fileClass+" file failed:"+fn);
        return ret;
    }
    public static void main(String[] args){

    }
}
