package net.somta.juggle.core.model.node;


public class SetHeaderNode extends FlowNode {
    private String headerName;
    private String headerValue;

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getHeaderValue() {
        return headerValue;
    }

    public void setHeaderValue(String headerValue) {
        this.headerValue = headerValue;
    }
}
