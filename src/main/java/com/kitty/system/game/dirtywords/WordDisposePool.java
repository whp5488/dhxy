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
    
    public WordDisposePool() {
    }
    
    public static void load() {
        InitService initService = SpringUtils.getInitService();
        List<Word> list = initService.getWord();
        Set<String> set = new HashSet();
        for (Word word : list) {
            set.add(word.getWord());
        }
        initWordsStore(set);
        System.out.println("初始化敏感词汇 == " + set.size());
    }
    
    public static List<String> checkDirtyWords(String sentence) {
        char[] stringArr = sentence.toCharArray();
        int wordCount = stringArr.length;
        List<String> result = new ArrayList<>();
        for (int i = 0; i < wordCount - 1; ++i) {
            char key = stringArr[i];
            if (WordDisposePool.dirtyWords.containsKey(Character.valueOf(key))) {
                int endIndex = Math.min(10, wordCount - i);
                String checkWord = new String(stringArr, i, endIndex);
                String matchedKey = checkWordsMatch(checkWord, Character.valueOf(key));
                if (matchedKey != null && matchedKey.trim().length() > 0) {
                    result.add(matchedKey);
                }
            }
        }
        return result;
    }
    
    public static String checkWordsMatch(String source, Character key) {
        List<String> match = (List)WordDisposePool.dirtyWords.get(key);
        for (String unit : match) {
            DirtyWordUnit wordUnit = new DirtyWordUnit(source, unit);
            wordUnit.checkWordIndex();
            if (wordUnit.foundDitryWord()) {
                return wordUnit.getKeyWord();
            }
            continue;
        }
        return "";
    }
    
    private static void initWordsStore(Set<String> words) {
        for (String word : words) {
            char first = word.charAt(0);
            List<String> sameFirst = (List)WordDisposePool.dirtyWords.computeIfAbsent(Character.valueOf(first), k -> new ArrayList<>());
            sameFirst.add(word);
        }
    }
    
    static {
        dirtyWords = new HashMap();
    }
}
