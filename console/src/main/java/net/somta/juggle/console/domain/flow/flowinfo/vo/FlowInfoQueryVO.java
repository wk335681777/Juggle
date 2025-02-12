package net.somta.juggle.console.domain.flow.flowinfo.vo;

/**
 * @author husong
 */
public class FlowInfoQueryVO {
    private String flowName;

    private String flowType;

    private String appCode;;

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
}
