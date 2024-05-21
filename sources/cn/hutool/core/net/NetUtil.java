package cn.hutool.core.net;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.EnumerationIter;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.JNDIUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Authenticator;
import java.net.DatagramSocket;
import java.net.HttpCookie;
import java.net.IDN;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
/* loaded from: classes.dex */
public class NetUtil {
    public static final String LOCAL_IP = "127.0.0.1";
    public static final int PORT_RANGE_MAX = 65535;
    public static final int PORT_RANGE_MIN = 1024;
    public static String localhostName;

    public static String longToIpv4(long longIP) {
        return Ipv4Util.longToIpv4(longIP);
    }

    public static long ipv4ToLong(String strIP) {
        return Ipv4Util.ipv4ToLong(strIP);
    }

    public static BigInteger ipv6ToBitInteger(String IPv6Str) {
        try {
            InetAddress address = InetAddress.getByName(IPv6Str);
            if (address instanceof Inet6Address) {
                return new BigInteger(1, address.getAddress());
            }
            return null;
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public static String bigIntegerToIPv6(BigInteger bigInteger) {
        try {
            return InetAddress.getByAddress(bigInteger.toByteArray()).toString().substring(1);
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public static boolean isUsableLocalPort(int port) {
        if (isValidPort(port)) {
            try {
                ServerSocket ss = new ServerSocket(port);
                ss.setReuseAddress(true);
                ss.close();
                try {
                    DatagramSocket ds = new DatagramSocket(port);
                    ds.setReuseAddress(true);
                    ds.close();
                    return true;
                } catch (IOException e) {
                    return false;
                }
            } catch (IOException e2) {
                return false;
            }
        }
        return false;
    }

    public static boolean isValidPort(int port) {
        return port >= 0 && port <= 65535;
    }

    public static int getUsableLocalPort() {
        return getUsableLocalPort(1024);
    }

    public static int getUsableLocalPort(int minPort) {
        return getUsableLocalPort(minPort, 65535);
    }

    public static int getUsableLocalPort(int minPort, int maxPort) {
        int maxPortExclude = maxPort + 1;
        for (int i = minPort; i < maxPortExclude; i++) {
            int randomPort = RandomUtil.randomInt(minPort, maxPortExclude);
            if (isUsableLocalPort(randomPort)) {
                return randomPort;
            }
        }
        throw new UtilException("Could not find an available port in the range [{}, {}] after {} attempts", Integer.valueOf(minPort), Integer.valueOf(maxPort), Integer.valueOf(maxPort - minPort));
    }

    public static TreeSet<Integer> getUsableLocalPorts(int numRequested, int minPort, int maxPort) {
        TreeSet<Integer> availablePorts = new TreeSet<>();
        int attemptCount = 0;
        while (true) {
            attemptCount++;
            if (attemptCount > numRequested + 100 || availablePorts.size() >= numRequested) {
                break;
            }
            availablePorts.add(Integer.valueOf(getUsableLocalPort(minPort, maxPort)));
        }
        if (availablePorts.size() != numRequested) {
            throw new UtilException("Could not find {} available  ports in the range [{}, {}]", Integer.valueOf(numRequested), Integer.valueOf(minPort), Integer.valueOf(maxPort));
        }
        return availablePorts;
    }

    public static boolean isInnerIP(String ipAddress) {
        long ipNum = ipv4ToLong(ipAddress);
        long aBegin = ipv4ToLong("10.0.0.0");
        long aEnd = ipv4ToLong("10.255.255.255");
        long bBegin = ipv4ToLong("172.16.0.0");
        long bEnd = ipv4ToLong("172.31.255.255");
        long cBegin = ipv4ToLong("192.168.0.0");
        long cEnd = ipv4ToLong("192.168.255.255");
        if (!isInner(ipNum, aBegin, aEnd) && !isInner(ipNum, bBegin, bEnd) && !isInner(ipNum, cBegin, cEnd)) {
            if (!"127.0.0.1".equals(ipAddress)) {
                return false;
            }
        }
        return true;
    }

    public static String toAbsoluteUrl(String absoluteBasePath, String relativePath) {
        try {
            URL absoluteUrl = new URL(absoluteBasePath);
            return new URL(absoluteUrl, relativePath).toString();
        } catch (Exception e) {
            throw new UtilException(e, "To absolute url [{}] base [{}] error!", relativePath, absoluteBasePath);
        }
    }

    public static String hideIpPart(String ip) {
        StringBuilder builder = StrUtil.builder(ip.length());
        builder.append((CharSequence) ip, 0, ip.lastIndexOf(".") + 1);
        builder.append("*");
        return builder.toString();
    }

    public static String hideIpPart(long ip) {
        return hideIpPart(longToIpv4(ip));
    }

    public static InetSocketAddress buildInetSocketAddress(String host, int defaultPort) {
        String destHost;
        int port;
        if (StrUtil.isBlank(host)) {
            host = "127.0.0.1";
        }
        int index = host.indexOf(":");
        if (index != -1) {
            destHost = host.substring(0, index);
            port = Integer.parseInt(host.substring(index + 1));
        } else {
            destHost = host;
            port = defaultPort;
        }
        return new InetSocketAddress(destHost, port);
    }

    public static String getIpByHost(String hostName) {
        try {
            return InetAddress.getByName(hostName).getHostAddress();
        } catch (UnknownHostException e) {
            return hostName;
        }
    }

    public static NetworkInterface getNetworkInterface(String name) {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = networkInterfaces.nextElement();
                if (netInterface != null && name.equals(netInterface.getName())) {
                    return netInterface;
                }
            }
            return null;
        } catch (SocketException e) {
            return null;
        }
    }

    public static Collection<NetworkInterface> getNetworkInterfaces() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            return CollUtil.addAll((Collection) new ArrayList(), (Enumeration) networkInterfaces);
        } catch (SocketException e) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$localIpv4s$0(InetAddress t) {
        return t instanceof Inet4Address;
    }

    public static LinkedHashSet<String> localIpv4s() {
        LinkedHashSet<InetAddress> localAddressList = localAddressList(new Filter() { // from class: cn.hutool.core.net.-$$Lambda$NetUtil$-joFEMJCxKbo2rTKrkBqQfMTeFw
            @Override // cn.hutool.core.lang.Filter
            public final boolean accept(Object obj) {
                return NetUtil.lambda$localIpv4s$0((InetAddress) obj);
            }
        });
        return toIpList(localAddressList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$localIpv6s$1(InetAddress t) {
        return t instanceof Inet6Address;
    }

    public static LinkedHashSet<String> localIpv6s() {
        LinkedHashSet<InetAddress> localAddressList = localAddressList(new Filter() { // from class: cn.hutool.core.net.-$$Lambda$NetUtil$_txqBJZo8rjKZnD4l3Btjyf3neU
            @Override // cn.hutool.core.lang.Filter
            public final boolean accept(Object obj) {
                return NetUtil.lambda$localIpv6s$1((InetAddress) obj);
            }
        });
        return toIpList(localAddressList);
    }

    public static LinkedHashSet<String> toIpList(Set<InetAddress> addressList) {
        LinkedHashSet<String> ipSet = new LinkedHashSet<>();
        for (InetAddress address : addressList) {
            ipSet.add(address.getHostAddress());
        }
        return ipSet;
    }

    public static LinkedHashSet<String> localIps() {
        LinkedHashSet<InetAddress> localAddressList = localAddressList(null);
        return toIpList(localAddressList);
    }

    public static LinkedHashSet<InetAddress> localAddressList(Filter<InetAddress> addressFilter) {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            if (networkInterfaces == null) {
                throw new UtilException("Get network interface error!");
            }
            LinkedHashSet<InetAddress> ipSet = new LinkedHashSet<>();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (inetAddress != null && (addressFilter == null || addressFilter.accept(inetAddress))) {
                        ipSet.add(inetAddress);
                    }
                }
            }
            return ipSet;
        } catch (SocketException e) {
            throw new UtilException(e);
        }
    }

