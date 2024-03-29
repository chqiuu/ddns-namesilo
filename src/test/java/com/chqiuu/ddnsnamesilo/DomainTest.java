package com.chqiuu.ddnsnamesilo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class DomainTest {
    @Test
    public void test() {
        String domain = "chqiu2.chqiu.com";
        log.info(getMainDomain(domain));
        domain = "d.chqiu2.chqiu.com";
        log.info(getMainDomain(domain));
        domain = "chqiu.com";
        log.info(getMainDomain(domain));
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
