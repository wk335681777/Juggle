<script lang="ts" setup>
import { computed, ref, onMounted, watch, PropType } from "vue";
import { RawData, ElementType } from "../../types";
import { cloneDeep } from "lodash-es";
import { ElMessage } from "element-plus";
import { flowDefineList } from "@/service/api/flowDefine.ts";
import { useFlowDataInject } from "@/views/flow/design/hooks/flow-data.ts";

const flowContext = useFlowDataInject();
type RedirectRawData = RawData & { appCode: string, flowKey: string, flowName: string };

// 获取默认数据
function getDefaultData(): RedirectRawData {
  return {
    key: "",
    name: "",
    desc: "",
    outgoings: [],
    incomings: [],
    elementType: ElementType.REDIRECT,
    log: "",
    expression: "",
    appCode: "",
    flowName: "",
    flowKey: "",
  };
}

const emit = defineEmits(["update", "cancel"]);
const props = defineProps({
  data: {
    type: Object as PropType<RedirectRawData>,
    required: true,
  },
});

const nodeData = ref(getDefaultData());
const flowList = ref<{ flowKey: string; flowName: string }[]>([]); // 存储从 API 获取的 flowKey 和 flowName 列表

// 监听 props 数据变化
watch(
    () => props.data,
    (val) => {
      if (val !== nodeData.value) {
        nodeData.value = Object.assign(getDefaultData(), cloneDeep(val));
        console.log("Updated nodeData:", nodeData.value);  // 打印 nodeData，检查 flowKey 是否更新
      }
    },
    { immediate: true, deep: true }
);

// 计算可用的流程编码列表
const availableFlowKeys = computed(() => {
  return flowList.value || [];
});

// 组件挂载后获取流程数据
onMounted(() => {
  fetchFlowData();
});

function fetchFlowData() {
  const appCode = flowContext.data.value.appCode;
  if (appCode) {
    flowDefineList({ appCode })
        .then((response) => {
          if (response?.response?.data && Array.isArray(response.response.data)) {
            flowList.value = response.response.data;
            // 更新 flowName 对应的值
            const selectedFlow = flowList.value.find(flow => flow.flowKey === nodeData.value.flowKey);
            if (selectedFlow) {
              nodeData.value.flowName = selectedFlow.flowName;
            }
          }
        })
        .catch((error) => {
          ElMessage.error("获取流程数据失败");
        });
  }
}


function onSubmit() {
  if (!nodeData.value.name) {
    ElMessage.error("请填写必填项！");
    return;
  }

  if (!nodeData.value.flowKey) {
    ElMessage.error("请选择流程编码！");
    return;
  }

  console.log("Submitting data:", nodeData.value);  // 检查提交的数据
  emit("update", cloneDeep(nodeData.value));
}

function onFlowKeyChange(value: string) {
  const selectedFlow = flowList.value.find(flow => flow.flowKey === value);
  if (selectedFlow) {
    nodeData.value.flowName = selectedFlow.flowName;
  }
}

function onCancel() {
  console.log("Cancel button clicked"); // 确认按钮是否触发了点击事件
  emit("cancel");
}
</script>

<template>
  <div class="node-method-form">
    <el-form label-position="top">
      <el-form-item label="节点编码">
        <span>{{ nodeData.key }}</span>
      </el-form-item>
      <el-form-item label="节点名称">
        <el-input v-model="nodeData.name" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item label="节点描述">
        <el-input v-model="nodeData.desc" placeholder="请输入" :rows="2" type="textarea"></el-input>
      </el-form-item>
      <el-select v-model="nodeData.flowKey" placeholder="请选择流程编码" filterable @change="onFlowKeyChange">
        <el-option
            v-for="flow in availableFlowKeys"
            :key="flow.flowKey"
            :value="flow.flowKey"
            :label="flow.flowName"
        ></el-option>
      </el-select>

      <el-form-item>
        <el-button type="primary" @click="onSubmit">确定</el-button>
        <el-button @click="onCancel">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
