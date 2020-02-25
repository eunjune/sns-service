import React from 'react'
import {Avatar, Card} from "antd";

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
    return (
        <Card
            action={[
                <div key="twit">짹짹<br/>{dummy.Post.length}</div>,
                <div key="following">팔로윙<br/>{dummy.Followings.length}</div>,
                <div key="twit">팔로워<br/>{dummy.Followers.length}</div>,
            ]}
        >
            <Card.Meta avatar={<Avatar>{dummy.name[0]}</Avatar>}
                       title={dummy.name}
            />

        </Card>
    );
};

export default UserProfile;