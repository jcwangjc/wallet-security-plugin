package com.plugin.wallet.security.business.sort;

import com.plugin.wallet.security.business.model.AntLog;

import java.util.Comparator;

/**
 * @author : laoA
 * @describe : 泛型对象排序逻辑
 * @email : laoa@markcoin.net
 */
public class IndexComparator implements Comparator<AntLog> {
    @Override
    public int compare(AntLog log1, AntLog log2) {
        return Long.compare(log2.getIndex(), log1.getIndex());
    }
}
