package net.somta.juggle.console.interfaces.param.flow;

import net.somta.core.base.page.PageParam;

/**
 * @author husong
 */
public class FlowVersionPageParam extends PageParam {

    private Long flowId;

    private String flowName;

    private String appCode;

    private String flowKey;

    /**
     * 流程状态   0:禁用  1:启用
     */
    private Integer flowVersionStatus;

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public Integer getFlowVersionStatus() {
        return flowVersionStatus;
    }

    public void setFlowVersionStatus(Integer flowVersionStatus) {
        this.flowVersionStatus = flowVersionStatus;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
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
