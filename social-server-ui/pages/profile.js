import React, {useEffect, useCallback, useState, useRef} from 'react';
import cookie from 'react-cookies';
import {Button, List, Card, Icon, Col, Row, Input, Form, Avatar} from 'antd';
import { useDispatch, useSelector } from 'react-redux';
import {
    LOAD_FOLLOWER_REQUEST,
    LOAD_FOLLOWING_REQUEST,
    UNFOLLOW_USER_REQUEST,
    REMOVE_FOLLOWER_REQUEST,
    UPLOAD_IMAGE_REQUEST
} from '../reducers/user';
import {LOAD_USER_POSTS_REQUEST, LOAD_MAIN_POSTS_REQUEST, UPLOAD_IMAGES_REQUEST} from '../reducers/post';
import PostCards from '../components/PostCards';
import FollowList from "../components/FollowList";
import CenterAlignment from "../components/CenterAlignment";
import Router from "next/router";
import ProfileEditForm from "../components/ProfileEditForm";


const Profile = () => {

    const [profileOn,setProfileOn] = useState(true);
    const [profileFollowOn,setProfileFollowOn] = useState(false);
    const [profilePostOn,setProfilePostOn] = useState(false);
    const [uploadImageReady,setUploadImageReady] = useState(false);

    const dispatch = useDispatch();
    const updateProfileImage = useRef();
    const imageInput = useRef();
    const {followers, followings, hasMoreFollower, hasMoreFollowing, me} = useSelector(state => state.user);
    const {posts} = useSelector(state => state.post);
    const token = cookie.load('token');
    let uploadImageFile = null;

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


    const onChangeImages = useCallback((e) => {
        const file = e.target.files[0];
        console.log('onChangeImages');
        console.log(file);
        uploadImageFile = file;
        updateProfileImage.current.src = window.URL.createObjectURL(file);
        setUploadImageReady(true);
    }, [uploadImageFile,uploadImageReady]);

    const onClickSelectImage = useCallback(() => {
        imageInput.current.click();
    },[imageInput.current]);

    const onClickUploadImage = useCallback(() => {
        console.log('onClickUploadImage');
        console.log(uploadImageFile);
        if(uploadImageFile != null) {
            dispatch({
                type: UPLOAD_IMAGE_REQUEST,
                data: {
                    file : uploadImageFile,
                    token,
                }
            });
            setUploadImageReady(false);
        }
    },[uploadImageFile]);

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
                        cover={<img ref={updateProfileImage} src={me.profileImageUrl ? `http://localhost:8080/image/profile/${me.profileImageUrl}` :
                                            'http://localhost:8080/image/profile/default-user.png'} alt="프로필 사진" style={{padding: 50}}/>}
                    >
                        {profileOn && <input type="file" multiple hidden ref={imageInput} onChange={onChangeImages}/>}
                        {profileOn && <Button style={{float: 'right'}} onClick={onClickSelectImage}>이미지 업로드</Button>}
                        <Card.Meta avatar={<Avatar>{me.name[0]}</Avatar>}
                                   title={me.name}
                        />


                        <Button onClick={clickProfile}>프로필 수정</Button>

                    </Card>
                    {profileOn && uploadImageReady && <Button type="primary" style={{width: '100%'}} onClick={onClickUploadImage}>프로필 이미지 변경</Button>}
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