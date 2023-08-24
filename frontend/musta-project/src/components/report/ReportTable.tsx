import React from 'react';
import { Report } from '../../types/report';
import { statusToKorean } from '../../util/statusHelpers.ts';
import { Table, TableBody, TableCell, TableRow, Paper } from '@mui/material';

const ReportTable: React.FC<{ report: Report }> = ({ report }) => {
  return (
    <Paper elevation={3}>
      <Table sx={{ minWidth: 650 }}>
        <TableBody>
          <TableRow>
            <TableCell
              component="th"
              scope="row"
              variant="head"
              sx={{ fontWeight: 'bold', width: '20%' }}>
              신고자
            </TableCell>
            <TableCell>{report.reporterName}</TableCell>
          </TableRow>
          <TableRow>
            <TableCell
              component="th"
              scope="row"
              variant="head"
              sx={{ fontWeight: 'bold' }}>
              신고대상
            </TableCell>
            <TableCell>{report.reportedName}</TableCell>
          </TableRow>
          <TableRow>
            <TableCell
              component="th"
              scope="row"
              variant="head"
              sx={{ fontWeight: 'bold' }}>
              신고 내용
            </TableCell>
            <TableCell>{report.content}</TableCell>
          </TableRow>
          <TableRow>
            <TableCell
              component="th"
              scope="row"
              variant="head"
              sx={{ fontWeight: 'bold' }}>
              상태
            </TableCell>
            <TableCell>{statusToKorean(report.status)}</TableCell>
          </TableRow>
        </TableBody>
      </Table>
    </Paper>
  );
};

export default ReportTable;
