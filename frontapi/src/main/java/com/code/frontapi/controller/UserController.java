package com.code.frontapi.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "用户报表", description = "流水api")
@Slf4j
@RestController
@RequestMapping(value = "/user/trans")
public class UserController {


}
