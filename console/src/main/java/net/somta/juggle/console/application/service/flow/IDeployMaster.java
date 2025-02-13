package net.somta.juggle.console.application.service.flow;

import net.somta.juggle.core.model.ServerInfo;

import java.util.List;

public interface IDeployMaster {

    void deployRouteDev(Long flowId);

    void deployRouteProd(Long flowVersionId);

    void stopRouteDev(Long flowId);

    void stopRouteProd(Long flowVersionId);

    /**
     * 接收心跳
     */
    void receiveHeartbeat();

    /**
     * 获取所有server列表
     * @return
     */
    List<ServerInfo> getServers();
}
