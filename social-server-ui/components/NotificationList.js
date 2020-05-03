import CaretRightOutlined from "@ant-design/icons/lib/icons/CaretRightOutlined";
import AvartarCustom from "./profile/AvartarCustom";
import {Button, Collapse} from "antd";
import React, {useCallback, useEffect} from "react";
import Link from "next/link";
import {
    LOG_OUT,
    READ_NOTIFICATION_LIST,
    READ_NOTIFICATION_REQUEST,
    REMOVE_NOTIFICATION_REQUEST
} from "../reducers/user";
import cookie from "react-cookies";
import {useDispatch} from "react-redux";
import {CloseButton} from "./styles/imagesZoomStyle";
import CloseOutlined from "@ant-design/icons/lib/icons/CloseOutlined";

const NotificationList = ({notifications, isRead}) => {
    const { Panel } = Collapse;
    const dispatch = useDispatch();
    const token = cookie.load('token');
    let readList = [];

    const readNotification = useCallback((keyList) => {

        if(isRead) {
            return;
        }

        console.log(readList);
        keyList.forEach((key) => {
            if(readList.includes(key)) {
                return;
            }

            dispatch({
                type: READ_NOTIFICATION_REQUEST,
                data: {
                    id : key,
                    token : token,
                }
            });
            readList.push(key);
        })

    },[]);

    const removeNotification = useCallback((id) => () => {
        dispatch({
            type: REMOVE_NOTIFICATION_REQUEST,
            data: {
                id : id,
                token : token,
            }
        });
    },[])

    return (
        <Collapse
            bordered={false}
            expandIcon={({ isActive }) => <CaretRightOutlined rotate={isActive ? 90 : 0} />}
            className="site-collapse-custom-collapse"
            onChange={readNotification}
        >
            {
                notifications.map(v => {

                    switch (v.notificationType) {
                        case 'FOLLOW':
                            return (
                                <Panel header= {v.message} key={v.id} className="site-collapse-custom-panel">

                                    <AvartarCustom shape={"circle"} size={"default"} profileImageUrl={v.senderProfileImage}/>

                                    <span style={{marginLeft: 10}}>{v.message}</span>
                                    <Button style={{marginLeft: 10}}>수락</Button>
                                    <Button style={{marginLeft: 10}}>거절</Button>
                                    <CloseOutlined style={{float:'right', cursor: 'pointer'}} onClick={removeNotification(v.id)} />
                                </Panel>
                            );
                        case 'COMMENT':
                            return (
                                <Panel header={v.message} key={v.id} className="site-collapse-custom-panel">
                                    <AvartarCustom shape={"circle"} size={"default"} profileImageUrl={v.senderProfileImage}/>
                                    <span style={{marginLeft: 10}}>{v.message.substr(0, v.message.indexOf('님'))}님의 댓글 : {v.subMessage.split(',')[0]}</span>
                                    <span style={{marginLeft: 10}}>
                                        <Link href={{pathname: '/post', query: {id: v.subMessage.split(',')[1]}}} as={`/post/${v.subMessage.split(',')[1]}`}>
                                            <a>게시글 이동</a>
                                        </Link>
                                    </span>
                                    <CloseOutlined style={{float:'right', cursor: 'pointer'}} onClick={removeNotification(v.id)} />
                                </Panel>
                            );
                        case 'LIKE':
                            return (
                                <Panel header={v.message} key={v.id} className="site-collapse-custom-panel">
                                    <AvartarCustom shape={"circle"} size={"default"} profileImageUrl={v.senderProfileImage}/>
                                    <span style={{marginLeft: 10}}>{v.message}</span>
                                    <span style={{marginLeft: 10}}>
                                        <Link href={{pathname: '/post', query: {id: v.subMessage}}} as={`/post/${v.subMessage}`}>
                                            <a>게시글 이동</a>
                                        </Link>
                                    </span>
                                    <CloseOutlined style={{float:'right', cursor: 'pointer'}} onClick={removeNotification(v.id)} />
                                </Panel>
                            );
                        case 'RETWEET':
                            return (
                                <Panel header={v.message} key={v.id} className="site-collapse-custom-panel">
                                    <AvartarCustom shape={"circle"} size={"default"} profileImageUrl={v.senderProfileImage}/>
                                    <span style={{marginLeft: 10}}>{v.message}</span>
                                    <span style={{marginLeft: 10}}>
                                        <Link href={{pathname: '/post', query: {id: v.subMessage}}} as={`/post/${v.subMessage}`}>
                                            <a>게시글 이동</a>
                                        </Link>
                                    </span>
                                    <CloseOutlined style={{float:'right', cursor: 'pointer'}} onClick={removeNotification(v.id)} />
                                </Panel>
                            );
                        default:
                            break;
                    }
                })
            }
        </Collapse>
    );
}

export default NotificationList;