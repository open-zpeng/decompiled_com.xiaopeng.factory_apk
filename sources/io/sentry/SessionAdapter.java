package io.sentry;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.xiaopeng.commonfunc.Constant;
import io.sentry.Session;
import io.sentry.util.Objects;
import io.sentry.util.StringUtils;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class SessionAdapter extends TypeAdapter<Session> {
    @NotNull
    private final SentryOptions options;

    public SessionAdapter(@NotNull SentryOptions options) {
        this.options = (SentryOptions) Objects.requireNonNull(options, "The SentryOptions is required.");
    }

    @Override // com.google.gson.TypeAdapter
    public void write(JsonWriter writer, Session value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }
        writer.beginObject();
        if (value.getSessionId() != null) {
            writer.name(Constant.KEY_SID).value(value.getSessionId().toString());
        }
        if (value.getDistinctId() != null) {
            writer.name("did").value(value.getDistinctId());
        }
        if (value.getInit() != null) {
            writer.name("init").value(value.getInit());
        }
        Date started = value.getStarted();
        if (started != null) {
            writer.name("started").value(DateUtils.getTimestamp(started));
        }
        Session.State status = value.getStatus();
        if (status != null) {
            writer.name("status").value(status.name().toLowerCase(Locale.ROOT));
        }
        if (value.getSequence() != null) {
            writer.name("seq").value(value.getSequence());
        }
        int errorCount = value.errorCount();
        if (errorCount > 0) {
            writer.name("errors").value(errorCount);
        }
        if (value.getDuration() != null) {
            writer.name("duration").value(value.getDuration());
        }
        if (value.getTimestamp() != null) {
            writer.name("timestamp").value(DateUtils.getTimestamp(value.getTimestamp()));
        }
        boolean hasInitAttrs = initAttrs(writer, false);
        writer.name("release").value(value.getRelease());
        if (value.getEnvironment() != null) {
            hasInitAttrs = initAttrs(writer, hasInitAttrs);
            writer.name("environment").value(value.getEnvironment());
        }
        if (value.getIpAddress() != null) {
            hasInitAttrs = initAttrs(writer, hasInitAttrs);
            writer.name("ip_address").value(value.getIpAddress());
        }
        if (value.getUserAgent() != null) {
            hasInitAttrs = initAttrs(writer, hasInitAttrs);
            writer.name("user_agent").value(value.getUserAgent());
        }
        if (hasInitAttrs) {
            writer.endObject();
        }
        writer.endObject();
    }

    private boolean initAttrs(JsonWriter writer, boolean hasInitAtts) throws IOException {
        if (!hasInitAtts) {
            writer.name("attrs").beginObject();
            return true;
        }
        return true;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.TypeAdapter
    @Nullable
    public Session read(JsonReader reader) throws IOException {
        char c;
        String ipAddress;
        char c2;
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        String did = null;
        Boolean init = null;
        Date started = null;
        Session.State status = null;
        int errors = 0;
        Long seq = null;
        Double duration = null;
        Date timestamp = null;
        String release = null;
        String ipAddress2 = null;
        String userAgent = null;
        reader.beginObject();
        String environment = null;
        UUID sid = null;
        while (reader.hasNext()) {
            String nextName = reader.nextName();
            String userAgent2 = userAgent;
            switch (nextName.hashCode()) {
                case -1992012396:
                    if (nextName.equals("duration")) {
                        c = 7;
                        break;
                    }
                    c = 65535;
                    break;
                case -1897185151:
                    if (nextName.equals("started")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case -1294635157:
                    if (nextName.equals("errors")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case -892481550:
                    if (nextName.equals("status")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case 99455:
                    if (nextName.equals("did")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case 113759:
                    if (nextName.equals("seq")) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case 113870:
                    if (nextName.equals(Constant.KEY_SID)) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case 3237136:
                    if (nextName.equals("init")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 55126294:
                    if (nextName.equals("timestamp")) {
                        c = '\b';
                        break;
                    }
                    c = 65535;
                    break;
                case 93152418:
                    if (nextName.equals("attrs")) {
                        c = '\t';
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                    ipAddress = ipAddress2;
                    String sidStr = null;
                    try {
                        sidStr = reader.nextString();
                        UUID sid2 = UUID.fromString(sidStr);
                        sid = sid2;
                        userAgent = userAgent2;
                        ipAddress2 = ipAddress;
                        continue;
                    } catch (IllegalArgumentException e) {
                        this.options.getLogger().log(SentryLevel.ERROR, "%s sid is not valid.", sidStr);
                        break;
                    }
                case 1:
                    String did2 = reader.nextString();
                    did = did2;
                    userAgent = userAgent2;
                    continue;
                case 2:
                    Boolean init2 = Boolean.valueOf(reader.nextBoolean());
                    init = init2;
                    userAgent = userAgent2;
                    continue;
                case 3:
                    Date started2 = converTimeStamp(reader.nextString(), "started");
                    started = started2;
                    userAgent = userAgent2;
                    continue;
                case 4:
                    String statusStr = null;
                    try {
                        statusStr = StringUtils.capitalize(reader.nextString());
                        if (statusStr != null) {
                            status = Session.State.valueOf(statusStr);
                        }
                        userAgent = userAgent2;
                        continue;
                    } catch (IllegalArgumentException e2) {
                        ipAddress = ipAddress2;
                        this.options.getLogger().log(SentryLevel.ERROR, "%s status is not valid.", statusStr);
                        break;
                    }
                case 5:
                    int errors2 = reader.nextInt();
                    errors = errors2;
                    userAgent = userAgent2;
                    continue;
                case 6:
                    Long seq2 = Long.valueOf(reader.nextLong());
                    seq = seq2;
                    userAgent = userAgent2;
                    continue;
                case 7:
                    Double duration2 = Double.valueOf(reader.nextDouble());
                    duration = duration2;
                    userAgent = userAgent2;
                    continue;
                case '\b':
                    Date timestamp2 = converTimeStamp(reader.nextString(), "timestamp");
                    timestamp = timestamp2;
                    userAgent = userAgent2;
                    continue;
                case '\t':
                    reader.beginObject();
                    while (reader.hasNext()) {
                        String nextName2 = reader.nextName();
                        switch (nextName2.hashCode()) {
                            case -85904877:
                                if (nextName2.equals("environment")) {
                                    c2 = 1;
                                    break;
                                }
                                c2 = 65535;
                                break;
                            case 1090594823:
                                if (nextName2.equals("release")) {
                                    c2 = 0;
                                    break;
                                }
                                c2 = 65535;
                                break;
                            case 1480014044:
                                if (nextName2.equals("ip_address")) {
                                    c2 = 2;
                                    break;
                                }
                                c2 = 65535;
                                break;
                            case 1917799825:
                                if (nextName2.equals("user_agent")) {
                                    c2 = 3;
                                    break;
                                }
                                c2 = 65535;
                                break;
                            default:
                                c2 = 65535;
                                break;
                        }
                        if (c2 == 0) {
                            String release2 = reader.nextString();
                            release = release2;
                        } else if (c2 == 1) {
                            String environment2 = reader.nextString();
                            environment = environment2;
                        } else if (c2 == 2) {
                            String ipAddress3 = reader.nextString();
                            ipAddress2 = ipAddress3;
                        } else if (c2 == 3) {
                            String userAgent3 = reader.nextString();
                            userAgent2 = userAgent3;
                        } else {
                            reader.skipValue();
                        }
                    }
                    reader.endObject();
                    userAgent = userAgent2;
                    continue;
                default:
                    ipAddress = ipAddress2;
                    reader.skipValue();
                    break;
            }
            userAgent = userAgent2;
            ipAddress2 = ipAddress;
        }
        String ipAddress4 = ipAddress2;
        String userAgent4 = userAgent;
        reader.endObject();
        if (status == null || started == null || release == null || release.isEmpty()) {
            this.options.getLogger().log(SentryLevel.ERROR, "Session is gonna be dropped due to invalid fields.", new Object[0]);
            return null;
        }
        return new Session(status, started, timestamp, errors, did, sid, init, seq, duration, ipAddress4, userAgent4, environment, release);
    }

    @Nullable
    private Date converTimeStamp(@NotNull String timestamp, @NotNull String field) {
        try {
            return DateUtils.getDateTime(timestamp);
        } catch (IllegalArgumentException e) {
            this.options.getLogger().log(SentryLevel.ERROR, e, "Error converting session (%s) field.", field);
            return null;
        }
    }
}
