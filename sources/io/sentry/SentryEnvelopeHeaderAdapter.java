package io.sentry;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.xiaopeng.commonfunc.Constant;
import com.xpeng.upso.aesserver.AesConstants;
import io.sentry.TraceState;
import io.sentry.protocol.SdkVersion;
import io.sentry.protocol.SentryId;
import io.sentry.protocol.SentryPackage;
import java.io.IOException;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class SentryEnvelopeHeaderAdapter extends TypeAdapter<SentryEnvelopeHeader> {
    @Override // com.google.gson.TypeAdapter
    public void write(@NotNull JsonWriter writer, @Nullable SentryEnvelopeHeader value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }
        writer.beginObject();
        if (value.getEventId() != null) {
            writer.name("event_id");
            writer.value(value.getEventId().toString());
        }
        SdkVersion sdkVersion = value.getSdkVersion();
        if (sdkVersion != null && hasValidSdkVersion(sdkVersion)) {
            writer.name("sdk").beginObject();
            writer.name("name").value(sdkVersion.getName());
            writer.name(AesConstants.REQUEST_PARAM_VERSION).value(sdkVersion.getVersion());
            List<String> integrations = sdkVersion.getIntegrations();
            if (integrations != null) {
                writer.name("integrations").beginArray();
                for (String integration : integrations) {
                    writer.value(integration);
                }
                writer.endArray();
            }
            List<SentryPackage> packages = sdkVersion.getPackages();
            if (packages != null) {
                writer.name("packages").beginArray();
                for (SentryPackage item : packages) {
                    writer.beginObject();
                    writer.name("name").value(item.getName());
                    writer.name(AesConstants.REQUEST_PARAM_VERSION).value(item.getVersion());
                    writer.endObject();
                }
                writer.endArray();
            }
            writer.endObject();
        }
        TraceState trace = value.getTrace();
        if (trace != null) {
            writer.name(SpanContext.TYPE);
            writer.beginObject();
            writer.name("trace_id").value(trace.getTraceId().toString());
            writer.name("public_key").value(trace.getPublicKey());
            if (trace.getRelease() != null) {
                writer.name("release").value(trace.getRelease());
            }
            if (trace.getEnvironment() != null) {
                writer.name("environment").value(trace.getEnvironment());
            }
            if (trace.getTransaction() != null) {
                writer.name("transaction").value(trace.getTransaction());
            }
            if (trace.getUser() != null && (trace.getUser().getId() != null || trace.getUser().getSegment() != null)) {
                writer.name(Constant.BUILD_TYPE_USER);
                writer.beginObject();
                if (trace.getUser().getId() != null) {
                    writer.name("id").value(trace.getUser().getId());
                }
                if (trace.getUser().getSegment() != null) {
                    writer.name("segment").value(trace.getUser().getSegment());
                }
                writer.endObject();
            }
            writer.endObject();
        }
        writer.endObject();
    }

    private boolean hasValidSdkVersion(@NotNull SdkVersion sdkVersion) {
        return (sdkVersion.getName() == null || sdkVersion.getName().isEmpty() || sdkVersion.getVersion() == null || sdkVersion.getVersion().isEmpty()) ? false : true;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.TypeAdapter
    @Nullable
    public SentryEnvelopeHeader read(@NotNull JsonReader reader) throws IOException {
        char c;
        char c2;
        char c3;
        TraceState.TraceStateUser traceStateUser;
        char c4;
        char c5;
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        SentryId eventId = null;
        SdkVersion sdkVersion = null;
        TraceState traceState = null;
        reader.beginObject();
        while (reader.hasNext()) {
            String nextName = reader.nextName();
            int hashCode = nextName.hashCode();
            if (hashCode == 113722) {
                if (nextName.equals("sdk")) {
                    c = 1;
                }
                c = 65535;
            } else if (hashCode != 110620997) {
                if (hashCode == 278118624 && nextName.equals("event_id")) {
                    c = 0;
                }
                c = 65535;
            } else {
                if (nextName.equals(SpanContext.TYPE)) {
                    c = 2;
                }
                c = 65535;
            }
            if (c == 0) {
                eventId = new SentryId(reader.nextString());
            } else if (c == 1) {
                reader.beginObject();
                sdkVersion = new SdkVersion();
                while (reader.hasNext()) {
                    String nextName2 = reader.nextName();
                    switch (nextName2.hashCode()) {
                        case 3373707:
                            if (nextName2.equals("name")) {
                                c2 = 0;
                                break;
                            }
                            c2 = 65535;
                            break;
                        case 351608024:
                            if (nextName2.equals(AesConstants.REQUEST_PARAM_VERSION)) {
                                c2 = 1;
                                break;
                            }
                            c2 = 65535;
                            break;
                        case 750867693:
                            if (nextName2.equals("packages")) {
                                c2 = 3;
                                break;
                            }
                            c2 = 65535;
                            break;
                        case 1487029535:
                            if (nextName2.equals("integrations")) {
                                c2 = 2;
                                break;
                            }
                            c2 = 65535;
                            break;
                        default:
                            c2 = 65535;
                            break;
                    }
                    if (c2 == 0) {
                        sdkVersion.setName(reader.nextString());
                    } else if (c2 == 1) {
                        sdkVersion.setVersion(reader.nextString());
                    } else if (c2 == 2) {
                        reader.beginArray();
                        while (reader.hasNext()) {
                            String integration = reader.nextString();
                            if (integration != null) {
                                sdkVersion.addIntegration(integration);
                            }
                        }
                        reader.endArray();
                    } else if (c2 == 3) {
                        reader.beginArray();
                        while (reader.hasNext()) {
                            reader.beginObject();
                            String name = null;
                            String version = null;
                            while (reader.hasNext()) {
                                String nextName3 = reader.nextName();
                                int hashCode2 = nextName3.hashCode();
                                if (hashCode2 != 3373707) {
                                    if (hashCode2 == 351608024 && nextName3.equals(AesConstants.REQUEST_PARAM_VERSION)) {
                                        c3 = 1;
                                    }
                                    c3 = 65535;
                                } else {
                                    if (nextName3.equals("name")) {
                                        c3 = 0;
                                    }
                                    c3 = 65535;
                                }
                                if (c3 == 0) {
                                    name = reader.nextString();
                                } else if (c3 == 1) {
                                    version = reader.nextString();
                                } else {
                                    reader.skipValue();
                                }
                            }
                            if (name != null && version != null) {
                                sdkVersion.addPackage(name, version);
                            }
                            reader.endObject();
                        }
                        reader.endArray();
                    } else {
                        reader.skipValue();
                    }
                }
                reader.endObject();
            } else if (c == 2) {
                reader.beginObject();
                SentryId traceId = null;
                String publicKey = null;
                String release = null;
                String environment = null;
                String transaction = null;
                String userId = null;
                String segment = null;
                while (reader.hasNext()) {
                    String nextName4 = reader.nextName();
                    switch (nextName4.hashCode()) {
                        case -85904877:
                            if (nextName4.equals("environment")) {
                                c4 = 3;
                                break;
                            }
                            c4 = 65535;
                            break;
                        case 3599307:
                            if (nextName4.equals(Constant.BUILD_TYPE_USER)) {
                                c4 = 5;
                                break;
                            }
                            c4 = 65535;
                            break;
                        case 1090594823:
                            if (nextName4.equals("release")) {
                                c4 = 2;
                                break;
                            }
                            c4 = 65535;
                            break;
                        case 1270300245:
                            if (nextName4.equals("trace_id")) {
                                c4 = 0;
                                break;
                            }
                            c4 = 65535;
                            break;
                        case 1904812937:
                            if (nextName4.equals("public_key")) {
                                c4 = 1;
                                break;
                            }
                            c4 = 65535;
                            break;
                        case 2141246174:
                            if (nextName4.equals("transaction")) {
                                c4 = 4;
                                break;
                            }
                            c4 = 65535;
                            break;
                        default:
                            c4 = 65535;
                            break;
                    }
                    if (c4 == 0) {
                        SentryId traceId2 = new SentryId(reader.nextString());
                        traceId = traceId2;
                    } else if (c4 == 1) {
                        String publicKey2 = reader.nextString();
                        publicKey = publicKey2;
                    } else if (c4 == 2) {
                        String release2 = reader.nextString();
                        release = release2;
                    } else if (c4 == 3) {
                        String environment2 = reader.nextString();
                        environment = environment2;
                    } else if (c4 == 4) {
                        String transaction2 = reader.nextString();
                        transaction = transaction2;
                    } else if (c4 == 5) {
                        reader.beginObject();
                        while (reader.hasNext()) {
                            String nextName5 = reader.nextName();
                            int hashCode3 = nextName5.hashCode();
                            if (hashCode3 != 3355) {
                                if (hashCode3 == 1973722931 && nextName5.equals("segment")) {
                                    c5 = 1;
                                }
                                c5 = 65535;
                            } else {
                                if (nextName5.equals("id")) {
                                    c5 = 0;
                                }
                                c5 = 65535;
                            }
                            if (c5 == 0) {
                                String userId2 = reader.nextString();
                                userId = userId2;
                            } else if (c5 == 1) {
                                String segment2 = reader.nextString();
                                segment = segment2;
                            } else {
                                reader.skipValue();
                            }
                        }
                        reader.endObject();
                    } else {
                        reader.skipValue();
                    }
                }
                if (traceId != null && publicKey != null) {
                    if (userId != null || segment != null) {
                        traceStateUser = new TraceState.TraceStateUser(userId, segment);
                    } else {
                        traceStateUser = null;
                    }
                    traceState = new TraceState(traceId, publicKey, release, environment, traceStateUser, transaction);
                }
                reader.endObject();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new SentryEnvelopeHeader(eventId, sdkVersion, traceState);
    }
}
