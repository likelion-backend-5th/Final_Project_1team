import React from 'react';
import { Box, Button } from '@mui/material';

type ReportStatusButtonsProps = {
  updateReportStatus: (status: 'IN_REVIEW' | 'RESOLVED' | 'DISMISSED') => void;
  deleteReport: () => void;
};

const ReportStatusButtons: React.FC<ReportStatusButtonsProps> = ({
  updateReportStatus,
  deleteReport,
}) => {
  return (
    <Box sx={{ mt: 2 }}>
      <Button
        variant="contained"
        onClick={() => updateReportStatus('IN_REVIEW')}
        sx={{ mr: 1 }}>
        검토중
      </Button>
      <Button
        variant="contained"
        onClick={() => updateReportStatus('RESOLVED')}
        sx={{ mr: 1 }}>
        해결됨
      </Button>
      <Button
        variant="contained"
        onClick={() => updateReportStatus('DISMISSED')}
        sx={{ mr: 1 }}>
        거절됨
      </Button>
      <Button variant="outlined" color="error" onClick={deleteReport}>
        신고 삭제
      </Button>
    </Box>
  );
};

export default ReportStatusButtons;
