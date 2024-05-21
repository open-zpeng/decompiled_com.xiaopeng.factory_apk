package cn.hutool.core.swing.clipboard;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
/* loaded from: classes.dex */
public interface ClipboardListener {
    Transferable onChange(Clipboard clipboard, Transferable transferable);
}
