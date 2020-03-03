import React from 'react';
import Head from "next/head";
import PropTypes from 'prop-types';
import AppLayout from "../components/AppLayout";
import reducer from '../reducers';
import withRedux from 'next-redux-wrapper';
import {Provider} from 'react-redux';
import {createStore, compose, applyMiddleware} from "redux";
import createSagaMiddleware from "redux-saga";
import rootSaga from "../saga";

const Root = ({Component, store, pageProps}) => {
    return (
        <Provider store={store}>
            <Head>
                <title>SNS</title>
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/antd/3.26.11/antd.css"/>
            </Head>
            <AppLayout>
                <Component {...pageProps} />
            </AppLayout>
        </Provider>
    );
};

Root.propTypes = {
    Component: PropTypes.elementType.isRequired,
    store: PropTypes.object.isRequired,
    pageProps: PropTypes.object.isRequired,
};

// next에서 실행시키는 부분
// context에 컴포넌트와 ctx가 들어있음. next에서 넣어줌.
// 컴포넌트에 getInitialProps가 있으면 그걸 실행시켜준다.
// ctx : 서버(url의 값 등)에서 넣어준 값이 존재
Root.getInitialProps = async(context) => {
  const {ctx, Component} = context;
  let pageProps = {};
  if(Component.getInitialProps) {
    pageProps = await Component.getInitialProps(ctx); // return 값이 여기에 담김.
  }
  return {pageProps}; // 컴포넌트의 props
};

export default withRedux((initialState, options) => {
    const sagaMiddleware = createSagaMiddleware();
    const middlewares = [sagaMiddleware];
    const enhancer = process.env.NODE_ENV === 'production'
        ? compose(applyMiddleware(...middlewares))
        : compose(applyMiddleware(...middlewares),
        !options.isServer && window.__REDUX_DEVTOOLS_EXTENSION__ !== 'undefined' ? window.__REDUX_DEVTOOLS_EXTENSION__() : (f) => f
        );
    const store = createStore(reducer, initialState, enhancer);
    sagaMiddleware.run(rootSaga);
    return store;
})(Root);
