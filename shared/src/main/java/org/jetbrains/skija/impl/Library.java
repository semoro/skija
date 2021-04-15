package org.jetbrains.skija.impl;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.file.*;
import java.util.*;
import lombok.*;
import org.jetbrains.annotations.*;

public class Library {
    @ApiStatus.Internal
    public static volatile boolean _loaded = false;

    public static void staticLoad() {
        if (!_loaded && !"false".equals(System.getProperty("skija.staticLoad")))
            load();
    }

    static class C {
        public final byte[] buf;
        public final int size;

        public C(byte[] buf, int size) {
            this.buf = buf;
            this.size = size;
        }
    }

    public static byte[] readAllBytes(InputStream is) throws IOException {
        ArrayList<C> blobs = new ArrayList<>();
        byte[] buf = new byte[1024];
        int r;
        while ((r = is.read(buf)) != 0) {
            blobs.add(new C(buf, r));
            buf = new byte[1024];
        }
        int total = blobs.stream().map((obj) -> obj.size).reduce(0, Integer::sum);
        ByteBuffer result = ByteBuffer.allocate(total);
        blobs.forEach((blob) -> result.put(blob.buf, 0, blob.size));
        return result.array();
    }

    public static String readResource(String path) {
        URL url = Library.class.getResource(path);
        if (url == null)
            return null;
        try (InputStream is = url.openStream()) {
            byte[] bytes = readAllBytes(is);
            return new String(bytes).trim();
        } catch (IOException e) {
            return null;
        }
    }

    public static synchronized void load() {
        if (_loaded) return;

        String version = readResource("/skija.version");
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "skija_" + (version == null ? "" + System.nanoTime() : version));
        String os = System.getProperty("os.name").toLowerCase();
        
        if (os.contains("mac") || os.contains("darwin")) {
            File library = _extract("/", "libskija.dylib", tempDir);
            System.load(library.getAbsolutePath());
        } else if (os.contains("windows")) {
            _extract("/", "icudtl.dat", tempDir);
            File library = _extract("/", "skija.dll", tempDir);
            System.load(library.getAbsolutePath());
        } else if (os.contains("nux") || os.contains("nix")) {
            File library = _extract("/", "libskija.so", tempDir);
            System.load(library.getAbsolutePath());
        } else
            throw new RuntimeException("Unknown operation system");

        if (tempDir.exists() && version == null) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    Files.walk(tempDir.toPath())
                         .map(Path::toFile)
                         .sorted(Comparator.reverseOrder())
                         .forEach((f) -> {
                            Log.debug("Deleting " + f);
                            f.delete();
                         });
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }));
        }
        
        _loaded = true;
        _nAfterLoad();
    }

    @ApiStatus.Internal
    @SneakyThrows
    public static File _extract(String resourcePath, String fileName, File tempDir) {
        File file;
        URL url = Library.class.getResource(resourcePath + fileName);
        if (url == null)
            throw new IllegalArgumentException("Library file " + fileName + " not found in " + resourcePath);
        else if (url.getProtocol() == "file") {
            file = new File(url.toURI());
        } else {
            file = new File(tempDir, fileName);
            if (!file.exists()) {
                if (!tempDir.exists())
                    tempDir.mkdirs();
                try (InputStream is = url.openStream()) {
                    Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
        Log.debug("Loading " + file);
        return file;
    }

    @ApiStatus.Internal public static native void _nAfterLoad();
}
