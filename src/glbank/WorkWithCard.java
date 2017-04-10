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
import java.util.Random;

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
        private boolean isCardNumberUniqu(String str_val){
            String querey = "select cardnumber from cards where cardnumber = "+str_val+" ;";
            boolean isUnique = false;
            if(OpenConnetion()){
                try{
                    Statement state = conn.createStatement();
                    ResultSet rs = state.executeQuery(querey);
                    if(rs.next()){       
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
        private long generate_cardNumber(){
            long value = 0;
            Random rand = new Random();
            String str_val = "";
            do{
                char[] arr = new char[12];
                arr[0] = (char) (rand.nextInt(8)+49);
                for (int i =1 ; i<arr.length;i++){
                    arr[i] = (char) (rand.nextInt(10)+48);
                }
                str_val = String.valueOf(arr);
            }while(!isCardNumberUniqu(str_val));
            value = Long.valueOf(str_val);
            
            return value;
        }
        private String generatePinCode(){
            String pin = "";
            char[] pin_arr = new char[4];
            Random rand = new Random();
            for (int i=0;i<pin_arr.length;i++){
                pin_arr[i] = (char) (rand.nextInt(10)+48);
            }
            pin = String.valueOf(pin_arr);
            return pin;
        }
        /**
         * create the new account
         * @param accId 
         */
        public void createNewCard(String accId){
            long cardNumber = generate_cardNumber();
            String pin_code = generatePinCode();
            String qurey = "insert into cards(cardnumber,idacc,pin_code) values("+cardNumber+", "+accId+",'"+pin_code+"');";
            if(OpenConnetion()){
                try{
                    Statement state = conn.createStatement();
                    state.executeUpdate(qurey);
                }catch(Exception e){
                    System.out.println(e.toString());
                }              
                CloseConnection();
            }
        }
        
        public CardContent getCartInfo(String cardNumber){
            CardContent cc = new CardContent();
            String query = "select * from cards where cardnumber = "+cardNumber+";";
            if(OpenConnetion()){
                try{
                    Statement state = conn.createStatement();
                    ResultSet rs = state.executeQuery(query);
                    if(rs.next()){
                        cc.accountNumber = rs.getLong("idacc");
                        cc.cardNumber = rs.getLong("cardnumber");
                        cc.idCard = rs.getInt("idcard");
                        cc.pinCode = rs.getString("pin_code");
                        cc.wrongTry = rs.getInt("vrong_try");
                        cc.isBlocked = rs.getString("blocked").equals("T");
                    }
                }catch(Exception e){
                    System.out.println(e.toString());
                }
                CloseConnection();
            }
            return cc;
        }
        
        public List<String> getCardList(String idAcc){
            List<String> card_list = new ArrayList<String>();
            String query = "select cardnumber from cards where idacc= "+idAcc+" ;";
            if(OpenConnetion()){
                try{
                    Statement state = conn.createStatement();
                    ResultSet rs = state.executeQuery(query);
                    while(rs.next()){
                        String pom;
                        pom = rs.getString("cardnumber");
                        card_list.add(pom);
                    }
                }catch(Exception e){
                    System.out.println(e.toString());
                }
                CloseConnection();
            }
            return card_list;
        }
}
