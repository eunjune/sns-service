import React, { useEffect } from 'react';
import cookie from 'react-cookies';
import PropTypes from 'prop-types';
import { useDispatch, useSelector } from 'react-redux';
import { Avatar, Card } from 'antd';
import { LOAD_USER_POSTS_REQUEST } from '../reducers/post';
import PostCards from '../components/PostCards';
import { LOAD_USER_REQUEST } from '../reducers/user';

const User = ({ id }) => {
  const dispatch = useDispatch();
  const { posts } = useSelector((state) => state.post);
  const { user } = useSelector((state) => state.user);

 

  return (
    <div>
      {user
        ? (
          <Card
            actions={[
              <div key="twit">
                게시글 수
                <br />
                {posts.length}
              </div>,
              <div key="following">
                팔로윙
                <br />
                {}
              </div>,
              <div key="twit">
                팔로워
                <br />
                {}
              </div>,
            ]}
          >
            <Card.Meta
              avatar={<Avatar>{user.name[0]}</Avatar>}
              title={user.name}
            />

          </Card>
        )
        : null}
      {posts.map((p) => (
        <PostCards key={+p.id} post={p} />
      ))}
    </div>
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
      token : token
    },
  });

  return { id: parseInt(context.query.id) }; // 프론트의 props에 전달 가능.
};

export default User;