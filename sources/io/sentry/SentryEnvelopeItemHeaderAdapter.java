package io.sentry;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import io.sentry.util.StringUtils;
import java.io.IOException;
import java.util.Locale;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class SentryEnvelopeItemHeaderAdapter extends TypeAdapter<SentryEnvelopeItemHeader> {
    @Override // com.google.gson.TypeAdapter
    public void write(JsonWriter writer, SentryEnvelopeItemHeader value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }
        writer.beginObject();
        if (value.getContentType() != null) {
            writer.name("content_type");
            writer.value(value.getContentType());
        }
        if (value.getFileName() != null) {
            writer.name("filename");
            writer.value(value.getFileName());
        }
        if (!SentryItemType.Unknown.equals(value.getType())) {
            writer.name("type");
            writer.value(value.getType().getItemType().toLowerCase(Locale.ROOT));
        }
        if (value.getAttachmentType() != null) {
            writer.name("attachment_type");
            writer.value(value.getAttachmentType());
        }
        writer.name("length");
        writer.value(value.getLength());
        writer.endObject();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.TypeAdapter
    @Nullable
    public SentryEnvelopeItemHeader read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        String contentType = null;
        String fileName = null;
        SentryItemType type = SentryItemType.Unknown;
        int length = 0;
        String attachmentType = null;
        reader.beginObject();
        while (reader.hasNext()) {
            String nextName = reader.nextName();
            char c = 65535;
            switch (nextName.hashCode()) {
                case -1106363674:
                    if (nextName.equals("length")) {
                        c = 3;
                        break;
                    }
                    break;
                case -734768633:
                    if (nextName.equals("filename")) {
                        c = 1;
                        break;
                    }
                    break;
                case -672977706:
                    if (nextName.equals("attachment_type")) {
                        c = 4;
                        break;
                    }
                    break;
                case 3575610:
                    if (nextName.equals("type")) {
                        c = 2;
                        break;
                    }
                    break;
                case 831846208:
                    if (nextName.equals("content_type")) {
                        c = 0;
                        break;
                    }
                    break;
            }
            if (c == 0) {
                contentType = reader.nextString();
            } else if (c == 1) {
                fileName = reader.nextString();
            } else if (c == 2) {
                try {
                    String nextString = reader.nextString();
                    if (nextString != null) {
                        if (nextString.equalsIgnoreCase("user_report")) {
                            type = SentryItemType.UserFeedback;
                        } else {
                            String capitalizedString = StringUtils.capitalize(nextString);
                            if (capitalizedString != null) {
                                type = SentryItemType.valueOf(capitalizedString);
                            }
                        }
                    }
                } catch (IllegalArgumentException e) {
                }
            } else if (c == 3) {
                length = reader.nextInt();
            } else if (c == 4) {
                attachmentType = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new SentryEnvelopeItemHeader(type, length, contentType, fileName, attachmentType);
    }
}
