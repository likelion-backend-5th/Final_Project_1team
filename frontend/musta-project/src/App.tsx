import Footer from './components/organisms/Footer';
import './App.css';
import { styled } from 'styled-components';
import { Provider } from 'mobx-react';
import useStores from './store/useStores';
import { GoogleOAuthProvider } from '@react-oauth/google';
import Navigation from './components/organisms/Navigation';
import UserRouter from './router/UserRouter';
import CustomAlert from './components/base/CustomAlert';

const StyledMain = styled.main`
  display: block;
  width: 100%;
  height: 100%;
  margin: 0px auto;
  padding: 0px;
  max-width: 1280px;
  //background-color: rgba(220, 250, 250);
  /* background-color: rgb(255, 245, 234); */
`;

function App() {
  const useStore = useStores();

  return (
    <GoogleOAuthProvider clientId="446534610656-14r63n2kho9aggjkp8ebi1rgods392uj.apps.googleusercontent.com">
      <Provider {...useStore}>
        <Navigation />
        <StyledMain>
          <UserRouter />
        </StyledMain>
        <Footer />
        <CustomAlert />
      </Provider>
    </GoogleOAuthProvider>
  );
}

export default App;
