package com.nnte.ac_business.mapper.confdb;

import com.github.pagehelper.PageInfo;
import com.nnte.framework.base.BaseService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ProjectMainService extends BaseService<ProjectMainDao,ProjectMain> {
    public ProjectMainService(){
        super(ProjectMainDao.class);
    }

    /**
     * 查询门店有效的门店费率政策列表(带分页)
     * */
    public List<ProjectMain> queryProjectWithPage(Map<String,Object> params){
        String statementName = "findModelList";
        PageInfo<ProjectMain> pageInfo=defaultListWithAutoPage(statementName,params);
        if (pageInfo==null){
            params.put("total",0);
            return null;
        }
        params.put("total",pageInfo.getTotal());
        return pageInfo.getList();
    }
}
