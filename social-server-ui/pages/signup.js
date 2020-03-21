import React, {useState, useCallback, useEffect} from 'react';
import { Form, Button, Input, Checkbox } from 'antd';
import {
    EMAIL_CHECK_REQUEST, NAME_CHECK_REQUEST,
    SIGN_UP_REQUEST,
} from '../reducers/user';
import { useDispatch, useSelector } from 'react-redux';
import Router from 'next/router';
import {DivWrap} from "../components/styles/ContainerStyle";
import CenterAlignment from "../components/CenterAlignment";
import {LockOutlined, UserOutlined} from "@ant-design/icons";

const Signup = () => {

  const [email, setEmail] = useState('');
    const [name, setName] = useState('');
    const [password, setPassword] = useState('');
    const [passwordCheck, setPasswordCheck] = useState('');
    const [term, setTerm] = useState(true);
    const [passwordError, setPasswordError] = useState(false);
    const [termError, setTermError] = useState(false);

  const dispatch = useDispatch();
    const { isEmailOk, isEmailChecking, emailCheckingErrorReason } = useSelector((state) => state.user);
    const { isNameOk, isNameChecking, nameCheckingErrorReason } = useSelector((state) => state.user);
    const { isSigningUp, signUpErrorReason } = useSelector((state) => state.user);
    const { me } = useSelector((state) => state.user);

    useEffect(() => {
        if (me) {
            alert('회원가입 성공');
            Router.push('/');
        }

        if(signUpErrorReason && signUpErrorReason.length() > 0) {
            alert(signUpErrorReason);
        }

    }, [me && me.id, signUpErrorReason]);

    const onSubmit = useCallback((e) => {
        e.preventDefault();

        if (!isEmailOk) {
                return;
            }

        if (password !== passwordCheck) {
                return setPasswordError(true);
            }

        if (!term) {
                return setTermError(true);
            }

        dispatch({
                type: SIGN_UP_REQUEST,
                data: {
                    name,
                    address: email,
                    password,
                },
    });
    }, [password, passwordCheck, term]);

    const onChangeEmail = useCallback((e) => {
        setEmail(e.target.value);
    }, []);

    const onChangeName = useCallback((e) => {
        setName(e.target.value);
    }, []);

    const onChangePassword = useCallback((e) => {
        setPassword(e.target.value);
    }, []);

    const onChangePasswordCheck = useCallback((e) => {
        setPasswordError(e.target.value !== password);
        setPasswordCheck(e.target.value);
    }, [password]);

    const onChangeTerm = useCallback((e) => {
        setTermError(false);
        setTerm(e.target.value);
    }, []);

    const onBlurEmail = useCallback((e) => {
        if (email == null || email === '') {
            return;
        }
        dispatch({
            type: EMAIL_CHECK_REQUEST,
            data: email,
    });
    }, [email]);

    const onBlurName = useCallback((e) => {
        if (name == null || name === '') {
            return;
        }
        dispatch({
            type: NAME_CHECK_REQUEST,
            data: name,
        });
    }, [name]);

    if(me) {
        return null;
    }



    return (
        <DivWrap>
            <CenterAlignment children={
                <Form onSubmit={onSubmit} style={{padding: 50}}>
                    <Form.Item
                        label='이메일'
                        rules={[
                            {
                                required: true,
                                message: '이메일을 입력해주세요!',
                            }
                        ]}>
                        <Input name="user-email" value={email} required onChange={onChangeEmail} onBlur={onBlurEmail}/>
                        {isEmailOk === true ? <div style={{color: 'green'}}>사용 가능한 이메일입니다.</div> :
                            (isEmailOk === false ? <div style={{color: 'red'}}>{emailCheckingErrorReason}</div> : null)}
                    </Form.Item>

                    <Form.Item
                        label='이름'
                        rules={[
                            {
                                required: true,
                                message: '이름을 입력해주세요!',
                            }
                        ]}>
                        <Input name="user-name" value={name} required onChange={onChangeName} onBlur={onBlurName}/>
                        {isNameOk === true ? <div style={{color: 'green'}}>사용 가능한 이름입니다.</div> :
                            (isNameOk === false ? <div style={{color: 'red'}}>{nameCheckingErrorReason}</div> : null)}
                    </Form.Item>

                    <Form.Item
                        label='비밀번호'
                        rules={[
                            {
                                required: true,
                                message: '비밀번호를 입력해주세요!',
                            }
                        ]}>
                        <Input name="user-password" value={password} type="password" required onChange={onChangePassword}/>
                    </Form.Item>

                    <Form.Item
                        label='비밀번호 확인'
                        rules={[
                            {
                                required: true,
                                message: '비밀번호를 입력해주세요!',
                            }
                        ]}>
                        <Input name="user-password-check" value={passwordCheck} type="password" required onChange={onChangePasswordCheck}/>
                        {passwordError && <div style={{color: 'red'}}>비밀번호가 일치하지 않습니다.</div>}
                    </Form.Item>

                    <Form.Item>
                        <Checkbox name="user-term" value={term} onChange={onChangeTerm}>약관 동의</Checkbox>
                        {termError && <div style={{color: 'red'}}>약관에 동의하셔야 합니다.</div>}
                    </Form.Item>

                    <Form.Item>
                        <Button type="primary" htmlType="submit" loading={isSigningUp} style={{width: '100%'}}>가입하기</Button>
                    </Form.Item>
                </Form>
            }/>
        </DivWrap>
    )
};

export default Signup;
