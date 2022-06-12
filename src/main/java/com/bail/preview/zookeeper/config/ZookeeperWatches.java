package com.bail.preview.zookeeper.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.zookeeper.data.Stat;

/**
 * 注册监听器
 * @author kai O'Brian
 * @create 2022/6/11 11:32
 */
@Slf4j
public class ZookeeperWatches {

    private CuratorFramework client;

    public ZookeeperWatches(CuratorFramework client){
        this.client = client;
    }

    public void znodeWatcher() throws Exception {
        NodeCache nodeCache = new NodeCache(client, "/node");
        nodeCache.start();
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                log.info("==============节点改变==============");
                String path = nodeCache.getPath();
                String currentDataPath = nodeCache.getCurrentData().getPath();
                String currentData = new String(nodeCache.getCurrentData().getData());
                Stat stat = nodeCache.getCurrentData().getStat();
                log.info("path:{}",path);
                log.info("currentDataPath:{}",currentDataPath);
                log.info("currentData:{}",currentData);
            }
        });
        log.info("节点监听注册完成");
    }

    public void addChildrenWatcher() throws Exception {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, "/node", true);
        pathChildrenCache.start();
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                log.info("============节点子节点改变==============");
                PathChildrenCacheEvent.Type type = event.getType();
                String childrenData = new String(event.getData().getData());
                String childrenPath = event.getData().getPath();
                Stat childrenStat = event.getData().getStat();
                log.info("子节点监听类型：{}",type);
                log.info("子节点路径：{}",childrenPath);
                log.info("子节点数据：{}",childrenData);
                log.info("子节点元数据：{}",childrenStat);
            }
        });
        log.info("子节点监听注册完成");
    }
}
