<script lang="ts" setup>
import { PropType, ref, watch } from 'vue';
import { ElementType, RawData } from '../../types';
import { cloneDeep } from 'lodash-es';
import { ElMessage } from 'element-plus';

import { useFlowDataInject } from "@/views/flow/design/hooks/flow-data.ts";

const flowContext = useFlowDataInject();

type EnAndDeData = RawData & {
  encrypt_type: string;
  decrypt_type: string;
  public_key: string;
  private_key: string;
  countersign: string;
  en_decrypt: string;
  sign_code: string;
  encrypt_code: string;
  decrypt_code: string;
  secret: string;
  checkSign: string;
  checkSignCode: string;
};

function getDefaultData() {
  return {
    key: '',
    name: '',
    desc: '',
    outgoings: [],
    incomings: [],
    elementType: ElementType.EN_AND_DE,
    log: '',
    encrypt_type: '',
    decrypt_type: '',
    public_key: '',
    private_key: '',
    countersign: '',
    en_decrypt: '',
    sign_code: '',
    encrypt_code: '',
    decrypt_code: '',
    secret: '',
    checkSign: '',
    checkSignCode: ''
  };
}

const emit = defineEmits(['update', 'cancel']);
const props = defineProps({
  data: {
    type: Object as PropType<EnAndDeData>,
    required: true,
  },
});

const nodeData = ref(getDefaultData() as EnAndDeData);
watch(
    () => props.data,
    (val) => {
      if (val !== nodeData.value) {
        nodeData.value = Object.assign(getDefaultData(), cloneDeep(val));
      }
    },
    { immediate: true }
);

// 表单验证规则
const rules = {
  name: [{ required: true, message: '节点名称不能为空', trigger: 'blur' }],
  en_decrypt: [{ required: true, message: '操作方式（加解密）不能为空', trigger: 'change' }],
  encrypt_type: [{ required: true, message: '加密方式不能为空', trigger: 'change' }],
  decrypt_type: [{ required: true, message: '解密方式不能为空', trigger: 'change' }],
  public_key: [{ required: true, message: '公钥不能为空', trigger: 'blur' }],
  private_key: [{ required: true, message: '私钥不能为空', trigger: 'blur' }],
  secret: [{ required: true, message: '密钥不能为空', trigger: 'blur' }],
  countersign: [{ required: true, message: '是否加签不能为空', trigger: 'change' }],
  sign_code: [{ required: true, message: '加签字段不能为空', trigger: 'blur' }],
  checkSign: [{ required: true, message: '是否验签不能为空', trigger: 'change' }],
  checkSignCode: [{ required: true, message: '验签字段不能为空', trigger: 'blur' }]
};


const formRef = ref(null);

// 表单验证函数
function validate() {
  return new Promise((resolve, reject) => {
    formRef.value?.validate((valid: boolean) => {
      if (valid) {
        resolve(true);
      } else {
        reject(false);
      }
    });
  });
}

function onSubmit() {
  validate().then(() => {
    emit('update', cloneDeep(nodeData.value));
  }).catch(() => {
    ElMessage.error('请填写所有必填项');
  });
}

function onCancel() {
  emit('cancel');
}

// 监听 en_decrypt 字段变化时清空相关字段
watch(() => nodeData.value.en_decrypt, (newVal) => {
  nodeData.value.encrypt_type = '';
  nodeData.value.decrypt_type = '';
  nodeData.value.public_key = '';
  nodeData.value.private_key = '';
  nodeData.value.secret = '';
  nodeData.value.encrypt_code = '';
  nodeData.value.decrypt_code = '';
  nodeData.value.countersign = '';
  nodeData.value.sign_code = '';
  nodeData.value.checkSign = '';
  nodeData.value.checkSignCode = '';
});

watch(() => nodeData.value.countersign, (newVal) => {
  nodeData.value.sign_code = '';
});
watch(() => nodeData.value.checkSign, (newVal) => {
  nodeData.value.checkSignCode = '';
});

watch(() => nodeData.value.encrypt_type, (newVal) => {
  nodeData.value.public_key = '';
  nodeData.value.private_key = '';
  nodeData.value.secret = '';
  nodeData.value.encrypt_code = '';
  nodeData.value.countersign = '';
  nodeData.value.sign_code = '';
  nodeData.value.checkSign = '';
  nodeData.value.checkSignCode = '';
});

watch(() => nodeData.value.decrypt_type, (newVal) => {
  nodeData.value.public_key = '';
  nodeData.value.private_key = '';
  nodeData.value.secret = '';
  nodeData.value.decrypt_code = '';
  nodeData.value.countersign = '';
  nodeData.value.sign_code = '';
  nodeData.value.checkSign = '';
  nodeData.value.checkSignCode = '';
});
</script>

