/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package glbank;

/**
 *
 * @author tomi
 */
public class Clients {
    private int idc;
    private String first_name;
    private String last_name;
    private boolean isAcrive;

    public int getIdc() {
        return idc;
    }

    public void setIdc(int idc) {
        this.idc = idc;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public boolean isIsAcrive() {
        return isAcrive;
    }

    public void setIsAcrive(boolean isAcrive) {
        this.isAcrive = isAcrive;
    }
}
