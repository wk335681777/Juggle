package net.somta.juggle.core.model.node;

public class ScriptNode extends FlowNode{
    private String language;
    private String expression;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
