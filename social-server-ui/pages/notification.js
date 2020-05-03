import React, {useCallback, useState} from "react";
import {Button, Collapse, Menu} from "antd";
import CaretRightOutlined from "@ant-design/icons/lib/icons/CaretRightOutlined";
import CenterAlignment from "../components/CenterAlignment";
import cookie from "react-cookies";
import {LOAD_NEW_NOTIFICATION_REQUEST, LOAD_READ_NOTIFICATION_REQUEST, LOAD_USER_REQUEST} from "../reducers/user";
import {LOAD_USER_POSTS_REQUEST} from "../reducers/post";
import User from "./user";
import {useSelector} from "react-redux";
import AvartarCustom from "../components/profile/AvartarCustom";
import NotificationList from "../components/NotificationList";

const Notification = () => {

    const [read,setRead] = useState(false);
    const [notRead,setNotRead] = useState(true);
    const {newNotifications, readNotifications} = useSelector((state) => state.user);

    const readClick = useCallback(() => {
        setRead(true);
        setNotRead(false);
    },[read,notRead]);

    const notReadClick = useCallback(() => {
        setRead(false);
        setNotRead(true);
    },[read,notRead]);



    return (
        <CenterAlignment children={
            <div className='index' style={{marginTop: 50}}>
                <Menu
                    defaultSelectedKeys={['1']}
                    defaultOpenKeys={['sub1']}
                    mode="horizontal"
                >
                    <Menu.Item key="1" onClick={notReadClick}>새로운 알림 <span>({newNotifications.length})</span></Menu.Item>
                    <Menu.Item key="2" onClick={readClick}>이전 알림 <span>({readNotifications.length})</span></Menu.Item>
                </Menu>

                {
                    notRead && <NotificationList notifications={newNotifications} isRead={false}/>
                }

                {
                    read && <NotificationList notifications={readNotifications} isRead={true}/>
                }
            </div>
        }
        />
    );
}

Notification.getInitialProps = async (context) => {
    const token = cookie.load('token') ||
        (context.isServer && context.req.headers.cookie && context.req.headers.cookie.replace(/(token=)(.+)/, "$2"));

    context.store.dispatch({
        type: LOAD_NEW_NOTIFICATION_REQUEST,
        data: token,
    });

    context.store.dispatch({
        type: LOAD_READ_NOTIFICATION_REQUEST,
        data: token,
    });

};

export default Notification;