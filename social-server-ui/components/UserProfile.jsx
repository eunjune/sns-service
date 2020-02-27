import React, {useCallback} from 'react'
import {Avatar, Card, Button} from "antd";
import {useDispatch, useSelector} from "react-redux";
import { LOG_OUT_REQUEST} from '../reducers/user';

const dummy = {
    name: '이름' +
        '' +
        '',
    Post: [],
    Followings: [],
    Followers: [],
    isLogin: false,
};

const UserProfile = () => {
    const {me} = useSelector(state => state.user);
    const dispatch = useDispatch();
    const onLogout = useCallback(() => {
        dispatch({
            type: LOG_OUT_REQUEST,
        });
    }, []);

    return (
        <Card
            action={[
                <div key="twit">짹짹<br/>{me.Post.length}</div>,
                <div key="following">팔로윙<br/>{me.Followings.length}</div>,
                <div key="twit">팔로워<br/>{me.Followers.length}</div>,
            ]}
        >
            <Card.Meta avatar={<Avatar>{me.name[0]}</Avatar>}
                       title={me.name}
            />
            <Button onClick={onLogout}>로그아웃</Button>
        </Card>
    );
};

export default UserProfile;
