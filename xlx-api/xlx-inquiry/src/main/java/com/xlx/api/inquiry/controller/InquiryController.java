package com.xlx.api.inquiry.controller;

import com.xlx.api.common.PageResult;
import com.xlx.api.common.Result;
import com.xlx.api.inquiry.entity.Inquiry;
import com.xlx.api.inquiry.service.InquiryService;
import org.springframework.web.bind.annotation.*;

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

    /** 获取询盘详情 */
    @GetMapping("/{id}")
    public Result<Inquiry> detail(@PathVariable Long id) {
        return Result.ok(service.getById(id));
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
