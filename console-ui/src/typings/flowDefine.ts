import { InputParams, OutputParams } from '@/typings/parameter.ts';

export interface FlowDefineInfo {
  appCode: string;
  id: number | null;
  flowKey: string;
  flowName: string;
  flowType: string;
  flowContent?: string;
  headers:[];
  body:string;
  remark: string;
  debugUri: string;
  flowInputParams: InputParams[];
  flowOutputParams: OutputParams[];
}
