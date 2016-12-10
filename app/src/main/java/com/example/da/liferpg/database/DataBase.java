package com.example.da.liferpg.database;

/**
 * Created by Da on 2016/12/8.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//连接外部数据库
//必须要在不同于ui进程的进程中进行
/**
 * Created by Da on 2016/12/4.
 */
public class DataBase{
    static private Connection conn;
    static int cnt = 0;
    /*
    public static void main(String args[]){
        DataBase db = new DataBase();
        db.connect();
        db.Update("create table a(name int(8));");
    }*/
    public void Database(){};
    public static boolean connect(){ //连接数据库
        String connectS = "jdbc:mysql://5848e2053c8ed.gz.cdb.myqcloud.com:14242/RPG"
                +"?autoReconnect=true&useUnicode=true"
                +"&characterEncoding=UTF-8&useSSL=false";
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn= DriverManager.getConnection(connectS, "root", "dada1996");
            return true;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }
    public static void disConnect() throws SQLException {   //关闭数据库连接
        conn.close();
    }
    public ResultSet Query(String sqlSentence) {  //查询,传入特定的sql select语句,返回resultset结果集
        Statement stat;
        ResultSet rs= null;
        try{
            stat = conn.createStatement(); //获取执行sql语句的对象
            rs= stat.executeQuery(sqlSentence); //执行sql查询，返回结果集
        } catch(Exception e) {
            System.out.println(e.getMessage()); //输出错误信息
        }
        return rs;
    }
    public boolean QueryExisted(String userName) throws SQLException {  //查询注册用户名是否已经存在,返回true或者false
        Statement stat;
        ResultSet rs= null;
        try{
            stat = conn.createStatement(); //获取执行sql语句的对象
            rs= stat.executeQuery("select userName from user where userName = '"+userName+"';"); //执行sql查询，返回结果集
        } catch(Exception e) {
            System.out.println(e.getMessage()); //输出错误信息
        }
        if(rs.next()) //结果集不为空,说明存在用户名,逻辑上还有些问题
            return true;
        else
            return false;
    }
    public int Insert(String sqlSentence) {  //插入,同样传入sql insert语句,将数据插入特定的表格
        Statement stat;
        int insertResult= 0;
        try{
            stat = conn.createStatement(); //获取执行sql语句的对象
            insertResult= stat.executeUpdate(sqlSentence); //执行sql查询,插入的元组数目
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return insertResult;
    }
    public int Delete(String sqlSentence) {  //插入,同样传入sql insert语句,将数据插入特定的表格
        Statement stat;
        int deleteResult= 0;
        try{
            stat = conn.createStatement(); //获取执行sql语句的对象
            deleteResult= stat.executeUpdate(sqlSentence); //执行sql查询,插入的元组数目
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return deleteResult;
    }
    public int Update(String sqlSentence) {  //插入,同样传入sql insert语句,将数据插入特定的表格
        Statement stat;
        int updateResult= 0;
        try{
            stat = conn.createStatement(); //获取执行sql语句的对象
            updateResult= stat.executeUpdate(sqlSentence); //执行sql查询,插入的元组数目
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return updateResult;
    }
}

