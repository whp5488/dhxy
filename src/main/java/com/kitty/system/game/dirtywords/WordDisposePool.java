
package com.kitty.system.game.dirtywords;

import java.util.HashMap;
import com.kitty.system.game.dirtywords.unit.DirtyWordUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import com.kitty.system.init.service.InitService;
import com.kitty.system.game.dirtywords.domain.Word;
import java.util.HashSet;
import com.kitty.common.utils.spring.SpringUtils;
import java.util.List;
import java.util.Map;

public class WordDisposePool
{
    private static final Map<Character, List<String>> dirtyWords;
    
    public static void load() {
        final InitService initService = SpringUtils.getInitService();
        final List<Word> list = initService.getWord();
        final Set<String> set = new HashSet<String>();
        for (final Word word : list) {
            set.add(word.getWord());
        }
        initWordsStore(set);
        System.out.println("初始化敏感词汇 == " + set.size());
    }
    
    public static List<String> checkDirtyWords(final String sentence) {
        final char[] stringArr = sentence.toCharArray();
        final int wordCount = stringArr.length;
        final List<String> result = new ArrayList<String>();
        for (int i = 0; i < wordCount - 1; ++i) {
            final char key = stringArr[i];
            if (WordDisposePool.dirtyWords.containsKey(key)) {
                final int endIndex = Math.min(10, wordCount - i);
                final String checkWord = new String(stringArr, i, endIndex);
                final String matchedKey = checkWordsMatch(checkWord, key);
                if (matchedKey != null && matchedKey.trim().length() > 0) {
                    result.add(matchedKey);
                }
            }
        }
        return result;
    }
    
    public static String checkWordsMatch(final String source, final Character key) {
        final List<String> match = WordDisposePool.dirtyWords.get(key);
        for (final String unit : match) {
            final DirtyWordUnit wordUnit = new DirtyWordUnit(source, unit);
            wordUnit.checkWordIndex();
            if (wordUnit.foundDitryWord()) {
                return wordUnit.getKeyWord();
            }
        }
        return "";
    }
    
    private static void initWordsStore(final Set<String> words) {
        for (final String word : words) {
            final char first = word.charAt(0);
            final List<String> sameFirst = WordDisposePool.dirtyWords.computeIfAbsent(Character.valueOf(first), k -> new ArrayList());
            sameFirst.add(word);
        }
    }
    
    static {
        dirtyWords = new HashMap<Character, List<String>>();
    }
}
