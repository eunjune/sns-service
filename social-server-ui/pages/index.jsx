import React, {useEffect} from 'react';
import PostCards from "../components/PostCards";
import PostForm from "../components/PostForm";
import {useDispatch, useSelector} from "react-redux";
import Router from 'next/router';
import { LOAD_MAIN_POSTS_REQUEST } from '../reducers/post';

const Home = () => {
    const {me,loginErrorReason} = useSelector(state => state.user);
    const {mainPosts} = useSelector(state => state.post);
    const dispatch = useDispatch();

    useEffect(() => {

        const apiToken = sessionStorage.getItem("apiToken");

        if(me) {
            console.log(me);
            dispatch({
                type: LOAD_MAIN_POSTS_REQUEST,
                userId: me.seq,
                token: apiToken,
            });
        }

    },[me]);

    return (
        <div>
            {me && <PostForm />}
            {mainPosts.map((c) => {
                return <PostCards key={c} post={c}/>;
            })}
        </div>
    );
};

export default Home;
