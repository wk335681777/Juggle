import FlowDefineList from './FlowDefineList.vue';
import FlowList from './FlowList.vue';
import FlowVersionList from './FlowVersionList.vue';
import FlowDebug from './FlowDebug.vue';
import FlowDebug2 from './FlowDebug2.vue';
import RouterNest from '@/views/RouterNest.vue';
import FlowTag from './FlowTag.vue';

export const FlowRoutes = [
  {
    path: 'flow',
    name: 'flow',
    component: RouterNest,
    redirect: () => ({ name: 'flow-define' }),
    meta: { name: '流程' },
    children: [
      {
        path: 'define',
        name: 'flow-define',
        component: FlowDefineList,
        meta: { name: '流程定义' },
        props: (route) => ({ appCode: route.params.appCode }),
      },
      {
        path: 'list',
        name: 'flow-api',
        component: FlowList,
        meta: { name: '流程列表' },
        props: (route) => ({ appCode: route.params.appCode }),
      },
      {
        path: 'version/:flowId',
        name: 'flow-version',
        component: FlowVersionList,
        meta: { name: '流程版本列表' },
      },
      {
        path: 'debug/:flowDefinitionId/:flowKey',
        name: 'flow-debug',
        component: FlowDebug2,
        meta: { name: '流程调试' },
        beforeRouteLeave(to, from, next) {
        },
      },
      {
        path: 'tag',
        name: 'flow-tag',
        component: FlowTag,
        meta: { name: '流程标签' },
        props: (route) => ({ appCode: route.params.appCode }),
      },
    ],
  },
];
