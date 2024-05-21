package com.xpeng.upso.proxy;

import ch.ethz.ssh2.packets.Packets;
import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.AbstractParser;
import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Descriptors;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Internal;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.LazyStringArrayList;
import com.google.protobuf.LazyStringList;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.Parser;
import com.google.protobuf.ProtocolMessageEnum;
import com.google.protobuf.ProtocolStringList;
import com.google.protobuf.RepeatedFieldBuilderV3;
import com.google.protobuf.SingleFieldBuilderV3;
import com.google.protobuf.UninitializedMessageException;
import com.google.protobuf.UnknownFieldSet;
import com.xiaopeng.libbluetooth.bean.AudioControl;
import com.xiaopeng.xmlconfig.Support;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: classes2.dex */
public final class PSOProtocol {
    private static Descriptors.FileDescriptor descriptor;
    private static final Descriptors.Descriptor internal_static_protocol_IdentityInfo_descriptor = getDescriptor().getMessageTypes().get(0);
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_protocol_IdentityInfo_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_protocol_IdentityInfo_descriptor, new String[]{"Role", Support.Version.TAG, "DeviceType", "DeviceId", "Mask", "HasSecret", "CarType", "User", "RomInfo", "Platform"});
    private static final Descriptors.Descriptor internal_static_protocol_SecretAuth_descriptor = getDescriptor().getMessageTypes().get(1);
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_protocol_SecretAuth_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_protocol_SecretAuth_descriptor, new String[]{"Index", "Cont", "Result"});
    private static final Descriptors.Descriptor internal_static_protocol_SecretPresetResponse_descriptor = getDescriptor().getMessageTypes().get(2);
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_protocol_SecretPresetResponse_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_protocol_SecretPresetResponse_descriptor, new String[]{"Index"});
    private static final Descriptors.Descriptor internal_static_protocol_SecretDelete_descriptor = getDescriptor().getMessageTypes().get(3);
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_protocol_SecretDelete_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_protocol_SecretDelete_descriptor, new String[]{"Index"});
    private static final Descriptors.Descriptor internal_static_protocol_Secret_descriptor = getDescriptor().getMessageTypes().get(4);
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_protocol_Secret_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_protocol_Secret_descriptor, new String[]{"Type", "Name", "Index", "CreateTime", "Secret", "Sign"});
    private static final Descriptors.Descriptor internal_static_protocol_SecretPreset_descriptor = getDescriptor().getMessageTypes().get(5);
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_protocol_SecretPreset_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_protocol_SecretPreset_descriptor, new String[]{"Index", "Cont", "Secret"});
    private static final Descriptors.Descriptor internal_static_protocol_SecretDeleteResponse_descriptor = getDescriptor().getMessageTypes().get(6);
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_protocol_SecretDeleteResponse_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_protocol_SecretDeleteResponse_descriptor, new String[]{"Index", "Status"});
    private static final Descriptors.Descriptor internal_static_protocol_RequestResponse_descriptor = getDescriptor().getMessageTypes().get(7);
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_protocol_RequestResponse_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_protocol_RequestResponse_descriptor, new String[]{"MessageType", "SnId", "Sequence", "IdentityInfo", "SecrettAuth", "SecretPreset", "SecretDelete", "SecretPresetResponse", "SecretDeleteReponse", "Result", "Content"});

    /* loaded from: classes2.dex */
    public interface IdentityInfoOrBuilder extends MessageOrBuilder {
        String getCarType();

        ByteString getCarTypeBytes();

        String getDeviceId();

        ByteString getDeviceIdBytes();

        String getDeviceType();

        ByteString getDeviceTypeBytes();

        int getHasSecret();

        int getMask();

        String getPlatform();

        ByteString getPlatformBytes();

        int getRole();

        String getRomInfo();

        ByteString getRomInfoBytes();

        int getUser();

        int getVersion();
    }

    /* loaded from: classes2.dex */
    public interface RequestResponseOrBuilder extends MessageOrBuilder {
        RequestResponse.ContentCase getContentCase();

        IdentityInfo getIdentityInfo();

        IdentityInfoOrBuilder getIdentityInfoOrBuilder();

        RequestResponse.MessageType getMessageType();

        int getMessageTypeValue();

        int getResult();

        SecretDelete getSecretDelete();

        SecretDeleteOrBuilder getSecretDeleteOrBuilder();

        SecretDeleteResponse getSecretDeleteReponse();

        SecretDeleteResponseOrBuilder getSecretDeleteReponseOrBuilder();

        SecretPreset getSecretPreset();

        SecretPresetOrBuilder getSecretPresetOrBuilder();

        SecretPresetResponse getSecretPresetResponse();

        SecretPresetResponseOrBuilder getSecretPresetResponseOrBuilder();

        SecretAuth getSecrettAuth();

        SecretAuthOrBuilder getSecrettAuthOrBuilder();

        int getSequence();

        String getSnId();

        ByteString getSnIdBytes();

        boolean hasIdentityInfo();

        boolean hasSecretDelete();

        boolean hasSecretDeleteReponse();

        boolean hasSecretPreset();

        boolean hasSecretPresetResponse();

        boolean hasSecrettAuth();
    }

    /* loaded from: classes2.dex */
    public interface SecretAuthOrBuilder extends MessageOrBuilder {
        int getCont();

        int getIndex();

        String getResult(int index);

        ByteString getResultBytes(int index);

        int getResultCount();

        List<String> getResultList();
    }

    /* loaded from: classes2.dex */
    public interface SecretDeleteOrBuilder extends MessageOrBuilder {
        int getIndex();
    }

    /* loaded from: classes2.dex */
    public interface SecretDeleteResponseOrBuilder extends MessageOrBuilder {
        int getIndex();

        int getStatus();
    }

    /* loaded from: classes2.dex */
    public interface SecretOrBuilder extends MessageOrBuilder {
        long getCreateTime();

        long getIndex();

        String getName();

        ByteString getNameBytes();

        String getSecret();

        ByteString getSecretBytes();

        String getSign();

        ByteString getSignBytes();

        Secret.SecretType getType();

        int getTypeValue();
    }

    /* loaded from: classes2.dex */
    public interface SecretPresetOrBuilder extends MessageOrBuilder {
        int getCont();

        int getIndex();

        Secret getSecret(int index);

        int getSecretCount();

        List<Secret> getSecretList();

        SecretOrBuilder getSecretOrBuilder(int index);

        List<? extends SecretOrBuilder> getSecretOrBuilderList();
    }

    /* loaded from: classes2.dex */
    public interface SecretPresetResponseOrBuilder extends MessageOrBuilder {
        int getIndex();
    }

    private PSOProtocol() {
    }

    public static void registerAllExtensions(ExtensionRegistryLite registry) {
    }

    public static void registerAllExtensions(ExtensionRegistry registry) {
        registerAllExtensions((ExtensionRegistryLite) registry);
    }

    /* loaded from: classes2.dex */
    public static final class IdentityInfo extends GeneratedMessageV3 implements IdentityInfoOrBuilder {
        public static final int CAR_TYPE_FIELD_NUMBER = 7;
        public static final int DEVICE_ID_FIELD_NUMBER = 4;
        public static final int DEVICE_TYPE_FIELD_NUMBER = 3;
        public static final int HAS_SECRET_FIELD_NUMBER = 6;
        public static final int MASK_FIELD_NUMBER = 5;
        public static final int PLATFORM_FIELD_NUMBER = 10;
        public static final int ROLE_FIELD_NUMBER = 1;
        public static final int ROM_INFO_FIELD_NUMBER = 9;
        public static final int USER_FIELD_NUMBER = 8;
        public static final int VERSION_FIELD_NUMBER = 2;
        private static final long serialVersionUID = 0;
        private volatile Object carType_;
        private volatile Object deviceId_;
        private volatile Object deviceType_;
        private int hasSecret_;
        private int mask_;
        private byte memoizedIsInitialized;
        private volatile Object platform_;
        private int role_;
        private volatile Object romInfo_;
        private int user_;
        private int version_;
        private static final IdentityInfo DEFAULT_INSTANCE = new IdentityInfo();
        private static final Parser<IdentityInfo> PARSER = new AbstractParser<IdentityInfo>() { // from class: com.xpeng.upso.proxy.PSOProtocol.IdentityInfo.1
            @Override // com.google.protobuf.Parser
            public IdentityInfo parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new IdentityInfo(input, extensionRegistry);
            }
        };

        private IdentityInfo(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private IdentityInfo() {
            this.memoizedIsInitialized = (byte) -1;
            this.deviceType_ = "";
            this.deviceId_ = "";
            this.carType_ = "";
            this.romInfo_ = "";
            this.platform_ = "";
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
            return new IdentityInfo();
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private IdentityInfo(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistry == null) {
                throw new NullPointerException();
            }
            UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
            boolean done = false;
            while (!done) {
                try {
                    try {
                        try {
                            int tag = input.readTag();
                            switch (tag) {
                                case 0:
                                    done = true;
                                    break;
                                case 8:
                                    this.role_ = input.readUInt32();
                                    break;
                                case 16:
                                    this.version_ = input.readUInt32();
                                    break;
                                case 26:
                                    String s = input.readStringRequireUtf8();
                                    this.deviceType_ = s;
                                    break;
                                case 34:
                                    String s2 = input.readStringRequireUtf8();
                                    this.deviceId_ = s2;
                                    break;
                                case 40:
                                    this.mask_ = input.readUInt32();
                                    break;
                                case 48:
                                    this.hasSecret_ = input.readUInt32();
                                    break;
                                case 58:
                                    String s3 = input.readStringRequireUtf8();
                                    this.carType_ = s3;
                                    break;
                                case 64:
                                    this.user_ = input.readUInt32();
                                    break;
                                case AudioControl.CONTROL_EJECT /* 74 */:
                                    String s4 = input.readStringRequireUtf8();
                                    this.romInfo_ = s4;
                                    break;
                                case Packets.SSH_MSG_REQUEST_FAILURE /* 82 */:
                                    String s5 = input.readStringRequireUtf8();
                                    this.platform_ = s5;
                                    break;
                                default:
                                    if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                                        done = true;
                                        break;
                                    } else {
                                        break;
                                    }
                            }
                        } catch (InvalidProtocolBufferException e) {
                            throw e.setUnfinishedMessage(this);
                        }
                    } catch (UninitializedMessageException e2) {
                        throw e2.asInvalidProtocolBufferException().setUnfinishedMessage(this);
                    } catch (IOException e3) {
                        throw new InvalidProtocolBufferException(e3).setUnfinishedMessage(this);
                    }
                } finally {
                    this.unknownFields = unknownFields.build();
                    makeExtensionsImmutable();
                }
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return PSOProtocol.internal_static_protocol_IdentityInfo_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return PSOProtocol.internal_static_protocol_IdentityInfo_fieldAccessorTable.ensureFieldAccessorsInitialized(IdentityInfo.class, Builder.class);
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
        public int getRole() {
            return this.role_;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
        public int getVersion() {
            return this.version_;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
        public String getDeviceType() {
            Object ref = this.deviceType_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            this.deviceType_ = s;
            return s;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
        public ByteString getDeviceTypeBytes() {
            Object ref = this.deviceType_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.deviceType_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
        public String getDeviceId() {
            Object ref = this.deviceId_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            this.deviceId_ = s;
            return s;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
        public ByteString getDeviceIdBytes() {
            Object ref = this.deviceId_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.deviceId_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
        public int getMask() {
            return this.mask_;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
        public int getHasSecret() {
            return this.hasSecret_;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
        public String getCarType() {
            Object ref = this.carType_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            this.carType_ = s;
            return s;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
        public ByteString getCarTypeBytes() {
            Object ref = this.carType_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.carType_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
        public int getUser() {
            return this.user_;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
        public String getRomInfo() {
            Object ref = this.romInfo_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            this.romInfo_ = s;
            return s;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
        public ByteString getRomInfoBytes() {
            Object ref = this.romInfo_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.romInfo_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
        public String getPlatform() {
            Object ref = this.platform_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            this.platform_ = s;
            return s;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
        public ByteString getPlatformBytes() {
            Object ref = this.platform_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.platform_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized == 1) {
                return true;
            }
            if (isInitialized == 0) {
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public void writeTo(CodedOutputStream output) throws IOException {
            int i = this.role_;
            if (i != 0) {
                output.writeUInt32(1, i);
            }
            int i2 = this.version_;
            if (i2 != 0) {
                output.writeUInt32(2, i2);
            }
            if (!GeneratedMessageV3.isStringEmpty(this.deviceType_)) {
                GeneratedMessageV3.writeString(output, 3, this.deviceType_);
            }
            if (!GeneratedMessageV3.isStringEmpty(this.deviceId_)) {
                GeneratedMessageV3.writeString(output, 4, this.deviceId_);
            }
            int i3 = this.mask_;
            if (i3 != 0) {
                output.writeUInt32(5, i3);
            }
            int i4 = this.hasSecret_;
            if (i4 != 0) {
                output.writeUInt32(6, i4);
            }
            if (!GeneratedMessageV3.isStringEmpty(this.carType_)) {
                GeneratedMessageV3.writeString(output, 7, this.carType_);
            }
            int i5 = this.user_;
            if (i5 != 0) {
                output.writeUInt32(8, i5);
            }
            if (!GeneratedMessageV3.isStringEmpty(this.romInfo_)) {
                GeneratedMessageV3.writeString(output, 9, this.romInfo_);
            }
            if (!GeneratedMessageV3.isStringEmpty(this.platform_)) {
                GeneratedMessageV3.writeString(output, 10, this.platform_);
            }
            this.unknownFields.writeTo(output);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSize;
            if (size != -1) {
                return size;
            }
            int i = this.role_;
            int size2 = i != 0 ? 0 + CodedOutputStream.computeUInt32Size(1, i) : 0;
            int i2 = this.version_;
            if (i2 != 0) {
                size2 += CodedOutputStream.computeUInt32Size(2, i2);
            }
            if (!GeneratedMessageV3.isStringEmpty(this.deviceType_)) {
                size2 += GeneratedMessageV3.computeStringSize(3, this.deviceType_);
            }
            if (!GeneratedMessageV3.isStringEmpty(this.deviceId_)) {
                size2 += GeneratedMessageV3.computeStringSize(4, this.deviceId_);
            }
            int i3 = this.mask_;
            if (i3 != 0) {
                size2 += CodedOutputStream.computeUInt32Size(5, i3);
            }
            int i4 = this.hasSecret_;
            if (i4 != 0) {
                size2 += CodedOutputStream.computeUInt32Size(6, i4);
            }
            if (!GeneratedMessageV3.isStringEmpty(this.carType_)) {
                size2 += GeneratedMessageV3.computeStringSize(7, this.carType_);
            }
            int i5 = this.user_;
            if (i5 != 0) {
                size2 += CodedOutputStream.computeUInt32Size(8, i5);
            }
            if (!GeneratedMessageV3.isStringEmpty(this.romInfo_)) {
                size2 += GeneratedMessageV3.computeStringSize(9, this.romInfo_);
            }
            if (!GeneratedMessageV3.isStringEmpty(this.platform_)) {
                size2 += GeneratedMessageV3.computeStringSize(10, this.platform_);
            }
            int size3 = size2 + this.unknownFields.getSerializedSize();
            this.memoizedSize = size3;
            return size3;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof IdentityInfo)) {
                return super.equals(obj);
            }
            IdentityInfo other = (IdentityInfo) obj;
            return getRole() == other.getRole() && getVersion() == other.getVersion() && getDeviceType().equals(other.getDeviceType()) && getDeviceId().equals(other.getDeviceId()) && getMask() == other.getMask() && getHasSecret() == other.getHasSecret() && getCarType().equals(other.getCarType()) && getUser() == other.getUser() && getRomInfo().equals(other.getRomInfo()) && getPlatform().equals(other.getPlatform()) && this.unknownFields.equals(other.unknownFields);
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = (((((((((((((((((((((((((((((((((((((((((((41 * 19) + getDescriptor().hashCode()) * 37) + 1) * 53) + getRole()) * 37) + 2) * 53) + getVersion()) * 37) + 3) * 53) + getDeviceType().hashCode()) * 37) + 4) * 53) + getDeviceId().hashCode()) * 37) + 5) * 53) + getMask()) * 37) + 6) * 53) + getHasSecret()) * 37) + 7) * 53) + getCarType().hashCode()) * 37) + 8) * 53) + getUser()) * 37) + 9) * 53) + getRomInfo().hashCode()) * 37) + 10) * 53) + getPlatform().hashCode()) * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash;
            return hash;
        }

        public static IdentityInfo parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static IdentityInfo parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static IdentityInfo parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static IdentityInfo parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static IdentityInfo parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static IdentityInfo parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static IdentityInfo parseFrom(InputStream input) throws IOException {
            return (IdentityInfo) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static IdentityInfo parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (IdentityInfo) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        public static IdentityInfo parseDelimitedFrom(InputStream input) throws IOException {
            return (IdentityInfo) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
        }

        public static IdentityInfo parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (IdentityInfo) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
        }

        public static IdentityInfo parseFrom(CodedInputStream input) throws IOException {
            return (IdentityInfo) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static IdentityInfo parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (IdentityInfo) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(IdentityInfo prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
            Builder builder = new Builder(parent);
            return builder;
        }

        /* loaded from: classes2.dex */
        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements IdentityInfoOrBuilder {
            private Object carType_;
            private Object deviceId_;
            private Object deviceType_;
            private int hasSecret_;
            private int mask_;
            private Object platform_;
            private int role_;
            private Object romInfo_;
            private int user_;
            private int version_;

            public static final Descriptors.Descriptor getDescriptor() {
                return PSOProtocol.internal_static_protocol_IdentityInfo_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return PSOProtocol.internal_static_protocol_IdentityInfo_fieldAccessorTable.ensureFieldAccessorsInitialized(IdentityInfo.class, Builder.class);
            }

            private Builder() {
                this.deviceType_ = "";
                this.deviceId_ = "";
                this.carType_ = "";
                this.romInfo_ = "";
                this.platform_ = "";
                maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessageV3.BuilderParent parent) {
                super(parent);
                this.deviceType_ = "";
                this.deviceId_ = "";
                this.carType_ = "";
                this.romInfo_ = "";
                this.platform_ = "";
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = IdentityInfo.alwaysUseFieldBuilders;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.role_ = 0;
                this.version_ = 0;
                this.deviceType_ = "";
                this.deviceId_ = "";
                this.mask_ = 0;
                this.hasSecret_ = 0;
                this.carType_ = "";
                this.user_ = 0;
                this.romInfo_ = "";
                this.platform_ = "";
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return PSOProtocol.internal_static_protocol_IdentityInfo_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public IdentityInfo getDefaultInstanceForType() {
                return IdentityInfo.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public IdentityInfo build() {
                IdentityInfo result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException((Message) result);
                }
                return result;
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public IdentityInfo buildPartial() {
                IdentityInfo result = new IdentityInfo(this);
                result.role_ = this.role_;
                result.version_ = this.version_;
                result.deviceType_ = this.deviceType_;
                result.deviceId_ = this.deviceId_;
                result.mask_ = this.mask_;
                result.hasSecret_ = this.hasSecret_;
                result.carType_ = this.carType_;
                result.user_ = this.user_;
                result.romInfo_ = this.romInfo_;
                result.platform_ = this.platform_;
                onBuilt();
                return result;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder
            /* renamed from: clone */
            public Builder mo93clone() {
                return (Builder) super.mo93clone();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setField(Descriptors.FieldDescriptor field, Object value) {
                return (Builder) super.setField(field, value);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder clearField(Descriptors.FieldDescriptor field) {
                return (Builder) super.clearField(field);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder clearOneof(Descriptors.OneofDescriptor oneof) {
                return (Builder) super.clearOneof(oneof);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
                return (Builder) super.setRepeatedField(field, index, value);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
                return (Builder) super.addRepeatedField(field, value);
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(Message other) {
                if (other instanceof IdentityInfo) {
                    return mergeFrom((IdentityInfo) other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(IdentityInfo other) {
                if (other == IdentityInfo.getDefaultInstance()) {
                    return this;
                }
                if (other.getRole() != 0) {
                    setRole(other.getRole());
                }
                if (other.getVersion() != 0) {
                    setVersion(other.getVersion());
                }
                if (!other.getDeviceType().isEmpty()) {
                    this.deviceType_ = other.deviceType_;
                    onChanged();
                }
                if (!other.getDeviceId().isEmpty()) {
                    this.deviceId_ = other.deviceId_;
                    onChanged();
                }
                if (other.getMask() != 0) {
                    setMask(other.getMask());
                }
                if (other.getHasSecret() != 0) {
                    setHasSecret(other.getHasSecret());
                }
                if (!other.getCarType().isEmpty()) {
                    this.carType_ = other.carType_;
                    onChanged();
                }
                if (other.getUser() != 0) {
                    setUser(other.getUser());
                }
                if (!other.getRomInfo().isEmpty()) {
                    this.romInfo_ = other.romInfo_;
                    onChanged();
                }
                if (!other.getPlatform().isEmpty()) {
                    this.platform_ = other.platform_;
                    onChanged();
                }
                mergeUnknownFields(other.unknownFields);
                onChanged();
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                IdentityInfo parsedMessage = null;
                try {
                    try {
                        parsedMessage = (IdentityInfo) IdentityInfo.PARSER.parsePartialFrom(input, extensionRegistry);
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        IdentityInfo identityInfo = (IdentityInfo) e.getUnfinishedMessage();
                        throw e.unwrapIOException();
                    }
                } finally {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                }
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
            public int getRole() {
                return this.role_;
            }

            public Builder setRole(int value) {
                this.role_ = value;
                onChanged();
                return this;
            }

            public Builder clearRole() {
                this.role_ = 0;
                onChanged();
                return this;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
            public int getVersion() {
                return this.version_;
            }

            public Builder setVersion(int value) {
                this.version_ = value;
                onChanged();
                return this;
            }

            public Builder clearVersion() {
                this.version_ = 0;
                onChanged();
                return this;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
            public String getDeviceType() {
                Object ref = this.deviceType_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    this.deviceType_ = s;
                    return s;
                }
                return (String) ref;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
            public ByteString getDeviceTypeBytes() {
                Object ref = this.deviceType_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.deviceType_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            public Builder setDeviceType(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.deviceType_ = value;
                onChanged();
                return this;
            }

            public Builder clearDeviceType() {
                this.deviceType_ = IdentityInfo.getDefaultInstance().getDeviceType();
                onChanged();
                return this;
            }

            public Builder setDeviceTypeBytes(ByteString value) {
                if (value != null) {
                    IdentityInfo.checkByteStringIsUtf8(value);
                    this.deviceType_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
            public String getDeviceId() {
                Object ref = this.deviceId_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    this.deviceId_ = s;
                    return s;
                }
                return (String) ref;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
            public ByteString getDeviceIdBytes() {
                Object ref = this.deviceId_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.deviceId_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            public Builder setDeviceId(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.deviceId_ = value;
                onChanged();
                return this;
            }

            public Builder clearDeviceId() {
                this.deviceId_ = IdentityInfo.getDefaultInstance().getDeviceId();
                onChanged();
                return this;
            }

            public Builder setDeviceIdBytes(ByteString value) {
                if (value != null) {
                    IdentityInfo.checkByteStringIsUtf8(value);
                    this.deviceId_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
            public int getMask() {
                return this.mask_;
            }

            public Builder setMask(int value) {
                this.mask_ = value;
                onChanged();
                return this;
            }

            public Builder clearMask() {
                this.mask_ = 0;
                onChanged();
                return this;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
            public int getHasSecret() {
                return this.hasSecret_;
            }

            public Builder setHasSecret(int value) {
                this.hasSecret_ = value;
                onChanged();
                return this;
            }

            public Builder clearHasSecret() {
                this.hasSecret_ = 0;
                onChanged();
                return this;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
            public String getCarType() {
                Object ref = this.carType_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    this.carType_ = s;
                    return s;
                }
                return (String) ref;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
            public ByteString getCarTypeBytes() {
                Object ref = this.carType_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.carType_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            public Builder setCarType(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.carType_ = value;
                onChanged();
                return this;
            }

            public Builder clearCarType() {
                this.carType_ = IdentityInfo.getDefaultInstance().getCarType();
                onChanged();
                return this;
            }

            public Builder setCarTypeBytes(ByteString value) {
                if (value != null) {
                    IdentityInfo.checkByteStringIsUtf8(value);
                    this.carType_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
            public int getUser() {
                return this.user_;
            }

            public Builder setUser(int value) {
                this.user_ = value;
                onChanged();
                return this;
            }

            public Builder clearUser() {
                this.user_ = 0;
                onChanged();
                return this;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
            public String getRomInfo() {
                Object ref = this.romInfo_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    this.romInfo_ = s;
                    return s;
                }
                return (String) ref;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
            public ByteString getRomInfoBytes() {
                Object ref = this.romInfo_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.romInfo_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            public Builder setRomInfo(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.romInfo_ = value;
                onChanged();
                return this;
            }

            public Builder clearRomInfo() {
                this.romInfo_ = IdentityInfo.getDefaultInstance().getRomInfo();
                onChanged();
                return this;
            }

            public Builder setRomInfoBytes(ByteString value) {
                if (value != null) {
                    IdentityInfo.checkByteStringIsUtf8(value);
                    this.romInfo_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
            public String getPlatform() {
                Object ref = this.platform_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    this.platform_ = s;
                    return s;
                }
                return (String) ref;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.IdentityInfoOrBuilder
            public ByteString getPlatformBytes() {
                Object ref = this.platform_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.platform_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            public Builder setPlatform(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.platform_ = value;
                onChanged();
                return this;
            }

            public Builder clearPlatform() {
                this.platform_ = IdentityInfo.getDefaultInstance().getPlatform();
                onChanged();
                return this;
            }

            public Builder setPlatformBytes(ByteString value) {
                if (value != null) {
                    IdentityInfo.checkByteStringIsUtf8(value);
                    this.platform_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public final Builder setUnknownFields(final UnknownFieldSet unknownFields) {
                return (Builder) super.setUnknownFields(unknownFields);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public final Builder mergeUnknownFields(final UnknownFieldSet unknownFields) {
                return (Builder) super.mergeUnknownFields(unknownFields);
            }
        }

        public static IdentityInfo getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<IdentityInfo> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<IdentityInfo> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public IdentityInfo getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    /* loaded from: classes2.dex */
    public static final class SecretAuth extends GeneratedMessageV3 implements SecretAuthOrBuilder {
        public static final int CONT_FIELD_NUMBER = 2;
        public static final int INDEX_FIELD_NUMBER = 1;
        public static final int RESULT_FIELD_NUMBER = 3;
        private static final long serialVersionUID = 0;
        private int cont_;
        private int index_;
        private byte memoizedIsInitialized;
        private LazyStringList result_;
        private static final SecretAuth DEFAULT_INSTANCE = new SecretAuth();
        private static final Parser<SecretAuth> PARSER = new AbstractParser<SecretAuth>() { // from class: com.xpeng.upso.proxy.PSOProtocol.SecretAuth.1
            @Override // com.google.protobuf.Parser
            public SecretAuth parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new SecretAuth(input, extensionRegistry);
            }
        };

        private SecretAuth(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private SecretAuth() {
            this.memoizedIsInitialized = (byte) -1;
            this.result_ = LazyStringArrayList.EMPTY;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
            return new SecretAuth();
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private SecretAuth(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistry == null) {
                throw new NullPointerException();
            }
            int mutable_bitField0_ = 0;
            UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
            boolean done = false;
            while (!done) {
                try {
                    try {
                        int tag = input.readTag();
                        if (tag == 0) {
                            done = true;
                        } else if (tag == 8) {
                            this.index_ = input.readUInt32();
                        } else if (tag == 16) {
                            this.cont_ = input.readUInt32();
                        } else if (tag == 26) {
                            String s = input.readStringRequireUtf8();
                            if ((mutable_bitField0_ & 1) == 0) {
                                this.result_ = new LazyStringArrayList();
                                mutable_bitField0_ |= 1;
                            }
                            this.result_.add(s);
                        } else if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                            done = true;
                        }
                    } catch (InvalidProtocolBufferException e) {
                        throw e.setUnfinishedMessage(this);
                    } catch (UninitializedMessageException e2) {
                        throw e2.asInvalidProtocolBufferException().setUnfinishedMessage(this);
                    } catch (IOException e3) {
                        throw new InvalidProtocolBufferException(e3).setUnfinishedMessage(this);
                    }
                } finally {
                    if ((mutable_bitField0_ & 1) != 0) {
                        this.result_ = this.result_.getUnmodifiableView();
                    }
                    this.unknownFields = unknownFields.build();
                    makeExtensionsImmutable();
                }
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return PSOProtocol.internal_static_protocol_SecretAuth_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return PSOProtocol.internal_static_protocol_SecretAuth_fieldAccessorTable.ensureFieldAccessorsInitialized(SecretAuth.class, Builder.class);
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.SecretAuthOrBuilder
        public int getIndex() {
            return this.index_;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.SecretAuthOrBuilder
        public int getCont() {
            return this.cont_;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.SecretAuthOrBuilder
        public ProtocolStringList getResultList() {
            return this.result_;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.SecretAuthOrBuilder
        public int getResultCount() {
            return this.result_.size();
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.SecretAuthOrBuilder
        public String getResult(int index) {
            return (String) this.result_.get(index);
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.SecretAuthOrBuilder
        public ByteString getResultBytes(int index) {
            return this.result_.getByteString(index);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized == 1) {
                return true;
            }
            if (isInitialized == 0) {
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public void writeTo(CodedOutputStream output) throws IOException {
            int i = this.index_;
            if (i != 0) {
                output.writeUInt32(1, i);
            }
            int i2 = this.cont_;
            if (i2 != 0) {
                output.writeUInt32(2, i2);
            }
            for (int i3 = 0; i3 < this.result_.size(); i3++) {
                GeneratedMessageV3.writeString(output, 3, this.result_.getRaw(i3));
            }
            this.unknownFields.writeTo(output);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSize;
            if (size != -1) {
                return size;
            }
            int i = this.index_;
            int size2 = i != 0 ? 0 + CodedOutputStream.computeUInt32Size(1, i) : 0;
            int i2 = this.cont_;
            if (i2 != 0) {
                size2 += CodedOutputStream.computeUInt32Size(2, i2);
            }
            int dataSize = 0;
            for (int i3 = 0; i3 < this.result_.size(); i3++) {
                dataSize += computeStringSizeNoTag(this.result_.getRaw(i3));
            }
            int size3 = size2 + dataSize + (getResultList().size() * 1) + this.unknownFields.getSerializedSize();
            this.memoizedSize = size3;
            return size3;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof SecretAuth)) {
                return super.equals(obj);
            }
            SecretAuth other = (SecretAuth) obj;
            return getIndex() == other.getIndex() && getCont() == other.getCont() && getResultList().equals(other.getResultList()) && this.unknownFields.equals(other.unknownFields);
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = (((((((((41 * 19) + getDescriptor().hashCode()) * 37) + 1) * 53) + getIndex()) * 37) + 2) * 53) + getCont();
            if (getResultCount() > 0) {
                hash = (((hash * 37) + 3) * 53) + getResultList().hashCode();
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        public static SecretAuth parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static SecretAuth parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static SecretAuth parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static SecretAuth parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static SecretAuth parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static SecretAuth parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static SecretAuth parseFrom(InputStream input) throws IOException {
            return (SecretAuth) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static SecretAuth parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (SecretAuth) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        public static SecretAuth parseDelimitedFrom(InputStream input) throws IOException {
            return (SecretAuth) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
        }

        public static SecretAuth parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (SecretAuth) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
        }

        public static SecretAuth parseFrom(CodedInputStream input) throws IOException {
            return (SecretAuth) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static SecretAuth parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (SecretAuth) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(SecretAuth prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
            Builder builder = new Builder(parent);
            return builder;
        }

        /* loaded from: classes2.dex */
        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements SecretAuthOrBuilder {
            private int bitField0_;
            private int cont_;
            private int index_;
            private LazyStringList result_;

            public static final Descriptors.Descriptor getDescriptor() {
                return PSOProtocol.internal_static_protocol_SecretAuth_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return PSOProtocol.internal_static_protocol_SecretAuth_fieldAccessorTable.ensureFieldAccessorsInitialized(SecretAuth.class, Builder.class);
            }

            private Builder() {
                this.result_ = LazyStringArrayList.EMPTY;
                maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessageV3.BuilderParent parent) {
                super(parent);
                this.result_ = LazyStringArrayList.EMPTY;
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = SecretAuth.alwaysUseFieldBuilders;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.index_ = 0;
                this.cont_ = 0;
                this.result_ = LazyStringArrayList.EMPTY;
                this.bitField0_ &= -2;
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return PSOProtocol.internal_static_protocol_SecretAuth_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public SecretAuth getDefaultInstanceForType() {
                return SecretAuth.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public SecretAuth build() {
                SecretAuth result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException((Message) result);
                }
                return result;
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public SecretAuth buildPartial() {
                SecretAuth result = new SecretAuth(this);
                int i = this.bitField0_;
                result.index_ = this.index_;
                result.cont_ = this.cont_;
                if ((this.bitField0_ & 1) != 0) {
                    this.result_ = this.result_.getUnmodifiableView();
                    this.bitField0_ &= -2;
                }
                result.result_ = this.result_;
                onBuilt();
                return result;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder
            /* renamed from: clone */
            public Builder mo93clone() {
                return (Builder) super.mo93clone();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setField(Descriptors.FieldDescriptor field, Object value) {
                return (Builder) super.setField(field, value);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder clearField(Descriptors.FieldDescriptor field) {
                return (Builder) super.clearField(field);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder clearOneof(Descriptors.OneofDescriptor oneof) {
                return (Builder) super.clearOneof(oneof);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
                return (Builder) super.setRepeatedField(field, index, value);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
                return (Builder) super.addRepeatedField(field, value);
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(Message other) {
                if (other instanceof SecretAuth) {
                    return mergeFrom((SecretAuth) other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(SecretAuth other) {
                if (other == SecretAuth.getDefaultInstance()) {
                    return this;
                }
                if (other.getIndex() != 0) {
                    setIndex(other.getIndex());
                }
                if (other.getCont() != 0) {
                    setCont(other.getCont());
                }
                if (!other.result_.isEmpty()) {
                    if (this.result_.isEmpty()) {
                        this.result_ = other.result_;
                        this.bitField0_ &= -2;
                    } else {
                        ensureResultIsMutable();
                        this.result_.addAll(other.result_);
                    }
                    onChanged();
                }
                mergeUnknownFields(other.unknownFields);
                onChanged();
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                SecretAuth parsedMessage = null;
                try {
                    try {
                        parsedMessage = (SecretAuth) SecretAuth.PARSER.parsePartialFrom(input, extensionRegistry);
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        SecretAuth secretAuth = (SecretAuth) e.getUnfinishedMessage();
                        throw e.unwrapIOException();
                    }
                } finally {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                }
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.SecretAuthOrBuilder
            public int getIndex() {
                return this.index_;
            }

            public Builder setIndex(int value) {
                this.index_ = value;
                onChanged();
                return this;
            }

            public Builder clearIndex() {
                this.index_ = 0;
                onChanged();
                return this;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.SecretAuthOrBuilder
            public int getCont() {
                return this.cont_;
            }

            public Builder setCont(int value) {
                this.cont_ = value;
                onChanged();
                return this;
            }

            public Builder clearCont() {
                this.cont_ = 0;
                onChanged();
                return this;
            }

            private void ensureResultIsMutable() {
                if ((this.bitField0_ & 1) == 0) {
                    this.result_ = new LazyStringArrayList(this.result_);
                    this.bitField0_ |= 1;
                }
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.SecretAuthOrBuilder
            public ProtocolStringList getResultList() {
                return this.result_.getUnmodifiableView();
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.SecretAuthOrBuilder
            public int getResultCount() {
                return this.result_.size();
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.SecretAuthOrBuilder
            public String getResult(int index) {
                return (String) this.result_.get(index);
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.SecretAuthOrBuilder
            public ByteString getResultBytes(int index) {
                return this.result_.getByteString(index);
            }

            public Builder setResult(int index, String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureResultIsMutable();
                this.result_.set(index, value);
                onChanged();
                return this;
            }

            public Builder addResult(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureResultIsMutable();
                this.result_.add(value);
                onChanged();
                return this;
            }

            public Builder addAllResult(Iterable<String> values) {
                ensureResultIsMutable();
                AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.result_);
                onChanged();
                return this;
            }

            public Builder clearResult() {
                this.result_ = LazyStringArrayList.EMPTY;
                this.bitField0_ &= -2;
                onChanged();
                return this;
            }

            public Builder addResultBytes(ByteString value) {
                if (value != null) {
                    SecretAuth.checkByteStringIsUtf8(value);
                    ensureResultIsMutable();
                    this.result_.add(value);
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public final Builder setUnknownFields(final UnknownFieldSet unknownFields) {
                return (Builder) super.setUnknownFields(unknownFields);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public final Builder mergeUnknownFields(final UnknownFieldSet unknownFields) {
                return (Builder) super.mergeUnknownFields(unknownFields);
            }
        }

        public static SecretAuth getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SecretAuth> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<SecretAuth> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public SecretAuth getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    /* loaded from: classes2.dex */
    public static final class SecretPresetResponse extends GeneratedMessageV3 implements SecretPresetResponseOrBuilder {
        public static final int INDEX_FIELD_NUMBER = 1;
        private static final long serialVersionUID = 0;
        private int index_;
        private byte memoizedIsInitialized;
        private static final SecretPresetResponse DEFAULT_INSTANCE = new SecretPresetResponse();
        private static final Parser<SecretPresetResponse> PARSER = new AbstractParser<SecretPresetResponse>() { // from class: com.xpeng.upso.proxy.PSOProtocol.SecretPresetResponse.1
            @Override // com.google.protobuf.Parser
            public SecretPresetResponse parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new SecretPresetResponse(input, extensionRegistry);
            }
        };

        private SecretPresetResponse(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private SecretPresetResponse() {
            this.memoizedIsInitialized = (byte) -1;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
            return new SecretPresetResponse();
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private SecretPresetResponse(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistry == null) {
                throw new NullPointerException();
            }
            UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
            boolean done = false;
            while (!done) {
                try {
                    try {
                        try {
                            int tag = input.readTag();
                            if (tag == 0) {
                                done = true;
                            } else if (tag == 8) {
                                this.index_ = input.readUInt32();
                            } else if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                                done = true;
                            }
                        } catch (InvalidProtocolBufferException e) {
                            throw e.setUnfinishedMessage(this);
                        }
                    } catch (UninitializedMessageException e2) {
                        throw e2.asInvalidProtocolBufferException().setUnfinishedMessage(this);
                    } catch (IOException e3) {
                        throw new InvalidProtocolBufferException(e3).setUnfinishedMessage(this);
                    }
                } finally {
                    this.unknownFields = unknownFields.build();
                    makeExtensionsImmutable();
                }
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return PSOProtocol.internal_static_protocol_SecretPresetResponse_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return PSOProtocol.internal_static_protocol_SecretPresetResponse_fieldAccessorTable.ensureFieldAccessorsInitialized(SecretPresetResponse.class, Builder.class);
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.SecretPresetResponseOrBuilder
        public int getIndex() {
            return this.index_;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized == 1) {
                return true;
            }
            if (isInitialized == 0) {
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public void writeTo(CodedOutputStream output) throws IOException {
            int i = this.index_;
            if (i != 0) {
                output.writeUInt32(1, i);
            }
            this.unknownFields.writeTo(output);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSize;
            if (size != -1) {
                return size;
            }
            int i = this.index_;
            int size2 = (i != 0 ? 0 + CodedOutputStream.computeUInt32Size(1, i) : 0) + this.unknownFields.getSerializedSize();
            this.memoizedSize = size2;
            return size2;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof SecretPresetResponse)) {
                return super.equals(obj);
            }
            SecretPresetResponse other = (SecretPresetResponse) obj;
            return getIndex() == other.getIndex() && this.unknownFields.equals(other.unknownFields);
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = (((((((41 * 19) + getDescriptor().hashCode()) * 37) + 1) * 53) + getIndex()) * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash;
            return hash;
        }

        public static SecretPresetResponse parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static SecretPresetResponse parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static SecretPresetResponse parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static SecretPresetResponse parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static SecretPresetResponse parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static SecretPresetResponse parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static SecretPresetResponse parseFrom(InputStream input) throws IOException {
            return (SecretPresetResponse) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static SecretPresetResponse parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (SecretPresetResponse) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        public static SecretPresetResponse parseDelimitedFrom(InputStream input) throws IOException {
            return (SecretPresetResponse) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
        }

        public static SecretPresetResponse parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (SecretPresetResponse) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
        }

        public static SecretPresetResponse parseFrom(CodedInputStream input) throws IOException {
            return (SecretPresetResponse) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static SecretPresetResponse parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (SecretPresetResponse) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(SecretPresetResponse prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
            Builder builder = new Builder(parent);
            return builder;
        }

        /* loaded from: classes2.dex */
        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements SecretPresetResponseOrBuilder {
            private int index_;

            public static final Descriptors.Descriptor getDescriptor() {
                return PSOProtocol.internal_static_protocol_SecretPresetResponse_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return PSOProtocol.internal_static_protocol_SecretPresetResponse_fieldAccessorTable.ensureFieldAccessorsInitialized(SecretPresetResponse.class, Builder.class);
            }

            private Builder() {
                maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessageV3.BuilderParent parent) {
                super(parent);
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = SecretPresetResponse.alwaysUseFieldBuilders;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.index_ = 0;
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return PSOProtocol.internal_static_protocol_SecretPresetResponse_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public SecretPresetResponse getDefaultInstanceForType() {
                return SecretPresetResponse.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public SecretPresetResponse build() {
                SecretPresetResponse result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException((Message) result);
                }
                return result;
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public SecretPresetResponse buildPartial() {
                SecretPresetResponse result = new SecretPresetResponse(this);
                result.index_ = this.index_;
                onBuilt();
                return result;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder
            /* renamed from: clone */
            public Builder mo93clone() {
                return (Builder) super.mo93clone();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setField(Descriptors.FieldDescriptor field, Object value) {
                return (Builder) super.setField(field, value);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder clearField(Descriptors.FieldDescriptor field) {
                return (Builder) super.clearField(field);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder clearOneof(Descriptors.OneofDescriptor oneof) {
                return (Builder) super.clearOneof(oneof);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
                return (Builder) super.setRepeatedField(field, index, value);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
                return (Builder) super.addRepeatedField(field, value);
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(Message other) {
                if (other instanceof SecretPresetResponse) {
                    return mergeFrom((SecretPresetResponse) other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(SecretPresetResponse other) {
                if (other == SecretPresetResponse.getDefaultInstance()) {
                    return this;
                }
                if (other.getIndex() != 0) {
                    setIndex(other.getIndex());
                }
                mergeUnknownFields(other.unknownFields);
                onChanged();
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                SecretPresetResponse parsedMessage = null;
                try {
                    try {
                        parsedMessage = (SecretPresetResponse) SecretPresetResponse.PARSER.parsePartialFrom(input, extensionRegistry);
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        SecretPresetResponse secretPresetResponse = (SecretPresetResponse) e.getUnfinishedMessage();
                        throw e.unwrapIOException();
                    }
                } finally {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                }
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.SecretPresetResponseOrBuilder
            public int getIndex() {
                return this.index_;
            }

            public Builder setIndex(int value) {
                this.index_ = value;
                onChanged();
                return this;
            }

            public Builder clearIndex() {
                this.index_ = 0;
                onChanged();
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public final Builder setUnknownFields(final UnknownFieldSet unknownFields) {
                return (Builder) super.setUnknownFields(unknownFields);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public final Builder mergeUnknownFields(final UnknownFieldSet unknownFields) {
                return (Builder) super.mergeUnknownFields(unknownFields);
            }
        }

        public static SecretPresetResponse getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SecretPresetResponse> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<SecretPresetResponse> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public SecretPresetResponse getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    /* loaded from: classes2.dex */
    public static final class SecretDelete extends GeneratedMessageV3 implements SecretDeleteOrBuilder {
        public static final int INDEX_FIELD_NUMBER = 1;
        private static final long serialVersionUID = 0;
        private int index_;
        private byte memoizedIsInitialized;
        private static final SecretDelete DEFAULT_INSTANCE = new SecretDelete();
        private static final Parser<SecretDelete> PARSER = new AbstractParser<SecretDelete>() { // from class: com.xpeng.upso.proxy.PSOProtocol.SecretDelete.1
            @Override // com.google.protobuf.Parser
            public SecretDelete parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new SecretDelete(input, extensionRegistry);
            }
        };

        private SecretDelete(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private SecretDelete() {
            this.memoizedIsInitialized = (byte) -1;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
            return new SecretDelete();
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private SecretDelete(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistry == null) {
                throw new NullPointerException();
            }
            UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
            boolean done = false;
            while (!done) {
                try {
                    try {
                        try {
                            int tag = input.readTag();
                            if (tag == 0) {
                                done = true;
                            } else if (tag == 8) {
                                this.index_ = input.readUInt32();
                            } else if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                                done = true;
                            }
                        } catch (InvalidProtocolBufferException e) {
                            throw e.setUnfinishedMessage(this);
                        }
                    } catch (UninitializedMessageException e2) {
                        throw e2.asInvalidProtocolBufferException().setUnfinishedMessage(this);
                    } catch (IOException e3) {
                        throw new InvalidProtocolBufferException(e3).setUnfinishedMessage(this);
                    }
                } finally {
                    this.unknownFields = unknownFields.build();
                    makeExtensionsImmutable();
                }
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return PSOProtocol.internal_static_protocol_SecretDelete_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return PSOProtocol.internal_static_protocol_SecretDelete_fieldAccessorTable.ensureFieldAccessorsInitialized(SecretDelete.class, Builder.class);
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.SecretDeleteOrBuilder
        public int getIndex() {
            return this.index_;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized == 1) {
                return true;
            }
            if (isInitialized == 0) {
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public void writeTo(CodedOutputStream output) throws IOException {
            int i = this.index_;
            if (i != 0) {
                output.writeUInt32(1, i);
            }
            this.unknownFields.writeTo(output);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSize;
            if (size != -1) {
                return size;
            }
            int i = this.index_;
            int size2 = (i != 0 ? 0 + CodedOutputStream.computeUInt32Size(1, i) : 0) + this.unknownFields.getSerializedSize();
            this.memoizedSize = size2;
            return size2;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof SecretDelete)) {
                return super.equals(obj);
            }
            SecretDelete other = (SecretDelete) obj;
            return getIndex() == other.getIndex() && this.unknownFields.equals(other.unknownFields);
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = (((((((41 * 19) + getDescriptor().hashCode()) * 37) + 1) * 53) + getIndex()) * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash;
            return hash;
        }

        public static SecretDelete parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static SecretDelete parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static SecretDelete parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static SecretDelete parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static SecretDelete parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static SecretDelete parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static SecretDelete parseFrom(InputStream input) throws IOException {
            return (SecretDelete) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static SecretDelete parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (SecretDelete) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        public static SecretDelete parseDelimitedFrom(InputStream input) throws IOException {
            return (SecretDelete) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
        }

        public static SecretDelete parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (SecretDelete) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
        }

        public static SecretDelete parseFrom(CodedInputStream input) throws IOException {
            return (SecretDelete) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static SecretDelete parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (SecretDelete) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(SecretDelete prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
            Builder builder = new Builder(parent);
            return builder;
        }

        /* loaded from: classes2.dex */
        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements SecretDeleteOrBuilder {
            private int index_;

            public static final Descriptors.Descriptor getDescriptor() {
                return PSOProtocol.internal_static_protocol_SecretDelete_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return PSOProtocol.internal_static_protocol_SecretDelete_fieldAccessorTable.ensureFieldAccessorsInitialized(SecretDelete.class, Builder.class);
            }

            private Builder() {
                maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessageV3.BuilderParent parent) {
                super(parent);
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = SecretDelete.alwaysUseFieldBuilders;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.index_ = 0;
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return PSOProtocol.internal_static_protocol_SecretDelete_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public SecretDelete getDefaultInstanceForType() {
                return SecretDelete.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public SecretDelete build() {
                SecretDelete result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException((Message) result);
                }
                return result;
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public SecretDelete buildPartial() {
                SecretDelete result = new SecretDelete(this);
                result.index_ = this.index_;
                onBuilt();
                return result;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder
            /* renamed from: clone */
            public Builder mo93clone() {
                return (Builder) super.mo93clone();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setField(Descriptors.FieldDescriptor field, Object value) {
                return (Builder) super.setField(field, value);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder clearField(Descriptors.FieldDescriptor field) {
                return (Builder) super.clearField(field);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder clearOneof(Descriptors.OneofDescriptor oneof) {
                return (Builder) super.clearOneof(oneof);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
                return (Builder) super.setRepeatedField(field, index, value);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
                return (Builder) super.addRepeatedField(field, value);
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(Message other) {
                if (other instanceof SecretDelete) {
                    return mergeFrom((SecretDelete) other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(SecretDelete other) {
                if (other == SecretDelete.getDefaultInstance()) {
                    return this;
                }
                if (other.getIndex() != 0) {
                    setIndex(other.getIndex());
                }
                mergeUnknownFields(other.unknownFields);
                onChanged();
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                SecretDelete parsedMessage = null;
                try {
                    try {
                        parsedMessage = (SecretDelete) SecretDelete.PARSER.parsePartialFrom(input, extensionRegistry);
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        SecretDelete secretDelete = (SecretDelete) e.getUnfinishedMessage();
                        throw e.unwrapIOException();
                    }
                } finally {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                }
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.SecretDeleteOrBuilder
            public int getIndex() {
                return this.index_;
            }

            public Builder setIndex(int value) {
                this.index_ = value;
                onChanged();
                return this;
            }

            public Builder clearIndex() {
                this.index_ = 0;
                onChanged();
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public final Builder setUnknownFields(final UnknownFieldSet unknownFields) {
                return (Builder) super.setUnknownFields(unknownFields);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public final Builder mergeUnknownFields(final UnknownFieldSet unknownFields) {
                return (Builder) super.mergeUnknownFields(unknownFields);
            }
        }

        public static SecretDelete getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SecretDelete> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<SecretDelete> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public SecretDelete getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    /* loaded from: classes2.dex */
    public static final class Secret extends GeneratedMessageV3 implements SecretOrBuilder {
        public static final int CREATE_TIME_FIELD_NUMBER = 4;
        public static final int INDEX_FIELD_NUMBER = 3;
        public static final int NAME_FIELD_NUMBER = 2;
        public static final int SECRET_FIELD_NUMBER = 5;
        public static final int SIGN_FIELD_NUMBER = 6;
        public static final int TYPE_FIELD_NUMBER = 1;
        private static final long serialVersionUID = 0;
        private long createTime_;
        private long index_;
        private byte memoizedIsInitialized;
        private volatile Object name_;
        private volatile Object secret_;
        private volatile Object sign_;
        private int type_;
        private static final Secret DEFAULT_INSTANCE = new Secret();
        private static final Parser<Secret> PARSER = new AbstractParser<Secret>() { // from class: com.xpeng.upso.proxy.PSOProtocol.Secret.1
            @Override // com.google.protobuf.Parser
            public Secret parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new Secret(input, extensionRegistry);
            }
        };

        private Secret(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private Secret() {
            this.memoizedIsInitialized = (byte) -1;
            this.type_ = 0;
            this.name_ = "";
            this.secret_ = "";
            this.sign_ = "";
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
            return new Secret();
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private Secret(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistry == null) {
                throw new NullPointerException();
            }
            UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
            boolean done = false;
            while (!done) {
                try {
                    try {
                        int tag = input.readTag();
                        if (tag == 0) {
                            done = true;
                        } else if (tag == 8) {
                            int rawValue = input.readEnum();
                            this.type_ = rawValue;
                        } else if (tag == 18) {
                            String s = input.readStringRequireUtf8();
                            this.name_ = s;
                        } else if (tag == 24) {
                            this.index_ = input.readInt64();
                        } else if (tag == 32) {
                            this.createTime_ = input.readInt64();
                        } else if (tag == 42) {
                            String s2 = input.readStringRequireUtf8();
                            this.secret_ = s2;
                        } else if (tag == 50) {
                            String s3 = input.readStringRequireUtf8();
                            this.sign_ = s3;
                        } else if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                            done = true;
                        }
                    } catch (InvalidProtocolBufferException e) {
                        throw e.setUnfinishedMessage(this);
                    } catch (UninitializedMessageException e2) {
                        throw e2.asInvalidProtocolBufferException().setUnfinishedMessage(this);
                    } catch (IOException e3) {
                        throw new InvalidProtocolBufferException(e3).setUnfinishedMessage(this);
                    }
                } finally {
                    this.unknownFields = unknownFields.build();
                    makeExtensionsImmutable();
                }
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return PSOProtocol.internal_static_protocol_Secret_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return PSOProtocol.internal_static_protocol_Secret_fieldAccessorTable.ensureFieldAccessorsInitialized(Secret.class, Builder.class);
        }

        /* loaded from: classes2.dex */
        public enum SecretType implements ProtocolMessageEnum {
            KEY_TYPE_AES(0),
            KEY_TYPE_CERTIFICATE(1),
            KEY_TYPE_TBD(2),
            UNRECOGNIZED(-1);
            
            public static final int KEY_TYPE_AES_VALUE = 0;
            public static final int KEY_TYPE_CERTIFICATE_VALUE = 1;
            public static final int KEY_TYPE_TBD_VALUE = 2;
            private final int value;
            private static final Internal.EnumLiteMap<SecretType> internalValueMap = new Internal.EnumLiteMap<SecretType>() { // from class: com.xpeng.upso.proxy.PSOProtocol.Secret.SecretType.1
                @Override // com.google.protobuf.Internal.EnumLiteMap
                public SecretType findValueByNumber(int number) {
                    return SecretType.forNumber(number);
                }
            };
            private static final SecretType[] VALUES = values();

            @Override // com.google.protobuf.ProtocolMessageEnum, com.google.protobuf.Internal.EnumLite
            public final int getNumber() {
                if (this == UNRECOGNIZED) {
                    throw new IllegalArgumentException("Can't get the number of an unknown enum value.");
                }
                return this.value;
            }

            @Deprecated
            public static SecretType valueOf(int value) {
                return forNumber(value);
            }

            public static SecretType forNumber(int value) {
                if (value != 0) {
                    if (value != 1) {
                        if (value == 2) {
                            return KEY_TYPE_TBD;
                        }
                        return null;
                    }
                    return KEY_TYPE_CERTIFICATE;
                }
                return KEY_TYPE_AES;
            }

            public static Internal.EnumLiteMap<SecretType> internalGetValueMap() {
                return internalValueMap;
            }

            @Override // com.google.protobuf.ProtocolMessageEnum
            public final Descriptors.EnumValueDescriptor getValueDescriptor() {
                if (this == UNRECOGNIZED) {
                    throw new IllegalStateException("Can't get the descriptor of an unrecognized enum value.");
                }
                return getDescriptor().getValues().get(ordinal());
            }

            @Override // com.google.protobuf.ProtocolMessageEnum
            public final Descriptors.EnumDescriptor getDescriptorForType() {
                return getDescriptor();
            }

            public static final Descriptors.EnumDescriptor getDescriptor() {
                return Secret.getDescriptor().getEnumTypes().get(0);
            }

            public static SecretType valueOf(Descriptors.EnumValueDescriptor desc) {
                if (desc.getType() != getDescriptor()) {
                    throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
                }
                if (desc.getIndex() == -1) {
                    return UNRECOGNIZED;
                }
                return VALUES[desc.getIndex()];
            }

            SecretType(int value) {
                this.value = value;
            }
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.SecretOrBuilder
        public int getTypeValue() {
            return this.type_;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.SecretOrBuilder
        public SecretType getType() {
            SecretType result = SecretType.valueOf(this.type_);
            return result == null ? SecretType.UNRECOGNIZED : result;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.SecretOrBuilder
        public String getName() {
            Object ref = this.name_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            this.name_ = s;
            return s;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.SecretOrBuilder
        public ByteString getNameBytes() {
            Object ref = this.name_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.name_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.SecretOrBuilder
        public long getIndex() {
            return this.index_;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.SecretOrBuilder
        public long getCreateTime() {
            return this.createTime_;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.SecretOrBuilder
        public String getSecret() {
            Object ref = this.secret_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            this.secret_ = s;
            return s;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.SecretOrBuilder
        public ByteString getSecretBytes() {
            Object ref = this.secret_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.secret_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.SecretOrBuilder
        public String getSign() {
            Object ref = this.sign_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            this.sign_ = s;
            return s;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.SecretOrBuilder
        public ByteString getSignBytes() {
            Object ref = this.sign_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.sign_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized == 1) {
                return true;
            }
            if (isInitialized == 0) {
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public void writeTo(CodedOutputStream output) throws IOException {
            if (this.type_ != SecretType.KEY_TYPE_AES.getNumber()) {
                output.writeEnum(1, this.type_);
            }
            if (!GeneratedMessageV3.isStringEmpty(this.name_)) {
                GeneratedMessageV3.writeString(output, 2, this.name_);
            }
            long j = this.index_;
            if (j != 0) {
                output.writeInt64(3, j);
            }
            long j2 = this.createTime_;
            if (j2 != 0) {
                output.writeInt64(4, j2);
            }
            if (!GeneratedMessageV3.isStringEmpty(this.secret_)) {
                GeneratedMessageV3.writeString(output, 5, this.secret_);
            }
            if (!GeneratedMessageV3.isStringEmpty(this.sign_)) {
                GeneratedMessageV3.writeString(output, 6, this.sign_);
            }
            this.unknownFields.writeTo(output);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSize;
            if (size != -1) {
                return size;
            }
            int size2 = this.type_ != SecretType.KEY_TYPE_AES.getNumber() ? 0 + CodedOutputStream.computeEnumSize(1, this.type_) : 0;
            if (!GeneratedMessageV3.isStringEmpty(this.name_)) {
                size2 += GeneratedMessageV3.computeStringSize(2, this.name_);
            }
            long j = this.index_;
            if (j != 0) {
                size2 += CodedOutputStream.computeInt64Size(3, j);
            }
            long j2 = this.createTime_;
            if (j2 != 0) {
                size2 += CodedOutputStream.computeInt64Size(4, j2);
            }
            if (!GeneratedMessageV3.isStringEmpty(this.secret_)) {
                size2 += GeneratedMessageV3.computeStringSize(5, this.secret_);
            }
            if (!GeneratedMessageV3.isStringEmpty(this.sign_)) {
                size2 += GeneratedMessageV3.computeStringSize(6, this.sign_);
            }
            int size3 = size2 + this.unknownFields.getSerializedSize();
            this.memoizedSize = size3;
            return size3;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Secret)) {
                return super.equals(obj);
            }
            Secret other = (Secret) obj;
            return this.type_ == other.type_ && getName().equals(other.getName()) && getIndex() == other.getIndex() && getCreateTime() == other.getCreateTime() && getSecret().equals(other.getSecret()) && getSign().equals(other.getSign()) && this.unknownFields.equals(other.unknownFields);
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = (((((((((((((((((((((((((((41 * 19) + getDescriptor().hashCode()) * 37) + 1) * 53) + this.type_) * 37) + 2) * 53) + getName().hashCode()) * 37) + 3) * 53) + Internal.hashLong(getIndex())) * 37) + 4) * 53) + Internal.hashLong(getCreateTime())) * 37) + 5) * 53) + getSecret().hashCode()) * 37) + 6) * 53) + getSign().hashCode()) * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash;
            return hash;
        }

        public static Secret parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Secret parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Secret parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Secret parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Secret parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Secret parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Secret parseFrom(InputStream input) throws IOException {
            return (Secret) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static Secret parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (Secret) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        public static Secret parseDelimitedFrom(InputStream input) throws IOException {
            return (Secret) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
        }

        public static Secret parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (Secret) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
        }

        public static Secret parseFrom(CodedInputStream input) throws IOException {
            return (Secret) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static Secret parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (Secret) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(Secret prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
            Builder builder = new Builder(parent);
            return builder;
        }

        /* loaded from: classes2.dex */
        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements SecretOrBuilder {
            private long createTime_;
            private long index_;
            private Object name_;
            private Object secret_;
            private Object sign_;
            private int type_;

            public static final Descriptors.Descriptor getDescriptor() {
                return PSOProtocol.internal_static_protocol_Secret_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return PSOProtocol.internal_static_protocol_Secret_fieldAccessorTable.ensureFieldAccessorsInitialized(Secret.class, Builder.class);
            }

            private Builder() {
                this.type_ = 0;
                this.name_ = "";
                this.secret_ = "";
                this.sign_ = "";
                maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessageV3.BuilderParent parent) {
                super(parent);
                this.type_ = 0;
                this.name_ = "";
                this.secret_ = "";
                this.sign_ = "";
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = Secret.alwaysUseFieldBuilders;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.type_ = 0;
                this.name_ = "";
                this.index_ = 0L;
                this.createTime_ = 0L;
                this.secret_ = "";
                this.sign_ = "";
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return PSOProtocol.internal_static_protocol_Secret_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public Secret getDefaultInstanceForType() {
                return Secret.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Secret build() {
                Secret result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException((Message) result);
                }
                return result;
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Secret buildPartial() {
                Secret result = new Secret(this);
                result.type_ = this.type_;
                result.name_ = this.name_;
                result.index_ = this.index_;
                result.createTime_ = this.createTime_;
                result.secret_ = this.secret_;
                result.sign_ = this.sign_;
                onBuilt();
                return result;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder
            /* renamed from: clone */
            public Builder mo93clone() {
                return (Builder) super.mo93clone();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setField(Descriptors.FieldDescriptor field, Object value) {
                return (Builder) super.setField(field, value);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder clearField(Descriptors.FieldDescriptor field) {
                return (Builder) super.clearField(field);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder clearOneof(Descriptors.OneofDescriptor oneof) {
                return (Builder) super.clearOneof(oneof);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
                return (Builder) super.setRepeatedField(field, index, value);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
                return (Builder) super.addRepeatedField(field, value);
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(Message other) {
                if (other instanceof Secret) {
                    return mergeFrom((Secret) other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(Secret other) {
                if (other == Secret.getDefaultInstance()) {
                    return this;
                }
                if (other.type_ != 0) {
                    setTypeValue(other.getTypeValue());
                }
                if (!other.getName().isEmpty()) {
                    this.name_ = other.name_;
                    onChanged();
                }
                if (other.getIndex() != 0) {
                    setIndex(other.getIndex());
                }
                if (other.getCreateTime() != 0) {
                    setCreateTime(other.getCreateTime());
                }
                if (!other.getSecret().isEmpty()) {
                    this.secret_ = other.secret_;
                    onChanged();
                }
                if (!other.getSign().isEmpty()) {
                    this.sign_ = other.sign_;
                    onChanged();
                }
                mergeUnknownFields(other.unknownFields);
                onChanged();
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                Secret parsedMessage = null;
                try {
                    try {
                        parsedMessage = (Secret) Secret.PARSER.parsePartialFrom(input, extensionRegistry);
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        Secret secret = (Secret) e.getUnfinishedMessage();
                        throw e.unwrapIOException();
                    }
                } finally {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                }
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.SecretOrBuilder
            public int getTypeValue() {
                return this.type_;
            }

            public Builder setTypeValue(int value) {
                this.type_ = value;
                onChanged();
                return this;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.SecretOrBuilder
            public SecretType getType() {
                SecretType result = SecretType.valueOf(this.type_);
                return result == null ? SecretType.UNRECOGNIZED : result;
            }

            public Builder setType(SecretType value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.type_ = value.getNumber();
                onChanged();
                return this;
            }

            public Builder clearType() {
                this.type_ = 0;
                onChanged();
                return this;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.SecretOrBuilder
            public String getName() {
                Object ref = this.name_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    this.name_ = s;
                    return s;
                }
                return (String) ref;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.SecretOrBuilder
            public ByteString getNameBytes() {
                Object ref = this.name_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.name_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            public Builder setName(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.name_ = value;
                onChanged();
                return this;
            }

            public Builder clearName() {
                this.name_ = Secret.getDefaultInstance().getName();
                onChanged();
                return this;
            }

            public Builder setNameBytes(ByteString value) {
                if (value != null) {
                    Secret.checkByteStringIsUtf8(value);
                    this.name_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.SecretOrBuilder
            public long getIndex() {
                return this.index_;
            }

            public Builder setIndex(long value) {
                this.index_ = value;
                onChanged();
                return this;
            }

            public Builder clearIndex() {
                this.index_ = 0L;
                onChanged();
                return this;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.SecretOrBuilder
            public long getCreateTime() {
                return this.createTime_;
            }

            public Builder setCreateTime(long value) {
                this.createTime_ = value;
                onChanged();
                return this;
            }

            public Builder clearCreateTime() {
                this.createTime_ = 0L;
                onChanged();
                return this;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.SecretOrBuilder
            public String getSecret() {
                Object ref = this.secret_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    this.secret_ = s;
                    return s;
                }
                return (String) ref;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.SecretOrBuilder
            public ByteString getSecretBytes() {
                Object ref = this.secret_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.secret_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            public Builder setSecret(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.secret_ = value;
                onChanged();
                return this;
            }

            public Builder clearSecret() {
                this.secret_ = Secret.getDefaultInstance().getSecret();
                onChanged();
                return this;
            }

            public Builder setSecretBytes(ByteString value) {
                if (value != null) {
                    Secret.checkByteStringIsUtf8(value);
                    this.secret_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.SecretOrBuilder
            public String getSign() {
                Object ref = this.sign_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    this.sign_ = s;
                    return s;
                }
                return (String) ref;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.SecretOrBuilder
            public ByteString getSignBytes() {
                Object ref = this.sign_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.sign_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            public Builder setSign(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.sign_ = value;
                onChanged();
                return this;
            }

            public Builder clearSign() {
                this.sign_ = Secret.getDefaultInstance().getSign();
                onChanged();
                return this;
            }

            public Builder setSignBytes(ByteString value) {
                if (value != null) {
                    Secret.checkByteStringIsUtf8(value);
                    this.sign_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public final Builder setUnknownFields(final UnknownFieldSet unknownFields) {
                return (Builder) super.setUnknownFields(unknownFields);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public final Builder mergeUnknownFields(final UnknownFieldSet unknownFields) {
                return (Builder) super.mergeUnknownFields(unknownFields);
            }
        }

        public static Secret getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<Secret> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<Secret> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public Secret getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    /* loaded from: classes2.dex */
    public static final class SecretPreset extends GeneratedMessageV3 implements SecretPresetOrBuilder {
        public static final int CONT_FIELD_NUMBER = 2;
        public static final int INDEX_FIELD_NUMBER = 1;
        public static final int SECRET_FIELD_NUMBER = 3;
        private static final long serialVersionUID = 0;
        private int cont_;
        private int index_;
        private byte memoizedIsInitialized;
        private List<Secret> secret_;
        private static final SecretPreset DEFAULT_INSTANCE = new SecretPreset();
        private static final Parser<SecretPreset> PARSER = new AbstractParser<SecretPreset>() { // from class: com.xpeng.upso.proxy.PSOProtocol.SecretPreset.1
            @Override // com.google.protobuf.Parser
            public SecretPreset parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new SecretPreset(input, extensionRegistry);
            }
        };

        private SecretPreset(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private SecretPreset() {
            this.memoizedIsInitialized = (byte) -1;
            this.secret_ = Collections.emptyList();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
            return new SecretPreset();
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private SecretPreset(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistry == null) {
                throw new NullPointerException();
            }
            int mutable_bitField0_ = 0;
            UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
            boolean done = false;
            while (!done) {
                try {
                    try {
                        try {
                            int tag = input.readTag();
                            if (tag == 0) {
                                done = true;
                            } else if (tag == 8) {
                                this.index_ = input.readUInt32();
                            } else if (tag == 16) {
                                this.cont_ = input.readUInt32();
                            } else if (tag == 26) {
                                if ((mutable_bitField0_ & 1) == 0) {
                                    this.secret_ = new ArrayList();
                                    mutable_bitField0_ |= 1;
                                }
                                this.secret_.add((Secret) input.readMessage(Secret.parser(), extensionRegistry));
                            } else if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                                done = true;
                            }
                        } catch (InvalidProtocolBufferException e) {
                            throw e.setUnfinishedMessage(this);
                        }
                    } catch (UninitializedMessageException e2) {
                        throw e2.asInvalidProtocolBufferException().setUnfinishedMessage(this);
                    } catch (IOException e3) {
                        throw new InvalidProtocolBufferException(e3).setUnfinishedMessage(this);
                    }
                } finally {
                    if ((mutable_bitField0_ & 1) != 0) {
                        this.secret_ = Collections.unmodifiableList(this.secret_);
                    }
                    this.unknownFields = unknownFields.build();
                    makeExtensionsImmutable();
                }
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return PSOProtocol.internal_static_protocol_SecretPreset_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return PSOProtocol.internal_static_protocol_SecretPreset_fieldAccessorTable.ensureFieldAccessorsInitialized(SecretPreset.class, Builder.class);
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.SecretPresetOrBuilder
        public int getIndex() {
            return this.index_;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.SecretPresetOrBuilder
        public int getCont() {
            return this.cont_;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.SecretPresetOrBuilder
        public List<Secret> getSecretList() {
            return this.secret_;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.SecretPresetOrBuilder
        public List<? extends SecretOrBuilder> getSecretOrBuilderList() {
            return this.secret_;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.SecretPresetOrBuilder
        public int getSecretCount() {
            return this.secret_.size();
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.SecretPresetOrBuilder
        public Secret getSecret(int index) {
            return this.secret_.get(index);
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.SecretPresetOrBuilder
        public SecretOrBuilder getSecretOrBuilder(int index) {
            return this.secret_.get(index);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized == 1) {
                return true;
            }
            if (isInitialized == 0) {
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public void writeTo(CodedOutputStream output) throws IOException {
            int i = this.index_;
            if (i != 0) {
                output.writeUInt32(1, i);
            }
            int i2 = this.cont_;
            if (i2 != 0) {
                output.writeUInt32(2, i2);
            }
            for (int i3 = 0; i3 < this.secret_.size(); i3++) {
                output.writeMessage(3, this.secret_.get(i3));
            }
            this.unknownFields.writeTo(output);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSize;
            if (size != -1) {
                return size;
            }
            int i = this.index_;
            int size2 = i != 0 ? 0 + CodedOutputStream.computeUInt32Size(1, i) : 0;
            int i2 = this.cont_;
            if (i2 != 0) {
                size2 += CodedOutputStream.computeUInt32Size(2, i2);
            }
            for (int i3 = 0; i3 < this.secret_.size(); i3++) {
                size2 += CodedOutputStream.computeMessageSize(3, this.secret_.get(i3));
            }
            int size3 = size2 + this.unknownFields.getSerializedSize();
            this.memoizedSize = size3;
            return size3;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof SecretPreset)) {
                return super.equals(obj);
            }
            SecretPreset other = (SecretPreset) obj;
            return getIndex() == other.getIndex() && getCont() == other.getCont() && getSecretList().equals(other.getSecretList()) && this.unknownFields.equals(other.unknownFields);
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = (((((((((41 * 19) + getDescriptor().hashCode()) * 37) + 1) * 53) + getIndex()) * 37) + 2) * 53) + getCont();
            if (getSecretCount() > 0) {
                hash = (((hash * 37) + 3) * 53) + getSecretList().hashCode();
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        public static SecretPreset parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static SecretPreset parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static SecretPreset parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static SecretPreset parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static SecretPreset parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static SecretPreset parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static SecretPreset parseFrom(InputStream input) throws IOException {
            return (SecretPreset) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static SecretPreset parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (SecretPreset) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        public static SecretPreset parseDelimitedFrom(InputStream input) throws IOException {
            return (SecretPreset) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
        }

        public static SecretPreset parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (SecretPreset) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
        }

        public static SecretPreset parseFrom(CodedInputStream input) throws IOException {
            return (SecretPreset) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static SecretPreset parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (SecretPreset) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(SecretPreset prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
            Builder builder = new Builder(parent);
            return builder;
        }

        /* loaded from: classes2.dex */
        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements SecretPresetOrBuilder {
            private int bitField0_;
            private int cont_;
            private int index_;
            private RepeatedFieldBuilderV3<Secret, Secret.Builder, SecretOrBuilder> secretBuilder_;
            private List<Secret> secret_;

            public static final Descriptors.Descriptor getDescriptor() {
                return PSOProtocol.internal_static_protocol_SecretPreset_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return PSOProtocol.internal_static_protocol_SecretPreset_fieldAccessorTable.ensureFieldAccessorsInitialized(SecretPreset.class, Builder.class);
            }

            private Builder() {
                this.secret_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessageV3.BuilderParent parent) {
                super(parent);
                this.secret_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (SecretPreset.alwaysUseFieldBuilders) {
                    getSecretFieldBuilder();
                }
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.index_ = 0;
                this.cont_ = 0;
                RepeatedFieldBuilderV3<Secret, Secret.Builder, SecretOrBuilder> repeatedFieldBuilderV3 = this.secretBuilder_;
                if (repeatedFieldBuilderV3 == null) {
                    this.secret_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                } else {
                    repeatedFieldBuilderV3.clear();
                }
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return PSOProtocol.internal_static_protocol_SecretPreset_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public SecretPreset getDefaultInstanceForType() {
                return SecretPreset.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public SecretPreset build() {
                SecretPreset result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException((Message) result);
                }
                return result;
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public SecretPreset buildPartial() {
                SecretPreset result = new SecretPreset(this);
                int i = this.bitField0_;
                result.index_ = this.index_;
                result.cont_ = this.cont_;
                RepeatedFieldBuilderV3<Secret, Secret.Builder, SecretOrBuilder> repeatedFieldBuilderV3 = this.secretBuilder_;
                if (repeatedFieldBuilderV3 != null) {
                    result.secret_ = repeatedFieldBuilderV3.build();
                } else {
                    if ((this.bitField0_ & 1) != 0) {
                        this.secret_ = Collections.unmodifiableList(this.secret_);
                        this.bitField0_ &= -2;
                    }
                    result.secret_ = this.secret_;
                }
                onBuilt();
                return result;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder
            /* renamed from: clone */
            public Builder mo93clone() {
                return (Builder) super.mo93clone();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setField(Descriptors.FieldDescriptor field, Object value) {
                return (Builder) super.setField(field, value);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder clearField(Descriptors.FieldDescriptor field) {
                return (Builder) super.clearField(field);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder clearOneof(Descriptors.OneofDescriptor oneof) {
                return (Builder) super.clearOneof(oneof);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
                return (Builder) super.setRepeatedField(field, index, value);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
                return (Builder) super.addRepeatedField(field, value);
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(Message other) {
                if (other instanceof SecretPreset) {
                    return mergeFrom((SecretPreset) other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(SecretPreset other) {
                if (other == SecretPreset.getDefaultInstance()) {
                    return this;
                }
                if (other.getIndex() != 0) {
                    setIndex(other.getIndex());
                }
                if (other.getCont() != 0) {
                    setCont(other.getCont());
                }
                if (this.secretBuilder_ == null) {
                    if (!other.secret_.isEmpty()) {
                        if (this.secret_.isEmpty()) {
                            this.secret_ = other.secret_;
                            this.bitField0_ &= -2;
                        } else {
                            ensureSecretIsMutable();
                            this.secret_.addAll(other.secret_);
                        }
                        onChanged();
                    }
                } else if (!other.secret_.isEmpty()) {
                    if (!this.secretBuilder_.isEmpty()) {
                        this.secretBuilder_.addAllMessages(other.secret_);
                    } else {
                        this.secretBuilder_.dispose();
                        RepeatedFieldBuilderV3<Secret, Secret.Builder, SecretOrBuilder> repeatedFieldBuilderV3 = null;
                        this.secretBuilder_ = null;
                        this.secret_ = other.secret_;
                        this.bitField0_ &= -2;
                        if (SecretPreset.alwaysUseFieldBuilders) {
                            repeatedFieldBuilderV3 = getSecretFieldBuilder();
                        }
                        this.secretBuilder_ = repeatedFieldBuilderV3;
                    }
                }
                mergeUnknownFields(other.unknownFields);
                onChanged();
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                SecretPreset parsedMessage = null;
                try {
                    try {
                        parsedMessage = (SecretPreset) SecretPreset.PARSER.parsePartialFrom(input, extensionRegistry);
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        SecretPreset secretPreset = (SecretPreset) e.getUnfinishedMessage();
                        throw e.unwrapIOException();
                    }
                } finally {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                }
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.SecretPresetOrBuilder
            public int getIndex() {
                return this.index_;
            }

            public Builder setIndex(int value) {
                this.index_ = value;
                onChanged();
                return this;
            }

            public Builder clearIndex() {
                this.index_ = 0;
                onChanged();
                return this;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.SecretPresetOrBuilder
            public int getCont() {
                return this.cont_;
            }

            public Builder setCont(int value) {
                this.cont_ = value;
                onChanged();
                return this;
            }

            public Builder clearCont() {
                this.cont_ = 0;
                onChanged();
                return this;
            }

            private void ensureSecretIsMutable() {
                if ((this.bitField0_ & 1) == 0) {
                    this.secret_ = new ArrayList(this.secret_);
                    this.bitField0_ |= 1;
                }
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.SecretPresetOrBuilder
            public List<Secret> getSecretList() {
                RepeatedFieldBuilderV3<Secret, Secret.Builder, SecretOrBuilder> repeatedFieldBuilderV3 = this.secretBuilder_;
                if (repeatedFieldBuilderV3 == null) {
                    return Collections.unmodifiableList(this.secret_);
                }
                return repeatedFieldBuilderV3.getMessageList();
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.SecretPresetOrBuilder
            public int getSecretCount() {
                RepeatedFieldBuilderV3<Secret, Secret.Builder, SecretOrBuilder> repeatedFieldBuilderV3 = this.secretBuilder_;
                if (repeatedFieldBuilderV3 == null) {
                    return this.secret_.size();
                }
                return repeatedFieldBuilderV3.getCount();
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.SecretPresetOrBuilder
            public Secret getSecret(int index) {
                RepeatedFieldBuilderV3<Secret, Secret.Builder, SecretOrBuilder> repeatedFieldBuilderV3 = this.secretBuilder_;
                if (repeatedFieldBuilderV3 == null) {
                    return this.secret_.get(index);
                }
                return repeatedFieldBuilderV3.getMessage(index);
            }

            public Builder setSecret(int index, Secret value) {
                RepeatedFieldBuilderV3<Secret, Secret.Builder, SecretOrBuilder> repeatedFieldBuilderV3 = this.secretBuilder_;
                if (repeatedFieldBuilderV3 == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureSecretIsMutable();
                    this.secret_.set(index, value);
                    onChanged();
                } else {
                    repeatedFieldBuilderV3.setMessage(index, value);
                }
                return this;
            }

            public Builder setSecret(int index, Secret.Builder builderForValue) {
                RepeatedFieldBuilderV3<Secret, Secret.Builder, SecretOrBuilder> repeatedFieldBuilderV3 = this.secretBuilder_;
                if (repeatedFieldBuilderV3 == null) {
                    ensureSecretIsMutable();
                    this.secret_.set(index, builderForValue.build());
                    onChanged();
                } else {
                    repeatedFieldBuilderV3.setMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addSecret(Secret value) {
                RepeatedFieldBuilderV3<Secret, Secret.Builder, SecretOrBuilder> repeatedFieldBuilderV3 = this.secretBuilder_;
                if (repeatedFieldBuilderV3 == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureSecretIsMutable();
                    this.secret_.add(value);
                    onChanged();
                } else {
                    repeatedFieldBuilderV3.addMessage(value);
                }
                return this;
            }

            public Builder addSecret(int index, Secret value) {
                RepeatedFieldBuilderV3<Secret, Secret.Builder, SecretOrBuilder> repeatedFieldBuilderV3 = this.secretBuilder_;
                if (repeatedFieldBuilderV3 == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureSecretIsMutable();
                    this.secret_.add(index, value);
                    onChanged();
                } else {
                    repeatedFieldBuilderV3.addMessage(index, value);
                }
                return this;
            }

            public Builder addSecret(Secret.Builder builderForValue) {
                RepeatedFieldBuilderV3<Secret, Secret.Builder, SecretOrBuilder> repeatedFieldBuilderV3 = this.secretBuilder_;
                if (repeatedFieldBuilderV3 == null) {
                    ensureSecretIsMutable();
                    this.secret_.add(builderForValue.build());
                    onChanged();
                } else {
                    repeatedFieldBuilderV3.addMessage(builderForValue.build());
                }
                return this;
            }

            public Builder addSecret(int index, Secret.Builder builderForValue) {
                RepeatedFieldBuilderV3<Secret, Secret.Builder, SecretOrBuilder> repeatedFieldBuilderV3 = this.secretBuilder_;
                if (repeatedFieldBuilderV3 == null) {
                    ensureSecretIsMutable();
                    this.secret_.add(index, builderForValue.build());
                    onChanged();
                } else {
                    repeatedFieldBuilderV3.addMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAllSecret(Iterable<? extends Secret> values) {
                RepeatedFieldBuilderV3<Secret, Secret.Builder, SecretOrBuilder> repeatedFieldBuilderV3 = this.secretBuilder_;
                if (repeatedFieldBuilderV3 == null) {
                    ensureSecretIsMutable();
                    AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.secret_);
                    onChanged();
                } else {
                    repeatedFieldBuilderV3.addAllMessages(values);
                }
                return this;
            }

            public Builder clearSecret() {
                RepeatedFieldBuilderV3<Secret, Secret.Builder, SecretOrBuilder> repeatedFieldBuilderV3 = this.secretBuilder_;
                if (repeatedFieldBuilderV3 == null) {
                    this.secret_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    onChanged();
                } else {
                    repeatedFieldBuilderV3.clear();
                }
                return this;
            }

            public Builder removeSecret(int index) {
                RepeatedFieldBuilderV3<Secret, Secret.Builder, SecretOrBuilder> repeatedFieldBuilderV3 = this.secretBuilder_;
                if (repeatedFieldBuilderV3 == null) {
                    ensureSecretIsMutable();
                    this.secret_.remove(index);
                    onChanged();
                } else {
                    repeatedFieldBuilderV3.remove(index);
                }
                return this;
            }

            public Secret.Builder getSecretBuilder(int index) {
                return getSecretFieldBuilder().getBuilder(index);
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.SecretPresetOrBuilder
            public SecretOrBuilder getSecretOrBuilder(int index) {
                RepeatedFieldBuilderV3<Secret, Secret.Builder, SecretOrBuilder> repeatedFieldBuilderV3 = this.secretBuilder_;
                if (repeatedFieldBuilderV3 == null) {
                    return this.secret_.get(index);
                }
                return repeatedFieldBuilderV3.getMessageOrBuilder(index);
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.SecretPresetOrBuilder
            public List<? extends SecretOrBuilder> getSecretOrBuilderList() {
                RepeatedFieldBuilderV3<Secret, Secret.Builder, SecretOrBuilder> repeatedFieldBuilderV3 = this.secretBuilder_;
                if (repeatedFieldBuilderV3 != null) {
                    return repeatedFieldBuilderV3.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.secret_);
            }

            public Secret.Builder addSecretBuilder() {
                return getSecretFieldBuilder().addBuilder(Secret.getDefaultInstance());
            }

            public Secret.Builder addSecretBuilder(int index) {
                return getSecretFieldBuilder().addBuilder(index, Secret.getDefaultInstance());
            }

            public List<Secret.Builder> getSecretBuilderList() {
                return getSecretFieldBuilder().getBuilderList();
            }

            private RepeatedFieldBuilderV3<Secret, Secret.Builder, SecretOrBuilder> getSecretFieldBuilder() {
                if (this.secretBuilder_ == null) {
                    this.secretBuilder_ = new RepeatedFieldBuilderV3<>(this.secret_, (this.bitField0_ & 1) != 0, getParentForChildren(), isClean());
                    this.secret_ = null;
                }
                return this.secretBuilder_;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public final Builder setUnknownFields(final UnknownFieldSet unknownFields) {
                return (Builder) super.setUnknownFields(unknownFields);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public final Builder mergeUnknownFields(final UnknownFieldSet unknownFields) {
                return (Builder) super.mergeUnknownFields(unknownFields);
            }
        }

        public static SecretPreset getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SecretPreset> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<SecretPreset> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public SecretPreset getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    /* loaded from: classes2.dex */
    public static final class SecretDeleteResponse extends GeneratedMessageV3 implements SecretDeleteResponseOrBuilder {
        public static final int INDEX_FIELD_NUMBER = 1;
        public static final int STATUS_FIELD_NUMBER = 2;
        private static final long serialVersionUID = 0;
        private int index_;
        private byte memoizedIsInitialized;
        private int status_;
        private static final SecretDeleteResponse DEFAULT_INSTANCE = new SecretDeleteResponse();
        private static final Parser<SecretDeleteResponse> PARSER = new AbstractParser<SecretDeleteResponse>() { // from class: com.xpeng.upso.proxy.PSOProtocol.SecretDeleteResponse.1
            @Override // com.google.protobuf.Parser
            public SecretDeleteResponse parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new SecretDeleteResponse(input, extensionRegistry);
            }
        };

        private SecretDeleteResponse(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private SecretDeleteResponse() {
            this.memoizedIsInitialized = (byte) -1;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
            return new SecretDeleteResponse();
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private SecretDeleteResponse(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistry == null) {
                throw new NullPointerException();
            }
            UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
            boolean done = false;
            while (!done) {
                try {
                    try {
                        try {
                            int tag = input.readTag();
                            if (tag == 0) {
                                done = true;
                            } else if (tag == 8) {
                                this.index_ = input.readUInt32();
                            } else if (tag == 16) {
                                this.status_ = input.readUInt32();
                            } else if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                                done = true;
                            }
                        } catch (InvalidProtocolBufferException e) {
                            throw e.setUnfinishedMessage(this);
                        }
                    } catch (UninitializedMessageException e2) {
                        throw e2.asInvalidProtocolBufferException().setUnfinishedMessage(this);
                    } catch (IOException e3) {
                        throw new InvalidProtocolBufferException(e3).setUnfinishedMessage(this);
                    }
                } finally {
                    this.unknownFields = unknownFields.build();
                    makeExtensionsImmutable();
                }
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return PSOProtocol.internal_static_protocol_SecretDeleteResponse_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return PSOProtocol.internal_static_protocol_SecretDeleteResponse_fieldAccessorTable.ensureFieldAccessorsInitialized(SecretDeleteResponse.class, Builder.class);
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.SecretDeleteResponseOrBuilder
        public int getIndex() {
            return this.index_;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.SecretDeleteResponseOrBuilder
        public int getStatus() {
            return this.status_;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized == 1) {
                return true;
            }
            if (isInitialized == 0) {
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public void writeTo(CodedOutputStream output) throws IOException {
            int i = this.index_;
            if (i != 0) {
                output.writeUInt32(1, i);
            }
            int i2 = this.status_;
            if (i2 != 0) {
                output.writeUInt32(2, i2);
            }
            this.unknownFields.writeTo(output);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSize;
            if (size != -1) {
                return size;
            }
            int i = this.index_;
            int size2 = i != 0 ? 0 + CodedOutputStream.computeUInt32Size(1, i) : 0;
            int i2 = this.status_;
            if (i2 != 0) {
                size2 += CodedOutputStream.computeUInt32Size(2, i2);
            }
            int size3 = size2 + this.unknownFields.getSerializedSize();
            this.memoizedSize = size3;
            return size3;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof SecretDeleteResponse)) {
                return super.equals(obj);
            }
            SecretDeleteResponse other = (SecretDeleteResponse) obj;
            return getIndex() == other.getIndex() && getStatus() == other.getStatus() && this.unknownFields.equals(other.unknownFields);
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = (((((((((((41 * 19) + getDescriptor().hashCode()) * 37) + 1) * 53) + getIndex()) * 37) + 2) * 53) + getStatus()) * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash;
            return hash;
        }

        public static SecretDeleteResponse parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static SecretDeleteResponse parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static SecretDeleteResponse parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static SecretDeleteResponse parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static SecretDeleteResponse parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static SecretDeleteResponse parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static SecretDeleteResponse parseFrom(InputStream input) throws IOException {
            return (SecretDeleteResponse) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static SecretDeleteResponse parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (SecretDeleteResponse) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        public static SecretDeleteResponse parseDelimitedFrom(InputStream input) throws IOException {
            return (SecretDeleteResponse) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
        }

        public static SecretDeleteResponse parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (SecretDeleteResponse) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
        }

        public static SecretDeleteResponse parseFrom(CodedInputStream input) throws IOException {
            return (SecretDeleteResponse) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static SecretDeleteResponse parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (SecretDeleteResponse) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(SecretDeleteResponse prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
            Builder builder = new Builder(parent);
            return builder;
        }

        /* loaded from: classes2.dex */
        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements SecretDeleteResponseOrBuilder {
            private int index_;
            private int status_;

            public static final Descriptors.Descriptor getDescriptor() {
                return PSOProtocol.internal_static_protocol_SecretDeleteResponse_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return PSOProtocol.internal_static_protocol_SecretDeleteResponse_fieldAccessorTable.ensureFieldAccessorsInitialized(SecretDeleteResponse.class, Builder.class);
            }

            private Builder() {
                maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessageV3.BuilderParent parent) {
                super(parent);
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = SecretDeleteResponse.alwaysUseFieldBuilders;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.index_ = 0;
                this.status_ = 0;
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return PSOProtocol.internal_static_protocol_SecretDeleteResponse_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public SecretDeleteResponse getDefaultInstanceForType() {
                return SecretDeleteResponse.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public SecretDeleteResponse build() {
                SecretDeleteResponse result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException((Message) result);
                }
                return result;
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public SecretDeleteResponse buildPartial() {
                SecretDeleteResponse result = new SecretDeleteResponse(this);
                result.index_ = this.index_;
                result.status_ = this.status_;
                onBuilt();
                return result;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder
            /* renamed from: clone */
            public Builder mo93clone() {
                return (Builder) super.mo93clone();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setField(Descriptors.FieldDescriptor field, Object value) {
                return (Builder) super.setField(field, value);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder clearField(Descriptors.FieldDescriptor field) {
                return (Builder) super.clearField(field);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder clearOneof(Descriptors.OneofDescriptor oneof) {
                return (Builder) super.clearOneof(oneof);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
                return (Builder) super.setRepeatedField(field, index, value);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
                return (Builder) super.addRepeatedField(field, value);
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(Message other) {
                if (other instanceof SecretDeleteResponse) {
                    return mergeFrom((SecretDeleteResponse) other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(SecretDeleteResponse other) {
                if (other == SecretDeleteResponse.getDefaultInstance()) {
                    return this;
                }
                if (other.getIndex() != 0) {
                    setIndex(other.getIndex());
                }
                if (other.getStatus() != 0) {
                    setStatus(other.getStatus());
                }
                mergeUnknownFields(other.unknownFields);
                onChanged();
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                SecretDeleteResponse parsedMessage = null;
                try {
                    try {
                        parsedMessage = (SecretDeleteResponse) SecretDeleteResponse.PARSER.parsePartialFrom(input, extensionRegistry);
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        SecretDeleteResponse secretDeleteResponse = (SecretDeleteResponse) e.getUnfinishedMessage();
                        throw e.unwrapIOException();
                    }
                } finally {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                }
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.SecretDeleteResponseOrBuilder
            public int getIndex() {
                return this.index_;
            }

            public Builder setIndex(int value) {
                this.index_ = value;
                onChanged();
                return this;
            }

            public Builder clearIndex() {
                this.index_ = 0;
                onChanged();
                return this;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.SecretDeleteResponseOrBuilder
            public int getStatus() {
                return this.status_;
            }

            public Builder setStatus(int value) {
                this.status_ = value;
                onChanged();
                return this;
            }

            public Builder clearStatus() {
                this.status_ = 0;
                onChanged();
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public final Builder setUnknownFields(final UnknownFieldSet unknownFields) {
                return (Builder) super.setUnknownFields(unknownFields);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public final Builder mergeUnknownFields(final UnknownFieldSet unknownFields) {
                return (Builder) super.mergeUnknownFields(unknownFields);
            }
        }

        public static SecretDeleteResponse getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SecretDeleteResponse> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<SecretDeleteResponse> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public SecretDeleteResponse getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    /* loaded from: classes2.dex */
    public static final class RequestResponse extends GeneratedMessageV3 implements RequestResponseOrBuilder {
        public static final int IDENTITY_INFO_FIELD_NUMBER = 4;
        public static final int MESSAGE_TYPE_FIELD_NUMBER = 1;
        public static final int RESULT_FIELD_NUMBER = 10;
        public static final int SECRETT_AUTH_FIELD_NUMBER = 5;
        public static final int SECRET_DELETE_FIELD_NUMBER = 7;
        public static final int SECRET_DELETE_REPONSE_FIELD_NUMBER = 9;
        public static final int SECRET_PRESET_FIELD_NUMBER = 6;
        public static final int SECRET_PRESET_RESPONSE_FIELD_NUMBER = 8;
        public static final int SEQUENCE_FIELD_NUMBER = 3;
        public static final int SN_ID_FIELD_NUMBER = 2;
        private static final long serialVersionUID = 0;
        private int contentCase_;
        private Object content_;
        private byte memoizedIsInitialized;
        private int messageType_;
        private int result_;
        private int sequence_;
        private volatile Object snId_;
        private static final RequestResponse DEFAULT_INSTANCE = new RequestResponse();
        private static final Parser<RequestResponse> PARSER = new AbstractParser<RequestResponse>() { // from class: com.xpeng.upso.proxy.PSOProtocol.RequestResponse.1
            @Override // com.google.protobuf.Parser
            public RequestResponse parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new RequestResponse(input, extensionRegistry);
            }
        };

        private RequestResponse(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.contentCase_ = 0;
            this.memoizedIsInitialized = (byte) -1;
        }

        private RequestResponse() {
            this.contentCase_ = 0;
            this.memoizedIsInitialized = (byte) -1;
            this.messageType_ = 0;
            this.snId_ = "";
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
            return new RequestResponse();
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private RequestResponse(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistry == null) {
                throw new NullPointerException();
            }
            UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
            boolean done = false;
            while (!done) {
                try {
                    try {
                        try {
                            int tag = input.readTag();
                            switch (tag) {
                                case 0:
                                    done = true;
                                    break;
                                case 8:
                                    int rawValue = input.readEnum();
                                    this.messageType_ = rawValue;
                                    break;
                                case 18:
                                    String s = input.readStringRequireUtf8();
                                    this.snId_ = s;
                                    break;
                                case 24:
                                    this.sequence_ = input.readUInt32();
                                    break;
                                case 34:
                                    IdentityInfo.Builder subBuilder = this.contentCase_ == 4 ? ((IdentityInfo) this.content_).toBuilder() : null;
                                    this.content_ = input.readMessage(IdentityInfo.parser(), extensionRegistry);
                                    if (subBuilder != null) {
                                        subBuilder.mergeFrom((IdentityInfo) this.content_);
                                        this.content_ = subBuilder.buildPartial();
                                    }
                                    this.contentCase_ = 4;
                                    break;
                                case 42:
                                    SecretAuth.Builder subBuilder2 = this.contentCase_ == 5 ? ((SecretAuth) this.content_).toBuilder() : null;
                                    this.content_ = input.readMessage(SecretAuth.parser(), extensionRegistry);
                                    if (subBuilder2 != null) {
                                        subBuilder2.mergeFrom((SecretAuth) this.content_);
                                        this.content_ = subBuilder2.buildPartial();
                                    }
                                    this.contentCase_ = 5;
                                    break;
                                case 50:
                                    SecretPreset.Builder subBuilder3 = this.contentCase_ == 6 ? ((SecretPreset) this.content_).toBuilder() : null;
                                    this.content_ = input.readMessage(SecretPreset.parser(), extensionRegistry);
                                    if (subBuilder3 != null) {
                                        subBuilder3.mergeFrom((SecretPreset) this.content_);
                                        this.content_ = subBuilder3.buildPartial();
                                    }
                                    this.contentCase_ = 6;
                                    break;
                                case 58:
                                    SecretDelete.Builder subBuilder4 = this.contentCase_ == 7 ? ((SecretDelete) this.content_).toBuilder() : null;
                                    this.content_ = input.readMessage(SecretDelete.parser(), extensionRegistry);
                                    if (subBuilder4 != null) {
                                        subBuilder4.mergeFrom((SecretDelete) this.content_);
                                        this.content_ = subBuilder4.buildPartial();
                                    }
                                    this.contentCase_ = 7;
                                    break;
                                case AudioControl.CONTROL_VOLUME_DOWN /* 66 */:
                                    SecretPresetResponse.Builder subBuilder5 = this.contentCase_ == 8 ? ((SecretPresetResponse) this.content_).toBuilder() : null;
                                    this.content_ = input.readMessage(SecretPresetResponse.parser(), extensionRegistry);
                                    if (subBuilder5 != null) {
                                        subBuilder5.mergeFrom((SecretPresetResponse) this.content_);
                                        this.content_ = subBuilder5.buildPartial();
                                    }
                                    this.contentCase_ = 8;
                                    break;
                                case AudioControl.CONTROL_EJECT /* 74 */:
                                    SecretDeleteResponse.Builder subBuilder6 = this.contentCase_ == 9 ? ((SecretDeleteResponse) this.content_).toBuilder() : null;
                                    this.content_ = input.readMessage(SecretDeleteResponse.parser(), extensionRegistry);
                                    if (subBuilder6 != null) {
                                        subBuilder6.mergeFrom((SecretDeleteResponse) this.content_);
                                        this.content_ = subBuilder6.buildPartial();
                                    }
                                    this.contentCase_ = 9;
                                    break;
                                case 80:
                                    this.result_ = input.readUInt32();
                                    break;
                                default:
                                    if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                                        done = true;
                                        break;
                                    } else {
                                        break;
                                    }
                            }
                        } catch (IOException e) {
                            throw new InvalidProtocolBufferException(e).setUnfinishedMessage(this);
                        }
                    } catch (InvalidProtocolBufferException e2) {
                        throw e2.setUnfinishedMessage(this);
                    } catch (UninitializedMessageException e3) {
                        throw e3.asInvalidProtocolBufferException().setUnfinishedMessage(this);
                    }
                } finally {
                    this.unknownFields = unknownFields.build();
                    makeExtensionsImmutable();
                }
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return PSOProtocol.internal_static_protocol_RequestResponse_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return PSOProtocol.internal_static_protocol_RequestResponse_fieldAccessorTable.ensureFieldAccessorsInitialized(RequestResponse.class, Builder.class);
        }

        /* loaded from: classes2.dex */
        public enum MessageType implements ProtocolMessageEnum {
            XP_INFO(0),
            XP_SECRET_PRESET(1),
            XP_SECRET_ENCRYPT_AUTH(2),
            XP_SECRET_DECRYPT_AUTH(3),
            XP_SECRET_DELETE(4),
            XP_INFO_RESPONSE(5),
            XP_SECRET_PRESET_RESPONSE(6),
            XP_SECRET_ENCRYPT_AUTH_RESPONSE(7),
            XP_SECRET_DECRYPT_AUTH_RESPONSE(8),
            XP_SECRET_DELETE_RESPONSE(9),
            XP_SECRET_RESULT(10),
            UNRECOGNIZED(-1);
            
            public static final int XP_INFO_RESPONSE_VALUE = 5;
            public static final int XP_INFO_VALUE = 0;
            public static final int XP_SECRET_DECRYPT_AUTH_RESPONSE_VALUE = 8;
            public static final int XP_SECRET_DECRYPT_AUTH_VALUE = 3;
            public static final int XP_SECRET_DELETE_RESPONSE_VALUE = 9;
            public static final int XP_SECRET_DELETE_VALUE = 4;
            public static final int XP_SECRET_ENCRYPT_AUTH_RESPONSE_VALUE = 7;
            public static final int XP_SECRET_ENCRYPT_AUTH_VALUE = 2;
            public static final int XP_SECRET_PRESET_RESPONSE_VALUE = 6;
            public static final int XP_SECRET_PRESET_VALUE = 1;
            public static final int XP_SECRET_RESULT_VALUE = 10;
            private final int value;
            private static final Internal.EnumLiteMap<MessageType> internalValueMap = new Internal.EnumLiteMap<MessageType>() { // from class: com.xpeng.upso.proxy.PSOProtocol.RequestResponse.MessageType.1
                @Override // com.google.protobuf.Internal.EnumLiteMap
                public MessageType findValueByNumber(int number) {
                    return MessageType.forNumber(number);
                }
            };
            private static final MessageType[] VALUES = values();

            @Override // com.google.protobuf.ProtocolMessageEnum, com.google.protobuf.Internal.EnumLite
            public final int getNumber() {
                if (this == UNRECOGNIZED) {
                    throw new IllegalArgumentException("Can't get the number of an unknown enum value.");
                }
                return this.value;
            }

            @Deprecated
            public static MessageType valueOf(int value) {
                return forNumber(value);
            }

            public static MessageType forNumber(int value) {
                switch (value) {
                    case 0:
                        return XP_INFO;
                    case 1:
                        return XP_SECRET_PRESET;
                    case 2:
                        return XP_SECRET_ENCRYPT_AUTH;
                    case 3:
                        return XP_SECRET_DECRYPT_AUTH;
                    case 4:
                        return XP_SECRET_DELETE;
                    case 5:
                        return XP_INFO_RESPONSE;
                    case 6:
                        return XP_SECRET_PRESET_RESPONSE;
                    case 7:
                        return XP_SECRET_ENCRYPT_AUTH_RESPONSE;
                    case 8:
                        return XP_SECRET_DECRYPT_AUTH_RESPONSE;
                    case 9:
                        return XP_SECRET_DELETE_RESPONSE;
                    case 10:
                        return XP_SECRET_RESULT;
                    default:
                        return null;
                }
            }

            public static Internal.EnumLiteMap<MessageType> internalGetValueMap() {
                return internalValueMap;
            }

            @Override // com.google.protobuf.ProtocolMessageEnum
            public final Descriptors.EnumValueDescriptor getValueDescriptor() {
                if (this == UNRECOGNIZED) {
                    throw new IllegalStateException("Can't get the descriptor of an unrecognized enum value.");
                }
                return getDescriptor().getValues().get(ordinal());
            }

            @Override // com.google.protobuf.ProtocolMessageEnum
            public final Descriptors.EnumDescriptor getDescriptorForType() {
                return getDescriptor();
            }

            public static final Descriptors.EnumDescriptor getDescriptor() {
                return RequestResponse.getDescriptor().getEnumTypes().get(0);
            }

            public static MessageType valueOf(Descriptors.EnumValueDescriptor desc) {
                if (desc.getType() != getDescriptor()) {
                    throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
                }
                if (desc.getIndex() == -1) {
                    return UNRECOGNIZED;
                }
                return VALUES[desc.getIndex()];
            }

            MessageType(int value) {
                this.value = value;
            }
        }

        /* loaded from: classes2.dex */
        public enum ContentCase implements Internal.EnumLite, AbstractMessageLite.InternalOneOfEnum {
            IDENTITY_INFO(4),
            SECRETT_AUTH(5),
            SECRET_PRESET(6),
            SECRET_DELETE(7),
            SECRET_PRESET_RESPONSE(8),
            SECRET_DELETE_REPONSE(9),
            CONTENT_NOT_SET(0);
            
            private final int value;

            ContentCase(int value) {
                this.value = value;
            }

            @Deprecated
            public static ContentCase valueOf(int value) {
                return forNumber(value);
            }

            public static ContentCase forNumber(int value) {
                if (value != 0) {
                    switch (value) {
                        case 4:
                            return IDENTITY_INFO;
                        case 5:
                            return SECRETT_AUTH;
                        case 6:
                            return SECRET_PRESET;
                        case 7:
                            return SECRET_DELETE;
                        case 8:
                            return SECRET_PRESET_RESPONSE;
                        case 9:
                            return SECRET_DELETE_REPONSE;
                        default:
                            return null;
                    }
                }
                return CONTENT_NOT_SET;
            }

            @Override // com.google.protobuf.Internal.EnumLite
            public int getNumber() {
                return this.value;
            }
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
        public ContentCase getContentCase() {
            return ContentCase.forNumber(this.contentCase_);
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
        public int getMessageTypeValue() {
            return this.messageType_;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
        public MessageType getMessageType() {
            MessageType result = MessageType.valueOf(this.messageType_);
            return result == null ? MessageType.UNRECOGNIZED : result;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
        public String getSnId() {
            Object ref = this.snId_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            this.snId_ = s;
            return s;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
        public ByteString getSnIdBytes() {
            Object ref = this.snId_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.snId_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
        public int getSequence() {
            return this.sequence_;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
        public boolean hasIdentityInfo() {
            return this.contentCase_ == 4;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
        public IdentityInfo getIdentityInfo() {
            if (this.contentCase_ == 4) {
                return (IdentityInfo) this.content_;
            }
            return IdentityInfo.getDefaultInstance();
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
        public IdentityInfoOrBuilder getIdentityInfoOrBuilder() {
            if (this.contentCase_ == 4) {
                return (IdentityInfo) this.content_;
            }
            return IdentityInfo.getDefaultInstance();
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
        public boolean hasSecrettAuth() {
            return this.contentCase_ == 5;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
        public SecretAuth getSecrettAuth() {
            if (this.contentCase_ == 5) {
                return (SecretAuth) this.content_;
            }
            return SecretAuth.getDefaultInstance();
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
        public SecretAuthOrBuilder getSecrettAuthOrBuilder() {
            if (this.contentCase_ == 5) {
                return (SecretAuth) this.content_;
            }
            return SecretAuth.getDefaultInstance();
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
        public boolean hasSecretPreset() {
            return this.contentCase_ == 6;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
        public SecretPreset getSecretPreset() {
            if (this.contentCase_ == 6) {
                return (SecretPreset) this.content_;
            }
            return SecretPreset.getDefaultInstance();
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
        public SecretPresetOrBuilder getSecretPresetOrBuilder() {
            if (this.contentCase_ == 6) {
                return (SecretPreset) this.content_;
            }
            return SecretPreset.getDefaultInstance();
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
        public boolean hasSecretDelete() {
            return this.contentCase_ == 7;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
        public SecretDelete getSecretDelete() {
            if (this.contentCase_ == 7) {
                return (SecretDelete) this.content_;
            }
            return SecretDelete.getDefaultInstance();
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
        public SecretDeleteOrBuilder getSecretDeleteOrBuilder() {
            if (this.contentCase_ == 7) {
                return (SecretDelete) this.content_;
            }
            return SecretDelete.getDefaultInstance();
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
        public boolean hasSecretPresetResponse() {
            return this.contentCase_ == 8;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
        public SecretPresetResponse getSecretPresetResponse() {
            if (this.contentCase_ == 8) {
                return (SecretPresetResponse) this.content_;
            }
            return SecretPresetResponse.getDefaultInstance();
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
        public SecretPresetResponseOrBuilder getSecretPresetResponseOrBuilder() {
            if (this.contentCase_ == 8) {
                return (SecretPresetResponse) this.content_;
            }
            return SecretPresetResponse.getDefaultInstance();
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
        public boolean hasSecretDeleteReponse() {
            return this.contentCase_ == 9;
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
        public SecretDeleteResponse getSecretDeleteReponse() {
            if (this.contentCase_ == 9) {
                return (SecretDeleteResponse) this.content_;
            }
            return SecretDeleteResponse.getDefaultInstance();
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
        public SecretDeleteResponseOrBuilder getSecretDeleteReponseOrBuilder() {
            if (this.contentCase_ == 9) {
                return (SecretDeleteResponse) this.content_;
            }
            return SecretDeleteResponse.getDefaultInstance();
        }

        @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
        public int getResult() {
            return this.result_;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized == 1) {
                return true;
            }
            if (isInitialized == 0) {
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public void writeTo(CodedOutputStream output) throws IOException {
            if (this.messageType_ != MessageType.XP_INFO.getNumber()) {
                output.writeEnum(1, this.messageType_);
            }
            if (!GeneratedMessageV3.isStringEmpty(this.snId_)) {
                GeneratedMessageV3.writeString(output, 2, this.snId_);
            }
            int i = this.sequence_;
            if (i != 0) {
                output.writeUInt32(3, i);
            }
            if (this.contentCase_ == 4) {
                output.writeMessage(4, (IdentityInfo) this.content_);
            }
            if (this.contentCase_ == 5) {
                output.writeMessage(5, (SecretAuth) this.content_);
            }
            if (this.contentCase_ == 6) {
                output.writeMessage(6, (SecretPreset) this.content_);
            }
            if (this.contentCase_ == 7) {
                output.writeMessage(7, (SecretDelete) this.content_);
            }
            if (this.contentCase_ == 8) {
                output.writeMessage(8, (SecretPresetResponse) this.content_);
            }
            if (this.contentCase_ == 9) {
                output.writeMessage(9, (SecretDeleteResponse) this.content_);
            }
            int i2 = this.result_;
            if (i2 != 0) {
                output.writeUInt32(10, i2);
            }
            this.unknownFields.writeTo(output);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSize;
            if (size != -1) {
                return size;
            }
            int size2 = this.messageType_ != MessageType.XP_INFO.getNumber() ? 0 + CodedOutputStream.computeEnumSize(1, this.messageType_) : 0;
            if (!GeneratedMessageV3.isStringEmpty(this.snId_)) {
                size2 += GeneratedMessageV3.computeStringSize(2, this.snId_);
            }
            int i = this.sequence_;
            if (i != 0) {
                size2 += CodedOutputStream.computeUInt32Size(3, i);
            }
            if (this.contentCase_ == 4) {
                size2 += CodedOutputStream.computeMessageSize(4, (IdentityInfo) this.content_);
            }
            if (this.contentCase_ == 5) {
                size2 += CodedOutputStream.computeMessageSize(5, (SecretAuth) this.content_);
            }
            if (this.contentCase_ == 6) {
                size2 += CodedOutputStream.computeMessageSize(6, (SecretPreset) this.content_);
            }
            if (this.contentCase_ == 7) {
                size2 += CodedOutputStream.computeMessageSize(7, (SecretDelete) this.content_);
            }
            if (this.contentCase_ == 8) {
                size2 += CodedOutputStream.computeMessageSize(8, (SecretPresetResponse) this.content_);
            }
            if (this.contentCase_ == 9) {
                size2 += CodedOutputStream.computeMessageSize(9, (SecretDeleteResponse) this.content_);
            }
            int i2 = this.result_;
            if (i2 != 0) {
                size2 += CodedOutputStream.computeUInt32Size(10, i2);
            }
            int size3 = size2 + this.unknownFields.getSerializedSize();
            this.memoizedSize = size3;
            return size3;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof RequestResponse)) {
                return super.equals(obj);
            }
            RequestResponse other = (RequestResponse) obj;
            if (this.messageType_ == other.messageType_ && getSnId().equals(other.getSnId()) && getSequence() == other.getSequence() && getResult() == other.getResult() && getContentCase().equals(other.getContentCase())) {
                switch (this.contentCase_) {
                    case 4:
                        if (!getIdentityInfo().equals(other.getIdentityInfo())) {
                            return false;
                        }
                        break;
                    case 5:
                        if (!getSecrettAuth().equals(other.getSecrettAuth())) {
                            return false;
                        }
                        break;
                    case 6:
                        if (!getSecretPreset().equals(other.getSecretPreset())) {
                            return false;
                        }
                        break;
                    case 7:
                        if (!getSecretDelete().equals(other.getSecretDelete())) {
                            return false;
                        }
                        break;
                    case 8:
                        if (!getSecretPresetResponse().equals(other.getSecretPresetResponse())) {
                            return false;
                        }
                        break;
                    case 9:
                        if (!getSecretDeleteReponse().equals(other.getSecretDeleteReponse())) {
                            return false;
                        }
                        break;
                }
                return this.unknownFields.equals(other.unknownFields);
            }
            return false;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = (((((((((((((((((41 * 19) + getDescriptor().hashCode()) * 37) + 1) * 53) + this.messageType_) * 37) + 2) * 53) + getSnId().hashCode()) * 37) + 3) * 53) + getSequence()) * 37) + 10) * 53) + getResult();
            switch (this.contentCase_) {
                case 4:
                    hash = (((hash * 37) + 4) * 53) + getIdentityInfo().hashCode();
                    break;
                case 5:
                    hash = (((hash * 37) + 5) * 53) + getSecrettAuth().hashCode();
                    break;
                case 6:
                    hash = (((hash * 37) + 6) * 53) + getSecretPreset().hashCode();
                    break;
                case 7:
                    hash = (((hash * 37) + 7) * 53) + getSecretDelete().hashCode();
                    break;
                case 8:
                    hash = (((hash * 37) + 8) * 53) + getSecretPresetResponse().hashCode();
                    break;
                case 9:
                    hash = (((hash * 37) + 9) * 53) + getSecretDeleteReponse().hashCode();
                    break;
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        public static RequestResponse parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static RequestResponse parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static RequestResponse parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static RequestResponse parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static RequestResponse parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static RequestResponse parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static RequestResponse parseFrom(InputStream input) throws IOException {
            return (RequestResponse) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static RequestResponse parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (RequestResponse) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        public static RequestResponse parseDelimitedFrom(InputStream input) throws IOException {
            return (RequestResponse) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
        }

        public static RequestResponse parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (RequestResponse) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
        }

        public static RequestResponse parseFrom(CodedInputStream input) throws IOException {
            return (RequestResponse) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static RequestResponse parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (RequestResponse) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(RequestResponse prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
            Builder builder = new Builder(parent);
            return builder;
        }

        /* loaded from: classes2.dex */
        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements RequestResponseOrBuilder {
            private int contentCase_;
            private Object content_;
            private SingleFieldBuilderV3<IdentityInfo, IdentityInfo.Builder, IdentityInfoOrBuilder> identityInfoBuilder_;
            private int messageType_;
            private int result_;
            private SingleFieldBuilderV3<SecretDelete, SecretDelete.Builder, SecretDeleteOrBuilder> secretDeleteBuilder_;
            private SingleFieldBuilderV3<SecretDeleteResponse, SecretDeleteResponse.Builder, SecretDeleteResponseOrBuilder> secretDeleteReponseBuilder_;
            private SingleFieldBuilderV3<SecretPreset, SecretPreset.Builder, SecretPresetOrBuilder> secretPresetBuilder_;
            private SingleFieldBuilderV3<SecretPresetResponse, SecretPresetResponse.Builder, SecretPresetResponseOrBuilder> secretPresetResponseBuilder_;
            private SingleFieldBuilderV3<SecretAuth, SecretAuth.Builder, SecretAuthOrBuilder> secrettAuthBuilder_;
            private int sequence_;
            private Object snId_;

            public static final Descriptors.Descriptor getDescriptor() {
                return PSOProtocol.internal_static_protocol_RequestResponse_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return PSOProtocol.internal_static_protocol_RequestResponse_fieldAccessorTable.ensureFieldAccessorsInitialized(RequestResponse.class, Builder.class);
            }

            private Builder() {
                this.contentCase_ = 0;
                this.messageType_ = 0;
                this.snId_ = "";
                maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessageV3.BuilderParent parent) {
                super(parent);
                this.contentCase_ = 0;
                this.messageType_ = 0;
                this.snId_ = "";
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = RequestResponse.alwaysUseFieldBuilders;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.messageType_ = 0;
                this.snId_ = "";
                this.sequence_ = 0;
                this.result_ = 0;
                this.contentCase_ = 0;
                this.content_ = null;
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return PSOProtocol.internal_static_protocol_RequestResponse_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public RequestResponse getDefaultInstanceForType() {
                return RequestResponse.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public RequestResponse build() {
                RequestResponse result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException((Message) result);
                }
                return result;
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public RequestResponse buildPartial() {
                RequestResponse result = new RequestResponse(this);
                result.messageType_ = this.messageType_;
                result.snId_ = this.snId_;
                result.sequence_ = this.sequence_;
                if (this.contentCase_ == 4) {
                    SingleFieldBuilderV3<IdentityInfo, IdentityInfo.Builder, IdentityInfoOrBuilder> singleFieldBuilderV3 = this.identityInfoBuilder_;
                    if (singleFieldBuilderV3 == null) {
                        result.content_ = this.content_;
                    } else {
                        result.content_ = singleFieldBuilderV3.build();
                    }
                }
                if (this.contentCase_ == 5) {
                    SingleFieldBuilderV3<SecretAuth, SecretAuth.Builder, SecretAuthOrBuilder> singleFieldBuilderV32 = this.secrettAuthBuilder_;
                    if (singleFieldBuilderV32 == null) {
                        result.content_ = this.content_;
                    } else {
                        result.content_ = singleFieldBuilderV32.build();
                    }
                }
                if (this.contentCase_ == 6) {
                    SingleFieldBuilderV3<SecretPreset, SecretPreset.Builder, SecretPresetOrBuilder> singleFieldBuilderV33 = this.secretPresetBuilder_;
                    if (singleFieldBuilderV33 == null) {
                        result.content_ = this.content_;
                    } else {
                        result.content_ = singleFieldBuilderV33.build();
                    }
                }
                if (this.contentCase_ == 7) {
                    SingleFieldBuilderV3<SecretDelete, SecretDelete.Builder, SecretDeleteOrBuilder> singleFieldBuilderV34 = this.secretDeleteBuilder_;
                    if (singleFieldBuilderV34 == null) {
                        result.content_ = this.content_;
                    } else {
                        result.content_ = singleFieldBuilderV34.build();
                    }
                }
                if (this.contentCase_ == 8) {
                    SingleFieldBuilderV3<SecretPresetResponse, SecretPresetResponse.Builder, SecretPresetResponseOrBuilder> singleFieldBuilderV35 = this.secretPresetResponseBuilder_;
                    if (singleFieldBuilderV35 == null) {
                        result.content_ = this.content_;
                    } else {
                        result.content_ = singleFieldBuilderV35.build();
                    }
                }
                if (this.contentCase_ == 9) {
                    SingleFieldBuilderV3<SecretDeleteResponse, SecretDeleteResponse.Builder, SecretDeleteResponseOrBuilder> singleFieldBuilderV36 = this.secretDeleteReponseBuilder_;
                    if (singleFieldBuilderV36 == null) {
                        result.content_ = this.content_;
                    } else {
                        result.content_ = singleFieldBuilderV36.build();
                    }
                }
                result.result_ = this.result_;
                result.contentCase_ = this.contentCase_;
                onBuilt();
                return result;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder
            /* renamed from: clone */
            public Builder mo93clone() {
                return (Builder) super.mo93clone();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setField(Descriptors.FieldDescriptor field, Object value) {
                return (Builder) super.setField(field, value);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder clearField(Descriptors.FieldDescriptor field) {
                return (Builder) super.clearField(field);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder clearOneof(Descriptors.OneofDescriptor oneof) {
                return (Builder) super.clearOneof(oneof);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
                return (Builder) super.setRepeatedField(field, index, value);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
                return (Builder) super.addRepeatedField(field, value);
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(Message other) {
                if (other instanceof RequestResponse) {
                    return mergeFrom((RequestResponse) other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(RequestResponse other) {
                if (other == RequestResponse.getDefaultInstance()) {
                    return this;
                }
                if (other.messageType_ != 0) {
                    setMessageTypeValue(other.getMessageTypeValue());
                }
                if (!other.getSnId().isEmpty()) {
                    this.snId_ = other.snId_;
                    onChanged();
                }
                if (other.getSequence() != 0) {
                    setSequence(other.getSequence());
                }
                if (other.getResult() != 0) {
                    setResult(other.getResult());
                }
                switch (other.getContentCase()) {
                    case IDENTITY_INFO:
                        mergeIdentityInfo(other.getIdentityInfo());
                        break;
                    case SECRETT_AUTH:
                        mergeSecrettAuth(other.getSecrettAuth());
                        break;
                    case SECRET_PRESET:
                        mergeSecretPreset(other.getSecretPreset());
                        break;
                    case SECRET_DELETE:
                        mergeSecretDelete(other.getSecretDelete());
                        break;
                    case SECRET_PRESET_RESPONSE:
                        mergeSecretPresetResponse(other.getSecretPresetResponse());
                        break;
                    case SECRET_DELETE_REPONSE:
                        mergeSecretDeleteReponse(other.getSecretDeleteReponse());
                        break;
                }
                mergeUnknownFields(other.unknownFields);
                onChanged();
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                RequestResponse parsedMessage = null;
                try {
                    try {
                        parsedMessage = (RequestResponse) RequestResponse.PARSER.parsePartialFrom(input, extensionRegistry);
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        RequestResponse requestResponse = (RequestResponse) e.getUnfinishedMessage();
                        throw e.unwrapIOException();
                    }
                } finally {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                }
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
            public ContentCase getContentCase() {
                return ContentCase.forNumber(this.contentCase_);
            }

            public Builder clearContent() {
                this.contentCase_ = 0;
                this.content_ = null;
                onChanged();
                return this;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
            public int getMessageTypeValue() {
                return this.messageType_;
            }

            public Builder setMessageTypeValue(int value) {
                this.messageType_ = value;
                onChanged();
                return this;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
            public MessageType getMessageType() {
                MessageType result = MessageType.valueOf(this.messageType_);
                return result == null ? MessageType.UNRECOGNIZED : result;
            }

            public Builder setMessageType(MessageType value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.messageType_ = value.getNumber();
                onChanged();
                return this;
            }

            public Builder clearMessageType() {
                this.messageType_ = 0;
                onChanged();
                return this;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
            public String getSnId() {
                Object ref = this.snId_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    this.snId_ = s;
                    return s;
                }
                return (String) ref;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
            public ByteString getSnIdBytes() {
                Object ref = this.snId_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.snId_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            public Builder setSnId(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.snId_ = value;
                onChanged();
                return this;
            }

            public Builder clearSnId() {
                this.snId_ = RequestResponse.getDefaultInstance().getSnId();
                onChanged();
                return this;
            }

            public Builder setSnIdBytes(ByteString value) {
                if (value != null) {
                    RequestResponse.checkByteStringIsUtf8(value);
                    this.snId_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
            public int getSequence() {
                return this.sequence_;
            }

            public Builder setSequence(int value) {
                this.sequence_ = value;
                onChanged();
                return this;
            }

            public Builder clearSequence() {
                this.sequence_ = 0;
                onChanged();
                return this;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
            public boolean hasIdentityInfo() {
                return this.contentCase_ == 4;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
            public IdentityInfo getIdentityInfo() {
                SingleFieldBuilderV3<IdentityInfo, IdentityInfo.Builder, IdentityInfoOrBuilder> singleFieldBuilderV3 = this.identityInfoBuilder_;
                if (singleFieldBuilderV3 == null) {
                    if (this.contentCase_ == 4) {
                        return (IdentityInfo) this.content_;
                    }
                    return IdentityInfo.getDefaultInstance();
                } else if (this.contentCase_ == 4) {
                    return singleFieldBuilderV3.getMessage();
                } else {
                    return IdentityInfo.getDefaultInstance();
                }
            }

            public Builder setIdentityInfo(IdentityInfo value) {
                SingleFieldBuilderV3<IdentityInfo, IdentityInfo.Builder, IdentityInfoOrBuilder> singleFieldBuilderV3 = this.identityInfoBuilder_;
                if (singleFieldBuilderV3 == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.content_ = value;
                    onChanged();
                } else {
                    singleFieldBuilderV3.setMessage(value);
                }
                this.contentCase_ = 4;
                return this;
            }

            public Builder setIdentityInfo(IdentityInfo.Builder builderForValue) {
                SingleFieldBuilderV3<IdentityInfo, IdentityInfo.Builder, IdentityInfoOrBuilder> singleFieldBuilderV3 = this.identityInfoBuilder_;
                if (singleFieldBuilderV3 == null) {
                    this.content_ = builderForValue.build();
                    onChanged();
                } else {
                    singleFieldBuilderV3.setMessage(builderForValue.build());
                }
                this.contentCase_ = 4;
                return this;
            }

            public Builder mergeIdentityInfo(IdentityInfo value) {
                SingleFieldBuilderV3<IdentityInfo, IdentityInfo.Builder, IdentityInfoOrBuilder> singleFieldBuilderV3 = this.identityInfoBuilder_;
                if (singleFieldBuilderV3 == null) {
                    if (this.contentCase_ == 4 && this.content_ != IdentityInfo.getDefaultInstance()) {
                        this.content_ = IdentityInfo.newBuilder((IdentityInfo) this.content_).mergeFrom(value).buildPartial();
                    } else {
                        this.content_ = value;
                    }
                    onChanged();
                } else if (this.contentCase_ == 4) {
                    singleFieldBuilderV3.mergeFrom(value);
                } else {
                    singleFieldBuilderV3.setMessage(value);
                }
                this.contentCase_ = 4;
                return this;
            }

            public Builder clearIdentityInfo() {
                if (this.identityInfoBuilder_ == null) {
                    if (this.contentCase_ == 4) {
                        this.contentCase_ = 0;
                        this.content_ = null;
                        onChanged();
                    }
                } else {
                    if (this.contentCase_ == 4) {
                        this.contentCase_ = 0;
                        this.content_ = null;
                    }
                    this.identityInfoBuilder_.clear();
                }
                return this;
            }

            public IdentityInfo.Builder getIdentityInfoBuilder() {
                return getIdentityInfoFieldBuilder().getBuilder();
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
            public IdentityInfoOrBuilder getIdentityInfoOrBuilder() {
                SingleFieldBuilderV3<IdentityInfo, IdentityInfo.Builder, IdentityInfoOrBuilder> singleFieldBuilderV3;
                if (this.contentCase_ == 4 && (singleFieldBuilderV3 = this.identityInfoBuilder_) != null) {
                    return singleFieldBuilderV3.getMessageOrBuilder();
                }
                if (this.contentCase_ == 4) {
                    return (IdentityInfo) this.content_;
                }
                return IdentityInfo.getDefaultInstance();
            }

            private SingleFieldBuilderV3<IdentityInfo, IdentityInfo.Builder, IdentityInfoOrBuilder> getIdentityInfoFieldBuilder() {
                if (this.identityInfoBuilder_ == null) {
                    if (this.contentCase_ != 4) {
                        this.content_ = IdentityInfo.getDefaultInstance();
                    }
                    this.identityInfoBuilder_ = new SingleFieldBuilderV3<>((IdentityInfo) this.content_, getParentForChildren(), isClean());
                    this.content_ = null;
                }
                this.contentCase_ = 4;
                onChanged();
                return this.identityInfoBuilder_;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
            public boolean hasSecrettAuth() {
                return this.contentCase_ == 5;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
            public SecretAuth getSecrettAuth() {
                SingleFieldBuilderV3<SecretAuth, SecretAuth.Builder, SecretAuthOrBuilder> singleFieldBuilderV3 = this.secrettAuthBuilder_;
                if (singleFieldBuilderV3 == null) {
                    if (this.contentCase_ == 5) {
                        return (SecretAuth) this.content_;
                    }
                    return SecretAuth.getDefaultInstance();
                } else if (this.contentCase_ == 5) {
                    return singleFieldBuilderV3.getMessage();
                } else {
                    return SecretAuth.getDefaultInstance();
                }
            }

            public Builder setSecrettAuth(SecretAuth value) {
                SingleFieldBuilderV3<SecretAuth, SecretAuth.Builder, SecretAuthOrBuilder> singleFieldBuilderV3 = this.secrettAuthBuilder_;
                if (singleFieldBuilderV3 == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.content_ = value;
                    onChanged();
                } else {
                    singleFieldBuilderV3.setMessage(value);
                }
                this.contentCase_ = 5;
                return this;
            }

            public Builder setSecrettAuth(SecretAuth.Builder builderForValue) {
                SingleFieldBuilderV3<SecretAuth, SecretAuth.Builder, SecretAuthOrBuilder> singleFieldBuilderV3 = this.secrettAuthBuilder_;
                if (singleFieldBuilderV3 == null) {
                    this.content_ = builderForValue.build();
                    onChanged();
                } else {
                    singleFieldBuilderV3.setMessage(builderForValue.build());
                }
                this.contentCase_ = 5;
                return this;
            }

            public Builder mergeSecrettAuth(SecretAuth value) {
                SingleFieldBuilderV3<SecretAuth, SecretAuth.Builder, SecretAuthOrBuilder> singleFieldBuilderV3 = this.secrettAuthBuilder_;
                if (singleFieldBuilderV3 == null) {
                    if (this.contentCase_ == 5 && this.content_ != SecretAuth.getDefaultInstance()) {
                        this.content_ = SecretAuth.newBuilder((SecretAuth) this.content_).mergeFrom(value).buildPartial();
                    } else {
                        this.content_ = value;
                    }
                    onChanged();
                } else if (this.contentCase_ == 5) {
                    singleFieldBuilderV3.mergeFrom(value);
                } else {
                    singleFieldBuilderV3.setMessage(value);
                }
                this.contentCase_ = 5;
                return this;
            }

            public Builder clearSecrettAuth() {
                if (this.secrettAuthBuilder_ == null) {
                    if (this.contentCase_ == 5) {
                        this.contentCase_ = 0;
                        this.content_ = null;
                        onChanged();
                    }
                } else {
                    if (this.contentCase_ == 5) {
                        this.contentCase_ = 0;
                        this.content_ = null;
                    }
                    this.secrettAuthBuilder_.clear();
                }
                return this;
            }

            public SecretAuth.Builder getSecrettAuthBuilder() {
                return getSecrettAuthFieldBuilder().getBuilder();
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
            public SecretAuthOrBuilder getSecrettAuthOrBuilder() {
                SingleFieldBuilderV3<SecretAuth, SecretAuth.Builder, SecretAuthOrBuilder> singleFieldBuilderV3;
                if (this.contentCase_ == 5 && (singleFieldBuilderV3 = this.secrettAuthBuilder_) != null) {
                    return singleFieldBuilderV3.getMessageOrBuilder();
                }
                if (this.contentCase_ == 5) {
                    return (SecretAuth) this.content_;
                }
                return SecretAuth.getDefaultInstance();
            }

            private SingleFieldBuilderV3<SecretAuth, SecretAuth.Builder, SecretAuthOrBuilder> getSecrettAuthFieldBuilder() {
                if (this.secrettAuthBuilder_ == null) {
                    if (this.contentCase_ != 5) {
                        this.content_ = SecretAuth.getDefaultInstance();
                    }
                    this.secrettAuthBuilder_ = new SingleFieldBuilderV3<>((SecretAuth) this.content_, getParentForChildren(), isClean());
                    this.content_ = null;
                }
                this.contentCase_ = 5;
                onChanged();
                return this.secrettAuthBuilder_;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
            public boolean hasSecretPreset() {
                return this.contentCase_ == 6;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
            public SecretPreset getSecretPreset() {
                SingleFieldBuilderV3<SecretPreset, SecretPreset.Builder, SecretPresetOrBuilder> singleFieldBuilderV3 = this.secretPresetBuilder_;
                if (singleFieldBuilderV3 == null) {
                    if (this.contentCase_ == 6) {
                        return (SecretPreset) this.content_;
                    }
                    return SecretPreset.getDefaultInstance();
                } else if (this.contentCase_ == 6) {
                    return singleFieldBuilderV3.getMessage();
                } else {
                    return SecretPreset.getDefaultInstance();
                }
            }

            public Builder setSecretPreset(SecretPreset value) {
                SingleFieldBuilderV3<SecretPreset, SecretPreset.Builder, SecretPresetOrBuilder> singleFieldBuilderV3 = this.secretPresetBuilder_;
                if (singleFieldBuilderV3 == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.content_ = value;
                    onChanged();
                } else {
                    singleFieldBuilderV3.setMessage(value);
                }
                this.contentCase_ = 6;
                return this;
            }

            public Builder setSecretPreset(SecretPreset.Builder builderForValue) {
                SingleFieldBuilderV3<SecretPreset, SecretPreset.Builder, SecretPresetOrBuilder> singleFieldBuilderV3 = this.secretPresetBuilder_;
                if (singleFieldBuilderV3 == null) {
                    this.content_ = builderForValue.build();
                    onChanged();
                } else {
                    singleFieldBuilderV3.setMessage(builderForValue.build());
                }
                this.contentCase_ = 6;
                return this;
            }

            public Builder mergeSecretPreset(SecretPreset value) {
                SingleFieldBuilderV3<SecretPreset, SecretPreset.Builder, SecretPresetOrBuilder> singleFieldBuilderV3 = this.secretPresetBuilder_;
                if (singleFieldBuilderV3 == null) {
                    if (this.contentCase_ == 6 && this.content_ != SecretPreset.getDefaultInstance()) {
                        this.content_ = SecretPreset.newBuilder((SecretPreset) this.content_).mergeFrom(value).buildPartial();
                    } else {
                        this.content_ = value;
                    }
                    onChanged();
                } else if (this.contentCase_ == 6) {
                    singleFieldBuilderV3.mergeFrom(value);
                } else {
                    singleFieldBuilderV3.setMessage(value);
                }
                this.contentCase_ = 6;
                return this;
            }

            public Builder clearSecretPreset() {
                if (this.secretPresetBuilder_ == null) {
                    if (this.contentCase_ == 6) {
                        this.contentCase_ = 0;
                        this.content_ = null;
                        onChanged();
                    }
                } else {
                    if (this.contentCase_ == 6) {
                        this.contentCase_ = 0;
                        this.content_ = null;
                    }
                    this.secretPresetBuilder_.clear();
                }
                return this;
            }

            public SecretPreset.Builder getSecretPresetBuilder() {
                return getSecretPresetFieldBuilder().getBuilder();
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
            public SecretPresetOrBuilder getSecretPresetOrBuilder() {
                SingleFieldBuilderV3<SecretPreset, SecretPreset.Builder, SecretPresetOrBuilder> singleFieldBuilderV3;
                if (this.contentCase_ == 6 && (singleFieldBuilderV3 = this.secretPresetBuilder_) != null) {
                    return singleFieldBuilderV3.getMessageOrBuilder();
                }
                if (this.contentCase_ == 6) {
                    return (SecretPreset) this.content_;
                }
                return SecretPreset.getDefaultInstance();
            }

            private SingleFieldBuilderV3<SecretPreset, SecretPreset.Builder, SecretPresetOrBuilder> getSecretPresetFieldBuilder() {
                if (this.secretPresetBuilder_ == null) {
                    if (this.contentCase_ != 6) {
                        this.content_ = SecretPreset.getDefaultInstance();
                    }
                    this.secretPresetBuilder_ = new SingleFieldBuilderV3<>((SecretPreset) this.content_, getParentForChildren(), isClean());
                    this.content_ = null;
                }
                this.contentCase_ = 6;
                onChanged();
                return this.secretPresetBuilder_;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
            public boolean hasSecretDelete() {
                return this.contentCase_ == 7;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
            public SecretDelete getSecretDelete() {
                SingleFieldBuilderV3<SecretDelete, SecretDelete.Builder, SecretDeleteOrBuilder> singleFieldBuilderV3 = this.secretDeleteBuilder_;
                if (singleFieldBuilderV3 == null) {
                    if (this.contentCase_ == 7) {
                        return (SecretDelete) this.content_;
                    }
                    return SecretDelete.getDefaultInstance();
                } else if (this.contentCase_ == 7) {
                    return singleFieldBuilderV3.getMessage();
                } else {
                    return SecretDelete.getDefaultInstance();
                }
            }

            public Builder setSecretDelete(SecretDelete value) {
                SingleFieldBuilderV3<SecretDelete, SecretDelete.Builder, SecretDeleteOrBuilder> singleFieldBuilderV3 = this.secretDeleteBuilder_;
                if (singleFieldBuilderV3 == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.content_ = value;
                    onChanged();
                } else {
                    singleFieldBuilderV3.setMessage(value);
                }
                this.contentCase_ = 7;
                return this;
            }

            public Builder setSecretDelete(SecretDelete.Builder builderForValue) {
                SingleFieldBuilderV3<SecretDelete, SecretDelete.Builder, SecretDeleteOrBuilder> singleFieldBuilderV3 = this.secretDeleteBuilder_;
                if (singleFieldBuilderV3 == null) {
                    this.content_ = builderForValue.build();
                    onChanged();
                } else {
                    singleFieldBuilderV3.setMessage(builderForValue.build());
                }
                this.contentCase_ = 7;
                return this;
            }

            public Builder mergeSecretDelete(SecretDelete value) {
                SingleFieldBuilderV3<SecretDelete, SecretDelete.Builder, SecretDeleteOrBuilder> singleFieldBuilderV3 = this.secretDeleteBuilder_;
                if (singleFieldBuilderV3 == null) {
                    if (this.contentCase_ == 7 && this.content_ != SecretDelete.getDefaultInstance()) {
                        this.content_ = SecretDelete.newBuilder((SecretDelete) this.content_).mergeFrom(value).buildPartial();
                    } else {
                        this.content_ = value;
                    }
                    onChanged();
                } else if (this.contentCase_ == 7) {
                    singleFieldBuilderV3.mergeFrom(value);
                } else {
                    singleFieldBuilderV3.setMessage(value);
                }
                this.contentCase_ = 7;
                return this;
            }

            public Builder clearSecretDelete() {
                if (this.secretDeleteBuilder_ == null) {
                    if (this.contentCase_ == 7) {
                        this.contentCase_ = 0;
                        this.content_ = null;
                        onChanged();
                    }
                } else {
                    if (this.contentCase_ == 7) {
                        this.contentCase_ = 0;
                        this.content_ = null;
                    }
                    this.secretDeleteBuilder_.clear();
                }
                return this;
            }

            public SecretDelete.Builder getSecretDeleteBuilder() {
                return getSecretDeleteFieldBuilder().getBuilder();
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
            public SecretDeleteOrBuilder getSecretDeleteOrBuilder() {
                SingleFieldBuilderV3<SecretDelete, SecretDelete.Builder, SecretDeleteOrBuilder> singleFieldBuilderV3;
                if (this.contentCase_ == 7 && (singleFieldBuilderV3 = this.secretDeleteBuilder_) != null) {
                    return singleFieldBuilderV3.getMessageOrBuilder();
                }
                if (this.contentCase_ == 7) {
                    return (SecretDelete) this.content_;
                }
                return SecretDelete.getDefaultInstance();
            }

            private SingleFieldBuilderV3<SecretDelete, SecretDelete.Builder, SecretDeleteOrBuilder> getSecretDeleteFieldBuilder() {
                if (this.secretDeleteBuilder_ == null) {
                    if (this.contentCase_ != 7) {
                        this.content_ = SecretDelete.getDefaultInstance();
                    }
                    this.secretDeleteBuilder_ = new SingleFieldBuilderV3<>((SecretDelete) this.content_, getParentForChildren(), isClean());
                    this.content_ = null;
                }
                this.contentCase_ = 7;
                onChanged();
                return this.secretDeleteBuilder_;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
            public boolean hasSecretPresetResponse() {
                return this.contentCase_ == 8;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
            public SecretPresetResponse getSecretPresetResponse() {
                SingleFieldBuilderV3<SecretPresetResponse, SecretPresetResponse.Builder, SecretPresetResponseOrBuilder> singleFieldBuilderV3 = this.secretPresetResponseBuilder_;
                if (singleFieldBuilderV3 == null) {
                    if (this.contentCase_ == 8) {
                        return (SecretPresetResponse) this.content_;
                    }
                    return SecretPresetResponse.getDefaultInstance();
                } else if (this.contentCase_ == 8) {
                    return singleFieldBuilderV3.getMessage();
                } else {
                    return SecretPresetResponse.getDefaultInstance();
                }
            }

            public Builder setSecretPresetResponse(SecretPresetResponse value) {
                SingleFieldBuilderV3<SecretPresetResponse, SecretPresetResponse.Builder, SecretPresetResponseOrBuilder> singleFieldBuilderV3 = this.secretPresetResponseBuilder_;
                if (singleFieldBuilderV3 == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.content_ = value;
                    onChanged();
                } else {
                    singleFieldBuilderV3.setMessage(value);
                }
                this.contentCase_ = 8;
                return this;
            }

            public Builder setSecretPresetResponse(SecretPresetResponse.Builder builderForValue) {
                SingleFieldBuilderV3<SecretPresetResponse, SecretPresetResponse.Builder, SecretPresetResponseOrBuilder> singleFieldBuilderV3 = this.secretPresetResponseBuilder_;
                if (singleFieldBuilderV3 == null) {
                    this.content_ = builderForValue.build();
                    onChanged();
                } else {
                    singleFieldBuilderV3.setMessage(builderForValue.build());
                }
                this.contentCase_ = 8;
                return this;
            }

            public Builder mergeSecretPresetResponse(SecretPresetResponse value) {
                SingleFieldBuilderV3<SecretPresetResponse, SecretPresetResponse.Builder, SecretPresetResponseOrBuilder> singleFieldBuilderV3 = this.secretPresetResponseBuilder_;
                if (singleFieldBuilderV3 == null) {
                    if (this.contentCase_ == 8 && this.content_ != SecretPresetResponse.getDefaultInstance()) {
                        this.content_ = SecretPresetResponse.newBuilder((SecretPresetResponse) this.content_).mergeFrom(value).buildPartial();
                    } else {
                        this.content_ = value;
                    }
                    onChanged();
                } else if (this.contentCase_ == 8) {
                    singleFieldBuilderV3.mergeFrom(value);
                } else {
                    singleFieldBuilderV3.setMessage(value);
                }
                this.contentCase_ = 8;
                return this;
            }

            public Builder clearSecretPresetResponse() {
                if (this.secretPresetResponseBuilder_ == null) {
                    if (this.contentCase_ == 8) {
                        this.contentCase_ = 0;
                        this.content_ = null;
                        onChanged();
                    }
                } else {
                    if (this.contentCase_ == 8) {
                        this.contentCase_ = 0;
                        this.content_ = null;
                    }
                    this.secretPresetResponseBuilder_.clear();
                }
                return this;
            }

            public SecretPresetResponse.Builder getSecretPresetResponseBuilder() {
                return getSecretPresetResponseFieldBuilder().getBuilder();
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
            public SecretPresetResponseOrBuilder getSecretPresetResponseOrBuilder() {
                SingleFieldBuilderV3<SecretPresetResponse, SecretPresetResponse.Builder, SecretPresetResponseOrBuilder> singleFieldBuilderV3;
                if (this.contentCase_ == 8 && (singleFieldBuilderV3 = this.secretPresetResponseBuilder_) != null) {
                    return singleFieldBuilderV3.getMessageOrBuilder();
                }
                if (this.contentCase_ == 8) {
                    return (SecretPresetResponse) this.content_;
                }
                return SecretPresetResponse.getDefaultInstance();
            }

            private SingleFieldBuilderV3<SecretPresetResponse, SecretPresetResponse.Builder, SecretPresetResponseOrBuilder> getSecretPresetResponseFieldBuilder() {
                if (this.secretPresetResponseBuilder_ == null) {
                    if (this.contentCase_ != 8) {
                        this.content_ = SecretPresetResponse.getDefaultInstance();
                    }
                    this.secretPresetResponseBuilder_ = new SingleFieldBuilderV3<>((SecretPresetResponse) this.content_, getParentForChildren(), isClean());
                    this.content_ = null;
                }
                this.contentCase_ = 8;
                onChanged();
                return this.secretPresetResponseBuilder_;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
            public boolean hasSecretDeleteReponse() {
                return this.contentCase_ == 9;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
            public SecretDeleteResponse getSecretDeleteReponse() {
                SingleFieldBuilderV3<SecretDeleteResponse, SecretDeleteResponse.Builder, SecretDeleteResponseOrBuilder> singleFieldBuilderV3 = this.secretDeleteReponseBuilder_;
                if (singleFieldBuilderV3 == null) {
                    if (this.contentCase_ == 9) {
                        return (SecretDeleteResponse) this.content_;
                    }
                    return SecretDeleteResponse.getDefaultInstance();
                } else if (this.contentCase_ == 9) {
                    return singleFieldBuilderV3.getMessage();
                } else {
                    return SecretDeleteResponse.getDefaultInstance();
                }
            }

            public Builder setSecretDeleteReponse(SecretDeleteResponse value) {
                SingleFieldBuilderV3<SecretDeleteResponse, SecretDeleteResponse.Builder, SecretDeleteResponseOrBuilder> singleFieldBuilderV3 = this.secretDeleteReponseBuilder_;
                if (singleFieldBuilderV3 == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.content_ = value;
                    onChanged();
                } else {
                    singleFieldBuilderV3.setMessage(value);
                }
                this.contentCase_ = 9;
                return this;
            }

            public Builder setSecretDeleteReponse(SecretDeleteResponse.Builder builderForValue) {
                SingleFieldBuilderV3<SecretDeleteResponse, SecretDeleteResponse.Builder, SecretDeleteResponseOrBuilder> singleFieldBuilderV3 = this.secretDeleteReponseBuilder_;
                if (singleFieldBuilderV3 == null) {
                    this.content_ = builderForValue.build();
                    onChanged();
                } else {
                    singleFieldBuilderV3.setMessage(builderForValue.build());
                }
                this.contentCase_ = 9;
                return this;
            }

            public Builder mergeSecretDeleteReponse(SecretDeleteResponse value) {
                SingleFieldBuilderV3<SecretDeleteResponse, SecretDeleteResponse.Builder, SecretDeleteResponseOrBuilder> singleFieldBuilderV3 = this.secretDeleteReponseBuilder_;
                if (singleFieldBuilderV3 == null) {
                    if (this.contentCase_ == 9 && this.content_ != SecretDeleteResponse.getDefaultInstance()) {
                        this.content_ = SecretDeleteResponse.newBuilder((SecretDeleteResponse) this.content_).mergeFrom(value).buildPartial();
                    } else {
                        this.content_ = value;
                    }
                    onChanged();
                } else if (this.contentCase_ == 9) {
                    singleFieldBuilderV3.mergeFrom(value);
                } else {
                    singleFieldBuilderV3.setMessage(value);
                }
                this.contentCase_ = 9;
                return this;
            }

            public Builder clearSecretDeleteReponse() {
                if (this.secretDeleteReponseBuilder_ == null) {
                    if (this.contentCase_ == 9) {
                        this.contentCase_ = 0;
                        this.content_ = null;
                        onChanged();
                    }
                } else {
                    if (this.contentCase_ == 9) {
                        this.contentCase_ = 0;
                        this.content_ = null;
                    }
                    this.secretDeleteReponseBuilder_.clear();
                }
                return this;
            }

            public SecretDeleteResponse.Builder getSecretDeleteReponseBuilder() {
                return getSecretDeleteReponseFieldBuilder().getBuilder();
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
            public SecretDeleteResponseOrBuilder getSecretDeleteReponseOrBuilder() {
                SingleFieldBuilderV3<SecretDeleteResponse, SecretDeleteResponse.Builder, SecretDeleteResponseOrBuilder> singleFieldBuilderV3;
                if (this.contentCase_ == 9 && (singleFieldBuilderV3 = this.secretDeleteReponseBuilder_) != null) {
                    return singleFieldBuilderV3.getMessageOrBuilder();
                }
                if (this.contentCase_ == 9) {
                    return (SecretDeleteResponse) this.content_;
                }
                return SecretDeleteResponse.getDefaultInstance();
            }

            private SingleFieldBuilderV3<SecretDeleteResponse, SecretDeleteResponse.Builder, SecretDeleteResponseOrBuilder> getSecretDeleteReponseFieldBuilder() {
                if (this.secretDeleteReponseBuilder_ == null) {
                    if (this.contentCase_ != 9) {
                        this.content_ = SecretDeleteResponse.getDefaultInstance();
                    }
                    this.secretDeleteReponseBuilder_ = new SingleFieldBuilderV3<>((SecretDeleteResponse) this.content_, getParentForChildren(), isClean());
                    this.content_ = null;
                }
                this.contentCase_ = 9;
                onChanged();
                return this.secretDeleteReponseBuilder_;
            }

            @Override // com.xpeng.upso.proxy.PSOProtocol.RequestResponseOrBuilder
            public int getResult() {
                return this.result_;
            }

            public Builder setResult(int value) {
                this.result_ = value;
                onChanged();
                return this;
            }

            public Builder clearResult() {
                this.result_ = 0;
                onChanged();
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public final Builder setUnknownFields(final UnknownFieldSet unknownFields) {
                return (Builder) super.setUnknownFields(unknownFields);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public final Builder mergeUnknownFields(final UnknownFieldSet unknownFields) {
                return (Builder) super.mergeUnknownFields(unknownFields);
            }
        }

        public static RequestResponse getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<RequestResponse> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<RequestResponse> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public RequestResponse getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    public static Descriptors.FileDescriptor getDescriptor() {
        return descriptor;
    }

    static {
        String[] descriptorData = {"\n\u0012proto/pso_v1.proto\u0012\bprotocol\"»\u0001\n\fIdentityInfo\u0012\f\n\u0004role\u0018\u0001 \u0001(\r\u0012\u000f\n\u0007version\u0018\u0002 \u0001(\r\u0012\u0013\n\u000bdevice_type\u0018\u0003 \u0001(\t\u0012\u0011\n\tdevice_id\u0018\u0004 \u0001(\t\u0012\f\n\u0004mask\u0018\u0005 \u0001(\r\u0012\u0012\n\nhas_secret\u0018\u0006 \u0001(\r\u0012\u0010\n\bcar_type\u0018\u0007 \u0001(\t\u0012\f\n\u0004user\u0018\b \u0001(\r\u0012\u0010\n\brom_info\u0018\t \u0001(\t\u0012\u0010\n\bplatform\u0018\n \u0001(\t\"9\n\nSecretAuth\u0012\r\n\u0005index\u0018\u0001 \u0001(\r\u0012\f\n\u0004cont\u0018\u0002 \u0001(\r\u0012\u000e\n\u0006result\u0018\u0003 \u0003(\t\"%\n\u0014SecretPresetResponse\u0012\r\n\u0005index\u0018\u0001 \u0001(\r\"\u001d\n\fSecretDelete\u0012\r\n\u0005index\u0018\u0001 \u0001(\r\"Ï\u0001\n\u0006Secret\u0012)\n\u0004type\u0018\u0001 \u0001(\u000e2\u001b.protocol.Secret.SecretType\u0012\f\n\u0004name\u0018\u0002 \u0001(\t\u0012\r\n\u0005index\u0018\u0003 \u0001(\u0003\u0012\u0013\n\u000bcreate_time\u0018\u0004 \u0001(\u0003\u0012\u000e\n\u0006secret\u0018\u0005 \u0001(\t\u0012\f\n\u0004sign\u0018\u0006 \u0001(\t\"J\n\nSecretType\u0012\u0010\n\fKEY_TYPE_AES\u0010\u0000\u0012\u0018\n\u0014KEY_TYPE_CERTIFICATE\u0010\u0001\u0012\u0010\n\fKEY_TYPE_TBD\u0010\u0002\"M\n\fSecretPreset\u0012\r\n\u0005index\u0018\u0001 \u0001(\r\u0012\f\n\u0004cont\u0018\u0002 \u0001(\r\u0012 \n\u0006secret\u0018\u0003 \u0003(\u000b2\u0010.protocol.Secret\"5\n\u0014SecretDeleteResponse\u0012\r\n\u0005index\u0018\u0001 \u0001(\r\u0012\u000e\n\u0006status\u0018\u0002 \u0001(\r\"\u0083\u0006\n\u000fRequestResponse\u0012;\n\fmessage_type\u0018\u0001 \u0001(\u000e2%.protocol.RequestResponse.MessageType\u0012\r\n\u0005sn_id\u0018\u0002 \u0001(\t\u0012\u0010\n\bsequence\u0018\u0003 \u0001(\r\u0012/\n\ridentity_info\u0018\u0004 \u0001(\u000b2\u0016.protocol.IdentityInfoH\u0000\u0012,\n\fsecrett_auth\u0018\u0005 \u0001(\u000b2\u0014.protocol.SecretAuthH\u0000\u0012/\n\rsecret_preset\u0018\u0006 \u0001(\u000b2\u0016.protocol.SecretPresetH\u0000\u0012/\n\rsecret_delete\u0018\u0007 \u0001(\u000b2\u0016.protocol.SecretDeleteH\u0000\u0012@\n\u0016secret_preset_response\u0018\b \u0001(\u000b2\u001e.protocol.SecretPresetResponseH\u0000\u0012?\n\u0015secret_delete_reponse\u0018\t \u0001(\u000b2\u001e.protocol.SecretDeleteResponseH\u0000\u0012\u000e\n\u0006result\u0018\n \u0001(\r\"²\u0002\n\u000bMessageType\u0012\u000b\n\u0007XP_INFO\u0010\u0000\u0012\u0014\n\u0010XP_SECRET_PRESET\u0010\u0001\u0012\u001a\n\u0016XP_SECRET_ENCRYPT_AUTH\u0010\u0002\u0012\u001a\n\u0016XP_SECRET_DECRYPT_AUTH\u0010\u0003\u0012\u0014\n\u0010XP_SECRET_DELETE\u0010\u0004\u0012\u0014\n\u0010XP_INFO_RESPONSE\u0010\u0005\u0012\u001d\n\u0019XP_SECRET_PRESET_RESPONSE\u0010\u0006\u0012#\n\u001fXP_SECRET_ENCRYPT_AUTH_RESPONSE\u0010\u0007\u0012#\n\u001fXP_SECRET_DECRYPT_AUTH_RESPONSE\u0010\b\u0012\u001d\n\u0019XP_SECRET_DELETE_RESPONSE\u0010\t\u0012\u0014\n\u0010XP_SECRET_RESULT\u0010\nB\t\n\u0007contentB#\n\u0014com.xpeng.upso.proxyB\u000bPSOProtocolb\u0006proto3"};
        descriptor = Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(descriptorData, new Descriptors.FileDescriptor[0]);
    }
}
