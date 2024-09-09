package com.kitty.system.game.equip.domain;

import com.kitty.common.db.Db4PetEquipService;
import java.util.ArrayList;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Default;
import com.kitty.system.game.vo.FiedEquip;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("pet_equip")
public class PetEquip extends BaseEntity<Long>
{
    @Id(auto = false)
    private Long id;
    @Column("r_id")
    private long rId;
    @Column("p_id")
    private long pId;
    @Column("name")
    private String name;
    @Column("bd")
    private int bd;
    @Column("serial")
    private int serial;
    @Column("position")
    private int position;
    @Column("product_order")
    private byte productOrder;
    @Column("field")
    @Default("[]")
    @ColDefine(customType = "longtext")
    private List<FiedEquip> field;
    @Column("position_num")
    private short positionNum;
    @Column("lock_state")
    private byte lock;
    
    public PetEquip(String name, int bd, int serial, int position, byte productOrder, List<FiedEquip> field) {
        this.pId = 0L;
        this.field = new ArrayList<>();
        this.positionNum = 2;
        this.lock = 0;
        this.name = name;
        this.bd = bd;
        this.serial = serial;
        this.position = position;
        this.productOrder = productOrder;
        this.field = field;
    }
    
    public PetEquip(Long id, long rId, long pId, String name, int bd, int serial, int position, byte productOrder, List<FiedEquip> field, short positionNum, byte lock) {
        this.pId = 0L;
        this.field = new ArrayList<>();
        this.positionNum = 2;
        this.lock = 0;
        this.id = id;
        this.rId = rId;
        this.pId = pId;
        this.name = name;
        this.bd = bd;
        this.serial = serial;
        this.position = position;
        this.productOrder = productOrder;
        this.field = field;
        this.positionNum = positionNum;
        this.lock = lock;
    }
    
    public PetEquip() {
        this.pId = 0L;
        this.field = new ArrayList<>();
        this.positionNum = 2;
        this.lock = 0;
    }
    
    public void save() {
        Db4PetEquipService.getInstance().add2Queue(this);
    }
    
    @Override
    public Long getId() {
        return this.id;
    }
    
    public long getRId() {
        return this.rId;
    }
    
    public long getPId() {
        return this.pId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getBd() {
        return this.bd;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getPosition() {
        return this.position;
    }
    
    public byte getProductOrder() {
        return this.productOrder;
    }
    
    public List<FiedEquip> getField() {
        return this.field;
    }
    
    public short getPositionNum() {
        return this.positionNum;
    }
    
    public byte getLock() {
        return this.lock;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setRId(final long rId) {
        this.rId = rId;
    }
    
    public void setPId(final long pId) {
        this.pId = pId;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setBd(final int bd) {
        this.bd = bd;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setPosition(final int position) {
        this.position = position;
    }
    
    public void setProductOrder(final byte productOrder) {
        this.productOrder = productOrder;
    }
    
    public void setField(final List<FiedEquip> field) {
        this.field = field;
    }
    
    public void setPositionNum(final short positionNum) {
        this.positionNum = positionNum;
    }
    
    public void setLock(final byte lock) {
        this.lock = lock;
    }
}
