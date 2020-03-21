import React, {useCallback, useEffect, useState} from 'react';
import {useDispatch, useSelector} from "react-redux";
import {LOG_IN_REQUEST} from "../reducers/user";
import {Button, Checkbox, Col, Form, Input, Row} from "antd";
import Link from "next/link";
import Router from "next/router";
import {DivWrap} from "../components/styles/ContainerStyle";
import { UserOutlined, LockOutlined } from '@ant-design/icons';
import CenterAlignment from "../components/CenterAlignment";

const Login = () => {
    const {me} = useSelector(state=>state.user);
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const {isLoggingIn, loginErrorReason} = useSelector(state => state.user);
    const dispatch = useDispatch();

    useEffect(() => {
        if(me) {
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
    }, [email, password]);

    const onChangeEmail = (e) => {
        setEmail(e.target.value);
    };

    const onChangePassword = (e) => {
        setPassword(e.target.value);
    };

    return (
        <DivWrap>
            <CenterAlignment children={
                <Form onSubmit={onSubmitForm} style={{maxWidth: '500px', padding: 50}}>
                    <Form.Item
                        label='이메일'
                        rules={[
                            {
                                required: true,
                                message: '이메일을 입력해주세요!',
                            }
                        ]}>
                        <Input prefix={<UserOutlined className="site-form-item-icon" />} placeholder="이메일" name="user-email" value={email} onChange={onChangeEmail}/>

                    </Form.Item>

                    <Form.Item
                        label='비밀번호'
                        rules={[
                            {
                                required: true,
                                message: '비밀번호를을 입력해주세요!',
                            }
                        ]}>
                        <Input prefix={<LockOutlined className="site-form-item-icon" />} placeholder="패스워드" name="user-password" type="password" value={password} onChange={onChangePassword}/>
                    </Form.Item>

                    <Form.Item>
                        <Button type="primary" htmlType="submit" loading={isLoggingIn}>로그인</Button>

                        <a className="login-form-forgot" href="" style={{float: 'right'}}>
                            Forgot password
                        </a>

                        {loginErrorReason.length > 0 ? <div style={{color: 'red'}}>{loginErrorReason}</div> : null}
                    </Form.Item>
                </Form>

            } />

        </DivWrap>

    );
};

export default Login;