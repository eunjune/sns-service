import React, {useEffect} from 'react';
import PropTypes from 'prop-types';
import { LOAD_HASHTAG_POSTS_REQUEST } from '../reducers/post';
import { useDispatch, useSelector } from 'react-redux';
import PostCards from '../components/PostCards';

const Hashtag = ({tag}) => {
  const dispatch = useDispatch();
  const {posts} = useSelector(state => state.post);

  useEffect(() => {
    const token = sessionStorage.getItem("token");

    dispatch({
      type: LOAD_HASHTAG_POSTS_REQUEST,
      data: {
        tag: tag,
        token: token,
      },
    });
  },[]);

  return (
    <div>
      {posts.map(p => (
        <PostCards key={+p.id} post={p}/>
      ))}
    </div>
  );
};

Hashtag.propTypes = {
  tag: PropTypes.string.isRequired,
};

Hashtag.getInitialProps = async(context) => {
  console.log('hashtag', context.query.tag);
  return {tag: context.query.tag};
};

export default Hashtag;
