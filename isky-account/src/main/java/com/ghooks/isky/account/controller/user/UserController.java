package com.ghooks.isky.account.controller.user;

import com.alibaba.fastjson.JSONObject;
import org.ghooks.isky.commons.web.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @RequestMapping("info")
    public String info() throws Exception {
        return "hello";
    }

    @RequestMapping("test")
    public JSONObject test() throws Exception {
        return writeSuccess();
    }
}
