package com.plugin.wallet.security.business;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.List;

/**
 * @author : laoA
 * @describe : 安全管理接口
 * @email : laoa@markcoin.net
 */
@Service
public interface SecurityPlugin<T> {
    /**
     * 签名接口
     * @param t 需要签名的log日志
     * @param last 已经完成签名的上一个log日志，没有的话就new一个空对象
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws IOException
     */
    public void signature(@NotNull T t,@NotNull T last) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, IOException, IllegalAccessException;


    /**
     * 验签接口
     * @param logs 需要校验的日志记录
     * @param lastAmount 账户余额
     * @return
     */
    public Boolean validate(@NotNull List<T> logs,@NotNull BigDecimal lastAmount) throws IllegalAccessException, IOException, KeyStoreException, NoSuchAlgorithmException;
}
