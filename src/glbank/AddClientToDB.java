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
import java.util.Random;

/**
 *
 * @author tomi
 */
public class AddClientToDB {
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
        /**
         * verified that user name is unique
         * @param login
         * @return 
         */
        public boolean isLoginUnique(String login){
            String query = "select * from loginclient where login like '"+login+"';";
            boolean isUniqu = false;
            if(OpenConnetion()){
                try{
                    Statement state = conn.createStatement();
                    ResultSet rs =  state.executeQuery(query);
                    if(rs.next()){
                        isUniqu = false;
                    }else{
                        isUniqu = true;
                    }
                }catch(Exception e){
                    System.out.println(e.toString());
                }
                CloseConnection();
            }
            System.out.println(generatePassword());
            return isUniqu;
        }
        
        private String generatePassword(){
            String passw = "";
            Random rand = new Random();
            int cross;
            for (int i=0;i<8;i++){
                cross = rand.nextInt(3);
                if(cross == 0){
                    passw += (char) (rand.nextInt(10) + 48);
                }else if(cross == 1){
                    passw += (char)(rand.nextInt(26) + 65);
                }else{
                    passw += (char) (rand.nextInt(26) + 97);
                }
            }
            
            return passw;
        }
        
}
