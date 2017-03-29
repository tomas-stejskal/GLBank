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
        /**
         * this method insert client to database
         * @param f_name
         * @param l_name 
         */
        
        public void insertNewClient(String f_name,String l_name,String email,String username,String dob, String street,String postcode,String house_number,String city){
            String query = "insert into clients(firstname,lastname) values('"+f_name+"','"+l_name+"');";
            String passw = generatePassword();
            String client_id = "";
            
            if(OpenConnetion()){
                try{
                    Statement state = conn.createStatement();
                    state.executeUpdate(query);
                }catch(Exception e){
                    System.out.println(e.toString());
                }
                CloseConnection();
            }
            client_id = getNewUserId(f_name, l_name);
            insertToClientdetails(client_id,street,house_number,postcode,dob,email,city);
            insertToClientlogin(client_id,username,passw);
        }
        
        private String getNewUserId(String f_name,String l_name){
            String result = "";
            String query = "select max(idc) from clients where firstname like '"+f_name+"' and lastname like '"+l_name+"';";
            if(OpenConnetion()){
                try{
                    Statement state = conn.createStatement();
                    ResultSet rs =  state.executeQuery(query);
                    if(rs.next()){
                        result = rs.getString("max(idc)");
                    }
                }catch(Exception e){
                    System.out.println(e.toString());
                }
                
                CloseConnection();
            }
            
            return result;
        }
        
        private void insertToClientdetails(String idc,String street,String housenumber,String postcode,String dob,String email,String city){
            String query = "insert into clientdetails(idc,street,housenumber,postcode,dob,email,city)"
                    + "values('"+idc+"','"+street+"','"+housenumber+"','"+postcode+"','"+dob+"','"+email+"','"+city+"');";
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
        
        private void insertToClientlogin(String idc,String login,String passw){
            String query ="insert into loginclient(idc,login,password) values('"+idc+"','"+login+"','"+passw+"');";
            
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
        
}
