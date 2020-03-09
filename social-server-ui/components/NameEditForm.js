import React, { useState, useCallback } from 'react';
import {Button, Form, Input} from "antd";
import { useDispatch, useSelector } from 'react-redux';
import { EDIT_NAME_REQUEST } from '../reducers/user';

const NameEditForm = () => {

    const [editedName, setEditedName] = useState('');
    const dispatch = useDispatch();
    const {me, isEditingName} = useSelector(state => state.user);

    const onChangeName = useCallback((e) => {
        setEditedName(e.target.value)
    },[]);

    const onEditName = useCallback((e) => {
        e.preventDefault();

        const token = sessionStorage.getItem("token");

        dispatch({
            type: EDIT_NAME_REQUEST,
            data: {
                name: editedName,
                token: token,
            }
        });

    }, [editedName]);

    return (
        <Form style={{marginBottom: '20px', border: '1px solid #d9d9d9', padding: '20px'}} onSubmit={onEditName}>
            <Input addonBefore="닉네임" value={editedName} onChange={onChangeName}/>
            <Button type="primary" htmlType="submit" loading={isEditingName}>수정</Button>
        </Form>
    );
};

export default NameEditForm;