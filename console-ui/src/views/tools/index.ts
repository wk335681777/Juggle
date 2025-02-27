import RouterNest from '@/views/RouterNest.vue';
import FreemarkerTool from "@/views/tools/FreemarkerTool.vue";
import Mock from "@/views/tools/Mock.vue";


export const ToolRoutes = [
    {
        path: 'tool',
        name: 'tool',
        component: RouterNest,
        meta: { name: '工具' },
        children: [
            {
                path: 'freemarker',
                name: 'freemarker',
                component: FreemarkerTool,
                meta: { name: 'freemarker工具' },
            },
            {
                path: 'mock',
                name: 'mock',
                component: Mock,
                meta: { name: '高级Mock' },
                props: (route) => ({ appCode: route.params.appCode }),
            },
        ],
    },
];