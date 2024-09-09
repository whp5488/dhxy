package com.kitty.system.enums.task;

public enum TaskEvent
{
    AddNewTask(1), 
    FinishTask(2), 
    NewReward(3), 
    TeamFinishTask(4), 
    AddTaskItem(5), 
    DelTaskItem(6), 
    ChangeCounter(7), 
    ChangeShape(8), 
    DelItem(9), 
    TeamChangeCounter(10), 
    EnterFight(11), 
    SendUser(12), 
    CreateNpc(13), 
    DestroyNpc(14), 
    Add(20), 
    Set(21);
    
    private int id;
    
    private TaskEvent(int id) {
        this.id = id;
    }
    
    public int getId() {
        return this.id;
    }
}
