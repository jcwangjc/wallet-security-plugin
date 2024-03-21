package com.plugin.wallet.security.ca.content;

import java.util.prefs.Preferences;

/**
 * @author : laoa
 * @describe : 内存资源管理
 * @email : laoa@markcoin.net
 */
public class SecuritySecureManagement {
    public static String getCertificatePassword(String key) {
        Preferences prefs = Preferences.userNodeForPackage(SecuritySecureManagement.class);
        return prefs.get(key, "");
    }

    public static void setCertificatePassword(String key,String source) {
        Preferences prefs = Preferences.userNodeForPackage(SecuritySecureManagement.class);
        prefs.put(key, source);
    }
}
