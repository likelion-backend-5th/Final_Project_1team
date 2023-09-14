import { useEffect, useState } from 'react';
import {
  DataGrid,
  GridColDef,
  GridPaginationModel,
  GridRenderCellParams,
  GridValueGetterParams,
} from '@mui/x-data-grid';
import {
  getFormattedDate,
  getFormattedTime,
  isToday,
} from '../../util/dateUtil.ts';
import { Chip } from '@mui/material';
import { Article, getChipColorByArticleStatus } from '../../types/article.ts';
import axios from 'axios';
import { ArticleDetail } from './ArticleDetail.tsx';

function getDate(params: GridValueGetterParams) {
  if (isToday(params.row.createdDate)) {
    return getFormattedTime(params.row.createdDate);
  }

  return getFormattedDate(params.row.createdDate);
}

const columns: GridColDef[] = [
  {
    field: 'articleStatus',
    headerName: '글 상태',
    renderCell: (params: GridRenderCellParams<Article>) => (
      <Chip
        label={params.row.articleStatus}
        color={getChipColorByArticleStatus(params.row.articleStatus)}></Chip>
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

const PAGE_SIZE: number = 10;
const baseURL = `${import.meta.env.VITE_API}api/articles`;

const ArticleList = () => {
  const [articleArrayList, setArticleArrayList] = useState<Article[]>([]);
  const [currentPageNumber, setCurrentPageNumber] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [curPageItemCount, setCurPageItemCount] = useState(10);
  const [totalElements, setTotalElements] = useState(100);
  const [limit, setLimits] = useState(20);
  const [loading, setLoading] = useState(true);
  const [url, setUrl] = useState(baseURL);
  const [paginationModel, setPaginationModel] = useState({
    page: 0,
    pageSize: PAGE_SIZE,
  });

  const handlePaginationModelChange = (
    newPaginationModel: GridPaginationModel
  ) => {
    let curUrl = new URL(url);
    let curUrlSearchParams = new URLSearchParams(curUrl.search);

    curUrlSearchParams.set('page', String(newPaginationModel.page));

    setUrl(`${baseURL}?${curUrlSearchParams}`);
    setPaginationModel(newPaginationModel);
  };

  const fetchData = async () => {
    const response = axios.get(url);

    response.then((data) => {
      setArticleArrayList(data.data.content);
      setCurrentPageNumber(data.data.number + 1);
      setTotalPages(data.data.totalPages);
      setCurPageItemCount(data.data.numberOfElements);
      setLimits(data.data.size);
      setTotalElements(data.data.totalElements);
      setLoading(false);
    });
  };

  useEffect(() => {
    setLoading(true);
    fetchData();
  }, [url]);

  return (
    <div style={{ width: '100%' }}>
      <DataGrid
        keepNonExistentRowsSelected
        onRowClick={() => console.log('clicked')}
        columns={columns}
        rows={articleArrayList}
        getRowId={(row) => row.apiId}
        pageSizeOptions={[curPageItemCount]}
        rowCount={totalElements}
        loading={loading}
        paginationMode="server"
        paginationModel={paginationModel}
        onPaginationModelChange={handlePaginationModelChange}
      />
      <a href="/article/detail/317b6154-d900-41dc-bd39-2fb0e76efa47">
        detail sample page
      </a>
    </div>
  );
};

export default ArticleList;
