package net.somta.juggle.console.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import net.somta.core.protocol.ResponseDataResult;
import net.somta.juggle.console.application.service.IToolService;
import net.somta.juggle.console.interfaces.param.tool.FreemarkExecuteParam;
import net.somta.juggle.core.model.CamelResult;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

import static net.somta.juggle.common.constants.ApplicationConstants.JUGGLE_SERVER_VERSION;

@RestController
@RequestMapping(JUGGLE_SERVER_VERSION + "/tool")
public class ToolController {

    @Resource
    private IToolService toolService;

    @Operation(summary = "freemarkerExecute")
    @PostMapping("/freemarkerExecute")
    @ResponseBody
    public ResponseDataResult<String> add(@RequestBody FreemarkExecuteParam param) {
        try {
            CamelResult camelResult = toolService.freemarkExecute(param);
            if (camelResult.getSuccess()) {
                return ResponseDataResult.setResponseResult(camelResult.getData());
            } else {
                return ResponseDataResult.setResponseResult(camelResult.getMessage());
            }
        } catch (Exception e) {
            return ResponseDataResult.setErrorResponseResult(999, ExceptionUtils.getStackTrace(e));
        }
    }
}