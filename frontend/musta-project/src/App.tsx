import { Provider } from 'mobx-react';
import { useEffect } from 'react';
import { styled } from 'styled-components';
import './App.css';
import CustomAlert from './components/base/CustomAlert';
import Footer from './components/organisms/Footer';
import Navigation from './components/organisms/Navigation';
import UserRouter from './router/UserRouter';
import useStores from './store/useStores';

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

  useEffect(() => {
    // localStorage.clear();
    // removeRefershToken();
  }, []);
  return (
   <Provider {...useStore}>
        <Navigation />
        <StyledMain>
          <UserRouter />
        </StyledMain>
        <Footer />
        <CustomAlert />
      </Provider>
  );
}

export default App;
