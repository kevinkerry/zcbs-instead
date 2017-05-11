package com.zcbspay.platform.instead.common.utils;

import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Random;

public class RiskInfoUtils {
	private final static Random rd = new Random();
    private static final String INT = "0123456789";
    private static final String STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String ALL = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
 
    public static String randomStr(int length) {
        return random(length, RndType.STRING);
    }
 
    public static String randomInt(int length) {
        return random(length, RndType.INT);
    }
 
    public static String randomAll(int length) {
        return random(length, RndType.ALL);
    }
 
    private static String random(int length, RndType rndType) {
        StringBuilder s = new StringBuilder();
        char num = 0;
        for (int i = 0; i < length; i++) {
            if (rndType.equals(RndType.INT))
                num =INT.charAt(rd.nextInt(INT.length()));//产生数字0-9的随机数
            else if (rndType.equals(RndType.STRING))
                num =STR.charAt(rd.nextInt(STR.length()));//产生字母a-z的随机数
            else {
                num = ALL.charAt(rd.nextInt(ALL.length()));
            }
            s.append(num);
        }
        return s.toString();
    }
 
    public static enum RndType {
        INT,
        STRING,
        ALL
    }
    public static String getMacAddress() {
        try {
          Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
          byte[] mac = null;
          while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
            if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
              continue;
            } else {
              mac = netInterface.getHardwareAddress();
              if (mac != null) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                  sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }
                if (sb.length() > 0) {
                  return sb.toString();
                }
              }
            }
          }
        } catch (Exception e) {
        }
        return "";
      }
    public static void main(String[] args) {
            System.out.println(RiskInfoUtils.randomInt(32));
            System.out.println(RiskInfoUtils.randomStr(32));
            System.out.println(RiskInfoUtils.randomAll(32));
            System.out.println(getMacAddress());
    }
}
