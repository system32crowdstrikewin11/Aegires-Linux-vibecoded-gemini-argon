package a.b.util;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public final class TokenStore {
    private static File getTokenFile() {
        String home = System.getProperty("user.home", ".");
        return new File(home, ".config/aegires/token");
    }

    private TokenStore() {}

    public static void save(String token) {
        if (token == null || token.isEmpty()) return;
        try {
            File f = getTokenFile();
            File parent = f.getParentFile();
            if (parent != null && !parent.exists()) parent.mkdirs();
            Files.write(f.toPath(), token.trim().getBytes(StandardCharsets.UTF_8));
            f.setReadable(false, false);
            f.setReadable(true, true);
            f.setWritable(false, false);
            f.setWritable(true, true);
        } catch (Exception e) {
            System.err.println("TokenStore save failed: " + e.getMessage());
        }
    }

    public static void save(Object unusedCtx, String token) {
        save(token);
    }

    public static String load() {
        try {
            File f = getTokenFile();
            if (!f.exists()) return null;
            String t = new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8).trim();
            return t.isEmpty() ? null : t;
        } catch (Exception e) {
            return null;
        }
    }

    public static String load(Object unusedCtx) {
        return load();
    }

    public static void clear() {
        try {
            File f = getTokenFile();
            if (f.exists()) f.delete();
        } catch (Exception ignored) {}
    }

    public static void clear(Object unusedCtx) {
        clear();
    }
}
