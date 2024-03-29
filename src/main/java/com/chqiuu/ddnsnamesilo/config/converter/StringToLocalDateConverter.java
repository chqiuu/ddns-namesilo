package com.chqiuu.ddnsnamesilo.config.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;

/**
 * Controller中接收时间格式参数
 * 处理2020-12-20格式的日期格式
 *
 * @author CHENQUAN
 * @date 2017-06-20
 */
public class StringToLocalDateConverter implements Converter<String, LocalDate> {

    @Override
    public LocalDate convert(String stringDate) {
        return LocalDate.parse(stringDate);
    }
}