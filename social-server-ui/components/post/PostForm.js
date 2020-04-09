import React, { useCallback, useEffect, useRef, useState } from 'react';
import cookie from 'react-cookies';
import {Input, Button, Form} from 'antd';
import { useDispatch, useSelector } from 'react-redux';
import {
  ADD_POST_REQUEST,
  REMOVE_IMAGE,
  UPLOAD_IMAGES_REQUEST
} from '../../reducers/post';

const PostForm = () => {
  const [text, setText] = useState('');
  const dispatch = useDispatch();
  const { imagePaths, isAddingPost, addedPost,addPostError} = useSelector(state => state.post);
  const imageInput = useRef();
  const token = cookie.load('token');

  useEffect(() => {
    setText('');
  },[addedPost === true]);

  const onSubmitForm = useCallback((e) => {
    e.preventDefault();

    /*console.log('submit');
    console.log(me.isEmailCertification);
    if(me && me.isEmailCertification === false) {
      alert('이메일 인증 후에 이용할 수 있습니다.');
      return;
    }*/

    if(addPostError && addPostError.status === 401) {
      alert(addPostError.message);
      return;
    }

    if(!text || !text.trim()) {
      alert('게시글을 작성하세욧.');
      return;
    }

    dispatch({
      type: ADD_POST_REQUEST,
      data: {
        token,
        post: {
          content : text,
          imagePaths: imagePaths,
        },
      }
    });
  },[text, imagePaths,addPostError]);

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

  const onClickImageUpload = useCallback(() => {
    imageInput.current.click();


  },[imageInput.current]);

  const onRemoveImage = useCallback((index) => () => {
    dispatch({
      type: REMOVE_IMAGE,
      index,
    });
  }, []);

  return (
        <Form onSubmit={onSubmitForm} encType="multipart/form-data" style={{margin: '10px 0 100px'}}>
            <Input.TextArea rows={5} maxLength={140} placeholder="어떤 일이 있었나요?" value={text} onChange={onChangeText}/>
            <div>
                <input type="file" multiple hidden ref={imageInput} onChange={onChangeImages}/>
                <Button onClick={onClickImageUpload}>이미지 업로드</Button>
                <Button type="primary" style={{float: 'right'}} htmlType="submit" loading={isAddingPost}>글 작성</Button>
            </div>
            <div>
                {
                  imagePaths.map((v,i) => {
                  return (
                        <div key={v} style={{display: 'inline-block'}}>
                            <img src={`http://localhost:8080/image/${v}`}  style={{width: '200px'}} alt={v}/>
                            <div>
                                <Button onClick={onRemoveImage(i)}>제거</Button>
                            </div>
                        </div>
                  )})
                }
            </div>
        </Form>
    );
};

export default PostForm;
