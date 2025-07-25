import React from 'react';
import { Admin, Resource } from 'react-admin';
import { dataProvider } from './dataProvider';
import { OcrTaskList, OcrTaskShow, OcrTaskEdit } from './components/OcrTask';
import { OcrTransactionList, OcrTransactionShow } from './components/OcrTransaction';
import { ProcessingStageList, ProcessingStageShow } from './components/ProcessingStage';
import { Dashboard } from './components/Dashboard';
import { Layout } from './components/Layout';
import { 
  Description as OcrTaskIcon, 
  Receipt as TransactionIcon,
  Timeline as StageIcon 
} from '@mui/icons-material';

const App: React.FC = () => (
  <Admin 
    dataProvider={dataProvider}
    dashboard={Dashboard}
    layout={Layout}
    title="OCR Gateway 관리도구"
  >
    <Resource 
      name="ocr-tasks" 
      list={OcrTaskList} 
      show={OcrTaskShow} 
      edit={OcrTaskEdit}
      icon={OcrTaskIcon}
      options={{ label: 'OCR 작업' }}
    />
    <Resource 
      name="ocr-transactions" 
      list={OcrTransactionList} 
      show={OcrTransactionShow}
      icon={TransactionIcon}
      options={{ label: 'OCR 거래' }}
    />
    <Resource 
      name="processing-stages" 
      list={ProcessingStageList} 
      show={ProcessingStageShow}
      icon={StageIcon}
      options={{ label: '처리 단계' }}
    />
  </Admin>
);

export default App;
