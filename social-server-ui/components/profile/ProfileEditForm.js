import React, {useState, useCallback, useEffect} from 'react';
import {Button, Form, Input, Radio} from "antd";
import {useDispatch, useSelector} from 'react-redux';
import {EDIT_PROFILE_REQUEST} from '../../reducers/user';
import cookie from "react-cookies";

const ProfileEditForm = ({me}) => {
    const [editName, setEditName] = useState(me && me.name);
    const [editPassword, setEditPassword] = useState('');
    const [editPasswordCheck, setEditPasswordCheck] = useState('');
    const [editPasswordError, setEditPasswordError] = useState(false);
    const [editPrivate, setEditPrivate] = useState(me && me.isPrivate);

    const dispatch = useDispatch();
    const {isEditing, editError} = useSelector(state => state.user);
    const token = cookie.load('token');

    const onSubmit = useCallback((e) => {
        e.preventDefault();

        if (editPassword !== editPasswordCheck) {
            return setEditPasswordError(true);
        }

        dispatch({
            type: EDIT_PROFILE_REQUEST,
            data: {
                profileRequest: {
                    name: editName,
                    password: editPassword,
                    isPrivate: !editPrivate,
                },
                token: token,
            },
        });
        setEditPassword('');
        setEditPasswordCheck('');
    }, [editName, editPassword, editPasswordCheck]);

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

    const onChangeRadio = useCallback((e) => {
        setEditPrivate(!editPrivate);
    }, [editPrivate]);

    return (
        <Form onSubmit={onSubmit} style={{padding: 50}}>

            <Form.Item
                label='이메일'>
                <div>{me && me.email.address}</div>
            </Form.Item>

            <Form.Item
                label='이름(닉네임)'
                rules={[
                    {
                        required: false,
                        message: '이름(닉네임)을 입력해주세요!',
                    }
                ]}>
                <Input name="user-name" value={editName} required onChange={onChangeName} pattern="^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$"/>
            </Form.Item>

            <Form.Item
                label='비밀번호'>
                <Input name="user-password" value={editPassword} type="password" onChange={onChangePassword} pattern="^.{8,50}$"/>
            </Form.Item>

            <Form.Item
                label='비밀번호 확인'
            >
                <Input name="user-password-check" value={editPasswordCheck} type="password" required onChange={onChangePasswordCheck}/>
                {editPasswordError && <div style={{color: 'red'}}>비밀번호가 일치하지 않습니다.</div>}
            </Form.Item>

            <Form.Item
                label='계정 설정'
                rules={[
                    {
                        required: true,
                    }
                ]}>

                <Radio.Group onChange={onChangeRadio} value={editPrivate}>
                    <Radio value={true}>비공개</Radio>
                    <Radio value={false}>공개</Radio>
                </Radio.Group>

            </Form.Item>

            <Form.Item>
                <Button type="primary" htmlType="submit" loading={isEditing} style={{width: '100%'}}>수정</Button>
                {editError && <div style={{color: 'red'}}>{editError.message}</div>}
            </Form.Item>
        </Form>


    );
};

export default ProfileEditForm;