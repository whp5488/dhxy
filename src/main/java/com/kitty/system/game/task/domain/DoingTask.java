
package com.kitty.system.game.task.domain;

import java.beans.ConstructorProperties;
import java.util.Collections;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import com.kitty.system.game.vo.EasyNpc;
import java.util.List;
import java.io.Serializable;

public class DoingTask implements Serializable
{
    private int taskId;
    private int targetProgress;
    private int fightId;
    private int npcId;
    private List<EasyNpc> easyNpcList;
    private int finishedTask;
    private ConcurrentHashMap<Integer, Integer> changeCounter;
    private String para;
    private int create;
    private int update;
    private int expire;
    
    public DoingTask(final int taskId, final int targetProgress, final int expire) {
        this.targetProgress = 0;
        this.easyNpcList = Collections.synchronizedList(new ArrayList<EasyNpc>());
        this.finishedTask = 0;
        this.changeCounter = new ConcurrentHashMap<Integer, Integer>();
        this.taskId = taskId;
        this.targetProgress = targetProgress;
        this.create = new Long(System.currentTimeMillis() / 1000L).intValue();
        this.update = this.create;
        this.expire = expire;
    }
    
    public void addProgress() {
        ++this.targetProgress;
        this.update = new Long(System.currentTimeMillis() / 1000L).intValue();
    }
    
    public int getTaskId() {
        return this.taskId;
    }
    
    public int getTargetProgress() {
        return this.targetProgress;
    }
    
    public int getFightId() {
        return this.fightId;
    }
    
    public int getNpcId() {
        return this.npcId;
    }
    
    public List<EasyNpc> getEasyNpcList() {
        return this.easyNpcList;
    }
    
    public int getFinishedTask() {
        return this.finishedTask;
    }
    
    public ConcurrentHashMap<Integer, Integer> getChangeCounter() {
        return this.changeCounter;
    }
    
    public String getPara() {
        return this.para;
    }
    
    public int getCreate() {
        return this.create;
    }
    
    public int getUpdate() {
        return this.update;
    }
    
    public int getExpire() {
        return this.expire;
    }
    
    public void setTaskId(final int taskId) {
        this.taskId = taskId;
    }
    
    public void setTargetProgress(final int targetProgress) {
        this.targetProgress = targetProgress;
    }
    
    public void setFightId(final int fightId) {
        this.fightId = fightId;
    }
    
    public void setNpcId(final int npcId) {
        this.npcId = npcId;
    }
    
    public void setEasyNpcList(final List<EasyNpc> easyNpcList) {
        this.easyNpcList = easyNpcList;
    }
    
    public void setFinishedTask(final int finishedTask) {
        this.finishedTask = finishedTask;
    }
    
    public void setChangeCounter(final ConcurrentHashMap<Integer, Integer> changeCounter) {
        this.changeCounter = changeCounter;
    }
    
    public void setPara(final String para) {
        this.para = para;
    }
    
    public void setCreate(final int create) {
        this.create = create;
    }
    
    public void setUpdate(final int update) {
        this.update = update;
    }
    
    public void setExpire(final int expire) {
        this.expire = expire;
    }
    
    public DoingTask() {
        this.targetProgress = 0;
        this.easyNpcList = Collections.synchronizedList(new ArrayList<EasyNpc>());
        this.finishedTask = 0;
        this.changeCounter = new ConcurrentHashMap<Integer, Integer>();
    }
    
    @ConstructorProperties({ "taskId", "targetProgress", "fightId", "npcId", "easyNpcList", "finishedTask", "changeCounter", "para", "create", "update", "expire" })
    public DoingTask(final int taskId, final int targetProgress, final int fightId, final int npcId, final List<EasyNpc> easyNpcList, final int finishedTask, final ConcurrentHashMap<Integer, Integer> changeCounter, final String para, final int create, final int update, final int expire) {
        this.targetProgress = 0;
        this.easyNpcList = Collections.synchronizedList(new ArrayList<EasyNpc>());
        this.finishedTask = 0;
        this.changeCounter = new ConcurrentHashMap<Integer, Integer>();
        this.taskId = taskId;
        this.targetProgress = targetProgress;
        this.fightId = fightId;
        this.npcId = npcId;
        this.easyNpcList = easyNpcList;
        this.finishedTask = finishedTask;
        this.changeCounter = changeCounter;
        this.para = para;
        this.create = create;
        this.update = update;
        this.expire = expire;
    }
    
    @Override
    public String toString() {
        return "DoingTask(taskId=" + this.getTaskId() + ", targetProgress=" + this.getTargetProgress() + ", fightId=" + this.getFightId() + ", npcId=" + this.getNpcId() + ", easyNpcList=" + this.getEasyNpcList() + ", finishedTask=" + this.getFinishedTask() + ", changeCounter=" + this.getChangeCounter() + ", para=" + this.getPara() + ", create=" + this.getCreate() + ", update=" + this.getUpdate() + ", expire=" + this.getExpire() + ")";
    }
}
