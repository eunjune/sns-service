import React, { useEffect } from 'react';
import PropTypes from 'prop-types';
import { useDispatch, useSelector } from 'react-redux';
import { LOAD_HASHTAG_POSTS_REQUEST } from '../reducers/post';
import PostCards from '../components/PostCards';

const Hashtag = ({ tag }) => {
  const dispatch = useDispatch();
  const { posts } = useSelector((state) => state.post);

  return (
    <div>
      {posts.map((p) => (
        <PostCards key={+p.id} post={p} />
      ))}
    </div>
  );
};

Hashtag.propTypes = {
  tag: PropTypes.string.isRequired,
};

Hashtag.getInitialProps = async (context) => {
  const tag = context.query.tag;
  console.log('tag');
  console.log(tag);
  context.store.dispatch({
    type: LOAD_HASHTAG_POSTS_REQUEST,
    data: tag,
  });

  return { tag: context.query.tag };
};

export default Hashtag;
