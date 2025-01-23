package net.somta.juggle.core.model.node;

public class TransformNode extends FlowNode {
    private String content;
    private String inputFormat;

    public String getInputFormat() {
        return inputFormat;
    }

    public void setInputFormat(String inputFormat) {
        this.inputFormat = inputFormat;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
