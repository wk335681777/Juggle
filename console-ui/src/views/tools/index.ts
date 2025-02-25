import RouterNest from '@/views/RouterNest.vue';
import FreemarkerTool from "@/views/tools/FreemarkerTool.vue";

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
        ],
    },
];