import {Col, Result} from "antd";
import React from "react";

const AuthenticationError = () => {

    return (
        <Col xs={24} md={24}>
            <Result
                style={{margin: 50}}
                status="warning"
                title="인증된 사용자만 이용할 수 있습니다."
            />
        </Col>
    );
};

export default AuthenticationError;