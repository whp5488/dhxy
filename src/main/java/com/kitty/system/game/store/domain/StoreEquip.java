package com.kitty.system.game.store.domain;

import com.kitty.common.db.BaseEntity;
import com.kitty.common.db.DbBagOrStoreService;
import com.kitty.system.game.vo.FiedEquip;
import java.util.ArrayList;
import java.util.List;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("store_equip")
public class StoreEquip extends BaseEntity<Long> {
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
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public void setRId(long rId) {
    this.rId = rId;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setBd(int bd) {
    this.bd = bd;
  }
  
  public void setType(int type) {
    this.type = type;
  }
  
  public void setPos(int pos) {
    this.pos = pos;
  }
  
  public void setPosition(int position) {
    this.position = position;
  }
  
  public void setRefining(double refining) {
    this.refining = refining;
  }
  
  public void setProductOrder(byte productOrder) {
    this.productOrder = productOrder;
  }
  
  public void setField(List<FiedEquip> field) {
    this.field = field;
  }
  
  public void setArtifice(List<FiedEquip> artifice) {
    this.artifice = artifice;
  }
  
  public void setArtificeGeneralHistory(List<FiedEquip> artificeGeneralHistory) {
    this.artificeGeneralHistory = artificeGeneralHistory;
  }
  
  public void setArtificeHighHistory(List<List<FiedEquip>> artificeHighHistory) {
    this.artificeHighHistory = artificeHighHistory;
  }
  
  public void setArtificeSuperHistory(List<List<FiedEquip>> artificeSuperHistory) {
    this.artificeSuperHistory = artificeSuperHistory;
  }
  
  public void setRedoGeneralHistory(List<FiedEquip> redoGeneralHistory) {
    this.redoGeneralHistory = redoGeneralHistory;
  }
  
  public void setRedoHighHistory(List<List<FiedEquip>> redoHighHistory) {
    this.redoHighHistory = redoHighHistory;
  }
  
  public void setRedoSuperHistory(List<List<FiedEquip>> redoSuperHistory) {
    this.redoSuperHistory = redoSuperHistory;
  }
  
  public void setPurifyGeneralHistory(List<FiedEquip> purifyGeneralHistory) {
    this.purifyGeneralHistory = purifyGeneralHistory;
  }
  
  public void setPurifyHighHistory(List<List<FiedEquip>> purifyHighHistory) {
    this.purifyHighHistory = purifyHighHistory;
  }
  
  public void setPurifySuperHistory(List<List<FiedEquip>> purifySuperHistory) {
    this.purifySuperHistory = purifySuperHistory;
  }
  
  public String toString() {
    return "StoreEquip(id=" + getId() + ", rId=" + getRId() + ", name=" + getName() + ", bd=" + getBd() + ", type=" + getType() + ", pos=" + getPos() + ", position=" + getPosition() + ", refining=" + getRefining() + ", productOrder=" + getProductOrder() + ", field=" + getField() + ", artifice=" + getArtifice() + ", artificeGeneralHistory=" + getArtificeGeneralHistory() + ", artificeHighHistory=" + getArtificeHighHistory() + ", artificeSuperHistory=" + getArtificeSuperHistory() + ", redoGeneralHistory=" + getRedoGeneralHistory() + ", redoHighHistory=" + getRedoHighHistory() + ", redoSuperHistory=" + getRedoSuperHistory() + ", purifyGeneralHistory=" + getPurifyGeneralHistory() + ", purifyHighHistory=" + getPurifyHighHistory() + ", purifySuperHistory=" + getPurifySuperHistory() + ")";
  }
  
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
  
  @Comment("炼器进度")
  @Column("refining")
  private double refining = 0.0D;
  
  public double getRefining() {
    return this.refining;
  }
  
  @Comment("品阶：配饰独有")
  @Column("product_order")
  private byte productOrder = 0;
  
  public byte getProductOrder() {
    return this.productOrder;
  }
  
  @Column("field")
  @Default("[]")
  @ColDefine(customType = "longtext")
  private List<FiedEquip> field = new ArrayList<>();
  
  public List<FiedEquip> getField() {
    return this.field;
  }
  
  @Column("artifice")
  @Default("[]")
  @ColDefine(customType = "longtext")
  private List<FiedEquip> artifice = new ArrayList<>();
  
  public List<FiedEquip> getArtifice() {
    return this.artifice;
  }
  
  @Column("artifice_general_history")
  @Default("[]")
  @ColDefine(customType = "longtext")
  private List<FiedEquip> artificeGeneralHistory = new ArrayList<>();
  
  public List<FiedEquip> getArtificeGeneralHistory() {
    return this.artificeGeneralHistory;
  }
  
  @Column("artifice_high_history")
  @Default("[]")
  @ColDefine(customType = "longtext")
  private List<List<FiedEquip>> artificeHighHistory = new ArrayList<>();
  
  public List<List<FiedEquip>> getArtificeHighHistory() {
    return this.artificeHighHistory;
  }
  
  @Column("artifice_super_history")
  @Default("[]")
  @ColDefine(customType = "longtext")
  private List<List<FiedEquip>> artificeSuperHistory = new ArrayList<>();
  
  public List<List<FiedEquip>> getArtificeSuperHistory() {
    return this.artificeSuperHistory;
  }
  
  @Column("redo_general_history")
  @Default("[]")
  @ColDefine(customType = "longtext")
  private List<FiedEquip> redoGeneralHistory = new ArrayList<>();
  
  public List<FiedEquip> getRedoGeneralHistory() {
    return this.redoGeneralHistory;
  }
  
  @Column("redo_high_history")
  @Default("[]")
  @ColDefine(customType = "longtext")
  private List<List<FiedEquip>> redoHighHistory = new ArrayList<>();
  
  public List<List<FiedEquip>> getRedoHighHistory() {
    return this.redoHighHistory;
  }
  
  @Column("redo_super_history")
  @Default("[]")
  @ColDefine(customType = "longtext")
  private List<List<FiedEquip>> redoSuperHistory = new ArrayList<>();
  
  public List<List<FiedEquip>> getRedoSuperHistory() {
    return this.redoSuperHistory;
  }
  
  @Column("purify_general_history")
  @Default("[]")
  @ColDefine(customType = "longtext")
  private List<FiedEquip> purifyGeneralHistory = new ArrayList<>();
  
  public List<FiedEquip> getPurifyGeneralHistory() {
    return this.purifyGeneralHistory;
  }
  
  @Column("purify_high_history")
  @Default("[]")
  @ColDefine(customType = "longtext")
  private List<List<FiedEquip>> purifyHighHistory = new ArrayList<>();
  
  public List<List<FiedEquip>> getPurifyHighHistory() {
    return this.purifyHighHistory;
  }
  
  @Column("purify_super_history")
  @Default("[]")
  @ColDefine(customType = "longtext")
  private List<List<FiedEquip>> purifySuperHistory = new ArrayList<>();
  
  public List<List<FiedEquip>> getPurifySuperHistory() {
    return this.purifySuperHistory;
  }
  
  public void save() {
    DbBagOrStoreService.getInstance().add2Queue(this);
  }
}
