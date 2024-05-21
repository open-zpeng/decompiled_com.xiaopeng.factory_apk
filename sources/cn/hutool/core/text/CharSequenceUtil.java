package cn.hutool.core.text;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.comparator.VersionComparator;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.IntConsumer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* loaded from: classes.dex */
public class CharSequenceUtil {
    public static final String EMPTY = "";
    public static final int INDEX_NOT_FOUND = -1;
    public static final String NULL = "null";
    public static final String SPACE = " ";

    public static boolean isBlank(CharSequence str) {
        int length;
        if (str == null || (length = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (!CharUtil.isBlankChar(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }

    public static boolean hasBlank(CharSequence... strs) {
        if (ArrayUtil.isEmpty((Object[]) strs)) {
            return true;
        }
        for (CharSequence str : strs) {
            if (isBlank(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAllBlank(CharSequence... strs) {
        if (ArrayUtil.isEmpty((Object[]) strs)) {
            return true;
        }
        for (CharSequence str : strs) {
            if (isNotBlank(str)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence str) {
        return !isEmpty(str);
    }

    public static String emptyIfNull(CharSequence str) {
        return nullToEmpty(str);
    }

    public static String nullToEmpty(CharSequence str) {
        return nullToDefault(str, "");
    }

    public static String nullToDefault(CharSequence str, String defaultStr) {
        return str == null ? defaultStr : str.toString();
    }

    public static String emptyToDefault(CharSequence str, String defaultStr) {
        return isEmpty(str) ? defaultStr : str.toString();
    }

    public static String blankToDefault(CharSequence str, String defaultStr) {
        return isBlank(str) ? defaultStr : str.toString();
    }

    public static String emptyToNull(CharSequence str) {
        if (isEmpty(str)) {
            return null;
        }
        return str.toString();
    }

    public static boolean hasEmpty(CharSequence... strs) {
        if (ArrayUtil.isEmpty((Object[]) strs)) {
            return true;
        }
        for (CharSequence str : strs) {
            if (isEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAllEmpty(CharSequence... strs) {
        if (ArrayUtil.isEmpty((Object[]) strs)) {
            return true;
        }
        for (CharSequence str : strs) {
            if (isNotEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAllNotEmpty(CharSequence... args) {
        return !hasEmpty(args);
    }

    public static boolean isAllNotBlank(CharSequence... args) {
        return !hasBlank(args);
    }

    public static boolean isNullOrUndefined(CharSequence str) {
        if (str == null) {
            return true;
        }
        return isNullOrUndefinedStr(str);
    }

    public static boolean isEmptyOrUndefined(CharSequence str) {
        if (isEmpty(str)) {
            return true;
        }
        return isNullOrUndefinedStr(str);
    }

    public static boolean isBlankOrUndefined(CharSequence str) {
        if (isBlank(str)) {
            return true;
        }
        return isNullOrUndefinedStr(str);
    }

    private static boolean isNullOrUndefinedStr(CharSequence str) {
        String strString = str.toString().trim();
        return NULL.equals(strString) || "undefined".equals(strString);
    }

    public static String trim(CharSequence str) {
        if (str == null) {
            return null;
        }
        return trim(str, 0);
    }

    public static String trimToEmpty(CharSequence str) {
        return str == null ? "" : trim(str);
    }

    public static String trimToNull(CharSequence str) {
        String trimStr = trim(str);
        if ("".equals(trimStr)) {
            return null;
        }
        return trimStr;
    }

    public static String trimStart(CharSequence str) {
        return trim(str, -1);
    }

    public static String trimEnd(CharSequence str) {
        return trim(str, 1);
    }

    public static String trim(CharSequence str, int mode) {
        return trim(str, mode, new Predicate() { // from class: cn.hutool.core.text.-$$Lambda$eyjfHrW56d90MXqb3v7q6gu39HI
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return CharUtil.isBlankChar(((Character) obj).charValue());
            }
        });
    }

    public static String trim(CharSequence str, int mode, Predicate<Character> predicate) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        int start = 0;
        int end = length;
        if (mode <= 0) {
            while (start < end && predicate.test(Character.valueOf(str.charAt(start)))) {
                start++;
            }
        }
        if (mode >= 0) {
            while (start < end && predicate.test(Character.valueOf(str.charAt(end - 1)))) {
                end--;
            }
        }
        if (start > 0 || end < length) {
            String result = str.toString().substring(start, end);
            return result;
        }
        String result2 = str.toString();
        return result2;
    }

    public static boolean startWith(CharSequence str, char c) {
        return !isEmpty(str) && c == str.charAt(0);
    }

    public static boolean startWith(CharSequence str, CharSequence prefix, boolean ignoreCase) {
        return startWith(str, prefix, ignoreCase, false);
    }

    public static boolean startWith(CharSequence str, CharSequence prefix, boolean ignoreCase, boolean ignoreEquals) {
        boolean isStartWith;
        if (str == null || prefix == null) {
            if (ignoreEquals) {
                return str == null && prefix == null;
            }
            return false;
        }
        if (ignoreCase) {
            isStartWith = str.toString().toLowerCase().startsWith(prefix.toString().toLowerCase());
        } else {
            isStartWith = str.toString().startsWith(prefix.toString());
        }
        if (isStartWith) {
            return (ignoreEquals && equals(str, prefix, ignoreCase)) ? false : true;
        }
        return false;
    }

    public static boolean startWith(CharSequence str, CharSequence prefix) {
        return startWith(str, prefix, false);
    }

    public static boolean startWithIgnoreEquals(CharSequence str, CharSequence prefix) {
        return startWith(str, prefix, false, true);
    }

    public static boolean startWithIgnoreCase(CharSequence str, CharSequence prefix) {
        return startWith(str, prefix, true);
    }

    public static boolean startWithAny(CharSequence str, CharSequence... prefixes) {
        if (isEmpty(str) || ArrayUtil.isEmpty((Object[]) prefixes)) {
            return false;
        }
        for (CharSequence suffix : prefixes) {
            if (startWith(str, suffix, false)) {
                return true;
            }
        }
        return false;
    }

    public static boolean endWith(CharSequence str, char c) {
        return !isEmpty(str) && c == str.charAt(str.length() - 1);
    }

    public static boolean endWith(CharSequence str, CharSequence suffix, boolean isIgnoreCase) {
        if (str == null || suffix == null) {
            return str == null && suffix == null;
        } else if (isIgnoreCase) {
            return str.toString().toLowerCase().endsWith(suffix.toString().toLowerCase());
        } else {
            return str.toString().endsWith(suffix.toString());
        }
    }

    public static boolean endWith(CharSequence str, CharSequence suffix) {
        return endWith(str, suffix, false);
    }

    public static boolean endWithIgnoreCase(CharSequence str, CharSequence suffix) {
        return endWith(str, suffix, true);
    }

    public static boolean endWithAny(CharSequence str, CharSequence... suffixes) {
        if (isEmpty(str) || ArrayUtil.isEmpty((Object[]) suffixes)) {
            return false;
        }
        for (CharSequence suffix : suffixes) {
            if (endWith(str, suffix, false)) {
                return true;
            }
        }
        return false;
    }

    public static boolean endWithAnyIgnoreCase(CharSequence str, CharSequence... suffixes) {
        if (isEmpty(str) || ArrayUtil.isEmpty((Object[]) suffixes)) {
            return false;
        }
        for (CharSequence suffix : suffixes) {
            if (endWith(str, suffix, true)) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(CharSequence str, char searchChar) {
        return indexOf(str, searchChar) > -1;
    }

    public static boolean contains(CharSequence str, CharSequence searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        return str.toString().contains(searchStr);
    }

    public static boolean containsAny(CharSequence str, CharSequence... testStrs) {
        return getContainsStr(str, testStrs) != null;
    }

    public static boolean containsAny(CharSequence str, char... testChars) {
        if (!isEmpty(str)) {
            int len = str.length();
            for (int i = 0; i < len; i++) {
                if (ArrayUtil.contains(testChars, str.charAt(i))) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static boolean containsOnly(CharSequence str, char... testChars) {
        if (!isEmpty(str)) {
            int len = str.length();
            for (int i = 0; i < len; i++) {
                if (!ArrayUtil.contains(testChars, str.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    public static boolean containsBlank(CharSequence str) {
        int length;
        if (str == null || (length = str.length()) == 0) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (CharUtil.isBlankChar(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static String getContainsStr(CharSequence str, CharSequence... testStrs) {
        if (isEmpty(str) || ArrayUtil.isEmpty((Object[]) testStrs)) {
            return null;
        }
        for (CharSequence checkStr : testStrs) {
            if (str.toString().contains(checkStr)) {
                return checkStr.toString();
            }
        }
        return null;
    }

    public static boolean containsIgnoreCase(CharSequence str, CharSequence testStr) {
        if (str == null) {
            return testStr == null;
        }
        return str.toString().toLowerCase().contains(testStr.toString().toLowerCase());
    }

    public static boolean containsAnyIgnoreCase(CharSequence str, CharSequence... testStrs) {
        return getContainsStrIgnoreCase(str, testStrs) != null;
    }

    public static String getContainsStrIgnoreCase(CharSequence str, CharSequence... testStrs) {
        if (isEmpty(str) || ArrayUtil.isEmpty((Object[]) testStrs)) {
            return null;
        }
        for (CharSequence testStr : testStrs) {
            if (containsIgnoreCase(str, testStr)) {
                return testStr.toString();
            }
        }
        return null;
    }

    public static int indexOf(CharSequence str, char searchChar) {
        return indexOf(str, searchChar, 0);
    }

    public static int indexOf(CharSequence str, char searchChar, int start) {
        if (str instanceof String) {
            return ((String) str).indexOf(searchChar, start);
        }
        return indexOf(str, searchChar, start, -1);
    }

    public static int indexOf(CharSequence str, char searchChar, int start, int end) {
        if (isEmpty(str)) {
            return -1;
        }
        int len = str.length();
        start = (start < 0 || start > len) ? 0 : 0;
        if (end > len || end < 0) {
            end = len;
        }
        for (int i = start; i < end; i++) {
            if (str.charAt(i) == searchChar) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
        return indexOfIgnoreCase(str, searchStr, 0);
    }

    public static int indexOfIgnoreCase(CharSequence str, CharSequence searchStr, int fromIndex) {
        return indexOf(str, searchStr, fromIndex, true);
    }

    public static int indexOf(CharSequence str, CharSequence searchStr, int fromIndex, boolean ignoreCase) {
        if (str == null || searchStr == null) {
            return -1;
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        int endLimit = (str.length() - searchStr.length()) + 1;
        if (fromIndex > endLimit) {
            return -1;
        }
        if (searchStr.length() == 0) {
            return fromIndex;
        }
        if (!ignoreCase) {
            return str.toString().indexOf(searchStr.toString(), fromIndex);
        }
        for (int i = fromIndex; i < endLimit; i++) {
            if (isSubEquals(str, i, searchStr, 0, searchStr.length(), true)) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
        return lastIndexOfIgnoreCase(str, searchStr, str.length());
    }

    public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr, int fromIndex) {
        return lastIndexOf(str, searchStr, fromIndex, true);
    }

    public static int lastIndexOf(CharSequence str, CharSequence searchStr, int fromIndex, boolean ignoreCase) {
        if (str == null || searchStr == null) {
            return -1;
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        int fromIndex2 = Math.min(fromIndex, str.length());
        if (searchStr.length() == 0) {
            return fromIndex2;
        }
        if (!ignoreCase) {
            return str.toString().lastIndexOf(searchStr.toString(), fromIndex2);
        }
        for (int i = fromIndex2; i >= 0; i--) {
            if (isSubEquals(str, i, searchStr, 0, searchStr.length(), true)) {
                return i;
            }
        }
        return -1;
    }

    public static int ordinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal) {
        if (str == null || searchStr == null || ordinal <= 0) {
            return -1;
        }
        if (searchStr.length() == 0) {
            return 0;
        }
        int found = 0;
        int index = -1;
        do {
            index = indexOf(str, searchStr, index + 1, false);
            if (index < 0) {
                return index;
            }
            found++;
        } while (found < ordinal);
        return index;
    }

    public static String removeAll(CharSequence str, CharSequence strToRemove) {
        if (isEmpty(str) || isEmpty(strToRemove)) {
            return str(str);
        }
        return str.toString().replace(strToRemove, "");
    }

    public static String removeAny(CharSequence str, CharSequence... strsToRemove) {
        String result = str(str);
        if (isNotEmpty(str)) {
            for (CharSequence strToRemove : strsToRemove) {
                result = removeAll(result, strToRemove);
            }
        }
        return result;
    }

    public static String removeAll(CharSequence str, char... chars) {
        if (str == null || ArrayUtil.isEmpty(chars)) {
            return str(str);
        }
        int len = str.length();
        if (len == 0) {
            return str(str);
        }
        StringBuilder builder = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (!ArrayUtil.contains(chars, c)) {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    public static String removeAllLineBreaks(CharSequence str) {
        return removeAll(str, '\r', '\n');
    }

    public static String removePreAndLowerFirst(CharSequence str, int preLength) {
        if (str == null) {
            return null;
        }
        if (str.length() > preLength) {
            char first = Character.toLowerCase(str.charAt(preLength));
            if (str.length() > preLength + 1) {
                return first + str.toString().substring(preLength + 1);
            }
            return String.valueOf(first);
        }
        return str.toString();
    }

    public static String removePreAndLowerFirst(CharSequence str, CharSequence prefix) {
        return lowerFirst(removePrefix(str, prefix));
    }

    public static String removePrefix(CharSequence str, CharSequence prefix) {
        if (isEmpty(str) || isEmpty(prefix)) {
            return str(str);
        }
        String str2 = str.toString();
        if (str2.startsWith(prefix.toString())) {
            return subSuf(str2, prefix.length());
        }
        return str2;
    }

    public static String removePrefixIgnoreCase(CharSequence str, CharSequence prefix) {
        if (isEmpty(str) || isEmpty(prefix)) {
            return str(str);
        }
        String str2 = str.toString();
        if (str2.toLowerCase().startsWith(prefix.toString().toLowerCase())) {
            return subSuf(str2, prefix.length());
        }
        return str2;
    }

    public static String removeSuffix(CharSequence str, CharSequence suffix) {
        if (isEmpty(str) || isEmpty(suffix)) {
            return str(str);
        }
        String str2 = str.toString();
        if (str2.endsWith(suffix.toString())) {
            return subPre(str2, str2.length() - suffix.length());
        }
        return str2;
    }

    public static String removeSufAndLowerFirst(CharSequence str, CharSequence suffix) {
        return lowerFirst(removeSuffix(str, suffix));
    }

    public static String removeSuffixIgnoreCase(CharSequence str, CharSequence suffix) {
        if (isEmpty(str) || isEmpty(suffix)) {
            return str(str);
        }
        String str2 = str.toString();
        if (str2.toLowerCase().endsWith(suffix.toString().toLowerCase())) {
            return subPre(str2, str2.length() - suffix.length());
        }
        return str2;
    }

    public static String cleanBlank(CharSequence str) {
        return filter(str, new Filter() { // from class: cn.hutool.core.text.-$$Lambda$CharSequenceUtil$oZvrcEbN-egf-iv6z7wdU144De8
            @Override // cn.hutool.core.lang.Filter
            public final boolean accept(Object obj) {
                return CharSequenceUtil.lambda$cleanBlank$0((Character) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$cleanBlank$0(Character c) {
        return !CharUtil.isBlankChar(c.charValue());
    }

    public static String strip(CharSequence str, CharSequence prefixOrSuffix) {
        if (equals(str, prefixOrSuffix)) {
            return "";
        }
        return strip(str, prefixOrSuffix, prefixOrSuffix);
    }

    public static String strip(CharSequence str, CharSequence prefix, CharSequence suffix) {
        if (isEmpty(str)) {
            return str(str);
        }
        int from = 0;
        int to = str.length();
        String str2 = str.toString();
        if (startWith(str2, prefix)) {
            from = prefix.length();
        }
        if (endWith(str2, suffix)) {
            to -= suffix.length();
        }
        return str2.substring(Math.min(from, to), Math.max(from, to));
    }

    public static String stripIgnoreCase(CharSequence str, CharSequence prefixOrSuffix) {
        return stripIgnoreCase(str, prefixOrSuffix, prefixOrSuffix);
    }

    public static String stripIgnoreCase(CharSequence str, CharSequence prefix, CharSequence suffix) {
        if (isEmpty(str)) {
            return str(str);
        }
        int from = 0;
        int to = str.length();
        String str2 = str.toString();
        if (startWithIgnoreCase(str2, prefix)) {
            from = prefix.length();
        }
        if (endWithIgnoreCase(str2, suffix)) {
            to -= suffix.length();
        }
        return str2.substring(from, to);
    }

    public static String addPrefixIfNot(CharSequence str, CharSequence prefix) {
        return prependIfMissing(str, prefix, prefix);
    }

    public static String addSuffixIfNot(CharSequence str, CharSequence suffix) {
        return appendIfMissing(str, suffix, suffix);
    }

    public static long[] splitToLong(CharSequence str, char separator) {
        return (long[]) Convert.convert((Class<Object>) long[].class, (Object) splitTrim(str, separator));
    }

    public static long[] splitToLong(CharSequence str, CharSequence separator) {
        return (long[]) Convert.convert((Class<Object>) long[].class, (Object) splitTrim(str, separator));
    }

    public static int[] splitToInt(CharSequence str, char separator) {
        return (int[]) Convert.convert((Class<Object>) int[].class, (Object) splitTrim(str, separator));
    }

    public static int[] splitToInt(CharSequence str, CharSequence separator) {
        return (int[]) Convert.convert((Class<Object>) int[].class, (Object) splitTrim(str, separator));
    }

    public static List<String> split(CharSequence str, char separator) {
        return split(str, separator, 0);
    }

    public static String[] splitToArray(CharSequence str, CharSequence separator) {
        if (str != null) {
            return StrSplitter.splitToArray(str.toString(), str(separator), 0, false, false);
        }
        return new String[0];
    }

    public static String[] splitToArray(CharSequence str, char separator) {
        return splitToArray(str, separator, 0);
    }

    public static String[] splitToArray(CharSequence str, char separator, int limit) {
        if (str != null) {
            return StrSplitter.splitToArray(str.toString(), separator, limit, false, false);
        }
        return new String[0];
    }

    public static List<String> split(CharSequence str, char separator, int limit) {
        return split(str, separator, limit, false, false);
    }

    public static List<String> splitTrim(CharSequence str, char separator) {
        return splitTrim(str, separator, -1);
    }

    public static List<String> splitTrim(CharSequence str, CharSequence separator) {
        return splitTrim(str, separator, -1);
    }

    public static List<String> splitTrim(CharSequence str, char separator, int limit) {
        return split(str, separator, limit, true, true);
    }

    public static List<String> splitTrim(CharSequence str, CharSequence separator, int limit) {
        return split(str, separator, limit, true, true);
    }

    public static List<String> split(CharSequence str, char separator, boolean isTrim, boolean ignoreEmpty) {
        return split(str, separator, 0, isTrim, ignoreEmpty);
    }

    public static List<String> split(CharSequence str, char separator, int limit, boolean isTrim, boolean ignoreEmpty) {
        if (str == null) {
            return new ArrayList(0);
        }
        return StrSplitter.split(str.toString(), separator, limit, isTrim, ignoreEmpty);
    }

    public static List<String> split(CharSequence str, CharSequence separator) {
        return split(str, separator, false, false);
    }

    public static List<String> split(CharSequence str, CharSequence separator, boolean isTrim, boolean ignoreEmpty) {
        return split(str, separator, 0, isTrim, ignoreEmpty);
    }

    public static List<String> split(CharSequence str, CharSequence separator, int limit, boolean isTrim, boolean ignoreEmpty) {
        if (str == null) {
            return new ArrayList(0);
        }
        String separatorStr = separator == null ? null : separator.toString();
        return StrSplitter.split(str.toString(), separatorStr, limit, isTrim, ignoreEmpty);
    }

    public static String[] split(CharSequence str, int len) {
        if (str == null) {
            return new String[0];
        }
        return StrSplitter.splitByLength(str.toString(), len);
    }

    public static String[] cut(CharSequence str, int partLength) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len < partLength) {
            return new String[]{str.toString()};
        }
        int part = NumberUtil.count(len, partLength);
        String[] array = new String[part];
        String str2 = str.toString();
        int i = 0;
        while (i < part) {
            array[i] = str2.substring(i * partLength, i == part + (-1) ? len : (i * partLength) + partLength);
            i++;
        }
        return array;
    }

    public static String sub(CharSequence str, int fromIndexInclude, int toIndexExclude) {
        if (isEmpty(str)) {
            return str(str);
        }
        int len = str.length();
        if (fromIndexInclude < 0) {
            fromIndexInclude += len;
            if (fromIndexInclude < 0) {
                fromIndexInclude = 0;
            }
        } else if (fromIndexInclude > len) {
            fromIndexInclude = len;
        }
        if (toIndexExclude < 0) {
            toIndexExclude += len;
            if (toIndexExclude < 0) {
                toIndexExclude = len;
            }
        } else if (toIndexExclude > len) {
            toIndexExclude = len;
        }
        if (toIndexExclude < fromIndexInclude) {
            int tmp = fromIndexInclude;
            fromIndexInclude = toIndexExclude;
            toIndexExclude = tmp;
        }
        if (fromIndexInclude == toIndexExclude) {
            return "";
        }
        return str.toString().substring(fromIndexInclude, toIndexExclude);
    }

    public static String subByCodePoint(CharSequence str, int fromIndex, int toIndex) {
        if (isEmpty(str)) {
            return str(str);
        }
        if (fromIndex < 0 || fromIndex > toIndex) {
            throw new IllegalArgumentException();
        }
        if (fromIndex == toIndex) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        int subLen = toIndex - fromIndex;
        str.toString().codePoints().skip(fromIndex).limit(subLen).forEach(new IntConsumer() { // from class: cn.hutool.core.text.-$$Lambda$CharSequenceUtil$uI7pf-H_h9Ss1Qh08OcVFZVfGGQ
            @Override // java.util.function.IntConsumer
            public final void accept(int i) {
                sb.append(Character.toChars(i));
            }
        });
        return sb.toString();
    }

    public static String subPreGbk(CharSequence str, int len, CharSequence suffix) {
        if (isEmpty(str)) {
            return str(str);
        }
        int counterOfDoubleByte = 0;
        byte[] b = str.toString().getBytes(CharsetUtil.CHARSET_GBK);
        if (b.length <= len) {
            return str.toString();
        }
        for (int i = 0; i < len; i++) {
            if (b[i] < 0) {
                counterOfDoubleByte++;
            }
        }
        int i2 = counterOfDoubleByte % 2;
        if (i2 != 0) {
            len++;
        }
        return new String(b, 0, len, CharsetUtil.CHARSET_GBK) + ((Object) suffix);
    }

    public static String subPre(CharSequence string, int toIndexExclude) {
        return sub(string, 0, toIndexExclude);
    }

    public static String subSuf(CharSequence string, int fromIndex) {
        if (isEmpty(string)) {
            return null;
        }
        return sub(string, fromIndex, string.length());
    }

    public static String subSufByLength(CharSequence string, int length) {
        if (isEmpty(string)) {
            return null;
        }
        if (length <= 0) {
            return "";
        }
        return sub(string, -length, string.length());
    }

    public static String subWithLength(String input, int fromIndex, int length) {
        return sub(input, fromIndex, fromIndex + length);
    }

    public static String subBefore(CharSequence string, CharSequence separator, boolean isLastSeparator) {
        if (isEmpty(string) || separator == null) {
            if (string == null) {
                return null;
            }
            return string.toString();
        }
        String str = string.toString();
        String sep = separator.toString();
        if (sep.isEmpty()) {
            return "";
        }
        int pos = isLastSeparator ? str.lastIndexOf(sep) : str.indexOf(sep);
        if (-1 == pos) {
            return str;
        }
        return pos == 0 ? "" : str.substring(0, pos);
    }

    public static String subBefore(CharSequence string, char separator, boolean isLastSeparator) {
        if (isEmpty(string)) {
            if (string == null) {
                return null;
            }
            return "";
        }
        String str = string.toString();
        int pos = isLastSeparator ? str.lastIndexOf(separator) : str.indexOf(separator);
        if (-1 == pos) {
            return str;
        }
        return pos == 0 ? "" : str.substring(0, pos);
    }

    public static String subAfter(CharSequence string, CharSequence separator, boolean isLastSeparator) {
        if (isEmpty(string)) {
            if (string == null) {
                return null;
            }
            return "";
        } else if (separator == null) {
            return "";
        } else {
            String str = string.toString();
            String sep = separator.toString();
            int pos = isLastSeparator ? str.lastIndexOf(sep) : str.indexOf(sep);
            return (-1 == pos || string.length() + (-1) == pos) ? "" : str.substring(separator.length() + pos);
        }
    }

    public static String subAfter(CharSequence string, char separator, boolean isLastSeparator) {
        if (isEmpty(string)) {
            if (string == null) {
                return null;
            }
            return "";
        }
        String str = string.toString();
        int pos = isLastSeparator ? str.lastIndexOf(separator) : str.indexOf(separator);
        return -1 == pos ? "" : str.substring(pos + 1);
    }

    public static String subBetween(CharSequence str, CharSequence before, CharSequence after) {
        int end;
        if (str == null || before == null || after == null) {
            return null;
        }
        String str2 = str.toString();
        String before2 = before.toString();
        String after2 = after.toString();
        int start = str2.indexOf(before2);
        if (start == -1 || (end = str2.indexOf(after2, before2.length() + start)) == -1) {
            return null;
        }
        return str2.substring(before2.length() + start, end);
    }

    public static String subBetween(CharSequence str, CharSequence beforeAndAfter) {
        return subBetween(str, beforeAndAfter, beforeAndAfter);
    }

    public static String[] subBetweenAll(CharSequence str, CharSequence prefix, CharSequence suffix) {
        if (hasEmpty(str, prefix, suffix) || !contains(str, prefix)) {
            return new String[0];
        }
        List<String> result = new LinkedList<>();
        String[] split = splitToArray(str, prefix);
        if (prefix.equals(suffix)) {
            int length = split.length - 1;
            for (int i = 1; i < length; i += 2) {
                result.add(split[i]);
            }
        } else {
            for (String fragment : split) {
                int suffixIndex = fragment.indexOf(suffix.toString());
                if (suffixIndex > 0) {
                    result.add(fragment.substring(0, suffixIndex));
                }
            }
        }
        return (String[]) result.toArray(new String[0]);
    }

    public static String[] subBetweenAll(CharSequence str, CharSequence prefixAndSuffix) {
        return subBetweenAll(str, prefixAndSuffix, prefixAndSuffix);
    }

    public static String repeat(char c, int count) {
        if (count <= 0) {
            return "";
        }
        char[] result = new char[count];
        for (int i = 0; i < count; i++) {
            result[i] = c;
        }
        return new String(result);
    }

    public static String repeat(CharSequence str, int count) {
        if (str == null) {
            return null;
        }
        if (count <= 0 || str.length() == 0) {
            return "";
        }
        if (count == 1) {
            return str.toString();
        }
        int len = str.length();
        long longSize = len * count;
        int size = (int) longSize;
        if (size != longSize) {
            throw new ArrayIndexOutOfBoundsException("Required String length is too large: " + longSize);
        }
        char[] array = new char[size];
        str.toString().getChars(0, len, array, 0);
        int n = len;
        while (n < size - n) {
            System.arraycopy(array, 0, array, n, n);
            n <<= 1;
        }
        System.arraycopy(array, 0, array, n, size - n);
        return new String(array);
    }

    public static String repeatByLength(CharSequence str, int padLen) {
        if (str == null) {
            return null;
        }
        if (padLen <= 0) {
            return "";
        }
        int strLen = str.length();
        if (strLen == padLen) {
            return str.toString();
        }
        if (strLen > padLen) {
            return subPre(str, padLen);
        }
        char[] padding = new char[padLen];
        for (int i = 0; i < padLen; i++) {
            padding[i] = str.charAt(i % strLen);
        }
        return new String(padding);
    }

    public static String repeatAndJoin(CharSequence str, int count, CharSequence conjunction) {
        if (count <= 0) {
            return "";
        }
        StrBuilder builder = StrBuilder.create();
        boolean isFirst = true;
        while (true) {
            int count2 = count - 1;
            if (count > 0) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    boolean isFirst2 = isNotEmpty(conjunction);
                    if (isFirst2) {
                        builder.append(conjunction);
                    }
                }
                builder.append(str);
                count = count2;
            } else {
                return builder.toString();
            }
        }
    }

    public static boolean equals(CharSequence str1, CharSequence str2) {
        return equals(str1, str2, false);
    }

    public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
        return equals(str1, str2, true);
    }

    public static boolean equals(CharSequence str1, CharSequence str2, boolean ignoreCase) {
        if (str1 == null) {
            return str2 == null;
        } else if (str2 == null) {
            return false;
        } else {
            if (ignoreCase) {
                return str1.toString().equalsIgnoreCase(str2.toString());
            }
            return str1.toString().contentEquals(str2);
        }
    }

    public static boolean equalsAnyIgnoreCase(CharSequence str1, CharSequence... strs) {
        return equalsAny(str1, true, strs);
    }

    public static boolean equalsAny(CharSequence str1, CharSequence... strs) {
        return equalsAny(str1, false, strs);
    }

    public static boolean equalsAny(CharSequence str1, boolean ignoreCase, CharSequence... strs) {
        if (ArrayUtil.isEmpty((Object[]) strs)) {
            return false;
        }
        for (CharSequence str : strs) {
            if (equals(str1, str, ignoreCase)) {
                return true;
            }
        }
        return false;
    }

    public static boolean equalsCharAt(CharSequence str, int position, char c) {
        return str != null && position >= 0 && str.length() > position && c == str.charAt(position);
    }

    public static boolean isSubEquals(CharSequence str1, int start1, CharSequence str2, int start2, int length, boolean ignoreCase) {
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.toString().regionMatches(ignoreCase, start1, str2.toString(), start2, length);
    }

    public static String format(CharSequence template, Object... params) {
        if (template == null) {
            return NULL;
        }
        if (ArrayUtil.isEmpty(params) || isBlank(template)) {
            return template.toString();
        }
        return StrFormatter.format(template.toString(), params);
    }

    public static String indexedFormat(CharSequence pattern, Object... arguments) {
        return MessageFormat.format(pattern.toString(), arguments);
    }

    public static byte[] utf8Bytes(CharSequence str) {
        return bytes(str, CharsetUtil.CHARSET_UTF_8);
    }

    public static byte[] bytes(CharSequence str) {
        return bytes(str, Charset.defaultCharset());
    }

    public static byte[] bytes(CharSequence str, String charset) {
        return bytes(str, isBlank(charset) ? Charset.defaultCharset() : Charset.forName(charset));
    }

    public static byte[] bytes(CharSequence str, Charset charset) {
        if (str == null) {
            return null;
        }
        if (charset == null) {
            return str.toString().getBytes();
        }
        return str.toString().getBytes(charset);
    }

    public static ByteBuffer byteBuffer(CharSequence str, String charset) {
        return ByteBuffer.wrap(bytes(str, charset));
    }

    public static String wrap(CharSequence str, CharSequence prefixAndSuffix) {
        return wrap(str, prefixAndSuffix, prefixAndSuffix);
    }

    public static String wrap(CharSequence str, CharSequence prefix, CharSequence suffix) {
        return nullToEmpty(prefix).concat(nullToEmpty(str)).concat(nullToEmpty(suffix));
    }

    public static String[] wrapAllWithPair(CharSequence prefixAndSuffix, CharSequence... strs) {
        return wrapAll(prefixAndSuffix, prefixAndSuffix, strs);
    }

    public static String[] wrapAll(CharSequence prefix, CharSequence suffix, CharSequence... strs) {
        String[] results = new String[strs.length];
        for (int i = 0; i < strs.length; i++) {
            results[i] = wrap(strs[i], prefix, suffix);
        }
        return results;
    }

    public static String wrapIfMissing(CharSequence str, CharSequence prefix, CharSequence suffix) {
        int len = isNotEmpty(str) ? 0 + str.length() : 0;
        if (isNotEmpty(prefix)) {
            len += str.length();
        }
        if (isNotEmpty(suffix)) {
            len += str.length();
        }
        StringBuilder sb = new StringBuilder(len);
        if (isNotEmpty(prefix) && !startWith(str, prefix)) {
            sb.append(prefix);
        }
        if (isNotEmpty(str)) {
            sb.append(str);
        }
        if (isNotEmpty(suffix) && !endWith(str, suffix)) {
            sb.append(suffix);
        }
        return sb.toString();
    }

    public static String[] wrapAllWithPairIfMissing(CharSequence prefixAndSuffix, CharSequence... strs) {
        return wrapAllIfMissing(prefixAndSuffix, prefixAndSuffix, strs);
    }

    public static String[] wrapAllIfMissing(CharSequence prefix, CharSequence suffix, CharSequence... strs) {
        String[] results = new String[strs.length];
        for (int i = 0; i < strs.length; i++) {
            results[i] = wrapIfMissing(strs[i], prefix, suffix);
        }
        return results;
    }

    public static String unWrap(CharSequence str, String prefix, String suffix) {
        if (isWrap(str, prefix, suffix)) {
            return sub(str, prefix.length(), str.length() - suffix.length());
        }
        return str.toString();
    }

    public static String unWrap(CharSequence str, char prefix, char suffix) {
        if (isEmpty(str)) {
            return str(str);
        }
        if (str.charAt(0) == prefix && str.charAt(str.length() - 1) == suffix) {
            return sub(str, 1, str.length() - 1);
        }
        return str.toString();
    }

    public static String unWrap(CharSequence str, char prefixAndSuffix) {
        return unWrap(str, prefixAndSuffix, prefixAndSuffix);
    }

    public static boolean isWrap(CharSequence str, String prefix, String suffix) {
        if (ArrayUtil.hasNull(str, prefix, suffix)) {
            return false;
        }
        String str2 = str.toString();
        return str2.startsWith(prefix) && str2.endsWith(suffix);
    }

    public static boolean isWrap(CharSequence str, String wrapper) {
        return isWrap(str, wrapper, wrapper);
    }

    public static boolean isWrap(CharSequence str, char wrapper) {
        return isWrap(str, wrapper, wrapper);
    }

    public static boolean isWrap(CharSequence str, char prefixChar, char suffixChar) {
        return str != null && str.charAt(0) == prefixChar && str.charAt(str.length() - 1) == suffixChar;
    }

    public static String padPre(CharSequence str, int length, CharSequence padStr) {
        if (str == null) {
            return null;
        }
        int strLen = str.length();
        if (strLen == length) {
            return str.toString();
        }
        if (strLen > length) {
            return subPre(str, length);
        }
        return repeatByLength(padStr, length - strLen).concat(str.toString());
    }

    public static String padPre(CharSequence str, int length, char padChar) {
        if (str == null) {
            return null;
        }
        int strLen = str.length();
        if (strLen == length) {
            return str.toString();
        }
        if (strLen > length) {
            return subPre(str, length);
        }
        return repeat(padChar, length - strLen).concat(str.toString());
    }

    public static String padAfter(CharSequence str, int length, char padChar) {
        if (str == null) {
            return null;
        }
        int strLen = str.length();
        if (strLen == length) {
            return str.toString();
        }
        if (strLen > length) {
            return sub(str, strLen - length, strLen);
        }
        return str.toString().concat(repeat(padChar, length - strLen));
    }

    public static String padAfter(CharSequence str, int length, CharSequence padStr) {
        if (str == null) {
            return null;
        }
        int strLen = str.length();
        if (strLen == length) {
            return str.toString();
        }
        if (strLen > length) {
            return subSufByLength(str, length);
        }
        return str.toString().concat(repeatByLength(padStr, length - strLen));
    }

    public static String center(CharSequence str, int size) {
        return center(str, size, ' ');
    }

    public static String center(CharSequence str, int size, char padChar) {
        if (str == null || size <= 0) {
            return str(str);
        }
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str.toString();
        }
        return padAfter(padPre(str, (pads / 2) + strLen, padChar), size, padChar).toString();
    }

    public static String center(CharSequence str, int size, CharSequence padStr) {
        if (str == null || size <= 0) {
            return str(str);
        }
        if (isEmpty(padStr)) {
            padStr = " ";
        }
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str.toString();
        }
        return padAfter(padPre(str, (pads / 2) + strLen, padStr), size, padStr).toString();
    }

    public static String str(CharSequence cs) {
        if (cs == null) {
            return null;
        }
        return cs.toString();
    }

    public static int count(CharSequence content, CharSequence strForSearch) {
        if (hasEmpty(content, strForSearch) || strForSearch.length() > content.length()) {
            return 0;
        }
        int count = 0;
        int idx = 0;
        String content2 = content.toString();
        String strForSearch2 = strForSearch.toString();
        while (true) {
            int idx2 = content2.indexOf(strForSearch2, idx);
            if (idx2 > -1) {
                count++;
                idx = idx2 + strForSearch.length();
            } else {
                return count;
            }
        }
    }

    public static int count(CharSequence content, char charForSearch) {
        int count = 0;
        if (isEmpty(content)) {
            return 0;
        }
        int contentLength = content.length();
        for (int i = 0; i < contentLength; i++) {
            if (charForSearch == content.charAt(i)) {
                count++;
            }
        }
        return count;
    }

    public static int compare(CharSequence str1, CharSequence str2, boolean nullIsLess) {
        if (str1 == str2) {
            return 0;
        }
        if (str1 == null) {
            return nullIsLess ? -1 : 1;
        } else if (str2 == null) {
            return nullIsLess ? 1 : -1;
        } else {
            return str1.toString().compareTo(str2.toString());
        }
    }

    public static int compareIgnoreCase(CharSequence str1, CharSequence str2, boolean nullIsLess) {
        if (str1 == str2) {
            return 0;
        }
        if (str1 == null) {
            return nullIsLess ? -1 : 1;
        } else if (str2 == null) {
            return nullIsLess ? 1 : -1;
        } else {
            return str1.toString().compareToIgnoreCase(str2.toString());
        }
    }

    public static int compareVersion(CharSequence version1, CharSequence version2) {
        return VersionComparator.INSTANCE.compare(str(version1), str(version2));
    }

    public static String appendIfMissing(CharSequence str, CharSequence suffix, CharSequence... suffixes) {
        return appendIfMissing(str, suffix, false, suffixes);
    }

    public static String appendIfMissingIgnoreCase(CharSequence str, CharSequence suffix, CharSequence... suffixes) {
        return appendIfMissing(str, suffix, true, suffixes);
    }

    public static String appendIfMissing(CharSequence str, CharSequence suffix, boolean ignoreCase, CharSequence... testSuffixes) {
        if (str == null || isEmpty(suffix) || endWith(str, suffix, ignoreCase)) {
            return str(str);
        }
        if (ArrayUtil.isNotEmpty((Object[]) testSuffixes)) {
            for (CharSequence testSuffix : testSuffixes) {
                if (endWith(str, testSuffix, ignoreCase)) {
                    return str.toString();
                }
            }
        }
        return str.toString().concat(suffix.toString());
    }

    public static String prependIfMissing(CharSequence str, CharSequence prefix, CharSequence... prefixes) {
        return prependIfMissing(str, prefix, false, prefixes);
    }

    public static String prependIfMissingIgnoreCase(CharSequence str, CharSequence prefix, CharSequence... prefixes) {
        return prependIfMissing(str, prefix, true, prefixes);
    }

    public static String prependIfMissing(CharSequence str, CharSequence prefix, boolean ignoreCase, CharSequence... prefixes) {
        if (str == null || isEmpty(prefix) || startWith(str, prefix, ignoreCase)) {
            return str(str);
        }
        if (prefixes != null && prefixes.length > 0) {
            for (CharSequence s : prefixes) {
                if (startWith(str, s, ignoreCase)) {
                    return str.toString();
                }
            }
        }
        return prefix.toString().concat(str.toString());
    }

    public static String replaceIgnoreCase(CharSequence str, CharSequence searchStr, CharSequence replacement) {
        return replace(str, 0, searchStr, replacement, true);
    }

    public static String replace(CharSequence str, CharSequence searchStr, CharSequence replacement) {
        return replace(str, 0, searchStr, replacement, false);
    }

    public static String replace(CharSequence str, CharSequence searchStr, CharSequence replacement, boolean ignoreCase) {
        return replace(str, 0, searchStr, replacement, ignoreCase);
    }

    public static String replace(CharSequence str, int fromIndex, CharSequence searchStr, CharSequence replacement, boolean ignoreCase) {
        if (isEmpty(str) || isEmpty(searchStr)) {
            return str(str);
        }
        if (replacement == null) {
            replacement = "";
        }
        int strLength = str.length();
        int searchStrLength = searchStr.length();
        if (fromIndex > strLength) {
            return str(str);
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        StrBuilder result = StrBuilder.create(strLength + 16);
        if (fromIndex != 0) {
            result.append(str.subSequence(0, fromIndex));
        }
        int preIndex = fromIndex;
        while (true) {
            int index = indexOf(str, searchStr, preIndex, ignoreCase);
            if (index <= -1) {
                break;
            }
            result.append(str.subSequence(preIndex, index));
            result.append(replacement);
            preIndex = index + searchStrLength;
        }
        if (preIndex < strLength) {
            result.append(str.subSequence(preIndex, strLength));
        }
        return result.toString();
    }

    public static String replace(CharSequence str, int startInclude, int endExclude, char replacedChar) {
        if (isEmpty(str)) {
            return str(str);
        }
        int strLength = str.length();
        if (startInclude > strLength) {
            return str(str);
        }
        if (endExclude > strLength) {
            endExclude = strLength;
        }
        if (startInclude > endExclude) {
            return str(str);
        }
        char[] chars = new char[strLength];
        for (int i = 0; i < strLength; i++) {
            if (i >= startInclude && i < endExclude) {
                chars[i] = replacedChar;
            } else {
                chars[i] = str.charAt(i);
            }
        }
        return new String(chars);
    }

    public static String replace(CharSequence str, Pattern pattern, Func1<Matcher, String> replaceFun) {
        return ReUtil.replaceAll(str, pattern, replaceFun);
    }

    public static String replace(CharSequence str, String regex, Func1<Matcher, String> replaceFun) {
        return ReUtil.replaceAll(str, regex, replaceFun);
    }

    public static String hide(CharSequence str, int startInclude, int endExclude) {
        return replace(str, startInclude, endExclude, '*');
    }

    public static String desensitized(CharSequence str, DesensitizedUtil.DesensitizedType desensitizedType) {
        return DesensitizedUtil.desensitized(str, desensitizedType);
    }

    public static String replaceChars(CharSequence str, String chars, CharSequence replacedStr) {
        if (isEmpty(str) || isEmpty(chars)) {
            return str(str);
        }
        return replaceChars(str, chars.toCharArray(), replacedStr);
    }

    public static String replaceChars(CharSequence str, char[] chars, CharSequence replacedStr) {
        if (isEmpty(str) || ArrayUtil.isEmpty(chars)) {
            return str(str);
        }
        Set<Character> set = new HashSet<>(chars.length);
        for (char c : chars) {
            set.add(Character.valueOf(c));
        }
        int strLen = str.length();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < strLen; i++) {
            char c2 = str.charAt(i);
            builder.append(set.contains(Character.valueOf(c2)) ? replacedStr : Character.valueOf(c2));
        }
        return builder.toString();
    }

    public static int length(CharSequence cs) {
        if (cs == null) {
            return 0;
        }
        return cs.length();
    }

    public static int byteLength(CharSequence cs, Charset charset) {
        if (cs == null) {
            return 0;
        }
        return cs.toString().getBytes(charset).length;
    }

    public static int totalLength(CharSequence... strs) {
        int length = strs.length;
        int totalLength = 0;
        for (int totalLength2 = 0; totalLength2 < length; totalLength2++) {
            CharSequence str = strs[totalLength2];
            totalLength += str == null ? 0 : str.length();
        }
        return totalLength;
    }

    public static String maxLength(CharSequence string, int length) {
        Assert.isTrue(length > 0);
        if (string == null) {
            return null;
        }
        if (string.length() <= length) {
            return string.toString();
        }
        return sub(string, 0, length) + "...";
    }

    public <T extends CharSequence> T firstNonNull(T... strs) {
        return (T) ArrayUtil.firstNonNull(strs);
    }

    public <T extends CharSequence> T firstNonEmpty(T... strs) {
        return (T) ArrayUtil.firstMatch(new cn.hutool.core.lang.Matcher() { // from class: cn.hutool.core.text.-$$Lambda$eL3nbfKXqyKUubHIkMhoO0WfiyM
            @Override // cn.hutool.core.lang.Matcher
            public final boolean match(Object obj) {
                return CharSequenceUtil.isNotEmpty((CharSequence) obj);
            }
        }, strs);
    }

    public <T extends CharSequence> T firstNonBlank(T... strs) {
        return (T) ArrayUtil.firstMatch(new cn.hutool.core.lang.Matcher() { // from class: cn.hutool.core.text.-$$Lambda$Z1FVV4LgKS-ZRqzZOrVInibOd78
            @Override // cn.hutool.core.lang.Matcher
            public final boolean match(Object obj) {
                return CharSequenceUtil.isNotBlank((CharSequence) obj);
            }
        }, strs);
    }

    public static String upperFirstAndAddPre(CharSequence str, String preString) {
        if (str == null || preString == null) {
            return null;
        }
        return preString + upperFirst(str);
    }

    public static String upperFirst(CharSequence str) {
        if (str == null) {
            return null;
        }
        if (str.length() > 0) {
            char firstChar = str.charAt(0);
            if (Character.isLowerCase(firstChar)) {
                return Character.toUpperCase(firstChar) + subSuf(str, 1);
            }
        }
        return str.toString();
    }

    public static String lowerFirst(CharSequence str) {
        if (str == null) {
            return null;
        }
        if (str.length() > 0) {
            char firstChar = str.charAt(0);
            if (Character.isUpperCase(firstChar)) {
                return Character.toLowerCase(firstChar) + subSuf(str, 1);
            }
        }
        return str.toString();
    }

    public static String filter(CharSequence str, Filter<Character> filter) {
        if (str == null || filter == null) {
            return str(str);
        }
        int len = str.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (filter.accept(Character.valueOf(c))) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static boolean isUpperCase(CharSequence str) {
        if (str == null) {
            return false;
        }
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (Character.isLowerCase(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isLowerCase(CharSequence str) {
        if (str == null) {
            return false;
        }
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (Character.isUpperCase(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String swapCase(String str) {
        if (isEmpty(str)) {
            return str;
        }
        char[] buffer = str.toCharArray();
        for (int i = 0; i < buffer.length; i++) {
            char ch2 = buffer[i];
            if (Character.isUpperCase(ch2)) {
                buffer[i] = Character.toLowerCase(ch2);
            } else if (Character.isTitleCase(ch2)) {
                buffer[i] = Character.toLowerCase(ch2);
            } else if (Character.isLowerCase(ch2)) {
                buffer[i] = Character.toUpperCase(ch2);
            }
        }
        return new String(buffer);
    }

    public static String toUnderlineCase(CharSequence str) {
        return NamingCase.toUnderlineCase(str);
    }

    public static String toSymbolCase(CharSequence str, char symbol) {
        return NamingCase.toSymbolCase(str, symbol);
    }

    public static String toCamelCase(CharSequence name) {
        return NamingCase.toCamelCase(name);
    }

    public static boolean isSurround(CharSequence str, CharSequence prefix, CharSequence suffix) {
        if (!StrUtil.isBlank(str) && str.length() >= prefix.length() + suffix.length()) {
            String str2 = str.toString();
            return str2.startsWith(prefix.toString()) && str2.endsWith(suffix.toString());
        }
        return false;
    }

    public static boolean isSurround(CharSequence str, char prefix, char suffix) {
        return !StrUtil.isBlank(str) && str.length() >= 2 && str.charAt(0) == prefix && str.charAt(str.length() - 1) == suffix;
    }

    public static StringBuilder builder(CharSequence... strs) {
        StringBuilder sb = new StringBuilder();
        for (CharSequence str : strs) {
            sb.append(str);
        }
        return sb;
    }

    public static StrBuilder strBuilder(CharSequence... strs) {
        return StrBuilder.create(strs);
    }

    public static String getGeneralField(CharSequence getOrSetMethodName) {
        String getOrSetMethodNameStr = getOrSetMethodName.toString();
        if (getOrSetMethodNameStr.startsWith("get") || getOrSetMethodNameStr.startsWith("set")) {
            return removePreAndLowerFirst(getOrSetMethodName, 3);
        }
        if (getOrSetMethodNameStr.startsWith("is")) {
            return removePreAndLowerFirst(getOrSetMethodName, 2);
        }
        return null;
    }

    public static String genSetter(CharSequence fieldName) {
        return upperFirstAndAddPre(fieldName, "set");
    }

    public static String genGetter(CharSequence fieldName) {
        return upperFirstAndAddPre(fieldName, "get");
    }

    public static String concat(boolean isNullToEmpty, CharSequence... strs) {
        StrBuilder sb = new StrBuilder();
        for (CharSequence str : strs) {
            sb.append(isNullToEmpty ? nullToEmpty(str) : str);
        }
        return sb.toString();
    }

    public static String brief(CharSequence str, int maxLength) {
        if (str == null) {
            return null;
        }
        int strLength = str.length();
        if (maxLength <= 0 || strLength <= maxLength) {
            return str.toString();
        }
        if (maxLength != 1) {
            if (maxLength == 2) {
                return str.charAt(0) + ".";
            } else if (maxLength == 3) {
                return str.charAt(0) + "." + str.charAt(str.length() - 1);
            } else {
                int w = maxLength / 2;
                String str2 = str.toString();
                return format("{}...{}", str2.substring(0, maxLength - w), str2.substring((strLength - w) + 3));
            }
        }
        return String.valueOf(str.charAt(0));
    }

    public static String join(CharSequence conjunction, Object... objs) {
        return ArrayUtil.join(objs, conjunction);
    }

    public static <T> String join(CharSequence conjunction, Iterable<T> iterable) {
        return CollUtil.join(iterable, conjunction);
    }

    public static boolean isAllCharMatch(CharSequence value, cn.hutool.core.lang.Matcher<Character> matcher) {
        if (StrUtil.isBlank(value)) {
            return false;
        }
        int i = value.length();
        do {
            i--;
            if (i < 0) {
                return true;
            }
        } while (matcher.match(Character.valueOf(value.charAt(i))));
        return false;
    }

    public static boolean isNumeric(CharSequence str) {
        return isAllCharMatch(str, new cn.hutool.core.lang.Matcher() { // from class: cn.hutool.core.text.-$$Lambda$91wjnFI3xLSOqg4MNCXxMyJKMOg
            @Override // cn.hutool.core.lang.Matcher
            public final boolean match(Object obj) {
                return Character.isDigit(((Character) obj).charValue());
            }
        });
    }

    public static String move(CharSequence str, int startInclude, int endExclude, int moveLength) {
        if (isEmpty(str)) {
            return str(str);
        }
        int len = str.length();
        if (Math.abs(moveLength) > len) {
            moveLength %= len;
        }
        StrBuilder strBuilder = StrBuilder.create(len);
        if (moveLength > 0) {
            int endAfterMove = Math.min(endExclude + moveLength, str.length());
            strBuilder.append(str.subSequence(0, startInclude)).append(str.subSequence(endExclude, endAfterMove)).append(str.subSequence(startInclude, endExclude)).append(str.subSequence(endAfterMove, str.length()));
        } else if (moveLength < 0) {
            int startAfterMove = Math.max(startInclude + moveLength, 0);
            strBuilder.append(str.subSequence(0, startAfterMove)).append(str.subSequence(startInclude, endExclude)).append(str.subSequence(startAfterMove, startInclude)).append(str.subSequence(endExclude, str.length()));
        } else {
            return str(str);
        }
        return strBuilder.toString();
    }

    public static boolean isCharEquals(String str) {
        return isBlank(str.replace(str.charAt(0), ' '));
    }
}
