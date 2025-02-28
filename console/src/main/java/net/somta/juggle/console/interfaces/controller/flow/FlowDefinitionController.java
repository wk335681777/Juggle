/*
Copyright (C) 2022-2024 husong

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 3
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, visit <https://www.gnu.org/licenses/gpl-3.0.html>.
*/
package net.somta.juggle.console.interfaces.controller.flow;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.somta.core.protocol.ResponseDataResult;
import net.somta.core.protocol.ResponsePaginationDataResult;
import net.somta.juggle.common.param.TriggerDataParam;
import net.somta.juggle.console.application.assembler.flow.IFlowDefinitionAssembler;
import net.somta.juggle.console.application.service.flow.IDeployMaster;
import net.somta.juggle.console.application.service.flow.IFlowDefinitionService;
import net.somta.juggle.console.domain.flow.definition.FlowDefinitionAO;
import net.somta.juggle.console.domain.flow.definition.enums.FlowDefinitionErrorEnum;
import net.somta.juggle.console.exception.BusinessException;
import net.somta.juggle.console.interfaces.dto.flow.FlowDefinitionExportDTO;
import net.somta.juggle.console.interfaces.dto.flow.FlowDefinitionInfoDTO;
import net.somta.juggle.console.interfaces.param.flow.definition.*;
import net.somta.juggle.core.model.FlowElement;
import net.somta.juggle.core.model.FlowResult;
import net.somta.juggle.core.validator.NodeValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static net.somta.juggle.common.constants.ApplicationConstants.JUGGLE_SERVER_VERSION;
import static net.somta.juggle.console.domain.flow.definition.enums.FlowDefinitionErrorEnum.*;

/**
 * 流程定义Controller
 * @author husong
 * @since 1.0.0
 **/
@Tag(name = "流程定义接口")
@RestController
@RequestMapping(JUGGLE_SERVER_VERSION + "/flow/definition")
public class FlowDefinitionController {
    private final static Logger logger = LoggerFactory.getLogger(FlowDefinitionController.class);

    private final IFlowDefinitionService flowDefinitionService;
    private final ObjectMapper objectMapper;
    @Resource
    private IDeployMaster iDeployMaster;
    @Resource
    private RestTemplate restTemplate;

    public FlowDefinitionController(IFlowDefinitionService flowDefinitionService,ObjectMapper objectMapper) {
        this.flowDefinitionService = flowDefinitionService;
        this.objectMapper = objectMapper;
    }

    /**
     * 创建流程
     * @param flowDefinitionAddParam 变量实体参数
     * @return Boolean
     */
    @Operation(summary = "创建流程定义")
    @PostMapping("/{appCode}/add")
    public ResponseDataResult<Boolean> addFlowDefinition(@PathVariable String appCode, @RequestBody FlowDefinitionAddParam flowDefinitionAddParam) {
        if (flowDefinitionAddParam == null) {
            return ResponseDataResult.setErrorResponseResult(FlowDefinitionErrorEnum.FLOW_PARAM_ERROR);
        }
        flowDefinitionAddParam.setAppCode(appCode);
        try {
            Boolean result = flowDefinitionService.addFlowDefinition(flowDefinitionAddParam);
            return ResponseDataResult.setResponseResult(result);  // 成功返回
        } catch (BusinessException ex) {
            return ResponseDataResult.setErrorResponseResult(ex.getCode(), ex.getMessage());
        }
    }

    /**
     * 删除变量
     * @param flowDefinitionId 变量实体参数
     * @return Boolean
     */
    @Operation(summary = "删除流程定义")
    @DeleteMapping("/delete/{flowDefinitionId}")
    public ResponseDataResult<Boolean> deleteFlowDefinition(@PathVariable Long flowDefinitionId){
        Boolean result = flowDefinitionService.deleteFlowDefinition(flowDefinitionId);
        return ResponseDataResult.setResponseResult(result);
    }

    /**
     * 修改流程
     * @param flowDefinitionUpdateParam 变量实体参数
     * @return Boolean
     */
    @Operation(summary = "修改流程定义")
    @PutMapping("/update")
    public ResponseDataResult<Boolean> updateFlowDefinition(@RequestBody FlowDefinitionUpdateParam flowDefinitionUpdateParam){
        if(flowDefinitionUpdateParam == null){
            return ResponseDataResult.setErrorResponseResult(FLOW_PARAM_ERROR);
        }
        Boolean result = flowDefinitionService.updateFlowDefinition(flowDefinitionUpdateParam);
        return ResponseDataResult.setResponseResult(result);
    }

