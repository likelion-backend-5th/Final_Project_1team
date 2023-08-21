import Footer from './components/organisms/Footer';
import Header from './components/organisms/Header';
import { Navigation } from './components/organisms/Navigation';
import UserRouter from './router/UserRouter';
import './App.css'
import { styled } from 'styled-components';

const StyledMain = styled.main`
  display: block;
  width: 100%;
  height: 100%;
  margin: 0px auto;
  padding: 0px;
  background-color: rgba(220, 250, 250);
`;


function App() {
  return (
    <StyledMain>
      <Header />
      <Navigation />
      {/* 권한별 라우터 정의 필요 */}
      <UserRouter />
      <Footer />
    </StyledMain>
  )
}

export default App
