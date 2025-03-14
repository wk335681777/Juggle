<script setup lang="ts">
import { reactive, ref, watch } from 'vue';
import { useRoute, onBeforeRouteLeave } from 'vue-router';
import { flowDefineService, flowVersionService } from '@/service';
import {ElMessage, ElMessageBox} from 'element-plus';
import CodeEditor from '@/components/common/CodeEditor.vue';
import { DataType, FlowDefineInfo } from '@/typings';
import { flowAPI } from '@/service/api';
import { deleteParamsFlow, viewParamsFlow} from '@/service/api/flow.ts';

// 获取路由参数
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
  flowKey: '',
});
let requestBody = ref('');
const responseHeaderData = ref([]);

queryFlowDefineInfo();

function generateUUID() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
    var r = (Math.random() * 16) | 0,
      v = c === 'x' ? r : (r & 0x3) | 0x8;
    return v.toString(16);
  });
}

const debugId = generateUUID(); // 生成 debugConnId

const dialogVisible = ref(false);
let eventSource; // 将 EventSource 声明在这里

// 追踪是否点击了查看参数
const isViewParamsClicked = ref(false);

// 动态生成 EventSource URL，不将 debugConnId 传给后端
const getEventSourceUrl = () => {
  let baseUrl = `/camelLogStream/logStream?debugConnId=${debugId}`; // debugConnId 仅用于前端
  if (isViewParamsClicked.value) {
    baseUrl += `&appCode=${savedParams.value.appCode}&id=${savedParams.value.id}`;
  }
  return baseUrl;
};

// 查询流程定义信息
async function queryFlowDefineInfo() {
  const res = await flowDefineService.getDebugInfo(paramsData.params.flowDefinitionId as number);
  if (res.success) {
    debugUrl.value = res.result.debugUri; // 不需要拼接 debugConnId 到 debugUrl
    flowDefine.value = res.result;
    savedParams.value.id = res.result.id || 0;
    savedParams.value.appCode = res.result.appCode || '';
    console.log('初始化 savedParams:', savedParams.value);
  } else {
    ElMessage({ type: 'error', message: res.errorMsg });
  }
}

