package com.nnte.ac_business.base;

import com.nnte.framework.annotation.DBSchema;
import com.nnte.framework.annotation.DBSchemaInterface;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.base.DBSchemaColum;
import com.nnte.framework.base.SpringContextHolder;
import com.nnte.framework.utils.PackageUtil;
import com.nnte.framework.utils.StringUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

@Component
public class DynamicDatabaseSourceHolder {

    private static Map<String, Object> DataSourceHolderMap = new HashMap<>();
    private static Map<String, Object> DBSchemaInterfaceMap = new HashMap<>();
    private static String defaultDatasrcName;

    public class dbsourceSqlSessionFactory {
        private String dbsrcName;                   //数据源名称
        private HikariDataSource dataSource;        //数据源
        private SqlSessionFactory sqlSessionFactory;//mybatis sqlSessionFactory

        public HikariDataSource getDataSource() {
            return dataSource;
        }

        public void setDataSource(HikariDataSource dataSource) {
            this.dataSource = dataSource;
        }

        public SqlSessionFactory getSqlSessionFactory() {
            return sqlSessionFactory;
        }

        public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
            this.sqlSessionFactory = sqlSessionFactory;
        }

        public String getDbsrcName() {
            return dbsrcName;
        }

        public void setDbsrcName(String dbsrcName) {
            this.dbsrcName = dbsrcName;
        }
    }

    public static void loadDBSchemaInterface(){
        Map<String,Object> beans = SpringContextHolder.getApplicationContext().getBeansWithAnnotation(DBSchema.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            DBSchema dbSchema=entry.getValue().getClass().getDeclaredAnnotation(DBSchema.class);
            if (dbSchema!=null)
            {
                DBSchemaInterfaceMap.put(dbSchema.value(),entry.getValue());
                BaseNnte.outConsoleLog("系统支持数据库："+dbSchema.value());
            }
        }
    }
    private static String getDBSrcName(String dbsName) {
        return "dbsrcSSF_" + dbsName;
    }

    //查询系统支持的数据库类型
    public static String[] queryDBTypes(){
        String[] ret = new String[DBSchemaInterfaceMap.size()];
        int index=0;
        for(Map.Entry<String, Object> entry : DBSchemaInterfaceMap.entrySet()){
            ret[index]=entry.getKey();
            index++;
        }
        return ret;
    }

    public static DBSchemaInterface getSchemaInterfaceByDBType(String dbType){
        if (DBSchemaInterfaceMap!=null && dbType!=null){
            return (DBSchemaInterface)DBSchemaInterfaceMap.get(dbType);
        }
        return null;
    }

    public static DBSchemaInterface getSchemaInterface(String driverClassname){
        if (DBSchemaInterfaceMap!=null && driverClassname!=null){
            String dcn= StringUtils.trim(driverClassname);
            for(Map.Entry<String, Object> entry : DBSchemaInterfaceMap.entrySet()){
                if (dcn.indexOf(entry.getKey())>=0)
                    return (DBSchemaInterface)entry.getValue();
            }
        }
        return null;
    }
    //按包设置映射文件
    public void setMapperPackage(Configuration configuration, List<String> mappers) {
        for (String packageName : mappers) {
            try {
                //装载正常的映射文件
                configuration.addMappers(packageName);
                //装载扩展映射文件，资源路径与正常映射文件相同
                List<String> exXmls = PackageUtil.getResourcePathName(packageName, "xml");
                for (String resource : exXmls) {
                    if (!configuration.isResourceLoaded(resource)) {
                        InputStream inputStream = Resources.getResourceAsStream(resource);
                        XMLMapperBuilder xmb = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
                        xmb.parse();
                    }
                }
            } catch (BindingException | IOException ibatisE) {
                ibatisE.printStackTrace();
            }
        }
    }

    //初始化一个简单数据源,该数据源没有mapper,只通过连接操作数据库
    public static Connection initSampleDBSrc(String className, String jdbcUrl, String userName, String passWord) {
        try {
            HikariConfig config = new HikariConfig();
            config.setDriverClassName(className); //"com.mysql.cj.jdbc.Driver"
            config.setJdbcUrl(jdbcUrl);//"jdbc:mysql://139.196.177.32:3306/qjb?autoReconnect=true&autoReconnectForPools=true&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai"
            if (StringUtils.isNotEmpty(userName))
                config.setUsername(userName);//"qjb"
            if (StringUtils.isNotEmpty(passWord))
                config.setPassword(passWord);//"QJBdb_2016"
            config.setMinimumIdle(0);
            config.setMaximumPoolSize(1);
            config.setIdleTimeout(1000 * 20);
            config.setConnectionTestQuery("SELECT 1");
            HikariDataSource ds = new HikariDataSource(config);
            return ds.getConnection();
        }catch (RuntimeException re){
            BaseNnte.outConsoleLog("数据连接异常："+re.getMessage());
        }catch (SQLException e) {
            BaseNnte.outConsoleLog("数据连接异常："+e.getMessage());
        }
        return null;
    }
    //关闭数据库连接
    public static void CloseDBConnection(Connection conn){
        try{
            if (conn!=null)
                conn.close();
        }catch (Exception e){ }
    }

    public static Boolean isConnClosed(Connection conn){
        try{
            if (conn!=null)
                return conn.isClosed();
        }catch (Exception e){ }
        return null;
    }
    //取得连接的所有的表名称列表
    public static List<String> getAllTables(Connection conn,String driverClassname) {
        try {
            if (conn == null || conn.isClosed())
                return null;
            DBSchemaInterface dbs=getSchemaInterface(driverClassname);
            if (dbs==null)
                return null;
            return dbs.queryDBTableNames(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //取得连接的所有的表名称列表
    public static List<DBSchemaColum> getTableColumns(Connection conn, String driverClassname, String table) {
        try {
            if (conn == null || conn.isClosed()||StringUtils.isEmpty(table))
                return null;
            DBSchemaInterface dbs=getSchemaInterface(driverClassname);
            if (dbs==null)
                return null;
            return dbs.queryDBTableColumns(conn,table);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<DBSchemaColum> queryKeyColumns(String driverClassname,List<DBSchemaColum> cols){
        if (cols==null || cols.size()<=0)
            return null;
        DBSchemaInterface dbs=getSchemaInterface(driverClassname);
        if (dbs==null)
            return null;
        return dbs.queryKeyColumns(cols);
    }

    //初始化一个数据源
    public void initDataBaseSource(String dbsrcName, HikariConfig config, List<String> mappers, boolean isDefault) {
        if (dbsrcName == null || config == null || dbsrcName.isEmpty())
            return;
        String db_srcName = getDBSrcName(dbsrcName);
        if (DataSourceHolderMap.get(db_srcName) != null)
            return;
        config.setPoolName(dbsrcName + "_HikariPool");
        HikariDataSource ds = new HikariDataSource(config);
        if (ds != null) {
            try {
                Connection conn = ds.getConnection();
                if (conn != null) {
                    conn.close();
                    //----------------------------------------------------------------
                    TransactionFactory transactionFactory = new JdbcTransactionFactory();
                    Environment environment = new Environment(dbsrcName, transactionFactory, ds);
                    Configuration configuration = new Configuration(environment);
                    configuration.setMapUnderscoreToCamelCase(false);
                    //创建SqlSessionFactory对象
                    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
                    setMapperPackage(sqlSessionFactory.getConfiguration(), mappers);
                    dbsourceSqlSessionFactory dbsrcSSF = new dbsourceSqlSessionFactory();
                    dbsrcSSF.setDbsrcName(db_srcName);
                    dbsrcSSF.setDataSource(ds);
                    dbsrcSSF.setSqlSessionFactory(sqlSessionFactory);
                    DataSourceHolderMap.put(dbsrcSSF.getDbsrcName(), dbsrcSSF);
                    if (isDefault)
                        defaultDatasrcName = dbsrcName;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //取得一个数据源SqlSessionFactory关联对象
    public dbsourceSqlSessionFactory getDBsrcSSF(String dbsrcName) {
        return (dbsourceSqlSessionFactory) DataSourceHolderMap.get(getDBSrcName(dbsrcName));
    }

    //取得一个数据源SqlSessionFactory默认关联对象
    public dbsourceSqlSessionFactory getDefaultDBsrcSSF() {
        if (DataSourceHolderMap.size() > 0)
            return (dbsourceSqlSessionFactory) DataSourceHolderMap.get(getDBSrcName(defaultDatasrcName));
        return null;
    }
}
