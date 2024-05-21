package cn.hutool.core.bean;

import cn.hutool.core.lang.func.Func0;
import java.io.Serializable;
/* compiled from: lambda */
/* renamed from: cn.hutool.core.bean.-$$Lambda$BeanUtil$Kde-CmJknWlbcd4T5U_P5sbAgqw  reason: invalid class name */
/* loaded from: classes.dex */
public final /* synthetic */ class $$Lambda$BeanUtil$KdeCmJknWlbcd4T5U_P5sbAgqw implements Func0, Serializable {
    private final /* synthetic */ Class f$0;
    private final /* synthetic */ boolean f$1;

    public /* synthetic */ $$Lambda$BeanUtil$KdeCmJknWlbcd4T5U_P5sbAgqw(Class cls, boolean z) {
        this.f$0 = cls;
        this.f$1 = z;
    }

    @Override // cn.hutool.core.lang.func.Func0
    public final Object call() {
        return BeanUtil.lambda$getPropertyDescriptorMap$58f3b7cb$1(this.f$0, this.f$1);
    }
}
