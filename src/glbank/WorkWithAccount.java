/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package glbank;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tomi
 */
public class WorkWithAccount {
        private String url = "jdbc:mysql://localhost:3306/";
        private String dbName = "glbank";
        private String driver = "com.mysql.jdbc.Driver";
        private String username = "root";
        private String password = "1234";
        
        private Connection conn = null;
        
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
        /**
         * load list account list from DB
         * @param idc
         * @return 
         */
        public List<String> loadAccountsByIDC(String idc){
            List<String> result = new ArrayList<String>();
            String query = "select idAcc from accounts where idc = "+idc+";";
            try{
                Statement state = conn.createStatement();
                ResultSet rs =  state.executeQuery(query);
                while(rs.next()){
                    result.add(rs.getString("idAcc"));
                    System.out.println(result.get(0));
                }
            }catch(Exception e){
                System.out.println(e.toString());
            }
            return result;
        }
}
