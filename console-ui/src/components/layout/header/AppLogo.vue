<script setup lang="ts">
import { useRouter } from 'vue-router';
const $router = useRouter();
</script>
<template>
  <div class="app-logo">{{ $router.currentRoute.value.params.appCode }}</div>
</template>
<style lang="less" scoped>
.app-logo {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 26px;
  padding: 0 24px;
  //width: 200px;
}
</style>
