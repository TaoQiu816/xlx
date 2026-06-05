package com.xlx.api.config.controller;

import com.xlx.api.common.Result;
import com.xlx.api.config.entity.SiteConfig;
import com.xlx.api.config.service.SiteConfigService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 站点配置管理控制器（后台）。
 * <p>提供网站全局配置的查询和批量更新接口，需要 JWT 鉴权。</p>
 */
@RestController
@RequestMapping("/admin/site-config")
public class SiteConfigController {

    private final SiteConfigService service;

    public SiteConfigController(SiteConfigService service) {
        this.service = service;
    }

    /** 查询所有配置项 */
    @GetMapping
    public Result<List<SiteConfig>> list() {
        return Result.ok(service.listAll());
    }

    /** 批量更新配置（接受 key-value Map） */
    @PutMapping
    public Result<Void> update(@RequestBody Map<String, String> configs) {
        service.updateConfigs(configs);
        return Result.ok();
    }
}
