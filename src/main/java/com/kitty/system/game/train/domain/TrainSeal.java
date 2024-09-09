package com.kitty.system.game.train.domain;

import com.kitty.common.db.DbTrainService;
import java.util.ArrayList;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Default;
import com.kitty.system.game.train.domain.vo.FieldSeal;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("train_seal")
public class TrainSeal extends BaseEntity<Long>
{
    @Id(auto = false)
    @Comment("主键")
    @Column("id")
    private Long id;
    @Comment("角色ID")
    @Column("r_id")
    private long rId;
    @Comment("信物名称")
    @Column("name")
    private String name;
    @Comment("信物编号")
    @Column("serial")
    private int serial;
    @Comment("是否绑定（否 -1，是 1）")
    @Column("bd")
    private int bd;
    @Comment("佩戴位置：0.未佩戴 1.乾 2.巽 3.坎 4.艮 5.坤 6.震 7.离 8.兑")
    @Column("position")
    private short position;
    @Column("refine")
    @Default("[]")
    @ColDefine(customType = "longtext")
    private List<FieldSeal> refine;
    @Column("refine_general_history")
    @Default("[]")
    @ColDefine(customType = "longtext")
    private List<FieldSeal> refineGeneralHistory;
    @Column("refine_high_history")
    @Default("[]")
    @ColDefine(customType = "longtext")
    private List<List<FieldSeal>> refineHighHistory;
    @Column("refine_super_history")
    @Default("[]")
    @ColDefine(customType = "longtext")
    private List<List<FieldSeal>> refineSuperHistory;
    
    public TrainSeal() {
        this.position = 0;
        this.refine = new ArrayList<>();
        this.refineGeneralHistory = new ArrayList<>();
        this.refineHighHistory = new ArrayList<>();
        this.refineSuperHistory = new ArrayList<>();
    }
    
    public TrainSeal(Long id, long rId, String name, int serial, List<FieldSeal> refine) {
        this.position = 0;
        this.refine = new ArrayList<>();
        this.refineGeneralHistory = new ArrayList<>();
        this.refineHighHistory = new ArrayList<>();
        this.refineSuperHistory = new ArrayList<>();
        this.id = id;
        this.rId = rId;
        this.name = name;
        this.serial = serial;
        this.refine = refine;
    }
    
    public void save() {
        DbTrainService.getInstance().add2Queue(this);
    }
    
    @Override
    public Long getId() {
        return this.id;
    }
    
    public long getRId() {
        return this.rId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getBd() {
        return this.bd;
    }
    
    public short getPosition() {
        return this.position;
    }
    
    public List<FieldSeal> getRefine() {
        return this.refine;
    }
    
    public List<FieldSeal> getRefineGeneralHistory() {
        return this.refineGeneralHistory;
    }
    
    public List<List<FieldSeal>> getRefineHighHistory() {
        return this.refineHighHistory;
    }
    
    public List<List<FieldSeal>> getRefineSuperHistory() {
        return this.refineSuperHistory;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setRId(final long rId) {
        this.rId = rId;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setBd(final int bd) {
        this.bd = bd;
    }
    
    public void setPosition(final short position) {
        this.position = position;
    }
    
    public void setRefine(final List<FieldSeal> refine) {
        this.refine = refine;
    }
    
    public void setRefineGeneralHistory(final List<FieldSeal> refineGeneralHistory) {
        this.refineGeneralHistory = refineGeneralHistory;
    }
    
    public void setRefineHighHistory(final List<List<FieldSeal>> refineHighHistory) {
        this.refineHighHistory = refineHighHistory;
    }
    
    public void setRefineSuperHistory(final List<List<FieldSeal>> refineSuperHistory) {
        this.refineSuperHistory = refineSuperHistory;
    }
}
