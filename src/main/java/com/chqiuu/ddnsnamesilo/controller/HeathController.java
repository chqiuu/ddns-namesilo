package com.chqiuu.ddnsnamesilo.controller;

import com.chqiuu.ddnsnamesilo.common.base.BaseController;
import com.chqiuu.ddnsnamesilo.common.domain.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping()
@Api(value = "页面跳转控制类", tags = "页面跳转控制类")
@RequiredArgsConstructor
public class HeathController extends BaseController {
    @ApiOperation(value = "hello", notes = "hello")
    @GetMapping("/hello")
    public Result<String> hello() {
        return Result.ok("hello");
    }
}
