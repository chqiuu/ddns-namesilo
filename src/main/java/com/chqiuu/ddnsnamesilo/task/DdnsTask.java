package com.chqiuu.ddnsnamesilo.task;

import cn.hutool.core.net.Ipv4Util;
import cn.hutool.core.util.StrUtil;
import com.chqiuu.ddnsnamesilo.common.constant.Constant;
import com.chqiuu.ddnsnamesilo.config.DdnsProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class DdnsTask {
    private final DdnsProperties properties;
    private final RestTemplate restTemplate;

    /**
     * <p>
     * https://www.namesilo.com/account/api-manager
     * <p>
     * 执行频率，每次启动项目后固定频率执行 30分钟
     */
    @Scheduled(initialDelay = 5000, fixedDelay = 30 * 60 * 1000)
    public void ddnsTask() {
        String[] ipUrls = new String[]{"https://ip.3322.net", "https://4.ipw.cn", "https://myip.ipip.net", "https://ddns.oray.com/checkip"};
        String localIp = null;
        for (String url : ipUrls) {
            try {
                String res = restTemplate.getForObject(url, String.class);
                if (res != null) {
                    // log.info("{} return {}", url, res);
                    if (res.trim().startsWith("Current IP Address: ")) {
                        localIp = res.substring(res.indexOf("Current IP Address: ") + "Current IP Address: ".length()).trim();
                    } else if (res.trim().startsWith("当前 IP：")) {
                        localIp = res.substring(res.indexOf("当前 IP：") + "当前 IP：".length(), res.indexOf("  ")).trim();
                    } else {
                        localIp = res.trim();
                    }
                    log.info("{} localIp [{}] {}", url, localIp, Ipv4Util.ipv4ToLong(localIp));
                }
            } catch (Exception e) {
                log.error("{} 获取IP失败！", url);
            }
            if (StrUtil.isNotBlank(localIp) && Ipv4Util.ipv4ToLong(localIp, -1L) < 0) {
                break;
            }
        }
        if (properties.getDomains() == null || properties.getDomains().length == 0) {
            log.error("请配置您的域名");
            return;
        }
        String mainDomain = getMainDomain(properties.getDomains()[0]);
        Constant.Public.DOMAIN_MAP = new HashMap<>();
        try {
            String url = String.format("https://www.namesilo.com/api/dnsListRecords?version=1&type=xml&key=%s&domain=%s", properties.getNamesiloApiKey(), mainDomain);
            log.info("列表 url {}", url);
            // 获取当前域名映射IP
            String res = restTemplate.getForObject(url, String.class);
            if (res != null) {
                log.info("列表： {}", res);
                org.dom4j.Document document = DocumentHelper.parseText(res);
                List<Node> nodeList = document.selectNodes("//reply/resource_record");
                for (Node node : nodeList) {
                    if (node instanceof Element) {
                        Element nodeElement = (Element) node;
                        for (String domain : properties.getDomains()) {
                            if (domain.equals(nodeElement.element("host").getText())) {
                                Constant.Public.DOMAIN_IP_ADDRESS = nodeElement.element("value").getText();
                                Constant.Public.DOMAIN_MAP.put(nodeElement.element("record_id").getText(), domain.equals(mainDomain) ? "www" : domain.replace("." + mainDomain, ""));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("namesilo连接失败！");
        }
        log.info("当前域名映射IP：{}", Constant.Public.DOMAIN_IP_ADDRESS);
        if (Constant.Public.DOMAIN_IP_ADDRESS == null) {
            log.error("当前域名映射IP为空，请检查namesilo是否配置正确！");
            return;
        }
        if (localIp.equals(Constant.Public.DOMAIN_IP_ADDRESS)) {
            log.info("IP一致，不需要更新映射IP localIp:{},domainIP:{}", localIp, Constant.Public.DOMAIN_IP_ADDRESS);
        } else {
            log.info("IP不一致，需要更新映射IP localIp:{},domainIP:{}", localIp, Constant.Public.DOMAIN_IP_ADDRESS);
            // 修改域名映射IP
            try {
                Constant.Public.DOMAIN_MAP.forEach((recordId, domain) -> {
                    String url = String.format("https://www.namesilo.com/api/dnsUpdateRecord?version=1&type=xml&key=%s&domain=%s&rrid=%s&rrhost=%s&rrvalue=%s&rrttl=7207", properties.getNamesiloApiKey(), mainDomain, recordId, domain, Constant.Public.DOMAIN_IP_ADDRESS);
                    log.info("设置url {}", url);
                    String res = restTemplate.getForObject(url, String.class);
                    if (res != null) {
                        log.info("设置完成！{}", res);
                    }
                });
            } catch (Exception e) {
                log.error("namesilo连接失败1！");
            }
        }
    }

    private String getMainDomain(String domain) {
        int pointCount = domain.length() - domain.replace(".", "").length();
        String rootDomain = null;
        if (pointCount > 1) {
            String[] domains = domain.split("\\.");
            rootDomain = domains[domains.length - 2] + "." + domains[domains.length - 1];
        } else {
            rootDomain = domain;
        }
        return rootDomain;
    }
}
