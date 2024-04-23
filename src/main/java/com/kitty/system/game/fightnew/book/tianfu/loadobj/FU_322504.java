package com.kitty.system.game.fightnew.book.tianfu.loadobj;

import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import org.nutz.lang.util.NutMap;

public class FU_322504 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public void attackOver(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
    // Byte code:
    //   0: aload_3
    //   1: invokevirtual isPlayer : ()Z
    //   4: ifne -> 8
    //   7: return
    //   8: aload_3
    //   9: checkcast com/kitty/system/game/fightnew/bean/PlayerFightObject
    //   12: invokevirtual getDayInCeFight : ()Lcom/kitty/system/game/fightnew/bean/DayInCeFight;
    //   15: astore #6
    //   17: aload #6
    //   19: ldc 322504
    //   21: invokestatic getTianFuObject : (Lcom/kitty/system/game/fightnew/bean/DayInCeFight;I)Lcom/kitty/system/game/fightnew/bean/TianFuObject;
    //   24: astore #7
    //   26: aload #7
    //   28: ifnonnull -> 32
    //   31: return
    //   32: iconst_0
    //   33: istore #8
    //   35: iconst_m1
    //   36: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   39: astore #9
    //   41: iconst_m1
    //   42: istore #10
    //   44: invokestatic getControlStatus : ()Ljava/util/List;
    //   47: invokeinterface iterator : ()Ljava/util/Iterator;
    //   52: astore #11
    //   54: aload #11
    //   56: invokeinterface hasNext : ()Z
    //   61: ifeq -> 186
    //   64: aload #11
    //   66: invokeinterface next : ()Ljava/lang/Object;
    //   71: checkcast java/lang/Integer
    //   74: astore #12
    //   76: aload_3
    //   77: aload #12
    //   79: invokevirtual intValue : ()I
    //   82: invokevirtual getStatusValue : (I)I
    //   85: ifle -> 183
    //   88: iconst_1
    //   89: istore #8
    //   91: aload #12
    //   93: astore #9
    //   95: aload #9
    //   97: invokevirtual intValue : ()I
    //   100: tableswitch default -> 180, 1002 -> 136, 1003 -> 147, 1004 -> 158, 1005 -> 180, 1006 -> 169
    //   136: getstatic com/kitty/system/enums/war/warskill/skilldef/SKILLATTR.SKILLATTR_CONFUSE : Lcom/kitty/system/enums/war/warskill/skilldef/SKILLATTR;
    //   139: invokevirtual getValue : ()I
    //   142: istore #10
    //   144: goto -> 186
    //   147: getstatic com/kitty/system/enums/war/warskill/skilldef/SKILLATTR.SKILLATTR_ICE : Lcom/kitty/system/enums/war/warskill/skilldef/SKILLATTR;
    //   150: invokevirtual getValue : ()I
    //   153: istore #10
    //   155: goto -> 186
    //   158: getstatic com/kitty/system/enums/war/warskill/skilldef/SKILLATTR.SKILLATTR_SLEEP : Lcom/kitty/system/enums/war/warskill/skilldef/SKILLATTR;
    //   161: invokevirtual getValue : ()I
    //   164: istore #10
    //   166: goto -> 186
    //   169: getstatic com/kitty/system/enums/war/warskill/skilldef/SKILLATTR.SKILLATTR_YIWANG : Lcom/kitty/system/enums/war/warskill/skilldef/SKILLATTR;
    //   172: invokevirtual getValue : ()I
    //   175: istore #10
    //   177: goto -> 186
    //   180: goto -> 186
    //   183: goto -> 54
    //   186: iload #8
    //   188: ifeq -> 197
    //   191: iload #10
    //   193: iconst_m1
    //   194: if_icmpne -> 198
    //   197: return
    //   198: aload #5
    //   200: iconst_0
    //   201: aaload
    //   202: checkcast com/kitty/system/game/skill/domain/RoleSkill
    //   205: astore #11
    //   207: iload #10
    //   209: aload #11
    //   211: invokevirtual getType : ()Ljava/lang/Integer;
    //   214: invokevirtual intValue : ()I
    //   217: if_icmpeq -> 221
    //   220: return
    //   221: ldc2_w 3.0
    //   224: aload #7
    //   226: invokevirtual getLevel : ()B
    //   229: aload #6
    //   231: invokevirtual getLevel : ()S
    //   234: invokestatic getTianFuCount : (BS)D
    //   237: dmul
    //   238: ldc2_w 100.0
    //   241: ddiv
    //   242: dstore #12
    //   244: dload #12
    //   246: invokestatic canAffectOnPro : (D)Z
    //   249: istore #14
    //   251: iload #14
    //   253: ifeq -> 330
    //   256: aload_1
    //   257: bipush #7
    //   259: aload_2
    //   260: invokevirtual getId : ()Ljava/lang/Integer;
    //   263: invokevirtual intValue : ()I
    //   266: i2l
    //   267: ldc 322504
    //   269: invokestatic addSkillRecord : (Lcom/kitty/system/game/fightnew/bean/Fight;IJI)Lcom/kitty/system/game/fightRecord/domain/SkillRecord;
    //   272: astore #15
    //   274: aload_3
    //   275: invokevirtual getStatusMap : ()Lorg/nutz/lang/util/NutMap;
    //   278: aload #9
    //   280: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   283: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   286: pop
    //   287: aload #4
    //   289: aload_3
    //   290: invokevirtual getId : ()Ljava/lang/Integer;
    //   293: invokevirtual intValue : ()I
    //   296: iconst_0
    //   297: invokevirtual gainTargetParam : (IZ)Lcom/kitty/system/game/fightnew/model/FightObjectParam;
    //   300: astore #16
    //   302: aload #16
    //   304: ifnull -> 330
    //   307: aload #16
    //   309: aload #9
    //   311: invokevirtual intValue : ()I
    //   314: invokevirtual addClearStatus : (I)V
    //   317: aload #15
    //   319: aload_3
    //   320: invokevirtual getId : ()Ljava/lang/Integer;
    //   323: invokevirtual intValue : ()I
    //   326: i2l
    //   327: invokevirtual setT : (J)V
    //   330: return
    // Line number table:
    //   Java source line number -> byte code offset
    //   #42	-> 0
    //   #44	-> 7
    //   #46	-> 8
    //   #47	-> 17
    //   #48	-> 26
    //   #49	-> 31
    //   #52	-> 32
    //   #53	-> 35
    //   #54	-> 41
    //   #55	-> 44
    //   #56	-> 76
    //   #57	-> 88
    //   #58	-> 91
    //   #59	-> 95
    //   #61	-> 136
    //   #62	-> 144
    //   #64	-> 147
    //   #65	-> 155
    //   #67	-> 158
    //   #68	-> 166
    //   #70	-> 169
    //   #71	-> 177
    //   #73	-> 180
    //   #77	-> 183
    //   #78	-> 186
    //   #79	-> 197
    //   #82	-> 198
    //   #83	-> 207
    //   #85	-> 220
    //   #88	-> 221
    //   #90	-> 244
    //   #91	-> 251
    //   #93	-> 256
    //   #96	-> 274
    //   #98	-> 287
    //   #99	-> 302
    //   #100	-> 307
    //   #102	-> 317
    //   #105	-> 330
    // Local variable table:
    //   start	length	slot	name	descriptor
    //   76	107	12	status	Ljava/lang/Integer;
    //   274	56	15	skillRecord	Lcom/kitty/system/game/fightRecord/domain/SkillRecord;
    //   302	28	16	targetParam	Lcom/kitty/system/game/fightnew/model/FightObjectParam;
    //   0	331	0	this	Lcom/kitty/system/game/fightnew/book/tianfu/loadobj/FU_322504;
    //   0	331	1	fight	Lcom/kitty/system/game/fightnew/bean/Fight;
    //   0	331	2	effector	Lcom/kitty/system/game/fightnew/FightObject;
    //   0	331	3	target	Lcom/kitty/system/game/fightnew/FightObject;
    //   0	331	4	actionDetailParam	Lcom/kitty/system/game/fightnew/model/ActionDetailParam;
    //   0	331	5	args	[Ljava/lang/Object;
    //   17	314	6	dayInCeFight	Lcom/kitty/system/game/fightnew/bean/DayInCeFight;
    //   26	305	7	tianFuObject	Lcom/kitty/system/game/fightnew/bean/TianFuObject;
    //   35	296	8	flag	Z
    //   41	290	9	theStatus	Ljava/lang/Integer;
    //   44	287	10	attr	I
    //   207	124	11	roleSkill	Lcom/kitty/system/game/skill/domain/RoleSkill;
    //   244	87	12	percenta	D
    //   251	80	14	flagTrigger	Z
  }
}
