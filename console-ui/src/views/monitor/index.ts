import RouterNest from '@/views/RouterNest.vue';
import Flow from "@/views/monitor/FlowMonitor.vue";


export const MonitorRoutes = [
    {
        path: 'monitor',
        name: 'monitor',
        component: RouterNest,
        meta: { name: '监控' },
        children: [
            {
                path: 'flowMonitor',
                name: 'flowMonitor',
                component: Flow,
                meta: { name: '流程监控' },
                props: (route) => ({ appCode: route.params.appCode }),
            },
        ],
    },
];