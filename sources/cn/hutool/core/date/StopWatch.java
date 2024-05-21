package cn.hutool.core.date;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.xiaopeng.commonfunc.Constant;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class StopWatch {
    private String currentTaskName;
    private final String id;
    private TaskInfo lastTaskInfo;
    private long startTimeNanos;
    private int taskCount;
    private List<TaskInfo> taskList;
    private long totalTimeNanos;

    public static StopWatch create(String id) {
        return new StopWatch(id);
    }

    public StopWatch() {
        this("");
    }

    public StopWatch(String id) {
        this(id, true);
    }

    public StopWatch(String id, boolean keepTaskList) {
        this.id = id;
        if (keepTaskList) {
            this.taskList = new ArrayList();
        }
    }

    public String getId() {
        return this.id;
    }

    public void setKeepTaskList(boolean keepTaskList) {
        if (keepTaskList) {
            if (this.taskList == null) {
                this.taskList = new ArrayList();
                return;
            }
            return;
        }
        this.taskList = null;
    }

    public void start() throws IllegalStateException {
        start("");
    }

    public void start(String taskName) throws IllegalStateException {
        if (this.currentTaskName != null) {
            throw new IllegalStateException("Can't start StopWatch: it's already running");
        }
        this.currentTaskName = taskName;
        this.startTimeNanos = System.nanoTime();
    }

    public void stop() throws IllegalStateException {
        if (this.currentTaskName == null) {
            throw new IllegalStateException("Can't stop StopWatch: it's not running");
        }
        long lastTime = System.nanoTime() - this.startTimeNanos;
        this.totalTimeNanos += lastTime;
        this.lastTaskInfo = new TaskInfo(this.currentTaskName, lastTime);
        List<TaskInfo> list = this.taskList;
        if (list != null) {
            list.add(this.lastTaskInfo);
        }
        this.taskCount++;
        this.currentTaskName = null;
    }

    public boolean isRunning() {
        return this.currentTaskName != null;
    }

    public String currentTaskName() {
        return this.currentTaskName;
    }

    public long getLastTaskTimeNanos() throws IllegalStateException {
        TaskInfo taskInfo = this.lastTaskInfo;
        if (taskInfo == null) {
            throw new IllegalStateException("No tasks run: can't get last task interval");
        }
        return taskInfo.getTimeNanos();
    }

    public long getLastTaskTimeMillis() throws IllegalStateException {
        TaskInfo taskInfo = this.lastTaskInfo;
        if (taskInfo == null) {
            throw new IllegalStateException("No tasks run: can't get last task interval");
        }
        return taskInfo.getTimeMillis();
    }

    public String getLastTaskName() throws IllegalStateException {
        TaskInfo taskInfo = this.lastTaskInfo;
        if (taskInfo == null) {
            throw new IllegalStateException("No tasks run: can't get last task name");
        }
        return taskInfo.getTaskName();
    }

    public TaskInfo getLastTaskInfo() throws IllegalStateException {
        TaskInfo taskInfo = this.lastTaskInfo;
        if (taskInfo == null) {
            throw new IllegalStateException("No tasks run: can't get last task info");
        }
        return taskInfo;
    }

    public long getTotalTimeNanos() {
        return this.totalTimeNanos;
    }

    public long getTotalTimeMillis() {
        return DateUtil.nanosToMillis(this.totalTimeNanos);
    }

    public double getTotalTimeSeconds() {
        return DateUtil.nanosToSeconds(this.totalTimeNanos);
    }

    public int getTaskCount() {
        return this.taskCount;
    }

    public TaskInfo[] getTaskInfo() {
        List<TaskInfo> list = this.taskList;
        if (list == null) {
            throw new UnsupportedOperationException("Task info is not being kept!");
        }
        return (TaskInfo[]) list.toArray(new TaskInfo[0]);
    }

    public String shortSummary() {
        return StrUtil.format("StopWatch '{}': running time = {} ns", this.id, Long.valueOf(this.totalTimeNanos));
    }

    public String prettyPrint() {
        TaskInfo[] taskInfo;
        StringBuilder sb = new StringBuilder(shortSummary());
        sb.append(FileUtil.getLineSeparator());
        if (this.taskList == null) {
            sb.append("No task info kept");
        } else {
            sb.append("---------------------------------------------");
            sb.append(FileUtil.getLineSeparator());
            sb.append("ns         %     Task name");
            sb.append(FileUtil.getLineSeparator());
            sb.append("---------------------------------------------");
            sb.append(FileUtil.getLineSeparator());
            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMinimumIntegerDigits(9);
            nf.setGroupingUsed(false);
            NumberFormat pf = NumberFormat.getPercentInstance();
            pf.setMinimumIntegerDigits(3);
            pf.setGroupingUsed(false);
            for (TaskInfo task : getTaskInfo()) {
                sb.append(nf.format(task.getTimeNanos()));
                sb.append(Constant.SPACE_2_STRING);
                sb.append(pf.format(task.getTimeNanos() / getTotalTimeNanos()));
                sb.append(Constant.SPACE_2_STRING);
                sb.append(task.getTaskName());
                sb.append(FileUtil.getLineSeparator());
            }
        }
        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(shortSummary());
        List<TaskInfo> list = this.taskList;
        if (list != null) {
            for (TaskInfo task : list) {
                sb.append("; [");
                sb.append(task.getTaskName());
                sb.append("] took ");
                sb.append(task.getTimeNanos());
                sb.append(" ns");
                long percent = Math.round((task.getTimeNanos() * 100.0d) / getTotalTimeNanos());
                sb.append(" = ");
                sb.append(percent);
                sb.append(Constant.PERCENT_STRING);
            }
        } else {
            sb.append("; no task info kept");
        }
        return sb.toString();
    }

    /* loaded from: classes.dex */
    public static final class TaskInfo {
        private final String taskName;
        private final long timeNanos;

        TaskInfo(String taskName, long timeNanos) {
            this.taskName = taskName;
            this.timeNanos = timeNanos;
        }

        public String getTaskName() {
            return this.taskName;
        }

        public long getTimeNanos() {
            return this.timeNanos;
        }

        public long getTimeMillis() {
            return DateUtil.nanosToMillis(this.timeNanos);
        }

        public double getTimeSeconds() {
            return DateUtil.nanosToSeconds(this.timeNanos);
        }
    }
}
