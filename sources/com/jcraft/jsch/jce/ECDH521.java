package com.jcraft.jsch.jce;

import com.jcraft.jsch.ECDH;
import com.xiaopeng.lib.framework.moduleinterface.carcontroller.IInputController;
/* loaded from: classes.dex */
public class ECDH521 extends ECDHN implements ECDH {
    public void init() throws Exception {
        super.init(IInputController.KEYCODE_KNOB_VOL_DOWN);
    }
}
