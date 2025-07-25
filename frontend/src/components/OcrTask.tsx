import React from 'react';
import {
  List,
  Datagrid,
  TextField,
  DateField,
  Show,
  SimpleShowLayout,
  Edit,
  SimpleForm,
  TextInput,
  SelectInput,
  ReferenceField,
  NumberField,
  BooleanField,
  ChipField,
} from 'react-admin';

// OCR Task 목록 컴포넌트
export const OcrTaskList: React.FC = () => (
  <List>
    <Datagrid rowClick="show">
      <TextField source="id" label="ID" />
      <TextField source="fileName" label="파일명" />
      <TextField source="documentType" label="문서 타입" />
      <TextField source="status" label="상태" />
      <DateField source="createdAt" label="생성일" />
      <DateField source="updatedAt" label="수정일" />
      <BooleanField source="isCompleted" label="완료여부" />
    </Datagrid>
  </List>
);

// OCR Task 상세보기 컴포넌트
export const OcrTaskShow: React.FC = () => (
  <Show>
    <SimpleShowLayout>
      <TextField source="id" label="ID" />
      <TextField source="fileName" label="파일명" />
      <TextField source="filePath" label="파일 경로" />
      <TextField source="documentType" label="문서 타입" />
      <TextField source="status" label="상태" />
      <TextField source="errorMessage" label="에러 메시지" />
      <NumberField source="priority" label="우선순위" />
      <BooleanField source="isCompleted" label="완료여부" />
      <DateField source="createdAt" label="생성일" />
      <DateField source="updatedAt" label="수정일" />
      <DateField source="completedAt" label="완료일" />
      
      <ReferenceField source="transactionId" reference="ocr-transactions" label="거래 ID">
        <TextField source="id" />
      </ReferenceField>
    </SimpleShowLayout>
  </Show>
);

// OCR Task 편집 컴포넌트
export const OcrTaskEdit: React.FC = () => (
  <Edit>
    <SimpleForm>
      <TextInput source="fileName" label="파일명" disabled />
      <TextInput source="filePath" label="파일 경로" disabled />
      <SelectInput 
        source="documentType" 
        label="문서 타입"
        choices={[
          { id: 'INVOICE', name: '인보이스' },
          { id: 'RECEIPT', name: '영수증' },
          { id: 'CONTRACT', name: '계약서' },
          { id: 'ID_CARD', name: '신분증' },
          { id: 'OTHER', name: '기타' },
        ]}
      />
      <SelectInput 
        source="status" 
        label="상태"
        choices={[
          { id: 'PENDING', name: '대기중' },
          { id: 'PROCESSING', name: '처리중' },
          { id: 'COMPLETED', name: '완료' },
          { id: 'FAILED', name: '실패' },
          { id: 'CANCELLED', name: '취소' },
        ]}
      />
      <TextInput source="errorMessage" label="에러 메시지" multiline rows={3} />
      <TextInput source="priority" label="우선순위" type="number" />
    </SimpleForm>
  </Edit>
); 