package com.xiaopeng.lib.framework.moduleinterface.aiassistantmodule;
/* loaded from: classes2.dex */
public interface IInteractiveMsgBuilder {
    IInteractiveMsg build();

    IInteractiveMsgBuilder data(String data);

    IInteractiveMsgBuilder msgId(int msgId);
}