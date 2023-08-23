import { useState } from 'react';
import {
  DataGrid,
  GridColDef,
  GridRenderCellParams,
  GridValueGetterParams,
} from '@mui/x-data-grid';
import {
  getFormattedDate,
  getFormattedDateTime,
  getFormattedTime,
  isToday,
} from '../../util/DateUtil.ts';
import { Chip } from '@mui/material';
import { Article, ArticleStatus } from '../../types/article.ts';

function getDate(params: GridValueGetterParams) {
  if (isToday(params.row.createdDate)) {
    return getFormattedTime(params.row.createdDate);
  }

  console.log(getFormattedDateTime(params.row.createdDate));

  return getFormattedDate(params.row.createdDate);
}

const columns: GridColDef[] = [
  {
    field: 'articleStatus',
    headerName: '글 상태',
    renderCell: (params: GridRenderCellParams<Article>) => (
      <Chip
        label={params.row.articleStatus}
        color={
          params.row.articleStatus === ArticleStatus.LIVE ? 'primary' : 'danger'
        }></Chip>
    ),
  },
  { field: 'title', headerName: 'Title', flex: 1 },
  { field: 'username', headerName: 'User', flex: 1 },
  {
    field: 'createdDate',
    headerName: 'Date',
    valueGetter: getDate,
    flex: 1,
  },
];

const ArticleList = () => {
  const [articleArrayList, setArticleArrayList] = useState<Article[]>([
    {
      id: '1111',
      title: 'test-1',
      description: 'test-1',
      username: 'user1',
      thumbnail: '',
      status: 'ACTIVE',
      articleStatus: 'LIVE',
      createdDate: '2023-08-23 24:10:00',
    },
    {
      id: '2222',
      title: 'test-2',
      description: 'test-2',
      username: 'user1',
      thumbnail: '',
      status: 'ACTIVE',
      articleStatus: 'LIVE',
      createdDate: '2023-08-23 00:20:00',
    },
    {
      id: '3333',
      title: 'test-3',
      description: 'test-3',
      username: 'user2',
      thumbnail: '',
      status: 'ACTIVE',
      articleStatus: 'LIVE',
      createdDate: '2023-08-23 00:30:00',
    },
    {
      id: '4444',
      title: 'test-4',
      description: 'test-4',
      username: 'user2',
      thumbnail: '',
      status: 'ACTIVE',
      articleStatus: 'LIVE',
      createdDate: '2023-08-21 00:30:00',
    },
  ]);

  return (
    <div style={{ width: '100%' }}>
      <DataGrid columns={columns} rows={articleArrayList} />
    </div>
  );
};

export default ArticleList;
