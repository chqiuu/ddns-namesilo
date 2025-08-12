package com.chqiuu.ddnsnamesilo.task;

import cn.hutool.core.net.Ipv4Util;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.chqiuu.ddnsnamesilo.common.constant.Constant;
import com.chqiuu.ddnsnamesilo.config.DdnsProperties;
import com.chqiuu.ddnsnamesilo.dto.ResourceRecordDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        if (properties.getDomains() == null || properties.getDomains().isEmpty()) {
            log.error("请配置您的域名");
            return;
        }
        String localIpv4 = getLocalIpv4();
        if (StrUtil.isBlank(localIpv4)) {
            log.error("获取本机IP失败");
            return;
        }
        if (Arrays.asList(properties.getExcludeLocalIps()).contains(localIpv4)){
            log.warn("IP不做处理 {}", localIpv4);
            return;
        }
        String localIpv6 = getLocalIpv6();
        log.info("本机IPv4：{}", localIpv4);
        log.info("本机IPv6：{}", localIpv6);
        Constant.Public.DOMAIN_MAP = new HashMap<>();
        for (Map<String, String> domain : properties.getDomains()) {
            getListRecords(domain.get("domain"));
        }

        for (Map<String, String> domain : properties.getDomains()) {
            ResourceRecordDTO resourceRecord = Constant.Public.DOMAIN_MAP.get(domain.get("domain"));
            if (resourceRecord == null) {
                log.error("请检查namesilo是否配置正确！");
                return;
            }
            if (domain.get("type").equals("A")) {
                if (resourceRecord.getValue().equals(localIpv4)) {
                    log.info("IPv4一致，不需要更新映射IP localIp:{},domainIP:{}", localIpv4, resourceRecord.getValue());
                } else {
                    log.info("IPv4不一致，需要更新映射IP localIp:{},domainIP:{}", localIpv4, resourceRecord.getValue());
                    dnsUpdateRecord(resourceRecord, localIpv4);
                }
            } else if (domain.get("type").equals("AAAA")) {
                if (resourceRecord.getValue().equals(localIpv6)) {
                    log.info("IPv6一致，不需要更新映射IP localIp:{},domainIP:{}", localIpv6, resourceRecord.getValue());
                } else {
                    log.info("IPv6不一致，需要更新映射IP localIp:{},domainIP:{}", localIpv6, resourceRecord.getValue());
                    dnsUpdateRecord(resourceRecord, localIpv6);
                }
            }
        }
    }

    private void dnsUpdateRecord(ResourceRecordDTO resourceRecord, String localIp) {
        // 修改域名映射IP
        try {
            String url = String.format("https://www.namesilo.com/api/dnsUpdateRecord?version=1&type=xml&key=%s&domain=%s&rrid=%s&rrhost=%s&rrvalue=%s&rrttl=3601", properties.getNamesiloApiKey(), resourceRecord.getMainDomain(), resourceRecord.getRecordId(), resourceRecord.getHost().replace("." + resourceRecord.getMainDomain(), ""), localIp);
            log.info("设置url {}", url);
            String res = restTemplate.getForObject(url, String.class);
            if (res != null) {
                log.info("设置完成！{}", res);
            }
        } catch (Exception e) {
            log.error("namesilo连接失败1！");
        }
    }

    private void getListRecords(String domain) {
        String mainDomain = getMainDomain(domain);
        if (Constant.Public.DOMAIN_MAP.containsKey(domain)) {
            return;
        }
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
                        ResourceRecordDTO resourceRecord = new ResourceRecordDTO();
                        resourceRecord.setRecordId(nodeElement.element("record_id").getText());
                        resourceRecord.setType(nodeElement.element("type").getText());
                        resourceRecord.setMainDomain(mainDomain);
                        resourceRecord.setHost(nodeElement.element("host").getText());
                        resourceRecord.setValue(nodeElement.element("value").getText());
                        resourceRecord.setTtl(nodeElement.element("ttl").getText());
                        resourceRecord.setDistance(nodeElement.element("distance").getText());
                        Constant.Public.DOMAIN_MAP.put(nodeElement.element("host").getText(), resourceRecord);
                    }
                }
            }
        } catch (Exception e) {
            log.error("namesilo连接失败！");
        }
    }

    private String getLocalIpv4() {
        String localIp = null;
        String[] ipUrls = new String[]{"https://ip.3322.net", "https://4.ipw.cn", "https://myip.ipip.net", "https://ddns.oray.com/checkip"};
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
            if (StrUtil.isNotBlank(localIp) && Ipv4Util.ipv4ToLong(localIp, -1L) > 0) {
                break;
            }
        }
        return localIp;
    }

    private String getLocalIpv6() {
        // 获取本机IPv6
        String localIpv6 = null;
        try {
            String url = "https://api6.ipify.org?format=json";
            String res = restTemplate.getForObject(url, String.class);
            if (res != null) {
                log.info("获取本机IPv6 {}", res);
                JSONObject jsonObject = JSONObject.parseObject(res);
                if (jsonObject.containsKey("ip")) {
                    localIpv6 = jsonObject.getString("ip");
                    log.info("获取本机IPv6 localIpv6 {}", localIpv6);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取本机IPv6失败！");
        }
        return localIpv6;
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
