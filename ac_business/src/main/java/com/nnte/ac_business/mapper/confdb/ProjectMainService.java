package com.nnte.ac_business.mapper.confdb;

import com.nnte.ac_business.base.BaseService;
import org.springframework.stereotype.Component;

@Component
public class ProjectMainService extends BaseService<ProjectMainDao,ProjectMain> {
    public ProjectMainService(){
        super(ProjectMainDao.class);
    }
}
