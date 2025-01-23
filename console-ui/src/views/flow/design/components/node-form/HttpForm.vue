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

type NettyHttpData = RawData & { uri: string; contextType: string; method: string; bridgeEndpoint: boolean };

function getDefaultData() {
  return {
    key: '',
    name: '',
    desc: '',
    outgoings: [],
    incomings: [],
    elementType: ElementType.HTTP,
    log: '',
  };
}


const emit = defineEmits(['update', 'cancel']);
const props = defineProps({
  data: {
    type: Object as PropType<NettyHttpData>,
    required: true,
  },
});

const nodeData = ref(getDefaultData() as LogRawData);
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
      <el-form-item label="uri" required>
        <el-input v-model="nodeData.uri"  placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item label="bridgeEndpoint" required>
        <el-select v-model="nodeData.bridgeEndpoint" placeholder="请输入">
          <el-option key="true" label="true" value="true" />
          <el-option key="false" label="false" value="false" />
        </el-select>
      </el-form-item>
      <el-form-item label="报文格式" required>
        <el-select v-model="nodeData.contextType" placeholder="请选接收报文数据格式">
          <el-option key="json" label="json" value="json" />
          <el-option key="xml" label="xml" value="xml" />
        </el-select>
      </el-form-item>
      <el-form-item label="method" required>
        <el-select v-model="nodeData.method" placeholder="请输入">
          <el-option key="get" label="get" value="get" />
          <el-option key="post" label="post" value="post" />
        </el-select>
      </el-form-item>
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
