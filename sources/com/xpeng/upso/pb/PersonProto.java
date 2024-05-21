package com.xpeng.upso.pb;

import com.activeandroid.annotation.Table;
import com.google.protobuf.AbstractParser;
import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Descriptors;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.Parser;
import com.google.protobuf.UninitializedMessageException;
import com.google.protobuf.UnknownFieldSet;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
/* loaded from: classes2.dex */
public final class PersonProto {
    private static Descriptors.FileDescriptor descriptor;
    private static final Descriptors.Descriptor internal_static_com_xpeng_upso_pb_Person_descriptor = getDescriptor().getMessageTypes().get(0);
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_com_xpeng_upso_pb_Person_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_com_xpeng_upso_pb_Person_descriptor, new String[]{"Name", Table.DEFAULT_ID_NAME, "Email", "Name", Table.DEFAULT_ID_NAME, "Email"});

    /* loaded from: classes2.dex */
    public interface PersonOrBuilder extends MessageOrBuilder {
        String getEmail();

        ByteString getEmailBytes();

        int getId();

        String getName();

        ByteString getNameBytes();

        boolean hasEmail();

        boolean hasId();

        boolean hasName();
    }

    private PersonProto() {
    }

    public static void registerAllExtensions(ExtensionRegistryLite registry) {
    }

    public static void registerAllExtensions(ExtensionRegistry registry) {
        registerAllExtensions((ExtensionRegistryLite) registry);
    }

    /* loaded from: classes2.dex */
    public static final class Person extends GeneratedMessageV3 implements PersonOrBuilder {
        public static final int EMAIL_FIELD_NUMBER = 3;
        public static final int ID_FIELD_NUMBER = 2;
        public static final int NAME_FIELD_NUMBER = 1;
        private static final long serialVersionUID = 0;
        private int bitField0_;
        private volatile Object email_;
        private int id_;
        private byte memoizedIsInitialized;
        private volatile Object name_;
        private static final Person DEFAULT_INSTANCE = new Person();
        private static final Parser<Person> PARSER = new AbstractParser<Person>() { // from class: com.xpeng.upso.pb.PersonProto.Person.1
            @Override // com.google.protobuf.Parser
            public Person parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new Person(input, extensionRegistry);
            }
        };

        private Person(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private Person() {
            this.memoizedIsInitialized = (byte) -1;
            this.name_ = "";
            this.email_ = "";
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
            return new Person();
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private Person(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
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
                            } else if (tag == 10) {
                                String s = input.readStringRequireUtf8();
                                this.bitField0_ |= 1;
                                this.name_ = s;
                            } else if (tag == 16) {
                                this.bitField0_ |= 2;
                                this.id_ = input.readInt32();
                            } else if (tag == 26) {
                                String s2 = input.readStringRequireUtf8();
                                this.bitField0_ |= 4;
                                this.email_ = s2;
                            } else if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                                done = true;
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
            return PersonProto.internal_static_com_xpeng_upso_pb_Person_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return PersonProto.internal_static_com_xpeng_upso_pb_Person_fieldAccessorTable.ensureFieldAccessorsInitialized(Person.class, Builder.class);
        }

        @Override // com.xpeng.upso.pb.PersonProto.PersonOrBuilder
        public boolean hasName() {
            return (this.bitField0_ & 1) != 0;
        }

        @Override // com.xpeng.upso.pb.PersonProto.PersonOrBuilder
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

        @Override // com.xpeng.upso.pb.PersonProto.PersonOrBuilder
        public ByteString getNameBytes() {
            Object ref = this.name_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.name_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // com.xpeng.upso.pb.PersonProto.PersonOrBuilder
        public boolean hasId() {
            return (this.bitField0_ & 2) != 0;
        }

        @Override // com.xpeng.upso.pb.PersonProto.PersonOrBuilder
        public int getId() {
            return this.id_;
        }

        @Override // com.xpeng.upso.pb.PersonProto.PersonOrBuilder
        public boolean hasEmail() {
            return (this.bitField0_ & 4) != 0;
        }

        @Override // com.xpeng.upso.pb.PersonProto.PersonOrBuilder
        public String getEmail() {
            Object ref = this.email_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            this.email_ = s;
            return s;
        }

        @Override // com.xpeng.upso.pb.PersonProto.PersonOrBuilder
        public ByteString getEmailBytes() {
            Object ref = this.email_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.email_ = b;
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
            if ((this.bitField0_ & 1) != 0) {
                GeneratedMessageV3.writeString(output, 1, this.name_);
            }
            if ((this.bitField0_ & 2) != 0) {
                output.writeInt32(2, this.id_);
            }
            if ((this.bitField0_ & 4) != 0) {
                GeneratedMessageV3.writeString(output, 3, this.email_);
            }
            this.unknownFields.writeTo(output);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSize;
            if (size != -1) {
                return size;
            }
            int size2 = (this.bitField0_ & 1) != 0 ? 0 + GeneratedMessageV3.computeStringSize(1, this.name_) : 0;
            if ((this.bitField0_ & 2) != 0) {
                size2 += CodedOutputStream.computeInt32Size(2, this.id_);
            }
            if ((this.bitField0_ & 4) != 0) {
                size2 += GeneratedMessageV3.computeStringSize(3, this.email_);
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
            if (!(obj instanceof Person)) {
                return super.equals(obj);
            }
            Person other = (Person) obj;
            if (hasName() != other.hasName()) {
                return false;
            }
            if ((!hasName() || getName().equals(other.getName())) && hasId() == other.hasId()) {
                if ((!hasId() || getId() == other.getId()) && hasEmail() == other.hasEmail()) {
                    return (!hasEmail() || getEmail().equals(other.getEmail())) && this.unknownFields.equals(other.unknownFields);
                }
                return false;
            }
            return false;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = (41 * 19) + getDescriptor().hashCode();
            if (hasName()) {
                hash = (((hash * 37) + 1) * 53) + getName().hashCode();
            }
            if (hasId()) {
                hash = (((hash * 37) + 2) * 53) + getId();
            }
            if (hasEmail()) {
                hash = (((hash * 37) + 3) * 53) + getEmail().hashCode();
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        public static Person parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Person parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Person parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Person parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Person parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Person parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Person parseFrom(InputStream input) throws IOException {
            return (Person) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static Person parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (Person) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        public static Person parseDelimitedFrom(InputStream input) throws IOException {
            return (Person) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
        }

        public static Person parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (Person) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
        }

        public static Person parseFrom(CodedInputStream input) throws IOException {
            return (Person) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static Person parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (Person) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(Person prototype) {
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
        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements PersonOrBuilder {
            private int bitField0_;
            private Object email_;
            private int id_;
            private Object name_;

            public static final Descriptors.Descriptor getDescriptor() {
                return PersonProto.internal_static_com_xpeng_upso_pb_Person_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return PersonProto.internal_static_com_xpeng_upso_pb_Person_fieldAccessorTable.ensureFieldAccessorsInitialized(Person.class, Builder.class);
            }

            private Builder() {
                this.name_ = "";
                this.email_ = "";
                maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessageV3.BuilderParent parent) {
                super(parent);
                this.name_ = "";
                this.email_ = "";
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = Person.alwaysUseFieldBuilders;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.name_ = "";
                this.bitField0_ &= -2;
                this.id_ = 0;
                this.bitField0_ &= -3;
                this.email_ = "";
                this.bitField0_ &= -5;
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return PersonProto.internal_static_com_xpeng_upso_pb_Person_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public Person getDefaultInstanceForType() {
                return Person.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Person build() {
                Person result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException((Message) result);
                }
                return result;
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Person buildPartial() {
                Person result = new Person(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) != 0) {
                    to_bitField0_ = 0 | 1;
                }
                result.name_ = this.name_;
                if ((from_bitField0_ & 2) != 0) {
                    result.id_ = this.id_;
                    to_bitField0_ |= 2;
                }
                if ((from_bitField0_ & 4) != 0) {
                    to_bitField0_ |= 4;
                }
                result.email_ = this.email_;
                result.bitField0_ = to_bitField0_;
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
                if (other instanceof Person) {
                    return mergeFrom((Person) other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(Person other) {
                if (other == Person.getDefaultInstance()) {
                    return this;
                }
                if (other.hasName()) {
                    this.bitField0_ |= 1;
                    this.name_ = other.name_;
                    onChanged();
                }
                if (other.hasId()) {
                    setId(other.getId());
                }
                if (other.hasEmail()) {
                    this.bitField0_ |= 4;
                    this.email_ = other.email_;
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
                Person parsedMessage = null;
                try {
                    try {
                        parsedMessage = (Person) Person.PARSER.parsePartialFrom(input, extensionRegistry);
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        Person person = (Person) e.getUnfinishedMessage();
                        throw e.unwrapIOException();
                    }
                } finally {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                }
            }

            @Override // com.xpeng.upso.pb.PersonProto.PersonOrBuilder
            public boolean hasName() {
                return (this.bitField0_ & 1) != 0;
            }

            @Override // com.xpeng.upso.pb.PersonProto.PersonOrBuilder
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

            @Override // com.xpeng.upso.pb.PersonProto.PersonOrBuilder
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
                this.bitField0_ |= 1;
                this.name_ = value;
                onChanged();
                return this;
            }

            public Builder clearName() {
                this.bitField0_ &= -2;
                this.name_ = Person.getDefaultInstance().getName();
                onChanged();
                return this;
            }

            public Builder setNameBytes(ByteString value) {
                if (value != null) {
                    Person.checkByteStringIsUtf8(value);
                    this.bitField0_ |= 1;
                    this.name_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.xpeng.upso.pb.PersonProto.PersonOrBuilder
            public boolean hasId() {
                return (this.bitField0_ & 2) != 0;
            }

            @Override // com.xpeng.upso.pb.PersonProto.PersonOrBuilder
            public int getId() {
                return this.id_;
            }

            public Builder setId(int value) {
                this.bitField0_ |= 2;
                this.id_ = value;
                onChanged();
                return this;
            }

            public Builder clearId() {
                this.bitField0_ &= -3;
                this.id_ = 0;
                onChanged();
                return this;
            }

            @Override // com.xpeng.upso.pb.PersonProto.PersonOrBuilder
            public boolean hasEmail() {
                return (this.bitField0_ & 4) != 0;
            }

            @Override // com.xpeng.upso.pb.PersonProto.PersonOrBuilder
            public String getEmail() {
                Object ref = this.email_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    this.email_ = s;
                    return s;
                }
                return (String) ref;
            }

            @Override // com.xpeng.upso.pb.PersonProto.PersonOrBuilder
            public ByteString getEmailBytes() {
                Object ref = this.email_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.email_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            public Builder setEmail(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 4;
                this.email_ = value;
                onChanged();
                return this;
            }

            public Builder clearEmail() {
                this.bitField0_ &= -5;
                this.email_ = Person.getDefaultInstance().getEmail();
                onChanged();
                return this;
            }

            public Builder setEmailBytes(ByteString value) {
                if (value != null) {
                    Person.checkByteStringIsUtf8(value);
                    this.bitField0_ |= 4;
                    this.email_ = value;
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

        public static Person getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<Person> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<Person> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public Person getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    public static Descriptors.FileDescriptor getDescriptor() {
        return descriptor;
    }

    static {
        String[] descriptorData = {"\n\u0012proto/Person.proto\u0012\u0011com.xpeng.upso.pb\"Z\n\u0006Person\u0012\u0011\n\u0004name\u0018\u0001 \u0001(\tH\u0000\u0088\u0001\u0001\u0012\u000f\n\u0002id\u0018\u0002 \u0001(\u0005H\u0001\u0088\u0001\u0001\u0012\u0012\n\u0005email\u0018\u0003 \u0001(\tH\u0002\u0088\u0001\u0001B\u0007\n\u0005_nameB\u0005\n\u0003_idB\b\n\u0006_emailB \n\u0011com.xpeng.upso.pbB\u000bPersonProtob\u0006proto3"};
        descriptor = Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(descriptorData, new Descriptors.FileDescriptor[0]);
    }
}
