package application.adapters.in.rest.controllers;

import application.adapters.in.rest.mappers.WarehouseMapper;
import application.adapters.in.rest.requests.CreateWarehouseRequest;
import application.adapters.in.rest.responses.WarehouseResponse;
import application.domain.ports.in.WarehouseManagementPort;
import application.domain.valueobjects.WarehouseId;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Expone los casos de uso de gestión de almacenes vía REST.
 */
@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final WarehouseManagementPort warehouseManagementPort;
    private final WarehouseMapper warehouseMapper;

    public WarehouseController(WarehouseManagementPort warehouseManagementPort,
                               WarehouseMapper warehouseMapper) {
        this.warehouseManagementPort = warehouseManagementPort;
        this.warehouseMapper = warehouseMapper;
    }

    @PostMapping
    public ResponseEntity<WarehouseResponse> create(
            @RequestBody CreateWarehouseRequest request) {
        application.domain.models.Warehouse domain =
            warehouseMapper.toDomain(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            warehouseMapper.toResponse(
                warehouseManagementPort.createWarehouse(
                    domain.getName(),
                    domain.getAddress(),
                    domain.getLocation())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(warehouseMapper.toResponse(
            warehouseManagementPort.findWarehouseById(WarehouseId.of(id))));
    }

    @GetMapping
    public ResponseEntity<List<WarehouseResponse>> findAll() {
        return ResponseEntity.ok(warehouseManagementPort.findAllWarehouses().stream()
            .map(warehouseMapper::toResponse).toList());
    }
}