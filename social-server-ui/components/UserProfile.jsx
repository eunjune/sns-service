import React, {useCallback} from 'react'
import {Avatar, Card, Button} from "antd";
import {useDispatch, useSelector} from "react-redux";
import {logoutAction} from "../reducers/user";

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
    const {user} = useSelector(state => state.user);
    const dispatch = useDispatch();
    const onLogout = useCallback(() => {
        dispatch(logoutAction);
    }, []);

    return (
        <Card
            action={[
                <div key="twit">짹짹<br/>{user.Post.length}</div>,
                <div key="following">팔로윙<br/>{user.Followings.length}</div>,
                <div key="twit">팔로워<br/>{user.Followers.length}</div>,
            ]}
        >
            <Card.Meta avatar={<Avatar>{user.name[0]}</Avatar>}
                       title={user.name}
            />
            <Button onClick={onLogout}>로그아웃</Button>
        </Card>
    );
};

export default UserProfile;