/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package glbank;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;

/**
 *
 * @author tomi
 */
public class WorkWithCard {
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
        
        private void createNewCard(String accId){
            
        }
}
