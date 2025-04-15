package com.demo.rbc.util;

import cn.hutool.core.util.IdUtil;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.concurrent.ThreadLocalRandom;

public class SnowFlakeIdUtils {

    private static final long MAX_WORKER = 31;
    private static final long MAX_DC = 31;

    public static String getId() {
        return IdUtil.getSnowflake(getWorkerId(), getDatacenterId()).nextIdStr();
    }

    // 基于IP末段生成WorkerID
    private static long getWorkerId() {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            int lastSegment = Integer.parseInt(ip.split("\\.")[3]);
            return lastSegment % (MAX_WORKER + 1);
        } catch (Exception e) {
            return Thread.currentThread().hashCode() % 32; // 异常回退
        }
    }

    // 基于MAC地址生成DatacenterID[8,9](@ref)
    private static long getDatacenterId() {
        try {
            byte[] mac = NetworkInterface.getByInetAddress(
                    InetAddress.getLocalHost()).getHardwareAddress();
            return (mac[mac.length-1] & 0xFF) % (MAX_DC + 1);
        } catch (Exception e) {
            return 1; // 默认值
        }
    }

}
