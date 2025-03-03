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
const flowDefine = ref<FlowDefineInfo>({
  flowName: '',
  flowKey: ''
});
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

  isLoading.value = true;
  const params = {
    flowData: getParams(),
  };
  const res = await flowDefineService.debugFlow(paramsData.params.flowKey as string, params);
  isLoading.value = false;
  if (res.success) {
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
  params['headers'] = headers.value;
  params['httpMethod'] = httpMethod.value;
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
  if (event.data == '' || event.data == '\n') {
    return;
  }

  const line = event.data.replace(/<br>/g, '\n');
  addMessage(line);
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


// 用来保存 HTTP headers 的数组
const headers = ref([
  { key: '_mock_', value: 'true' }, // 默认一行
]);

// 添加新的 HTTP header 行
const addHeader = () => {
  headers.value.push({ key: '', value: '' });
};

// 删除某一行
const removeHeader = (index) => {
  headers.value.splice(index, 1);
};

// 提交 HTTP headers，发送请求或其他操作
const submitHeaders = () => {
  // 模拟提交的操作
  console.log('Submitting headers:', headers.value);
};

const httpMethod = ref('POST');
// 状态管理：控制是否显示遮盖层
const isLoading = ref(false);

const messages = ref([]);
const addMessage = (line) => {
    messages.value.push(line);
};

</script>

<template>

  <div class="flow-debug">
    <div class="flow-header">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>{{ flowDefine.flowName }} - {{ flowDefine.flowKey }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <el-row :gutter="16">
      <el-col :span="2">
        <el-select v-model="httpMethod" placeholder="请选择加密方式">
          <el-option key="GET" label="GET" value="GET" />
          <el-option key="POST" label="POST" value="POST" />
        </el-select>
      </el-col>
      <el-col :span="18">
        <el-input v-model="debugUrl" />
      </el-col>
      <el-col :span="4">
        <el-button type="primary" @click="sendFlowDebug">发送</el-button>
        <el-button @click="resetParams">重置</el-button>
      </el-col>
    </el-row>
    <el-tabs model-value="body">
      <el-tab-pane label="Headers" name="requestHeader">
        <div id="app">
          <div class="header-container">
            <div v-for="(header, index) in headers" :key="index" class="header-row">
              <input v-model="header.key" class="header-input" placeholder="Header 键">
              <input v-model="header.value" class="header-input" placeholder="Header 值">
              <button @click="removeHeader(index)" class="btn btn-remove">删除</button>
            </div>
            <button @click="addHeader" class="btn btn-add">添加参数</button>
          </div>
        </div>
      </el-tab-pane>
<!--      <el-tab-pane label="Params" name="params">-->

<!--      </el-tab-pane>-->
      <el-tab-pane label="Body" name="body">
        <el-form-item required>
          <el-input v-model="requestBody" placeholder="请输入" :rows="10" type="textarea"></el-input>
        </el-form-item>
      </el-tab-pane>
    </el-tabs>
    <div class="code-editor-container" :style="{ position: 'relative' }">
      <el-tabs model-value="result">
        <el-tab-pane label="响应内容" name="result">
          <el-text line-clamp="2">
              <CodeEditor ref="codeEditRef" v-model="flowResponseJson" width="1000px" height="250px" language="json" />
          </el-text>
        </el-tab-pane>
        <el-tab-pane label="响应头" name="responseHeader">
          <el-table :data="responseHeaderData" style="width: 100%">
            <el-table-column prop="headerKey" label="响应头" width="350" />
            <el-table-column prop="headerValue" label="值" />
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="日志" name="log">
          <el-card v-for="(item, index) in messages" :key="index" :body-style="{ padding: '10px' }">
            <p>{{ item }}</p>
          </el-card>
        </el-tab-pane>
      </el-tabs>
      <!-- 遮盖层，当 isLoading 为 true 时显示 -->
      <div v-if="isLoading" class="overlay">
        <!-- <span class="loading-text">loading...</span>-->
      </div>
    </div>
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
.header-container {
  max-width: 600px;
  margin: 1px;
}
.header-row {
  display: flex;
  margin-bottom: 10px;
  gap: 10px;
}
.header-input {
  flex: 1;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  color: white;
}
.btn-add {
  background-color: #4CAF50;
}
.btn-remove {
  background-color: #f44336;
}

.code-editor-container {
  position: relative;
  border: 1px solid #ccc; /* 添加边框 */
  border-radius: 5px;      /* 可选：添加圆角效果 */
  padding: 10px;           /* 可选：添加内边距 */
  width: 100%
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.1); /* 半透明背景 */
  display: flex;
  justify-content: center;
  align-items: center;
  color: white;
  font-size: 18px;
  font-weight: bold;
}

.loading-text {
  z-index: 2;
}

.card-content {
  font-size: 18px;
}
</style>
