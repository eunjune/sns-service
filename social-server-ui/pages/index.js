import React, {useEffect, useCallback} from 'react';
import PostCards from "../components/post/PostCards";
import PostForm from "../components/post/PostForm";
import {useDispatch, useSelector} from "react-redux";
import {LOAD_MAIN_POSTS_REQUEST} from '../reducers/post';
import CenterAlignment from "../components/CenterAlignment";
import {Alert} from "antd";
import cookie from "react-cookies";
import {EMAIL_RESEND_REQUEST} from "../reducers/user";

const Home = ({isEmailLogin, usedLastIds}) => {
    const {me, isEmailLogInWaiting} = useSelector(state => state.user);
    const {posts, hasMorePost, retweetError} = useSelector(state => state.post);
    const dispatch = useDispatch();
    const token = cookie.load('token');

    useEffect(() => {

        window.addEventListener('scroll', onScroll);

        return () => {
            window.removeEventListener('scroll', onScroll);
        }
    }, [posts, hasMorePost]);

    const onScroll = useCallback(() => {

        if (window.scrollY + document.documentElement.clientHeight > document.documentElement.scrollHeight - 200 && hasMorePost) {
            const lastId = posts[posts.length - 1].id;

            if (!usedLastIds.includes(lastId)) {
                dispatch({
                    type: LOAD_MAIN_POSTS_REQUEST,
                    data: {
                        lastId,
                        token: !token ? null : token,
                    }
                });
                usedLastIds.push(lastId);
            }
        }

    }, [posts.length, hasMorePost]);

    const onClickResendEmail = useCallback(() => {
        dispatch({
            type: EMAIL_RESEND_REQUEST,
            data: token
        });
    }, []);

    return (
        <CenterAlignment children={
            <div className='index' style={{marginTop: 50}}>
                {
                    me && me.isEmailCertification === false ?
                        <div style={{paddingBottom: 40}}>
                            <Alert message='서비스를 이용하려면 계정 인증 이메일을 확인하세요.' type="warning"/> <span><a
                            onClick={onClickResendEmail}>인증 이메일 재전송</a></span>
                        </div> : null
                }
                {
                    isEmailLogin === false && isEmailLogInWaiting === true ?
                        <div style={{paddingBottom: 40}}>
                            <Alert message='이메일을 확인하여 로그인하세요.' type="warning"/>
                        </div> : null
                }
                {
                    isEmailLogin === true ?
                        <div style={{paddingBottom: 40}}>
                            <Alert message='이메일로 로그인 했습니다. 패스워드를 변경하세요.' type="success"/>
                        </div> : null
                }
                {
                    retweetError && <Alert message={retweetError.message} type="error"/>
                }
                {me && <PostForm/>}
                {posts.map((post) => {
                    return <PostCards key={post.id} post={post}/>;
                })}
            </div>}/>
    );
};

Home.getInitialProps = async (context) => {
    const isEmailLogin = !!context.query.token;
    const token = cookie.load('token') ||
        (context.isServer && context.req.headers.cookie && context.req.headers.cookie.replace(/(token=)(.+)/, "$2"));

    context.store.dispatch({
        type: LOAD_MAIN_POSTS_REQUEST,
        data: {
            lastId: 0,
            token: token
        }
    });

    return {isEmailLogin, usedLastIds: []};
};

export default Home;
