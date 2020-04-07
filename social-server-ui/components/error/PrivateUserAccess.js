import {Col, Result} from "antd";
import React from "react";


const PrivateUserAccess = () => {

    return (
        <Col xs={24} md={16}>

            <div style={{padding: 150, textAlign: 'center', verticalAlign: 'middle'}}>
                <Result
                    status="403"
                    title="비공개로 설정된 사용자입니다."
                    subTitle="팔로워들만 볼 수 있습니다."
                    extra={[]}
                />
            </div>
        </Col>
    );
};

export default PrivateUserAccess;