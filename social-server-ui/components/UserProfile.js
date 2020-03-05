import React, {useCallback} from 'react'
import {Avatar, Card, Button} from "antd";
import {useDispatch, useSelector} from "react-redux";
import { LOG_OUT} from '../reducers/user';

const UserProfile = () => {
    const {me} = useSelector(state => state.user);
    const {posts} = useSelector(state => state.post);
    const dispatch = useDispatch();
    const onLogout = useCallback(() => {
        dispatch({
            type: LOG_OUT,
        });
    }, []);

    return (
        <div>
            <Card
                actions={[
                    <div key="twit">게시글 수<br/>{posts.length}</div>,
                    <div key="following">팔로윙<br/>{}</div>,
                    <div key="twit">팔로워<br/>{}</div>,
                ]}
            >
                <Card.Meta avatar={<Avatar>{me.name[0]}</Avatar>}
                           title={me.name}
                />
                <Button onClick={onLogout}>로그아웃</Button>
            </Card>
        </div>
    );
};

export default UserProfile;