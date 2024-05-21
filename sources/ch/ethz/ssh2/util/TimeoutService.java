package ch.ethz.ssh2.util;

import ch.ethz.ssh2.log.Logger;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.LinkedList;
/* loaded from: classes.dex */
public class TimeoutService {
    static /* synthetic */ Class class$0;
    private static final Logger log;
    private static Thread timeoutThread;
    private static final LinkedList todolist;

    static {
        Class<?> cls = class$0;
        if (cls == null) {
            try {
                cls = Class.forName("ch.ethz.ssh2.util.TimeoutService");
                class$0 = cls;
            } catch (ClassNotFoundException e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        log = Logger.getLogger(cls);
        todolist = new LinkedList();
        timeoutThread = null;
    }

    /* loaded from: classes.dex */
    public static class TimeoutToken implements Comparable {
        private Runnable handler;
        private long runTime;

        private TimeoutToken(long runTime, Runnable handler) {
            this.runTime = runTime;
            this.handler = handler;
        }

        /* synthetic */ TimeoutToken(long j, Runnable runnable, TimeoutToken timeoutToken) {
            this(j, runnable);
        }

        @Override // java.lang.Comparable
        public int compareTo(Object o) {
            TimeoutToken t = (TimeoutToken) o;
            long j = this.runTime;
            long j2 = t.runTime;
            if (j > j2) {
                return 1;
            }
            if (j == j2) {
                return 0;
            }
            return -1;
        }
    }

    /* loaded from: classes.dex */
    private static class TimeoutThread extends Thread {
        private TimeoutThread() {
        }

        /* synthetic */ TimeoutThread(TimeoutThread timeoutThread) {
            this();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            synchronized (TimeoutService.todolist) {
                while (TimeoutService.todolist.size() != 0) {
                    long now = System.currentTimeMillis();
                    TimeoutToken tt = (TimeoutToken) TimeoutService.todolist.getFirst();
                    if (tt.runTime > now) {
                        try {
                            TimeoutService.todolist.wait(tt.runTime - now);
                        } catch (InterruptedException e) {
                        }
                    } else {
                        TimeoutService.todolist.removeFirst();
                        try {
                            tt.handler.run();
                        } catch (Exception e2) {
                            StringWriter sw = new StringWriter();
                            e2.printStackTrace(new PrintWriter(sw));
                            Logger logger = TimeoutService.log;
                            StringBuffer stringBuffer = new StringBuffer("Exeception in Timeout handler:");
                            stringBuffer.append(e2.getMessage());
                            stringBuffer.append("(");
                            stringBuffer.append(sw.toString());
                            stringBuffer.append(")");
                            logger.log(20, stringBuffer.toString());
                        }
                    }
                }
                TimeoutService.timeoutThread = null;
            }
        }
    }

    public static final TimeoutToken addTimeoutHandler(long runTime, Runnable handler) {
        TimeoutToken token = new TimeoutToken(runTime, handler, null);
        synchronized (todolist) {
            todolist.add(token);
            Collections.sort(todolist);
            if (timeoutThread != null) {
                timeoutThread.interrupt();
            } else {
                timeoutThread = new TimeoutThread(null);
                timeoutThread.setDaemon(true);
                timeoutThread.start();
            }
        }
        return token;
    }

    public static final void cancelTimeoutHandler(TimeoutToken token) {
        synchronized (todolist) {
            todolist.remove(token);
            if (timeoutThread != null) {
                timeoutThread.interrupt();
            }
        }
    }
}
