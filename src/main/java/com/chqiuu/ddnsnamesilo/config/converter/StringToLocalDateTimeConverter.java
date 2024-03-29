package com.chqiuu.ddnsnamesilo.config.converter;

import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;

/**
 * Controller中接收时间格式参数
 *
 * @author CHENQUAN
 * @date 2017-06-20
 */
@Slf4j
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

    @Override
    public LocalDateTime convert( String stringDate) {
        return LocalDateTimeUtil.parse(stringDate);
    }
}