import React, {useEffect} from 'react';
import PostCards from "../components/PostCards";
import PostForm from "../components/PostForm";
import {useDispatch, useSelector} from "react-redux";
import Router from 'next/router';

const Home = () => {
    const {me,loginErrorReason} = useSelector(state => state.user);
    const {mainPosts} = useSelector(state => state.post);

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
