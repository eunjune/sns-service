import React, {useCallback, useState} from "react";
import {Button, Collapse, Menu} from "antd";
import CaretRightOutlined from "@ant-design/icons/lib/icons/CaretRightOutlined";
import CenterAlignment from "../components/CenterAlignment";

const Notification = () => {
    const { Panel } = Collapse;
    const [read,setRead] = useState(false);
    const [notRead,setNotRead] = useState(true);
    const list = ['text1','text2','text3'];

    const readClick = useCallback(() => {
        setRead(true);
        setNotRead(false);
    },[]);

    const notReadClick = useCallback(() => {
        setRead(false);
        setNotRead(true);
    },[]);

    const callback = useCallback((e) => {
        console.log('click');
    },[]);

    return (
        <CenterAlignment children={
            <div className='index' style={{marginTop: 50}}>
                <Menu
                    defaultSelectedKeys={['1']}
                    defaultOpenKeys={['sub1']}
                    mode="horizontal"
                >
                    <Menu.Item key="1" onClick={notReadClick}>새로운 알림 <span>(0)</span></Menu.Item>
                    <Menu.Item key="2" onClick={readClick}>이전 알림 <span>(0)</span></Menu.Item>
                </Menu>
                <Collapse
                    bordered={false}
                    expandIcon={({ isActive }) => <CaretRightOutlined rotate={isActive ? 90 : 0} />}
                    className="site-collapse-custom-collapse"
                    onChange={callback}
                >
                    {
                        list.map(v => {
                            return (
                                <Panel header="~가 팔로우를 요청했습니다." key="1" className="site-collapse-custom-panel">
                                    <Button style={{marginLeft: 10}}>수락</Button>
                                    <Button style={{marginLeft: 10}}>거절</Button>
                                </Panel>
                            );
                        })
                    }

                    {
                        list.map(v => {
                            return (
                                <Panel header={"~가 포스트id에 대해 댓글을 작성했습니다."} key="2" className="site-collapse-custom-panel">
                                    <p>{v}</p>
                                </Panel>
                            );
                        })
                    }

                    {
                        list.map(v => {
                            return (
                                <Panel header={"~가 포스트id에 대해 좋아요, 리트윗을 했습니다."} key="3" className="site-collapse-custom-panel">
                                    <p>{v}</p>
                                </Panel>
                            );
                        })
                    }

                </Collapse>
            </div>
        }
        />
    );

}

export default Notification;