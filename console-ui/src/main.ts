import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import ElementPlus from 'element-plus';
import './assets/base.css';
import 'element-plus/dist/index.css';
import { userService } from './service';
import { createPinia } from 'pinia'

const pinia = createPinia()
const app = createApp(App);

app.use(pinia)
app.use(ElementPlus);
app.use(router);

app.config.errorHandler = (err, instance, info) => {
  console.error(err);
};

async function startup() {
  // 检查登录状态
  const isLogin = await userService.check();
  if (!isLogin) {
    await router.push({ name: 'login' });
  }
  app.mount('#app');
}

startup();
