package com.nnte.ac_business.mapper.confdb;

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
        return defaultListWithAutoPage(statementName,params);
    }
}
