package com.nnte.ac_backend.sqlite;

import com.nnte.ac_business.base.DynamicDatabaseSourceHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;

//Sqlite3 local init and config class
@Component
public class sqlite3Local {
    private final static String localDbsrcName="sqlite3db";
    @Autowired
    private DynamicDatabaseSourceHolder ddsh;

    private void createTable(Connection conn,String tableName,String sql){
        try {
            if (conn==null)
                return;
            //-------------------------------------
            Statement command=conn.createStatement();
            Boolean tableExist=isTableExist(conn,tableName);
            if (tableExist!=null && !tableExist) {
                command.execute(sql);
            }
            command.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Boolean isTableExist(Connection conn,String tableName){
        try {
            if (conn==null)
                return null;
            //----------------------------------------
            String queryTableSql="select * from sqlite_master where type = 'table' and name = '"+tableName+"'";
            Statement command=conn.createStatement();
            ResultSet rSet=command.executeQuery(queryTableSql);
            if (rSet != null) {
                if (rSet.next()) {
                    rSet.close();
                    command.close();
                    return Boolean.valueOf(true);
                }
                rSet.close();
            }
            command.close();
            return Boolean.valueOf(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void initSchema()
    {
        DynamicDatabaseSourceHolder.dbsourceSqlSessionFactory dssf=ddsh.getDBsrcSSF(localDbsrcName);
        if (dssf==null && dssf.getDataSource()==null)
            return;
        Connection conn= null;
        try {
            conn = dssf.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (conn==null)
            return;
        try {
            createTable(conn,"PROJECT_MAIN","create table PROJECT_MAIN (" +
                    "   PROJECT_CODE         INT not null," +
                    "   PEOJECT_NAME         varchar(100)," +
                    "   ROOT_PACKAGE         varchar(100)," +
                    "   SUB_CLASS            text," +
                    "   ROOT_DIR             varchar(100)," +
                    "   CONN_DRIVER_NAME     varchar(100)," +
                    "   CONN_URL             varchar(200)," +
                    "   CONN_USERNAME        varchar(50)," +
                    "   CONN_PASSWORD        varchar(50)," +
                    "   primary key (PROJECT_CODE));");
            createTable(conn,"PROJECT_PACKAGE","create table PROJECT_PACKAGE (" +
                    "   PACKAGE_FULL_NAME    varchar(300) not null," +
                    "   PROJECT_CODE         INT," +
                    "   PACKAGE_NAME         varchar(100)," +
                    "   PRI_PACKAGE_NAME     varchar(100)," +
                    "   primary key (PACKAGE_FULL_NAME));");
            createTable(conn,"PROJECT_COMPONENT","create table PROJECT_COMPONENT (" +
                    "   PACKAGE_FULL_NAME    varchar(300) not null," +
                    "   COMPONENT_NAME       varchar(50) not null," +
                    "   COMPONENT_TYPE       varchar(20) ," +
                    "   COMPONENT_SQL        varchar(500)," +
                    "   XML_FILE             varchar(100)," +
                    "   DAO_FILE             varchar(100)," +
                    "   ENTERTY_FILE         varchar(100)," +
                    "   primary key (PACKAGE_FULL_NAME, COMPONENT_NAME));");
            createTable(conn,"COMPONENT_METHOD","create table COMPONENT_METHOD (" +
                    "   PACKAGE_FULL_NAME    varchar(300) not null," +
                    "   COMPONENT_NAME       varchar(50) not null," +
                    "   METHOD_NAME          varchar(50) not null," +
                    "   METHOD_MTXT          text," +
                    "   primary key (PACKAGE_FULL_NAME, COMPONENT_NAME, METHOD_NAME));");
            createTable(conn,"COMPONENT_DEFAULT_METHOD","create table COMPONENT_DEFAULT_METHOD (" +
                    "   METHOD_NAME          varchar(50) not null," +
                    "   METHOD_MTXT          text," +
                    "   primary key (METHOD_NAME));");
        }finally {
            try {
                conn.close();
            } catch (SQLException connExcp) {
                connExcp.printStackTrace();
            }
        }
    }
}
