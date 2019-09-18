package com.nnte.ac_business.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
/*
* 本注解用于函数，表示函数需要统一获取数据源，业务入口函数使用
* @value 用于标识需要创建的数据源的名称
* @autocommit 标识是否自动提交，如果为fals，切面程序将在程序执行成功后手动提交
* */
public @interface DBSrcTranc {
    public static final String Config_DBSrc_Name = "sqlite3db";
    public static final String Work_DBSrc_Name = "mysqldb";
    String value() default Config_DBSrc_Name;
    boolean autocommit() default true;
}