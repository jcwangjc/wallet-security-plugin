package com.plugin.wallet.security.test;

import com.plugin.wallet.security.business.SecurityPlugin;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityHandlerTest {

    @Autowired
    SecurityPlugin securityPlugin;

    ////////////模拟批量签名和验签//////////////////////////////
    @Test
    public void test1() throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, IOException, IllegalAccessException {
        //模拟创建批量财务日志
        List<LogInfo> logList = createLogList();
        LogInfo t=null;
        //已入库的最后一条日志，如果没有直接new一个
        LogInfo last=new LogInfo();
        for(int i=0;i<logList.size();i++){
            t=logList.get(i);
            //给每一条日志进行签名
            securityPlugin.signature(t,last);
            last=t;
        }

        log.info("----> step 1 end");
        //1.到这里为止，等于我们的某一个用户，连续产生了多比财务日志的记录。并且数据已经入库

        //2.用户准备提币了

        //3.我进入的提币审核的阶段了，或者说到了准备放款的阶段了，我需要对用户的余额进行一个合法性的校验

        //4.我需要校验，是不是需要把所有的数据都查询出来，我直接使用logList数据表示被查询出来的数据

        //在做数据校验的时候，查询资产余额，得到余额和日志索引，根据日志索引去查询小于资产余额上保存的索引编号的日志
        Boolean validate = securityPlugin.validate(logList, new BigDecimal("10"));
        log.info("----> step 2 end , validate result ---> {}",validate);

        //日志索引和资产余额需要放到同一个地方，比如钱包模型里面，获取资产的同时，获取到索引。
        //获取索引的目的，是为了根据这个索引去查询小于这个索引值的所有的财务日志（可以是指定周期内的）
    }

    private LogInfo createLog(Long id,BigDecimal balance,BigDecimal locked,BigDecimal totalChangeValue,Long index){
        LogInfo logInfo=new LogInfo();
        logInfo.setId(id);
        logInfo.setBalance(balance);
        logInfo.setLocked(locked);
        logInfo.setTotalChangeValue(totalChangeValue);
        logInfo.setA("a");
        logInfo.setB("b");
        logInfo.setC("c");
        logInfo.setD("d");
        logInfo.setIndex(index);
        return logInfo;
    }

    private List<LogInfo> createLogList(){
        List<LogInfo> logs=new ArrayList<>();
        BigDecimal balance=BigDecimal.ZERO;
        for(long i = 0 ; i < 10 ; i++){
            BigDecimal locked=balance.add(BigDecimal.ONE);
            LogInfo log = createLog(i,balance,locked,BigDecimal.ONE,i+1);
            logs.add(log);
            balance=locked;
        }
        return logs;
    }

    ////////////模拟单条数据的签名//////////////////////////////
    @Test
    public void test2() throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, IOException, IllegalAccessException {
        LogInfo t = getT();
        securityPlugin.signature(t,getLast());
        log.info("----> end");
    }

    private LogInfo getT(){
        LogInfo logInfo=new LogInfo();
        logInfo.setId(2l);
        return logInfo;
    }

    private LogInfo getLast(){
        LogInfo logInfo=new LogInfo();
//        logInfo.setId(1l);
//        logInfo.setHash("222223333");
//        logInfo.setIndex(1001l);
        return logInfo;
    }

    ////////////模拟验签//////////////////////////////
    @Test
    public void test3() throws IllegalAccessException, NoSuchAlgorithmException, KeyStoreException, IOException {
        LogInfo logInfo=new LogInfo();
        logInfo.setId(1l);
        logInfo.setBalance(new BigDecimal("100"));
        logInfo.setTotalChangeValue(new BigDecimal("10"));
        logInfo.setLocked(new BigDecimal("110"));
        logInfo.setIndex(100l);
        logInfo.setHash("aaaa");
        logInfo.setPreHash("bbb");
        logInfo.setSign("cccc");
        List<LogInfo> logs=new ArrayList<>();
        logs.add(logInfo);

        LogInfo logInfo2=new LogInfo();
        logInfo2.setId(2l);
        logInfo2.setBalance(new BigDecimal("100"));
        logInfo2.setTotalChangeValue(new BigDecimal("10"));
        logInfo2.setLocked(new BigDecimal("110"));
        logInfo2.setIndex(101l);
        logInfo2.setHash("aaaa");
        logInfo2.setPreHash("bbb");
        logInfo2.setSign("cccc");
        logs.add(logInfo2);
        securityPlugin.validate(logs,new BigDecimal("100"));
    }
}
