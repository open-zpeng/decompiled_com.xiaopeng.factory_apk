package cn.hutool.core.text;

import cn.hutool.core.lang.hash.MurmurHash;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;
/* loaded from: classes.dex */
public class Simhash {
    private final int bitNum;
    private final int fracBitNum;
    private final int fracCount;
    private final int hammingThresh;
    private final StampedLock lock;
    private final List<Map<String, List<Long>>> storage;

    public Simhash() {
        this(4, 3);
    }

    public Simhash(int fracCount, int hammingThresh) {
        this.bitNum = 64;
        this.lock = new StampedLock();
        this.fracCount = fracCount;
        this.fracBitNum = 64 / fracCount;
        this.hammingThresh = hammingThresh;
        this.storage = new ArrayList(fracCount);
        for (int i = 0; i < fracCount; i++) {
            this.storage.add(new HashMap());
        }
    }

    public long hash(Collection<? extends CharSequence> segList) {
        getClass();
        int[] weight = new int[64];
        for (CharSequence seg : segList) {
            long wordHash = MurmurHash.hash64(seg);
            for (int i = 0; i < 64; i++) {
                if (((wordHash >> i) & 1) == 1) {
                    weight[i] = weight[i] + 1;
                } else {
                    weight[i] = weight[i] - 1;
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < 64; i2++) {
            sb.append(weight[i2] > 0 ? 1 : 0);
        }
        return new BigInteger(sb.toString(), 2).longValue();
    }

    public boolean equals(Collection<? extends CharSequence> segList) {
        long simhash = hash(segList);
        List<String> fracList = splitSimhash(Long.valueOf(simhash));
        int hammingThresh = this.hammingThresh;
        long stamp = this.lock.readLock();
        for (int i = 0; i < this.fracCount; i++) {
            try {
                String frac = fracList.get(i);
                Map<String, List<Long>> fracMap = this.storage.get(i);
                if (fracMap.containsKey(frac)) {
                    for (Long simhash2 : fracMap.get(frac)) {
                        if (hamming(Long.valueOf(simhash), simhash2) < hammingThresh) {
                            this.lock.unlockRead(stamp);
                            return true;
                        }
                    }
                    continue;
                }
            } finally {
                this.lock.unlockRead(stamp);
            }
        }
        return false;
    }

    public void store(Long simhash) {
        int fracCount = this.fracCount;
        List<Map<String, List<Long>>> storage = this.storage;
        List<String> lFrac = splitSimhash(simhash);
        long stamp = this.lock.writeLock();
        for (int i = 0; i < fracCount; i++) {
            try {
                String frac = lFrac.get(i);
                Map<String, List<Long>> fracMap = storage.get(i);
                if (fracMap.containsKey(frac)) {
                    fracMap.get(frac).add(simhash);
                } else {
                    List<Long> ls = new ArrayList<>();
                    ls.add(simhash);
                    fracMap.put(frac, ls);
                }
            } finally {
                this.lock.unlockWrite(stamp);
            }
        }
    }

    private int hamming(Long s1, Long s2) {
        getClass();
        int dis = 0;
        for (int i = 0; i < 64; i++) {
            if (((s1.longValue() >> i) & 1) != (1 & (s2.longValue() >> i))) {
                dis++;
            }
        }
        return dis;
    }

    private List<String> splitSimhash(Long simhash) {
        getClass();
        int fracBitNum = this.fracBitNum;
        List<String> ls = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 64; i++) {
            sb.append((simhash.longValue() >> i) & 1);
            if ((i + 1) % fracBitNum == 0) {
                ls.add(sb.toString());
                sb.setLength(0);
            }
        }
        return ls;
    }
}
