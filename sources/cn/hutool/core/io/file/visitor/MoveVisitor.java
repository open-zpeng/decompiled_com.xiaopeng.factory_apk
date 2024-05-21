package cn.hutool.core.io.file.visitor;

import cn.hutool.core.io.file.PathUtil;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
/* loaded from: classes.dex */
public class MoveVisitor extends SimpleFileVisitor<Path> {
    private final CopyOption[] copyOptions;
    private boolean isTargetCreated;
    private final Path source;
    private final Path target;

    public MoveVisitor(Path source, Path target, CopyOption... copyOptions) {
        if (PathUtil.exists(target, false) && !PathUtil.isDirectory(target)) {
            throw new IllegalArgumentException("Target must be a directory");
        }
        this.source = source;
        this.target = target;
        this.copyOptions = copyOptions;
    }

    @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        initTarget();
        Path targetDir = this.target.resolve(this.source.relativize(dir));
        if (!Files.exists(targetDir, new LinkOption[0])) {
            Files.createDirectories(targetDir, new FileAttribute[0]);
        } else if (!Files.isDirectory(targetDir, new LinkOption[0])) {
            throw new FileAlreadyExistsException(targetDir.toString());
        }
        return FileVisitResult.CONTINUE;
    }

    @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        initTarget();
        Files.move(file, this.target.resolve(this.source.relativize(file)), this.copyOptions);
        return FileVisitResult.CONTINUE;
    }

    private void initTarget() {
        if (!this.isTargetCreated) {
            PathUtil.mkdir(this.target);
            this.isTargetCreated = true;
        }
    }
}
