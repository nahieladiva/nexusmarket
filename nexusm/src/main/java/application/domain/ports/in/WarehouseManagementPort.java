package application.domain.ports.in;

import application.domain.models.Warehouse;
import application.domain.valueobjects.Address;
import application.domain.valueobjects.WarehouseId;
import application.domain.valueobjects.WarehouseLocation;

import java.util.List;

/**
 * Puerto de entrada (casos de uso) de gestión de almacenes.
 */
public interface WarehouseManagementPort {

    Warehouse createWarehouse(String name, Address address, WarehouseLocation location);

    Warehouse findWarehouseById(WarehouseId id);

    List<Warehouse> findAllWarehouses();
}