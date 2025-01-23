package net.somta.juggle.core.model.node;

public class RecipientListNode extends FlowNode {
    private String expression;
    private Boolean parallelProcessing;

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Boolean getParallelProcessing() {
        return parallelProcessing;
    }

    public void setParallelProcessing(Boolean parallelProcessing) {
        this.parallelProcessing = parallelProcessing;
    }
}
