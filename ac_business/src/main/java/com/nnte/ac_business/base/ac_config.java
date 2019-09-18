package com.nnte.ac_business.base;

import com.nnte.framework.base.DataLibrary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ac_config {
    @Bean(initMethod = "initDataLibrary")
    public DataLibrary dataLibrary(){
        return new DataLibrary();
    }
}
