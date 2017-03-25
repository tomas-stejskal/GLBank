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
import java.util.ArrayList;
import java.util.List;

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
        /**
         * login user to system if login credentials is OK then return user ID, if login credentials is wrong then return -1
         * @param username
         * @param password
         * @return 
         */
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
        /**
         * save login history to database
         * @param id 
         */
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
        /**
         * get all information about user:
         * first name, last name, email, permissions
         * parm must by use ID
         * @param id
         * @return 
         */
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
        /**********************************************************************/
        /**
         * function return list of all clients from database for clients combobox
         * @param onlyActice
         * @return 
         */
        public List<Clients> getListOfAllClients(boolean onlyActice){
            String query;
            if (onlyActice){
                query = "select * from Clients where disable like 'F' order by lastname asc;";
            }else{
                query = "select * from Clients order by lastname asc;";
            }
            
            List<Clients> cl_list = new ArrayList<Clients>();
            
            if (OpenConnetion()){
                try{
                    Statement state = conn.createStatement();
                    ResultSet rs =  state.executeQuery(query);
                    while(rs.next()){
                        Clients pom = new Clients();
                        pom.setIdc( rs.getInt("idc"));
                        pom.setFirst_name(rs.getString("firstname"));
                        pom.setLast_name(rs.getString("lastname"));
                        if (rs.getString("disable").equals("F")){
                            pom.setIsAcrive(true);
                        }else{
                            pom.setIsAcrive(false);
                        }
                        cl_list.add(pom);
                    }
                }catch(Exception e){
                    System.out.println(e.toString());
                }
                CloseConnection();
                
                return cl_list;
            }
            return null;
        }
        /***********************************************************************/
        /**
         * this method returns list of all details about the client
         * @param id
         * @return 
         */
        public ClientDetail getCelientDetail(int id){
            String query = "select Clients.idc,firstname,lastname,disable,street,housenumber,postcode,dob,email from Clients "
                    + "inner join ClientDetails on ClientDetails.idc=Clients.idc where Clients.idc="+id+";";
            ClientDetail cd = new ClientDetail();
            
            if(OpenConnetion()){
                try{
                    Statement state = conn.createStatement();
                    ResultSet rs =  state.executeQuery(query);
                    if(rs.next()){
                        cd.idc = rs.getInt("idc");
                        cd.firstname = rs.getString("firstname");
                        cd.lastanme = rs.getString("lastname");
                        cd.email = rs.getString("email");
                        cd.street = rs.getString("street");
                        cd.postcode = rs.getString("postcode");
                        cd.houseNumber = rs.getInt("housenumber");
                        if(rs.getString("disable").equals("F")){
                            cd.isActive = true;
                        }else{
                            cd.isActive = false;
                        }
                        cd.dob = rs.getString("dob");
                    }
                }catch(Exception e){
                    System.out.println(e.toString());
                }
                
                
                CloseConnection();
            }            
            return cd;
        }
        /**********************************************************************/
        /**
         * this method activate client
         * @param id 
         */
        public void activateClient(int id){
            String query = "update clients set disable='F' where idc="+id+";";
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
        /**
         * this method deactivate client
         * @param id 
         */
        public void deactivateClient(int id){
            String query = "update clients set disable='T' where idc="+id+";";
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
