package net.somta.juggle.console.interfaces.param.flow.definition;

/**
 * @author Gavin
 */
public class FlowDefinitionDraftParam {

    private String appCode;

    private Long id;

    private String flowKey;

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlowKey() {
        return flowKey;
    }

    public void setFlowKey(String flowKey) {
        this.flowKey = flowKey;
    }
}
