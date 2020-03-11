import React, {useEffect,useCallback} from 'react';
import PostCards from "../components/PostCards";
import PostForm from "../components/PostForm";
import {useDispatch, useSelector} from "react-redux";
import Router from 'next/router';
import { LOAD_MAIN_POSTS_REQUEST } from '../reducers/post';

const Home = () => {
    const {me,loginErrorReason} = useSelector(state => state.user);
    const {posts,hasMorePost} = useSelector(state => state.post);
    const dispatch = useDispatch();


    const onScroll = useCallback(() => {
        
        if(window.scrollY + document.documentElement.clientHeight > document.documentElement.scrollHeight - 200 && hasMorePost) {
            console.log('onScroll');
            console.log(posts[posts.length-1].id);

            dispatch({
                type: LOAD_MAIN_POSTS_REQUEST,
                lastId: posts[posts.length-1].id,
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
            {me && <PostForm />}
            {posts.map((post) => {
                return <PostCards key={post.id} post={post}/>;
            })}
        </div>
    );
};

Home.getInitialProps = async (context) => {

    context.store.dispatch({
        type: LOAD_MAIN_POSTS_REQUEST,
    });

};

export default Home;
