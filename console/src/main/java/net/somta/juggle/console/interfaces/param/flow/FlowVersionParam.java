package net.somta.juggle.console.interfaces.param.flow;

public class FlowVersionParam {


    private String appCode;
    private String flowKey;
    private Long flowVersionId;

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public Long getFlowVersionId() {
        return flowVersionId;
    }

    public void setFlowVersionId(Long flowVersionId) {
        this.flowVersionId = flowVersionId;
    }

    public String getFlowKey() {
        return flowKey;
    }

    public void setFlowKey(String flowKey) {
        this.flowKey = flowKey;
    }
}
