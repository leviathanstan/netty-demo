package com.rdc.zrj.nettydemo.im.client.executor;

import lombok.Data;

@Data
public class ResendConfig {

    private int maxCount = 1;
    private int resendInterval = 3000;
    private int maxWait = 1000;
}
