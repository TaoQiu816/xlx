package com.xlx.api.controller.admin;

import com.xlx.api.common.Result;
import com.xlx.api.entity.SiteConfig;
import com.xlx.api.service.SiteConfigService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/site-config")
public class SiteConfigController {

    private final SiteConfigService service;

    public SiteConfigController(SiteConfigService service) {
        this.service = service;
    }

    @GetMapping
    public Result<List<SiteConfig>> list() {
        return Result.ok(service.listAll());
    }

    @PutMapping
    public Result<Void> update(@RequestBody Map<String, String> configs) {
        service.updateConfigs(configs);
        return Result.ok();
    }
}
