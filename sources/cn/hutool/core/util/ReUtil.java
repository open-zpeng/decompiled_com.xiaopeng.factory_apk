package cn.hutool.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Holder;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.lang.func.Func1;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* loaded from: classes.dex */
public class ReUtil {
    public static final String RE_CHINESE = "[一-\u9fff]";
    public static final String RE_CHINESES = "[一-\u9fff]+";
    public static final Set<Character> RE_KEYS = CollUtil.newHashSet('$', '(', ')', '*', '+', '.', '[', ']', '?', '\\', '^', '{', '}', '|');

    public static String getGroup0(String regex, CharSequence content) {
        return get(regex, content, 0);
    }

    public static String getGroup1(String regex, CharSequence content) {
        return get(regex, content, 1);
    }

    public static String get(String regex, CharSequence content, int groupIndex) {
        if (content == null || regex == null) {
            return null;
        }
        Pattern pattern = PatternPool.get(regex, 32);
        return get(pattern, content, groupIndex);
    }

    public static String getGroup0(Pattern pattern, CharSequence content) {
        return get(pattern, content, 0);
    }

    public static String getGroup1(Pattern pattern, CharSequence content) {
        return get(pattern, content, 1);
    }

    public static String get(Pattern pattern, CharSequence content, int groupIndex) {
        if (content == null || pattern == null) {
            return null;
        }
        Matcher matcher = pattern.matcher(content);
        if (!matcher.find()) {
            return null;
        }
        return matcher.group(groupIndex);
    }

    public static List<String> getAllGroups(Pattern pattern, CharSequence content) {
        return getAllGroups(pattern, content, true);
    }

