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
 * @describe : 处理数据的hash
 * @email : laoa@markcoin.net
 */
public class HashSignHandler extends SecuritySignHandler {
    @Override
    public <S> void doHandler(S t, Map<String, Object> carrierMap) throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException, IOException, IllegalAccessException {
        //得到hash
        SecurityApi securityApi = Beans.getBean(SecurityApi.class);
        Map<String, Object> params = Converts.convert(t);
        String data = JSONObject.toJSONString(params);
        String hash = securityApi.hash().digest(data);
        //设置hash
        String hashName = Converts.bestow(t, hash, SecurityElement.HASH);
        params.put(hashName,hash);
        next.doHandler(t,params);
    }
}
