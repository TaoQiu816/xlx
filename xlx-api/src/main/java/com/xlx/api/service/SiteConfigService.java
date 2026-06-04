package com.xlx.api.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xlx.api.entity.SiteConfig;
import com.xlx.api.mapper.SiteConfigMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SiteConfigService {

    private final SiteConfigMapper mapper;

    public SiteConfigService(SiteConfigMapper mapper) {
        this.mapper = mapper;
    }

    public List<SiteConfig> listAll() {
        return mapper.selectList(new LambdaQueryWrapper<>());
    }

    public Map<String, String> getConfigMap() {
        List<SiteConfig> configs = listAll();
        Map<String, String> map = new HashMap<>();
        for (SiteConfig config : configs) {
            map.put(config.getConfigKey(), config.getConfigValue());
        }
        return map;
    }

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

    public void updateConfigs(Map<String, String> configs) {
        for (Map.Entry<String, String> entry : configs.entrySet()) {
            updateConfig(entry.getKey(), entry.getValue());
        }
    }
}
