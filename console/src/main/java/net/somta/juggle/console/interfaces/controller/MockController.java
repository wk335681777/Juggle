package net.somta.juggle.console.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import net.somta.core.protocol.ResponseDataResult;
import net.somta.core.protocol.ResponsePaginationDataResult;
import net.somta.juggle.console.application.service.IAppService;
import net.somta.juggle.console.application.service.IMockService;
import net.somta.juggle.console.domain.app.enums.AppErrorEnum;
import net.somta.juggle.console.domain.app.vo.AppVO;
import net.somta.juggle.console.interfaces.dto.MockDTO;
import net.somta.juggle.console.interfaces.dto.ObjectDTO;
import net.somta.juggle.console.interfaces.param.app.AppAddParam;
import net.somta.juggle.console.interfaces.param.app.AppQueryParam;
import net.somta.juggle.console.interfaces.param.app.AppUpdateParam;
import net.somta.juggle.console.interfaces.param.mock.MockQueryParam;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

import static net.somta.juggle.common.constants.ApplicationConstants.JUGGLE_SERVER_VERSION;

@RestController
@RequestMapping(JUGGLE_SERVER_VERSION + "/mock")
public class MockController {

    @Resource
    private IAppService appService;
    @Resource
    private IMockService mockService;

    @Operation(summary = "新增")
    @PostMapping("/add")
    public ResponseDataResult<Boolean> add(@RequestBody MockDTO param){
        mockService.add(param);
        return ResponseDataResult.setResponseResult();
    }

    @Operation(summary = "根据ID删除")
    @DeleteMapping("/delete/{id}")
    public ResponseDataResult<Boolean> deleteObject(@PathVariable("id") Long id){
        mockService.delete(id);
        return ResponseDataResult.setResponseResult();
    }

    @Operation(summary = "修改")
    @PostMapping("/update")
    public ResponseDataResult<Boolean> updateObject(@RequestBody MockDTO param){
        mockService.update(param);
        return ResponseDataResult.setResponseResult();
    }

    @Operation(summary = "查询详情")
    @GetMapping("/info/{id}")
    public ResponseDataResult<MockDTO> getObject(@PathVariable("id") Long id){
        MockDTO mockDTO = mockService.get(id);
        return ResponseDataResult.setResponseResult(mockDTO);
    }

    @Operation(summary = "查询分页列表")
    @PostMapping("/page")
    public ResponsePaginationDataResult<MockDTO> queryPageList(@RequestBody MockQueryParam param){
        return mockService.queryPageList(param);
    }
}
