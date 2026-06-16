package com.warehouse.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.warehouse.common.Result;
import com.warehouse.entity.Location;
import com.warehouse.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @PostMapping
    public Result<Location> create(@RequestBody Location location) {
        locationService.save(location);
        return Result.success(location);
    }

    @GetMapping
    public Result<List<Location>> list(@RequestParam(required = false) Long warehouseId) {
        return Result.success(locationService.list());
    }

    @GetMapping("/{id}")
    public Result<Location> getById(@PathVariable Long id) {
        return Result.success(locationService.getById(id));
    }

    @PutMapping("/{id}")
    public Result<Location> update(@PathVariable Long id, @RequestBody Location location) {
        location.setId(id);
        locationService.updateById(location);
        return Result.success(location);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        locationService.removeById(id);
        return Result.success();
    }
}