import React, { useState, useEffect } from 'react';
import { Card, CardContent, CardHeader, Typography, Box } from '@mui/material';
import { 
  Description as TaskIcon, 
  Receipt as TransactionIcon,
  Timeline as StageIcon,
  CheckCircle as SuccessIcon,
  Error as ErrorIcon,
  Pending as PendingIcon
} from '@mui/icons-material';
import { useDataProvider } from 'react-admin';

interface Statistics {
  totalTasks: number;
  completedTasks: number;
  failedTasks: number;
  pendingTasks: number;
  totalTransactions: number;
  totalStages: number;
}

export const Dashboard: React.FC = () => {
  const dataProvider = useDataProvider();
  const [stats, setStats] = useState<Statistics>({
    totalTasks: 0,
    completedTasks: 0,
    failedTasks: 0,
    pendingTasks: 0,
    totalTransactions: 0,
    totalStages: 0,
  });

  useEffect(() => {
    const fetchStats = async () => {
      try {
        // 실제 API가 준비되면 이 부분을 수정
        // const statistics = await dataProvider.getList('statistics', {
        //   pagination: { page: 1, perPage: 1 },
        //   sort: { field: 'id', order: 'ASC' },
        //   filter: {},
        // });
        
        // 임시 데이터
        setStats({
          totalTasks: 1250,
          completedTasks: 980,
          failedTasks: 45,
          pendingTasks: 225,
          totalTransactions: 1250,
          totalStages: 3750,
        });
      } catch (error) {
        console.error('Failed to fetch statistics:', error);
      }
    };

    fetchStats();
  }, [dataProvider]);

  const StatCard = ({ title, value, icon, color }: { 
    title: string; 
    value: number; 
    icon: React.ReactNode; 
    color: string; 
  }) => (
    <Card sx={{ height: '100%' }}>
      <CardContent>
        <Box display="flex" alignItems="center" justifyContent="space-between">
          <Box>
            <Typography variant="h4" component="div" color={color}>
              {value.toLocaleString()}
            </Typography>
            <Typography variant="body2" color="text.secondary">
              {title}
            </Typography>
          </Box>
          <Box color={color}>
            {icon}
          </Box>
        </Box>
      </CardContent>
    </Card>
  );

  return (
    <Box p={3}>
      <Typography variant="h4" gutterBottom>
        OCR Gateway 대시보드
      </Typography>
      
      <Box display="grid" gridTemplateColumns="repeat(auto-fit, minmax(250px, 1fr))" gap={3} sx={{ mb: 3 }}>
        <StatCard
          title="전체 작업"
          value={stats.totalTasks}
          icon={<TaskIcon sx={{ fontSize: 40 }} />}
          color="primary.main"
        />
        <StatCard
          title="완료된 작업"
          value={stats.completedTasks}
          icon={<SuccessIcon sx={{ fontSize: 40 }} />}
          color="success.main"
        />
        <StatCard
          title="실패한 작업"
          value={stats.failedTasks}
          icon={<ErrorIcon sx={{ fontSize: 40 }} />}
          color="error.main"
        />
        <StatCard
          title="대기 중인 작업"
          value={stats.pendingTasks}
          icon={<PendingIcon sx={{ fontSize: 40 }} />}
          color="warning.main"
        />
      </Box>

      <Box display="grid" gridTemplateColumns="repeat(auto-fit, minmax(300px, 1fr))" gap={3}>
        <StatCard
          title="전체 거래"
          value={stats.totalTransactions}
          icon={<TransactionIcon sx={{ fontSize: 40 }} />}
          color="info.main"
        />
        <StatCard
          title="처리 단계"
          value={stats.totalStages}
          icon={<StageIcon sx={{ fontSize: 40 }} />}
          color="secondary.main"
        />
      </Box>
    </Box>
  );
}; 