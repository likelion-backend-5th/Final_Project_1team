import { useState } from 'react';
import { DataGrid, GridColDef, GridValueGetterParams } from '@mui/x-data-grid';
import { isToday, toDate } from '../../util/DateUtil.ts';

function getDate(params: GridValueGetterParams) {
  const date: Date = toDate(params.row.createdDate);

  if (isToday(params.row.createdDate)) {
    const H: number = date.getHours();
    const amPm: string = `${H < 12 ? '오전' : '오후'}`;
    const M: number = date.getMinutes();
    return `${
      (H % 12 == 0 ? '12' : H % 12 < 10 ? '0' : '') +
      (H % 12 == 0 ? '' : H % 12)
    }:${(M < 10 ? '0' : '') + M} ${amPm}`;
  }

  return `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`;
}

const columns: GridColDef[] = [
  { field: 'title', headerName: 'Title', maxWidth: 500 },
  { field: 'username', headerName: 'User', maxWidth: 500 },
  // { field: 'createdDate', headerName: 'Date', maxWidth: 500 },
  {
    field: 'createdDate',
    headerName: 'Date',
    maxWidth: 500,
    valueGetter: getDate,
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
    <div>
      <DataGrid columns={columns} rows={articleArrayList} />
    </div>
  );
};

export default ArticleList;
