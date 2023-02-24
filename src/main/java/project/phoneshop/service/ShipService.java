package project.phoneshop.service;

import project.phoneshop.model.entity.ShipEntity;

import java.util.List;

public interface ShipService {
    List<ShipEntity> getAll();
    ShipEntity findShipById(int id);

    ShipEntity create(ShipEntity ship);
    ShipEntity update(ShipEntity ship);
    void delete(int id);
}
