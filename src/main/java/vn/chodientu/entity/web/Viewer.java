package vn.chodientu.entity.web;

import java.io.Serializable;
import java.util.List;
import vn.chodientu.entity.db.Administrator;
import vn.chodientu.entity.db.Order;
import vn.chodientu.entity.db.User;

/**
 *
 * @author phugt
 */
public class Viewer implements Serializable {

    private User user;
    private Administrator administrator;
    private boolean cpAuthRequired;
    private List<Order> cart;
    private Order invoice;
    private List<Order> invoiceSeries;
    private String topup;
    private String shipchung;
    private String fname;
    private String fid;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Administrator getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
    }

    public boolean isCpAuthRequired() {
        return cpAuthRequired;
    }

    public void setCpAuthRequired(boolean cpAuthRequired) {
        this.cpAuthRequired = cpAuthRequired;
    }

    public List<Order> getCart() {
        return cart;
    }

    public void setCart(List<Order> cart) {
        this.cart = cart;
    }

    public Order getInvoice() {
        return invoice;
    }

    public void setInvoice(Order invoice) {
        this.invoice = invoice;
    }

    public List<Order> getInvoiceSeries() {
        return invoiceSeries;
    }

    public void setInvoiceSeries(List<Order> invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
    }

    public String getTopup() {
        return topup;
    }

    public void setTopup(String topup) {
        this.topup = topup;
    }

    public String getShipchung() {
        return shipchung;
    }

    public void setShipchung(String shipchung) {
        this.shipchung = shipchung;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }
    
    

}
