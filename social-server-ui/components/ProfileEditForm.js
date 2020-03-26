import React, {useState, useCallback, useEffect} from 'react';
import {Button, Card, Form, Input} from "antd";
import { useDispatch, useSelector } from 'react-redux';
import {EDIT_PROFILE_REQUEST} from '../reducers/user';
import cookie from "react-cookies";

const ProfileEditForm = ({me}) => {
    const [editName, setEditName] = useState(me && me.name);
    const [editPassword, setEditPassword] = useState('');
    const [editPasswordCheck, setEditPasswordCheck] = useState('');
    const [editPasswordError, setEditPasswordError] = useState(false);

    const dispatch = useDispatch();
    const {isEditing, editErrorReason} = useSelector(state => state.user);
    const token = cookie.load('token');


    const onSubmit = useCallback((e) => {
        e.preventDefault();

        if (editPassword !== editPasswordCheck) {
            return setEditPasswordError(true);
        }

        dispatch({
            type: EDIT_PROFILE_REQUEST,
            data: {
                name: editName,
                password: editPassword,
                token: token,
            },
        });
        setEditPassword('');
        setEditPasswordCheck('');
    }, [editName,editPassword, editPasswordCheck]);

    const onChangeName = useCallback((e) => {
        setEditName(e.target.value);
    }, []);

    const onChangePassword = useCallback((e) => {
        setEditPassword(e.target.value);
    }, []);

    const onChangePasswordCheck = useCallback((e) => {
        setEditPasswordError(e.target.value !== editPassword);
        setEditPasswordCheck(e.target.value);
    }, [editPassword]);

    return (
        <Form onSubmit={onSubmit} style={{padding: 50}}>

            <Form.Item
                label='이메일'
                rules={[
                    {
                        required: true,
                        message: '이메일을 입력해주세요!',
                    }
                ]}>
                <div>{me && me.email}</div>
            </Form.Item>

            <Form.Item
                label='이름'
                rules={[
                    {
                        required: true,
                        message: '이름을 입력해주세요!',
                    }
                ]}>
                <Input name="user-name" value={editName} required onChange={onChangeName} />
                {editErrorReason.length > 0 ? <div style={{color: 'red'}}>{editErrorReason}</div> : null}
            </Form.Item>

            <Form.Item
                label='비밀번호'
                rules={[
                    {
                        required: true,
                        message: '비밀번호를 입력해주세요!',
                    }
                ]}>
                <Input name="user-password" value={editPassword} type="password" required onChange={onChangePassword}/>
            </Form.Item>

            <Form.Item
                label='비밀번호 확인'
                rules={[
                    {
                        required: true,
                        message: '비밀번호를 입력해주세요!',
                    }
                ]}>
                <Input name="user-password-check" value={editPasswordCheck} type="password" required onChange={onChangePasswordCheck}/>
                {editPasswordError && <div style={{color: 'red'}}>비밀번호가 일치하지 않습니다.</div>}
            </Form.Item>

            <Form.Item>
                <Button type="primary" htmlType="submit" loading={isEditing} style={{width: '100%'}}>수정</Button>
                {editErrorReason.length > 0 ? <div style={{color: 'red'}}>{editErrorReason}</div> : null}
            </Form.Item>
        </Form>


    );
};

export default ProfileEditForm;