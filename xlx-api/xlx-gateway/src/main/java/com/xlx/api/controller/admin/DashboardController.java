package com.xlx.api.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xlx.api.common.Result;
import com.xlx.api.dto.DashboardStats;
import com.xlx.api.dto.DashboardStats.StatusItem;
import com.xlx.api.dto.DashboardStats.TrendItem;
import com.xlx.api.inquiry.entity.Inquiry;
import com.xlx.api.inquiry.mapper.InquiryMapper;
import com.xlx.api.product.entity.Product;
import com.xlx.api.product.mapper.ProductMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Dashboard 统计接口（后台）。
 * 聚合产品和询盘数据，供后台首页展示。
 */
@RestController
@RequestMapping("/admin/dashboard")
public class DashboardController {

    private static final Map<String, String> STATUS_LABELS = Map.of(
            "new", "新询盘",
            "contacted", "已联系",
            "quoted", "已报价",
            "sample_confirmed", "样品沟通中",
            "order_confirmed", "订单确认",
            "closed", "已关闭",
            "invalid", "无效询盘"
    );

    private final ProductMapper productMapper;
    private final InquiryMapper inquiryMapper;

    public DashboardController(ProductMapper productMapper, InquiryMapper inquiryMapper) {
        this.productMapper = productMapper;
        this.inquiryMapper = inquiryMapper;
    }

    /**
     * 获取 Dashboard 聚合统计数据
     *
     * @param trendDays 趋势天数，默认 7
     */
    @GetMapping("/stats")
    public Result<DashboardStats> stats(
            @RequestParam(defaultValue = "7") int trendDays) {

        DashboardStats stats = new DashboardStats();

        // 产品统计
        stats.setProductTotal(productMapper.selectCount(null));
        stats.setProductOnline(productMapper.selectCount(
                new LambdaQueryWrapper<Product>().eq(Product::getStatus, 1)));

        // 询盘统计
        stats.setInquiryTotal(inquiryMapper.selectCount(null));
        stats.setInquiryPending(inquiryMapper.selectCount(
                new LambdaQueryWrapper<Inquiry>().eq(Inquiry::getStatus, "new")));

        // 询盘趋势（最近 N 天按天统计）
        stats.setInquiryTrend(buildTrend(trendDays));

        // 询盘状态分布
        stats.setInquiryStatus(buildStatusDistribution());

        // 最新 5 条询盘
        stats.setLatestInquiries(buildLatestInquiries());

        return Result.ok(stats);
    }

    /** 构建最近 N 天的询盘趋势数据 */
    private List<TrendItem> buildTrend(int days) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("M/d");

        // 查询最近 N 天的所有询盘
        LocalDate startDate = today.minusDays(days - 1);
        List<Inquiry> inquiries = inquiryMapper.selectList(
                new LambdaQueryWrapper<Inquiry>()
                        .ge(Inquiry::getCreatedAt, startDate.atStartOfDay())
                        .orderByAsc(Inquiry::getCreatedAt)
        );

        // 按日期分组计数
        Map<String, Integer> dateCountMap = new LinkedHashMap<>();
        for (int i = days - 1; i >= 0; i--) {
            dateCountMap.put(today.minusDays(i).format(fmt), 0);
        }
        for (Inquiry inq : inquiries) {
            String key = inq.getCreatedAt().format(fmt);
            dateCountMap.merge(key, 1, Integer::sum);
        }

        return dateCountMap.entrySet().stream()
                .map(e -> new TrendItem(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    /** 构建询盘状态分布 */
    private List<StatusItem> buildStatusDistribution() {
        List<Inquiry> all = inquiryMapper.selectList(null);
        Map<String, Long> countByStatus = all.stream()
                .collect(Collectors.groupingBy(Inquiry::getStatus, Collectors.counting()));

        return STATUS_LABELS.entrySet().stream()
                .map(e -> new StatusItem(e.getKey(), e.getValue(),
                        countByStatus.getOrDefault(e.getKey(), 0L).intValue()))
                .filter(item -> item.getCount() > 0)
                .collect(Collectors.toList());
    }

    /** 构建最新 5 条询盘 */
    private List<Map<String, Object>> buildLatestInquiries() {
        List<Inquiry> list = inquiryMapper.selectList(
                new LambdaQueryWrapper<Inquiry>()
                        .orderByDesc(Inquiry::getCreatedAt)
                        .last("LIMIT 5")
        );
        return list.stream().map(inq -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", inq.getId());
            map.put("name", inq.getName());
            map.put("company", inq.getCompany());
            map.put("productName", inq.getProductName());
            map.put("status", inq.getStatus());
            map.put("createdAt", inq.getCreatedAt());
            return map;
        }).collect(Collectors.toList());
    }
}
