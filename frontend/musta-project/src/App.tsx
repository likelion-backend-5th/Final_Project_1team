import Footer from './components/organisms/Footer';
import Header from './components/organisms/Header';
import { Navigation } from './components/organisms/Navigation';
import UserRouter from './router/UserRouter';
import './App.css';
import { styled } from 'styled-components';
import { Provider } from 'mobx-react';
import useStores from './store/useStores';
import { Route, Routes } from 'react-router-dom';
import MainPage from './pages/MainPage';
import LoginForm from './components/auth/LoginForm';
import { Suspense, lazy } from 'react';
import Circular from './components/organisms/Circular';

const StyledMain = styled.main`
  display: block;
  width: 100%;
  height: 100%;
  margin: 0px auto;
  padding: 0px;
  //background-color: rgba(220, 250, 250);
  background-color: rgb(255, 245, 234);
`;

function App() {
  const useStore = useStores();
  return (
    <Provider {...useStore}>
      <Suspense fallback={<Circular />}></Suspense>
      {/* <MainPage>
        <LoginForm />
        <Routes>
          <Route
            path={''}
            element={<Suspense fallback={<Circular />}></Suspense>}
          />
        </Routes>
      </MainPage> */}
      <StyledMain>
        <Header />
        <Navigation />
        <UserRouter />
        <Footer />
      </StyledMain>
    </Provider>
  );
}

export default App;
