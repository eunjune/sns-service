import React, {useEffect,useCallback,useRef} from 'react';
import PostCards from "../components/PostCards";
import PostForm from "../components/PostForm";
import {useDispatch, useSelector} from "react-redux";
import { LOAD_MAIN_POSTS_REQUEST } from '../reducers/post';
import Login from "./login";
import CenterAlignment from "../components/CenterAlignment";
import {Alert} from "antd";
import cookie from "react-cookies";
import {EMAIL_RESEND_REQUEST} from "../reducers/user";

const Home = () => {
    const {me} = useSelector(state => state.user);
    const {posts,hasMorePost} = useSelector(state => state.post);
    const dispatch = useDispatch();
    const usedLastIds = useRef([]);
    const token = cookie.load('token');

    console.log(me);
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

    const onClickResendEmail = useCallback(() => {
        dispatch({
            type: EMAIL_RESEND_REQUEST,
            data: token
        });
    },[]);

    return (
        <CenterAlignment children={
            <div className='index' style={{padding: 50}}>
                {me && me.isEmailCertification === false ?
                <div style={{paddingBottom: 40}}>
                    <Alert message='서비스를 이용하려면 계정 인증 이메일을 확인하세요.' type="warning" /> <span><a onClick={onClickResendEmail}>인증 이메일 재전송</a></span>
                </div> : null}
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
