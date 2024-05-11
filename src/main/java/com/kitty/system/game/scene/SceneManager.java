
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
    private static final List<TaskWorker> workerPool;
    private static final Map<Integer, AOI> scenes;
    
    private SceneManager() {
        this.atomicInteger = new AtomicInteger();
    }
    
    public void initiate(final List<SysMapInfo> maps) {
        maps.forEach(gameMap -> {
            final AOI scene = new Scene();
            scene.setId(this.atomicInteger.getAndIncrement());
            SceneManager.scenes.put(gameMap.getMapSerial(), scene);
        });
    }
    
    private AOI getScene(final int mapId) {
        return SceneManager.scenes.get(mapId);
    }
    
    public void enterMap(final int mapId, final Role role) {
        final AOI scene = this.getScene(mapId);
        this.acceptTask(scene.getId(), () -> scene.enter(role));
    }
    
    public void leaveMap(final int mapId, final Role role) {
        final AOI scene = this.getScene(mapId);
        this.acceptTask(scene.getId(), () -> scene.leave(role.getId()));
    }
    
    public void movePosition(final Role role) {
        final int mapId = role.getPos().getMapId();
        final AOI scene = this.getScene(mapId);
        this.acceptTask(scene.getId(), () -> scene.move(role));
    }
    
    public void goToPosition(final Role role) {
        final int mapId = role.getPos().getMapId();
        final AOI scene = this.getScene(mapId);
        this.acceptTask(scene.getId(), () -> scene.goTo(role));
    }
    
    public void sendMessages(final Role role, final Message... messages) {
        final AOI scene = this.getScene(role.getPos().getMapId());
        this.acceptTask(scene.getId(), () -> scene.sendMessages(role, messages));
    }
    
    public void giveAppear(final Role role, final Set<Long> enters) {
        final AOI scene = this.getScene(role.getPos().getMapId());
        this.acceptTask(scene.getId(), () -> scene.giveAppear(role, enters));
    }
    
    public void acceptTask(final int sceneId, final Runnable task) {
        if (task == null) {
            return;
        }
        final int size = SceneManager.workerPool.size();
        final int index = sceneId % size;
        SceneManager.workerPool.get(index).addTask(task);
    }
    
    static {
        logger = LoggerFunction.SCENE.getLogger();
        workerPool = new ArrayList<TaskWorker>();
        scenes = new HashMap<Integer, AOI>();
        for (int i = 1; i <= 16; ++i) {
            final TaskWorker worker = new TaskWorker(i);
            SceneManager.workerPool.add(worker);
            new NamedThreadFactory("scene_" + i).newThread(worker).start();
        }
    }
    
    private static class TaskWorker implements Runnable
    {
        private int id;
        private BlockingQueue<Runnable> taskQueue;
        
        TaskWorker(final int index) {
            this.taskQueue = new LinkedBlockingQueue<Runnable>();
            this.id = index;
        }
        
        void addTask(final Runnable task) {
            this.taskQueue.add(task);
        }
        
        @Override
        public void run() {
            while (true) {
                try {
                    while (true) {
                        final Runnable task = this.taskQueue.take();
                        task.run();
                    }
                }
                catch (final Exception e) {
                    SceneManager.logger.error("Scene task error" + this.id, e);
                    continue;
                }
            }
        }
    }
}
