package a.b.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingUtilities;

public class Dispatcher {
    private final ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "dispatcher-timer");
        t.setDaemon(true);
        return t;
    });
    private final Map<Runnable, ScheduledFuture<?>> scheduled = new ConcurrentHashMap<>();
    private final boolean useSwingEdt;

    public Dispatcher() {
        this(true);
    }

    public Dispatcher(boolean useSwingEdt) {
        this.useSwingEdt = useSwingEdt;
    }

    public void post(Runnable r) {
        if (r == null) return;
        if (useSwingEdt) {
            if (SwingUtilities.isEventDispatchThread()) {
                r.run();
            } else {
                SwingUtilities.invokeLater(r);
            }
        } else {
            r.run();
        }
    }

    public void postDelayed(Runnable r, long delayMs) {
        if (r == null) return;
        removeCallbacks(r);
        ScheduledFuture<?> future = timer.schedule(() -> {
            scheduled.remove(r);
            post(r);
        }, Math.max(0L, delayMs), TimeUnit.MILLISECONDS);
        scheduled.put(r, future);
    }

    public void removeCallbacks(Runnable r) {
        if (r == null) return;
        ScheduledFuture<?> future = scheduled.remove(r);
        if (future != null) {
            future.cancel(false);
        }
    }
}
