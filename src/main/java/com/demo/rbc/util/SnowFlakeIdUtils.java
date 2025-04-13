package com.demo.rbc.util;

import cn.hutool.core.util.IdUtil;

public class SnowFlakeIdUtils {

    public static String getId() {
        return IdUtil.getSnowflake(1, 1).nextIdStr();
    }

}
