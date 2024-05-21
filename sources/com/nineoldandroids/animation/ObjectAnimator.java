package com.nineoldandroids.animation;

import android.view.View;
import com.nineoldandroids.util.Property;
import com.nineoldandroids.view.animation.AnimatorProxy;
import com.xiaopeng.libtheme.ThemeManager;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public final class ObjectAnimator extends ValueAnimator {
    private static final boolean DBG = false;
    private static final Map<String, Property> PROXY_PROPERTIES = new HashMap();
    private Property mProperty;
    private String mPropertyName;
    private Object mTarget;

    static {
        PROXY_PROPERTIES.put(ThemeManager.AttributeSet.ALPHA, PreHoneycombCompat.ALPHA);
        PROXY_PROPERTIES.put("pivotX", PreHoneycombCompat.PIVOT_X);
        PROXY_PROPERTIES.put("pivotY", PreHoneycombCompat.PIVOT_Y);
        PROXY_PROPERTIES.put("translationX", PreHoneycombCompat.TRANSLATION_X);
        PROXY_PROPERTIES.put("translationY", PreHoneycombCompat.TRANSLATION_Y);
        PROXY_PROPERTIES.put("rotation", PreHoneycombCompat.ROTATION);
        PROXY_PROPERTIES.put("rotationX", PreHoneycombCompat.ROTATION_X);
        PROXY_PROPERTIES.put("rotationY", PreHoneycombCompat.ROTATION_Y);
        PROXY_PROPERTIES.put("scaleX", PreHoneycombCompat.SCALE_X);
        PROXY_PROPERTIES.put("scaleY", PreHoneycombCompat.SCALE_Y);
        PROXY_PROPERTIES.put("scrollX", PreHoneycombCompat.SCROLL_X);
        PROXY_PROPERTIES.put("scrollY", PreHoneycombCompat.SCROLL_Y);
        PROXY_PROPERTIES.put("x", PreHoneycombCompat.X);
        PROXY_PROPERTIES.put("y", PreHoneycombCompat.Y);
    }

    public void setPropertyName(String propertyName) {
        if (this.mValues != null) {
            PropertyValuesHolder valuesHolder = this.mValues[0];
            String oldName = valuesHolder.getPropertyName();
            valuesHolder.setPropertyName(propertyName);
            this.mValuesMap.remove(oldName);
            this.mValuesMap.put(propertyName, valuesHolder);
        }
        this.mPropertyName = propertyName;
        this.mInitialized = false;
    }

    public void setProperty(Property property) {
        if (this.mValues != null) {
            PropertyValuesHolder valuesHolder = this.mValues[0];
            String oldName = valuesHolder.getPropertyName();
            valuesHolder.setProperty(property);
            this.mValuesMap.remove(oldName);
            this.mValuesMap.put(this.mPropertyName, valuesHolder);
        }
        if (this.mProperty != null) {
            this.mPropertyName = property.getName();
        }
        this.mProperty = property;
        this.mInitialized = false;
    }

    public String getPropertyName() {
        return this.mPropertyName;
    }

    public ObjectAnimator() {
    }

    private ObjectAnimator(Object target, String propertyName) {
        this.mTarget = target;
        setPropertyName(propertyName);
    }

    private <T> ObjectAnimator(T target, Property<T, ?> property) {
        this.mTarget = target;
        setProperty(property);
    }

    public static ObjectAnimator ofInt(Object target, String propertyName, int... values) {
        ObjectAnimator anim = new ObjectAnimator(target, propertyName);
        anim.setIntValues(values);
        return anim;
    }

    public static <T> ObjectAnimator ofInt(T target, Property<T, Integer> property, int... values) {
        ObjectAnimator anim = new ObjectAnimator(target, property);
        anim.setIntValues(values);
        return anim;
    }

    public static ObjectAnimator ofFloat(Object target, String propertyName, float... values) {
        ObjectAnimator anim = new ObjectAnimator(target, propertyName);
        anim.setFloatValues(values);
        return anim;
    }

    public static <T> ObjectAnimator ofFloat(T target, Property<T, Float> property, float... values) {
        ObjectAnimator anim = new ObjectAnimator(target, property);
        anim.setFloatValues(values);
        return anim;
    }

    public static ObjectAnimator ofObject(Object target, String propertyName, TypeEvaluator evaluator, Object... values) {
        ObjectAnimator anim = new ObjectAnimator(target, propertyName);
        anim.setObjectValues(values);
        anim.setEvaluator(evaluator);
        return anim;
    }

    public static <T, V> ObjectAnimator ofObject(T target, Property<T, V> property, TypeEvaluator<V> evaluator, V... values) {
        ObjectAnimator anim = new ObjectAnimator(target, property);
        anim.setObjectValues(values);
        anim.setEvaluator(evaluator);
        return anim;
    }

    public static ObjectAnimator ofPropertyValuesHolder(Object target, PropertyValuesHolder... values) {
        ObjectAnimator anim = new ObjectAnimator();
        anim.mTarget = target;
        anim.setValues(values);
        return anim;
    }

    @Override // com.nineoldandroids.animation.ValueAnimator
    public void setIntValues(int... values) {
        if (this.mValues == null || this.mValues.length == 0) {
            Property property = this.mProperty;
            if (property != null) {
                setValues(PropertyValuesHolder.ofInt(property, values));
                return;
            } else {
                setValues(PropertyValuesHolder.ofInt(this.mPropertyName, values));
                return;
            }
        }
        super.setIntValues(values);
    }

    @Override // com.nineoldandroids.animation.ValueAnimator
    public void setFloatValues(float... values) {
        if (this.mValues == null || this.mValues.length == 0) {
            Property property = this.mProperty;
            if (property != null) {
                setValues(PropertyValuesHolder.ofFloat(property, values));
                return;
            } else {
                setValues(PropertyValuesHolder.ofFloat(this.mPropertyName, values));
                return;
            }
        }
        super.setFloatValues(values);
    }

    @Override // com.nineoldandroids.animation.ValueAnimator
    public void setObjectValues(Object... values) {
        if (this.mValues == null || this.mValues.length == 0) {
            Property property = this.mProperty;
            if (property != null) {
                setValues(PropertyValuesHolder.ofObject(property, (TypeEvaluator) null, values));
                return;
            } else {
                setValues(PropertyValuesHolder.ofObject(this.mPropertyName, (TypeEvaluator) null, values));
                return;
            }
        }
        super.setObjectValues(values);
    }

    @Override // com.nineoldandroids.animation.ValueAnimator, com.nineoldandroids.animation.Animator
    public void start() {
        super.start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.nineoldandroids.animation.ValueAnimator
    public void initAnimation() {
        if (!this.mInitialized) {
            if (this.mProperty == null && AnimatorProxy.NEEDS_PROXY && (this.mTarget instanceof View) && PROXY_PROPERTIES.containsKey(this.mPropertyName)) {
                setProperty(PROXY_PROPERTIES.get(this.mPropertyName));
            }
            int numValues = this.mValues.length;
            for (int i = 0; i < numValues; i++) {
                this.mValues[i].setupSetterAndGetter(this.mTarget);
            }
            super.initAnimation();
        }
    }

    @Override // com.nineoldandroids.animation.ValueAnimator, com.nineoldandroids.animation.Animator
    public ObjectAnimator setDuration(long duration) {
        super.setDuration(duration);
        return this;
    }

    public Object getTarget() {
        return this.mTarget;
    }

    @Override // com.nineoldandroids.animation.Animator
    public void setTarget(Object target) {
        if (this.mTarget != target) {
            Object oldTarget = this.mTarget;
            this.mTarget = target;
            if (oldTarget != null && target != null && oldTarget.getClass() == target.getClass()) {
                return;
            }
            this.mInitialized = false;
        }
    }

    @Override // com.nineoldandroids.animation.Animator
    public void setupStartValues() {
        initAnimation();
        int numValues = this.mValues.length;
        for (int i = 0; i < numValues; i++) {
            this.mValues[i].setupStartValue(this.mTarget);
        }
    }

    @Override // com.nineoldandroids.animation.Animator
    public void setupEndValues() {
        initAnimation();
        int numValues = this.mValues.length;
        for (int i = 0; i < numValues; i++) {
            this.mValues[i].setupEndValue(this.mTarget);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.nineoldandroids.animation.ValueAnimator
    public void animateValue(float fraction) {
        super.animateValue(fraction);
        int numValues = this.mValues.length;
        for (int i = 0; i < numValues; i++) {
            this.mValues[i].setAnimatedValue(this.mTarget);
        }
    }

    @Override // com.nineoldandroids.animation.ValueAnimator, com.nineoldandroids.animation.Animator
    /* renamed from: clone */
    public ObjectAnimator mo100clone() {
        ObjectAnimator anim = (ObjectAnimator) super.mo100clone();
        return anim;
    }

    @Override // com.nineoldandroids.animation.ValueAnimator
    public String toString() {
        String returnVal = "ObjectAnimator@" + Integer.toHexString(hashCode()) + ", target " + this.mTarget;
        if (this.mValues != null) {
            for (int i = 0; i < this.mValues.length; i++) {
                returnVal = returnVal + "\n    " + this.mValues[i].toString();
            }
        }
        return returnVal;
    }
}
