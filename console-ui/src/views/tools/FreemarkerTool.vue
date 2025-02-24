<script lang="ts" setup>
import { useRouter } from 'vue-router';
import {ref} from 'vue';
import {Search} from "@element-plus/icons-vue";
import { reactive } from 'vue';
import { toolService } from '@/service';
import {ElMessage} from "element-plus";
import CodeEditor from "@/components/common/CodeEditor.vue";
const router = useRouter();
const freemarker = ref({
  "bodyParam": "",
  "headerParamList": [{"key": "", "value": ""}],
  "template": "",
})
const result = ref('');

const codeEditRef = ref<InstanceType<typeof CodeEditor>>();

function addParameter() {
  freemarker.value.headerParamList.push({ key: '', value: '' });
}

function removeParameter(index: number) {
  freemarker.value.headerParamList.splice(index, 1);
}

async function onSubmit() {
  const res = await toolService.freemarkerExecute(freemarker.value);
  if (res.success) {
    result.value = res.result;
    ElMessage({ type: 'success', message: '请求完成' });
  } else {
    ElMessage({ type: 'error', message: res.errorMsg });
  }
}

</script>

<template>
  <div class="node-method-form">
    <el-form ref="form" label-position="top" :model="nodeData">
      <el-form-item label="模板：" required>
        <el-input v-model="freemarker.template" placeholder="请输入" :rows="5" type="textarea"></el-input>
      </el-form-item>
      <el-form-item label="Body参数：" required>
        <el-input v-model="freemarker.bodyParam" placeholder="请输入" :rows="5" type="textarea"></el-input>
      </el-form-item>
      <el-form-item label="header参数：">
        <div  :key="index" class="parameter-item" style="width: 100%">
          <el-row v-for="(param, index) in freemarker.headerParamList" :gutter="10" align="middle">
            <!-- Key 输入框 -->
            <el-col :span="4">
              <el-input v-model="param.key" placeholder="请输入参数的key" />
            </el-col>

            <!-- Value 输入框 -->
            <el-col :span="18" >
              <el-input v-model="param.value" placeholder="请输入参数的值" />
            </el-col>

            <!-- 操作按钮 -->
            <el-col :span="2" class="button-group" style="display: flex; align-items: center;">
              <!-- 加号按钮 -->
              <el-button
                  size="mini"
                  @click="addParameter"
                  type="primary"
                  class="add-btn"
                  style="background-color: cornflowerblue; border-color: cornflowerblue; color: #fff; padding: 4px 8px; font-size: 14px; margin-left: 2px;"
              >
                +
              </el-button>

              <!-- 减号按钮 -->
              <el-button
                  size="mini"
                  type="danger"
                  @click="removeParameter(index)"
                  v-if="freemarker.headerParamList.length > 1"
                  class="remove-btn"
                  style="background-color: red; border-color: red; color: #fff; padding: 4px 8px; font-size: 14px; margin-left: 6px;"
              >
                -
              </el-button>
            </el-col>
          </el-row>
        </div>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit">确定</el-button>
      </el-form-item>
      <el-form-item label="转换结果">
<!--        <el-input v-model="result" placeholder="转换结果" :rows="10" type="textarea"></el-input>-->
        <el-text line-clamp="2">
          <CodeEditor ref="codeEditRef" v-model="result" width="1000px" height="250px" language="json" />
        </el-text>
      </el-form-item>
    </el-form>
  </div>

</template>