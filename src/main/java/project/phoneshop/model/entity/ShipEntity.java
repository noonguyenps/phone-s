package project.phoneshop.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "\"ships\"")
@NoArgsConstructor
@RestResource(exported = false)
public class ShipEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ship_id\"")
    private int shipId;
    @Column(name = "\"ship_type\"")
    private String shipType;
    @Column(name = "\"ship_price\"")
    private double shipPrice;


    public int getShipId() {
        return shipId;
    }

    public void setShipId(int shipId) {
        this.shipId = shipId;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public double getShipPrice() {
        return shipPrice;
    }

    public void setShipPrice(double shipPrice) {
        this.shipPrice = shipPrice;
    }

    @Override
    public String toString() {
        return "ShipEntity{" +
                "shipType='" + shipType + '\'' +
                ", shipPrice=" + shipPrice +
                '}';
    }
}
