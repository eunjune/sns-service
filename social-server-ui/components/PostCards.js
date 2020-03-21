import React, {useState, useCallback, useEffect, memo} from 'react';
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
import moment from "moment";
import CommentForm from "./CommentForm";
import FollowButton from "./FollowButton";
moment.locale('ko');

const CardWrapper = styled.div`
  margin-bottom: 20px;
`;

const PostCards = memo(({post}) => {
    const [commentFormOpened, setCommentFormOpened] = useState(false);
    const meId = useSelector(state => state.user.me && state.user.me.id);
    const dispatch = useDispatch();
    const token = cookie.load('token');
    const liked = post.likes && post.likes.find(v =>v.user.id === meId);

    const onToggleComment = useCallback(() => {
        setCommentFormOpened(prev => !prev);


        if(!commentFormOpened) {
          dispatch({
              type: LOAD_COMMENTS_REQUEST,
              data: {
                  postId: post.id,
                  userId: meId,
                  token,
              },
          })
        }

    },[]);

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

    const onToggleLike = useCallback(() => {

      if(!meId) {
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

    },[meId, post]);

    const onRetweet = useCallback(() => {


      if(!meId) {
        return alert('로그인이 필요합니다.');
      }

      return dispatch({
        type: RETWEET_REQUEST,
        data: {
          postId: post.id,
          token,
        }
      });
    }, [meId, post.id] );



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
                        {meId && post.user.id === meId
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
              extra = { <FollowButton post={post} onUnfollow={onUnfollow} onFollow={onFollow} /> }
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
              {moment(post.createAt).format('YYYY.MM.DD.')}
          </Card>
          {commentFormOpened && (
            <>
                <CommentForm post={post}/>
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
});

PostCards.propTypes = {
    post: PropTypes.shape({
        user: PropTypes.object,
        content: PropTypes.string,
        // images: PropTypes.string,
        createAt: PropTypes.string,
    }),
};

export default PostCards;
