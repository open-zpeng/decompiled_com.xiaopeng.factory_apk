package cn.hutool.core.lang.generator;

import cn.hutool.core.util.IdUtil;
/* loaded from: classes.dex */
public class UUIDGenerator implements Generator<String> {
    @Override // cn.hutool.core.lang.generator.Generator
    public String next() {
        return IdUtil.fastUUID();
    }
}
