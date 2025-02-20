import ConditionForm from './ConditionForm.vue';
import MethodForm from './MethodForm.vue';
import CodeForm from './CodeForm.vue';
import MySqlForm from './MySqlForm.vue';
import { ElementType } from '../../types';
import AssignForm from "./AssignForm.vue";
import LogForm from "./LogForm.vue";
import NettyHttpForm from "./NettyHttpForm.vue";
import ConvertBodyToForm from "./ConvertBodyToForm.vue";
import DirectForm from "./DirectForm.vue";
import DynamicRouteForm from "./DynamicRouteForm.vue";
import HttpForm from "./HttpForm.vue";
import MarshalForm from "./MarshalForm.vue";
import UnMarshalForm from "./UnMarshalForm.vue";
import SetHeaderForm from "./SetHeaderForm.vue";
import TransformForm from "./TransformForm.vue";
import RecipientListForm from "./RecipientListForm.vue";
import WebServiceForm from "./WebServiceForm.vue";
import EnAndDeForm from "@/views/flow/design/components/node-form/EnAndDeForm.vue";
import YingDaEncryptForm from "@/views/flow/design/components/node-form/YingDaEncryptForm.vue";
import FeignForm from "@/views/flow/design/components/node-form/FeignForm.vue";


const nodeFormMap = {
  [ElementType.CONDITION]: ConditionForm,
  [ElementType.METHOD]: MethodForm,
  [ElementType.CODE]: CodeForm,
  [ElementType.ASSIGN]: AssignForm,
  [ElementType.MYSQL]: MySqlForm,
  [ElementType.LOG]: LogForm,
  [ElementType.NETTY_HTTP]: NettyHttpForm,
  [ElementType.CONVERT_BODY_TO]: ConvertBodyToForm,
  [ElementType.DIRECT]: DirectForm,
  [ElementType.DYNAMIC_ROUTE]: DynamicRouteForm,
  [ElementType.HTTP]: HttpForm,
  [ElementType.MARSHAL]: MarshalForm,
  [ElementType.UNMARSHAL]: UnMarshalForm,
  [ElementType.SET_HEADER]: SetHeaderForm,
  [ElementType.TRANSFORM]: TransformForm,
  [ElementType.RECIPIENT_LIST]: RecipientListForm,
  [ElementType.WEBSERVICE]: WebServiceForm,
  [ElementType.EN_AND_DE]: EnAndDeForm,//加解密
  [ElementType.YDA_SM4]: YingDaEncryptForm,//英大国密
  [ElementType.FEIGN]: FeignForm,

};

export function getNodeForm(type: ElementType) {
  return nodeFormMap[type as keyof typeof nodeFormMap];
}
