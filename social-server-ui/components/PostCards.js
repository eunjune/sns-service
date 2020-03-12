import React, { useState, useCallback, useEffect } from 'react';
import cookie from 'react-cookies';
import PropTypes from 'prop-types';
import { Button, Card, Icon, Avatar, Form, List,Comment,Input,Popover } from 'antd';
import { useSelector, useDispatch } from 'react-redux';
import {ADD_COMMENT_REQUEST, LOAD_COMMENTS_REQUEST, UNLIKE_POST_REQUEST, LIKE_POST_REQUEST, RETWEET_REQUEST, REMOVE_POST_REQUEST} from '../reducers/post';
import {FOLLOW_USER_REQUEST,UNFOLLOW_USER_REQUEST} from '../reducers/user'
import Link from 'next/link';
import PostImages from '../components/PostImages'
import PostCardContent from '../components/PostCardContent'
import styled from 'styled-components';

const CardWrapper = styled.div`
  margin-bottom: 20px;
`;

const PostCards = ({post}) => {
    const [commentFormOpened, setCommentFormOpened] = useState(false);
    const [commentText, setCommentText] = useState('');
    const { me } = useSelector(state => state.user);
    const { addedComment,isAddingComment } = useSelector(state => state.post);
    const dispatch = useDispatch();
    const token = cookie.load('token');

    const liked = post.likes && post.likes.find(v =>v.user.id === me.id);

    useEffect(() => {
      setCommentText('');
    }, [addedComment === true]);

    const onToggleComment = useCallback(() => {
        setCommentFormOpened(prev => !prev);


        if(!commentFormOpened) {
          dispatch({
              type: LOAD_COMMENTS_REQUEST,
              data: {
                  postId: post.id,
                  userId: me.id,
                  token,
              },
          })
        }

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

    const onChangeCommentText = useCallback((e) => {
      setCommentText(e.target.value);
    },[]);

    const onToggleLike = useCallback(() => {

      if(!me) {
        return alert('로그인이 필요합니다.');
      }

      if(liked) {
        return dispatch({
          type: UNLIKE_POST_REQUEST,
          data: {
            userId: post.user.id,
            postId: post.id,
            token
          }
        });
      } else {
        return dispatch({
          type: LIKE_POST_REQUEST,
          data: {
            userId: post.user.id,
            postId: post.id,
            token
          }
        });
      }

    },[me && me.id, post]);

    const onRetweet = useCallback(() => {


      if(!me) {
        return alert('로그인이 필요합니다.');
      }

      return dispatch({
        type: RETWEET_REQUEST,
        data: {
          postId: post.id,
          token,
        }
      });
    }, [me && me.id, post.id] );

    const onFollow = useCallback(userId => () => {

      dispatch({
        type: FOLLOW_USER_REQUEST,
        data: {
          userId,
          token
        }
      });
    },[]);

    const onUnfollow = useCallback(userId => () => {

      dispatch({
        type: UNFOLLOW_USER_REQUEST,
        data: {
          userId,
          token
        }
      });
    },[]);

    const onRemovePost = useCallback(postId => () => {
      dispatch({
        type: REMOVE_POST_REQUEST,
        data: {
          postId,
          token
        }
      });
    },[])

  return (
        <CardWrapper>
          <Card
              cover={post.images[0] && <PostImages images={post.images}/>}
              actions={[
                  <Icon type="retweet" key="retweet" onClick={onRetweet}/>,
                  <Icon type="heart" key="heart" theme={liked ? 'twoTone' : 'outlined'} twoToneColor="#eb2f96" onClick={onToggleLike}/>,
                  <Icon type="message" key="message" onClick={onToggleComment}/>,
                  
                  <Popover
                    key="ellipsis"
                    content={(
                      <Button.Group>
                        {me && post.user.id === me.id
                          ? (
                            <>
                              <Button>수정</Button>
                              <Button type="danger" onClick={onRemovePost(post.id)}>삭제</Button>
                            </>
                          )
                          : <Button>신고</Button>}
                      </Button.Group>
                    )}
                    >
                    <Icon type="ellipsis" />
                  </Popover>,
              ]}
              title={post.isRetweet ? `${post.user.name}님이 리트윗 하셨습니다.` : null}
              extra = { !me || post.user.id === me.id
              ? null
              : me.followings && me.followings.find(id => id === post.user.id)
              ? <Button onClick={onUnfollow(post.user.id)}>언팔로우</Button>
              : <Button onClick={onFollow(post.user.id)}>팔로우</Button>
              }
          >
            {post.isRetweet ? 
                <Card
                  cover={post.retweet.targetPost.images[0] && <PostImages images={post.retweet.targetPost.images} />}
                >
                  <Card.Meta
                  
                    avatar={<Link href={{pathname: '/user', query: {id:post.retweet.targetPost.user.id}}} as={`/user/${post.retweet.targetPost.user.id}`}>
                              <a><Avatar>{post.retweet.targetPost.user.name[0]}</Avatar></a></Link>  }
                    title={post.retweet.targetPost.user.name}
                    description={<PostCardContent postData={post.retweet.targetPost.content} />}
                  />
                </Card>
              : 
              (
                <Card.Meta
                  avatar={<Link href={{pathname: '/user', query: {id:post.user.id}}} as={`/user/${post.user.id}`}><a><Avatar>{post.user.name[0]}</Avatar></a></Link>  }
                  title={post.user.name}
                  description={<PostCardContent postData={post.content} />}
                />
              )
              
            }


              

          </Card>
          {commentFormOpened && (
            <>
              <Form onSubmit={onSubmitComment}>
                <Form.Item>
                  <Input.TextArea rows={4} value={commentText} onChange={onChangeCommentText} />
                </Form.Item>
                <Button type="primary" htmlType="submit" loading={isAddingComment}>댓글작성</Button>
              </Form>
              <List
                header={` 댓글`}
                itemLayout = "horizontal"
                dataSource={post.comments || []}
                renderItem={ item => (
                  <li>
                    <Comment
                      author={item.user.name}
                      avatar={<Link href={{pathname: '/user', query: {id : post.user.id}}} as={`/user/${post.user.id}`}><a><Avatar>{post.user.name[0]}</Avatar></a></Link> }
                      content={item.content}
                    />
                  </li>
                )}
              />
            </>
          )}
        </CardWrapper>
    );
};

PostCards.propTypes = {
    post: PropTypes.shape({
        user: PropTypes.object,
        content: PropTypes.string,
        // img: PropTypes.string,
        createAt: PropTypes.string,
    }),
};

export default PostCards;
