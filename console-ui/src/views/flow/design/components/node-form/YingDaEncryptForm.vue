<script lang="ts" setup>
import { PropType, ref, watch } from 'vue';
import { ElementType, RawData } from '../../types';
import { cloneDeep } from 'lodash-es';
import { ElMessage } from 'element-plus';

import { useFlowDataInject } from "@/views/flow/design/hooks/flow-data.ts";

const flowContext = useFlowDataInject();

type YDa_SM4Data = RawData & {
  sm2PrivateKey: string;
  sm2PublicKey: string;
  encodingType: string;
  sm4EncryptType: string;
  encryptMode: string;
  ak: string;
  sk: string;
  path: string;
};

function getDefaultData() {
  return {
    key: '',
    name: '',
    desc: '',
    outgoings: [],
    incomings: [],
    elementType: ElementType.YDA_SM4,
    log: '',
    encryptMode: '',
    sm2PublicKey: '',
    sm2PrivateKey: '',
    encodingType: '',
    sm4EncryptType: '',
    ak: '',
    sk: '',
    path: '',
  };
}

const emit = defineEmits(['update', 'cancel']);
const props = defineProps({
  data: {
    type: Object as PropType<YDa_SM4Data>,
    required: true,
  },
});

const nodeData = ref(getDefaultData() as YDa_SM4Data);

// 监听 prop 数据变化
watch(
    () => props.data,
    (val) => {
      if (val !== nodeData.value) {
        nodeData.value = Object.assign(getDefaultData(), cloneDeep(val));
      }
    },
    { immediate: true }
);

// 获取表单引用
const formRef = ref(null);

// 表单验证函数
function validate() {
  return new Promise((resolve, reject) => {
    // 触发表单验证
    formRef.value?.validate((valid: boolean) => {
      if (valid) {
        resolve(true);
      } else {
        ElMessage.error('请填写必填项');
        reject(false);
      }
    });
  });
}

// 提交函数
function onSubmit() {
  validate().then(() => {
    emit('update', cloneDeep(nodeData.value));
  });
}

// 取消函数
function onCancel() {
  emit('cancel');
}

// 监听 encryptMode字段变化时清空相关字段
watch(() => nodeData.value.encryptMode, (newVal) => {
  nodeData.value.sm2PublicKey = '';
  nodeData.value.sm2PrivateKey = '';
  nodeData.value.encodingType = '';
  nodeData.value.sm4EncryptType = '';
  nodeData.value.path = '';
});
</script>

<template>
  <div class="node-method-form">
    <el-form ref="formRef" label-position="top" :model="nodeData" :rules="rules">
      <el-form-item label="节点编码">
        <span>{{ nodeData.key }}</span>
      </el-form-item>

      <el-form-item label="节点名称" prop="name" :rules="[{ required: true, message: '节点名称不能为空', trigger: 'blur' }]">
        <el-input v-model="nodeData.name" placeholder="请输入"></el-input>
      </el-form-item>

      <el-form-item label="节点描述" prop="desc">
        <el-input v-model="nodeData.desc" placeholder="请输入" :rows="2" type="textarea"></el-input>
      </el-form-item>

      <el-form-item label="AK" prop="ak" :rules="[{ required: true, message: '不能为空', trigger: 'blur' }]">
        <el-input v-model="nodeData.ak" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item label="SK" prop="sk" :rules="[{ required: true, message: '不能为空', trigger: 'blur' }]">
        <el-input v-model="nodeData.sk" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item label="加解密" required prop="encryptMode">
        <el-select v-model="nodeData.encryptMode" placeholder="请选择加解密类型">
          <el-option key="encrypt" label="加密" value="加密" />
          <el-option key="decrypt" label="解密" value="解密" />
        </el-select>
      </el-form-item>

      <el-form-item label="公钥" required v-if="nodeData.encryptMode === '加密'" prop="sm2PublicKey" :rules="[{ required: true, message: '公钥不能为空', trigger: 'blur' }]">
        <el-input v-model="nodeData.sm2PublicKey" placeholder="请输入公钥"></el-input>
      </el-form-item>

      <el-form-item label="密钥" required v-if="nodeData.encryptMode === '解密'" prop="sm2PrivateKey" :rules="[{ required: true, message: '密钥不能为空', trigger: 'blur' }]">
        <el-input v-model="nodeData.sm2PrivateKey" placeholder="请输入密钥"></el-input>
      </el-form-item>

      <el-form-item label="数据格式" required prop="encodingType" :rules="[{ required: true, message: '数据格式不能为空', trigger: 'blur' }]">
        <el-select v-model="nodeData.encodingType" placeholder="请选择数据格式">
          <el-option key="TEXT" label="TEXT" value="TEXT" />
          <el-option key="BASE64" label="BASE64" value="BASE64" />
          <el-option key="HEX" label="HEX" value="HEX" />
        </el-select>
      </el-form-item>

      <el-form-item label="加解密模式" required prop="sm4EncryptType" :rules="[{ required: true, message: '加解密模式不能为空', trigger: 'blur' }]">
        <el-select v-model="nodeData.sm4EncryptType" placeholder="请选择加解密模式">
          <el-option key="ECB" label="ECB" value="ECB" />
          <el-option key="CBC" label="CBC" value="CBC" />
        </el-select>
      </el-form-item>
      <el-form-item label="Path" required v-if="nodeData.encryptMode === '加密'" prop="path" :rules="[{ required: true, message: '不能为空', trigger: 'blur' }]">
        <el-input v-model="nodeData.path" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">确定</el-button>
        <el-button @click="onCancel">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
