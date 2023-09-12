import React, { useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import authStore from "../../store/user/authStore";
import useStores from "../../store/useStores";


const OauthRedirect = () => {
  const param = useLocation();
  const navigate = useNavigate();
  const { authStore } = useStores();
  useEffect(() => {
    const search = param.search;
    const queryString = search.split('?')[1];
    const params = new URLSearchParams(queryString);

    const token = params.get('token');
    const isNewUser = params.get("isNewUser");

    if (token !== null) {
      const [bearer, tokenValue] = token.split(' ');
      localStorage.setItem("accessToken", bearer + ' ' + tokenValue);

      authStore.findUserInfo()
        .then((res) => {
          if (isNewUser==='true') {
            console.log('isNewUser')
            navigate('/oauth-signup');
          } else {
            navigate('/');
          }
        })
        .catch((res) => {
          alert('Could not obtain access token');
        });

      console.log(isNewUser);
    }

  }, []);

  return <></>;
};

export default OauthRedirect;
