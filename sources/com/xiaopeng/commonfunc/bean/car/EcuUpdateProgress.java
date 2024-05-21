package com.xiaopeng.commonfunc.bean.car;

import cn.hutool.core.text.CharPool;
/* loaded from: classes.dex */
public class EcuUpdateProgress {
    private String Partition;
    private String Process;
    private String Target;

    public EcuUpdateProgress(String target, String process, String partition) {
        this.Target = target;
        this.Process = process;
        this.Partition = partition;
    }

    public String getTarget() {
        return this.Target;
    }

    public void setTarget(String target) {
        this.Target = target;
    }

    public String getProcess() {
        return this.Process;
    }

    public void setProcess(String process) {
        this.Process = process;
    }

    public String getPartition() {
        return this.Partition;
    }

    public void setPartition(String partition) {
        this.Partition = partition;
    }

    public String toString() {
        return "EcuUpdateProcess{Target='" + this.Target + CharPool.SINGLE_QUOTE + ", Process='" + this.Process + CharPool.SINGLE_QUOTE + ", Partition='" + this.Partition + CharPool.SINGLE_QUOTE + '}';
    }
}
