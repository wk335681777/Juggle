package net.somta.juggle.core.model.node;

public class RedirectNode extends FlowNode {
    private String flowKey;
    private String flowName;


    public String getFlowKey() {
        return flowKey;
    }

    public void setFlowKey(String flowKey) {
        this.flowKey = flowKey;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }
}
