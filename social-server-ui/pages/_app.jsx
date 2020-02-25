import React from 'react';
import Head from "next/head";
import PropTypes from 'prop-types';
import AppLayout from "../components/AppLayout";
import reducer from '../reducers';
import withRedux from 'next-redux-wrapper';
import {Provider} from 'react-redux';
import {createStore, compose, applyMiddleware} from "redux";

const Root = ({Component, store}) => {
    return (
        <Provider store={store}>
            <Head>
                <title>SNS</title>
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/antd/3.26.11/antd.css"/>
            </Head>
            <AppLayout>
                <Component />
            </AppLayout>
        </Provider>
    );
};

Root.propTypes = {
    Component: PropTypes.element,
    store: PropTypes.object,
};

export default withRedux((initialState, options) => {
    const middlewares = [];
    const enhancer = compose(applyMiddleware(...middlewares),
        !options.isServer && window.__REDUX_DEVTOOLS_EXTENSION__ !== 'undefined' ? window.__REDUX_DEVTOOLS_EXTENSION__() : (f) => f);
    const store = createStore(reducer, initialState, enhancer);

    return store;
})(Root);