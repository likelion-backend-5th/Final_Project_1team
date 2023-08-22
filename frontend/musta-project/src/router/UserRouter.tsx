import Error404Page from "../pages/Error404Page";
import HelloPage from "../pages/HelloPage";
import MainPage from "../pages/MainPage";
import { Route, Routes } from "react-router-dom";
import { HomePage } from "../pages/HomePage";
import ArticleOrder from "../components/ArticleOrder";
import ArticleOrderPage from "../pages/AricleOrders";
import ArticlePage from '../pages/ArticlePage.tsx';

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
        path="/orders"
        element={<ArticleOrderPage />}
        errorElement={<Error404Page />}
      />
      <Route path="/articles" element={<ArticlePage />} />
    </Routes>
  );
};

export default UserRouter;
