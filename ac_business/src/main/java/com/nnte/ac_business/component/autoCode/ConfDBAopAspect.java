package com.nnte.ac_business.component.autoCode;

import com.nnte.ac_business.annotation.DBSrcTranc;
import com.nnte.ac_business.base.DynamicDatabaseSourceHolder;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.base.ConnSqlSessionFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Map;

@Aspect
@Component
public class ConfDBAopAspect {
    @Autowired
    private DynamicDatabaseSourceHolder dynamicDSHolder;

    /*切点条件：
    * 方法必须有@DBSrcTranc注解，第一个参数必须是Map<String,Object> paramMap，
    * 返回值必须是Map<String,Object>
    */
    @Pointcut("!@target(com.nnte.framework.annotation.WorkDBAspect) && @annotation(com.nnte.ac_business.annotation.DBSrcTranc) && args(paramMap,..) && execution(public java.util.Map<String,Object> *(..))")
    public void ConfDBAopPointCut(Map<String,Object> paramMap){ }

    @Around(value = "ConfDBAopPointCut(paramMap)")
    public Object doAround(ProceedingJoinPoint pjp,Map<String,Object> paramMap) throws Throwable {
        Connection conn=null;
        Map<String,Object> ret = BaseNnte.newMapRetObj();
        try {
            Method method = getMethod(pjp);
            DBSrcTranc dbSrcTranc = method.getAnnotation(DBSrcTranc.class);
            String value = dbSrcTranc.value();
            boolean autocommit=dbSrcTranc.autocommit();
            DynamicDatabaseSourceHolder.dbsourceSqlSessionFactory dbsrcSSF=dynamicDSHolder.getDBsrcSSF(value);
            if (dbsrcSSF==null)
            {
                BaseNnte.setRetFalse(ret,9998,"系统繁忙[错误的数据源:"+value+"]");
                BaseNnte.outConsoleLog(ret.get("msg").toString());
                return ret;
            }
            conn=dbsrcSSF.getDataSource().getConnection();
            if (dbsrcSSF.getDataSource()==null ||
                    conn==null || conn.isClosed())
            {
                BaseNnte.setRetFalse(ret,9998,"系统繁忙[取数据链接失败]");
                BaseNnte.outConsoleLog(ret.get("msg").toString());
                return ret;
            }
            conn.setAutoCommit(autocommit);
            if (null != paramMap) {
                ConnSqlSessionFactory cssf=new ConnSqlSessionFactory(conn,dbsrcSSF.getSqlSessionFactory());
                paramMap.put("ConnSqlSessionFactory",cssf);
            }
            ret =  (Map<String,Object>)pjp.proceed();
            return ret;
        } catch (Throwable e) {
            BaseNnte.setRetFalse(ret,9999,"系统异常[ConfDBAopAspect:doAround]");
            BaseNnte.outConsoleLog(ret.get("msg").toString());
            e.printStackTrace();
            return ret;
        } finally {
            if (conn!=null && !conn.isClosed())
            {
                if (!conn.getAutoCommit()) {
                    if (ret!=null && BaseNnte.getRetSuc(ret))
                        conn.commit();
                    else
                        conn.rollback();
                }
                conn.close();
            }
        }
    }

    private Method getMethod(JoinPoint point) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Class<?> targetClass = point.getTarget().getClass();
        try {
            return targetClass.getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
