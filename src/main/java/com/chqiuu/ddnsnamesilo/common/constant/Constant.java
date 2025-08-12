package com.chqiuu.ddnsnamesilo.common.constant;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.chqiuu.ddnsnamesilo.dto.ResourceRecordDTO;

import java.util.Map;

public class Constant {

    /**
     * 系统默认值
     */
    public static class DefaultValues {

    }

    /**
     * 系统全局变量
     */
    public static class Public {
        /**
         * 初始化ID生成器，调用方式 SNOW_FLAKE.nextId();
         */
        public static Snowflake SNOW_FLAKE = IdUtil.getSnowflake(1, 2);
        /**
         * record_id map
         */
        public static Map<String, ResourceRecordDTO> DOMAIN_MAP;
        /**
         * Domain mapping IP
         */
        public static String DOMAIN_IP_ADDRESS;

        public Public() {
        }
    }

    /**
     * Session 属性名公共变量
     */
    public static class SessionAttr {
        /**
         * 当前登录用户信息
         */
        public static final String INFO = "INFO";
        /**
         * 图形验证码参数名
         */
        public static final String IMAGE_CODE = "IMAGE_CODE";

        public SessionAttr() {
        }
    }

    /**
     * 资源文件路径
     */
    public static class ResourcePath {
        /**
         * 静态资源跟路径（不带resource）
         */
        public static String LOCAL_RESOURCE_PATH;
        /**
         * 上传文件临时存储路径
         */
        public static String FILE_DIR = "";
        /**
         * 临时文件存储路径
         */
        public static String FILE_DIR_TMP = "";

        public ResourcePath() {
        }
    }
}
