package net.somta.juggle.console.domain.flow.flowinfo.vo;

import net.somta.juggle.console.domain.flow.FlowParametersInfoAO;

import java.util.Date;
import java.util.List;

public class FlowInfoParamesVO {
    private Long id;
    private Long flowId;
    private String appCode;
    private String headers;
    private String body;
    private Integer deleted;
    private Date createdAt;
    private Date updatedAt;
    private Long createdBy;
    private Long updatedBy;

    // Getters 和 Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public String toString() {
        return "FlowInfoParamesPO{" +
                "id=" + id +
                ", flowId=" + flowId +
                ", appCode='" + appCode + '\'' +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}

