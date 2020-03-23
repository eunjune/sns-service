import React, { useEffect, useCallback, useState } from 'react';
import cookie from 'react-cookies';
import {Button, List, Card, Icon, Col, Row, Input, Form, Avatar} from 'antd';
import { useDispatch, useSelector } from 'react-redux';
import { LOAD_FOLLOWER_REQUEST, LOAD_FOLLOWING_REQUEST, UNFOLLOW_USER_REQUEST, REMOVE_FOLLOWER_REQUEST } from '../reducers/user';
import { LOAD_USER_POSTS_REQUEST, LOAD_MAIN_POSTS_REQUEST } from '../reducers/post';
import PostCards from '../components/PostCards';
import FollowList from "../components/FollowList";
import CenterAlignment from "../components/CenterAlignment";
import Router from "next/router";
import ProfileEditForm from "../components/ProfileEditForm";

const Profile = () => {

    const [profileOn,setProfileOn] = useState(true);
    const [profileFollowOn,setProfileFollowOn] = useState(false);
    const [profilePostOn,setProfilePostOn] = useState(false);

    const dispatch = useDispatch();
    const {followers, followings, hasMoreFollower, hasMoreFollowing, me} = useSelector(state => state.user);
    const {posts} = useSelector(state => state.post);
    const token = cookie.load('token');

    const onUnfollow = useCallback(userId => () => {

        dispatch({
            type: UNFOLLOW_USER_REQUEST,
            data: {
                userId,
                token
            }
        });
    },[]);

    const onRemoveFollower = useCallback(userId => () => {

        dispatch({
            type: REMOVE_FOLLOWER_REQUEST,
            data: {
                userId,
                token
            }
        });
        
    },[]);

    const loadMoreFollowings = useCallback(() => {
        console.log(followings.length);

        dispatch({
            type: LOAD_FOLLOWING_REQUEST,
            data: {
                token,
                offset: followings.length/3

            }
        })
    },[followings]);

    const loadMoreFollowers = useCallback(() => {
        dispatch({
            type: LOAD_FOLLOWER_REQUEST,
            data: {
                token,
                offset: followers.length/3
            }
        })
    },[followers]);

    const clickProfile = useCallback(() => {
        setProfileOn(true);
        setProfilePostOn(false);
        setProfileFollowOn(false);
    }, []);

    const clickPost = useCallback(() => {
        setProfileOn(false);
        setProfilePostOn(true);
        setProfileFollowOn(false);
    },[]);

    const clickFollow = useCallback(() => {
        setProfileOn(false);
        setProfilePostOn(false);
        setProfileFollowOn(true);
    },[]);

    return (
        <Row gutter={8}>
            <Col xs={24} md={8}>
                <div style={{padding: 50}}>
                    <Card
                        actions={[
                            <div onClick={clickPost}>게시글<br/>{posts.length}</div>,
                            <div onClick={clickFollow}>팔로윙<br/>{me.followings.length}</div>,
                            <div onClick={clickFollow}>팔로워<br/>{me.followers.length}</div>
                        ]}
                        cover={<img src={me.profileImageUrl || 'http://localhost:8080/image/default-user.png'} alt="프로필 사진" style={{padding: 50}}/>}
                    >

                        <Card.Meta avatar={<Avatar>{me.name[0]}</Avatar>}
                                   title={me.name}
                        />


                        <Button onClick={clickProfile}>프로필 수정</Button>

                    </Card>
                </div>
                {/**/}
            </Col>
            <Col xs={24} md={8}>
                {profileOn && <ProfileEditForm me={me}/>}
                {profilePostOn &&
                    <div style={{paddingTop: 50}}>
                        {posts.map((p) => (
                            <PostCards key={+p.id} post={p} />
                        ))}
                    </div>
                }
                {profileFollowOn &&
                    <div style={{paddingTop: 50}}>
                        <FollowList
                            header="팔로잉 목록"
                            hasMore={hasMoreFollowing}
                            onClickMore={loadMoreFollowings}
                            onClickStop={onUnfollow}
                            data={followings}
                        />

                        <FollowList
                            header="팔로워 목록"
                            hasMore={hasMoreFollower}
                            onClickMore={loadMoreFollowers}
                            onClickStop={onRemoveFollower}
                            data={followers}
                        />

                    </div>
                }
            </Col>
            <Col xs={24} md={8}>

            </Col>
        </Row>



    );

};

Profile.getInitialProps = async(context) => {
    const token = cookie.load('token') || (context.isServer && context.req.headers.cookie.includes('token')
        ? context.req.headers.cookie.replace(/(.+)(token=)(.+)/,"$3") : '');

    if(token.length > 0) {
        context.store.dispatch({
            type: LOAD_FOLLOWER_REQUEST,
            data: {token}
        });

        context.store.dispatch({
            type: LOAD_FOLLOWING_REQUEST,
            data: {token}
        });

        context.store.dispatch({
            type: LOAD_USER_POSTS_REQUEST,
            data: {
                userId: 0,
                token: token,
            }
        });
    }

};

export default Profile;