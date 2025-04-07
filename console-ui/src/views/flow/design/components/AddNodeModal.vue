<script setup lang="ts">
import { ref } from 'vue';
import { ElementType } from '../types';
import IconMethod from '@/components/icons/IconMethod.vue';
import IconCondition from '@/components/icons/IconCondition.vue';
import IconCode from '@/components/icons/IconCode.vue';
import IconMysql from '@/components/icons/IconMysql.vue';
import IconAssign from '@/components/icons/IconAssign.vue';
const visible = ref(false);
type NodeInfo = { name: string; elementType: ElementType };
let openParams: { afterSelect: (info: NodeInfo) => void };
function open(params: typeof openParams) {
  visible.value = true;
  openParams = params;
}

// const containerRef = ref<HTMLElement | null>(null);

const handleClick = (e: MouseEvent) => {
  e.preventDefault();
};

const flowNodes = [
  // {
  //   name: '方法节点',
  //   type: ElementType.METHOD,
  //   icon: IconMethod,
  // },
  {
    name: '判断节点',
    type: ElementType.CONDITION,
    icon: IconCondition,
  },
  // {
  //   name: '赋值节点',
  //   type: ElementType.ASSIGN,
  //   icon: IconAssign,
  // },
  // {
  //   name: '代码节点',
  //   type: ElementType.CODE,
  //   icon: IconCode,
  // },
  {
    name: 'DynamicRoute',
    type: ElementType.DYNAMIC_ROUTE,
    icon: IconCode,
  },
  {
    name: 'Marshal',
    type: ElementType.MARSHAL,
    icon: IconCode,
  },
  {
    name: 'UnMarshal',
    type: ElementType.UNMARSHAL,
    icon: IconCode,
  },
  {
    name: 'Set Header',
    type: ElementType.SET_HEADER,
    icon: IconCode,
  },
  {
    name: 'Transform',
    type: ElementType.TRANSFORM,
    icon: IconCode,
  },
  {
    name: 'Log',
    type: ElementType.LOG,
    icon: IconCode,
  },
  {
    name: 'ConvertBodyTo',
    type: ElementType.CONVERT_BODY_TO,
    icon: IconCode,
  },
  {
    name: 'RecipientList',
    type: ElementType.RECIPIENT_LIST,
    icon: IconCode,
  },

  {
    name: 'Redirect',
    type: ElementType.REDIRECT,
    icon: IconCode,
  },

  {
    name: 'WebService',
    type: ElementType.WEBSERVICE,
    icon: IconCode,
  },
    //加解密
  {
    name:'加解密',
    type: ElementType.EN_AND_DE,
    icon: IconCode,
  },

  //英大国密
  {
    name:'英大SM4国密加解密',
    type: ElementType.YDA_SM4,
    icon: IconCode,
  },
  //Feign
  {
    name:'Feign',
    type: ElementType.FEIGN,
    icon: IconCode,
  },

  //脚本
  {
    name:'脚本节点',
    type: ElementType.SCRIPT,
    icon: IconCode,
  },

  {
    name:'SetHeaders',
    type: ElementType.SET_HEADERS,
    icon: IconCode,
  },

  {
    name:'SetBody',
    type: ElementType.SET_BODY,
    icon: IconCode,
  },

];

const flowDataNodes = [
  // {
  //   name: 'MySql节点',
  //   type: ElementType.MYSQL,
  //   icon: IconMysql,
  // },
  {
    name: 'netty-http',
    type: ElementType.NETTY_HTTP,
    icon: IconCode,
  },
  {
    name: 'http',
    type: ElementType.HTTP,
    icon: IconCode,
  },
  {
    name: 'direct',
    type: ElementType.DIRECT,
    icon: IconCode,
  },
];

const flowOtherNodes = [

];

function addNode(item: { name: string; type: ElementType }) {
  const info = {
    name: item.name,
    elementType: item.type,
  };
  if (typeof openParams.afterSelect === 'function') {
    openParams.afterSelect(info);
  }
  visible.value = false;
}

defineExpose({ open });
</script>

<template>
  <el-dialog v-model="visible" title="" class="design-add-node-modal" :width="500" :show-close="false" align-center>
    <el-anchor :offset="70" :container="containerRef" direction="horizontal" @click="handleClick">
      <el-anchor-link href="#baseNodes" title="处理器节点" />
      <el-anchor-link href="#dataNodes" title="组件节点" />
    </el-anchor>
    <el-row>
      <el-col>
        <div ref="containerRef" style="height: 300px; overflow-y: auto">
          <div id="baseNodes" class="node-types">
            <div class="node-type-name">处理器节点</div>
            <div class="node-type" v-for="item in flowNodes" :key="item.type" @click="addNode(item)">
              <span
                ><el-icon :size="25"><component :is="item.icon"></component></el-icon
              ></span>
              <span class="node-text">{{ item.name }}</span>
            </div>
          </div>

          <div id="dataNodes" class="node-types">
            <div class="node-type-name">组件节点</div>
            <div class="node-type" v-for="item in flowDataNodes" :key="item.type" @click="addNode(item)">
              <el-icon :size="25"><component :is="item.icon"></component></el-icon>
              <span class="node-text">{{ item.name }}</span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </el-dialog>
</template>

<style lang="less">
.design-add-node-modal {
  .el-dialog__header {
    padding: 0;
    width: 100%;
    position: absolute;
    z-index: 1;
  }
  .el-dialog__headerbtn {
    height: 40px;
    width: 40px;
    top: 0;
  }
  .el-dialog__body {
    padding: 0;
  }
  .el-anchor__link {
    font-size: 15px;
  }
  .node-types {
    padding: 8px 0;
    display: flex;
    flex-direction: column;
    align-items: flex-start;
  }
  .node-type-name {
    font-size: 18px;
    margin-bottom: 10px;
  }
  .node-type {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 448px;
    margin-bottom: 8px;
    font-size: 14px;
    cursor: pointer;
    border: 1px solid #f0f2f5;
  }
  .node-text {
    margin: 10px 5px;
  }
}
</style>
