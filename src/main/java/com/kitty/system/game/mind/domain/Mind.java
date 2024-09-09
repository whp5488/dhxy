package com.kitty.system.game.mind.domain;

import com.kitty.common.db.Db4CommonService;
import java.util.Collections;
import java.util.ArrayList;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Default;
import com.kitty.system.game.vo.FieldProp;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("mind")
public class Mind extends BaseEntity<Long>
{
    @Comment("主键")
    @Id(auto = false)
    @Column("id")
    private long id;
    @Comment("玩家唯一ID")
    @Column("r_id")
    private long rId;
    @Comment("分类：初涉-1，熟路-2，登峰-3，造极-4")
    @Column("classify")
    private int classify;
    @Comment("编号如：心、贪、嗔、痴")
    @Column("serial")
    private int serial;
    @Comment("洗练次数")
    @Column("refine_num")
    private int refineNum;
    @Comment("洗练数据")
    @Column("refine")
    @Default("[]")
    private List<FieldProp> refine;
    @Column("refine_general_history")
    @Default("[]")
    @ColDefine(customType = "longtext")
    private List<FieldProp> refineGeneralHistory;
    @Column("refine_high_history")
    @Default("[]")
    @ColDefine(customType = "longtext")
    private List<List<FieldProp>> refineHighHistory;
    @Column("refine_super_history")
    @Default("[]")
    @ColDefine(customType = "longtext")
    private List<List<FieldProp>> refineSuperHistory;
    
    public Mind() {
        this.refineNum = 1;
        this.refine = Collections.synchronizedList(new ArrayList<>(3));
        this.refineGeneralHistory = new ArrayList<>();
        this.refineHighHistory = new ArrayList<>();
        this.refineSuperHistory = new ArrayList<>();
    }
    
    public Mind(long id, long rId, int classify, int serial) {
        this.refineNum = 1;
        this.refine = Collections.synchronizedList(new ArrayList<>(3));
        this.refineGeneralHistory = new ArrayList<>();
        this.refineHighHistory = new ArrayList<>();
        this.refineSuperHistory = new ArrayList<>();
        this.id = id;
        this.rId = rId;
        this.classify = classify;
        this.serial = serial;
    }
    
    public Mind(long id, long rId, int classify, int serial, int refineNum, List<FieldProp> refine) {
        this.refineNum = 1;
        this.refine = Collections.synchronizedList(new ArrayList<>(3));
        this.refineGeneralHistory = new ArrayList<>();
        this.refineHighHistory = new ArrayList<>();
        this.refineSuperHistory = new ArrayList<>();
        this.id = id;
        this.rId = rId;
        this.classify = classify;
        this.serial = serial;
        this.refineNum = refineNum;
        this.refine = refine;
    }
    
    @Override
    public Long getId() {
        return this.id;
    }
    
    public void save() {
        Db4CommonService.getInstance().add2Queue(this);
    }
    
    public long getRId() {
        return this.rId;
    }
    
    public int getClassify() {
        return this.classify;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getRefineNum() {
        return this.refineNum;
    }
    
    public List<FieldProp> getRefine() {
        return this.refine;
    }
    
    public List<FieldProp> getRefineGeneralHistory() {
        return this.refineGeneralHistory;
    }
    
    public List<List<FieldProp>> getRefineHighHistory() {
        return this.refineHighHistory;
    }
    
    public List<List<FieldProp>> getRefineSuperHistory() {
        return this.refineSuperHistory;
    }
    
    public void setId(final long id) {
        this.id = id;
    }
    
    public void setRId(final long rId) {
        this.rId = rId;
    }
    
    public void setClassify(final int classify) {
        this.classify = classify;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setRefineNum(final int refineNum) {
        this.refineNum = refineNum;
    }
    
    public void setRefine(final List<FieldProp> refine) {
        this.refine = refine;
    }
    
    public void setRefineGeneralHistory(final List<FieldProp> refineGeneralHistory) {
        this.refineGeneralHistory = refineGeneralHistory;
    }
    
    public void setRefineHighHistory(final List<List<FieldProp>> refineHighHistory) {
        this.refineHighHistory = refineHighHistory;
    }
    
    public void setRefineSuperHistory(final List<List<FieldProp>> refineSuperHistory) {
        this.refineSuperHistory = refineSuperHistory;
    }
}
