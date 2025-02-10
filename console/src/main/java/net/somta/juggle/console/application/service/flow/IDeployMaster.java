package net.somta.juggle.console.application.service.flow;

public interface IDeployMaster {

    void deployRouteDev(Long flowId);

    void deployRouteProd(Long flowVersionId);

    void stopRouteDev(Long flowId);

    void stopRouteProd(Long flowVersionId);

    /**
     * 接收心跳
     */
    void receiveHeartbeat();
}
