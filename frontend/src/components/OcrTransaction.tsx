import React from 'react';
import {
  List,
  Datagrid,
  TextField,
  DateField,
  Show,
  SimpleShowLayout,
  ReferenceArrayField,
  NumberField,
  BooleanField,
} from 'react-admin';

// OCR Transaction 목록 컴포넌트
export const OcrTransactionList: React.FC = () => (
  <List>
    <Datagrid rowClick="show">
      <TextField source="id" label="ID" />
      <TextField source="status" label="상태" />
      <NumberField source="totalTasks" label="전체 작업 수" />
      <NumberField source="completedTasks" label="완료된 작업 수" />
      <NumberField source="failedTasks" label="실패한 작업 수" />
      <DateField source="createdAt" label="생성일" />
      <DateField source="updatedAt" label="수정일" />
      <DateField source="completedAt" label="완료일" />
    </Datagrid>
  </List>
);

// OCR Transaction 상세보기 컴포넌트
export const OcrTransactionShow: React.FC = () => (
  <Show>
    <SimpleShowLayout>
      <TextField source="id" label="ID" />
      <TextField source="status" label="상태" />
      <TextField source="requestId" label="요청 ID" />
      <TextField source="clientId" label="클라이언트 ID" />
      <NumberField source="totalTasks" label="전체 작업 수" />
      <NumberField source="completedTasks" label="완료된 작업 수" />
      <NumberField source="failedTasks" label="실패한 작업 수" />
      <NumberField source="pendingTasks" label="대기 중인 작업 수" />
      <TextField source="errorMessage" label="에러 메시지" />
      <BooleanField source="isCompleted" label="완료여부" />
      <DateField source="createdAt" label="생성일" />
      <DateField source="updatedAt" label="수정일" />
      <DateField source="completedAt" label="완료일" />
      
      <ReferenceArrayField source="tasks" reference="ocr-tasks" label="작업 목록">
        <Datagrid>
          <TextField source="fileName" label="파일명" />
          <TextField source="status" label="상태" />
        </Datagrid>
      </ReferenceArrayField>
    </SimpleShowLayout>
  </Show>
); 