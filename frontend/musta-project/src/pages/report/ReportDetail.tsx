import React, { useEffect, useState } from 'react';
import { Report } from '../../types/report';
import { useParams } from 'react-router-dom';
import { Container, Typography, Box, Grid, styled } from '@mui/material';
import ReportTable from '../../components/report/ReportTable';
import ReportStatusButtons from '../../components/report/ReportStatusButtons';
import BackButton from '../../components/BackButton';

const StyledTypography = styled(Typography)({
  fontWeight: 700,
  marginTop: '10px',
  color: 'darkblue',
});

const ReportDetail: React.FC = () => {
  const { reportApiId } = useParams<{ reportApiId: string }>();
  const [report, setReport] = useState<Report | null>(null);

  const fetchReportDetail = async () => {
    const response = await fetch(
      `${import.meta.env.VITE_API}api/reports/${reportApiId}`
    );
    if (response.ok) {
      const data = await response.json();
      setReport(data);
    } else {
      // 오류 처리
      console.error('Failed to fetch report details');
    }
  };

  useEffect(() => {
    fetchReportDetail();
  }, [reportApiId]);

  const updateReportStatus = async (newStatus: string) => {
    const response = await fetch(
      `${import.meta.env.VITE_API}api/reports/${reportApiId}`,
      {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ status: newStatus }),
      }
    );

    if (response.ok) {
      alert('신고 상태가 업데이트되었습니다.');
      fetchReportDetail(); // 상태 업데이트 후 다시 데이터를 불러옴
    } else {
      alert('신고 상태 업데이트에 실패하였습니다.');
    }
  };

  const deleteReport = async () => {
    const response = await fetch(
      `${import.meta.env.VITE_API}api/reports/${reportApiId}`,
      {
        method: 'DELETE',
      }
    );

    if (response.ok) {
      alert('신고가 삭제되었습니다.');
    } else {
      alert('신고 삭제에 실패하였습니다.');
    }
  };

  if (!report) return <Typography>Loading...</Typography>;

  return (
    <Container>
      <Box sx={{ my: 4 }}>
        <Grid
          container
          justifyContent="space-between"
          alignItems="center"
          spacing={2}>
          <Grid item>
            <BackButton />
          </Grid>
          <Grid item>
            <StyledTypography variant="h6" color="black" gutterBottom>
              신고 상세 조회
            </StyledTypography>
          </Grid>
        </Grid>
        <ReportTable report={report} />
        <ReportStatusButtons
          updateReportStatus={updateReportStatus}
          deleteReport={deleteReport}
        />
      </Box>
    </Container>
  );
};

export default ReportDetail;
