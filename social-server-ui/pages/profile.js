import React, {useEffect, useCallback, useState, useRef} from 'react';
import cookie from 'react-cookies';
import {Button, Card, Col, Row} from 'antd';
import { useDispatch, useSelector } from 'react-redux';
import {
    LOAD_FOLLOWER_REQUEST,
    LOAD_FOLLOWING_REQUEST,
    UNFOLLOW_USER_REQUEST,
    REMOVE_FOLLOWER_REQUEST,
    UPLOAD_IMAGE_REQUEST
} from '../reducers/user';
import {
    LOAD_MY_POSTS_REQUEST
} from '../reducers/post';
import PostCards from '../components/post/PostCards';
import FollowList from "../components/profile/FollowList";
import Router from "next/router";
import ProfileEditForm from "../components/profile/ProfileEditForm";
import AvartarCustom from "../components/profile/AvartarCustom";
import PictureTwoTone from "@ant-design/icons/lib/icons/PictureTwoTone";
import PictureOutlined from "@ant-design/icons/lib/icons/PictureOutlined";
import FileImageOutlined from "@ant-design/icons/lib/icons/FileImageOutlined";
import {baseUrl} from "../config/config";


const Profile = () => {

    const [profileOn,setProfileOn] = useState(true);
    const [profileFollowOn,setProfileFollowOn] = useState(false);
    const [profilePostOn,setProfilePostOn] = useState(false);
    const [uploadImageReady,setUploadImageReady] = useState(false);

    const dispatch = useDispatch();
    const updateProfileImage = useRef();
    const imageInput = useRef();
    const {me,followers, followings, hasMoreFollower, hasMoreFollowing} = useSelector(state => state.user);
    const {posts, hasMorePost} = useSelector(state => state.post);
    const token = cookie.load('token');
    const usedLastIds = useRef([]);
    let uploadImageFile = null;

    useEffect(() => {
        if(!me) {
            Router.push("/");
            return;
        }

        if(me && me.isEmailCertification === false) {
            alert('이메일 인증 후에 이용할 수 있습니다.');
            Router.push("/");
            return;
        }

        window.addEventListener('scroll', onScroll);

        return () => {
            window.removeEventListener('scroll', onScroll);
        }

    },[me,posts,hasMorePost]);

    const onScroll = useCallback(() => {

        if(window.scrollY + document.documentElement.clientHeight > document.documentElement.scrollHeight - 200 && hasMorePost) {
            const lastId = posts[posts.length-1].id;
            if(!usedLastIds.current.includes(lastId)) {
                dispatch({
                    type: LOAD_MY_POSTS_REQUEST,
                    data : {
                        lastId: lastId,
                        token: token,
                    }
                });
                usedLastIds.current.push(lastId);
            }
        }

    }, [posts, hasMorePost]);

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
        if(followings) {

            dispatch({
                type: LOAD_FOLLOWING_REQUEST,
                data: {
                    token,
                    offset: followings.length/3

                }
            });
        }

    },[followings]);

    const loadMoreFollowers = useCallback(() => {
        if(followers) {
            dispatch({
                type: LOAD_FOLLOWER_REQUEST,
                data: {
                    token,
                    offset: followers.length/3
                }
            });
        }

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

        uploadImageFile = file;
        updateProfileImage.current.src = window.URL.createObjectURL(file);
        setUploadImageReady(true);
    }, [uploadImageFile,uploadImageReady]);

    const onClickSelectImage = useCallback(() => {
        imageInput.current.click();
    },[imageInput.current]);

    const onClickUploadImage = useCallback(() => {

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
                                <div onClick={clickPost}>게시글<br/>{me && me.postCount}</div>,
                                <div onClick={clickFollow}>팔로윙<br/>{me && me.followings.length}</div>,
                                <div onClick={clickFollow}>팔로워<br/>{me && me.followers.length}</div>
                            ]}
                            cover={<img ref={updateProfileImage} src={me.profileImageUrl} alt="프로필 사진" style={{padding: 50}}/>}
                        >
                            {profileOn && <input type="file" multiple hidden ref={imageInput} onChange={onChangeImages}/>}
                            <span style={{float: 'right' ,height: '100%'}}>{profileOn && <FileImageOutlined  onClick={onClickSelectImage}/>}</span>
                            <Card.Meta avatar={<AvartarCustom shape={"circle"} size={"default"} profileImageUrl={me && me.profileImageUrl} name={me && me.name} />}
                                       title={me &&
                                            <div>
                                                <span style={{cursor: 'pointer'}}>{me.name}</span>

                                            </div>}
                                       onClick={clickProfile}/>


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
    const token = cookie.load('token') ||
        (context.isServer && context.req.headers.cookie && context.req.headers.cookie.replace(/(token=)(.+)/,"$2"));

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
            type: LOAD_MY_POSTS_REQUEST,
            data: {
                lastId: 0,
                token: token,
            }
        });
    }

};

export default Profile;