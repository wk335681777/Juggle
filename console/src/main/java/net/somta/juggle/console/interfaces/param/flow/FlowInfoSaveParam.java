package net.somta.juggle.console.interfaces.param.flow;

import net.somta.core.base.page.PageParam;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FlowInfoSaveParam extends PageParam {

    private String appCode;
    private Long id;
    private List<KeyValuePair> headers;
    private String body;

    // 无参构造函数
    public FlowInfoSaveParam() {}

    // 带参构造函数，使用@JsonCreator和@JsonProperty来反序列化
    @JsonCreator
    public FlowInfoSaveParam(
            @JsonProperty("appCode") String appCode,
            @JsonProperty("id") Long id,
            @JsonProperty("headers") List<KeyValuePair> headers,
            @JsonProperty("body") String body) {
        this.appCode = appCode;
        this.id = id;
        this.headers = headers;
        this.body = body;
    }

    // Getters 和 Setters
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

    public List<KeyValuePair> getHeaders() {
        return headers;
    }

    public void setHeaders(List<KeyValuePair> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    // 内部类 KeyValuePair
    public static class KeyValuePair {
        private String key;
        private String value;

        // 无参构造函数
        public KeyValuePair() {}

        // 带参构造函数
        @JsonCreator
        public KeyValuePair(
                @JsonProperty("key") String key,
                @JsonProperty("value") String value) {
            this.key = key;
            this.value = value;
        }

        // Getters 和 Setters
        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        // 重写 toString() 方法，返回更有意义的信息
        @Override
        public String toString() {
            return "KeyValuePair{" +
                    "key='" + key + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

}
