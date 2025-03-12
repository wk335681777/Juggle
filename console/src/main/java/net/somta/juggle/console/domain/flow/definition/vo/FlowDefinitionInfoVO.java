package net.somta.juggle.console.domain.flow.definition.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author husong
 */
public class FlowDefinitionInfoVO {

    private Long id;

    private String appCode;

    /**
     * 流程Key,全局唯一
     */
    private String flowKey;

    /**
     * 流程名称
     */
    private String flowName;

    /**
     * 流程类型  sync：同步  async：异步
     */
    private String flowType;

    /**
     * 流程描述
     */
    private String remark;

    private Date createdAt;

    private Date updatedAt;

    private String flowContent;

    private List<Long> flowTagIdList;
    private List<String> flowTagNameList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

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

    public String getFlowType() {
        return flowType;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getFlowContent() {
        return flowContent;
    }

    public void setFlowContent(String flowContent) {
        this.flowContent = flowContent;
    }

    public List<Long> getFlowTagIdList() {
        return flowTagIdList;
    }

    public void setFlowTagIdList(List<Long> flowTagIdList) {
        this.flowTagIdList = flowTagIdList;
    }

    public List<String> getFlowTagNameList() {
        return flowTagNameList;
    }

    public void setFlowTagNameList(List<String> flowTagNameList) {
        this.flowTagNameList = flowTagNameList;
    }
}
