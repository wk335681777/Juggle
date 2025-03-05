<script lang="ts" setup>
import { useRouter } from 'vue-router';
import {appService, flowDefineService, flowVersionService} from '@/service';
import { reactive, ref } from 'vue';
import {ElMessage} from "element-plus";
import {useGlobalStore} from "@/store/globaleStore.ts"

const globalStore = useGlobalStore();
const router = useRouter();

const appFormVisible = ref(false);
const isSubmitting = ref(false);  // 控制按钮的加载状态
let appForm = ref({
  appCode: '',
  appName: '',
  remark: '',
});

const apps = ref<Record<string, any>[]>([]);

function goToApp (app) {
  globalStore.setAppCode(app.appCode);
  globalStore.setAppName(app.appName);

  router.push({
    name: 'main',
    params: {
      appCode: app.appCode,
    },
  });
}

async function queryApps() {
  const res = await appService.list({});
  if (res.success) {
    apps.value = res.result;
  } else {
    ElMessage({ type: 'error', message: res.errorMsg });
  }
}

function openAddDialog(row: any) {
  appFormVisible.value = true;
  isSubmitting.value = false;
}

// 打开对话框时重置表单数据
const resetForm = () => {
  appForm.value = {
    appName: '',
    appCode: '',
    remark: '',
  };
};

async function onSubmit() {
  isSubmitting.value = true;
  await addApp(appForm.appName, appForm.appCode, appForm.remark);
  isSubmitting.value = false;
}

async function addApp(appName: string, appCode: string, remark: string) {
  const res = await appService.add({
    appName: appName,
    appCode: appCode,
    remark: remark,
  });
  if (res.success) {
    ElMessage({ type: 'success', message: '新增成功' });
    appFormVisible.value = false;
    await queryApps();
  } else {
    ElMessage({ type: 'error', message: res.errorMsg });
  }
}

queryApps();
</script>

<template>
  <div class="app-container">
    <!--    <h1>应用列表</h1>-->
    <div class="apps-grid">
      <div
          v-for="app in apps"
          :key="app.id"
          class="list-card" @click.prevent="goToApp(app)"
      >
        <div class="app-icon">
          <!-- 这里可以放置图标或图片 -->
          <span class="icon-placeholder">📱</span>
        </div>
        <div class="add-card-content">
          <h3>{{ app.appName }}</h3>
          <h3>{{ app.appCode }}</h3>
          <p class="description">{{ app.remark }}</p>
        </div>
      </div>

      <!-- 如果没有应用，显示添加应用的豆腐块 -->
      <div class="add-list-card" @click="openAddDialog">
        <div class="add-app-icon">+</div>
        <div class="add-app-text">点击添加应用</div>
      </div>

    </div>
  </div>

  <el-dialog v-model="appFormVisible" :show-close="false" title="新建应用" width="400" @open="resetForm">
    <el-form :model="appForm">
      <el-form-item label="应用名称" required>
        <el-input v-model="appForm.appName" />
      </el-form-item>
      <el-form-item label="应用编码" required>
        <el-input v-model="appForm.appCode" />
      </el-form-item>
      <el-form-item label="应用描述">
        <el-input type="textarea" v-model="appForm.remark" maxlength="120" />
      </el-form-item>
    </el-form>
    <template #footer>
        <span class="dialog-footer">
          <el-button @click="appFormVisible = false">取消</el-button>
          <el-button type="primary" @click="onSubmit" :loading="isSubmitting">提交</el-button>
        </span>
    </template>
  </el-dialog>
</template>

<style scoped>
.app-container {
  //max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

h1 {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
}

.apps-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
  padding: 0 20px;
}

.app-card {
  background: #1b1b1f;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  padding: 20px;
  transition: transform 0.2s;
  /* 固定豆腐块高度 */
  height: 200px;  /* 修改这个值调整高度 */
  display: flex;
  flex-direction: column;
}

.app-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
}

.app-icon {
  text-align: center;
  font-size: 40px;
  margin-bottom: 15px;
}

.app-content {
  flex: 1;
  overflow: hidden;
}

.app-content h3 {
  margin: 0 0 10px;
  //color: #2c3e50;
  color: #bbb;
  font-size: 1.1em;
}

.description {
  color: #666;
  font-size: 0.9em;
  line-height: 1.4;
  /* 处理长文本显示 */
  display: -webkit-box;
  -webkit-line-clamp: 3;  /* 控制显示行数 */
  -webkit-box-orient: vertical;
  overflow: hidden;
}

@media (max-width: 768px) {
  .apps-grid {
    grid-template-columns: repeat(auto-fill, minmax(100%, 1fr));
  }

  .app-card {
    height: 180px;  /* 移动端稍小的高度 */
  }
}


.add-app-card {
  background: #1b1b1f;
  //background-color: #f0f0f0;
  //background: #605d5d;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  padding: 20px;
  transition: transform 0.2s;
  /* 固定豆腐块高度 */
  height: 200px;  /* 修改这个值调整高度 */
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  cursor: pointer;
}

.add-app-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
}

.add-app-icon {
  font-size: 48px;
  font-weight: bold;
  color: #bbb;
}

.add-app-text {
  font-size: 16px;
  color: #bbb;
  margin-top: 10px;
}


.list-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background-color: #fff;
}
.card-content {
  display: flex;
  flex-direction: column;
}
.card-content h3 {
  margin: 0 0 8px;
  font-size: 18px;
}
.card-content p {
  margin: 0;
  font-size: 14px;
  color: #555;
}
.add-list-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  justify-content: center;
  align-items: center;
  text-align: center;
}
</style>