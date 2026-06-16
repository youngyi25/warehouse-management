package com.warehouse.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.warehouse.common.Result;
import com.warehouse.entity.InboundOrder;
import com.warehouse.service.InboundOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inbound")
public class InboundOrderController {

    @Autowired
    private InboundOrderService inboundOrderService;

    @PostMapping
    public Result<InboundOrder> create(@RequestBody InboundOrder inboundOrder) {
        inboundOrderService.save(inboundOrder);
        return Result.success(inboundOrder);
    }

    @GetMapping
    public Result<Page<InboundOrder>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer status) {
        Page<InboundOrder> pageParam = new Page<>(page, size);
        return Result.success(inboundOrderService.page(pageParam));
    }

    @GetMapping("/{id}")
    public Result<InboundOrder> getById(@PathVariable Long id) {
        return Result.success(inboundOrderService.getById(id));
    }

    @PutMapping("/{id}/audit")
    public Result<Void> audit(@PathVariable Long id, @RequestBody InboundOrder inboundOrder) {
        inboundOrder.setId(id);
        inboundOrderService.updateById(inboundOrder);
        return Result.success();
    }

    @PutMapping("/{id}/execute")
    public Result<Void> execute(@PathVariable Long id) {
        InboundOrder inboundOrder = new InboundOrder();
        inboundOrder.setId(id);
        inboundOrder.setStatus(2);
        inboundOrderService.updateById(inboundOrder);
        return Result.success();
    }
}