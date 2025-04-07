import {DataType} from "@/typings";

export type ConditionItem = {
  conditionName: string;
  conditionType: string;
  expression: string;
  outgoing: string;
  conditionExpressions: any[];
};

export type RawData = {
  key: string;
  name: string;
  outgoings: string[];
  incomings: string[];
  elementType: ElementType;
  conditions?: ConditionItem[];
  desc?: string;
  method?: MethodInfo;
};

export type MethodInfo = {
  methodCode: string;
  suiteCode: string;
  requestContentType: string;
  requestType: string;
  url: string;
  inputParamSchemas: any;
  headerFillRules: any;
  inputFillRules: any;
  outputFillRules: any;
};

export enum ElementType {
  START = 'START',
  METHOD = 'METHOD',
  CONDITION = 'CONDITION',
  CODE = 'CODE',
  ASSIGN = 'ASSIGN',
  MYSQL = 'MYSQL',
  NETTY_HTTP = 'NETTY_HTTP',
  CONVERT_BODY_TO = 'CONVERT_BODY_TO',
  DIRECT = 'DIRECT',
  DYNAMIC_ROUTE = 'DYNAMIC_ROUTE',
  HTTP = 'HTTP',
  MARSHAL = 'MARSHAL',
  UNMARSHAL = 'UNMARSHAL',
  SET_HEADER = 'SET_HEADER',
  TRANSFORM = 'TRANSFORM',
  RECIPIENT_LIST = 'RECIPIENT_LIST',
  REDIRECT='REDIRECT',
  WEBSERVICE = 'WEBSERVICE',
  EN_AND_DE = 'EN_AND_DE',//加解密
  YDA_SM4="YDA_SM4",//英大国密
  FEIGN="FEIGN",
  SCRIPT="SCRIPT",//脚本
  SET_HEADERS="SET_HEADERS",
  SET_BODY="SET_BODY",
  LOG = 'LOG',
  END = 'END',

  // 前端创建的
  BRANCH = 'BRANCH',
}

export type D3Element = d3.Selection<any, any, any, any>;

export type MyOptional<T, K extends keyof T> = Partial<Omit<T, K>> & Pick<T, K>;

export enum FlowVariableType {
  INPUT = 1,
  OUTPUT = 2,
  TEMP = 3,
}

export type FlowVariable = {
  dataType: DataType;
  envKey: string;
  envName: string;
  envType: FlowVariableType;
  id: number;
};

export type FlowData = {
  flowKey: string;
  flowName: string;
  flowType: string;
  flowContent: RawData[];
  flowInputParams: any[];
  flowOutputParams: any[];
  flowVariables: FlowVariable[];
  id: number;
  remark: string;
};
