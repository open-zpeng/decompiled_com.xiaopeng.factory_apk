package cn.hutool.core.io;

import cn.hutool.core.collection.CollUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
/* loaded from: classes.dex */
public class ValidateObjectInputStream extends ObjectInputStream {
    private Set<String> blackClassSet;
    private Set<String> whiteClassSet;

    public ValidateObjectInputStream(InputStream inputStream, Class<?>... acceptClasses) throws IOException {
        super(inputStream);
        accept(acceptClasses);
    }

    public void refuse(Class<?>... refuseClasses) {
        if (this.blackClassSet == null) {
            this.blackClassSet = new HashSet();
        }
        for (Class<?> acceptClass : refuseClasses) {
            this.blackClassSet.add(acceptClass.getName());
        }
    }

    public void accept(Class<?>... acceptClasses) {
        if (this.whiteClassSet == null) {
            this.whiteClassSet = new HashSet();
        }
        for (Class<?> acceptClass : acceptClasses) {
            this.whiteClassSet.add(acceptClass.getName());
        }
    }

    @Override // java.io.ObjectInputStream
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        validateClassName(desc.getName());
        return super.resolveClass(desc);
    }

    private void validateClassName(String className) throws InvalidClassException {
        if (CollUtil.isNotEmpty((Collection<?>) this.blackClassSet) && this.blackClassSet.contains(className)) {
            throw new InvalidClassException("Unauthorized deserialization attempt by black list", className);
        }
        if (CollUtil.isEmpty((Collection<?>) this.whiteClassSet) || className.startsWith("java.") || this.whiteClassSet.contains(className)) {
            return;
        }
        throw new InvalidClassException("Unauthorized deserialization attempt", className);
    }
}