    public static String getLocalhostStr() {
        InetAddress localhost = getLocalhost();
        if (localhost != null) {
            return localhost.getHostAddress();
        }
        return null;
    }

    public static InetAddress getLocalhost() {
        LinkedHashSet<InetAddress> localAddressList = localAddressList(new Filter() { // from class: cn.hutool.core.net.-$$Lambda$NetUtil$NYwa8FXTAy1aoUesNFGTEDep5cU
            @Override // cn.hutool.core.lang.Filter
            public final boolean accept(Object obj) {
                return NetUtil.lambda$getLocalhost$2((InetAddress) obj);
            }
        });
        if (CollUtil.isNotEmpty((Collection<?>) localAddressList)) {
            InetAddress address2 = null;
            Iterator<InetAddress> it = localAddressList.iterator();
            while (it.hasNext()) {
                InetAddress inetAddress = it.next();
                if (!inetAddress.isSiteLocalAddress()) {
                    return inetAddress;
                }
                if (address2 == null) {
                    address2 = inetAddress;
                }
            }
            if (address2 != null) {
                return address2;
            }
        }
        try {
            InetAddress address22 = InetAddress.getLocalHost();
            return address22;
        } catch (UnknownHostException e) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$getLocalhost$2(InetAddress address) {
        return !address.isLoopbackAddress() && (address instanceof Inet4Address);
    }

    public static String getLocalMacAddress() {
        return getMacAddress(getLocalhost());
    }

    public static String getMacAddress(InetAddress inetAddress) {
        return getMacAddress(inetAddress, "-");
    }

    public static String getMacAddress(InetAddress inetAddress, String separator) {
        byte[] mac;
        if (inetAddress == null || (mac = getHardwareAddress(inetAddress)) == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append(separator);
            }
            String s = Integer.toHexString(mac[i] & 255);
            sb.append(s.length() == 1 ? 0 + s : s);
        }
        return sb.toString();
    }

