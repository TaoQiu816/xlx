package com.xlx.api.inquiry.controller;

import com.alibaba.excel.EasyExcel;
import com.xlx.api.common.PageResult;
import com.xlx.api.common.Result;
import com.xlx.api.inquiry.dto.InquiryExcelDTO;
import com.xlx.api.inquiry.entity.Inquiry;
import com.xlx.api.inquiry.entity.InquiryItem;
import com.xlx.api.inquiry.service.InquiryService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 询盘管理控制器（后台）。
 * <p>提供询盘的查询、状态更新、备注管理接口，需要 JWT 鉴权。</p>
 */
@RestController
@RequestMapping("/admin/inquiries")
public class InquiryController {

    private final InquiryService service;

    public InquiryController(InquiryService service) {
        this.service = service;
    }

    /** 导出询盘为 Excel */
    @GetMapping("/export")
    public void export(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            HttpServletResponse response) throws Exception {
        List<Inquiry> inquiries = service.listAll(status, keyword, startDate, endDate);
        List<InquiryExcelDTO> dtos = inquiries.stream()
                .map(InquiryExcelDTO::from)
                .toList();

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String filename = "询盘列表_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".xlsx";
        response.setHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8));

        EasyExcel.write(response.getOutputStream(), InquiryExcelDTO.class)
                .sheet("询盘列表")
                .doWrite(dtos);
    }

    /** 分页查询询盘列表 */
    @GetMapping
    public Result<PageResult<Inquiry>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return Result.ok(service.list(page, size, status, keyword, startDate, endDate));
    }

    /** 获取询盘详情（含产品明细） */
    @GetMapping("/{id}")
    public Result<Map<String, Object>> detail(@PathVariable Long id) {
        Inquiry inquiry = service.getById(id);
        List<InquiryItem> items = service.getItems(id);
        Map<String, Object> data = new HashMap<>();
        data.put("inquiry", inquiry);
        data.put("items", items);
        return Result.ok(data);
    }

    /** 更新处理状态 */
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        service.updateStatus(id, body.get("status"));
        return Result.ok();
    }

    /** 更新后台备注 */
    @PutMapping("/{id}/remark")
    public Result<Void> updateRemark(@PathVariable Long id, @RequestBody Map<String, String> body) {
        service.updateRemark(id, body.get("remark"));
        return Result.ok();
    }

    /** 删除询盘 */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return Result.ok();
    }
}
