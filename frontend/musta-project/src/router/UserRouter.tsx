import ArticlePage from '../pages/ArticlePage.tsx';
import ReviewPage from '../pages/reviewPages/ReviewPage.tsx';
import ReviewEditPage from '../pages/reviewPages/ReviewEditPage.tsx';
import OrderConsumePage from '../pages/orderPages/OrderConsumePage.tsx';
import { Routes, Route } from 'react-router-dom';
import ArticleOrderPage from '../pages/orderPages/AricleOrdersPage.tsx';
import Error404Page from '../pages/Error404Page.tsx';
import HelloPage from '../pages/HelloPage.tsx';
import { HomePage } from '../pages/HomePage.tsx';
import MainPage from '../pages/MainPage.tsx';
import OrderDetailPage from '../pages/orderPages/OrderDetailPage.tsx';
import OrderSellerPage from '../pages/orderPages/OrderSellerPage.tsx';
import { ArticleDetail } from '../components/article/ArticleDetail.tsx';
import ReportList from '../pages/report/ReportList.tsx';
import ReportDetail from '../pages/report/ReportDetail.tsx';
import ReviewCreatePage from '../pages/reviewPages/ReviewCreatePage.tsx';

const UserRouter = () => {
  return (
    <Routes>
      <Route path="" element={<MainPage />} errorElement={<Error404Page />} />
      <Route
        path="/hello"
        element={<HelloPage />}
        errorElement={<Error404Page />}
      />
      <Route
        path="/home"
        element={<HomePage />}
        errorElement={<Error404Page />}
      />
      <Route
        path="/article/:articleId/order"
        element={<ArticleOrderPage />}
        errorElement={<Error404Page />}
      />
      <Route
        path="/review/create"
        element={<ReviewCreatePage />}
        errorElement={<Error404Page />}
      />
      <Route
        path="/review/:reviewApiId"
        element={<ReviewPage />}
        errorElement={<Error404Page />}
      />
      <Route
        path="/review/edit/:reviewApiId"
        element={<ReviewEditPage />}
        errorElement={<Error404Page />}
      />
      <Route path="/my/order/consume" element={<OrderConsumePage />} />
      <Route path="/my/order/sell" element={<OrderSellerPage />} />

      <Route
        path="/article/:articleApiId/order/:orderApiId"
        element={<OrderDetailPage />}
        errorElement={<Error404Page />}
      />

      <Route
        path={`/article/detail/:articleApiId`}
        element={<ArticleDetail />}
        errorElement={<Error404Page />}
      />
      <Route path="/article" element={<ArticlePage />} />

      <Route path="/reports" element={<ReportList />} />
      <Route path="/report/:reportApiId" element={<ReportDetail />} />
    </Routes>
  );
};

export default UserRouter;
