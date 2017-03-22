/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package glbank;

import com.mysql.jdbc.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

/**
 *
 * @author tomi
 */
public class DatabaseInterface {
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "glbank";
        String driver = "com.mysql.jdbc.Driver";
        String username = "root";
        String password = "1234";
        
        Connection conn = null;
        
        private boolean OpenConnetion(){
            try{
                Class.forName(driver).newInstance();
                conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(url + dbName,username,password);
                return true;
            }catch(Exception e){
                System.out.println(e.toString());
                return false;
            }
        }
        
        private boolean CloseConnection(){
            try{
                conn.close();
                return true;
            }catch(Exception e){
                System.out.println(e.toString());
                return false;
            }
        }
        /**********************************************************************/
        //login user to system if login credentials is OK then retrun user ID, if login credentials is wrong then return -1
        public int Login(String username,String password){
            String query = "select idemp from loginemployees where login like'"+username+"' and password like '"+password+"';";
            int employee_id = -1;
            if (OpenConnetion()){
                try{
                    Statement state = conn.createStatement();
                    ResultSet rs =  state.executeQuery(query);
                    
                    if (rs.next()){
                        employee_id = rs.getInt("idemp");
                    }else{
                        employee_id = -1;
                    }
                }catch(Exception e){
                    System.out.println(e.toString());
                } 
                CloseConnection();
            }
            return employee_id;
        }
        /**********************************************************************/
        // save login hystory to database
        public void HistoryOfLogin(int id){
            //mysql format YYYY-MM-DD HH:MM:SS
            Date dTime = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String datStr = sdf.format(dTime);
            //System.out.println(datStr);
            String query = "insert into historyloginemployee(idemp, login_date) values('"+id+"','"+datStr+"');";
            if(OpenConnetion()){
                try{
                    Statement state = conn.createStatement();
                    state.executeUpdate(query);
                }catch(Exception e){
                    System.out.println(e.toString());
                }
                CloseConnection();
            }
        }
        /**********************************************************************/
        public String[] getEmpNames(int id){
            String query = " select * from employees where idemp = "+id+" ;";
            String[] result = new String[4];
            if (OpenConnetion()){ 
                try{
                    Statement state = conn.createStatement();
                    ResultSet rs =  state.executeQuery(query);
                    if (rs.next()){
                        result[0] = rs.getString("firstname");
                        result[1] = rs.getString("lastname");
                        result[2] = rs.getString("email");
                        result[3] = rs.getString("isDirector");
                    }
                }catch(Exception e){
                    System.out.println(e.toString());
                }
                CloseConnection();
                return result;
            }
            return null;
        }
}
