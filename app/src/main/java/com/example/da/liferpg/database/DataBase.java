package com.example.da.liferpg.database;

/**
 * Created by Da on 2016/12/8.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//�����ⲿ���ݿ�
//����Ҫ�ڲ�ͬ��ui���̵Ľ����н���
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
    public static boolean connect(){ //�������ݿ�
        String connectS = "jdbc:mysql://5878521d3d3fe.gz.cdb.myqcloud.com:17078/LifeRPG"
                +"?autoReconnect=true&useUnicode=true"
                +"&characterEncoding=UTF-8&useSSL=false";
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn= DriverManager.getConnection(connectS, "root", "llq123456");
            return true;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }
    public static void disConnect() throws SQLException {   //�ر����ݿ�����
        conn.close();
    }
    public static ResultSet Query(String sqlSentence) {  //��ѯ,�����ض���sql select���,����resultset�����
        Statement stat;
        ResultSet rs= null;
        try{
            stat = conn.createStatement(); //��ȡִ��sql���Ķ���
            rs= stat.executeQuery(sqlSentence); //ִ��sql��ѯ�����ؽ����
        } catch(Exception e) {
            System.out.println(e.getMessage()); //���������Ϣ
        }
        return rs;
    }
    public static boolean QueryExisted(String userName) throws SQLException {  //��ѯע���û����Ƿ��Ѿ�����,����true����false
        Statement stat;
        ResultSet rs= null;
        try{
            stat = conn.createStatement(); //��ȡִ��sql���Ķ���
            rs= stat.executeQuery("select userName from user where userName = '"+userName+"';"); //ִ��sql��ѯ�����ؽ����
        } catch(Exception e) {
            System.out.println(e.getMessage()); //���������Ϣ
        }
        if(rs.next()) //�������Ϊ��,˵�������û���,�߼��ϻ���Щ����
            return true;
        else
            return false;
    }
    public static int Insert(String sqlSentence) {  //����,ͬ������sql insert���,�����ݲ����ض��ı��
        Statement stat;
        int insertResult= 0;
        try{
            stat = conn.createStatement(); //��ȡִ��sql���Ķ���
            insertResult= stat.executeUpdate(sqlSentence); //ִ��sql��ѯ,�����Ԫ����Ŀ
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return insertResult;
    }
    public static int Delete(String sqlSentence) {  //����,ͬ������sql insert���,�����ݲ����ض��ı��
        Statement stat;
        int deleteResult= 0;
        try{
            stat = conn.createStatement(); //��ȡִ��sql���Ķ���
            deleteResult= stat.executeUpdate(sqlSentence); //ִ��sql��ѯ,�����Ԫ����Ŀ
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return deleteResult;
    }
    public static int Update(String sqlSentence) {  //����,ͬ������sql insert���,�����ݲ����ض��ı��
        Statement stat;
        int updateResult= 0;
        try{
            stat = conn.createStatement(); //��ȡִ��sql���Ķ���
            updateResult= stat.executeUpdate(sqlSentence); //ִ��sql��ѯ,�����Ԫ����Ŀ
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return updateResult;
    }
}

