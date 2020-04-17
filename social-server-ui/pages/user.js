import React, {useEffect, useCallback, useState, useRef} from 'react';
import cookie from 'react-cookies';
import PropTypes from 'prop-types';
import { useDispatch, useSelector } from 'react-redux';
import {Card, Col, Row,Result} from 'antd';
import {LOAD_MAIN_POSTS_REQUEST, LOAD_USER_POSTS_REQUEST} from '../reducers/post';
import PostCards from '../components/post/PostCards';
import {LOAD_FOLLOWER_REQUEST, LOAD_FOLLOWING_REQUEST, LOAD_USER_REQUEST} from '../reducers/user';
import FollowList from "../components/profile/FollowList";
import AvartarCustom from "../components/profile/AvartarCustom";
import PrivateUserAccess from "../components/error/PrivateUserAccess";
import {baseUrl} from "../config/config";

const User = ({ id }) => {
    const dispatch = useDispatch();
    const { posts, hasMorePost,loadUserPostError } = useSelector((state) => state.post);
    const { user } = useSelector((state) => state.user);
    const token = cookie.load('token');
    const usedLastIds = useRef([]);

    useEffect(() => {
      window.addEventListener('scroll', onScroll);
      return () => {
          window.removeEventListener('scroll', onScroll);
      }
    }, [posts, hasMorePost]);


    const onScroll = useCallback(() => {

        if(window.scrollY + document.documentElement.clientHeight > document.documentElement.scrollHeight - 200 && hasMorePost) {
            const lastId = posts[posts.length-1].id;
            if(!usedLastIds.current.includes(lastId)) {
                dispatch({
                    type: LOAD_USER_POSTS_REQUEST,
                    data : {
                        userId: id,
                        lastId: lastId,
                        token: token,
                    }
                });
                usedLastIds.current.push(lastId);
            }
        }

    }, [posts, hasMorePost]);


    return (
      <Row gutter={8}>
          <Col xs={24} md={8}>
              <div style={{padding: 50}}>
                  <Card
                      actions={[
                          <div>게시글<br/>{user && user.postCount}</div>,
                          <div>팔로윙<br/>{user && user.followingCount}</div>,
                          <div>팔로워<br/>{user && user.followerCount}</div>
                      ]}
                      cover={<img src={user && user.profileImageUrl ? `${baseUrl}/image/profile/${user.profileImageUrl}` :
                          `${baseUrl}/image/profile/default-user.png`} alt="프로필 사진" style={{padding: 50}}/>}
                  >
                      <Card.Meta avatar={<AvartarCustom shape={"circle"} size={"default"} profileImageUrl={user && user.profileImageUrl} name={user && user.name} />}
                                 title={user && user.name}/>
                  </Card>
              </div>
          </Col>
          {
              loadUserPostError && loadUserPostError.status === 403 ?
              <PrivateUserAccess /> :

              <>
              <Col xs={24} md={8}>
                  <div style={{paddingTop: 50}}>
                      {posts.map((p) => (
                          <PostCards key={+p.id} post={p} />
                      ))}
                  </div>
              </Col>
              <Col xs={24} md={8}></Col>
              </>
          }


      </Row>
    );
};

User.propTypes = {
  id: PropTypes.number.isRequired,
};

User.getInitialProps = async (context) => {
    const id = context.query.id;
    const token = cookie.load('token') ||
        (context.isServer && context.req.headers.cookie && context.req.headers.cookie.replace(/(token=)(.+)/,"$2"));


    context.store.dispatch({
        type: LOAD_USER_REQUEST,
        data: {
            userId: id,
            token : token
        },
    });

    context.store.dispatch({
        type: LOAD_USER_POSTS_REQUEST,
        data: {
            userId: id,
            token: token,
        },
    });

  return { id: parseInt(context.query.id) }; // 프론트의 props에 전달 가능.
};

export default User;