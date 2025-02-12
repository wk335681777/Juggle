import { defineStore } from 'pinia'
import { ref } from 'vue';

export const useGlobalStore = defineStore('globalStore', () => {
    const appCode = ref<number | null>(Number(localStorage.getItem('selectedAppCode')) || null);

    // 保存 appCode 到 localStorage
    const setAppCode = (appCodeStr: string) => {
        appCode.value = appCodeStr;
        localStorage.setItem('selectedAppCode', appCodeStr);
    };

    return { appCode, setAppCode };
})