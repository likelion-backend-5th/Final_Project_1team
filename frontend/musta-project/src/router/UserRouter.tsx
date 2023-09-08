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
import ReviewListPage from '../pages/reviewPages/ReviewListPage.tsx';
import ReportRegister from '../components/report/ReportRegister.tsx';
import { ArticlePost } from '../components/article/ArticlePost.tsx';
import { ArticleEdit } from '../components/article/ArticleEdit.tsx';
import LoginForm from '../components/auth/LoginForm.tsx';
import PaymentWidget from '../components/payment/PaymentWidget.tsx';
import ChatPage from '../pages/chat/ChatPage.tsx';
import ChatRoomList from '../components/chat/ChatRoomList.tsx';
import SignUpForm from '../components/auth/SignUpForm.tsx';
// import UserPage from '../pages/user/UserPage.tsx';
import SignUpSuccessForm from '../components/auth/SignUpSuccessForm.tsx';
import ProfileForm from '../components/user/ProfileForm.tsx';
import UserPage from '../pages/user/UserPage.tsx';
import ChangePasswordForm from '../components/user/ChangePasswordFrom.tsx';
import ReviewForm from '../components/user/ReviewForm.tsx';
import OrderForm from '../components/user/OrderForm.tsx';

const UserRouter = () => {
  return (
    <Routes>
      <Route path="/" element={<MainPage />} />
      <Route path="/login" element={<LoginForm />} />
      <Route path="/signup" element={<SignUpForm />} />
      <Route path="/signup/success" element={<SignUpSuccessForm />} />
      <Route
        path="/user/profile"
        element={
          <UserPage>
            <ProfileForm />
          </UserPage>
        }
      />
      <Route
        path="/user/reviews"
        element={
          <UserPage>
            <ReviewForm />
          </UserPage>
        }
      />
      <Route
        path="/user/password/edit"
        element={
          <UserPage>
            <ChangePasswordForm />
          </UserPage>
        }
      />
      <Route
        path="/user/orders"
        element={
          <UserPage>
            <OrderForm />
          </UserPage>
        }
      />

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
        path="/article/:articleApiId/order/:orderApiId/review"
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
      <Route path="/review-list/test" element={<ReviewListPage />} />
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
      <Route path="/article/post" element={<ArticlePost />} />
      <Route path="/article/edit/:articleApiId" element={<ArticleEdit />} />
      <Route
        path="/article/detail/:articleApiId/payment"
        element={<PaymentWidget />}
      />

      <Route path="/reports" element={<ReportList />} />
      <Route path="/report/:reportApiId" element={<ReportDetail />} />
      <Route
        path="/report/:resourceType/:resourceApiId"
        element={<ReportRegister />}
      />
      <Route path="/chatrooms" element={<ChatRoomList />} />
      <Route path="/chatroom/:roomId" element={<ChatPage />} />
      <Route path="/*" element={<Error404Page />} />
    </Routes>
  );
};

export default UserRouter;
