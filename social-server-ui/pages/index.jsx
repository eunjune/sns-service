import React, {useEffect} from 'react';
import PostCards from "../components/PostCards";
import PostForm from "../components/PostForm";
import {useDispatch, useSelector} from "react-redux";

const Home = () => {
    const {me, isLogin} = useSelector(state => state.user);
    const {mainPosts} = useSelector(state => state.post);

    return (
        <div>
            {me ? <div>로그인 했습니다. {me.name}</div> : <div>로그아웃 했습니다.</div>}
            {isLogin && <PostForm />}
            {mainPosts.map((c) => {
                return <PostCards key={c} post={c}/>;
            })}
        </div>
    );
};

export default Home;
