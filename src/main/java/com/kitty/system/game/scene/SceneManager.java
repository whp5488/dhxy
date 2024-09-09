package com.kitty.system.game.scene;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.BlockingQueue;
import com.kitty.common.thread.NamedThreadFactory;
import java.util.HashMap;
import java.util.ArrayList;
import com.kitty.logs.LoggerFunction;
import java.util.Set;
import com.kitty.mina.message.Message;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.scene.aoi.Scene;
import com.kitty.system.init.domain.SysMapInfo;
import com.kitty.system.game.scene.aoi.AOI;
import java.util.Map;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;

public enum SceneManager
{
    INSTANCE;
    
    private static final Logger logger;
    private static final int CORE_SIZE = 16;
    private final AtomicInteger atomicInteger;
    private static final List<SceneManager.TaskWorker> workerPool;
    private static final Map<Integer, AOI> scenes;
    
    private SceneManager() {
        this.atomicInteger = new AtomicInteger();
    }
    
    public void initiate(List<SysMapInfo> maps) {
        maps.forEach(gameMap -> {
            AOI scene = new Scene();
            scene.setId(this.atomicInteger.getAndIncrement());
            SceneManager.scenes.put(gameMap.getMapSerial(), scene);
            return;
        });
    }
    
    private AOI getScene(int mapId) {
        return (AOI)SceneManager.scenes.get(Integer.valueOf(mapId));
    }
    
    public void enterMap(int mapId, Role role) {
        AOI scene = this.getScene(mapId);
        this.acceptTask(scene.getId(), () -> scene.enter(role));
    }
    
    public void leaveMap(int mapId, Role role) {
        AOI scene = this.getScene(mapId);
        this.acceptTask(scene.getId(), () -> scene.leave((long)role.getId()));
    }
    
    public void movePosition(Role role) {
        int mapId = (int)role.getPos().getMapId();
        AOI scene = this.getScene(mapId);
        this.acceptTask(scene.getId(), () -> scene.move(role));
    }
    
    public void goToPosition(Role role) {
        int mapId = (int)role.getPos().getMapId();
        AOI scene = this.getScene(mapId);
        this.acceptTask(scene.getId(), () -> scene.goTo(role));
    }
    
    public void sendMessages(Role role, Message... messages) {
        AOI scene = this.getScene((int)role.getPos().getMapId());
        this.acceptTask(scene.getId(), () -> scene.sendMessages(role, messages));
    }
    
    public void giveAppear(Role role, Set<Long> enters) {
        AOI scene = this.getScene((int)role.getPos().getMapId());
        this.acceptTask(scene.getId(), () -> scene.giveAppear(role, enters));
    }
    
    public void acceptTask(int sceneId, Runnable task) {
        if (task == null) {
            return;
        }
        int size = SceneManager.workerPool.size();
        int index = sceneId % size;
        ((SceneManager.TaskWorker)SceneManager.workerPool.get(index)).addTask(task);
        return;
    }
    
    static {
        logger = LoggerFunction.SCENE.getLogger();
        workerPool = new ArrayList<>();
        scenes = new HashMap();
        for (int i = 1; i <= 16; ++i) {
            SceneManager.TaskWorker worker = new TaskWorker(i);
            SceneManager.workerPool.add(worker);
            new NamedThreadFactory("scene_" + i).newThread(worker).start();
        }
    }
    
    private static class TaskWorker implements Runnable
    {
        private int id;
        private BlockingQueue<Runnable> taskQueue;
        
        TaskWorker(int index) {
            this.taskQueue = new LinkedBlockingQueue();
            this.id = index;
        }
        
        void addTask(Runnable task) {
            this.taskQueue.add(task);
        }
        
        @Override
        public void run() {
            while (true) {
                try {
                    Runnable task = (Runnable)this.taskQueue.take();
                    task.run();
                    continue;
                }
                catch (Exception e) {
                    SceneManager.logger.error("Scene task error" + this.id, e);
                }
            }
        }
    }
}
