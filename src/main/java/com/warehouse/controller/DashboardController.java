package com.warehouse.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.warehouse.common.Result;
import com.warehouse.entity.*;
import com.warehouse.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private InboundOrderMapper inboundOrderMapper;

    @Autowired
    private OutboundOrderMapper outboundOrderMapper;

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();

        // 商品总数
        long productCount = productMapper.selectCount(new LambdaQueryWrapper<Product>().eq(Product::getDeleted, 0));
        stats.put("productCount", productCount);

        // 库存总量
        List<Inventory> inventories = inventoryMapper.selectList(null);
        int totalQuantity = inventories.stream().mapToInt(Inventory::getQuantity).sum();
        int totalFrozen = inventories.stream().mapToInt(Inventory::getFrozenQuantity).sum();
        stats.put("totalQuantity", totalQuantity);
        stats.put("totalFrozen", totalFrozen);

        // 今日入库单数
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        long todayInbound = inboundOrderMapper.selectCount(
            new LambdaQueryWrapper<InboundOrder>()
                .ge(InboundOrder::getCreateTime, todayStart)
                .le(InboundOrder::getCreateTime, todayEnd)
        );
        stats.put("todayInbound", todayInbound);

        // 今日出库单数
        long todayOutbound = outboundOrderMapper.selectCount(
            new LambdaQueryWrapper<OutboundOrder>()
                .ge(OutboundOrder::getCreateTime, todayStart)
                .le(OutboundOrder::getCreateTime, todayEnd)
        );
        stats.put("todayOutbound", todayOutbound);

        // 仓库数量
        long warehouseCount = warehouseMapper.selectCount(null);
        stats.put("warehouseCount", warehouseCount);

        return Result.success(stats);
    }

    @GetMapping("/inbound-trend")
    public Result<List<Map<String, Object>>> getInboundTrend() {
        List<Map<String, Object>> result = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            LocalDateTime start = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime end = LocalDateTime.of(date, LocalTime.MAX);

            long count = inboundOrderMapper.selectCount(
                new LambdaQueryWrapper<InboundOrder>()
                    .ge(InboundOrder::getCreateTime, start)
                    .le(InboundOrder::getCreateTime, end)
            );

            Map<String, Object> item = new HashMap<>();
            item.put("date", date.format(DateTimeFormatter.ofPattern("MM-dd")));
            item.put("count", count);
            result.add(item);
        }

        return Result.success(result);
    }

    @GetMapping("/outbound-trend")
    public Result<List<Map<String, Object>>> getOutboundTrend() {
        List<Map<String, Object>> result = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            LocalDateTime start = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime end = LocalDateTime.of(date, LocalTime.MAX);

            long count = outboundOrderMapper.selectCount(
                new LambdaQueryWrapper<OutboundOrder>()
                    .ge(OutboundOrder::getCreateTime, start)
                    .le(OutboundOrder::getCreateTime, end)
            );

            Map<String, Object> item = new HashMap<>();
            item.put("date", date.format(DateTimeFormatter.ofPattern("MM-dd")));
            item.put("count", count);
            result.add(item);
        }

        return Result.success(result);
    }

    @GetMapping("/warehouse-inventory")
    public Result<List<Map<String, Object>>> getWarehouseInventory() {
        List<Inventory> inventories = inventoryMapper.selectList(null);

        Map<Long, Integer> warehouseQuantityMap = inventories.stream()
            .collect(Collectors.groupingBy(Inventory::getWarehouseId, Collectors.summingInt(Inventory::getQuantity)));

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : warehouseQuantityMap.entrySet()) {
            Warehouse warehouse = warehouseMapper.selectById(entry.getKey());
            Map<String, Object> item = new HashMap<>();
            item.put("name", warehouse != null ? warehouse.getName() : "未知仓库");
            item.put("value", entry.getValue());
            result.add(item);
        }

        return Result.success(result);
    }

    @GetMapping("/category-distribution")
    public Result<List<Map<String, Object>>> getCategoryDistribution() {
        List<Inventory> inventories = inventoryMapper.selectList(null);

        // 获取所有商品
        Map<Long, Long> productCategoryMap = new HashMap<>();
        List<Product> products = productMapper.selectList(new LambdaQueryWrapper<Product>().eq(Product::getDeleted, 0));
        for (Product product : products) {
            productCategoryMap.put(product.getId(), product.getCategoryId());
        }

        // 按分类汇总库存数量
        Map<Long, Integer> categoryQuantityMap = new HashMap<>();
        for (Inventory inv : inventories) {
            Long categoryId = productCategoryMap.get(inv.getProductId());
            if (categoryId != null) {
                categoryQuantityMap.merge(categoryId, inv.getQuantity(), Integer::sum);
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : categoryQuantityMap.entrySet()) {
            Category category = categoryMapper.selectById(entry.getKey());
            Map<String, Object> item = new HashMap<>();
            item.put("name", category != null ? category.getName() : "未分类");
            item.put("value", entry.getValue());
            result.add(item);
        }

        return Result.success(result);
    }

    @GetMapping("/recent-orders")
    public Result<Map<String, Object>> getRecentOrders() {
        Map<String, Object> result = new HashMap<>();

        List<InboundOrder> recentInbound = inboundOrderMapper.selectList(
            new LambdaQueryWrapper<InboundOrder>()
                .orderByDesc(InboundOrder::getCreateTime)
                .last("LIMIT 5")
        );
        result.put("inbound", recentInbound);

        List<OutboundOrder> recentOutbound = outboundOrderMapper.selectList(
            new LambdaQueryWrapper<OutboundOrder>()
                .orderByDesc(OutboundOrder::getCreateTime)
                .last("LIMIT 5")
        );
        result.put("outbound", recentOutbound);

        return Result.success(result);
    }
}
