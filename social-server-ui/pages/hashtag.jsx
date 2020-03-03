import React, {useEffect} from 'react';
import PropTypes from 'prop-types';
import { LOAD_HASHTAG_POSTS_REQUEST } from '../reducers/post';
import { useDispatch, useSelector } from 'react-redux';
import PostCards from '../components/PostCards';

const Hashtag = ({tag}) => {
  const dispatch = useDispatch();
  const {mainPosts} = useSelector(state => state.post);

  useEffect(() => {
    const apiToken = sessionStorage.getItem("apiToken");

    dispatch({
      type: LOAD_HASHTAG_POSTS_REQUEST,
      data: {
        tag: tag,
        token: apiToken,
      },
    });
  },[]);

  return (
    <div>
      {mainPosts.map(p => (
        <PostCards key={+p.seq} post={p}/>
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
