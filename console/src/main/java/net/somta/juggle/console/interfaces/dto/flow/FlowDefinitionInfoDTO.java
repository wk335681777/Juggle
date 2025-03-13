package net.somta.juggle.console.interfaces.dto.flow;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.somta.juggle.console.domain.parameter.vo.InputParameterVO;
import net.somta.juggle.console.domain.parameter.vo.OutputParameterVO;
import net.somta.juggle.console.domain.flow.definition.vo.VariableInfoVO;

import java.util.Date;
import java.util.List;

/**
 * @author husong
 */
public class FlowDefinitionInfoDTO {

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

    private String flowContent;

    private boolean enableDebug;

    private String debugUri;

    /**
     * 流程描述
     */
    private String remark;

    private List<Long> flowTagIdList;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    private List<InputParameterVO> flowInputParams;

    private List<OutputParameterVO> flowOutputParams;

    private List<VariableInfoVO> flowVariables;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFlowContent() {
        return flowContent;
    }

    public void setFlowContent(String flowContent) {
        this.flowContent = flowContent;
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

    public List<InputParameterVO> getFlowInputParams() {
        return flowInputParams;
    }

    public void setFlowInputParams(List<InputParameterVO> flowInputParams) {
        this.flowInputParams = flowInputParams;
    }

    public List<OutputParameterVO> getFlowOutputParams() {
        return flowOutputParams;
    }

    public void setFlowOutputParams(List<OutputParameterVO> flowOutputParams) {
        this.flowOutputParams = flowOutputParams;
    }

    public List<VariableInfoVO> getFlowVariables() {
        return flowVariables;
    }

    public void setFlowVariables(List<VariableInfoVO> flowVariables) {
        this.flowVariables = flowVariables;
    }

    public String getDebugUri() {
        return debugUri;
    }

    public void setDebugUri(String debugUri) {
        this.debugUri = debugUri;
    }

    public boolean isEnableDebug() {
        return enableDebug;
    }

    public void setEnableDebug(boolean enableDebug) {
        this.enableDebug = enableDebug;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public List<Long> getFlowTagIdList() {
        return flowTagIdList;
    }

    public void setFlowTagIdList(List<Long> flowTagIdList) {
        this.flowTagIdList = flowTagIdList;
    }

    @Override
    public String toString() {
        return "FlowDefinitionInfoDTO{" +
                "id=" + id +
                ", flowKey='" + flowKey + '\'' +
                ", flowName='" + flowName + '\'' +
                ", flowType='" + flowType + '\'' +
                ", flowContent='" + flowContent + '\'' +
                ", remark='" + remark + '\'' +
                ", flowInputParams=" + flowInputParams +
                ", flowOutputParams=" + flowOutputParams +
                '}';
    }
}
