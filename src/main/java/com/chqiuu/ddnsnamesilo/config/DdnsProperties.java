package com.chqiuu.ddnsnamesilo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Data
@Order(-1)
@Component
@ConfigurationProperties(prefix = "chqiuu.ddns")
public class DdnsProperties {
    /**
     * 是否启用API接口
     * swagger-enable
     */
    private boolean swaggerEnable = false;
    /**
     * 本机静态资源根路径，默认文件JAR包相对路径
     * local-resource-path
     */
    private String localResourcePath;
    /**
     * namesilo-api-key
     */
    private String namesiloApiKey;
    /**
     * 域名列表
     */
    private String[] domains;
    /**
     * 域名列表
     */
    private String[] excludeLocalIps;
}
