package com.nforetek.bt.aidl;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;
/* loaded from: classes.dex */
public class UsbBluetoothDevice implements Parcelable {
    public static final Parcelable.Creator<UsbBluetoothDevice> CREATOR = new Parcelable.Creator<UsbBluetoothDevice>() { // from class: com.nforetek.bt.aidl.UsbBluetoothDevice.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UsbBluetoothDevice createFromParcel(Parcel in) {
            return new UsbBluetoothDevice(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UsbBluetoothDevice[] newArray(int size) {
            return new UsbBluetoothDevice[size];
        }
    };
    private String mAddress;
    private String mName;

    public UsbBluetoothDevice(String mAddress, String mName) {
        this.mAddress = mAddress;
        this.mName = mName;
    }

    protected UsbBluetoothDevice(Parcel in) {
        this.mAddress = in.readStringNoHelper();
        this.mName = in.readStringNoHelper();
    }

    public String getAddress() {
        return this.mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringNoHelper(this.mAddress);
        dest.writeStringNoHelper(this.mName);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UsbBluetoothDevice that = (UsbBluetoothDevice) o;
        return this.mAddress.equals(that.mAddress);
    }

    public int hashCode() {
        return Objects.hash(this.mAddress);
    }
}
