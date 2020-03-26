import PropTypes from "prop-types";
import {EMAIL_CERTIFICATION_REQUEST} from "../reducers/user";
import {useSelector} from "react-redux";
import React, {useEffect} from "react";
import {Spin} from "antd";
import {SpinDiv} from "../components/styles/SpinDivStyle";
import Router from 'next/router';

const Certification = ({emailToken, email}) => {
    const {me} = useSelector((state) => state.user);

    useEffect(() => {

        if(me.isEmailCertification === true) {
            Router.push('/');
        }

    },[me]);

    return (
        <SpinDiv>
            <Spin />
        </SpinDiv>
    );
};

Certification.propTypes = {
    emailToken: PropTypes.string.isRequired,
    email: PropTypes.string.isRequired,
};

Certification.getInitialProps = async (context) => {
    const emailToken = context.query.emailToken;
    const email = context.query.email;


    context.store.dispatch({
        type: EMAIL_CERTIFICATION_REQUEST,
        data: {emailToken, email},
    });

    return { emailToken, email };
};

export default Certification;