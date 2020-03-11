import React, { useEffect,useCallback } from 'react';
import PropTypes from 'prop-types';
import { useDispatch, useSelector } from 'react-redux';
import { LOAD_HASHTAG_POSTS_REQUEST } from '../reducers/post';
import PostCards from '../components/PostCards';

const Hashtag = ({ tag }) => {
  const dispatch = useDispatch();
  const { posts, hasMorePost} = useSelector((state) => state.post);

  const onScroll = useCallback(() => {
        
      if(window.scrollY + document.documentElement.clientHeight > document.documentElement.scrollHeight - 200 && hasMorePost) {
          console.log('onScroll');
          console.log(posts[posts.length-1].id);

          dispatch({
              type: LOAD_HASHTAG_POSTS_REQUEST,
              data : {
                tag,
                lastId: posts[posts.length-1].id,
              }
          });
      }
  }, [posts.length, hasMorePost]);

  useEffect(() => {

      window.addEventListener('scroll', onScroll);
      return () => {
          window.removeEventListener('scroll', onScroll);
      }
  }, [posts]);

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
    data: {tag},
  });

  return { tag: context.query.tag };
};

export default Hashtag;
