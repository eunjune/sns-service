import React, {useEffect,useCallback,useRef} from 'react';
import PostCards from "../components/PostCards";
import PostForm from "../components/PostForm";
import {useDispatch, useSelector} from "react-redux";
import { LOAD_MAIN_POSTS_REQUEST } from '../reducers/post';
import Login from "./login";
import CenterAlignment from "../components/CenterAlignment";

const Home = () => {
    const {me,loginErrorReason} = useSelector(state => state.user);
    const {posts,hasMorePost} = useSelector(state => state.post);
    const dispatch = useDispatch();
    const usedLastIds = useRef([]);


    const onScroll = useCallback(() => {
        
        if(window.scrollY + document.documentElement.clientHeight > document.documentElement.scrollHeight - 200 && hasMorePost) {
            console.log('onScroll');
            console.log(posts[posts.length-1].id);
            
            const lastId = posts[posts.length-1].id;
            if(!usedLastIds.current.includes(lastId)) {
                dispatch({
                    type: LOAD_MAIN_POSTS_REQUEST,
                    lastId: lastId
                });
                usedLastIds.current.push(lastId);
            }
        }

    }, [posts.length, hasMorePost]);

    useEffect(() => {

        window.addEventListener('scroll', onScroll);
        return () => {
            window.removeEventListener('scroll', onScroll);
        }
    }, [posts]);

    return (
        <CenterAlignment children={
            <div>
                {me && <PostForm />}
                {posts.map((post) => {
                    return <PostCards key={post.id} post={post}/>;
                })}
            </div>}/>

    );
};

Home.getInitialProps = async (context) => {

    context.store.dispatch({
        type: LOAD_MAIN_POSTS_REQUEST,
    });

};

export default Home;
