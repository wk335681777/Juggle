import { createRouter, createWebHashHistory } from 'vue-router';
import LayoutView from '../views/LayoutView.vue';
import HomeLayoutView from '../views/HomeLayoutView.vue';
import LoginView from '../views/LoginView.vue';
import NotFound from '../views/NotFound.vue';
import { FlowRoutes } from '../views/flow';
import { CommonRoutes } from '../views/common';
import ObjectList from '@/views/object/ObjectList.vue';
import App from '@/views/app/App.vue';
import { SystemRoutes } from '@/views/system';
import { SuiteRoutes } from '@/views/suite';
import { ToolRoutes } from '@/views/tools';
import { MonitorRoutes } from '@/views/monitor';
import FlowDesign from "@/views/flow/FlowDesign.vue";
import FlowDesignView from "@/views/flow/FlowDesignView.vue";

import {MarketRoutes} from "@/views/market";

const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: LoginView,
      meta: { name: '登录' },
    },
    {
      path: '/design/:flowDefinitionId/:flowKey',
      name: 'flow-design',
      component: FlowDesign,
      meta: { name: '流程设计' },
    },
    {
      path: '/design/view/:flowVersionId/:flowKey',
      name: 'flow-design-view',
      component: FlowDesignView,
      meta: { name: '流程设计查看' },
    },
    {
      path: '/',
      name: 'index',
      component: HomeLayoutView,
      redirect: () => ({ name: 'app' }),
      meta: { name: '首页' },
      children: [
        {
          path: 'app',
          name: 'app',
          component: App,
          meta: { name: 'app' },
        },
      ]
    },
    {
      path: '/main/:appCode',
      name: 'main',
      component: LayoutView,
      redirect: () => ({ name: 'flow-define' }),
      meta: { name: '首页' },
      children: [
        // {
        //   path: '',
        //   name: 'home',
        //   component: HomeView,
        //   meta: { name: '首页' },
        // },
        ...CommonRoutes,
        ...FlowRoutes,
        ...SuiteRoutes,
        ...ToolRoutes,
        ...MonitorRoutes,
        // {
        //   path: 'app',
        //   name: 'app',
        //   component: App,
        //   meta: { name: 'app' },
        // },
        {
          path: 'object/list',
          name: 'object-list',
          component: ObjectList,
          meta: { name: '对象' },
        },
        ...MarketRoutes,
        ...SystemRoutes,
        // {
        //   path: '/main/:appCode/:pathMatch(.*)*',
        //   name: 'notfound',
        //   component: NotFound,
        //   meta: { name: '页面不存在' },
        // },
        {
          path: '/:pathMatch(.*)*',
          name: 'notfound',
          component: NotFound,
          meta: { name: '页面不存在' },
          beforeEnter: (to, from, next) => {
            debugger
            next({ path: '/' });
          },
        },
      ],
    },
  ],
});

export default router;
