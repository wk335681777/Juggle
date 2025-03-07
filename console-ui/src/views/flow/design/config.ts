import { ElementType } from '@/views/flow/design/types.ts';

export const nodeMap = {
  [ElementType.METHOD]: {
    nodeName: '方法节点',
    nodeHeaderColor: '#6bcb7b',
  },
  [ElementType.CONDITION]: {
    nodeName: '判断节点',
    nodeHeaderColor: '#e5cf7a',
  },
  [ElementType.ASSIGN]: {
    nodeName: '赋值节点',
    nodeHeaderColor: '#e0b290',
  },
  [ElementType.CODE]: {
    nodeName: '代码节点',
    nodeHeaderColor: '#b471e7',
  },
  [ElementType.MYSQL]: {
    nodeName: 'MySql节点',
    nodeHeaderColor: '#48a6bb',
  },
  [ElementType.AI]: {
    nodeName: 'AI节点',
    nodeHeaderColor: '#640505',
  },
  [ElementType.LOG]: {
    nodeName: 'Log',
    nodeHeaderColor: '#b471e7',
  },
  [ElementType.NETTY_HTTP]: {
    nodeName: 'Netty Http',
    nodeHeaderColor: '#b471e7',
  },
  [ElementType.CONVERT_BODY_TO]: {
    nodeName: 'Convert Body To',
    nodeHeaderColor: '#b471e7',
  },
  [ElementType.DIRECT]: {
    nodeName: 'Direct',
    nodeHeaderColor: '#b471e7',
  },
  [ElementType.DYNAMIC_ROUTE]: {
    nodeName: 'Dynamic Route',
    nodeHeaderColor: '#b471e7',
  },
  [ElementType.HTTP]: {
    nodeName: 'Http',
    nodeHeaderColor: '#b471e7',
  },
  [ElementType.MARSHAL]: {
    nodeName: 'Marshal',
    nodeHeaderColor: '#b471e7',
  },
  [ElementType.UNMARSHAL]: {
    nodeName: 'Unmarshal',
    nodeHeaderColor: '#b471e7',
  },
  [ElementType.SET_HEADER]: {
    nodeName: 'Set Header',
    nodeHeaderColor: '#b471e7',
  },
  [ElementType.TRANSFORM]: {
    nodeName: 'Transform',
    nodeHeaderColor: '#b471e7',
  },
  [ElementType.RECIPIENT_LIST]: {
    nodeName: 'RecipientList',
    nodeHeaderColor: '#b471e7',
  },
  [ElementType.WEBSERVICE]: {
    nodeName: 'WebService',
    nodeHeaderColor: '#b471e7',
  },
  //加解密
  [ElementType.EN_AND_DE]: {
    nodeName: '加解密',
    nodeHeaderColor: '#b471e7',

  },
  //英大国密
  [ElementType.YDA_SM4]: {
    nodeName: '英大SM4国密加解密',
    nodeHeaderColor: '#b471e7',

  },
  [ElementType.FEIGN]: {
    nodeName: 'Feign',
    nodeHeaderColor: '#b471e7',

  },

  [ElementType.SCRIPT]: {
    nodeName: '脚本节点',
    nodeHeaderColor: '#b471e7',
  },

  [ElementType.SET_HEADERS]: {
    nodeName: 'Set Headers',
    nodeHeaderColor: '#b471e7',
  },
}
