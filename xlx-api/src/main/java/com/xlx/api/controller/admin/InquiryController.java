package com.xlx.api.controller.admin;

import com.xlx.api.common.PageResult;
import com.xlx.api.common.Result;
import com.xlx.api.entity.Inquiry;
import com.xlx.api.service.InquiryService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/inquiries")
public class InquiryController {

    private final InquiryService service;

    public InquiryController(InquiryService service) {
        this.service = service;
    }

    @GetMapping
    public Result<PageResult<Inquiry>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status) {
        return Result.ok(service.list(page, size, status));
    }

    @GetMapping("/{id}")
    public Result<Inquiry> detail(@PathVariable Long id) {
        return Result.ok(service.getById(id));
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        service.updateStatus(id, body.get("status"));
        return Result.ok();
    }

    @PutMapping("/{id}/remark")
    public Result<Void> updateRemark(@PathVariable Long id, @RequestBody Map<String, String> body) {
        service.updateRemark(id, body.get("remark"));
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return Result.ok();
    }
}
