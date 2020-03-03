import React, { useEffect } from 'react';
import PropTypes from 'prop-types';
import { useDispatch, useSelector } from 'react-redux';
import { LOAD_USER_POSTS_REQUEST } from '../reducers/post';
import PostCards from '../components/PostCards';
import { Avatar, Card } from 'antd';
import { LOAD_USER_REQUEST } from '../reducers/user';

const User = ({id}) => {
  const dispatch = useDispatch();
  const {mainPosts} = useSelector(state => state.post);
  const {userInfo} = useSelector(state => state.user);

  useEffect(() => {
    const apiToken = sessionStorage.getItem("apiToken");

    dispatch({
      type: LOAD_USER_REQUEST,
      data: {
        userId : id,
      },
    });

    dispatch({
      type: LOAD_USER_POSTS_REQUEST,
      data: {
        userId : id,
        token: apiToken,
      },
    });
  },[]);

  return (
    <div>
      {userInfo
        ? <Card
          actions={[
            <div key="twit">짹짹<br/></div>,/*{me.posts.length}*/
            <div key="following">팔로윙<br/></div>,
            <div key="twit">팔로워<br/></div>,
          ]}
        >
          <Card.Meta
            avatar={<Avatar>{userInfo.name[0]}</Avatar>}
            title={userInfo.name}
          />

        </Card>
        : null}
      {mainPosts.map(p => (
        <PostCards key={+p.seq} post={p}/>
      ))}
    </div>
  );
};

User.propTypes = {
  id: PropTypes.number.isRequired,
};

User.getInitialProps = async(context) => {
  console.log('hashtag', context.query.id);
  return {id: parseInt(context.query.id)} // 프론트의 props에 전달 가능.
};

export default User;
