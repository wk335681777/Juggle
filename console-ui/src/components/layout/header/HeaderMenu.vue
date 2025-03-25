
<script lang="ts" setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
const router = useRouter();

function goToApp () {
  router.push({
    name: 'index',
  });
}

</script>
<template>
  <div class="header-menu">
    <ul>
      <li>
        <a @click="goToApp" rel="noopener noreferrer">我的应用</a>
      </li>
<!--      <li>-->
<!--        <a href="https://www.juggle.plus/docs/guide/introduce/" target="_blank" rel="noopener noreferrer">文档</a>-->
<!--      </li>-->
<!--      <li>-->
<!--        <a href="https://www.juggle.plus/blog" target="_blank" rel="noopener noreferrer">博客</a>-->
<!--      </li>-->
<!--      <li>-->
<!--        <a href="https://www.juggle.plus/docs/changelog" target="_blank" rel="noopener noreferrer">更新日志</a>-->
<!--      </li>-->
    </ul>
  </div>
</template>
<style lang="less" scoped>
.header-menu {
  float: left;
  line-height: 60px;
  li {
    display: inline-block;
    margin-right: 40px;
    a {
      text-decoration: none;
      cursor: pointer;
      font-size: 15px;
      color: #fff;
    }
  }
}
</style>
