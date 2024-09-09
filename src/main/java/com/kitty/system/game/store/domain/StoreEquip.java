package com.kitty.system.game.store.domain;

import com.kitty.common.db.DbBagOrStoreService;
import java.util.ArrayList;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Default;
import com.kitty.system.game.vo.FiedEquip;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("store_equip")
public class StoreEquip extends BaseEntity<Long>
{
    @Id(auto = false)
    private Long id;
    @Comment("角色编号")
    @Column("r_id")
    private long rId;
    @Comment("装备名称")
    @Column("name")
    private String name;
    @Comment("是否绑定")
    @Column("bd")
    private int bd;
    @Comment("物品编号")
    @Column("type")
    private int type;
    @Comment("背包的位置")
    @Column("pos")
    private int pos;
    @Comment("佩戴位置")
    @Column("position")
    private int position;
    @Comment("炼器进度")
    @Column("refining")
    private double refining;
    @Comment("品阶：配饰独有")
    @Column("product_order")
    private byte productOrder;
    @Column("field")
    @Default("[]")
    @ColDefine(customType = "longtext")
    private List<FiedEquip> field;
    @Column("artifice")
    @Default("[]")
    @ColDefine(customType = "longtext")
    private List<FiedEquip> artifice;
    @Column("artifice_general_history")
    @Default("[]")
    @ColDefine(customType = "longtext")
    private List<FiedEquip> artificeGeneralHistory;
    @Column("artifice_high_history")
    @Default("[]")
    @ColDefine(customType = "longtext")
    private List<List<FiedEquip>> artificeHighHistory;
    @Column("artifice_super_history")
    @Default("[]")
    @ColDefine(customType = "longtext")
    private List<List<FiedEquip>> artificeSuperHistory;
    @Column("redo_general_history")
    @Default("[]")
    @ColDefine(customType = "longtext")
    private List<FiedEquip> redoGeneralHistory;
    @Column("redo_high_history")
    @Default("[]")
    @ColDefine(customType = "longtext")
    private List<List<FiedEquip>> redoHighHistory;
    @Column("redo_super_history")
    @Default("[]")
    @ColDefine(customType = "longtext")
    private List<List<FiedEquip>> redoSuperHistory;
    @Column("purify_general_history")
    @Default("[]")
    @ColDefine(customType = "longtext")
    private List<FiedEquip> purifyGeneralHistory;
    @Column("purify_high_history")
    @Default("[]")
    @ColDefine(customType = "longtext")
    private List<List<FiedEquip>> purifyHighHistory;
    @Column("purify_super_history")
    @Default("[]")
    @ColDefine(customType = "longtext")
    private List<List<FiedEquip>> purifySuperHistory;
    
    public StoreEquip() {
        this.refining = 0.0;
        this.productOrder = 0;
        this.field = new ArrayList<>();
        this.artifice = new ArrayList<>();
        this.artificeGeneralHistory = new ArrayList<>();
        this.artificeHighHistory = new ArrayList<>();
        this.artificeSuperHistory = new ArrayList<>();
        this.redoGeneralHistory = new ArrayList<>();
        this.redoHighHistory = new ArrayList<>();
        this.redoSuperHistory = new ArrayList<>();
        this.purifyGeneralHistory = new ArrayList<>();
        this.purifyHighHistory = new ArrayList<>();
        this.purifySuperHistory = new ArrayList<>();
    }
    
