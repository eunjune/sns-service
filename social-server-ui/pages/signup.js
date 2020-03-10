import {useState, useCallback, useEffect} from 'react';
import { Form, Button, Input, Checkbox } from 'antd';
import {
    EMAIL_CHECK_REQUEST,
    SIGN_UP_REQUEST,
} from '../reducers/user';
import { useDispatch, useSelector } from 'react-redux';
import Router from 'next/router';

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
    const { isSigningUp, signUpErrorReason } = useSelector((state) => state.user);
    const { me } = useSelector((state) => state.user);

    useEffect(() => {
        if (me) {
            alert('회원가입 성공');
            Router.push('/');
        }
    }, [me && me.id]);

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

    const onClickChecking = useCallback((e) => {
        if (email == null) {
            return;
        }
        dispatch({
            type: EMAIL_CHECK_REQUEST,
            data: {
                address: email,
            },
    });
    }, [email]);

    if(me) {
        return null;
    }

    return (
    <>
        <Form onSubmit={onSubmit} style={{padding: 10}}>
            <div>
                <label htmlFor="user-email">아이디</label>
                <br/>
                <Input name="user-email" value={email} required onChange={onChangeEmail}/>
                <Button onClick={onClickChecking} loading={isEmailChecking}>중복확인</Button>
                {isEmailOk === true ? <div style={{color: 'green'}}>사용 가능한 이메일입니다.</div> :
                (isEmailOk === false ? <div style={{color: 'red'}}>이메일이 중복입니다.</div> : null)}
                {emailCheckingErrorReason !== '' ? <div style={{color: 'red'}}>{emailCheckingErrorReason}</div> : null}
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
                <Button type="primary" htmlType="submit" loading={isSigningUp}>가입하기</Button>
            </div>
        </Form>
    </>
    )
};

export default Signup;
