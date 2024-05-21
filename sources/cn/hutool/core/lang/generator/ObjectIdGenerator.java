package cn.hutool.core.lang.generator;

import cn.hutool.core.lang.ObjectId;
/* loaded from: classes.dex */
public class ObjectIdGenerator implements Generator<String> {
    @Override // cn.hutool.core.lang.generator.Generator
    public String next() {
        return ObjectId.next();
    }
}
