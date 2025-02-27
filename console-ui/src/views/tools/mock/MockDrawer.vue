<script setup lang="ts">
import { computed, nextTick, reactive, ref } from 'vue';
import { FormInstance, FormRules } from 'element-plus';
import {  MockInfo } from '@/typings';
import { mockService } from '@/service';
import ResizableDrawer from "@/components/common/ResizableDrawer.vue";
import { Delete, Plus} from "@element-plus/icons-vue";
import CodeEditor from "@/components/common/CodeEditor.vue";

const mockDrawerVisible = ref(false);
const formRef = ref<FormInstance>();
const editItem = ref<Record<string, any>>();
const mockFormValue = reactive<MockInfo>(getDefaultData());

const mode = ref(""); // 可选值：'add' | 'edit' | 'view'

function getDefaultData() {
  return {
    id: null,
    name: '',
    url: '',
    remark: '',
    conditions: [{
      conditionType: 'else',
      conditionName: 'else分支',
      expression: 'true',
      response: {
        body: '{}',
        responseHeader: [{}]
      }
    }],
  };
}

const rules = reactive<FormRules>({
  name: [{ required: true, message: '请输入Mock名称', trigger: 'blur' }],
  url: [{ required: true, message: '请选输入Mock地址', trigger: 'blur' }],
});

const emit = defineEmits(['add', 'edit']);

function onCancel() {
  mockDrawerVisible.value = false;
}

async function onSubmit() {
  if (!formRef.value) return;
  const valid = await formRef.value?.validate(() => {});
  if (!valid) {
    return;
  }

  mockDrawerVisible.value = false;
  if (mockFormValue.id) {
    emit('edit', mockFormValue);
  } else {
    emit('add', mockFormValue);
  }
}

function open(type, item?: Record<string, any>) {
  mode.value = type;
  editItem.value = item;
  mockDrawerVisible.value = true;
  nextTick(async () => {
    formRef.value?.resetFields();
    if (type === 'edit' || type === 'view') {
      const res = await mockService.get(item.id);
      if (res.success) {
        mockFormValue.id = res.result.id;
        mockFormValue.name = res.result.name;
        mockFormValue.url = res.result.url;
        mockFormValue.remark = res.result.remark;
        mockFormValue.conditions = res.result.conditions;
      }
    } else {
      Object.assign(mockFormValue, getDefaultData());
    }
  });
}

const title = computed(() => {
  return mode.value === "add"
      ? "新增 Mock"
      : mode.value === "edit"
          ? "编辑 Mock"
          : "查看 Mock";
});

function addCondition() {
  mockFormValue.conditions.push({
    conditionType: 'if',
    response: {
      body: '',
      responseHeader: [{}]
    }
  })

  // 确保else在最后
  const elseItemIndex = mockFormValue.conditions?.findIndex(item => item.conditionType === 'else');
  const elseItem = mockFormValue.conditions?.[elseItemIndex];
  if (elseItemIndex > -1 && elseItemIndex !== mockFormValue.conditions.length - 1) {
    mockFormValue.conditions.splice(elseItemIndex, 1);
    mockFormValue.conditions.push(elseItem);
  }
}

function removeCondition(rootIndex) {
  mockFormValue.conditions.splice(rootIndex, 1);
}

function addHeader(index) {
  mockFormValue.conditions[index].response.responseHeader.push({ key: '', value: '' })
}

function removeHeader(index, headerIndex) {
  mockFormValue.conditions[index].response.responseHeader.splice(headerIndex, 1);
}

defineExpose({ open });
</script>

<template>
  <ResizableDrawer v-model="mockDrawerVisible" :size="600" :title="title" drawer-key="FLOW_DEFINE" destroyOnClose>
    <div>
      <el-form :disabled="mode == 'view'" ref="formRef" label-position="top" :model="mockFormValue" :rules="rules">
        <el-form-item label="Mock名称" prop="name">
          <el-input v-model="mockFormValue.name" maxlength="30" />
        </el-form-item>
        <el-form-item label="Mock url"  prop="url">
          <el-input v-model="mockFormValue.url" placeholder=""></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input type="textarea" v-model="mockFormValue.remark" maxlength="120" />
        </el-form-item>
        <el-form-item label="触发条件">
          <div class="condition-list">
            <div class="condition-item" v-for="(item, indexRoot) in mockFormValue.conditions" :key="indexRoot">
              <el-form-item required label="条件名称">
                <el-input v-model="item.conditionName" placeholder=""></el-input>
              </el-form-item>
              <el-form-item  v-if="item.conditionType === 'else'" label="默认else表达式">
                <el-input :readonly="true" v-model="item.expression" maxlength="30" />
              </el-form-item>
              <el-form-item required v-else label="条件表达式">
                <el-input v-model="item.expression" maxlength="30" />
              </el-form-item>
              <el-tabs model-value="body">
                <el-tab-pane label="Body" name="body">
                  <el-text line-clamp="2">
                    <CodeEditor ref="codeEditRef" v-model="item.response.body" width="800px" height="100px" language="json" />
                  </el-text>
                </el-tab-pane>
                <el-tab-pane label="Header" name="responseHeader">
                  <el-form-item>
                    <div  :key="index" class="parameter-item" style="width: 100%">
                      <el-row v-for="(param, index) in item.response.responseHeader" :gutter="10" align="middle">
                        <!-- Key 输入框 -->
                        <el-col :span="4">
                          <el-input v-model="param.key" placeholder="请输入参数的key" />
                        </el-col>

                        <!-- Value 输入框 -->
                        <el-col :span="16" >
                          <el-input v-model="param.value" placeholder="请输入参数的值" />
                        </el-col>

                        <!-- 操作按钮 -->
                        <el-col :span="4" class="button-group" style="display: flex; align-items: center;">
                          <!-- 加号按钮 -->
                          <el-button
                              size="mini"
                              @click="addHeader(indexRoot)"
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
                              @click="removeHeader(indexRoot, index)"
                              v-if="item.response.responseHeader.length > 1"
                              class="remove-btn"
                              style="background-color: red; border-color: red; color: #fff; padding: 4px 8px; font-size: 14px; margin-left: 6px;"
                          >
                            -
                          </el-button>
                        </el-col>
                      </el-row>
                    </div>
                  </el-form-item>
                </el-tab-pane>
              </el-tabs>
              <el-button v-if="item.conditionType === 'if'" link :icon="Delete" @click="removeCondition(index)" title="删除"></el-button>
            </div>
            <div class="condition-add">
              <el-button link :icon="Plus" @click="addCondition" title="新增分支"></el-button>
            </div>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button v-if="mode !== 'view'" type="primary" @click="onSubmit">确定</el-button>
          <el-button v-if="mode !== 'view'" @click="onCancel">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </ResizableDrawer>
</template>

<style scoped>
.condition-list {
  width: 100%;
  .condition-item {
    padding: 5px 10px;
    border: 1px solid #ccc;
    border-radius: 4px;
    margin-bottom: 10px;
  }
}
</style>
