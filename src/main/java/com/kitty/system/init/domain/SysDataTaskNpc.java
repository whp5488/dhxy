
package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Default;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("data_task_npc")
public class SysDataTaskNpc
{
    @Column("serial")
    private int serial;
    @Column("scene")
    private int scene;
    @Column("x")
    private int x;
    @Column("y")
    private int y;
    @Column("dir")
    private int dir;
    @Column("shape")
    private int shape;
    @Column("name")
    private String name;
    @Column("shape_type")
    private int shapeType;
    @Column("talk")
    private String talk;
    @Column("types")
    @Default("[]")
    private List<Integer> types;
    @Column("is_scene")
    private int isScene;
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getScene() {
        return this.scene;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getDir() {
        return this.dir;
    }
    
    public int getShape() {
        return this.shape;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getShapeType() {
        return this.shapeType;
    }
    
    public String getTalk() {
        return this.talk;
    }
    
    public List<Integer> getTypes() {
        return this.types;
    }
    
    public int getIsScene() {
        return this.isScene;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setScene(final int scene) {
        this.scene = scene;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public void setDir(final int dir) {
        this.dir = dir;
    }
    
    public void setShape(final int shape) {
        this.shape = shape;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setShapeType(final int shapeType) {
        this.shapeType = shapeType;
    }
    
    public void setTalk(final String talk) {
        this.talk = talk;
    }
    
    public void setTypes(final List<Integer> types) {
        this.types = types;
    }
    
    public void setIsScene(final int isScene) {
        this.isScene = isScene;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataTaskNpc)) {
            return false;
        }
        final SysDataTaskNpc other = (SysDataTaskNpc)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        if (this.getScene() != other.getScene()) {
            return false;
        }
        if (this.getX() != other.getX()) {
            return false;
        }
        if (this.getY() != other.getY()) {
            return false;
        }
        if (this.getDir() != other.getDir()) {
            return false;
        }
        if (this.getShape() != other.getShape()) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0143: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0143;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0143;
            }
            return false;
        }
        if (this.getShapeType() != other.getShapeType()) {
            return false;
        }
        final Object this$talk = this.getTalk();
        final Object other$talk = other.getTalk();
        Label_0193: {
            if (this$talk == null) {
                if (other$talk == null) {
                    break Label_0193;
                }
            }
            else if (this$talk.equals(other$talk)) {
                break Label_0193;
            }
            return false;
        }
        final Object this$types = this.getTypes();
        final Object other$types = other.getTypes();
        if (this$types == null) {
            if (other$types == null) {
                return this.getIsScene() == other.getIsScene();
            }
        }
        else if (this$types.equals(other$types)) {
            return this.getIsScene() == other.getIsScene();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataTaskNpc;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getScene();
        result = result * 59 + this.getX();
        result = result * 59 + this.getY();
        result = result * 59 + this.getDir();
        result = result * 59 + this.getShape();
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getShapeType();
        final Object $talk = this.getTalk();
        result = result * 59 + (($talk == null) ? 43 : $talk.hashCode());
        final Object $types = this.getTypes();
        result = result * 59 + (($types == null) ? 43 : $types.hashCode());
        result = result * 59 + this.getIsScene();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataTaskNpc(serial=" + this.getSerial() + ", scene=" + this.getScene() + ", x=" + this.getX() + ", y=" + this.getY() + ", dir=" + this.getDir() + ", shape=" + this.getShape() + ", name=" + this.getName() + ", shapeType=" + this.getShapeType() + ", talk=" + this.getTalk() + ", types=" + this.getTypes() + ", isScene=" + this.getIsScene() + ")";
    }
}
