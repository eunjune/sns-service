import React from 'react';
import {useState, useCallback} from 'react';
import AppLayout from '../components/AppLayout'
import Head from "next/head";
import {Form, Button, Input, Checkbox} from 'antd';
import {signUpAction} from "../reducers/user";
import {useDispatch} from "react-redux";

const Signup = () => {

    const [id, setId] = useState('');
    const [name, setName] = useState('');
    const [password, setPassword] = useState('');
    const [passwordCheck, setPasswordCheck] = useState('');
    const [term, setTerm] = useState(true);
    const [passwordError, setPasswordError] = useState(false);
    const [termError, setTermError] = useState(false);

    const dispatch = useDispatch();

    const onSubmit = useCallback((e) => {
        e.preventDefault();

        if(password !== passwordCheck) {
            return setPasswordError(true);
        }

        if(!term) {
            return setTermError(true);
        }

        dispatch(signUpAction({
            id,
            password,
            name,
        }));
    },[password, passwordCheck, term]);

    const onChangeId = useCallback((e) => {
        setId(e.target.value);
    },[]);

    const onChangeName = useCallback((e) =>{
        setName(e.target.value);
    },[]);

    const onChangePassword = useCallback((e) =>{
        setPassword(e.target.value);
    },[]);

    const onChangePasswordCheck = useCallback((e) => {
        setPasswordError(e.target.value !== password);
        setPasswordCheck(e.target.value);
    },[password]);

    const onChangeTerm = useCallback((e) => {
        setTermError(false);
        setTerm(e.target.value);
    },[]);

    return <>
        <Form onSubmit={onSubmit} style={{padding: 10}}>
            <div>
                <label htmlFor="user-id">아이디</label>
                <br/>
                <Input name="user-id" value={id} required onChange={onChangeId}/>
            </div>
            <div>
                <label htmlFor="user-name">닉네임</label>
                <br/>
                <Input name="user-name" value={name} required onChange={onChangeName}/>
            </div>
            <div>
                <label htmlFor="user-password">비밀번호</label>
                <br/>
                <Input name="user-password" value={password} type="password" required onChange={onChangePassword}/>
            </div>
            <div>
                <label htmlFor="user-password-check">비밀번호 체크</label>
                <br/>
                <Input name="user-password-check" value={passwordCheck} type="password" required onChange={onChangePasswordCheck}/>
                {passwordError && <div style={{color: 'red'}}>비밀번호가 일치하지 않습니다.</div>}
            </div>
            <div>
                <Checkbox name="user-term" value={term} onChange={onChangeTerm}>약관 동의</Checkbox>
                {termError && <div style={{color: 'red'}}>약관에 동의하셔야 합니다.</div>}
            </div>
            <div style={{marginTop: 10}}>
                <Button type="primary" htmlType="submit">가입하기</Button>
            </div>
        </Form>
    </>
};

export default Signup;