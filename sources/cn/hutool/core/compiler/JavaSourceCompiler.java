package cn.hutool.core.compiler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.FileResource;
import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.io.resource.StringResource;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.URLUtil;
import com.xiaopeng.commonfunc.Constant;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
/* loaded from: classes.dex */
public class JavaSourceCompiler {
    private final ClassLoader parentClassLoader;
    private final List<Resource> sourceList = new ArrayList();
    private final List<File> libraryFileList = new ArrayList();

    public static JavaSourceCompiler create(ClassLoader parent) {
        return new JavaSourceCompiler(parent);
    }

    private JavaSourceCompiler(ClassLoader parent) {
        this.parentClassLoader = (ClassLoader) ObjectUtil.defaultIfNull(parent, ClassLoaderUtil.getClassLoader());
    }

    public JavaSourceCompiler addSource(Resource... resources) {
        if (ArrayUtil.isNotEmpty((Object[]) resources)) {
            this.sourceList.addAll(Arrays.asList(resources));
        }
        return this;
    }

    public JavaSourceCompiler addSource(File... files) {
        if (ArrayUtil.isNotEmpty((Object[]) files)) {
            for (File file : files) {
                this.sourceList.add(new FileResource(file));
            }
        }
        return this;
    }

    public JavaSourceCompiler addSource(Map<String, String> sourceCodeMap) {
        if (MapUtil.isNotEmpty(sourceCodeMap)) {
            sourceCodeMap.forEach(new BiConsumer() { // from class: cn.hutool.core.compiler.-$$Lambda$sAkzE23ANjWLrpMWM6rvRQE-xWE
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    JavaSourceCompiler.this.addSource((String) obj, (String) obj2);
                }
            });
        }
        return this;
    }

    public JavaSourceCompiler addSource(String className, String sourceCode) {
        if (className != null && sourceCode != null) {
            this.sourceList.add(new StringResource(sourceCode, className));
        }
        return this;
    }

    public JavaSourceCompiler addLibrary(File... files) {
        if (ArrayUtil.isNotEmpty((Object[]) files)) {
            this.libraryFileList.addAll(Arrays.asList(files));
        }
        return this;
    }

    public ClassLoader compile() {
        List<File> classPath = getClassPath();
        URL[] urLs = URLUtil.getURLs((File[]) classPath.toArray(new File[0]));
        URLClassLoader ucl = URLClassLoader.newInstance(urLs, this.parentClassLoader);
        if (this.sourceList.isEmpty()) {
            return ucl;
        }
        JavaFileManager javaClassFileManager = new JavaClassFileManager(ucl, CompilerUtil.getFileManager());
        List<String> options = new ArrayList<>();
        if (!classPath.isEmpty()) {
            List<String> cp = CollUtil.map(classPath, new Function() { // from class: cn.hutool.core.compiler.-$$Lambda$qYnA_lRWeZdeTRKfT5Tujj7uSxw
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return ((File) obj).getAbsolutePath();
                }
            }, true);
            options.add("-cp");
            options.add(CollUtil.join(cp, FileUtil.isWindows() ? Constant.SEMICOLON_STRING : ":"));
        }
        DiagnosticCollector<? super JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();
        List<JavaFileObject> javaFileObjectList = getJavaFileObject();
        JavaCompiler.CompilationTask task = CompilerUtil.getTask(javaClassFileManager, diagnosticCollector, options, javaFileObjectList);
        try {
            if (task.call().booleanValue()) {
                return javaClassFileManager.getClassLoader(StandardLocation.CLASS_OUTPUT);
            }
            IoUtil.close((Closeable) javaClassFileManager);
            throw new CompilerException(DiagnosticUtil.getMessages(diagnosticCollector));
        } finally {
            IoUtil.close((Closeable) javaClassFileManager);
        }
    }

    private List<File> getClassPath() {
        List<File> classPathFileList = new ArrayList<>();
        for (File file : this.libraryFileList) {
            List<File> jarOrZipFile = FileUtil.loopFiles(file, new FileFilter() { // from class: cn.hutool.core.compiler.-$$Lambda$JavaSourceCompiler$ctFyBpBOOXXYV0nSDc2PmsIb0LQ
                @Override // java.io.FileFilter
                public final boolean accept(File file2) {
                    boolean isJarOrZipFile;
                    isJarOrZipFile = JavaFileObjectUtil.isJarOrZipFile(file2.getName());
                    return isJarOrZipFile;
                }
            });
            classPathFileList.addAll(jarOrZipFile);
            if (file.isDirectory()) {
                classPathFileList.add(file);
            }
        }
        return classPathFileList;
    }

    private List<JavaFileObject> getJavaFileObject() {
        final List<JavaFileObject> list = new ArrayList<>();
        for (Resource resource : this.sourceList) {
            if (resource instanceof FileResource) {
                final File file = ((FileResource) resource).getFile();
                FileUtil.walkFiles(file, new Consumer() { // from class: cn.hutool.core.compiler.-$$Lambda$JavaSourceCompiler$2DrGFKZXy5Ic_WsFVDi7jFuPSHY
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        File file2 = (File) obj;
                        list.addAll(JavaFileObjectUtil.getJavaFileObjects(file));
                    }
                });
            } else {
                list.add(new JavaSourceFileObject(resource.getName(), resource.getStream()));
            }
        }
        return list;
    }

    private Collection<JavaFileObject> getJavaFileObjectByMap(Map<String, String> sourceCodeMap) {
        if (MapUtil.isNotEmpty(sourceCodeMap)) {
            return (Collection) sourceCodeMap.entrySet().stream().map(new Function() { // from class: cn.hutool.core.compiler.-$$Lambda$JavaSourceCompiler$dDIxuXSQv_u4lF_IrpBCFWslFT8
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return JavaSourceCompiler.lambda$getJavaFileObjectByMap$2((Map.Entry) obj);
                }
            }).collect(Collectors.toList());
        }
        return Collections.emptySet();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ JavaSourceFileObject lambda$getJavaFileObjectByMap$2(Map.Entry entry) {
        return new JavaSourceFileObject((String) entry.getKey(), (String) entry.getValue(), CharsetUtil.CHARSET_UTF_8);
    }

    private JavaFileObject getJavaFileObjectByJavaFile(File file) {
        return new JavaSourceFileObject(file.toURI());
    }
}
