package net.somta.juggle.console.interfaces.param.tool;

import lombok.Data;
import net.somta.juggle.console.interfaces.dto.ParamDTO;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class FreemarkExecuteParam {
    @NotEmpty
    private String template;
    @NotEmpty
    private String bodyParam;
    private List<ParamDTO> headerParamList;

    public @NotEmpty String getTemplate() {
        return template;
    }

    public void setTemplate(@NotEmpty String template) {
        this.template = template;
    }

    public @NotEmpty String getBodyParam() {
        return bodyParam;
    }

    public void setBodyParam(@NotEmpty String bodyParam) {
        this.bodyParam = bodyParam;
    }

    public List<ParamDTO> getHeaderParamList() {
        return headerParamList;
    }

    public void setHeaderParamList(List<ParamDTO> headerParamList) {
        this.headerParamList = headerParamList;
    }
}
