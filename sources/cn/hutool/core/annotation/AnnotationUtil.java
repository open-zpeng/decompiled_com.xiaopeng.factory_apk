package cn.hutool.core.annotation;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class AnnotationUtil {
    public static CombinationAnnotationElement toCombination(AnnotatedElement annotationEle) {
        if (annotationEle instanceof CombinationAnnotationElement) {
            return (CombinationAnnotationElement) annotationEle;
        }
        return new CombinationAnnotationElement(annotationEle);
    }

    public static Annotation[] getAnnotations(AnnotatedElement annotationEle, boolean isToCombination) {
        if (annotationEle == null) {
            return null;
        }
        return (isToCombination ? toCombination(annotationEle) : annotationEle).getAnnotations();
    }

    public static <A extends Annotation> A getAnnotation(AnnotatedElement annotationEle, Class<A> annotationType) {
        if (annotationEle == null) {
            return null;
        }
        return (A) toCombination(annotationEle).getAnnotation(annotationType);
    }

    public static boolean hasAnnotation(AnnotatedElement annotationEle, Class<? extends Annotation> annotationType) {
        return getAnnotation(annotationEle, annotationType) != null;
    }

    public static <T> T getAnnotationValue(AnnotatedElement annotationEle, Class<? extends Annotation> annotationType) throws UtilException {
        return (T) getAnnotationValue(annotationEle, annotationType, "value");
    }

    public static <T> T getAnnotationValue(AnnotatedElement annotationEle, Class<? extends Annotation> annotationType, String propertyName) throws UtilException {
        Method method;
        Annotation annotation = getAnnotation(annotationEle, annotationType);
        if (annotation == null || (method = ReflectUtil.getMethodOfObj(annotation, propertyName, new Object[0])) == null) {
            return null;
        }
        return (T) ReflectUtil.invoke(annotation, method, new Object[0]);
    }

    public static Map<String, Object> getAnnotationValueMap(AnnotatedElement annotationEle, Class<? extends Annotation> annotationType) throws UtilException {
        Annotation annotation = getAnnotation(annotationEle, annotationType);
        if (annotation == null) {
            return null;
        }
        Method[] methods = ReflectUtil.getMethods(annotationType, new Filter() { // from class: cn.hutool.core.annotation.-$$Lambda$AnnotationUtil$eHm3ef58V8EdXPzCqdQGnRmWUpA
            @Override // cn.hutool.core.lang.Filter
            public final boolean accept(Object obj) {
                return AnnotationUtil.lambda$getAnnotationValueMap$0((Method) obj);
            }
        });
        HashMap<String, Object> result = new HashMap<>(methods.length, 1.0f);
        for (Method method : methods) {
            result.put(method.getName(), ReflectUtil.invoke(annotation, method, new Object[0]));
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$getAnnotationValueMap$0(Method t) {
        if (ArrayUtil.isEmpty((Object[]) t.getParameterTypes())) {
            String name = t.getName();
            return ("hashCode".equals(name) || "toString".equals(name) || "annotationType".equals(name)) ? false : true;
        }
        return false;
    }

    public static RetentionPolicy getRetentionPolicy(Class<? extends Annotation> annotationType) {
        Retention retention = (Retention) annotationType.getAnnotation(Retention.class);
        if (retention == null) {
            return RetentionPolicy.CLASS;
        }
        return retention.value();
    }

    public static ElementType[] getTargetType(Class<? extends Annotation> annotationType) {
        Target target = (Target) annotationType.getAnnotation(Target.class);
        return target == null ? new ElementType[]{ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.PACKAGE} : target.value();
    }

    public static boolean isDocumented(Class<? extends Annotation> annotationType) {
        return annotationType.isAnnotationPresent(Documented.class);
    }

    public static boolean isInherited(Class<? extends Annotation> annotationType) {
        return annotationType.isAnnotationPresent(Inherited.class);
    }

    public static void setValue(Annotation annotation, String annotationField, Object value) {
        Map memberValues = (Map) ReflectUtil.getFieldValue(Proxy.getInvocationHandler(annotation), "memberValues");
        memberValues.put(annotationField, value);
    }
}
