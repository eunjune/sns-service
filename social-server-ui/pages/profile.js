import React, { useEffect, useCallback, useState } from 'react';
import cookie from 'react-cookies';
import {Button, List, Card, Icon} from 'antd';
import NameEditForm from "../components/NameEditForm";
import { useDispatch, useSelector } from 'react-redux';
import { LOAD_FOLLOWER_REQUEST, LOAD_FOLLOWING_REQUEST, UNFOLLOW_USER_REQUEST, REMOVE_FOLLOWER_REQUEST } from '../reducers/user';
import { LOAD_USER_POSTS_REQUEST, LOAD_MAIN_POSTS_REQUEST } from '../reducers/post';
import PostCards from '../components/PostCards';
import FollowList from "../components/FollowList";
import CenterAlignment from "../components/CenterAlignment";

const Profile = () => {

    const dispatch = useDispatch();
    const {followers, followings, hasMoreFollower, hasMoreFollowing} = useSelector(state => state.user);
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
    },[followings])

    const loadMoreFollowers = useCallback(() => {
        dispatch({
            type: LOAD_FOLLOWER_REQUEST,
            data: {
                token,
                offset: followers.length/3
            }
        })
    },[followers])

    return (
            <CenterAlignment>
                <div>
                    <NameEditForm/>
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
                    <div>
                        {posts.map((p) => (
                            <PostCards key={+p.id} post={p} />
                        ))}
                    </div>
                </div>

            </CenterAlignment>
    );

};

Profile.getInitialProps = async(context) => {
    const token = cookie.load('token') || (context.isServer ? context.req.headers.cookie.replace(/(.+)(token=)(.+)/,"$3") : '');

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

export default Profile;