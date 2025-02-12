import { defineStore } from 'pinia'
import { ref } from 'vue';

export const useGlobalStore = defineStore('globalStore', () => {
    const appCode = ref<string | null>(String(sessionStorage.getItem('selectedAppCode') || ''));
    const appName = ref<string | null>(String(sessionStorage.getItem('selectedAppName') || ''));

    // 保存 appCode 到 localStorage
    const setAppCode = (appCodeStr: string) => {
        appCode.value = appCodeStr;
        sessionStorage.setItem('selectedAppCode', appCodeStr);
    };

    const setAppName = (appNameStr: string) => {
        appName.value = appNameStr;
        sessionStorage.setItem('selectedAppName', appNameStr);
    };

    const reset = () => {
        appCode.value = '';
        appName.value = '';
        sessionStorage.removeItem('selectedAppCode');
        sessionStorage.removeItem('selectedAppName');
    }

    return { appCode, appName, setAppCode, setAppName, reset };
})