package xp.hardware.dab;
/* loaded from: classes2.dex */
public class DabFreqInfo {
    private int frequenceID = -1;
    private String strFrequence = "";

    public String getstrFrequence() {
        return this.strFrequence;
    }

    public int getFrequenceID() {
        return this.frequenceID;
    }

    public void setStrFrequenceEnsemble(String strFrequence, int frequenceID) {
        this.strFrequence = strFrequence;
        this.frequenceID = frequenceID;
    }
}
