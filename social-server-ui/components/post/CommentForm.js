import {Button, Form, Input} from "antd";
import React, {useCallback, useEffect, useState} from "react";
import {ADD_COMMENT_REQUEST} from "../../reducers/post";
import {useDispatch, useSelector} from "react-redux";
import cookie from "react-cookies";
import PropTypes from 'prop-types';

const CommentForm = ({post}) => {
    const { isAddingComment,addedComment } = useSelector(state => state.post);
    const [commentText, setCommentText] = useState('');
    const { me } = useSelector(state => state.user);
    const token = cookie.load('token');
    const dispatch = useDispatch();

    useEffect(() => {
        setCommentText('');
    }, [addedComment === true]);

    const onChangeCommentText = useCallback((e) => {
        setCommentText(e.target.value);
    },[]);

    const onSubmitComment = useCallback((e) => {
        e.preventDefault();
        if(!me) {
            alert('로그인이 필요합니다.');
        }
        dispatch({
            type: ADD_COMMENT_REQUEST,
            data: {
                userId: me.id,
                postId: post.id,
                comment: commentText,
                token,
            }
        })
    },[me && me.id, commentText]);

    return (
        <Form onSubmit={onSubmitComment}>
            <Form.Item>
                <Input.TextArea rows={4} value={commentText} onChange={onChangeCommentText} />
            </Form.Item>
            <Button type="primary" htmlType="submit" loading={isAddingComment}>댓글작성</Button>
        </Form>
    );
};

CommentForm.propTypes = {
    post: PropTypes.object.isRequired
};

export default CommentForm;