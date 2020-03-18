import React from 'react';
import Helmet from 'react-helmet';
import PropTypes from 'prop-types';
import withRedux from 'next-redux-wrapper';
import withReduxSaga from 'next-redux-saga';
import { Provider } from 'react-redux';
import { createStore, compose, applyMiddleware } from 'redux';
import createSagaMiddleware from 'redux-saga';
import reducer from '../reducers';
import AppLayout from '../components/AppLayout';
import rootSaga from '../saga';
import cookie from 'react-cookies';
import { LOAD_ME_REQUEST } from '../reducers/user';
import {Container} from 'next/app';

const Root = ({ Component, store, pageProps }) => (
  <Container>
    <Provider store={store}>
        <Helmet 
          title="SNS"
          htmlAttributes={{lang: 'ko'}}
          meta = {[{
            charset: 'UTF-8',
          } , {
            name: 'viewport', content: 'width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=yes,viewport-fit=cover'
          } , {
            'http-equiv' : 'X-UA-Compatible', content: 'IE-edge',
          }, {
            name: 'description', content: '트위터 클론 페이지'
          }, {
            name: 'og:tittle', content: 'SNS',
          }, {
            name: 'og:description',content: '트위터 클론 페이지'
          }, {
            property: 'og:type', content: 'website',
          }]}
          link={[{
            
          }, {
            rel: 'stylesheet', href: 'https://cdnjs.cloudflare.com/ajax/libs/antd/3.16.2/antd.css',
          }, {
            rel: 'stylesheet', href: 'https://cdnjs.cloudflare.com/ajax/libs/slick-carousel/1.6.0/slick.min.css',
          }, {
            rel: 'stylesheet', href: 'https://cdnjs.cloudflare.com/ajax/libs/slick-carousel/1.6.0/slick-theme.min.css',
          }]}
        />
        <AppLayout >
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
  const { ctx, Component } = context;
  let pageProps = {};

  const state = ctx.store.getState();
  const token = cookie.load('token') || (ctx.isServer ? ctx.req.headers.cookie.replace(/(.+)(token=)(.+)/,"$3") : '');
  
  console.log(token);
  if(!state.user.me && token) {
    ctx.store.dispatch({
      type: LOAD_ME_REQUEST,
      data : token,
    });    
  }

  if (Component.getInitialProps) {
    pageProps = await Component.getInitialProps(ctx) || {}; // return 값이 여기에 담김.
  }
  

  return { pageProps }; // 컴포넌트의 props
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