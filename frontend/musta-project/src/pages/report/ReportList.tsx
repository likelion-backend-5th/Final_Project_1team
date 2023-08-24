import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Report } from '../../types/report';
import {
  Box,
  Typography,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  ListItemIcon,
  Grid,
  List,
  ListItem,
  Paper,
  styled,
} from '@mui/material';
import ReportIcon from '@mui/icons-material/Report';
import { statusToKorean } from '../../util/statusHelpers';

const StyledListItem = styled(ListItem)({
  margin: '7px 0',
  backgroundColor: '#ffffff',
  '&:hover': {
    backgroundColor: '#e0e0e0',
  },
});

const ReportList: React.FC = () => {
  const [reports, setReports] = useState<Report[]>([]);
  const [filterStatus, setFilterStatus] = useState<string>('ALL');

  useEffect(() => {
    let url = 'http://localhost:8080/api/reports';
    if (filterStatus !== 'ALL') {
      url += `?status=${filterStatus}`;
    }

    fetch(url)
      .then((response) => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then((data) => setReports(data))
      .catch((error) =>
        console.error('There was a problem with the fetch operation:', error)
      );
  }, [filterStatus]);

  return (
    <Box sx={{ my: 4, maxWidth: 752, margin: '0 auto' }}>
      <Typography variant="h6" color="black" gutterBottom>
        신고 목록 조회
      </Typography>

      <FormControl fullWidth variant="outlined" sx={{ marginBottom: 2 }}>
        <InputLabel>상태 필터</InputLabel>
        <Select
          value={filterStatus}
          onChange={(e) => setFilterStatus(e.target.value as string)}
          label="상태 필터">
          <MenuItem value="ALL">모두 보기</MenuItem>
          <MenuItem value="PENDING">대기중</MenuItem>
          <MenuItem value="IN_REVIEW">검토중</MenuItem>
          <MenuItem value="RESOLVED">해결됨</MenuItem>
          <MenuItem value="DISMISSED">거절됨</MenuItem>
        </Select>
      </FormControl>

      <Paper elevation={3} sx={{ p: 2 }}>
        <List>
          {reports.map((report) => (
            <Link
              key={report.apiId}
              to={`/report/${report.apiId}`}
              style={{ textDecoration: 'none', color: 'inherit' }}>
              <StyledListItem>
                <ListItemIcon>
                  <ReportIcon />
                </ListItemIcon>
                <Grid
                  container
                  alignItems="center"
                  justifyContent="space-between">
                  <Grid item>
                    <Typography variant="body1" color="black">
                      {report.reporterName}
                    </Typography>
                  </Grid>
                  <Grid item>
                    <Typography variant="body2" color="black">
                      {statusToKorean(report.status)}
                    </Typography>
                  </Grid>
                </Grid>
              </StyledListItem>
            </Link>
          ))}
        </List>
      </Paper>
    </Box>
  );
};

export default ReportList;
