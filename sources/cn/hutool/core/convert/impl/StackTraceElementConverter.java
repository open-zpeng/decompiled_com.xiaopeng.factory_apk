package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.lzy.okgo.model.Progress;
import java.util.Map;
/* loaded from: classes.dex */
public class StackTraceElementConverter extends AbstractConverter<StackTraceElement> {
    private static final long serialVersionUID = 1;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.hutool.core.convert.AbstractConverter
    public StackTraceElement convertInternal(Object value) {
        if (value instanceof Map) {
            Map<?, ?> map = (Map) value;
            String declaringClass = MapUtil.getStr(map, "className");
            String methodName = MapUtil.getStr(map, "methodName");
            String fileName = MapUtil.getStr(map, Progress.FILE_NAME);
            Integer lineNumber = MapUtil.getInt(map, "lineNumber");
            return new StackTraceElement(declaringClass, methodName, fileName, ((Integer) ObjectUtil.defaultIfNull(lineNumber, 0)).intValue());
        }
        return null;
    }
}
