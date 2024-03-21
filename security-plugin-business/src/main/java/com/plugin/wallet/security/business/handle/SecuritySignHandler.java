package com.plugin.wallet.security.business.handle;


import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Map;

/**
 * @author : laoA
 * @describe : 按步校验处理逻辑
 * @email : laoa@markcoin.net
 */
public abstract class SecuritySignHandler {
    protected static final long FIRST_INDEX=1;
    protected SecuritySignHandler next;
    // 返回handler方便链式操作
    public SecuritySignHandler next(SecuritySignHandler next) {
        this.next = next;
        return next;
    }
    // 流程开始的方法
    public abstract <S> void doHandler(S t, Map<String, Object> carrierMap) throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException, IOException, IllegalAccessException;
    // 编译
    public static class Builder{
        private SecuritySignHandler head;
        private SecuritySignHandler tail;
        public Builder addHandler(SecuritySignHandler handler) {
            if (this.head == null) {
                this.head = this.tail = handler;
                return this;
            }
            this.tail.next(handler);
            this.tail = handler;
            return this;
        }
        public SecuritySignHandler build() {
            return this.head;
        }
    }
}
