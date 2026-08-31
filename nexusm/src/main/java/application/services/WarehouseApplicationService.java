package application.services;

import application.domain.exceptions.ResourceNotFoundException;
import application.domain.models.Warehouse;
import application.domain.ports.in.WarehouseManagementPort;
import application.domain.ports.out.WarehouseRepository;
import application.domain.valueobjects.Address;
import application.domain.valueobjects.WarehouseId;
import application.domain.valueobjects.WarehouseLocation;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

/**
 * Servicio de aplicación que implementa los casos de uso de gestión de almacenes.
 */
@Service
public class WarehouseApplicationService implements WarehouseManagementPort {

    private final WarehouseRepository warehouseRepository;

    public WarehouseApplicationService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = Objects.requireNonNull(warehouseRepository,
            "warehouseRepository es obligatorio");
    }

    @Override
    public Warehouse createWarehouse(String name, Address address, WarehouseLocation location) {
        return warehouseRepository.save(Warehouse.create(name, address, location));
    }

    @Override
    public Warehouse findWarehouseById(WarehouseId id) {
        return warehouseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Almacén", id.toString()));
    }

    @Override
    public List<Warehouse> findAllWarehouses() {
        return warehouseRepository.findAll();
    }
}