package net.somta.juggle.console.interfaces.param.flow.definition;

import lombok.Getter;
import lombok.Setter;
import net.somta.juggle.console.domain.parameter.vo.InputParameterVO;
import net.somta.juggle.console.domain.parameter.vo.OutputParameterVO;

import java.util.List;

/**
 * @author Gavin
 */
public class FlowDefinitionCopyParam {
    private Long id;

    private String flowName;

    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
