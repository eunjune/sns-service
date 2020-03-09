import React, { useEffect, useCallback } from 'react';
import {Button, List, Card, Icon} from 'antd';
import NameForm from "../components/NameForm";
import { useDispatch, useSelector } from 'react-redux';
import { LOAD_FOLLOWER_REQUEST, LOAD_FOLLOWING_REQUEST, UNFOLLOW_USER_REQUEST, REMOVE_FOLLOWER_REQUEST } from '../reducers/user';
import { LOAD_USER_POSTS_REQUEST } from '../reducers/post';
import PostCards from '../components/PostCards';

const Profile = () => {

    const dispatch = useDispatch();
    const {me, followers, followings} = useSelector(state => state.user);
    const {posts} = useSelector(state => state.post);

    useEffect(() => {
        if(me) {
            const token = sessionStorage.getItem("token");

            dispatch({
                type: LOAD_FOLLOWER_REQUEST,
                data: token,
            });
    
            dispatch({
                type: LOAD_FOLLOWING_REQUEST,
                data: token,
            });
    
            dispatch({
                type: LOAD_USER_POSTS_REQUEST,
                data: {
                    userId: me.id,
                    token: token,
                }
            });
        }
        
    }, [me && me.id]);

    const onUnfollow = useCallback(userId => () => {
        const token = sessionStorage.getItem("token");

        dispatch({
            type: UNFOLLOW_USER_REQUEST,
            data: {
                userId,
                token
            }
        });
    },[]);

    const onRemoveFollower = useCallback(userId => () => {
        const token = sessionStorage.getItem("token");

        dispatch({
            type: REMOVE_FOLLOWER_REQUEST,
            data: {
                userId,
                token
            }
        });
        
    },[]);

    return <>
            <div>
                <NameForm/>
                <List
                    style={{marginBottom: '20px'}}
                    grid={{gutter: 4, xs: 2, md: 3}}
                    size="small"
                    header={<div>팔로잉 목록</div>}
                    loadMore={<Button style={{width: '100%'}}>더 보기</Button>}
                    bordered
                    dataSource={followings}
                    renderItem={item => (
                        <List.Item style={{marginTop: '20px'}}>
                            <Card actions={[<Icon key="stop" type="stop" onClick={onUnfollow(item.id)} />]}>
                                <Card.Meta description={item.name}/>
                            </Card>
                        </List.Item>
                    )}
                />
                <List
                    style={{marginBottom: '20px'}}
                    grid={{gutter: 4, xs: 2, md: 3}}
                    size="small"
                    header={<div>팔로워 목록</div>}
                    loadMore={<Button style={{width: '100%'}}>더 보기</Button>}
                    bordered
                    dataSource={followers}
                    renderItem={item => (
                        <List.Item style={{marginTop: '20px'}}>
                            <Card actions={[<Icon key="stop" type="stop" onClick={onRemoveFollower(item.id)}/>]}>
                                <Card.Meta description={item.name}/>
                            </Card>
                        </List.Item>
                    )}
                />
                <div>
                    {posts.map((p) => (
                        <PostCards key={+p.id} post={p} />
                    ))}
                </div>
            </div>
        </>
};

export default Profile;