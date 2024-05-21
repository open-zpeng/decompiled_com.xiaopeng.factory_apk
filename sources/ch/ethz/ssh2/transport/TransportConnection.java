package ch.ethz.ssh2.transport;

import ch.ethz.ssh2.crypto.cipher.BlockCipher;
import ch.ethz.ssh2.crypto.cipher.CipherInputStream;
import ch.ethz.ssh2.crypto.cipher.CipherOutputStream;
import ch.ethz.ssh2.crypto.cipher.NullCipher;
import ch.ethz.ssh2.crypto.digest.MAC;
import ch.ethz.ssh2.log.Logger;
import ch.ethz.ssh2.packets.Packets;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
/* loaded from: classes.dex */
public class TransportConnection {
    static /* synthetic */ Class class$0;
    private static final Logger log;
    CipherInputStream cis;
    CipherOutputStream cos;
    ClientServerHello csh;
    MAC recv_mac;
    byte[] recv_mac_buffer;
    byte[] recv_mac_buffer_cmp;
    final SecureRandom rnd;
    MAC send_mac;
    byte[] send_mac_buffer;
    int send_seq_number = 0;
    int recv_seq_number = 0;
    boolean useRandomPadding = false;
    int send_padd_blocksize = 8;
    int recv_padd_blocksize = 8;
    final byte[] send_padding_buffer = new byte[256];
    final byte[] send_packet_header_buffer = new byte[5];
    final byte[] recv_padding_buffer = new byte[256];
    final byte[] recv_packet_header_buffer = new byte[5];
    boolean recv_packet_header_present = false;

