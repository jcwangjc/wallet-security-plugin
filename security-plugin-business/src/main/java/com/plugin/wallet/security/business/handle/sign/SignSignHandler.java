package com.plugin.wallet.security.business.handle.sign;

import com.alibaba.fastjson.JSONObject;
import com.plugin.wallet.security.business.handle.SecuritySignHandler;
import com.plugin.wallet.security.business.enums.SecurityElement;
import com.plugin.wallet.security.business.utils.Beans;
import com.plugin.wallet.security.business.utils.Converts;
import com.plugin.wallet.security.ca.SecurityApi;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Map;

/**
 * @author : laoA
 * @describe : 数据签名
 * @email : laoa@markcoin.net
 */
public class SignSignHandler extends SecuritySignHandler {
    @Override
    public <S> void doHandler(S t, Map<String, Object> carrierMap) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, IOException {
        //签名
        SecurityApi securityApi = Beans.getBean(SecurityApi.class);
        String message = JSONObject.toJSONString(carrierMap);
        String sign = securityApi.keystore().sign(message);
        Converts.bestow(t,sign, SecurityElement.SIGN);
    }
}
