import React, {useEffect} from 'react';
import PostCards from "../components/PostCards";
import PostForm from "../components/PostForm";
import {useDispatch, useSelector} from "react-redux";
import Router from 'next/router';
import { LOAD_MAIN_POSTS_REQUEST } from '../reducers/post';

const Home = () => {
    const {me,loginErrorReason} = useSelector(state => state.user);
    const {posts} = useSelector(state => state.post);

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
