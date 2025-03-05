package net.somta.juggle.console.domain.flow.definition.vo;

/**
 * @author husong
 */
public class FlowDefinitionInfoQueryVO {

    /**
     * 流程名称
     */
    private String flowName;

    private String flowKey;

    /**
     * 流程类型  sync：同步  async：异步
     */
    private String flowType;

    private String appCode;

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getFlowType() {
        return flowType;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getFlowKey() {
        return flowKey;
    }

    public void setFlowKey(String flowKey) {
        this.flowKey = flowKey;
    }
}
