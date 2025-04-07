<script lang="ts" setup>
import { PropType, ref, watch, onMounted, computed, nextTick } from "vue";
import { ElementType, RawData } from "../../types";
import { cloneDeep } from "lodash-es";
import { ElMessage } from "element-plus";
import { useFlowDataInject } from "@/views/flow/design/hooks/flow-data.ts";

const flowContext = useFlowDataInject(); // 获取流程上下文

type DirectRawData = RawData & { uri: string };

function getDefaultData(): DirectRawData {
  return {
    key: "",
    name: "",
    desc: "",
    outgoings: [],
    incomings: [],
    elementType: ElementType.DIRECT,
    log: ""

  };
}

const emit = defineEmits(["update", "cancel"]);
const props = defineProps({
  data: {
    type: Object as PropType<DirectRawData>,
    required: true,
  },
});

const nodeData = ref(getDefaultData());

const computedFlowKey = computed(() => {
  return props.data.uri || flowContext.data.value.flowKey || "";
});

onMounted(() => {
  console.log("流程上下文 flowKey:", flowContext.data.value.flowKey);
  nodeData.value.uri = computedFlowKey.value;
});

watch(
    () => props.data,
    (newData) => {
      console.log("接收到的新的数据:", newData);
      if (newData) {
        Object.assign(nodeData.value, cloneDeep(newData));
      }
    },
    { immediate: true, deep: true }
);

function validate() {
  if (!nodeData.value.name) {
    ElMessage.error("节点名称不能为空");
    return false;
  }
  return true;
}
function onSubmit() {
  if (!validate()) {
    return;
  }

  // 删除 flowKey 字段，确保提交的数据没有 flowKey
  const finalData = { ...cloneDeep(nodeData.value) };
  delete finalData.flowKey;  // 删除 flowKey 字段

  console.log("提交的数据:", finalData);
  emit('update', finalData);
}


function onCancel() {


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
      <el-form-item label="uri" required>
        <el-input v-model="nodeData.uri" placeholder="自动回填" disabled></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">确定</el-button>
        <el-button @click="onCancel">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<style lang="less" scoped>
.code-btn {
  padding-bottom: 5px;
  margin-left: auto;
}
</style>