    public static List<String> getAllGroups(Pattern pattern, CharSequence content, boolean withGroup0) {
        if (content == null || pattern == null) {
            return null;
        }
        ArrayList<String> result = new ArrayList<>();
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            int startGroup = !withGroup0 ? 1 : 0;
            int groupCount = matcher.groupCount();
            for (int i = startGroup; i <= groupCount; i++) {
                result.add(matcher.group(i));
            }
        }
        return result;
    }

    public static String extractMulti(Pattern pattern, CharSequence content, String template) {
        if (content == null || pattern == null || template == null) {
            return null;
        }
        TreeSet<Integer> varNums = new TreeSet<>(new Comparator() { // from class: cn.hutool.core.util.-$$Lambda$ReUtil$XXV1kNIG9IYt3ArEyb2pbPW9BjI
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int compare;
                compare = ObjectUtil.compare((Integer) obj2, (Integer) obj);
                return compare;
            }
        });
        Matcher matcherForTemplate = PatternPool.GROUP_VAR.matcher(template);
        while (matcherForTemplate.find()) {
            varNums.add(Integer.valueOf(Integer.parseInt(matcherForTemplate.group(1))));
        }
        Matcher matcher = pattern.matcher(content);
        if (!matcher.find()) {
            return null;
        }
        Iterator<Integer> it = varNums.iterator();
        while (it.hasNext()) {
            Integer group = it.next();
            template = template.replace("$" + group, matcher.group(group.intValue()));
        }
        return template;
    }

    public static String extractMulti(String regex, CharSequence content, String template) {
        if (content == null || regex == null || template == null) {
            return null;
        }
        Pattern pattern = PatternPool.get(regex, 32);
        return extractMulti(pattern, content, template);
    }

    public static String extractMultiAndDelPre(Pattern pattern, Holder<CharSequence> contentHolder, String template) {
        if (contentHolder == null || pattern == null || template == null) {
            return null;
        }
        HashSet<String> varNums = (HashSet) findAll(PatternPool.GROUP_VAR, template, 1, new HashSet());
        CharSequence content = contentHolder.get();
        Matcher matcher = pattern.matcher(content);
        if (!matcher.find()) {
            return null;
        }
        Iterator<String> it = varNums.iterator();
        while (it.hasNext()) {
            String var = it.next();
            int group = Integer.parseInt(var);
            template = template.replace("$" + var, matcher.group(group));
        }
        contentHolder.set(StrUtil.sub(content, matcher.end(), content.length()));
        return template;
    }

    public static String extractMultiAndDelPre(String regex, Holder<CharSequence> contentHolder, String template) {
        if (contentHolder == null || regex == null || template == null) {
            return null;
        }
        Pattern pattern = PatternPool.get(regex, 32);
        return extractMultiAndDelPre(pattern, contentHolder, template);
    }

    public static String delFirst(String regex, CharSequence content) {
        if (StrUtil.hasBlank(regex, content)) {
            return StrUtil.str(content);
        }
        Pattern pattern = PatternPool.get(regex, 32);
        return delFirst(pattern, content);
    }

    public static String delFirst(Pattern pattern, CharSequence content) {
        return replaceFirst(pattern, content, "");
    }

    public static String replaceFirst(Pattern pattern, CharSequence content, String replacement) {
        if (pattern == null || StrUtil.isEmpty(content)) {
            return StrUtil.str(content);
        }
        return pattern.matcher(content).replaceFirst(replacement);
    }

    public static String delLast(String regex, CharSequence str) {
        if (StrUtil.hasBlank(regex, str)) {
            return StrUtil.str(str);
        }
        Pattern pattern = PatternPool.get(regex, 32);
        return delLast(pattern, str);
    }

    public static String delLast(Pattern pattern, CharSequence str) {
        MatchResult matchResult;
        if (pattern != null && StrUtil.isNotEmpty(str) && (matchResult = lastIndexOf(pattern, str)) != null) {
            return StrUtil.subPre(str, matchResult.start()) + StrUtil.subSuf(str, matchResult.end());
        }
        return StrUtil.str(str);
    }

    public static String delAll(String regex, CharSequence content) {
        if (StrUtil.hasBlank(regex, content)) {
            return StrUtil.str(content);
        }
        Pattern pattern = PatternPool.get(regex, 32);
        return delAll(pattern, content);
    }

    public static String delAll(Pattern pattern, CharSequence content) {
        if (pattern == null || StrUtil.isBlank(content)) {
            return StrUtil.str(content);
        }
        return pattern.matcher(content).replaceAll("");
    }

    public static String delPre(String regex, CharSequence content) {
        if (content == null || regex == null) {
            return StrUtil.str(content);
        }
        Pattern pattern = PatternPool.get(regex, 32);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return StrUtil.sub(content, matcher.end(), content.length());
        }
        return StrUtil.str(content);
    }

    public static List<String> findAllGroup0(String regex, CharSequence content) {
        return findAll(regex, content, 0);
    }

    public static List<String> findAllGroup1(String regex, CharSequence content) {
        return findAll(regex, content, 1);
    }

    public static List<String> findAll(String regex, CharSequence content, int group) {
        return (List) findAll(regex, content, group, new ArrayList());
    }

    public static <T extends Collection<String>> T findAll(String regex, CharSequence content, int group, T collection) {
        if (regex == null) {
            return collection;
        }
        return (T) findAll(Pattern.compile(regex, 32), content, group, collection);
    }

    public static List<String> findAllGroup0(Pattern pattern, CharSequence content) {
        return findAll(pattern, content, 0);
    }

    public static List<String> findAllGroup1(Pattern pattern, CharSequence content) {
        return findAll(pattern, content, 1);
    }

    public static List<String> findAll(Pattern pattern, CharSequence content, int group) {
        return (List) findAll(pattern, content, group, new ArrayList());
    }

    public static <T extends Collection<String>> T findAll(Pattern pattern, CharSequence content, int group, T collection) {
        if (pattern == null || content == null) {
            return null;
        }
        if (collection == null) {
            throw new NullPointerException("Null collection param provided!");
        }
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            collection.add(matcher.group(group));
        }
        return collection;
    }

    public static int count(String regex, CharSequence content) {
        if (regex == null || content == null) {
            return 0;
        }
        Pattern pattern = PatternPool.get(regex, 32);
        return count(pattern, content);
    }

    public static int count(Pattern pattern, CharSequence content) {
        if (pattern == null || content == null) {
            return 0;
        }
        int count = 0;
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    public static boolean contains(String regex, CharSequence content) {
        if (regex == null || content == null) {
            return false;
        }
        Pattern pattern = PatternPool.get(regex, 32);
        return contains(pattern, content);
    }

    public static boolean contains(Pattern pattern, CharSequence content) {
        if (pattern == null || content == null) {
            return false;
        }
        return pattern.matcher(content).find();
    }

    public static MatchResult indexOf(String regex, CharSequence content) {
        if (regex == null || content == null) {
            return null;
        }
        Pattern pattern = PatternPool.get(regex, 32);
        return indexOf(pattern, content);
    }

    public static MatchResult indexOf(Pattern pattern, CharSequence content) {
        if (pattern != null && content != null) {
            Matcher matcher = pattern.matcher(content);
            if (matcher.find()) {
                return matcher.toMatchResult();
            }
            return null;
        }
        return null;
    }

    public static MatchResult lastIndexOf(String regex, CharSequence content) {
        if (regex == null || content == null) {
            return null;
        }
        Pattern pattern = PatternPool.get(regex, 32);
        return lastIndexOf(pattern, content);
    }

    public static MatchResult lastIndexOf(Pattern pattern, CharSequence content) {
        MatchResult result = null;
        if (pattern != null && content != null) {
            Matcher matcher = pattern.matcher(content);
            while (matcher.find()) {
                result = matcher.toMatchResult();
            }
        }
        return result;
    }

    public static Integer getFirstNumber(CharSequence StringWithNumber) {
        return Convert.toInt(get(PatternPool.NUMBERS, StringWithNumber, 0), null);
    }

    public static boolean isMatch(String regex, CharSequence content) {
        if (content == null) {
            return false;
        }
        if (StrUtil.isEmpty(regex)) {
            return true;
        }
        Pattern pattern = PatternPool.get(regex, 32);
        return isMatch(pattern, content);
    }

    public static boolean isMatch(Pattern pattern, CharSequence content) {
        if (content == null || pattern == null) {
            return false;
        }
        return pattern.matcher(content).matches();
    }

    public static String replaceAll(CharSequence content, String regex, String replacementTemplate) {
        Pattern pattern = Pattern.compile(regex, 32);
        return replaceAll(content, pattern, replacementTemplate);
    }

    public static String replaceAll(CharSequence content, Pattern pattern, String replacementTemplate) {
        boolean result;
        if (StrUtil.isEmpty(content)) {
            return StrUtil.str(content);
        }
        Matcher matcher = pattern.matcher(content);
        boolean result2 = matcher.find();
        if (result2) {
            Set<String> varNums = (Set) findAll(PatternPool.GROUP_VAR, replacementTemplate, 1, new HashSet());
            StringBuffer sb = new StringBuffer();
            do {
                String replacement = replacementTemplate;
                for (String var : varNums) {
                    int group = Integer.parseInt(var);
                    replacement = replacement.replace("$" + var, matcher.group(group));
                }
                matcher.appendReplacement(sb, escape(replacement));
                result = matcher.find();
            } while (result);
            matcher.appendTail(sb);
            return sb.toString();
        }
        return StrUtil.str(content);
    }

    public static String replaceAll(CharSequence str, String regex, Func1<Matcher, String> replaceFun) {
        return replaceAll(str, Pattern.compile(regex), replaceFun);
    }

    public static String replaceAll(CharSequence str, Pattern pattern, Func1<Matcher, String> replaceFun) {
        if (StrUtil.isEmpty(str)) {
            return StrUtil.str(str);
        }
        Matcher matcher = pattern.matcher(str);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            try {
                matcher.appendReplacement(buffer, replaceFun.call(matcher));
            } catch (Exception e) {
                throw new UtilException(e);
            }
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    public static String escape(char c) {
        StringBuilder builder = new StringBuilder();
        if (RE_KEYS.contains(Character.valueOf(c))) {
            builder.append('\\');
        }
        builder.append(c);
        return builder.toString();
    }

    public static String escape(CharSequence content) {
        if (StrUtil.isBlank(content)) {
            return StrUtil.str(content);
        }
        StringBuilder builder = new StringBuilder();
        int len = content.length();
        for (int i = 0; i < len; i++) {
            char current = content.charAt(i);
            if (RE_KEYS.contains(Character.valueOf(current))) {
                builder.append('\\');
            }
            builder.append(current);
        }
        return builder.toString();
    }
}