<template>
  <div class="node-method-form">
    <el-form ref="formRef" label-position="top" :model="nodeData" :rules="rules">
      <el-form-item label="节点编码">
        <span>{{ nodeData.key }}</span>
      </el-form-item>
      <el-form-item label="节点名称" prop="name">
        <el-input v-model="nodeData.name" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item label="节点描述" prop="desc">
        <el-input v-model="nodeData.desc" placeholder="请输入" :rows="2" type="textarea"></el-input>
      </el-form-item>
      <el-form-item label="加解密" prop="en_decrypt" required>
        <el-select v-model="nodeData.en_decrypt" placeholder="请选择操作方式">
          <el-option key="encrypt" label="加密" value="加密" />
          <el-option key="decrypt" label="解密" value="解密" />
        </el-select>
      </el-form-item>

      <el-form-item label="加密方式" prop="encrypt_type" required v-if="nodeData.en_decrypt === '加密'">
        <el-select v-model="nodeData.encrypt_type" placeholder="请选择加密方式">
          <el-option key="RSA" label="RSA" value="RSA" />
          <el-option key="3DES" label="3DES" value="3DES" />
          <el-option key="AES"  label="AES" value="AES"/>
        </el-select>
      </el-form-item>
      <el-form-item label="解密方式" prop="decrypt_type" required v-if="nodeData.en_decrypt === '解密'">
        <el-select v-model="nodeData.decrypt_type" placeholder="请选择解密方式">
          <el-option key="RSA" label="RSA" value="RSA" />
          <el-option key="3DES" label="3DES" value="3DES" />
          <el-option key="AES"  label="AES" value="AES"/>
        </el-select>
      </el-form-item>

      <el-form-item label="公钥" prop="public_key" required v-if="nodeData.en_decrypt === '加密' && nodeData.encrypt_type === 'RSA'">
        <el-input v-model="nodeData.public_key" placeholder="请输入公钥"></el-input>
      </el-form-item>
      <el-form-item label="私钥" prop="private_key" required v-if="nodeData.en_decrypt === '加密' && nodeData.encrypt_type === 'RSA'">
        <el-input v-model="nodeData.private_key" placeholder="请输入私钥"></el-input>
      </el-form-item>
      <el-form-item label="密钥" prop="secret" required v-if="nodeData.en_decrypt === '加密' && nodeData.encrypt_type === '3DES' || nodeData.encrypt_type==='AES'">
        <el-input v-model="nodeData.secret" placeholder="请输入密钥"></el-input>
      </el-form-item>

      <el-form-item label="加密字段" prop="encrypt_code" v-if="nodeData.en_decrypt === '加密'">
        <el-input v-model="nodeData.encrypt_code" placeholder="请输入加签字段"></el-input>
      </el-form-item>

      <el-form-item label="私钥" prop="private_key" required v-if="nodeData.en_decrypt === '解密' && nodeData.decrypt_type === 'RSA'">
        <el-input v-model="nodeData.private_key" placeholder="请输入私钥"></el-input>
      </el-form-item>
      <el-form-item label="密钥" prop="secret" required v-if="nodeData.en_decrypt === '解密' && nodeData.decrypt_type === '3DES'|| nodeData.decrypt_type==='AES'">
        <el-input v-model="nodeData.secret" placeholder="请输入密钥"></el-input>
      </el-form-item>

      <el-form-item label="解密字段" prop="decrypt_code" v-if="nodeData.en_decrypt === '解密'">
        <el-input v-model="nodeData.decrypt_code" placeholder="请输入解密字段"></el-input>
      </el-form-item>

      <el-form-item label="是否加签" prop="countersign" required v-if="nodeData.en_decrypt === '加密'">
        <el-select v-model="nodeData.countersign" placeholder="请选择是否加签">
          <el-option key="是" label="是" value="是" />
          <el-option key="否" label="否" value="否" />
        </el-select>
      </el-form-item>
      <el-form-item label="加签字段" prop="sign_code"  v-if="nodeData.countersign === '是' && nodeData.en_decrypt === '加密'">
        <el-input v-model="nodeData.sign_code" placeholder="请输入加签字段"></el-input>
      </el-form-item>

      <el-form-item label="是否验签" prop="checkSign" required v-if="nodeData.en_decrypt === '解密'">
        <el-select v-model="nodeData.checkSign" placeholder="请选择是否验签">
          <el-option key="是" label="是" value="是" />
          <el-option key="否" label="否" value="否" />
        </el-select>
      </el-form-item>
      <el-form-item label="验签字段" prop="checkSignCode" required v-if="nodeData.checkSign === '是' && nodeData.en_decrypt === '解密'">
        <el-input v-model="nodeData.checkSignCode" placeholder="请输入验签字段"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit">确定</el-button>
        <el-button @click="onCancel">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
