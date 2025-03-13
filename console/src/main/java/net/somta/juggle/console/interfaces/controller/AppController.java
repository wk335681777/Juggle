package net.somta.juggle.console.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import net.somta.core.protocol.ResponseDataResult;
import net.somta.core.protocol.ResponsePaginationDataResult;
import net.somta.juggle.console.application.service.IAppService;
import net.somta.juggle.console.domain.app.enums.AppErrorEnum;
import net.somta.juggle.console.domain.app.vo.AppVO;
import net.somta.juggle.console.interfaces.dto.ObjectDTO;
import net.somta.juggle.console.interfaces.param.app.AppAddParam;
import net.somta.juggle.console.interfaces.param.app.AppQueryParam;
import net.somta.juggle.console.interfaces.param.app.AppUpdateParam;
import org.springframework.web.bind.annotation.*;

//import jakarta.annotation.Resource;
import java.util.List;

import static net.somta.juggle.common.constants.ApplicationConstants.JUGGLE_SERVER_VERSION;

@RestController
@RequestMapping(JUGGLE_SERVER_VERSION + "/app")
public class AppController {

    @Resource
    private IAppService appService;

    @Operation(summary = "新增")
    @PostMapping("/add")
    public ResponseDataResult<Boolean> add(@RequestBody AppAddParam param){
        AppQueryParam appQueryParam = new AppQueryParam();
        appQueryParam.setAppCode(param.getAppCode());
        List<AppVO> appVOList = appService.queryPageList(appQueryParam);
        if(!appVOList.isEmpty()){
            return ResponseDataResult.setErrorResponseResult(AppErrorEnum.APP_KEY_EXIST);
        }
        appService.add(param);
        return ResponseDataResult.setResponseResult();
    }

    @Operation(summary = "根据ID删除")
    @DeleteMapping("/delete/{id}")
    public ResponseDataResult<Boolean> deleteObject(@PathVariable("id") Long id){
        appService.delete(id);
        return ResponseDataResult.setResponseResult();
    }

    @Operation(summary = "修改")
    @PutMapping("/update")
    public ResponseDataResult<Boolean> updateObject(@RequestBody AppUpdateParam appUpdateParam){
        appService.update(appUpdateParam);
        return ResponseDataResult.setResponseResult();
    }

    @Operation(summary = "查询详情")
    @GetMapping("/info/{id}")
    public ResponseDataResult<AppVO> getObject(@PathVariable("id") Long id){
        AppVO appVO = appService.get(id);
        return ResponseDataResult.setResponseResult(appVO);
    }

    @Operation(summary = "根据列表")
    @PostMapping("/list")
    public ResponseDataResult<List<ObjectDTO>> queryList(){
        AppQueryParam appQueryParam = new AppQueryParam();
        List<AppVO> objList = appService.queryPageList(appQueryParam);
        return ResponseDataResult.setResponseResult(objList);
    }

    @Operation(summary = "查询分页列表")
    @PostMapping("/page")
    public ResponsePaginationDataResult<List<AppVO>> queryPageList(@RequestBody AppQueryParam appQueryParam){
        return null;
    }
}
