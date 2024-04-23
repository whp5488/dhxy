package com.kitty.system.init.domain;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_partner")
public class SysPartner implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Comment("主键")
  @Column("id")
  private int id;
  
  @Comment("编号")
  @Column("serial")
  private int serial;
  
  @Comment("伙伴名称")
  @Column("name")
  private String name;
  
  @Comment("是否可以招募")
  @Column("is_recruits")
  private byte isRecruits;
  
  @Comment("气血最大资质")
  @Column("hp_max")
  private int hpMax;
  
  @Comment("需要的等级")
  @Column("need_drade")
  private int needDrade;
  
  @Comment("默认使用的技能")
  @Column("oper_default_skill_id")
  private int operDefaultSkillId;
  
  @Comment("角色定位代码")
  @Column("pos_define")
  private int posDefine;
  
  @Comment("角色定位")
  @Column("position")
  private String position;
  
  @Comment("类型")
  @Column("type")
  private int type;
  
  @Comment("角色定位描述")
  @Column("position_desc")
  private String positionDesc;
  
  @Comment("优先级")
  @Column("priority")
  private int priority;
  
  @Comment("种族")
  @Column("race")
  private int race;
  
  @Comment("性别")
  @Column("sex")
  private int sex;
  
  @Comment("外观")
  @Column("icon")
  private int icon;
  
  @Comment("速度最大资质")
  @Column("speed")
  private int speed;
  
  @Comment("解锁需要的银两")
  @Column("need_yl")
  private int needYl;
  
  @Comment("和定位差不多，类型名字")
  @Column("type_name")
  private String typeName;
  
  @Comment("天赋1id")
  @Column("unique_aptitude1")
  private int uniqueAptitude1;
  
  @Comment("天赋1名称")
  @Column("unique_aptitude1_name")
  private String uniqueAptitude1Name;
  
  @Comment("天赋2id")
  @Column("unique_aptitude2")
  private int uniqueAptitude2;
  
  @Comment("天赋2名称")
  @Column("unique_aptitude2_name")
  private String uniqueAptitude2Name;
  
  @Comment("成长类型")
  @Column("upgrade_type")
  private int upgradeType;
  
  @Comment("是否带有天赋")
  @Column("nb_partner")
  private int nbPartner;
  
  @Comment("天赋配置编号")
  @Column("tfcfgidx")
  private int tfcfgidx;
  
  @Comment("解锁等级")
  @Column("auto_unlock_grade")
  private int autoUnlockGrade;
  
  @Comment("不知道干什么的,优先级？")
  @Column("conflict_priority")
  private int conflictPriority;
  
  @Comment("技能编号集合")
  @Column("skills")
  @Default("[]")
  private List<Integer> skills;
  
  @Comment("技能使用描述集合")
  @Column("skills_detail_json")
  private String skillsDetailJson;

}
