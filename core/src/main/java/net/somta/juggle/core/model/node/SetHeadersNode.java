package net.somta.juggle.core.model.node;


import java.util.List;
import java.util.Map;

public class SetHeadersNode extends FlowNode {
        List<Map<String, String>> headers;

        public List<Map<String, String>> getHeaders() {
                return headers;
        }

        public void setHeaders(List<Map<String, String>> headers) {
                this.headers = headers;
        }
}
