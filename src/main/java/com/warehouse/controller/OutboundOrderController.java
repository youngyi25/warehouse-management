package com.warehouse.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.warehouse.common.Result;
import com.warehouse.entity.OutboundOrder;
import com.warehouse.service.OutboundOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/outbound")
public class OutboundOrderController {

    @Autowired
    private OutboundOrderService outboundOrderService;

    @PostMapping
    public Result<OutboundOrder> create(@RequestBody OutboundOrder outboundOrder) {
        outboundOrderService.save(outboundOrder);
        return Result.success(outboundOrder);
    }

    @GetMapping
    public Result<Page<OutboundOrder>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer status) {
        Page<OutboundOrder> pageParam = new Page<>(page, size);
        return Result.success(outboundOrderService.page(pageParam));
    }

    @GetMapping("/{id}")
    public Result<OutboundOrder> getById(@PathVariable Long id) {
        return Result.success(outboundOrderService.getById(id));
    }

    @PutMapping("/{id}/audit")
    public Result<Void> audit(@PathVariable Long id, @RequestBody OutboundOrder outboundOrder) {
        outboundOrder.setId(id);
        outboundOrderService.updateById(outboundOrder);
        return Result.success();
    }

    @PutMapping("/{id}/execute")
    public Result<Void> execute(@PathVariable Long id) {
        OutboundOrder outboundOrder = new OutboundOrder();
        outboundOrder.setId(id);
        outboundOrder.setStatus(2);
        outboundOrderService.updateById(outboundOrder);
        return Result.success();
    }
}