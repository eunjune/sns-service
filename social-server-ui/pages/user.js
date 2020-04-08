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

const User = ({ id }) => {
    const dispatch = useDispatch();
    const { posts, hasMorePost,loadUserError } = useSelector((state) => state.post);
    const { user } = useSelector((state) => state.user);

    const [profileFollowOn,setProfileFollowOn] = useState(false);
    const [profilePostOn,setProfilePostOn] = useState(true);

    const {followers, followings, hasMoreFollower, hasMoreFollowing} = useSelector(state => state.user);
    const token = cookie.load('token');
    const usedLastIds = useRef([]);

    console.log('user');
    console.log(user);
    console.log(posts);

    useEffect(() => {

      window.addEventListener('scroll', onScroll);
      return () => {
          window.removeEventListener('scroll', onScroll);
      }
    }, [posts]);


    const onScroll = useCallback(() => {

        if(window.scrollY + document.documentElement.clientHeight > document.documentElement.scrollHeight - 200 && hasMorePost) {

            const lastId = posts[posts.length-1].id;
            if(!usedLastIds.current.includes(lastId)) {
                dispatch({
                    type: LOAD_MAIN_POSTS_REQUEST,
                    token: token,
                    lastId: lastId
                });
                usedLastIds.current.push(lastId);
            }
        }

    }, [posts.length, hasMorePost]);

    const clickPost = useCallback(() => {
        setProfilePostOn(true);
        setProfileFollowOn(false);
    },[]);

    const clickFollow = useCallback(() => {
        setProfilePostOn(false);
        setProfileFollowOn(true);
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
    return (
      <Row gutter={8}>
          <Col xs={24} md={8}>
              <div style={{padding: 50}}>
                  <Card
                      actions={[
                          <div onClick={clickPost}>게시글<br/>{user && user.postCount}</div>,
                          <div onClick={clickFollow}>팔로윙<br/>{user && user.followingCount}</div>,
                          <div onClick={clickFollow}>팔로워<br/>{user && user.followerCount}</div>
                      ]}
                      cover={<img src={user && user.profileImageUrl ? `http://localhost:8080/image/profile/${user.profileImageUrl}` :
                          'http://localhost:8080/image/profile/default-user.png'} alt="프로필 사진" style={{padding: 50}}/>}
                  >
                      <Card.Meta avatar={<AvartarCustom shape={"circle"} size={"default"} profileImageUrl={user.profileImageUrl} name={user.name} />}
                                 title={user.name}/>
                  </Card>
              </div>
          </Col>
          {
              loadUserError && loadUserError.status === 403 ?
              <PrivateUserAccess /> :

              <>
              <Col xs={24} md={8}>

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
                          onClickStop={null}
                          data={followings}
                      />

                      <FollowList
                          header="팔로워 목록"
                          hasMore={hasMoreFollower}
                          onClickMore={loadMoreFollowers}
                          onClickStop={null}
                          data={followers}
                      />

                  </div>
                  }
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
    const token = cookie.load('token') || (context.isServer ? context.req.headers.cookie.replace(/(.+)(token=)(.+)/,"$3") : '');


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
        lastId: 0
    },
  });

  return { id: parseInt(context.query.id) }; // 프론트의 props에 전달 가능.
};

export default User;