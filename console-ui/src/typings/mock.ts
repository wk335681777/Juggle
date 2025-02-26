export interface MockInfo {
    id: number;
    name: string;
    url: string;
    appCode: string;
    remark?: string;
    conditions: MockCondition[];
}

export interface MockCondition {
    conditionName: string;
    expression: string;
    conditionType: string;
    response: MockResponse;
}

export interface MockResponse {
    responseType: string;
    body: string;
    responseHeader?: ResponseHeader[]
}

export interface ResponseHeader {
    key: string;
    value: string;
}