import React, {useCallback} from 'react'
import {Avatar, Card, Button} from "antd";
import Link from 'next/link'
import {useDispatch, useSelector} from "react-redux";
import { LOG_OUT} from '../reducers/user';
import Router from 'next/router';

const UserProfile = (props) => {
    const {me} = useSelector(state => state.user);
    const {posts} = useSelector(state => state.post);
    const dispatch = useDispatch();

    const onLogout = useCallback(() => {
    
        Router.push('/');
        dispatch({
            type: LOG_OUT,
        });

        
    }, []);

    return (
        <div>
            <Card
                actions={[
                    <Link href="/profile" key="twit" prefetch><a><div>게시글<br/>{posts.length}</div></a></Link>,
                    <Link href="/profile" key="following" prefetch><a><div>팔로윙<br/>{me.followings.length}</div></a></Link>,
                    <Link href="/profile" key="follower" prefetch><a><div>팔로워<br/>{me.followers.length}</div></a></Link>,         
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
