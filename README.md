# 安全隐患

平台资产安全隐患更多的来自于交易所内部，而非所谓的骇客。动不动就拿骇客说事的都是吹牛皮的

---

## 1.作弊的方式

盗取交易所资产的常见方式有两种：

- 代码作弊：直接开放maven仓库的deploy权限；
- 修改存储：修改db和cache中的资产数值；

代码可以通过git的提交日志锁定安全问题，所以我们今天主要讨论的是“修改存储”带来的安全隐患。

---

## 2.资产日志

某用户资产初始为0，经过3次修改，生成3条日志数据，最终用户资产为200.

| 索引  | 修改前金额 | 修改的金额 | 修改后金额 |
| --------| ------------ | ------------ | ------------ |
| 3       | 250        | -50        | 200        |
| 2       | 300        | -50        | 250        |
| 1       | 0            | 300        | 300        |

---

## 3.提币校验

通过当前资产余额和资产日志回放日志数据，如果数据对不上，则说明存在修改行为。

| 索引 | 修改前金额 | 修改后金额 | 修改后金额 |验证逻辑|
| ------------ | ------------ | ------------ | ------------ |------------ |
| 3       | 250        | -50        | 200        |余额+50 ==修改前金额==250 |
| 2       | 300        | -50        | 250        |索引3的修改前金额+50==修改前金额==300      |
| 1       | 0            | 300        | 300        |索引2的修改前金额-300==修改前金额==0       |

回溯日志能匹配就说明校验通过，整体逻辑没有问题。

* 问题：如果有人从第一条日志开始，同时修改了账号余额和日志数据，那么这个逻辑还是能够验证通过。
* 办法：在这个校验的逻辑基础之上，我们还需要加入一个认证的逻辑，即**认证我们的日志数据是否被修改过。**

---

## 4.日志防止篡改

* Hash chain，每一条数据都根据日志本身进行hash计算，得到数据指纹，且当前数据的hash，包含上一条数据的hash，可以确保消息的有序性。hash chain可以相对有效防止数据被篡改，但不能完全避免数据被篡改，比如把某一个用户的日志数据进行全量hash计算(从用户第一条财务日志开始修改记录和重新计算hash)，最终修改资产余额。
* SHA256withRSA，为了确保数据几乎完全不可能被修改，且确保数据能够合法的新增。在新增数据的时候，除了生成hash指纹，再在hash的基础上，对数据进行数字签名（同时会对hash签名）。为了确保数字签名中公钥和私钥的安全，会选择采用数字证书的方式存储整数密码，最终将所有的安全问题归集到数字证书密码安全的管理上。

---

## 5.模块说明

#### 模块说明

1. security-plugin-ca，证书管理模块
2. security-plugin-business，签名和验签模块，如果签证过期，自行生成一个签证，注意签证密码保护问题，因为是demo我写的比较粗糙

#### 使用说明

执行运行测试用例（如遇签名过期，使用KeyStore命令自行替换）：

com.plugin.wallet.security.test.SecurityHandlerTest

---

## 6.KeyStore命令

keytool -genkeypair -alias mykey -keyalg RSA -keysize 2048 -keystore keystore.jks -validity 365

这条命令的含义如下：

* `-genkeypair`：生成一对密钥（公钥和私钥）。
* `-alias mykey`：指定密钥对的别名。
* `-keyalg RSA`：指定密钥对的算法为 RSA。
* `-keysize 2048`：指定密钥的大小为 2048 位。
* `-keystore keystore.jks`：指定生成的密钥库文件名为 `keystore.jks`。
* `-validity 365`：设置密钥的有效期为 365 天。

