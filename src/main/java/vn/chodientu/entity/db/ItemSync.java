/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vn.chodientu.entity.db;

import org.springframework.data.annotation.Id;
import vn.chodientu.entity.enu.ShipmentType;
import vn.chodientu.entity.enu.SyncType;

/**
 *
 * @author Phan
 */
public class ItemSync implements java.io.Serializable  {
    @Id
    private String id;
    private SyncType syncType;
    private String sellerId;
    private boolean nlIntegrated;
    private boolean scIntegrated;
    private ShipmentType shipmentType;
    private int shipmentPrice;
    private long time;
    private boolean sync;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SyncType getSyncType() {
        return syncType;
    }

    public void setSyncType(SyncType syncType) {
        this.syncType = syncType;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public boolean isNlIntegrated() {
        return nlIntegrated;
    }

    public void setNlIntegrated(boolean nlIntegrated) {
        this.nlIntegrated = nlIntegrated;
    }

    public boolean isScIntegrated() {
        return scIntegrated;
    }

    public void setScIntegrated(boolean scIntegrated) {
        this.scIntegrated = scIntegrated;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public ShipmentType getShipmentType() {
        return shipmentType;
    }

    public void setShipmentType(ShipmentType shipmentType) {
        this.shipmentType = shipmentType;
    }

    public int getShipmentPrice() {
        return shipmentPrice;
    }

    public void setShipmentPrice(int shipmentPrice) {
        this.shipmentPrice = shipmentPrice;
    }
    
}
