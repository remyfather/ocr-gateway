import React from 'react';
import {
  List,
  Datagrid,
  TextField,
  DateField,
  Show,
  SimpleShowLayout,
  ReferenceField,
  NumberField,
  BooleanField,
} from 'react-admin';

// Processing Stage 목록 컴포넌트
export const ProcessingStageList: React.FC = () => (
  <List>
    <Datagrid rowClick="show">
      <TextField source="id" label="ID" />
      <TextField source="stageType" label="단계 타입" />
      <TextField source="status" label="상태" />
      <NumberField source="orderIndex" label="순서" />
      <TextField source="result" label="결과" />
      <DateField source="startedAt" label="시작일" />
      <DateField source="completedAt" label="완료일" />
      <BooleanField source="isCompleted" label="완료여부" />
    </Datagrid>
  </List>
);

// Processing Stage 상세보기 컴포넌트
export const ProcessingStageShow: React.FC = () => (
  <Show>
    <SimpleShowLayout>
      <TextField source="id" label="ID" />
      <TextField source="stageType" label="단계 타입" />
      <TextField source="status" label="상태" />
      <NumberField source="orderIndex" label="순서" />
      <TextField source="result" label="결과" />
      <TextField source="errorMessage" label="에러 메시지" />
      <TextField source="metadata" label="메타데이터" />
      <BooleanField source="isCompleted" label="완료여부" />
      <DateField source="createdAt" label="생성일" />
      <DateField source="updatedAt" label="수정일" />
      <DateField source="startedAt" label="시작일" />
      <DateField source="completedAt" label="완료일" />
      
      <ReferenceField source="taskId" reference="ocr-tasks" label="작업 ID">
        <TextField source="id" />
      </ReferenceField>
      
      <ReferenceField source="transactionId" reference="ocr-transactions" label="거래 ID">
        <TextField source="id" />
      </ReferenceField>
    </SimpleShowLayout>
  </Show>
); 