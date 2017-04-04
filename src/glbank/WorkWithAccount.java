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
import java.util.Random;

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
            if(OpenConnetion()){
                try{
                    Statement state = conn.createStatement();
                    ResultSet rs =  state.executeQuery(query);
                    while(rs.next()){
                        String r = rs.getString("idAcc");
                        result.add(r);
                    }
                }catch(Exception e){
                    System.out.println(e.toString());
                }
                CloseConnection();
            }
            return result;
        }
        
        public String getBalance(String idAcc){
            String balance = "0";
            String query = "select balance from accounts where idAcc = "+idAcc+";";
            if(OpenConnetion()){
                try{
                    Statement state = conn.createStatement();
                    ResultSet rs =  state.executeQuery(query);
                    if(rs.next()){
                        balance = rs.getString("balance");
                    }
                }catch(Exception e){
                    System.out.println(e.toString());
                }
                
             CloseConnection();
            }
            return balance;
        }
        
        public void addToBalance(double sum,String idAcc,String id_client,String id_emp,String bank_code){
            String query = "update accounts set balance = balance + "+sum+" where idAcc = "+idAcc+";";
            if(OpenConnetion()){
                try{
                    Statement state = conn.createStatement();
                    state.executeUpdate(query);
                }catch(Exception e){
                    System.out.println(e.toString());
                } 
                CloseConnection();
            }
            archive_transaction(sum,idAcc,id_emp,bank_code);
        }
        
        public void submitFromBalance(double sum,String idAcc,String id_client,String id_emp,String bank_code){
            String query = "update accounts set balance = balance - "+sum+" where idAcc = "+idAcc+";";
            if(OpenConnetion()){
                try{
                    Statement state = conn.createStatement();
                    state.executeUpdate(query);
                }catch(Exception e){
                    System.out.println(e.toString());
                }
                CloseConnection();
            }
            archive_transaction(-sum,idAcc,id_emp,bank_code);
        }
        /**********************************************************************/
        private boolean isAccIdUniqu(double idac){
            String query = "select idAcc from accounts where idc = "+idac+";";
            boolean isUnique = false;
            if(OpenConnetion()){
                try{
                    Statement state = conn.createStatement();
                    ResultSet rs = state.executeQuery(query);
                    if(rs.next()){
                        isUnique = false;
                    }else{
                        isUnique = true;
                    }
                }catch(Exception e){
                    System.out.println(e.toString());
                }
                CloseConnection();
            }
            return isUnique;
        }
        
        private double generateAccountID(){  
            Random rand = new Random();
            double idAcc = 0;
            do{
                String idAc = "";
                idAc += (char) (rand.nextInt(8)+49);
                for(int i =0;i<8;i++){
                    idAc += (char) (rand.nextInt(10)+48);
                }
                idAcc = Double.valueOf(idAc);
                idAcc *= 11;
                idAcc = Math.abs(idAcc);
            }while(!isAccIdUniqu(idAcc));
            
            return idAcc;
        }
        
        /**
         * method which create a new account
         * @param idc 
         */
        public void createNewAccount(String idc){
            double idAcc = generateAccountID();
            String query = "insert into accounts(idAcc,idc,balance) values("+idAcc+","+idc+",0);";
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
        
        private void archive_transaction(double ammount,String idAcc,String id_emp,String bank_code){
            Date dTime = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String datStr = sdf.format(dTime);
            String query = "insert into bank_transaction(amount,trans_datetime,idemp,dest_acc,dest_code,src_acc,src_code,description) "
                         + "values("+ammount+",'"+datStr+"','"+id_emp+"','"+idAcc+"','"+bank_code+"','0','0','cash operation')";
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
