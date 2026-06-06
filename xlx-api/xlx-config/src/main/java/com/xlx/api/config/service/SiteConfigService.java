package com.xlx.api.config.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xlx.api.config.entity.SiteConfig;
import com.xlx.api.config.mapper.SiteConfigMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 站点配置服务。
 * <p>提供网站全局配置的读取和更新操作，采用键值对存储模式。
 * 更新时如果键不存在则自动创建（upsert 模式）。</p>
 */
@Service
public class SiteConfigService {

    private final SiteConfigMapper mapper;

    public SiteConfigService(SiteConfigMapper mapper) {
        this.mapper = mapper;
    }

    /** 查询所有配置项 */
    public List<SiteConfig> listAll() {
        return mapper.selectList(new LambdaQueryWrapper<>());
    }

    /** 获取配置 Map（key → value），用于前台接口返回 */
    public Map<String, String> getConfigMap() {
        List<SiteConfig> configs = listAll();
        Map<String, String> map = new HashMap<>();
        for (SiteConfig config : configs) {
            map.put(config.getConfigKey(), config.getConfigValue());
        }
        return map;
    }

    /** 不对外暴露的敏感配置键 */
    private static final Set<String> SENSITIVE_KEYS = Set.of(
            "mail_password", "mail_username", "mail_host", "mail_port",
            "mail_to", "mail_from_name", "mail_tls", "mail_enabled"
    );

    /** 获取前台安全配置（过滤敏感键） */
    public Map<String, String> getPublicConfigMap() {
        Map<String, String> all = getConfigMap();
        all.keySet().removeAll(SENSITIVE_KEYS);
        return all;
    }

    /** 更新单个配置项（不存在则创建） */
    public void updateConfig(String key, String value) {
        SiteConfig existing = mapper.selectOne(
                new LambdaQueryWrapper<SiteConfig>().eq(SiteConfig::getConfigKey, key)
        );
        if (existing != null) {
            existing.setConfigValue(value);
            mapper.updateById(existing);
        } else {
            SiteConfig config = new SiteConfig();
            config.setConfigKey(key);
            config.setConfigValue(value);
            mapper.insert(config);
        }
    }

    /** 批量更新配置项 */
    public void updateConfigs(Map<String, String> configs) {
        for (Map.Entry<String, String> entry : configs.entrySet()) {
            updateConfig(entry.getKey(), entry.getValue());
        }
    }
}