    public static byte[] getLocalHardwareAddress() {
        return getHardwareAddress(getLocalhost());
    }

    public static byte[] getHardwareAddress(InetAddress inetAddress) {
        if (inetAddress == null) {
            return null;
        }
        try {
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(inetAddress);
            if (networkInterface == null) {
                return null;
            }
            return networkInterface.getHardwareAddress();
        } catch (SocketException e) {
            throw new UtilException(e);
        }
    }

    public static String getLocalHostName() {
        if (StrUtil.isNotBlank(localhostName)) {
            return localhostName;
        }
        InetAddress localhost = getLocalhost();
        if (localhost != null) {
            String name = localhost.getHostName();
            if (StrUtil.isEmpty(name)) {
                name = localhost.getHostAddress();
            }
            localhostName = name;
        }
        return localhostName;
    }

    public static InetSocketAddress createAddress(String host, int port) {
        if (StrUtil.isBlank(host)) {
            return new InetSocketAddress(port);
        }
        return new InetSocketAddress(host, port);
    }

    public static void netCat(String host, int port, boolean isBlock, ByteBuffer data) throws IORuntimeException {
        try {
            SocketChannel channel = SocketChannel.open(createAddress(host, port));
            channel.configureBlocking(isBlock);
            channel.write(data);
            channel.close();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static void netCat(String host, int port, byte[] data) throws IORuntimeException {
        OutputStream out = null;
        try {
            try {
                Socket socket = new Socket(host, port);
                try {
                    out = socket.getOutputStream();
                    out.write(data);
                    out.flush();
                    socket.close();
                } catch (Throwable th) {
                    try {
                        throw th;
                    } catch (Throwable th2) {
                        try {
                            socket.close();
                        } catch (Throwable th3) {
                            th.addSuppressed(th3);
                        }
                        throw th2;
                    }
                }
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        } finally {
            IoUtil.close((Closeable) out);
        }
    }

    public static boolean isInRange(String ip, String cidr) {
        String[] ips = StrUtil.splitToArray((CharSequence) ip, '.');
        int ipAddr = (Integer.parseInt(ips[0]) << 24) | (Integer.parseInt(ips[1]) << 16) | (Integer.parseInt(ips[2]) << 8) | Integer.parseInt(ips[3]);
        int type = Integer.parseInt(cidr.replaceAll(".*/", ""));
        int mask = (-1) << (32 - type);
        String cidrIp = cidr.replaceAll("/.*", "");
        String[] cidrIps = cidrIp.split("\\.");
        int cidrIpAddr = (Integer.parseInt(cidrIps[2]) << 8) | (Integer.parseInt(cidrIps[0]) << 24) | (Integer.parseInt(cidrIps[1]) << 16) | Integer.parseInt(cidrIps[3]);
        return (ipAddr & mask) == (cidrIpAddr & mask);
    }

    public static String idnToASCII(String unicode) {
        return IDN.toASCII(unicode);
    }

    public static String getMultistageReverseProxyIp(String ip) {
        if (ip != null && ip.indexOf(",") > 0) {
            String[] ips = ip.trim().split(",");
            for (String subIp : ips) {
                if (!isUnknown(subIp)) {
                    return subIp;
                }
            }
            return ip;
        }
        return ip;
    }

    public static boolean isUnknown(String checkString) {
        return StrUtil.isBlank(checkString) || "unknown".equalsIgnoreCase(checkString);
    }

    public static boolean ping(String ip) {
        return ping(ip, 200);
    }

    public static boolean ping(String ip, int timeout) {
        try {
            return InetAddress.getByName(ip).isReachable(timeout);
        } catch (Exception e) {
            return false;
        }
    }

    public static List<HttpCookie> parseCookies(String cookieStr) {
        if (StrUtil.isBlank(cookieStr)) {
            return Collections.emptyList();
        }
        return HttpCookie.parse(cookieStr);
    }

    public static boolean isOpen(InetSocketAddress address, int timeout) {
        try {
            Socket sc = new Socket();
            sc.connect(address, timeout);
            sc.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void setGlobalAuthenticator(String user, char[] pass) {
        setGlobalAuthenticator(new UserPassAuthenticator(user, pass));
    }

    public static void setGlobalAuthenticator(Authenticator authenticator) {
        Authenticator.setDefault(authenticator);
    }

    public static List<String> getDnsInfo(String hostName, String... attrNames) {
        String uri = StrUtil.addPrefixIfNot(hostName, "dns:");
        Attributes attributes = JNDIUtil.getAttributes(uri, attrNames);
        List<String> infos = new ArrayList<>();
        Iterator it = new EnumerationIter(attributes.getAll()).iterator();
        while (it.hasNext()) {
            Attribute attribute = (Attribute) it.next();
            try {
                infos.add((String) attribute.get());
            } catch (NamingException e) {
            }
        }
        return infos;
    }

    private static boolean isInner(long userIp, long begin, long end) {
        return userIp >= begin && userIp <= end;
    }
}
