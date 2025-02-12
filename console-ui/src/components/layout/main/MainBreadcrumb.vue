<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
const $router = useRouter();
const matched = ref($router.currentRoute.value.matched);
$router.afterEach(to => {
  matched.value = to.matched;
});

const getBreadcrumbLink = (item) => {
  // 如果路由路径中有动态参数 :appCode，生成带参数的完整路径
  if (item.path.includes(':')) {
    const pathWithParams = item.path.replace(':appCode', $router.currentRoute.value.params.appCode);
    return { path: pathWithParams };
  }
  return { path: item.path };  // 否则返回静态路径
};
</script>
<template>
  <el-breadcrumb separator="/">
    <template v-for="item in matched" :key="item.path">
      <el-breadcrumb-item :to="getBreadcrumbLink(item)" v-if="item.name !== 'home'">
        {{ item.meta.name }}
      </el-breadcrumb-item>
    </template>
  </el-breadcrumb>
</template>
