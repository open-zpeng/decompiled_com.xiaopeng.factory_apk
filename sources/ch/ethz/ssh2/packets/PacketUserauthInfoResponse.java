package ch.ethz.ssh2.packets;
/* loaded from: classes.dex */
public class PacketUserauthInfoResponse {
    byte[] payload;
    String[] responses;

    public PacketUserauthInfoResponse(String[] responses) {
        this.responses = responses;
    }

    public byte[] getPayload() {
        if (this.payload == null) {
            TypesWriter tw = new TypesWriter();
            tw.writeByte(61);
            tw.writeUINT32(this.responses.length);
            int i = 0;
            while (true) {
                String[] strArr = this.responses;
                if (i >= strArr.length) {
                    break;
                }
                tw.writeString(strArr[i]);
                i++;
            }
            this.payload = tw.getBytes();
        }
        return this.payload;
    }
}
