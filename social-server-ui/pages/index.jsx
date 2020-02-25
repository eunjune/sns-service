import React, {useEffect} from 'react';
import PostCards from "../components/PostCards";
import PostForm from "../components/PostForm";
import {useDispatch, useSelector} from "react-redux";
import {loginAction, logoutAction} from "../reducers/user";

const Home = () => {
    const {user, isLogin} = useSelector(state => state.user);
    const {mainPosts} = useSelector(state => state.post);

    return (
        <div>
            {user ? <div>로그인 했습니다. {user.name}</div> : <div>로그아웃 했습니다.</div>}
            {isLogin && <PostForm />}
            {mainPosts.map((c) => {
                return <PostCards key={c} post={c}/>;
            })}
        </div>
    );
};

export default Home;