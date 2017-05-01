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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        /**
         * input validator
         * @param from_no
         * @param from_code
         * @param ammount
         * @param to_no
         * @param to_code
         * @return 
         */
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
        
        /**
         * realize transaction
         * @param from_no
         * @param from_code
         * @param ammount
         * @param to_no
         * @param to_code 
         * @param idEmp
         */
        public void doTransaction(String from_no,String from_code, String ammount, String to_no, String to_code, String idEmp){
            submitFromSource(from_no,ammount);
            addToTarget(to_no,ammount);
            createArchiveRecord(from_no,from_code,ammount,to_no,to_code,idEmp);
        }
        
        private void submitFromSource(String from_no,String ammount){
            double amm = Double.valueOf(ammount);
            amm = Math.abs(amm);
            String query = "update accounts set balance = balance - "+amm+" where idAcc = "+from_no+";";
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
        
        private void addToTarget(String to_no,String ammount){
            double amm = Double.valueOf(ammount);
            amm = Math.abs(amm);
            String query = "update accounts set balance = balance + "+amm+" where idAcc = "+to_no+";";
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
        
        private void createArchiveRecord(String from_no,String from_code, String ammount, String to_no, String to_code, String idEmp){
            Date dTime = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String datStr = sdf.format(dTime);
            if(to_code.equals("")){
                to_code = "0";
            }
            if(from_code.equals("")){
                from_code = "0";
            }
            
            String query = "insert into bank_transaction(amount,trans_datetime,description,idemp,dest_acc,src_acc,dest_code,src_code) "
                    + "values('"+ammount+"','"+datStr+"','bank transaction','"+idEmp+"','"+to_no+"','"+from_no+"','"+to_code+"','"+from_code+"');"; 
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
        
        public List<TransHistoryData> getTransactionhistory(String idEmp){
            List<TransHistoryData> data = new ArrayList();
            String query = "select * from bank_transaction where idemp= "+idEmp+";";
            if(OpenConnetion()){
                try{
                    Statement state = conn.createStatement();
                    ResultSet rs = state.executeQuery(query);
                    while(rs.next()){
                        TransHistoryData t = new TransHistoryData();
                        t.ammount = rs.getString("amount");
                        t.dect_code = rs.getString("dest_code");
                        t.description = rs.getString("description");
                        t.dest_acc = rs.getString("dest_acc");
                        t.src_acc = rs.getString("src_acc");
                        t.src_code = rs.getString("dest_code");
                        t.trans_date_time = rs.getString("trans_datetime");
                        
                        data.add(t);
                    }
                }catch(Exception ex){
                    System.out.println(ex.toString());
                }
                CloseConnection();
            }
            return data;
        }
}
