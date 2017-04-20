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

/**
 *
 * @author tomi
 */
public class TransactionCore {
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
        
        private boolean isSourceAccountExist(String from_no,String from_code){
            boolean isExist = false;
            String query = "select idAcc from accounts where idAcc = "+from_no+";";
            if(OpenConnetion()){
                try{
                    Statement state = conn.createStatement();
                    ResultSet rs = state.executeQuery(query);
                    if(rs.next()){
                        isExist = true;
                    }
                }catch(Exception ex){
                    System.out.println(ex.toString());
                }
                CloseConnection();
            }
            return isExist;
        }
        
        private boolean isTargetAccountExist(String to_no,String to_code){
            boolean isExist = false;
            String query = "select idAcc from accounts where idAcc = "+to_no+";";
            if(OpenConnetion()){
                try{
                    Statement state = conn.createStatement();
                    ResultSet rs = state.executeQuery(query);
                    if(rs.next()){
                        isExist = true;
                    }
                }catch(Exception ex){
                    System.out.println(ex.toString());
                }
                CloseConnection();
            }
            return isExist;
        }
        
        
        private boolean hasSourceAccSufficientMoney(String from_no,String ammount){
            boolean hasMoney = false;
            String query = "select balance from accounts where idAcc = "+from_no+";";
            double money;
            double money_from_db;
            try{
                money = Double.valueOf(ammount);
            }catch(Exception ex){
                return false;
            }
            if(OpenConnetion()){
                
                try{
                    Statement state = conn.createStatement();
                    ResultSet rs = state.executeQuery(query);
                    if(rs.next()){
                        money_from_db = rs.getDouble("balance");
                        if (money_from_db - money < 0){
                            hasMoney = false;
                        }else{
                            hasMoney = true;
                        }
                    }else{
                        hasMoney = false;
                    }
                }catch(Exception ex){
                    System.out.println(ex.toString());
                }
                CloseConnection();
            }
            return hasMoney;
        }
        
        public int checkValidOfTransaction(String from_no,String from_code, String ammount, String to_no, String to_code){
            if(!isSourceAccountExist(from_no, from_code)){
                return 1; //sourse account not exist
            }
            if(!isTargetAccountExist(to_no, to_code)){
                return 2; //target account is not exist
            }
            if (!hasSourceAccSufficientMoney(from_no, ammount)){
                return 3; //lack of money on source account
            }
            
            return 0;
        }
        /*--------------------------------------------------------------------*/
}
