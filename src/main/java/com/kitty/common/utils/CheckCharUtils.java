
package com.kitty.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckCharUtils
{
    public static Pattern CHECK_EMOJI;
    
    public static boolean checkQQNumber(final String word) {
        final String reg = "[1-9][0-9]{5,15}";
        final Pattern pattern = Pattern.compile(reg);
        final Matcher matcher = pattern.matcher(word);
        if (matcher.find()) {
            final String group = matcher.group();
            return true;
        }
        return false;
    }
    
    public static String replaceName(String name) {
        if (name == null) {
            return null;
        }
        name = name.replace("#", "");
        name = name.replace("～", "");
        name = name.replace("~", "");
        name = name.replace("-", "");
        name = name.replace("_", "");
        name = name.replace("@", "");
        name = name.replace("G", "");
        name = name.replace("M", "");
        name = name.replace("g", "");
        name = name.replace("m", "");
        name = name.replace("Ｇ", "");
        name = name.replace("Ｍ", "");
        name = name.replace("ｇ", "");
        name = name.replace("ｍ", "");
        name = name.replace("全体", "");
        name = name.replace("运维", "");
        name = name.replace("。", "");
        name = name.replace("丶", "");
        name = name.replace("=", "");
        name = name.replace(":", "");
        name = name.replace("：", "");
        name = name.replace("\\", "");
        name = name.replace("、", "");
        name = name.replace("＝", "");
        name = name.replace("丶", "");
        name = name.replace("￡", "");
        name = name.replace("゛", "");
        name = name.replace(".", "");
        name = name.replace("", "");
        name = name.replace("『", "");
        name = name.replace("』", "");
        name = name.replace("群", "");
        name = name.replace("裙", "");
        name = name.replace("裙", "");
        name = name.replace("Q", "");
        name = name.replace("q", "");
        name = name.replace("V", "");
        name = name.replace("X", "");
        name = name.replace("x", "");
        name = name.replace("w", "");
        name = name.replace("W", "");
        name = name.replace("信", "");
        name = name.replace("微", "");
        name = name.replace("利", "");
        name = name.replace("返", "");
        name = name.replace("术", "");
        name = name.replace("技", "");
        name = name.replace("v", "");
        name = filterEmoji(name);
        return filterStr(name);
    }
    
    public static String filterStr(final String str) {
        final String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        final Pattern p = Pattern.compile(regEx);
        final Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
    
    public static String filterEmoji(String source) {
        if (source == null) {
            return source;
        }
        final Pattern emoji = Pattern.compile("[🀀-🏿]|[🐀-🟿]|[☀-⟿]|[🤀-🧿]|[⌀-\u23ff]|[─-◿]|[℀-⇿]|[ -\u0fff]|[⬀-\u2bff]|[ⴆ]|[〰]", 66);
        final Matcher emojiMatcher = emoji.matcher(source);
        if (emojiMatcher.find()) {
            source = emojiMatcher.replaceAll("*");
            return source;
        }
        return source;
    }
    
    static {
        CheckCharUtils.CHECK_EMOJI = Pattern.compile("[🀀-🏿]|[🐀-🟿]|[☀-⟿]|[🤀-🧿]|[⌀-\u23ff]|[─-◿]|[℀-⇿]|[ -\u0fff]|[⬀-\u2bff]|[ⴆ]|[〰]", 66);
    }
}
