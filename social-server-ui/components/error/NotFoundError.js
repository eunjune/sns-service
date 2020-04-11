import {Col, Result, Row} from "antd";
import React from "react";

const NotFoundError = () => {

    return (
        <Row gutter={8}>
            <Col xs={24} md={8}/>
            <Col xs={24} md={8}>
                <Result
                    style={{marginTop: '50%'}}
                    status="404"
                    title="404"
                    subTitle="존재하지 페이지 입니다."
                />
            </Col>
            <Col xs={24} md={8}/>
        </Row>
    );
};

export default NotFoundError;