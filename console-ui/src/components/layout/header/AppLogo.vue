<script setup lang="ts">
import {useGlobalStore} from "@/store/globaleStore.ts";
import {storeToRefs} from "pinia";

const globalStore = useGlobalStore();
const { appName, appCode } = storeToRefs(globalStore);
</script>
<template>
  <div class="app-logo">{{ appName }}</div>
</template>
<style lang="less" scoped>
.app-logo {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 26px;
  padding: 0 24px;
  width: 200px;
}
</style>
