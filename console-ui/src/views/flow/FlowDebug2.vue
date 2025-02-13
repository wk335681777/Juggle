<script setup lang="ts">
import { reactive, ref, watch } from 'vue';
import { useRoute, onBeforeRouteLeave  } from 'vue-router';
import { flowDefineService, flowVersionService } from '@/service';
import { ElMessage } from 'element-plus';
import CodeEditor from '@/components/common/CodeEditor.vue';
import {DataType, FlowDefineInfo} from '@/typings';
import FilterValue from '@/components/filter/FilterValue.vue';
import {InfoFilled} from "@element-plus/icons-vue";
import DataTypeDisplay from "@/components/common/DataTypeDisplay.vue";

const route = useRoute();
let paramsData = reactive({
  params: route.params,
  query: route.query,
});

const codeEditRef = ref<InstanceType<typeof CodeEditor>>();

const debugUrl = ref('');
let flowResponseJson = ref('');
const flowDefine = ref<FlowDefineInfo>();
let requestBody = ref('');

const responseHeaderData = ref([]);

queryFlowDefineInfo();

function generateUUID() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
    var r = Math.random() * 16 | 0,
        v = c === 'x' ? r : (r & 0x3 | 0x8);
    return v.toString(16);
  });
}
const debugId = generateUUID();

async function queryFlowDefineInfo() {
  const res = await flowDefineService.getDebugInfo(paramsData.params.flowDefinitionId as number);
  if (res.success) {
    debugUrl.value = res.result.debugUri + "?debugConnId=" + debugId;
    flowDefine.value = res.result;
  } else {
    ElMessage({ type: 'error', message: res.errorMsg });
  }
}

let timerId;
async function sendFlowDebug() {
  if (!validate()) {
    return;
  }
  const params = {
    flowData: getParams(),
  };
  const res = await flowDefineService.debugFlow(paramsData.params.flowKey as string, params);
  if (res.success) {
    // if (flowDefine.value?.flowType === 'sync') {
    //   flowResponseJson.value = res.result;
    // } else {
    //   timerId = setInterval(getAsyncFlowResult, 1000, res.result.flowInstanceId);
    // }
    flowResponseJson.value = res.result;
  } else {
    ElMessage({ type: 'error', message: res.errorMsg });
  }
  responseHeaderData.value = Object.entries(res.response?.headers).map(([key, value]) => {
    return {
      headerKey: key,
      headerValue: value,
    };
  });
}

async function getAsyncFlowResult(flowInstanceId: string) {
  const res = await flowVersionService.getAsyncFlowResult(flowInstanceId);
  if (res.success) {
    if (res.result) {
      flowResponseJson.value = JSON.stringify(res.result);
      clearInterval(timerId);
    }
  } else {
    ElMessage({ type: 'error', message: res.errorMsg });
  }
}

function isEmpty(val: any) {
  return val === undefined || val === null || val === '';
}

function validate() {
  const flowInputParams = flowDefine.value?.flowInputParams || [];
  const errors: string[] = [];
  flowInputParams.forEach((param: any) => {
    if (param.required && isEmpty(param.value)) {
      param.error = '必填字段不能为空';
      errors.push(param.paramKey);
    } else {
      param.error = '';
    }
  });
  return errors.length === 0;
}

function getParams() {
  const flowInputParams = flowDefine.value?.flowInputParams || [];
  const params: any = {};
  flowInputParams.forEach((param: any) => {
    if (!isEmpty(param.value)) {
      const dataType: DataType = param.dataType;
      if (dataType.type === 'Object' || dataType.type === 'List') {
        params[param.paramKey] = JSON.parse(param.value);
      } else {
        params[param.paramKey] = param.value;
      }
      console.log(param);
    }
  });
  params['requestBody'] = requestBody.value;
  params['uri'] = debugUrl.value;
  return params;
}

function resetParams() {
  flowDefine.value?.flowInputParams.forEach((param: any) => {
    param.value = '';
    param.error = '';
  });
}


const eventSource = new EventSource("/camelLogStream/logStream?debugConnId=" + debugId);

eventSource.onmessage = function(event) {
  var logContainer = document.getElementById("logContainer");
  if (event.data == '' || event.data == '\n') {
    return;
  }

  var newLog = document.createElement("div");
  newLog.textContent = event.data.replace(/<br>/g, '\n');
  console.log(event.data);
  logContainer.appendChild(newLog);
  logContainer.scrollTop = logContainer.scrollHeight;  // 滚动到底部
};

