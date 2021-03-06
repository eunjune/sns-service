import React from 'react';
import Helmet from 'react-helmet';
import PropTypes from 'prop-types';
import withRedux from 'next-redux-wrapper';
import withReduxSaga from 'next-redux-saga';
import {Provider} from 'react-redux';
import {createStore, compose, applyMiddleware} from 'redux';
import createSagaMiddleware from 'redux-saga';
import reducer from '../reducers';
import AppLayout from '../components/AppLayout';
import rootSaga from '../saga';
import cookie from 'react-cookies';
import {EMAIL_LOGIN_FINISH, LOAD_ME_REQUEST} from '../reducers/user';
import {Container} from 'next/app';

const Root = ({Component, store, pageProps}) => (
    <Container>
        <Provider store={store}>
            <Helmet
                title="SNS"
                htmlAttributes={{lang: 'ko'}}
                meta={[{
                    charset: 'UTF-8',
                }, {
                    name: 'viewport',
                    content: 'width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=yes,viewport-fit=cover'
                }, {
                    'http-equiv': 'X-UA-Compatible', content: 'IE-edge',
                }, {
                    name: 'description', content: 'SNS 페이지'
                }, {
                    name: 'og:title', content: 'SNS',
                }, {
                    name: 'og:description', content: 'SNS 페이지'
                }, {
                    property: 'og:type', content: 'website',
                }]}
                link={[{}, {
                    rel: 'stylesheet', href: 'https://cdnjs.cloudflare.com/ajax/libs/antd/3.16.2/antd.css',
                }, {
                    rel: 'stylesheet',
                    href: 'https://cdnjs.cloudflare.com/ajax/libs/slick-carousel/1.6.0/slick.min.css',
                }, {
                    rel: 'stylesheet',
                    href: 'https://cdnjs.cloudflare.com/ajax/libs/slick-carousel/1.6.0/slick-theme.min.css',
                }]}
            />
            <AppLayout>
                <Component {...pageProps} />
            </AppLayout>
        </Provider>
    </Container>
);

Root.propTypes = {
    Component: PropTypes.elementType.isRequired,
    store: PropTypes.object.isRequired,
    pageProps: PropTypes.object.isRequired,
};


Root.getInitialProps = async (context) => {
    const {ctx, Component} = context;
    let pageProps = {};

    const state = ctx.store.getState();

    const token = cookie.load('token') ||
        ctx.query.token ||
        (ctx.isServer && ctx.req.headers.cookie && ctx.req.headers.cookie.replace(/(token=)(.+)/, "$2"));


    if(ctx.query.token) {
        cookie.save('token', token, {path: '/'});
        ctx.store.dispatch({
            type: EMAIL_LOGIN_FINISH
        });
    }

    if (!state.user.me && token && state.user.isEmailLogInWaiting === false) {
        ctx.store.dispatch({
            type: LOAD_ME_REQUEST,
            data: token,
        });
    }

    if (Component.getInitialProps) {
        pageProps = await Component.getInitialProps(ctx) || {}; // return 값이 여기에 담김.
    }


    return {pageProps}; // 컴포넌트의 props
};

const configureStore = (initialState, options) => {
    const sagaMiddleware = createSagaMiddleware();
    const middlewares = [sagaMiddleware, (store) => (next) => (action) => {
        console.log(action);
        next(action);
    }];
    const enhancer = process.env.NODE_ENV === 'production'
        ? compose(applyMiddleware(...middlewares))
        : compose(applyMiddleware(...middlewares),
            !options.isServer && window.__REDUX_DEVTOOLS_EXTENSION__ !== 'undefined' ? window.__REDUX_DEVTOOLS_EXTENSION__() : (f) => f);
    const store = createStore(reducer, initialState, enhancer);
    store.sagaTask = sagaMiddleware.run(rootSaga);
    return store;
};

export default withRedux(configureStore)(withReduxSaga(Root));