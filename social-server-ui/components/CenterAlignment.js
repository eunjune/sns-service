import {Col, Row} from "antd";
import React, {memo} from "react";

const CenterAlignment = memo(({children}) => {
     console.log(children);
    const center = children.props.className === 'index' ? 12 : 8;
    const side = children.props.className === 'index' ? 6 : 8;

    return (
        <Row gutter={8}>
            <Col xs={24} md={side}/>
            <Col xs={24} md={center}>
                {children}
            </Col>
            <Col xs={24} md={side}/>
        </Row>
    );
});

export default CenterAlignment;