package cn.hutool.core.annotation;

import cn.hutool.core.collection.CollUtil;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
/* loaded from: classes.dex */
public class CombinationAnnotationElement implements AnnotatedElement, Serializable {
    private static final Set<Class<? extends Annotation>> META_ANNOTATIONS = CollUtil.newHashSet(Target.class, Retention.class, Inherited.class, Documented.class, SuppressWarnings.class, Override.class, Deprecated.class);
    private static final long serialVersionUID = 1;
    private Map<Class<? extends Annotation>, Annotation> annotationMap;
    private Map<Class<? extends Annotation>, Annotation> declaredAnnotationMap;

    public CombinationAnnotationElement(AnnotatedElement element) {
        init(element);
    }

    @Override // java.lang.reflect.AnnotatedElement
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return this.annotationMap.containsKey(annotationClass);
    }

    @Override // java.lang.reflect.AnnotatedElement
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        T t = (T) this.annotationMap.get(annotationClass);
        if (t == null) {
            return null;
        }
        return t;
    }

    @Override // java.lang.reflect.AnnotatedElement
    public Annotation[] getAnnotations() {
        Collection<Annotation> annotations = this.annotationMap.values();
        return (Annotation[]) annotations.toArray(new Annotation[0]);
    }

    @Override // java.lang.reflect.AnnotatedElement
    public Annotation[] getDeclaredAnnotations() {
        Collection<Annotation> annotations = this.declaredAnnotationMap.values();
        return (Annotation[]) annotations.toArray(new Annotation[0]);
    }

    private void init(AnnotatedElement element) {
        Annotation[] declaredAnnotations = element.getDeclaredAnnotations();
        this.declaredAnnotationMap = new HashMap();
        parseDeclared(declaredAnnotations);
        Annotation[] annotations = element.getAnnotations();
        if (Arrays.equals(declaredAnnotations, annotations)) {
            this.annotationMap = this.declaredAnnotationMap;
            return;
        }
        this.annotationMap = new HashMap();
        parse(annotations);
    }

    private void parseDeclared(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            if (!META_ANNOTATIONS.contains(annotationType)) {
                this.declaredAnnotationMap.put(annotationType, annotation);
                parseDeclared(annotationType.getDeclaredAnnotations());
            }
        }
    }

    private void parse(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            if (!META_ANNOTATIONS.contains(annotationType)) {
                this.annotationMap.put(annotationType, annotation);
                parse(annotationType.getAnnotations());
            }
        }
    }
}
