package net.somta.juggle.console.interfaces.controller.flow;

import io.swagger.v3.oas.annotations.Operation;
import net.somta.core.protocol.ResponseDataResult;
import net.somta.juggle.console.application.service.flow.IFlowTagService;
import net.somta.juggle.console.interfaces.dto.flow.FlowTagDTO;
import net.somta.juggle.console.interfaces.dto.flow.FlowTagTreeDTO;
import net.somta.juggle.console.interfaces.param.flow.FlowTagQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static net.somta.juggle.common.constants.ApplicationConstants.JUGGLE_SERVER_VERSION;

@RestController
@RequestMapping(JUGGLE_SERVER_VERSION + "/flow/tag")
public class FlowTagController {

    @Autowired
    private IFlowTagService flowTagService;

    @Operation(summary = "查询树形结构")
    @PostMapping("/queryTree")
    public ResponseDataResult<List<FlowTagTreeDTO>> queryTree(@RequestBody FlowTagQueryParam param) {
        List<FlowTagTreeDTO> flowTagTreeDTOList = flowTagService.queryTree(param);
        return ResponseDataResult.setResponseResult(flowTagTreeDTOList);
    }

    @Operation(summary = "新增标签")
    @PostMapping("/add")
    public ResponseDataResult<Void> add(@RequestBody FlowTagDTO flowTagDTO) {
        flowTagService.add(flowTagDTO);
        return ResponseDataResult.setResponseResult(null);
    }

    @Operation(summary = "修改标签")
    @PostMapping("/update")
    public ResponseDataResult<Void> update(@RequestBody FlowTagDTO flowTagDTO) {
        flowTagService.update(flowTagDTO);
        return ResponseDataResult.setResponseResult(null);
    }

    @Operation(summary = "删除标签")
    @PostMapping("/delete")
    public ResponseDataResult<Void> delete(@RequestBody FlowTagDTO flowTagDTO) {
        flowTagService.delete(flowTagDTO.getId(), flowTagDTO.getAppCode());
        return ResponseDataResult.setResponseResult(null);
    }
}