    static {
        Class<?> cls = class$0;
        if (cls == null) {
            try {
                cls = Class.forName("ch.ethz.ssh2.transport.TransportConnection");
                class$0 = cls;
            } catch (ClassNotFoundException e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        log = Logger.getLogger(cls);
    }

    public TransportConnection(InputStream is, OutputStream os, SecureRandom rnd) {
        this.cis = new CipherInputStream(new NullCipher(), is);
        this.cos = new CipherOutputStream(new NullCipher(), os);
        this.rnd = rnd;
    }

    public void changeRecvCipher(BlockCipher bc, MAC mac) {
        this.cis.changeCipher(bc);
        this.recv_mac = mac;
        this.recv_mac_buffer = mac != null ? new byte[mac.size()] : null;
        this.recv_mac_buffer_cmp = mac != null ? new byte[mac.size()] : null;
        this.recv_padd_blocksize = bc.getBlockSize();
        if (this.recv_padd_blocksize < 8) {
            this.recv_padd_blocksize = 8;
        }
    }

    public void changeSendCipher(BlockCipher bc, MAC mac) {
        if (!(bc instanceof NullCipher)) {
            this.useRandomPadding = true;
        }
        this.cos.changeCipher(bc);
        this.send_mac = mac;
        this.send_mac_buffer = mac != null ? new byte[mac.size()] : null;
        this.send_padd_blocksize = bc.getBlockSize();
        if (this.send_padd_blocksize < 8) {
            this.send_padd_blocksize = 8;
        }
    }

    public void sendMessage(byte[] message) throws IOException {
        sendMessage(message, 0, message.length, 0);
    }

    public void sendMessage(byte[] message, int off, int len) throws IOException {
        sendMessage(message, off, len, 0);
    }

    public int getPacketOverheadEstimate() {
        return (this.send_padd_blocksize - 1) + 9 + this.send_mac_buffer.length;
    }

    public void sendMessage(byte[] message, int off, int len, int padd) throws IOException {
        if (padd < 4) {
            padd = 4;
        } else if (padd > 64) {
            padd = 64;
        }
        int packet_len = len + 5 + padd;
        int i = this.send_padd_blocksize;
        int slack = packet_len % i;
        if (slack != 0) {
            packet_len += i - slack;
        }
        if (packet_len < 16) {
            packet_len = 16;
        }
        int padd_len = packet_len - (len + 5);
        if (this.useRandomPadding) {
            for (int i2 = 0; i2 < padd_len; i2 += 4) {
                int r = this.rnd.nextInt();
                byte[] bArr = this.send_padding_buffer;
                bArr[i2] = (byte) r;
                bArr[i2 + 1] = (byte) (r >> 8);
                bArr[i2 + 2] = (byte) (r >> 16);
                bArr[i2 + 3] = (byte) (r >> 24);
            }
        } else {
            for (int i3 = 0; i3 < padd_len; i3++) {
                this.send_padding_buffer[i3] = 0;
            }
        }
        byte[] bArr2 = this.send_packet_header_buffer;
        bArr2[0] = (byte) ((packet_len - 4) >> 24);
        bArr2[1] = (byte) ((packet_len - 4) >> 16);
        bArr2[2] = (byte) ((packet_len - 4) >> 8);
        bArr2[3] = (byte) (packet_len - 4);
        bArr2[4] = (byte) padd_len;
        this.cos.write(bArr2, 0, 5);
        this.cos.write(message, off, len);
        this.cos.write(this.send_padding_buffer, 0, padd_len);
        MAC mac = this.send_mac;
        if (mac != null) {
            mac.initMac(this.send_seq_number);
            this.send_mac.update(this.send_packet_header_buffer, 0, 5);
            this.send_mac.update(message, off, len);
            this.send_mac.update(this.send_padding_buffer, 0, padd_len);
            this.send_mac.getMac(this.send_mac_buffer, 0);
            CipherOutputStream cipherOutputStream = this.cos;
            byte[] bArr3 = this.send_mac_buffer;
            cipherOutputStream.writePlain(bArr3, 0, bArr3.length);
        }
        this.cos.flush();
        if (log.isEnabled()) {
            Logger logger = log;
            StringBuffer stringBuffer = new StringBuffer("Sent ");
            stringBuffer.append(Packets.getMessageName(message[off] & 255));
            stringBuffer.append(" ");
            stringBuffer.append(len);
            stringBuffer.append(" bytes payload");
            logger.log(90, stringBuffer.toString());
        }
        this.send_seq_number++;
    }

    public int peekNextMessageLength() throws IOException {
        if (!this.recv_packet_header_present) {
            this.cis.read(this.recv_packet_header_buffer, 0, 5);
            this.recv_packet_header_present = true;
        }
        byte[] bArr = this.recv_packet_header_buffer;
        int packet_length = ((bArr[0] & 255) << 24) | ((bArr[1] & 255) << 16) | ((bArr[2] & 255) << 8) | (bArr[3] & 255);
        int padding_length = bArr[4] & 255;
        if (packet_length > 35000 || packet_length < 12) {
            StringBuffer stringBuffer = new StringBuffer("Illegal packet size! (");
            stringBuffer.append(packet_length);
            stringBuffer.append(")");
            throw new IOException(stringBuffer.toString());
        }
        int payload_length = (packet_length - padding_length) - 1;
        if (payload_length >= 0) {
            return payload_length;
        }
        StringBuffer stringBuffer2 = new StringBuffer("Illegal padding_length in packet from remote (");
        stringBuffer2.append(padding_length);
        stringBuffer2.append(")");
        throw new IOException(stringBuffer2.toString());
    }

    public int receiveMessage(byte[] buffer, int off, int len) throws IOException {
        if (this.recv_packet_header_present) {
            this.recv_packet_header_present = false;
        } else {
            this.cis.read(this.recv_packet_header_buffer, 0, 5);
        }
        byte[] bArr = this.recv_packet_header_buffer;
        int packet_length = ((bArr[0] & 255) << 24) | ((bArr[1] & 255) << 16) | ((bArr[2] & 255) << 8) | (bArr[3] & 255);
        int padding_length = bArr[4] & 255;
        if (packet_length > 35000 || packet_length < 12) {
            StringBuffer stringBuffer = new StringBuffer("Illegal packet size! (");
            stringBuffer.append(packet_length);
            stringBuffer.append(")");
            throw new IOException(stringBuffer.toString());
        }
        int payload_length = (packet_length - padding_length) - 1;
        if (payload_length < 0) {
            StringBuffer stringBuffer2 = new StringBuffer("Illegal padding_length in packet from remote (");
            stringBuffer2.append(padding_length);
            stringBuffer2.append(")");
            throw new IOException(stringBuffer2.toString());
        } else if (payload_length >= len) {
            StringBuffer stringBuffer3 = new StringBuffer("Receive buffer too small (");
            stringBuffer3.append(len);
            stringBuffer3.append(", need ");
            stringBuffer3.append(payload_length);
            stringBuffer3.append(")");
            throw new IOException(stringBuffer3.toString());
        } else {
            this.cis.read(buffer, off, payload_length);
            this.cis.read(this.recv_padding_buffer, 0, padding_length);
            if (this.recv_mac != null) {
                CipherInputStream cipherInputStream = this.cis;
                byte[] bArr2 = this.recv_mac_buffer;
                cipherInputStream.readPlain(bArr2, 0, bArr2.length);
                this.recv_mac.initMac(this.recv_seq_number);
                this.recv_mac.update(this.recv_packet_header_buffer, 0, 5);
                this.recv_mac.update(buffer, off, payload_length);
                this.recv_mac.update(this.recv_padding_buffer, 0, padding_length);
                this.recv_mac.getMac(this.recv_mac_buffer_cmp, 0);
                int i = 0;
                while (true) {
                    byte[] bArr3 = this.recv_mac_buffer;
                    if (i >= bArr3.length) {
                        break;
                    } else if (bArr3[i] != this.recv_mac_buffer_cmp[i]) {
                        throw new IOException("Remote sent corrupt MAC.");
                    } else {
                        i++;
                    }
                }
            }
            int i2 = this.recv_seq_number;
            this.recv_seq_number = i2 + 1;
            if (log.isEnabled()) {
                Logger logger = log;
                StringBuffer stringBuffer4 = new StringBuffer("Received ");
                stringBuffer4.append(Packets.getMessageName(buffer[off] & 255));
                stringBuffer4.append(" ");
                stringBuffer4.append(payload_length);
                stringBuffer4.append(" bytes payload");
                logger.log(90, stringBuffer4.toString());
            }
            return payload_length;
        }
    }
}
