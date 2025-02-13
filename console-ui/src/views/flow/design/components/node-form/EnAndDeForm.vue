<script lang="ts" setup>
import {PropType, ref, watch} from 'vue';
import {ElementType,RawData} from '../../types';
import { cloneDeep } from 'lodash-es';
import { ElMessage } from 'element-plus';

import {useFlowDataInject} from "@/views/flow/design/hooks/flow-data.ts";


const flowContext = useFlowDataInject();

type EnAndDeData = RawData & { encrypt_type: string;decrypt_type:string ; public_key: string; private_key: string;countersign: string;
  en_decrypt:string;sign_code:string;encrypt_code:string;decrypt_code:string;secret:string;check_sign:string;
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
<!--      <el-form-item label="加解密" required>-->
<!--        <el-select v-model="nodeData.en_decrypt" placeholder="请选择操作方式">-->
<!--          <el-option key="加密" label="加密" value="加密" />-->
<!--          <el-option key="解密" label="解密" value="解密" />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="加解密方式" required>-->
<!--        <el-select v-model="nodeData.type" placeholder="请选择加解密方式">-->
<!--          <el-option key="RSA" label="RSA" value="RSA" />-->
<!--          <el-option key="3DES" label="3DES" value="3DES" />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="公钥" required>-->
<!--        <el-input v-model="nodeData.public_key"  placeholder="请输入公钥"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="私钥" required>-->
<!--        <el-input v-model="nodeData.private_key"  placeholder="请输入私钥"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="密钥" required>-->
<!--        <el-input v-model="nodeData.secret"  placeholder="请输入密钥"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="加签" required>-->
<!--        <el-select v-model="nodeData.countersign" placeholder="是否加签">-->
<!--          <el-option key="是" label="是" value="是" />-->
<!--          <el-option key="否" label="否" value="否" />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="加签字段" required>-->
<!--        <el-input v-model="nodeData.sign"  placeholder="请输入需加签字段"></el-input>-->
<!--      </el-form-item>-->
      <el-form-item label="加解密" required>
        <el-select v-model="nodeData.en_decrypt" placeholder="请选择操作方式">
          <el-option key="加密" label="加密" value="加密" />
          <el-option key="解密" label="解密" value="解密" />
        </el-select>
      </el-form-item>

      <el-form-item label="加密方式" required v-if="nodeData.en_decrypt === '加密'">
        <el-select v-model="nodeData.encrypt_type" placeholder="请选择加密方式">
          <el-option key="RSA" label="RSA" value="RSA" />
          <el-option key="3DES" label="3DES" value="3DES" />
        </el-select>
      </el-form-item>
      <el-form-item label="解密方式" required v-if="nodeData.en_decrypt === '解密'">
        <el-select v-model="nodeData.decrypt_type" placeholder="请选择解密方式">
          <el-option key="RSA" label="RSA" value="RSA" />
          <el-option key="3DES" label="3DES" value="3DES" />
        </el-select>
      </el-form-item>
      <!--加密方式-->
      <el-form-item label="公钥" required v-if="nodeData.en_decrypt === '加密' && nodeData.encrypt_type === 'RSA'">
        <el-input v-model="nodeData.public_key" placeholder="请输入公钥"></el-input>
      </el-form-item>
      <el-form-item label="私钥" required v-if="nodeData.en_decrypt=== '加密' && nodeData.encrypt_type === 'RSA'">
        <el-input v-model="nodeData.private_key" placeholder="请输入私钥"></el-input>
      </el-form-item>
      <el-form-item label="密钥" required v-if="nodeData.en_decrypt === '加密' && nodeData.encrypt_type === '3DES'">
        <el-input v-model="nodeData.secret" placeholder="请输入密钥"></el-input>
      </el-form-item>
      <!--加密字段-->
      <el-form-item label="加密字段" required v-if="nodeData.en_decrypt === '加密'">
        <el-input v-model="nodeData.encrypt_code" placeholder="请输入加签字段"></el-input>
      </el-form-item>
      <!--解密方式-->
      <el-form-item label="私钥" required v-if="nodeData.en_decrypt === '解密' && nodeData.decrypt_type === 'RSA'">
        <el-input v-model="nodeData.private_key" placeholder="请输入私钥"></el-input>
      </el-form-item>
      <el-form-item label="密钥" required v-if="nodeData.en_decrypt === '解密' && nodeData.decrypt_type === '3DES'">
        <el-input v-model="nodeData.secret" placeholder="请输入密钥"></el-input>
      </el-form-item>
      <!--解密字段-->
      <el-form-item label="解密字段" required v-if="nodeData.en_decrypt === '解密'">
        <el-input v-model="nodeData.decrypt_code" placeholder="请输入解密字段"></el-input>
      </el-form-item>
      <!-- 加签-->
      <el-form-item label="是否加签" required v-if="nodeData.en_decrypt === '加密'">
        <el-select v-model="nodeData.countersign" placeholder="请选择是否加签">
          <el-option key="是" label="是" value="是" />
          <el-option key="否" label="否" value="否" />
        </el-select>
      </el-form-item>

      <el-form-item label="是否加签" required v-if="nodeData.en_decrypt === '解密'">
        <el-select v-model="nodeData.countersign" placeholder="请选择是否加签">
          <el-option key="是" label="是" value="是" />
          <el-option key="否" label="否" value="否" />
        </el-select>
      </el-form-item>

      <el-form-item label="加签字段" required v-if="nodeData.countersign === '是'&& nodeData.en_decrypt === '加密' ">
        <el-input v-model="nodeData.sign_code" placeholder="请输入加签字段"></el-input>
      </el-form-item>

      <el-form-item label="加签字段" required v-if="nodeData.countersign === '是'&& nodeData.en_decrypt === '解密' ">
        <el-input v-model="nodeData.sign_code" placeholder="请输入加签字段"></el-input>
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
