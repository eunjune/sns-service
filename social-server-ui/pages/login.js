import React, {useCallback, useEffect, useState} from 'react';
import {useDispatch, useSelector} from "react-redux";
import {LOG_IN_REQUEST} from "../reducers/user";
import {Button, Form, Input} from "antd";
import Link from "next/link";
import Router from "next/router";

const Login = () => {
    const {me} = useSelector(state=>state.user);
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const {isLoggingIn} = useSelector(state => state.user);
    const dispatch = useDispatch();

    useEffect(() => {
        if(me) {
            alert('로그인이 되어있습니다.');
            Router.push('/');
        }
    },[me]);

    const onSubmitForm = useCallback((e) => {
        e.preventDefault();
        dispatch({
            type: LOG_IN_REQUEST,
            data: {
                address: email,
                password: password,
            }
        });
        Router.push('/');
    }, [email, password]);

    const onChangeEmail = (e) => {
        setEmail(e.target.value);
    };

    const onChangePassword = (e) => {
        setPassword(e.target.value);
    };

    return (
        <Form onSubmit={onSubmitForm} style={{padding: '10px'}}>
            <div>
                <label htmlFor="user-email">이메일</label>
                <br/>
                <Input name="user-email" value={email} onChange={onChangeEmail} required/>
            </div>
            <div>
                <label htmlFor="user-password">비밀번호</label>
                <br/>
                <Input name="user-password" type="password" value={password} required
                       onChange={onChangePassword}/>
            </div>
            <div style={{marginTop: '10px'}}>
                <Button type="primary" htmlType="submit" loading={isLoggingIn}>로그인</Button>
                <Link href="/signup"><a><Button>회원가입</Button></a></Link>
            </div>
        </Form>
    );
};

export default Login;