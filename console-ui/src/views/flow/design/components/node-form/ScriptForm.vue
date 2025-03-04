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

type ScriptData = RawData & { language: string; expression: string};

function getDefaultData() {
  return {
    key: '',
    name: '',
    desc: '',
    outgoings: [],
    incomings: [],
    elementType: ElementType.SCRIPT,
    log: '',
  };
}


const emit = defineEmits(['update', 'cancel']);
const props = defineProps({
  data: {
    type: Object as PropType<ScriptData>,
    required: true,
  },
});

const nodeData = ref(getDefaultData() as ScriptData);
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
      <el-form-item label="语言" >
        <el-select v-model="nodeData.language" placeholder="请选择语言">
          <el-option key="groovy" label="groovy" value="groovy" />
          <el-option key="qlExpression" label="qlExpression" value="qlExpression" />
        </el-select>
      </el-form-item>
      <!-- 动态显示脚本表达式输入框 -->
      <el-form-item label="脚本表达式" v-if="nodeData.language === 'groovy' || nodeData.language === 'qlExpression'">
        <el-input v-model="nodeData.expression" placeholder="请输入脚本表达式" type="textarea" :rows="3"></el-input>
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
