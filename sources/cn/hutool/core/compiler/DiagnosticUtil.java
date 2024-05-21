package cn.hutool.core.compiler;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.tools.DiagnosticCollector;
/* loaded from: classes.dex */
public class DiagnosticUtil {
    public static String getMessages(DiagnosticCollector<?> collector) {
        List<?> diagnostics = collector.getDiagnostics();
        return (String) diagnostics.stream().map(new Function() { // from class: cn.hutool.core.compiler.-$$Lambda$bnDHEqIMgOpdB3FcAW24k7BVbvA
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return String.valueOf(obj);
            }
        }).collect(Collectors.joining(System.lineSeparator()));
    }
}
