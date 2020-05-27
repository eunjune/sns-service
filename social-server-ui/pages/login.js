import React, {useCallback, useEffect, useMemo, useState} from 'react';
import {useDispatch, useSelector} from "react-redux";
import {EMAIL_LOG_IN_REQUEST, LOG_IN_REQUEST} from "../reducers/user";
import {Button, Form, Input} from "antd";
import Router from "next/router";
import {DivWrap} from "../components/styles/ContainerStyle";
import {UserOutlined, LockOutlined} from '@ant-design/icons';
import CenterAlignment from "../components/CenterAlignment";

const Login = () => {
    const {me} = useSelector(state => state.user);
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [forgotPassword, setForgotPassword] = useState(false);
    const {isLoggingIn, loginError, isEmailLogInWaiting} = useSelector(state => state.user);
    const dispatch = useDispatch();

    useEffect(() => {
        if (me || isEmailLogInWaiting === true) {
            Router.push('/');
        }
    }, [me, isEmailLogInWaiting]);

    const onSubmitForm = useCallback((e) => {
        e.preventDefault();

        console.log('forgotPassword');
        console.log(forgotPassword);
        if (forgotPassword) {
            dispatch({
                type: EMAIL_LOG_IN_REQUEST,
                data: {
                    address: email
                }
            });
        } else {
            dispatch({
                type: LOG_IN_REQUEST,
                data: {
                    address: email,
                    password: password,
                }
            });
        }

    }, [email, password]);

    const onChangeEmail = useCallback((e) => {
        setEmail(e.target.value);
    },[email]);

    const onChangePassword = useCallback((e) => {
        setPassword(e.target.value);
    },[password]);

    const onClickForgotPassword = useCallback((e) => {
        setForgotPassword(true);
        setEmail('');
    }, [forgotPassword, email]);

    return (
        <DivWrap>
            <CenterAlignment children={

                <Form onSubmit={onSubmitForm} style={{maxWidth: '500px',marginTop: 50}}>
                    {forgotPassword === true ? <h1>이메일 로그인</h1> : null}
                    <Form.Item
                        label='이메일'
                        rules={[
                            {
                                required: true,
                                message: '이메일을 입력해주세요!',
                            }
                        ]}>
                        <Input prefix={<UserOutlined className="site-form-item-icon"/>} placeholder="이메일"
                               name="user-email" value={email} onChange={onChangeEmail}/>

                    </Form.Item>

                    {
                        forgotPassword === false ?
                            <Form.Item
                                label='비밀번호'
                                rules={[
                                    {
                                        required: true,
                                        message: '비밀번호를을 입력해주세요!',
                                    }
                                ]}>
                                <Input prefix={<LockOutlined className="site-form-item-icon"/>} placeholder="패스워드"
                                       name="user-password" type="password" value={password}
                                       onChange={onChangePassword}/>
                            </Form.Item>
                            :
                            null
                    }

                    <Form.Item>
                        <Button type="primary" htmlType="submit" loading={isLoggingIn}>로그인</Button>

                        {forgotPassword === true ?
                            null :
                            <a className="login-form-forgot" style={{float: 'right'}} onClick={onClickForgotPassword}>
                                Forgot password
                            </a>
                        }
                        {loginError && <div style={{color: 'red'}}>{loginError.message}</div>}
                    </Form.Item>
                </Form>

            }/>

        </DivWrap>

    );
};

export default Login;