    @Operation(summary = "保存流程内容")
    @PutMapping("/save")
    public ResponseDataResult<Boolean> saveFlowDefinitionContent(@RequestBody FlowDefinitionContentParam flowDefinitionContentParam){
        // logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        if(flowDefinitionContentParam == null){
            return ResponseDataResult.setErrorResponseResult(FLOW_PARAM_ERROR);
        }
        Boolean result = flowDefinitionService.saveFlowDefinitionContent(flowDefinitionContentParam);
        try {
//            String url = "http://localhost:30888/router/buildNode?id=" + flowDefinitionContentParam.getId() + "&env=DEV";
//            String response = HttpUtil.get(url);
            iDeployMaster.deployRouteDev(flowDefinitionContentParam.getId());
//            logger.info("buildNode result: {}", response);
            return ResponseDataResult.setResponseResult(result);
        } catch (Throwable e) {
            logger.error("buildNode error", e);
            return ResponseDataResult.setErrorResponseResult(FLOW_PARAM_ERROR.getErrorCode(), e.getMessage());
        }
    }

    @Operation(summary = "查询流程定义详情")
    @GetMapping("/info/{flowDefinitionId}")
    public ResponseDataResult<FlowDefinitionInfoDTO> getFlowDefinitionInfo(@PathVariable Long flowDefinitionId){
        FlowDefinitionAO flowDefinitionAo = flowDefinitionService.getFlowDefinitionInfo(flowDefinitionId);
        FlowDefinitionInfoDTO flowDefinitionInfoDto = IFlowDefinitionAssembler.IMPL.aoToDto(flowDefinitionAo);
        return ResponseDataResult.setResponseResult(flowDefinitionInfoDto);
    }

    @Operation(summary = "查询流程定义debug详情")
    @GetMapping("/debugInfo/{flowDefinitionId}")
    public ResponseDataResult<FlowDefinitionInfoDTO> getDebugInfo(@PathVariable Long flowDefinitionId){
        FlowDefinitionInfoDTO flowDefinitionInfoDTO = flowDefinitionService.getDebugInfo(flowDefinitionId);
        return ResponseDataResult.setResponseResult(flowDefinitionInfoDTO);
    }

    /**
     * 获取流程列表
     * @param flowDefinitionPageParam 变量实体参数
     * @return Boolean
     */
    @Operation(summary = "获取流程定义分页列表")
    @PostMapping("/{appCode}/page")
    public ResponsePaginationDataResult<FlowDefinitionInfoDTO> getFlowDefinitionPageList(@PathVariable String appCode, @RequestBody FlowDefinitionPageParam flowDefinitionPageParam){
        flowDefinitionPageParam.setAppCode(appCode);
        PageInfo pageInfo = flowDefinitionService.getFlowDefinitionPageList(flowDefinitionPageParam);
        return ResponsePaginationDataResult.setPaginationDataResult(pageInfo.getTotal(),pageInfo.getList());
    }

    /**
     * 调试流程
     * @param triggerData 触发流程实体
     * @return Boolean
     */
    @Operation(summary = "调试流程")
    @PostMapping("/debug/{flowKey}")
    public ResponseDataResult<FlowResult> debugFlow(@PathVariable String flowKey, @RequestBody TriggerDataParam triggerData){
//        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//        if(StringUtils.isEmpty(flowKey)){
//            return ResponseDataResult.setErrorResponseResult(FLOW_KEY_IS_EMPTY);
//        }
//        FlowDefinitionAO flowDefinitionAo = flowDefinitionService.getFlowDefinitionByKey(flowKey);
//        if(flowDefinitionAo == null){
//            return ResponseDataResult.setErrorResponseResult(FLOW_DEFINITION_NOT_EXIST);
//        }
//        if(StringUtils.isEmpty(flowDefinitionAo.getFlowContent())){
//            return ResponseDataResult.setErrorResponseResult(FlowDefinitionErrorEnum.FLOW_DEFINITION_CONTENT_IS_NULL_ERROR);
//        }
//        FlowResult rst = flowDefinitionService.debugFlow(flowDefinitionAo,triggerData);
//        return ResponseDataResult.setResponseResult(rst);
        String uri = (String) triggerData.getFlowData().get("uri");
        String requestBody = (String) triggerData.getFlowData().get("requestBody");
        List<Map<String, String>> headerList = (List<Map<String, String>>) triggerData.getFlowData().get("headers");
        String method = (String) triggerData.getFlowData().get("httpMethod");
        HttpHeaders headers = new HttpHeaders();
        if (CollectionUtils.isNotEmpty(headerList)) {
            for (int i = 0; i < headerList.size(); i++) {
                Map<String, String> header = headerList.get(i);
                if (StringUtils.isNotEmpty(header.get("key"))) {
                    headers.add(header.get("key"), header.get("value"));
                }
            }
        }
        HttpMethod httpMethod = HttpMethod.POST.name().equalsIgnoreCase(method) ? HttpMethod.POST : HttpMethod.GET;
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(uri, httpMethod, entity, String.class);
            return ResponseDataResult.setResponseResult(response.getBody());
        } catch (Exception e) {
            logger.error("debugFlow error", e);
            return ResponseDataResult.setResponseResult(ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 部署上线流程
     * @param flowDefinitionDeployParam 变量实体参数
     * @return Boolean
     */
    @Operation(summary = "部署流程")
    @PostMapping("/deploy")
    public ResponseDataResult<Boolean> deployFlowDefinition(@RequestBody FlowDefinitionDeployParam flowDefinitionDeployParam){
        FlowDefinitionAO flowDefinitionAo = flowDefinitionService.getFlowDefinitionInfo(flowDefinitionDeployParam.getFlowDefinitionId());
        if(flowDefinitionAo == null){
            return ResponseDataResult.setErrorResponseResult(FLOW_DEFINITION_NOT_EXIST);
        }
        try {
            List<FlowElement> nodeList = objectMapper.readValue(flowDefinitionAo.getFlowContent(), new TypeReference<List<FlowElement>>() {});
            NodeValidator nodeValidator = new NodeValidator();
            for (FlowElement flowElement :  nodeList) {
                nodeValidator.validateNode(flowElement);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResponseDataResult.setErrorResponseResult(FLOW_DEFINITION_CONTENT_VALIDATOR_ERROR);
        }

        Boolean result = flowDefinitionService.deployFlowDefinition(flowDefinitionDeployParam,flowDefinitionAo);
        return ResponseDataResult.setResponseResult(result);
    }

    /**
     * 复制流程
     * @param flowDefinitionCopyParam 变量实体参数
     * @return Boolean
     */
    @Operation(summary = "复制流程定义")
    @PutMapping("/copy")
    public ResponseDataResult<Boolean> copyFlowDefinition(@RequestBody FlowDefinitionCopyParam flowDefinitionCopyParam){
        if(flowDefinitionCopyParam == null){
            return ResponseDataResult.setErrorResponseResult(FLOW_PARAM_ERROR);
        }
        Boolean result = flowDefinitionService.copyFlowDefinition(flowDefinitionCopyParam);
        return ResponseDataResult.setResponseResult(result);
    }

    /**
     * 导出流程
     * @param param 变量实体参数
     * @return Boolean
     */
    @Operation(summary = "导出流程定义")
    @PostMapping("/export")
    public ResponseEntity<?> exportFlowDefinition(@RequestBody FlowDefinitionExportParam param, HttpServletResponse response) throws Exception {
        if(CollectionUtils.isEmpty(param.getIdList())) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(FLOW_EXPORT_ID_IS_NULL_ERROR.getErrorMsg());
        }

        String fileName = "flowDefine_" + System.currentTimeMillis() + ".json";
        // 设置响应头
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        List<FlowDefinitionExportDTO> dtoList = flowDefinitionService.export(param);
        JSONArray jsonArray = new JSONArray(dtoList);

        // 获取文件输入流
        try (OutputStream os = response.getOutputStream()) {
            os.write(jsonArray.toStringPretty().getBytes());
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{appCode}/import")
    public ResponseDataResult<Void> uploadFile(@PathVariable String appCode, @RequestParam("file") MultipartFile file) {
        try {
            // 获取文件名
            String fileName = file.getOriginalFilename();
            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            System.out.println(content);
            List<FlowDefinitionExportDTO> flowDefinitionExportDTOS = JSONUtil.toList(content, FlowDefinitionExportDTO.class);
            flowDefinitionService.importFlowDefinition(appCode, flowDefinitionExportDTOS);
            return ResponseDataResult.setResponseResult();
        } catch (Exception e) {
            logger.error("uploadFile error", e);
            return ResponseDataResult.setErrorResponseResult(999, e.getMessage());
        }
    }
}
