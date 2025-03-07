<script lang="ts" setup>
import {computed, PropType, ref, watch} from 'vue';
import {ElementType, FlowVariable, FlowVariableType, RawData} from '../../types';
import { cloneDeep } from 'lodash-es';
import { ElMessage } from 'element-plus';
import { Delete } from '@element-plus/icons-vue';
import {useFlowDataInject} from "@/views/flow/design/hooks/flow-data.ts";
import {DataType, RuleItem, valueType} from "@/typings";
import FilterValue from "@/components/filter/FilterValue.vue";
import {getVariableDataType, isDataTypeEqual, isDataTypeMatch} from "@/utils/dataType.ts";
import VariableSelect from '@/components/common/VariableSelect.vue';
import DataTypeDisplay from "@/components/common/DataTypeDisplay.vue";

const flowContext = useFlowDataInject();

type SetHeadersRawData = RawData & {
  headers:Array<{headerName:string;headerValue:string}>;
};

function getDefaultData() {
  return {
    key: '',
    name: '',
    desc: '',
    outgoings: [],
    incomings: [],
    elementType: ElementType.SET_HEADERS,
    log: '',
    headers: [{ headerName: '', headerValue: '' }], // 初始化为一对空的 header
  };
}


const emit = defineEmits(['update', 'cancel']);
const props = defineProps({
  data: {
    type: Object as PropType<SetHeadersRawData>,
    required: true,
  },
});

const nodeData = ref(getDefaultData() as SetHeadersRawData);
watch(
    () => props.data,
    val => {
      if (val !== nodeData.value) {
        nodeData.value = Object.assign(getDefaultData(), cloneDeep(val));
      }
    },
    { immediate: true }
);

function validate() {
  if (!nodeData.value.name) {
    ElMessage.error('节点名称不能为空');
    return false;
  }
  return true;
}
function addHeader() {
  nodeData.value.headers.push({ headerName: '', headerValue: '' }); // 添加空的键值对对象
}
function removeHeader(index: number) {
  nodeData.value.headers.splice(index, 1); // 删除指定的项
}

function onSubmit() {
  if (!validate()) {
    return;
  }
  emit('update', cloneDeep(nodeData.value));
}
function onCancel() {
  emit('cancel');
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

      <!-- 动态生成 header 列表 -->
      <div v-for="(header, index) in nodeData.headers" :key="index">
        <el-row gutter={20}>
          <el-col :span="11">
            <!-- 仅在第一个 header 显示 headerName 标签 -->
            <el-form-item v-if="index === 0" label="headerName" required>
              <el-input v-model="header.headerName" placeholder="请输入 headerName"></el-input>
            </el-form-item>
            <!-- 后续项不显示 headerName 标签 -->
            <el-form-item v-if="index !== 0">
              <el-input v-model="header.headerName" placeholder="请输入 headerName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <!-- 仅在第一个 header 显示 headerValue 标签 -->
            <el-form-item v-if="index === 0" label="headerValue" required>
              <el-input v-model="header.headerValue" placeholder="请输入 headerValue"></el-input>
            </el-form-item>
            <!-- 后续项不显示 headerValue 标签 -->
            <el-form-item v-if="index !== 0">
              <el-input v-model="header.headerValue" placeholder="请输入 headerValue"></el-input>
            </el-form-item>
          </el-col>
          <!-- 删除按钮，只有在不是第一个 header 时才显示 -->
          <el-col v-if="index !== 0" :span="2">
            <el-button type="danger" @click="removeHeader(index)" size="mini">删除</el-button>
          </el-col>
        </el-row>
      </div>

      <!-- 添加 Header 按钮 -->
      <el-button @click="addHeader">添加 Header</el-button>

      <el-form-item>
        <el-button type="primary" @click="onSubmit">确定</el-button>
        <el-button @click="onCancel">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<style lang="less" scoped>
.code-btn{
  padding-bottom: 5px;
  margin-left: auto;
}
</style>
