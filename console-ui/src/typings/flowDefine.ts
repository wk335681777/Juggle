import { InputParams, OutputParams } from '@/typings/parameter.ts';

export interface FlowDefineInfo {
  id: number | null;
  flowKey: string;
  flowName: string;
  flowType: string;
  flowContent?: string;
  remark: string;
  debugUri: string;
  flowInputParams: InputParams[];
  flowOutputParams: OutputParams[];
}
