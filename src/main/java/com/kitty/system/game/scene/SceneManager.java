package com.kitty.system.game.scene;

import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.logs.LoggerFunction;
import com.kitty.mina.message.Message;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.scene.aoi.AOI;
import com.kitty.system.game.scene.aoi.Scene;
import com.kitty.system.init.domain.SysMapInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;

public enum SceneManager {
  INSTANCE;
  
  private static final Map<Integer, AOI> scenes;
  
  private static final List<TaskWorker> workerPool;
  
  private final AtomicInteger atomicInteger;
  
  private static final int CORE_SIZE = 16;
  
  private static final Logger logger;
  
  SceneManager() {
    this.atomicInteger = new AtomicInteger();
  }
  
  static {
    logger = LoggerFunction.SCENE.getLogger();
    workerPool = new ArrayList<>();
    scenes = new HashMap<>();
    for (int i = 1; i <= 16; i++) {
      TaskWorker worker = new TaskWorker(i);
      workerPool.add(worker);
      (new NamedThreadFactory("scene_" + i)).newThread(worker).start();
    } 
  }
  
  public void initiate(List<SysMapInfo> maps) {
    maps.forEach(gameMap -> {
          Scene scene = new Scene();
          scene.setId(this.atomicInteger.getAndIncrement());
          scenes.put(gameMap.getMapSerial(), scene);
        });
  }
  
  private AOI getScene(int mapId) {
    return scenes.get(mapId);
  }
  
  public void enterMap(int mapId, Role role) {
    AOI scene = getScene(mapId);
    acceptTask(scene.getId(), () -> scene.enter(role));
  }
  
  public void leaveMap(int mapId, Role role) {
    AOI scene = getScene(mapId);
    acceptTask(scene.getId(), () -> scene.leave(role.getId()));
  }
  
  public void movePosition(Role role) {
    int mapId = role.getPos().getMapId();
    AOI scene = getScene(mapId);
    acceptTask(scene.getId(), () -> scene.move(role));
  }
  
  public void goToPosition(Role role) {
    int mapId = role.getPos().getMapId();
    AOI scene = getScene(mapId);
    acceptTask(scene.getId(), () -> scene.goTo(role));
  }
  
  public void sendMessages(Role role, Message... messages) {
    AOI scene = getScene(role.getPos().getMapId());
    acceptTask(scene.getId(), () -> scene.sendMessages(role, messages));
  }
  
  public void giveAppear(Role role, Set<Long> enters) {
    AOI scene = getScene(role.getPos().getMapId());
    acceptTask(scene.getId(), () -> scene.giveAppear(role, enters));
  }
  
  public void acceptTask(int sceneId, Runnable task) {
    if (task == null)
      return; 
    int size = workerPool.size();
    int index = sceneId % size;
    ((TaskWorker)workerPool.get(index)).addTask(task);
  }
  
  private static class TaskWorker implements Runnable {
    private int id;
    
    private BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();
    
    TaskWorker(int index) {
      this.id = index;
    }
    
    void addTask(Runnable task) {
      this.taskQueue.add(task);
    }
    
    public void run() {
      while (true) {
        try {
          while (true) {
            Runnable task = this.taskQueue.take();
            task.run();
          }
        } catch (Exception e) {
          SceneManager.logger.error("Scene task error" + this.id, e);
        } 
      } 
    }
  }
}
