package a.b.util;

import org.json.JSONObject;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

public final class SuperProperties {
    private static final String LAUNCH_ID = UUID.randomUUID().toString();
    private static String heartbeatId = UUID.randomUUID().toString();
    private static long lastHeartbeat = System.currentTimeMillis();

    public static final String CLIENT_VERSION = "347.12";
    public static final int CLIENT_BUILD = 347012;
    public static final String USER_AGENT = "Discord-Android/" + CLIENT_BUILD;

    private SuperProperties() {}

    public static String getBase64() {
        if (System.currentTimeMillis() - lastHeartbeat > 30 * 60 * 1000) {
            heartbeatId = UUID.randomUUID().toString();
            lastHeartbeat = System.currentTimeMillis();
        }

        try {
            JSONObject p = new JSONObject();
            p.put("os", "Android");
            p.put("browser", "Discord Android");
            p.put("device", System.getProperty("os.arch", "aarch64") + " Linux");
            p.put("system_locale", "en-US");
            p.put("client_version", CLIENT_VERSION);
            p.put("release_channel", "google-play");
            p.put("client_build_number", CLIENT_BUILD);
            p.put("design_id", 0);

            p.put("client_launch_id", LAUNCH_ID);
            p.put("client_heartbeat_session_id", heartbeatId);
            p.put("launch_signature", "00000000-0000-0000-0000-000000000000");

            p.put("os_version", "14");
            p.put("os_sdk_version", 34);
            p.put("client_performance_cpu", 11);
            p.put("client_performance_memory", 246220);
            p.put("accessibility_support_enabled", false);

            return Base64.getEncoder().encodeToString(p.toString().getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            return "";
        }
    }
}