async function sendFlowDebug() {
  if (!validate()) {
    return;
  }
  isLoading.value = true;
  const params = {
    flowData: getParams(), // 这里的 getParams 已经排除了 debugConnId
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

// 获取流程调试参数
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

// 验证参数是否为空
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

// 获取参数
function getParams() {
  const flowInputParams = flowDefine.value?.flowInputParams || [];
  const params: any = {};

  // 只收集需要的参数，排除 debugConnId
  flowInputParams.forEach((param: any) => {
    if (!isEmpty(param.value)) {
      const dataType: DataType = param.dataType;
      if (dataType.type === 'Object' || dataType.type === 'List') {
        params[param.paramKey] = JSON.parse(param.value);
      } else {
        params[param.paramKey] = param.value;
      }
    }
  });

  // 添加其他必要的参数（这里不包括 debugConnId）
  params['requestBody'] = requestBody.value;
  params['uri'] = debugUrl.value; // 只使用 debugUrl，而不包括 debugConnId
  params['headers'] = headers.value;
  params['httpMethod'] = httpMethod.value;

  return params; // 这里不包括 debugConnId
}

function resetParams() {
  flowDefine.value?.flowInputParams.forEach((param: any) => {
    param.value = '';
    param.error = '';
  });
}

const createEventSource = () => {
  // 仅在前端传递 debugConnId 给 EventSource
  const url = getEventSourceUrl(); // 这里使用了 debugConnId，但是它不会传递到后端
  eventSource = new EventSource(url);

  eventSource.onmessage = function (event) {
    if (event.data == '' || event.data == '\n') {
      return;
    }

    const line = event.data.replace(/<br>/g, '\n');
    addMessage(line);
  };

  eventSource.onerror = function (error) {
    console.log('Error:', error);
  };
};

// 监听页面离开事件，关闭 EventSource
onBeforeRouteLeave((to, from, next) => {
  console.log('准备离开当前页面，执行清理操作');
  if (eventSource) {
    eventSource.close();
  }
  next();
});

// 格式化 JSON
watch(flowResponseJson, newJson => {
  try {
    const jsonObject = JSON.parse(newJson);
    flowResponseJson.value = JSON.stringify(jsonObject, null, 2);
  } catch (error) {
    flowResponseJson.value = newJson;
  }
});

// 用来保存 HTTP headers 的数组
const headers = ref([{ key: '_mock_', value: 'true' }]);

// 添加新的 HTTP header 行
const addHeader = () => {
  headers.value.push({ key: '', value: '' });
};

// 删除某一行
const removeHeader = index => {
  headers.value.splice(index, 1);
};

// 提交 HTTP headers，发送请求或其他操作
const submitHeaders = () => {
  console.log('Submitting headers:', headers.value);
};

const httpMethod = ref('POST');
const isLoading = ref(false);
const messages = ref([]);
const addMessage = line => {
  messages.value.push(line);
};

const savedParams = ref({
  id: 0,
  appCode: '',
  headers: [],
  body: '',
});

// 保存参数
const saveParams = async () => {
  const currentTab = activeTab.value;
  let paramsToSave = {
    id: savedParams.value.id,
    appCode: savedParams.value.appCode,
    headers: headers.value.map(header => ({
      key: header.key,
      value: header.value,
    })),
    body: requestBody.value,
  };

  if (currentTab === 'requestHeader') {
    paramsToSave.headers = headers.value.map(header => ({
      key: header.key,
      value: header.value,
    }));
  } else if (currentTab === 'body') {
    paramsToSave.body = requestBody.value;
  }

  console.log('最终提交参数:', JSON.stringify(paramsToSave, null, 2));

  try {
    const res = await flowAPI.saveParamsFlow(paramsToSave);
    if (res.success) {
      ElMessage({ type: 'success', message: '参数已保存' });
    } else {
      ElMessage({ type: 'error', message: res.errorMsg });
    }
  } catch (error) {
    ElMessage({ type: 'error', message: '保存参数时出错' });
    console.error(error);
  }
};
const activeTab = ref('requestHeader');


// 查看参数
const savedParamsData = ref([]);
const totalRecords = ref(0);
const currentPage = ref(1);
const pageSize = ref(10);

const viewParams = async () => {
  try {
    const response = await viewParamsFlow({
      appCode: savedParams.value.appCode,
      id: savedParams.value.id,
      pageNum: currentPage.value,
      pageSize: pageSize.value,
    });


    if (response.success) {
      debugger
      savedParamsData.value = response.result;
      totalRecords.value = response.total;
      console.log('更新后：',savedParamsData);
      dialogVisible.value = true;
    } else {
      ElMessage({ type: 'error', message: response.errorMsg });
    }
  } catch (error) {
    ElMessage({ type: 'error', message: '无法加载参数' });
  }
};

const resetDialog = () => {
  dialogVisible.value = false;
};
const handlePageChange = newPage => {
  // 更新当前页码
  currentPage.value = newPage;
  viewParams(); // 获取当前页的数据
};

//删除数据
const deleteRow = async (row) => {
  const { id } = row;

  // 显示确认框
  try {
    const confirmResult = await ElMessageBox.confirm(
        '确定删除这条数据吗?',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
    );

    // 点击“确定”
    if (confirmResult === 'confirm') {
      const res = await deleteParamsFlow(id);

      if (res.success) {
        ElMessage({ type: 'success', message: '删除成功' });
        viewParams(); // 重新加载数据
      } else {
        ElMessage({ type: 'error', message: res.errorMsg });
      }
    }
  } catch (error) {
    console.log('用户取消了删除');
  }
};


const enableRow = (row) => {
  // 清空之前的 body 和 headers
  requestBody.value = '';
  savedParams.value.headers = [];

  // 启用选中的行数据
  if (row.headers) {
    try {
      headers.value = row.headers && typeof row.headers === 'string' ? JSON.parse(row.headers) : [];
    } catch (e) {
      ElMessage({ type: 'error', message: '解析 headers 失败' });
    }
  }

  if (row.body) {
    requestBody.value = row.body;
  }

  console.log('启用成功，已回填数据:', row);
  ElMessage({ type: 'success', message: '已启用该参数，并回填数据' });
  // 自动关闭弹窗
  dialogVisible.value = false;

  // 在数据更新后确保视图也更新
  this.$nextTick(() => {
    console.log('视图已更新');
  });
};



</script>

<template>
  <div class="flow-debug">
    <div class="flow-header">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>{{ flowDefine.flowName }} - {{ flowDefine.flowKey }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <el-dialog title="查看保存的参数" v-model="dialogVisible" width="50%" @close="resetDialog" style="z-index: 9999;">
      <el-table :data="savedParamsData" style="width: 100%">
        <el-table-column prop="id" label="id" width="50" />

        <!-- headers列，超出宽度时显示省略号 -->
        <el-table-column label="headers" width="280">
          <template #default="{ row }">
            <div style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis; max-width: 270px;">
              <el-tooltip :content="row.headers" placement="top">
                <span>{{ row.headers }}</span>
              </el-tooltip>
            </div>
          </template>
        </el-table-column>

        <!-- body列，超出宽度时显示省略号 -->
        <el-table-column label="body" width="280">
          <template #default="{ row }">
            <div style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis; max-width: 270px;">
              <el-tooltip :content="row.body" placement="top">
                <span>{{ row.body }}</span>
              </el-tooltip>
            </div>
          </template>
        </el-table-column>

        <!-- 操作列：删除和启用按钮放在同一行 -->
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <div style="display: flex; gap: 2px;">
              <el-button @click="enableRow(row)" type="success" size="small">启用</el-button>
              <el-button @click="deleteRow(row)" type="danger" size="small">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-if="totalRecords > 0"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="totalRecords"
        @current-change="handlePageChange"
        layout="total, prev, pager, next, jumper"
      />

      <el-form-item>
        <el-button @click="dialogVisible = false" type="primary">关闭</el-button>
      </el-form-item>
    </el-dialog>

    <el-row :gutter="16">
      <el-col :span="2">
        <el-select v-model="httpMethod" placeholder="请选择加密方式">
          <el-option key="GET" label="GET" value="GET" />
          <el-option key="POST" label="POST" value="POST" />
        </el-select>
      </el-col>
      <el-col :span="15">
        <el-input v-model="debugUrl" />
      </el-col>
      <el-col :span="7">
        <el-button type="primary" @click="sendFlowDebug">发送</el-button>
        <el-button @click="resetParams">重置</el-button>
        <el-button @click="saveParams">保存参数</el-button>
        <el-button @click="viewParams">查看参数</el-button>
      </el-col>
    </el-row>

    <el-tabs v-model="activeTab">
      <el-tab-pane label="Headers" name="requestHeader">
        <div id="app">
          <div class="header-container">
            <div v-for="(header, index) in headers" :key="index">
              <input v-model="header.key" class="header-input" placeholder="Header 键" />
              <input v-model="header.value" class="header-input" placeholder="Header 值" />
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
        <el-tab-pane label="日志" name="log" class="black-tab">
          <el-card v-for="(item, index) in messages" :key="index" :body-style="{ padding: '0px' }" class="black-tab">
            <div style="color: white">{{ item }}</div>
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
  background-color: #4caf50;
}
.btn-remove {
  background-color: #f44336;
}

.code-editor-container {
  position: relative;
  border: 1px solid #ccc; /* 添加边框 */
  border-radius: 5px; /* 可选：添加圆角效果 */
  padding: 10px; /* 可选：添加内边距 */
  width: 100%;
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
.el-dialog {
  z-index: 9999 !important;
}
.black-tab {
  background-color: black !important;
  color: white;
  padding: 2px; /* 防止内容贴边 */
  font-size: 12px;
  border: none !important; /* 移除默认边框 */
  border-bottom: 0px solid white !important; /* 仅显示底部边框 */
  border-radius: 0; /* 防止圆角影响 */
}
</style>