eventSource.onerror = function(error) {
  console.log("Error:", error);
};

onBeforeRouteLeave((to, from, next) => {
  console.log('准备离开当前页面，执行清理操作');
  eventSource.close();
  next();
});

watch(flowResponseJson, (newJson) => {
  try {
    const jsonObject = JSON.parse(newJson); // 将原始字符串转为对象
    flowResponseJson.value = JSON.stringify(jsonObject, null, 2); // 格式化为 JSON 字符串
  } catch (error) {
    flowResponseJson.value = newJson; // 如果解析失败，直接显示原始字符串
  }
});

</script>

<template>

  <div class="flow-debug">
    <div class="flow-header">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>{{ flowDefine.flowName }} - {{ flowDefine.flowKey }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <el-row :gutter="16">
      <el-col :span="20">
        <el-input v-model="debugUrl" />
      </el-col>
      <el-col :span="4">
        <el-button type="primary" @click="sendFlowDebug">发送</el-button>
        <el-button @click="resetParams">重置</el-button>
      </el-col>
    </el-row>
<!--    <el-tabs model-value="inputParam">-->
<!--      <el-tab-pane label="请求参数" name="inputParam">-->
<!--        <div class="input-param-head">-->
<!--          <div class="input-param-tr">-->
<!--            <div class="input-param-td"></div>-->
<!--            <div class="input-param-td">参数编码</div>-->
<!--            <div class="input-param-td">参数名称</div>-->
<!--            <div class="input-param-td">参数类型</div>-->
<!--            <div class="input-param-td td-value">参数值</div>-->
<!--          </div>-->
<!--        </div>-->
<!--        <div class="input-param-body">-->
<!--          <div class="input-param-tr" v-for="param in flowDefine?.flowInputParams" :key="param.paramKey">-->
<!--            <div class="input-param-td">-->
<!--              <template v-if="param.required">*</template>-->
<!--            </div>-->
<!--            <div class="input-param-td" >{{ param.paramKey }}</div>-->
<!--            <div class="input-param-td" :title="param.paramName">-->
<!--              {{ param.paramName }}-->
<!--              <el-tooltip v-if="param.paramDesc" effect="dark" placement="top" :content="param.paramDesc">-->
<!--                <el-icon><InfoFilled /></el-icon>-->
<!--              </el-tooltip>-->
<!--            </div>-->
<!--            <div class="input-param-td">-->
<!--              <DataTypeDisplay :dataType="param.dataType"/>-->
<!--            </div>-->
<!--            <div class="input-param-td td-value">-->
<!--              <FilterValue v-model="param.value" :dataType="param.dataType" />-->
<!--            </div>-->
<!--            <div class="input-param-td td-error">{{ param.error || '' }}</div>-->
<!--          </div>-->
<!--        </div>-->
<!--      </el-tab-pane>-->
<!--    </el-tabs>-->
    <el-form-item required>
      <el-input v-model="requestBody" placeholder="请输入" :rows="13" type="textarea"></el-input>
    </el-form-item>

    <el-tabs model-value="result">
      <el-tab-pane label="响应内容" name="result">
        <el-text line-clamp="2">
          <CodeEditor ref="codeEditRef" v-model="flowResponseJson" width="1000px" height="200px" language="json" />
        </el-text>
      </el-tab-pane>
      <el-tab-pane label="响应头" name="responseHeader">
        <el-table :data="responseHeaderData" style="width: 100%">
          <el-table-column prop="headerKey" label="响应头" width="350" />
          <el-table-column prop="headerValue" label="值" />
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="日志" name="log">
        <div id="logContainer"></div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<style lang="less" scoped>
.flow-debug {
  padding: 24px 40px;

  .input-param-body {
    color: #666;
  }
  .input-param-tr {
    display: flex;
    font-size: 14px;
    line-height: 28px;
    margin-bottom: 8px;
  }
  .input-param-td {
    margin-right: 12px;
    width: 120px;
    text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;
    &:first-child {
      margin: 0;
      width: 12px;
      color: red;
    }
    &.td-value {
      width: 240px;
    }
    &.td-error {
      color: red;
    }
  }
}
#logContainer {
  white-space: pre-wrap; /* 保留换行符并自动换行 */
}
</style>
