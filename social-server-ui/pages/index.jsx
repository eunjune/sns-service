import React, {useEffect} from 'react';
import PostCards from "../components/PostCards";
import PostForm from "../components/PostForm";
import {useDispatch, useSelector} from "react-redux";

const Home = () => {
    const {me, isLogin} = useSelector(state => state.user);
    const {mainPosts} = useSelector(state => state.post);

    return (
        <div>
            {isLogin && <PostForm />}
            {mainPosts.map((c) => {
                return <PostCards key={c} post={c}/>;
            })}
        </div>
    );
};

export default Home;
