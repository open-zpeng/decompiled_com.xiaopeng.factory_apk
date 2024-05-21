package xp.hardware.dab;

import android.os.Parcel;
import android.os.Parcelable;
import cn.hutool.core.text.CharPool;
/* loaded from: classes2.dex */
public class DabInfo implements Parcelable {
    public static final Parcelable.Creator<DabInfo> CREATOR = new Parcelable.Creator<DabInfo>() { // from class: xp.hardware.dab.DabInfo.1
        @Override // android.os.Parcelable.Creator
        public DabInfo createFromParcel(Parcel in) {
            return new DabInfo(in);
        }

        @Override // android.os.Parcelable.Creator
        public DabInfo[] newArray(int size) {
            return new DabInfo[size];
        }
    };
    public String albumText;
    public String artistText;
    public int componentID;
    public int dabStatus;
    public int ensembleID;
    public int frequenceID;
    public int iPtyCode;
    public int index;
    public int level;
    public int linkSource;
    public int primaryFlag;
    public String programLabel;
    public int programTime;
    public String psName;
    public int radioSourceType;
    public String radioText;
    public int serviceID;
    public String strComponentLabel;
    public String strEnsembleLabel;
    public String strFrequence;
    public String strServiceLabel;
    public String titleText;

    public DabInfo() {
        this.index = 0;
        this.radioSourceType = -1;
        this.iPtyCode = 0;
        this.ensembleID = 0;
        this.serviceID = -1;
        this.componentID = 65535;
        this.frequenceID = 0;
        this.primaryFlag = -1;
        this.dabStatus = 0;
        this.programTime = -1;
        this.linkSource = 0;
        this.level = 0;
        this.strServiceLabel = "";
        this.strComponentLabel = "";
        this.strEnsembleLabel = "";
        this.strFrequence = "--";
        this.programLabel = "";
        this.radioText = "";
        this.titleText = "";
        this.albumText = "";
        this.artistText = "";
        this.psName = "";
    }

    private DabInfo(Parcel in) {
        this.index = in.readInt();
        this.radioSourceType = in.readInt();
        this.iPtyCode = in.readInt();
        this.frequenceID = in.readInt();
        this.ensembleID = in.readInt();
        this.serviceID = in.readInt();
        this.componentID = in.readInt();
        this.primaryFlag = in.readInt();
        this.dabStatus = in.readInt();
        this.programTime = in.readInt();
        this.linkSource = in.readInt();
        this.level = in.readInt();
        this.strServiceLabel = in.readString();
        this.strFrequence = in.readString();
        this.strComponentLabel = in.readString();
        this.strEnsembleLabel = in.readString();
        this.programLabel = in.readString();
        this.radioText = in.readString();
        this.titleText = in.readString();
        this.albumText = in.readString();
        this.artistText = in.readString();
        this.psName = in.readString();
    }

    public void reset() {
        this.index = 0;
        this.radioSourceType = -1;
        this.iPtyCode = 0;
        this.ensembleID = 0;
        this.serviceID = -1;
        this.componentID = 255;
        this.frequenceID = 0;
        this.primaryFlag = -1;
        this.dabStatus = 0;
        this.programTime = -1;
        this.linkSource = 0;
        this.level = 0;
        this.strServiceLabel = "";
        this.strComponentLabel = "";
        this.strEnsembleLabel = "";
        this.strFrequence = "--";
        this.programLabel = "";
        this.radioText = "";
        this.titleText = "";
        this.albumText = "";
        this.artistText = "";
        this.psName = "";
    }

    public int getIndex() {
        return this.index;
    }

    public int getRadioSourceType() {
        return this.radioSourceType;
    }

    public int getPtyCode() {
        return this.iPtyCode;
    }

    public int getFrequenceID() {
        return this.frequenceID;
    }

    public int getEnsembleID() {
        return this.ensembleID;
    }

    public int getServiceID() {
        return this.serviceID;
    }

    public int getComponentID() {
        return this.componentID;
    }

    public int getPrimaryFlag() {
        return this.primaryFlag;
    }

    public int getDabStatus() {
        return this.dabStatus;
    }

    public int getProgramTime() {
        return this.programTime;
    }

    public int getLinkSource() {
        return this.linkSource;
    }

    public int getLevel() {
        return this.level;
    }

    public String getStrServiceLabel() {
        return this.strServiceLabel;
    }

    public String getStrFrequence() {
        return this.strFrequence;
    }

    public String getStrComponentLabel() {
        return this.strComponentLabel;
    }

    public String getStrEnsembleLabel() {
        return this.strEnsembleLabel;
    }

    public String getProgramLabel() {
        return this.programLabel;
    }

    public String getRadioText() {
        return this.radioText;
    }

    public String getTitleText() {
        return this.titleText;
    }

    public String getArtistText() {
        return this.artistText;
    }

    public String getAlbumText() {
        return this.albumText;
    }

    public String getPsName() {
        return this.psName;
    }

    public String getLogoPath() {
        return "/data/stationlogo/" + this.ensembleID + "_" + this.serviceID + ".png";
    }

    public String toString() {
        return "DabInfo{index=" + this.index + ", radioSourceType= " + this.radioSourceType + ", iPtyCode=" + this.iPtyCode + ", frequenceId=" + this.frequenceID + ", ensembleId=" + this.ensembleID + ", serviceId=" + this.serviceID + ", componentId=" + this.componentID + ", primaryFlag=" + this.primaryFlag + ", dabStatus=" + this.dabStatus + ", linkSource=" + this.linkSource + ", level=" + this.level + ", strServiceLabel='" + this.strServiceLabel + CharPool.SINGLE_QUOTE + ", strFrequence='" + this.strFrequence + CharPool.SINGLE_QUOTE + ", strComponentLabel='" + this.strComponentLabel + CharPool.SINGLE_QUOTE + '}';
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.index);
        parcel.writeInt(this.radioSourceType);
        parcel.writeInt(this.iPtyCode);
        parcel.writeInt(this.frequenceID);
        parcel.writeInt(this.ensembleID);
        parcel.writeInt(this.serviceID);
        parcel.writeInt(this.componentID);
        parcel.writeInt(this.primaryFlag);
        parcel.writeInt(this.dabStatus);
        parcel.writeInt(this.programTime);
        parcel.writeInt(this.linkSource);
        parcel.writeInt(this.level);
        parcel.writeString(this.strServiceLabel);
        parcel.writeString(this.strFrequence);
        parcel.writeString(this.strComponentLabel);
        parcel.writeString(this.strEnsembleLabel);
        parcel.writeString(this.programLabel);
        parcel.writeString(this.radioText);
        parcel.writeString(this.titleText);
        parcel.writeString(this.albumText);
        parcel.writeString(this.artistText);
        parcel.writeString(this.psName);
    }
}
