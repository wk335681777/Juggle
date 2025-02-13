package net.somta.juggle.console.application.service.flow.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import net.somta.juggle.console.application.service.flow.IDeployMaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class DeployMasterImpl implements IDeployMaster {
    private static final Logger log = LoggerFactory.getLogger(DeployMasterImpl.class);

    private static final String DEPLOY_ROUTE_DEV = "/router/buildNode";
    private static final String DEPLOY_ROUTE_POD = "/router/deployFlowForPod";
    private static final String STOP_ROUTE_POD = "/router/stopFlowForPod";

    private static final String REDIS_WORKER_KEY = "lowcode-integration-camel-worker";
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    private final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(2);
    private final Map<String, ServerInfo> workerServers = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        receiveHeartbeat();
        scheduledExecutor.scheduleAtFixedRate(this::receiveHeartbeat, 1, 1, TimeUnit.SECONDS);
    }

    @Override
    public void deployRouteDev(Long flowId) {
        callWorker(DEPLOY_ROUTE_DEV + "?id=" + flowId);
    }

    @Override
    public void deployRouteProd(Long flowVersionId) {
        callWorker(DEPLOY_ROUTE_POD + "?flowVersionId=" + flowVersionId);
    }

    private void callWorker(String url) {
        Map<String, ServerInfo> servers = new ConcurrentHashMap<>(workerServers);
        if (CollectionUtils.isEmpty(servers)) {
            throw new RuntimeException("no deploy server worker available");
        }

        for (ServerInfo serverInfo : servers.values()) {
            String fullUrl = serverInfo.getProtocol() + "://" + serverInfo.getIp() + ":" + serverInfo.getPort() + url;
            //try {
            log.info("start deploy worker for {}", fullUrl);
            String result = HttpUtil.get(fullUrl);
            log.info("deploy server:{}, path: {}, deploy result:{}", serverInfo.getIp() + ":" + serverInfo.getPort(), url, result);
            processResult(result);
//            } catch (Exception e) {
//                log.error("deploy server:{}, path: {}, error: {}", serverInfo.getIp() + ":" + serverInfo.getPort(), url, e.getMessage(), e);
//            }
        }
    }

    private void processResult(String result) {
        JSONObject jsonObject = new JSONObject(result);
        if (!jsonObject.getBool("success")) {
            throw new RuntimeException(jsonObject.getStr("message"));
        }
    }

    @Override
    public void stopRouteDev(Long flowId) {
    }

    @Override
    public void stopRouteProd(Long flowVersionId) {
        callWorker(STOP_ROUTE_POD + "?flowVersionId=" + flowVersionId);

    }

    @Override
    public synchronized void receiveHeartbeat() {
        Set<Object> keys = redisTemplate.opsForHash().keys(REDIS_WORKER_KEY);
        if (CollectionUtils.isEmpty(keys)) {
            return;
        }

        for (Object key : keys) {
            String value = (String) redisTemplate.opsForHash().get(REDIS_WORKER_KEY, key);
            assert value != null;
            long lastHeartBeatTime = Long.parseLong(value);
            if (System.currentTimeMillis() - lastHeartBeatTime <= 60 * 1000) {
                String[] ipPort = key.toString().split(":");
                ServerInfo serverInfo = new ServerInfo(ipPort[0], ipPort[1], ipPort[2], lastHeartBeatTime);
                workerServers.put(key.toString(), serverInfo);
            } else {
                redisTemplate.opsForHash().delete(REDIS_WORKER_KEY, key);
                log.info("worker lastBeatHeart time more than 1 mini");
            }
        }
    }

    public static class ServerInfo {
        private String ip;
        private String port;
        private long lastHeartBeatTime;
        private String protocol;
        public ServerInfo(String protocol, String ip, String port, long lastHeartBeatTime) {
            this.protocol = protocol;
            this.ip = ip;
            this.port = port;
            this.lastHeartBeatTime = lastHeartBeatTime;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public long getLastHeartBeatTime() {
            return lastHeartBeatTime;
        }

        public void setLastHeartBeatTime(long lastHeartBeatTime) {
            this.lastHeartBeatTime = lastHeartBeatTime;
        }

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }
    }
}
