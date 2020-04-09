import React, { useEffect,useCallback } from 'react';
import PropTypes from 'prop-types';
import { useDispatch, useSelector } from 'react-redux';
import {LOAD_HASHTAG_POSTS_REQUEST, SIZE} from '../reducers/post';
import PostCards from '../components/post/PostCards';
import CenterAlignment from "../components/CenterAlignment";
import {Alert} from "antd";
import PostForm from "../components/post/PostForm";

const Hashtag = ({ tag }) => {
  const dispatch = useDispatch();
  const { posts, hasMorePost} = useSelector((state) => state.post);

  const onScroll = useCallback(() => {
        console.log(hasMorePost);
      if(window.scrollY + document.documentElement.clientHeight > document.documentElement.scrollHeight - 200 && hasMorePost) {

          dispatch({
              type: LOAD_HASHTAG_POSTS_REQUEST,
              data : {
                tag,
                lastId: posts[posts.length-1] && posts[posts.length-1].id,
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
    <CenterAlignment children={<div className='index' style={{padding: 50}}>
        {posts.map((post) => {
            return <PostCards key={+post.id} post={post}/>;
        })}
    </div>}/>
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
