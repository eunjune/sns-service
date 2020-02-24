import React from 'react';
import Head from "next/head";
import PropTypes from 'prop-types';
import AppLayout from "../components/AppLayout";

const Root = ({Component}) => {
    return (
        <>
            <Head>
                <title>SNS</title>
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/antd/3.26.11/antd.css"/>
            </Head>
            <AppLayout>
                <Component />
            </AppLayout>
        </>
    );
};

Root.propTypes = {
    Component: PropTypes.element,
};

export default Root;