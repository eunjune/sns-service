import React, {useEffect} from 'react';
import PostCards from "../components/PostCards";
import PostForm from "../components/PostForm";
import {useDispatch, useSelector} from "react-redux";
import Router from 'next/router';
import { LOAD_MAIN_POSTS_REQUEST } from '../reducers/post';

const Home = () => {
    const {me,loginErrorReason} = useSelector(state => state.user);
    const {posts} = useSelector(state => state.post);
    const dispatch = useDispatch();

    useEffect(() => {

        const token = sessionStorage.getItem("token");

        if(me) {
            console.log(me);
            dispatch({
                type: LOAD_MAIN_POSTS_REQUEST,
                data : {
                    userId: me.id,
                    token: token,
                }
            });
        }

    },[me]);

    return (
        <div>
            {me && <PostForm />}
            {posts.map((post) => {
                return <PostCards key={post.id} post={post}/>;
            })}
        </div>
    );
};

export default Home;
