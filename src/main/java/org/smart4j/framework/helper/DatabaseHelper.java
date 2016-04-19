package org.smart4j.framework.helper;

import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.util.ClassUtil;
import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.MapUtil;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by joooo on 2016/4/19.
 */

public final class DatabaseHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);

    private static final ThreadLocal<Connection> CONNECTION_HOLDER;

    private static final QueryRunner QUERY_RUNNER;

    private static final BasicDataSource DATA_SOURCE;

    static {
        CONNECTION_HOLDER = new ThreadLocal<Connection>();

        QUERY_RUNNER  = new QueryRunner();

        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(ConfigHelper.getJdbcDriver());
        DATA_SOURCE.setUrl(ConfigHelper.getJdbcUrl());
        DATA_SOURCE.setUsername(ConfigHelper.getJdbcUsername());
        DATA_SOURCE.setPassword(ConfigHelper.getJdbcPassword());
    }

    /**
     * @return 数据源
     */
    public static DataSource getDataSource(){
        return DATA_SOURCE;
    }

    /**
     * @return 数据库连接
     */
    public static Connection getConnection(){
        Connection conn = CONNECTION_HOLDER.get();
        if (conn == null){
            try{
                conn = DATA_SOURCE.getConnection();
            } catch (SQLException e){
                LOGGER.error("get connection failure.", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
        return conn;
    }

    /**
     * 开启事务
     */
    public static void beginTransaction(){
        Connection conn = getConnection();
        if(conn != null){
            try{
                conn.setAutoCommit(false);
            } catch (SQLException e){
                LOGGER.error("begin transaction failure.", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
    }

    /**
     * 提交事务
     */
    public static void commitTransaction(){
        Connection conn = getConnection();
        if (conn != null){
            try{
                conn.commit();
                conn.close();
            } catch (SQLException e){
                LOGGER.error("commit transaction failure.", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

    /**
     * 回滚事务
     */
    public static void rollbackTransaction(){
        Connection conn = getConnection();
        if(conn != null){
            try{
                conn.rollback();
                conn.close();
            } catch (SQLException e){
                LOGGER.error("rollback transaction failure.", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

    /**
     * 查询实体
     * @param entityClass
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params){
        T entity;
        try{
            Connection conn = getConnection();
            entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass), params);
        } catch (SQLException e){
            LOGGER.error("query emtity failure.", e);
            throw new RuntimeException(e);
        }
        return entity;
    }

    /**
     * 查询实体列表
     * @param entityClass
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> List<T> queryList(Class<T> entityClass, String sql, Object... params){
        List<T> entityList;
        try{
            Connection conn = getConnection();
            entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e){
            LOGGER.error("query entity list failure.", e);
            throw new RuntimeException(e);
        }
        return entityList;
    }

    /**
     * 返回某行的单个列值
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> T query(String sql, Object... params){
        T scalarObj;//列值
        try{
            Connection conn = getConnection();
            scalarObj = QUERY_RUNNER.query(conn, sql, new ScalarHandler<T>(), params);
        } catch (SQLException e){
            LOGGER.error("query failure.", e);
            throw new RuntimeException(e);
        }
        return scalarObj;
    }

    /**
     * 查询多行的同一列值
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> List<T> queryList(String sql, Object... params){
        List<T> columnList;
        try{
            Connection conn = getConnection();
            columnList = QUERY_RUNNER.query(conn, sql, new ColumnListHandler<T>(), params);
        } catch (SQLException e){
            LOGGER.error("query list failure",e);
            throw new RuntimeException(e);
        }
        return columnList;
    }

    /**
     * 查询多行的同一列值(Unique)
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
     public static <T> Set<T> querySet(String sql, Object... params){
         Collection<T> valueList = queryList(sql,params);
         return new LinkedHashSet<T>(valueList);
     }

    /**
     * 查询并返回数组
     * @param sql
     * @param params
     * @return
     */
    public static Object[] queryArray(String sql, Object... params) {
        Object[] resultArray;
        try {
            Connection conn = getConnection();
            resultArray = QUERY_RUNNER.query(conn, sql, new ArrayHandler(), params);
            /*
            resultArray = QUERY_RUNNER.query(conn,sql, new ArrayHandler(new RowProcessor() {
                @Override
                public Object[] toArray(ResultSet rs) throws SQLException {
                    return new Object[0];
                }

                @Override
                public <T> T toBean(ResultSet rs, Class<T> type) throws SQLException {
                    return null;
                }

                @Override
                public <T> List<T> toBeanList(ResultSet rs, Class<T> type) throws SQLException {
                    return null;
                }

                @Override
                public Map<String, Object> toMap(ResultSet rs) throws SQLException {
                    return null;
                }
            }),params);
            */
        } catch (SQLException e) {
            LOGGER.error("query array failure", e);
            throw new RuntimeException(e);
        }
        return resultArray;
    }

    /**
     * 返回数组列表
     * @param sql
     * @param params
     * @return
     */
    public static List<Object[]> queryArrayList(String sql, Object... params) {
        List<Object[]> resultArrayList;
        try {
            Connection conn = getConnection();
            resultArrayList = QUERY_RUNNER.query(conn, sql, new ArrayListHandler(), params);
        } catch (SQLException e) {
            LOGGER.error("query array list failure", e);
            throw new RuntimeException(e);
        }
        return resultArrayList;
    }

    /**
     * 执行更新语句（包括：update、insert、delete）
     */
    public static int update(String sql, Object... params) {
        int rows;
        try {
            Connection conn = getConnection();
            rows = QUERY_RUNNER.update(conn, sql, params);
        } catch (SQLException e) {
            LOGGER.error("execute update failure", e);
            throw new RuntimeException(e);
        }
        return rows;
    }

    /**
     * 插入实体
     */
    public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap) {
        if (MapUtil.isEmpty(fieldMap)) {
            LOGGER.error("can not insert entity: fieldMap is empty");
            return false;
        }

        String sql = "INSERT INTO " + entityClass.getSimpleName();
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append(", ");
            values.append("?, ");
        }
        columns.replace(columns.lastIndexOf(", "), columns.length(), ")");
        values.replace(values.lastIndexOf(", "), values.length(), ")");
        sql += columns + " VALUES " + values;

        Object[] params = fieldMap.values().toArray();

        return update(sql, params) == 1;
    }

    /**
     * 更新实体
     */
    public static <T> boolean updateEntity(Class<T> entityClass, long id, Map<String, Object> fieldMap) {
        if (MapUtil.isEmpty(fieldMap)) {
            LOGGER.error("can not update entity: fieldMap is empty");
            return false;
        }

        String sql = "UPDATE " + entityClass.getSimpleName() + " SET ";
        StringBuilder columns = new StringBuilder();
        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append(" = ?, ");
        }
        sql += columns.substring(0, columns.lastIndexOf(", ")) + " WHERE id = ?";

        List<Object> paramList = new ArrayList<Object>();
        paramList.addAll(fieldMap.values());
        paramList.add(id);
        Object[] params = paramList.toArray();

        return update(sql, params) == 1;
    }

    /**
     * 删除实体
     */
    public static <T> boolean deleteEntity(Class<T> entityClass, long id) {
        String sql = "DELETE FROM " + entityClass.getSimpleName() + " WHERE id = ?";
        return update(sql, id) == 1;
    }

    /**
     * 执行 SQL 文件
     */
    public static void executeSqlFile(String filePath) {
        InputStream is = ClassUtil.getClassLoader().getResourceAsStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String sql;
            while ((sql = reader.readLine()) != null) {
                update(sql);
            }
        } catch (Exception e) {
            LOGGER.error("execute sql file failure", e);
            throw new RuntimeException(e);
        }
    }
}
