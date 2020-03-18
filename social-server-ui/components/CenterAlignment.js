import {Col, Row} from "antd";
import React from "react";

const CenterAlignment = ({children}) => {
    return (
        <Row gutter={8}>
            <Col xs={24} md={6}/>
            <Col xs={24} md={12}>
                {children}
            </Col>
            <Col xs={24} md={6}/>
        </Row>
    );
};

export default CenterAlignment;