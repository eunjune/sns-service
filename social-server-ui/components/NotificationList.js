import CaretRightOutlined from "@ant-design/icons/lib/icons/CaretRightOutlined";
import AvartarCustom from "./profile/AvartarCustom";
import {Button, Collapse} from "antd";
import React, {useCallback, useEffect} from "react";
import Link from "next/link";
import {
    READ_NOTIFICATION_REQUEST,
    REMOVE_NOTIFICATION_REQUEST
} from "../reducers/user";
import cookie from "react-cookies";
import {useDispatch, useSelector} from "react-redux";
import CloseOutlined from "@ant-design/icons/lib/icons/CloseOutlined";
import moment from "moment";

moment.locale('ko');

const NotificationList = ({notifications, isRead}) => {
    const {me} = useSelector(state => state.user);
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

    const followAccept = useCallback(() => {


    },[]);

    const followRefusal = useCallback(() => {


    },[]);

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
                                    <AvartarCustom shape={"circle"} size={"default"} profileImageUrl={v.senderProfileImage} id={v.sender}/>

                                    <span style={{marginLeft: 10}}>{v.message}</span>
                                    <Button style={{marginLeft: 10}} onClick={followAccept}>수락</Button>
                                    <Button style={{marginLeft: 10}} onClick={followRefusal}>거절</Button>
                                    <CloseOutlined style={{float:'right', marginLeft: 10, cursor: 'pointer'}} onClick={removeNotification(v.id)} />
                                    <span style={{float: 'right'}}>{moment(v.createdAt).fromNow()}</span>
                                </Panel>
                            );
                        case 'COMMENT':
                            return (
                                <Panel header={v.message} key={v.id} className="site-collapse-custom-panel">
                                    <AvartarCustom shape={"circle"} size={"default"} profileImageUrl={v.senderProfileImage} id={v.sender}/>
                                    <span style={{marginLeft: 10}}>{v.message.substr(0, v.message.indexOf('님'))}님의 댓글 : {v.subMessage.split(',')[0]}</span>
                                    <span style={{marginLeft: 10}}>
                                        <Link href={{pathname: '/post', query: {postId: v.subMessage.split(',')[1], userId: me.id}}}
                                            as={`/user/${me.id}/post/${v.subMessage.split(',')[1]}`}>
                                            <a target='_blank'>게시글 이동</a>
                                        </Link>
                                    </span>
                                    <CloseOutlined style={{float:'right', marginLeft: 10, cursor: 'pointer'}} onClick={removeNotification(v.id)} />
                                    <span style={{float: 'right'}}>{moment(v.createdAt).fromNow()}</span>
                                </Panel>
                            );
                        case 'LIKE':
                            return (
                                <Panel header={v.message} key={v.id} className="site-collapse-custom-panel">
                                    <AvartarCustom shape={"circle"} size={"default"} profileImageUrl={v.senderProfileImage} id={v.sender}/>
                                    <span style={{marginLeft: 10}}>{v.message}</span>
                                    <span style={{marginLeft: 10}}>
                                        <Link href={{pathname: '/post', query: {postId: v.subMessage, userId: me.id}}}
                                              as={`/user/${me.id}/post/${v.subMessage}`}>
                                            <a target='_blank'>게시글 이동</a>
                                        </Link>
                                    </span>
                                    <CloseOutlined style={{float:'right', marginLeft: 10, cursor: 'pointer'}} onClick={removeNotification(v.id)} />
                                    <span style={{float: 'right'}}>{moment(v.createdAt).fromNow()}</span>
                                </Panel>
                            );
                        case 'RETWEET':
                            return (
                                <Panel header={v.message} key={v.id} className="site-collapse-custom-panel">
                                    <AvartarCustom shape={"circle"} size={"default"} profileImageUrl={v.senderProfileImage} id={v.sender}/>
                                    <span style={{marginLeft: 10}}>{v.message}</span>
                                    <span style={{marginLeft: 10}}>
                                        <Link href={{pathname: '/post', query: {postId: v.subMessage, userId: me.id}}}
                                              as={`/user/${me.id}/post/${v.subMessage}`}>
                                            <a target='_blank'>게시글 이동</a>
                                        </Link>
                                    </span>
                                    <CloseOutlined style={{float:'right', marginLeft: 10, cursor: 'pointer'}} onClick={removeNotification(v.id)} />
                                    <span style={{float: 'right'}}>{moment(v.createdAt).fromNow()}</span>
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