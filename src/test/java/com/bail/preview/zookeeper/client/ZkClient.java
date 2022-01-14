package com.bail.preview.zookeeper.client;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/1/14 16:09
 */
public class ZkClient {

    private String connectionStr = "127.0.0.1:2181";
    private int sessionTimeout = 3000;
    ZooKeeper zkCli = null;
    @BeforeEach
    public void init() throws IOException {
        zkCli = new ZooKeeper(connectionStr, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("watchedEvent.getPath():"+watchedEvent.getPath() + "\t" + "watchedEvent.getState():"+watchedEvent.getState() + "\t"
                        + "watchedEvent.getType():"+watchedEvent.getType());
                try {
                    List<String> children = zkCli.getChildren("/", true);
                    for (String child : children) {
                        System.out.println(child);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public void init2() throws IOException {
        zkCli = new ZooKeeper(connectionStr, sessionTimeout, null);

    }
    // 创建子节点
    @Test
    public void createZnode() throws Exception {
        String path = zkCli.create("/servers", "root".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(path);
    }

    // 获取子节点
    @Test
    public void getChild() throws KeeperException, InterruptedException {
        List<String> children = zkCli.getChildren("/test/hello", true);
        for (String c : children) {
            System.out.println(c);
        }
        Thread.sleep(Long.MAX_VALUE);
    }

    // 删除节点
    @Test
    public void rmChildData() throws KeeperException, InterruptedException {
        // byte[] data = zkCli.getData("/bbq", true, null);
        // System.out.println(new String(data));
        zkCli.delete("/hello/java", -1);
    }

    // 修改数据
    @Test
    public void setData() throws KeeperException, InterruptedException {
        zkCli.setData("/test/hello/java", "18".getBytes(), -1);
    }

    // 判断节点是否存在
    @Test
    public void testExist() throws KeeperException, InterruptedException {
        Stat exists = zkCli.exists("/hello", false);
        System.out.println(exists == null ? "not exists" : "exists");
    }
    // 定义父节点
    private String parentNode = "/servers";
    // 2.注册信息
    @Test
    public void regist(String hostname) throws KeeperException, InterruptedException {
        hostname = "";
        String node = zkCli.create(parentNode + "/server", hostname.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(node);
    }
}
