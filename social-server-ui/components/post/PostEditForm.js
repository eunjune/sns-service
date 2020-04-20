import React, {useCallback, useEffect, useRef, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import cookie from "react-cookies";
import {
    ADD_POST_REQUEST,
    CANCEL_EDIT_POST,
    EDIT_POST_REQUEST, REMOVE_EDIT_IMAGE,
    REMOVE_IMAGE,
    UPLOAD_IMAGES_REQUEST
} from "../../reducers/post";
import {Button, Form, Input, Modal} from "antd";
import Router from "next/router";
import {baseUrl} from "../../config/config";

const PostEditForm = ({post}) => {
    const [text, setText] = useState(post.content);
    const {isEditingPost, isEditPost, editPostImages} = useSelector(state => state.post);
    const dispatch = useDispatch();
    const imageInput = useRef();
    const token = cookie.load('token');

    useEffect(() => {
        setText(post.content);

    }, [post]);

    const onSubmitForm = useCallback((e) => {
        e.preventDefault();



        if(!text || !text.trim()) {
            alert('게시글을 작성하세욧.');
            return;
        }

        dispatch({
            type: EDIT_POST_REQUEST,
            data: {
                token,
                postId: post.id,
                postRequest: {
                    content : text,
                    imagePaths : editPostImages,
                },
            }
        });
    },[text,editPostImages]);

    const onChangeText = useCallback((e) => {
        setText(e.target.value)
    }, []);

    const onChangeImages = useCallback((e) => {
        const imageFormData = new FormData();

        [].forEach.call(e.target.files, (f) => {
            imageFormData.append('images', f);
        });

        dispatch({
            type: UPLOAD_IMAGES_REQUEST,
            data: {
                imageFormData,
                token,
            },
        });

    }, []);

    const onCancel = useCallback(() => {
        dispatch({
            type: CANCEL_EDIT_POST,
        });
    });

    const onClickImageUpload = useCallback(() => {
        imageInput.current.click();
    },[imageInput.current]);

    const onRemoveImage = useCallback((index) => () => {
        dispatch({
            type: REMOVE_IMAGE,
            index: index
        });
    }, []);

    return (

            <Form onSubmit={onSubmitForm} encType="multipart/form-data" style={{margin: '10px 0 20px'}}>
                <div>
                    <Input.TextArea maxLength={140} value={text} onChange={onChangeText}/>
                    <input type="file" multiple hidden ref={imageInput} onChange={onChangeImages}/>
                    <Button onClick={onClickImageUpload}>이미지 업로드</Button>
                </div>
                <div>
                    {
                        editPostImages.map((v,i) => {
                            return (
                                <div key={v} style={{display: 'inline-block' ,marginTop: '20px'}}>
                                    <img src={`${v}`}  style={{width: '200px'}} alt={v}/>
                                    <div>
                                        <Button onClick={onRemoveImage(i)}>제거</Button>
                                    </div>
                                </div>
                            );
                        })
                    }
                </div>

                <Button type="primary" style={{float: 'right'}} htmlType="submit" loading={isEditingPost}>수정</Button>
                <Button style={{float: 'right', marginRight: 10}} onClick={onCancel}>취소</Button>
            </Form>

    );
};

export default PostEditForm;