import React, { useCallback, useEffect, useState } from 'react';
import {Input, Button, Form} from 'antd';
import { useDispatch, useSelector } from 'react-redux';
import { ADD_POST_REQUEST } from '../reducers/post';

const PostForm = () => {
  const [text, setText] = useState('');
  const dispatch = useDispatch();
  const { imagePaths, isAddingPost, addedPost} = useSelector(state => state.post);

  useEffect(() => {
    setText('');
  },[addedPost == true]);

  const onSubmitForm = useCallback((e) => {
    e.preventDefault();

    dispatch({
      type: ADD_POST_REQUEST,
      data: {
        text,
      }
    });
  },[]);

  const onChangeText = useCallback((e) => {
    setText(e.target.value)
  }, []);

  return (
        <Form onSubmit={onSubmitForm} encType="multipart/form-data" style={{margin: '10px 0 20px'}}>
            <Input.TextArea maxLength={140} placeholder="어떤 일이 있었나요?" value={text} onChange={onChangeText}/>
            <div>
                <input type="file" multiple hidden/>
                <Button>이미지 업로드</Button>
                <Button type="primary" style={{float: 'right'}} htmlType="submit" loading={isAddingPost}>글 작성</Button>
            </div>
            <div>
                {imagePaths.map((v,i) => {
                    return (
                        <div key={v} style={{display: 'inline-block'}}>
                            <img src={'http://localhost:3065/' + v}  style={{width: '200px'}} alt={v}/>
                            <div>
                                <Button>제거</Button>
                            </div>
                        </div>
                    );
                })}
            </div>
        </Form>
    );
};

export default PostForm;
