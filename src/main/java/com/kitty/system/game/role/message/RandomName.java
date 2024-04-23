package com.kitty.system.game.role.message;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class RandomName {
  private static String[] Surname = new String[] { 
      "万俟", "司马", "上官", "欧阳", "夏侯", "诸葛", "闻人", "东方", "赫连", "皇甫", 
      "羊舌", "尉迟", "公羊", "澹台", "公冶", "宗正", "濮阳", "淳于", "单于", "太叔", 
      "申屠", "公孙", "仲孙", "轩辕", "令狐", "钟离", "宇文", "长孙", "慕容", "鲜于", 
      "闾丘", "司徒", "司空", "兀官", "司寇", "南门", "呼延", "子车", "颛孙", "端木", 
      "巫马", "公西", "漆雕", "车正", "壤驷", "公良", "拓跋", "夹谷", "宰父", "谷梁", 
      "段干", "百里", "东郭", "微生", "梁丘", "左丘", "东门", "西门", "南宫", "第五", 
      "公仪", "公乘", "太史", "仲长", "叔孙", "屈突", "尔朱", "东乡", "相里", "胡母", 
      "司城", "张廖", "雍门", "毋丘", "贺兰", "綦毋", "屋庐", "独孤", "南郭", "北宫", 
      "王孙" };
  
  public static String getChineseName() {
    Random random = new Random(System.currentTimeMillis());
    int index = random.nextInt(Surname.length - 1);
    String name = Surname[index];
    name = name + getChinese(2);
    return name;
  }
  
  private static String getChinese(int count) {
    StringBuilder str = new StringBuilder();
    Random random = new Random();
    for (int i = 0; i < count; i++) {
      int highPos = 176 + Math.abs(random.nextInt(71));
      random = new Random();
      int lowPos = 161 + Math.abs(random.nextInt(94));
      byte[] bArr = new byte[2];
      bArr[0] = (new Integer(highPos)).byteValue();
      bArr[1] = (new Integer(lowPos)).byteValue();
      try {
        str.append(new String(bArr, "GB2312"));
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      } 
    } 
    return str.toString();
  }
}
