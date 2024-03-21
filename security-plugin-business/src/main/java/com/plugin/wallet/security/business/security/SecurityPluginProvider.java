package com.plugin.wallet.security.business.security;

import com.plugin.wallet.security.business.handle.SecuritySignHandler;
import com.plugin.wallet.security.business.handle.sign.HashSignHandler;
import com.plugin.wallet.security.business.handle.sign.MigrateSignHandler;
import com.plugin.wallet.security.business.handle.sign.SignSignHandler;
import com.plugin.wallet.security.business.model.AntLog;
import com.plugin.wallet.security.business.sort.IndexComparator;
import com.plugin.wallet.security.business.utils.Converts;
import com.plugin.wallet.security.ca.SecurityApi;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author : laoA
 * @describe : 安全管理接口实现
 * @email : laoa@markcoin.net
 */
@Slf4j
@Service
public class SecurityPluginProvider<T> extends SecurityPluginAbstract<T> {

    @Autowired
    private SecurityApi securityApi;

    @Override
    public void signature(T t,T last) throws IllegalAccessException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException, IOException {
        if(t==null||last==null){
            throw new RuntimeException("....t and last need not null");
        }
        //获取载体数据
        Map<String, Object> carrier = this.carrier(last);
        //计算流程
        SecuritySignHandler.Builder builder = new SecuritySignHandler.Builder();
        builder
                .addHandler(new MigrateSignHandler())
                .addHandler(new HashSignHandler())
                .addHandler(new SignSignHandler())
                .build()
                .doHandler(t,carrier);
    }

    @Override
    public Boolean validate(@NotNull List<T> logs, @NotNull BigDecimal lastAmount) throws IllegalAccessException, IOException, KeyStoreException, NoSuchAlgorithmException {
        if(logs==null||logs.size()==0||lastAmount==null){
            throw new RuntimeException("....logs need not null");
        }
        //数据类型转换
        List<AntLog> antLogs=new ArrayList<>();
        for(T t:logs){
            Map<String, Object> carrier = this.carrier(t);
            AntLog convert = Converts.convert(t, carrier);
            antLogs.add(convert);
        }
        //数据排序
        Collections.sort(antLogs, new IndexComparator());
        //开始验证
        for(int i=0;i<antLogs.size();i++){
            AntLog antLog = antLogs.get(i);
            //1.验证金额 变化前的金额+变化的金额=lastAmount
            BigDecimal checkAmount = antLog.getBalance().add(antLog.getTotalChangeValue());
            if(lastAmount.compareTo(checkAmount)!=0||lastAmount.compareTo(antLog.getLocked())!=0){
                log.error("....validate false , because lastAmount ！= checkAmount, antLog-->{}",antLog);
                return false;
            }else{
                lastAmount=antLog.getBalance();
            }
            //2.验证签名
            boolean signed = securityApi.keystore().signed(antLog.getSignMessage(), antLog.getSign());
            if(!signed){
                log.error("....validate false , because signed is false, antLog-->{}",antLog);
                return false;
            }
            //3.验证hash
            String digest = securityApi.hash().digest(antLog.getHashMessage());
            if(!digest.equals(antLog.getHash())){
                log.error("....validate false , because check hash is false, antLog-->{}",antLog);
                return false;
            }
            //4.验证pre hash
            if(i+1<antLogs.size()){
                String preHash = antLog.getPreHash();
                AntLog next=antLogs.get(i+1);
                if(!next.getHash().equals(preHash)){
                    log.error("....validate false , because check pre hash is false, antLog-->{}",antLog);
                    return false;
                }
            }
        }
        return true;
    }
}