    public void save() {
        DbBagOrStoreService.getInstance().add2Queue(this);
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
    
    public int getBd() {
        return this.bd;
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getPos() {
        return this.pos;
    }
    
    public int getPosition() {
        return this.position;
    }
    
    public double getRefining() {
        return this.refining;
    }
    
    public byte getProductOrder() {
        return this.productOrder;
    }
    
    public List<FiedEquip> getField() {
        return this.field;
    }
    
    public List<FiedEquip> getArtifice() {
        return this.artifice;
    }
    
    public List<FiedEquip> getArtificeGeneralHistory() {
        return this.artificeGeneralHistory;
    }
    
    public List<List<FiedEquip>> getArtificeHighHistory() {
        return this.artificeHighHistory;
    }
    
    public List<List<FiedEquip>> getArtificeSuperHistory() {
        return this.artificeSuperHistory;
    }
    
    public List<FiedEquip> getRedoGeneralHistory() {
        return this.redoGeneralHistory;
    }
    
    public List<List<FiedEquip>> getRedoHighHistory() {
        return this.redoHighHistory;
    }
    
    public List<List<FiedEquip>> getRedoSuperHistory() {
        return this.redoSuperHistory;
    }
    
    public List<FiedEquip> getPurifyGeneralHistory() {
        return this.purifyGeneralHistory;
    }
    
    public List<List<FiedEquip>> getPurifyHighHistory() {
        return this.purifyHighHistory;
    }
    
    public List<List<FiedEquip>> getPurifySuperHistory() {
        return this.purifySuperHistory;
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
    
    public void setBd(final int bd) {
        this.bd = bd;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setPos(final int pos) {
        this.pos = pos;
    }
    
    public void setPosition(final int position) {
        this.position = position;
    }
    
    public void setRefining(final double refining) {
        this.refining = refining;
    }
    
    public void setProductOrder(final byte productOrder) {
        this.productOrder = productOrder;
    }
    
    public void setField(final List<FiedEquip> field) {
        this.field = field;
    }
    
    public void setArtifice(final List<FiedEquip> artifice) {
        this.artifice = artifice;
    }
    
    public void setArtificeGeneralHistory(final List<FiedEquip> artificeGeneralHistory) {
        this.artificeGeneralHistory = artificeGeneralHistory;
    }
    
    public void setArtificeHighHistory(final List<List<FiedEquip>> artificeHighHistory) {
        this.artificeHighHistory = artificeHighHistory;
    }
    
    public void setArtificeSuperHistory(final List<List<FiedEquip>> artificeSuperHistory) {
        this.artificeSuperHistory = artificeSuperHistory;
    }
    
    public void setRedoGeneralHistory(final List<FiedEquip> redoGeneralHistory) {
        this.redoGeneralHistory = redoGeneralHistory;
    }
    
    public void setRedoHighHistory(final List<List<FiedEquip>> redoHighHistory) {
        this.redoHighHistory = redoHighHistory;
    }
    
    public void setRedoSuperHistory(final List<List<FiedEquip>> redoSuperHistory) {
        this.redoSuperHistory = redoSuperHistory;
    }
    
    public void setPurifyGeneralHistory(final List<FiedEquip> purifyGeneralHistory) {
        this.purifyGeneralHistory = purifyGeneralHistory;
    }
    
    public void setPurifyHighHistory(final List<List<FiedEquip>> purifyHighHistory) {
        this.purifyHighHistory = purifyHighHistory;
    }
    
    public void setPurifySuperHistory(final List<List<FiedEquip>> purifySuperHistory) {
        this.purifySuperHistory = purifySuperHistory;
    }
    
    @Override
    public String toString() {
        return "StoreEquip(id=" + this.getId() + ", rId=" + this.getRId() + ", name=" + this.getName() + ", bd=" + this.getBd() + ", type=" + this.getType() + ", pos=" + this.getPos() + ", position=" + this.getPosition() + ", refining=" + this.getRefining() + ", productOrder=" + (int)this.getProductOrder() + ", field=" + this.getField() + ", artifice=" + this.getArtifice() + ", artificeGeneralHistory=" + this.getArtificeGeneralHistory() + ", artificeHighHistory=" + this.getArtificeHighHistory() + ", artificeSuperHistory=" + this.getArtificeSuperHistory() + ", redoGeneralHistory=" + this.getRedoGeneralHistory() + ", redoHighHistory=" + this.getRedoHighHistory() + ", redoSuperHistory=" + this.getRedoSuperHistory() + ", purifyGeneralHistory=" + this.getPurifyGeneralHistory() + ", purifyHighHistory=" + this.getPurifyHighHistory() + ", purifySuperHistory=" + this.getPurifySuperHistory() + ")";
    }
}
