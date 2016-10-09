package com.crazy.controller;

import com.crazy.mapper.AccountMapper;
import com.crazy.model.Account;
import com.crazy.util.ConvertJson;
import com.crazy.util.DateUtil;
import com.crazy.util.Encryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * Created by SHIKUN on 2016/9/30.
 */

@RestController
@RequestMapping(value = "/account")
public class AccountController {
    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private ConvertJson convertJson;

    @Autowired
    private Encryption encryption;

    @Autowired
    private DateUtil dateUtil;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public int addAccount(@RequestBody Account account){

        System.out.println(account.getExt_params());

        return accountMapper.addAcount(account.getUsername(), account.getName(), account.getIcon(),
                encryption.doEncryption(account.getPassword()),
                account.getMobile(), account.getCreate_time(), account.getUpdate_time(), account.getEmail(),
                convertJson.Map2Json(account.getExt_params()));
    }

    @RequestMapping(value = "/list/username", method = RequestMethod.GET)
    public List<String> getAllUsername() {
        return accountMapper.getAllUsername();
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String checkAccount(@RequestBody Account account, @RequestHeader(value = "User-Agent") String useragent,
                               HttpServletRequest request) {

        String result = null;
        String token = null;
        Map<String, String> selectResult = accountMapper.getCheckInfo(account.getUsername());

        if (selectResult == null) {
            result = "没有此用户";
        } else if (!encryption.checkPassword(account.getPassword(), selectResult.get("password"))) {
            result = "密码错误";
        } else {
            token=encryption.createToken();
            accountMapper.addLoginLog(request.getRemoteAddr(), token, dateUtil.Str2Date(dateUtil.getNowTime()),
                    dateUtil.Str2Date(dateUtil.setExpire(30)), accountMapper.getUserId(account.getUsername()),
                    useragent,account.getUsername());
            result = token;
        }
        return result;
    }



}
