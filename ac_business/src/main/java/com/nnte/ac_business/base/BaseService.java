package com.nnte.ac_business.base;

import com.nnte.framework.base.BaseDao;
import com.nnte.framework.base.BaseModel;
import com.nnte.framework.base.ConnSqlSessionFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.util.List;

public abstract class BaseService <T extends BaseDao,M extends BaseModel>{
    private Class<T> persistentClass;
    @Autowired
    private DynamicDatabaseSourceHolder ddsh;
    public class connDaoSession implements AutoCloseable{
        private SqlSession sqlSession;
        private Connection connection;
        private T mapper;
        public connDaoSession(SqlSession sqlSession,Connection connection,T mapper){
            this.sqlSession=sqlSession;
            this.connection=connection;
            this.mapper=mapper;
        }

        public SqlSession getSqlSession() {
            return sqlSession;
        }

        public Connection getConnection() {
            return connection;
        }

        public T getMapper() {
            return mapper;
        }

        @Override
        public void close() {
            try {
                if (sqlSession!=null && (connection==null||connection.isClosed()))
                    sqlSession.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public BaseService(final Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }
    public connDaoSession getConnDaoSession(SqlSessionFactory ssf, Connection conn){
        if (conn==null || ssf==null)
            return null;
        try {
            SqlSession ss=ssf.openSession(conn);
            if (ss==null)
                return null;
            T mapper=ss.getMapper(persistentClass);
            return new connDaoSession(ss,conn,mapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public connDaoSession getDefaultConnDaoSession(){
        try {
            DynamicDatabaseSourceHolder.dbsourceSqlSessionFactory dBsrcSSF = ddsh.getDefaultDBsrcSSF();
            SqlSession ss = dBsrcSSF.getSqlSessionFactory().openSession();
            System.out.println("default dbconnect="+ss.getConnection().toString());
            T mapper = ss.getMapper(persistentClass);
            return new connDaoSession(ss,null,mapper);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    //-----------------------------------------------------------------------
    public Integer addModel(M model) {
        try(connDaoSession cds=getDefaultConnDaoSession()){
            return cds.getMapper().addModel(model);
        }
    }
    public Integer addModel(ConnSqlSessionFactory cssf, M model){
        try(connDaoSession cds=getConnDaoSession(cssf.getSsf(),cssf.getConn())){
            return cds.getMapper().addModel(model);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public M findModelByKey(Object key){
        try(connDaoSession cds=getDefaultConnDaoSession()) {
            List<?> list=cds.getMapper().findModelByKey(key);
            if (list!=null && list.size()==1)
                return (M)list.get(0);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public M findModelByKey(ConnSqlSessionFactory cssf, Object key){
        try(connDaoSession cds=getConnDaoSession(cssf.getSsf(),cssf.getConn())){
            List<M> list=(List<M>)cds.getMapper().findModelByKey(key);
            if (list!=null && list.size()==1)
                return list.get(0);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Integer deleteModel(Object key){
        try(connDaoSession cds=getDefaultConnDaoSession()) {
            return cds.getMapper().deleteModel(key);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public Integer deleteModel(ConnSqlSessionFactory cssf,Object key){
        try(connDaoSession cds=getConnDaoSession(cssf.getSsf(),cssf.getConn())){
            return cds.getMapper().deleteModel(key);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public Integer updateModel(M model){
        try(connDaoSession cds=getDefaultConnDaoSession()) {
            return cds.getMapper().updateModel(model);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public Integer updateModel(ConnSqlSessionFactory cssf, M model){
        try(connDaoSession cds=getConnDaoSession(cssf.getSsf(),cssf.getConn())) {
            return cds.getMapper().updateModel(model);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public List<M> findModelList(M model){
        try(connDaoSession cds=getDefaultConnDaoSession()) {
            return (List<M>)cds.getMapper().findModelList(model);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<M> findModelList(ConnSqlSessionFactory cssf,M model){
        try(connDaoSession cds=getConnDaoSession(cssf.getSsf(),cssf.getConn())){
            return (List<M>)cds.getMapper().findModelList(model);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Integer findModelCount(M model){
        try(connDaoSession cds=getDefaultConnDaoSession()) {
            return cds.getMapper().findModelCount(model);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public Integer findModelCount(ConnSqlSessionFactory cssf,M model){
        try(connDaoSession cds=getConnDaoSession(cssf.getSsf(),cssf.getConn())){
            return cds.getMapper().findModelCount(model);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public List<M> findModelWithPg(M model){
        try(connDaoSession cds=getDefaultConnDaoSession()){
            return (List<M>)cds.getMapper().findModelWithPg(model);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<M> findModelWithPg(ConnSqlSessionFactory cssf,M model){
        try (connDaoSession cds=getConnDaoSession(cssf.getSsf(),cssf.getConn())){
            return (List<M>)cds.getMapper().findModelWithPg(model);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //isReLoad:是否重新装载，当Update时，如果选择true，会重新装载该行数据
    public M save(ConnSqlSessionFactory cssf,M model,boolean isReLoad){
        if (model==null)
            return null;
        Object pkVal=model.getSinglePKVal();
        if (pkVal!=null) {
            Integer count=updateModel(cssf,model);
            if (count!=null && count>0)
            {
                if (isReLoad)
                    return findModelByKey(pkVal);
                else
                    return model;
            }
        } else{
            Integer count=addModel(cssf,model);
            if (count!=null && count>0)
                return model;
        }
        return null;
    }
}
