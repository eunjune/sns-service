import Error from 'next/error';
import React from 'react';
import PropTypes from 'prop-types';
import NotFoundError from "../components/error/NotFoundError";

const MyError = (props) => {
    return (
        <div>
            {
                props.statusCode === 404 && <NotFoundError />
            }
            {
                props.statusCode !== 404 && <Error statusCode={props.statusCode}/>
            }
        </div>
    );
}

MyError.propTypes = {
    statusCode: PropTypes.number,
}

MyError.defaultProps = {
    statusCode: 400,
}

MyError.getInitialProps = async(context) => {
    const statusCode = context.res ? context.res.statusCode : context.err ? err.statusCode : null;
    return {statusCode};
}

export default MyError;