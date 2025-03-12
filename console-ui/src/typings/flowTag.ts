export interface TagInfo {
    id: number;
    name: string;
    code: string;
    appCode: string;
    parentCode: string;
    path: string;
}

export interface TagTreeInfo {
    id: number;
    name: string;
    code: string;
    appCode: string;
    parentCode: string;
    path: string;
    children?: TagTreeInfo[];
}