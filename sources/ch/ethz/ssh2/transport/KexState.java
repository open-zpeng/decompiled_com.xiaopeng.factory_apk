package ch.ethz.ssh2.transport;

import ch.ethz.ssh2.DHGexParameters;
import ch.ethz.ssh2.crypto.dh.DhExchange;
import ch.ethz.ssh2.crypto.dh.DhGroupExchange;
import ch.ethz.ssh2.packets.PacketKexInit;
import java.math.BigInteger;
/* loaded from: classes.dex */
public class KexState {
    public byte[] H;
    public BigInteger K;
    public DHGexParameters dhgexParameters;
    public DhGroupExchange dhgx;
    public DhExchange dhx;
    public byte[] hostkey;
    public PacketKexInit localKEX;
    public NegotiatedParameters np;
    public PacketKexInit remoteKEX;
    public int state = 0;
}
