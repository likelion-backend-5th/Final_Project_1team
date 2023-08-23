
import ArticlePage from '../pages/ArticlePage.tsx';
import ReviewPage from '../pages/reviewPages/ReviewPage.tsx';
import ReviewEditPage from '../pages/reviewPages/ReviewEditPage.tsx';
import OrderDetailPage from "../pages/OrderDetailPage.tsx";
import OrderConsumePage from "../pages/OrderConsumePage.tsx";
import OrderSellerPage from "../pages/OrderSellerPage.tsx";
import { Routes, Route } from 'react-router-dom';
import ArticleOrderPage from '../pages/AricleOrdersPage.tsx';
import Error404Page from '../pages/Error404Page.tsx';
import HelloPage from '../pages/HelloPage.tsx';
import { HomePage } from '../pages/HomePage.tsx';
import MainPage from '../pages/MainPage.tsx';

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
        element={<ArticleOrderPage/>}
        errorElement={<Error404Page />}
      />
      <Route path="/articles" element={<ArticlePage />} />
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

    </Routes>
  );
};

export default UserRouter;
