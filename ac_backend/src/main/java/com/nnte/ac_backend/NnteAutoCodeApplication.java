package com.nnte.ac_backend;

import com.nnte.ac_backend.sqlite.sqlite3Local;
import com.nnte.ac_business.component.autoCode.AutoCodeComponent;
import com.nnte.basebusi.annotation.DBSrcTranc;
import com.nnte.framework.base.DynamicDatabaseSourceHolder;
import com.nnte.framework.base.SpringContextHolder;
import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan("com.nnte")
public class NnteAutoCodeApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(NnteAutoCodeApplication.class, args);
        DynamicDatabaseSourceHolder ddh= SpringContextHolder.getBean("dynamicDatabaseSourceHolder");
        DynamicDatabaseSourceHolder.loadDBSchemaInterface();

        HikariConfig sqlite3Config = new HikariConfig();
        sqlite3Config.setDriverClassName("org.sqlite.JDBC");
        sqlite3Config.setConnectionTimeout(1000*30);
        sqlite3Config.setJdbcUrl("jdbc:sqlite:identifier.sqlite");
        sqlite3Config.setMinimumIdle(1);
        sqlite3Config.setMaximumPoolSize(3);
        sqlite3Config.setIdleTimeout(500000);
        sqlite3Config.setMaxLifetime(540000);
        sqlite3Config.setConnectionTestQuery("SELECT 1");

        List<String> sqlite3mappers=new ArrayList<>();
        sqlite3mappers.add("com.nnte.ac_business.mapper.confdb");
        ddh.initDataBaseSource(DBSrcTranc.Config_DBSrc_Name,sqlite3Config,sqlite3mappers, AutoCodeComponent.class,true);

        sqlite3Local sqlite3local=SpringContextHolder.getBean("sqlite3Local");
        sqlite3local.initSchema();
    }
}
