import React, { useCallback, useEffect, useState } from "react";
import * as authAction from './auth-action';

// let logoutTimer: NodeJS.Timeout;

type Props = { children?: React.ReactNode }
type UserInfo = { username: string, nickname: string };
type LoginToken = {
    grantType: string,
    accessToken: string,
    accessTokenExpiresIn: number
}

const AuthContext = React.createContext({
    token: '',
    userObj: { username: '', nickname: '' },
    isLoggedIn: false,
    isSuccess: false,
    isGetSuccess: false,
    login: (username: string, password: string) => {
    },
    logout: () => {
    },
    getUser: () => {
    }
});


export const AuthContextProvider: React.FC<Props> = (props) => {

    const tokenData = authAction.retrieveStoredToken();

    let initialToken: any;
    if (tokenData) {
        initialToken = tokenData.token!;
    }

    const [token, setToken] = useState(initialToken);
    const [userObj, setUserObj] = useState({
        username: '',
        nickname: '',
    });

    const [isSuccess, setIsSuccess] = useState<boolean>(false);
    const [isGetSuccess, setIsGetSuccess] = useState<boolean>(false);

    const userIsLoggedIn = !!token;

    const loginHandler = (username: string, password: string) => {
        setIsSuccess(false);

        const data = authAction.loginActionHandler(username, password);

        data.then((result) => {
            if (result !== null) {
                const loginData: LoginToken = result.data;
                setToken(loginData.accessToken);
                console.log("logind token" + token);

                // logoutTimer = setTimeout(
                //     logoutHandler,
                //     authAction.loginTokenHandler(loginData.accessToken, loginData.accessTokenExpiresIn)
                // );

                setIsSuccess(true);

                console.log("loginHandler: " + isSuccess);
            }
        })
    };

    const logoutHandler = useCallback(() => {
        setToken('');
        authAction.logoutActionHandler();
        // if (logoutTimer) {
        //     clearTimeout(logoutTimer);
        // }
    }, []);

    const getUserHandler = () => {
        setIsGetSuccess(false);
        const data = authAction.getUserActionHandler(token);
        data.then((result) => {
            if (result !== null) {
                console.log('get user start!');
                const userData: UserInfo = result.data;
                setUserObj(userData);
                setIsGetSuccess(true);
            }
        })
    };

    useEffect(() => {
        if (tokenData) {
            console.log(tokenData.duration);
            // logoutTimer = setTimeout(logoutHandler, tokenData.duration);
        }
    }, [tokenData, logoutHandler]);


    const contextValue = {
        token,
        userObj,
        isLoggedIn: userIsLoggedIn,
        isSuccess,
        isGetSuccess,
        login: loginHandler,
        logout: logoutHandler,
        getUser: getUserHandler,
    }

    return (
        <AuthContext.Provider value={contextValue}>
            {props.children}
        </AuthContext.Provider>
    )
}

export default AuthContext;