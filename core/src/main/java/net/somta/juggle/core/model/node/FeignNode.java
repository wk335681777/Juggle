package net.somta.juggle.core.model.node;

import java.util.List;
import java.util.Map;

public class FeignNode  extends FlowNode{
    private String serviceName;//服务名
    private String path;//路径
    private List<Map<String, String>> parameters;//入参
    private String method;//请求方式
    private String contextType;//报文格式
    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Map<String, String>> getParameters() {
        return parameters;
    }

    public void setParameters(List<Map<String, String>> parameters) {
        this.parameters = parameters;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getContextType() {
        return contextType;
    }

    public void setContextType(String contextType) {
        this.contextType = contextType;
    }
}
