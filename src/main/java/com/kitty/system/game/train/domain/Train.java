package com.kitty.system.game.train.domain;

import java.util.List;
import java.util.Collections;
import java.util.Arrays;
import com.kitty.common.db.DbTrainService;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("train")
public class Train extends BaseEntity<Long>
{
    @Id(auto = false)
    @Comment("主键")
    @Column("id")
    private Long id;
    @Comment("角色ID")
    @Column("r_id")
    private long rId;
    @Comment("兑位等级")
    @Column("dui_grade")
    private int duiGrade;
    @Comment("艮位等级")
    @Column("gen_grade")
    private int genGrade;
    @Comment("坎位等级")
    @Column("kan_grade")
    private int kanGrade;
    @Comment("坤位等级")
    @Column("kun_grade")
    private int kunGrade;
    @Comment("离位等级")
    @Column("li_grade")
    private int liGrade;
    @Comment("乾位等级")
    @Column("qian_grade")
    private int qianGrade;
    @Comment("巽位等级")
    @Column("xun_grade")
    private int xunGrade;
    @Comment("震位等级")
    @Column("zhen_grade")
    private int zhenGrade;
    @Comment("法阵")
    @Column("magic_circle")
    private int magicCircle;
    @Comment("洗练度")
    @Column("refine")
    private int refine;
    
    public Train() {
        this.duiGrade = 0;
        this.genGrade = 0;
        this.kanGrade = 0;
        this.kunGrade = 0;
        this.liGrade = 0;
        this.qianGrade = 0;
        this.xunGrade = 0;
        this.zhenGrade = 0;
        this.magicCircle = 0;
        this.refine = 0;
    }
    
    public Train(Long id, long rId) {
        this.duiGrade = 0;
        this.genGrade = 0;
        this.kanGrade = 0;
        this.kunGrade = 0;
        this.liGrade = 0;
        this.qianGrade = 0;
        this.xunGrade = 0;
        this.zhenGrade = 0;
        this.magicCircle = 0;
        this.refine = 0;
        this.id = id;
        this.rId = rId;
    }
    
    public void save() {
        DbTrainService.getInstance().add2Queue(this);
    }
    
    public int getMin() {
        List<Integer> list = Arrays.asList(this.qianGrade, this.xunGrade, this.kanGrade, this.genGrade, this.kunGrade, this.zhenGrade, this.liGrade, this.duiGrade);
        return Collections.min(list);
    }
    
    public int getPosLevelForNum(int posNum) {
        switch (posNum) {
            case 1: {
                return this.qianGrade;
            }
            case 2: {
                return this.xunGrade;
            }
            case 3: {
                return this.kanGrade;
            }
            case 4: {
                return this.genGrade;
            }
            case 5: {
                return this.kunGrade;
            }
            case 6: {
                return this.zhenGrade;
            }
            case 8: {
                return this.liGrade;
            }
            case 9: {
                return this.duiGrade;
            }
            default: {
                return 0;
            }
        }
    }
    
    @Override
    public Long getId() {
        return this.id;
    }
    
    public long getRId() {
        return this.rId;
    }
    
    public int getDuiGrade() {
        return this.duiGrade;
    }
    
    public int getGenGrade() {
        return this.genGrade;
    }
    
    public int getKanGrade() {
        return this.kanGrade;
    }
    
    public int getKunGrade() {
        return this.kunGrade;
    }
    
    public int getLiGrade() {
        return this.liGrade;
    }
    
    public int getQianGrade() {
        return this.qianGrade;
    }
    
    public int getXunGrade() {
        return this.xunGrade;
    }
    
    public int getZhenGrade() {
        return this.zhenGrade;
    }
    
    public int getMagicCircle() {
        return this.magicCircle;
    }
    
    public int getRefine() {
        return this.refine;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setRId(final long rId) {
        this.rId = rId;
    }
    
    public void setDuiGrade(final int duiGrade) {
        this.duiGrade = duiGrade;
    }
    
    public void setGenGrade(final int genGrade) {
        this.genGrade = genGrade;
    }
    
    public void setKanGrade(final int kanGrade) {
        this.kanGrade = kanGrade;
    }
    
    public void setKunGrade(final int kunGrade) {
        this.kunGrade = kunGrade;
    }
    
    public void setLiGrade(final int liGrade) {
        this.liGrade = liGrade;
    }
    
    public void setQianGrade(final int qianGrade) {
        this.qianGrade = qianGrade;
    }
    
    public void setXunGrade(final int xunGrade) {
        this.xunGrade = xunGrade;
    }
    
    public void setZhenGrade(final int zhenGrade) {
        this.zhenGrade = zhenGrade;
    }
    
    public void setMagicCircle(final int magicCircle) {
        this.magicCircle = magicCircle;
    }
    
    public void setRefine(final int refine) {
        this.refine = refine;
    }
}
