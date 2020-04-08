import React, {useState, useCallback, useEffect, memo} from 'react';
import cookie from 'react-cookies';
import PropTypes from 'prop-types';
import {Button, Card, Icon, Avatar, Tooltip, List, Comment, Input, Popover, Modal} from 'antd';
import { useSelector, useDispatch } from 'react-redux';
import {
    ADD_COMMENT_REQUEST,
    LOAD_COMMENTS_REQUEST,
    UNLIKE_POST_REQUEST,
    LIKE_POST_REQUEST,
    RETWEET_REQUEST,
    REMOVE_POST_REQUEST,
    REMOVE_IMAGE, SHOW_EDIT_POST, CANCEL_EDIT_POST, EDIT_POST_REQUEST
} from '../../reducers/post';
import {FOLLOW_USER_REQUEST,UNFOLLOW_USER_REQUEST} from '../../reducers/user'
import Link from 'next/link';
import PostImages from './PostImages'
import PostCardContent from './PostCardContent'
import styled from 'styled-components';
import moment from "moment";
import CommentForm from "./CommentForm";
import FollowButton from "./FollowButton";
import PostForm from "./PostForm";
import PostEditForm from "./PostEditForm";
import UserOutlined from "@ant-design/icons/lib/icons/UserOutlined";
import AvartarCustom from "../profile/AvartarCustom";
moment.locale('ko');

const CardWrapper = styled.div`
  margin-bottom: 100px;
`;

const PostCards = memo(({post}) => {
    const {isEditPost, editPostId} = useSelector(state => state.post);
    const [commentFormOpened, setCommentFormOpened] = useState(false);
    const meId = useSelector(state => state.user.me && state.user.me.id);
    const dispatch = useDispatch();
    const token = cookie.load('token');

    useEffect(() => {

    },[post]);

    const onToggleComment = useCallback(() => {

        if(!meId) {
            return alert('로그인이 필요합니다.');
        }

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

    },[meId,post.id]);

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

      if(post.likesOfMe) {
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
    },[]);

    const showEditPost = useCallback(() => {
        if(!post.isRetweet) {
            dispatch({
                type: SHOW_EDIT_POST,
                postId: post.id,
                images: post.images,
            });
        }

    },[post]);


  return (
        <CardWrapper>
            <Card
                type="inner"
                cover={post.images[0] && <PostImages images={post.images}/>}
                actions={[
                    <Icon type="retweet" key="retweet" onClick={onRetweet}/>,
                    <div>
                        <Icon type="heart" key="heart" theme={post.likesOfMe ? 'twoTone' : 'outlined'} twoToneColor="#eb2f96" onClick={onToggleLike}/>
                        <span style={{marginLeft: 10}}>{post.likeCount}</span>
                    </div>,

                    <div>
                        <Icon type="message" key="message" onClick={onToggleComment}/>
                        <span style={{marginLeft: 10}}>{post.commentCount}</span>
                    </div>,

                    <Popover
                        key="ellipsis"
                        content={(
                            <Button.Group>
                                {meId && post.user.id === meId
                                    ? (
                                        <>
                                            <Button onClick={showEditPost}>수정</Button>
                                            <Button type="danger" onClick={onRemovePost(post.id)}>삭제</Button>
                                        </>
                                    )
                                    : <Button>신고</Button>}
                            </Button.Group>
                        )}
                    >
                        <Icon type="ellipsis"/>
                    </Popover>,
                ]}
                title={post.isRetweet ? `${post.user.name}님이 리트윗 하셨습니다.` : null}
                extra={<FollowButton post={post} onUnfollow={onUnfollow} onFollow={onFollow}/>}
            >
                <div style={{float: 'right'}}>{moment(post.createAt).fromNow()}</div>
                {post.isRetweet ?
                    <Card
                        cover={post.retweetPost.images[0] && <PostImages images={post.retweetPost.images}/>}
                    >
                        <Card.Meta

                            avatar={
                                <Link href={{pathname: '/user', query: {id: post.retweetPost.user.id}}} as={`/user/${post.retweetPost.user.id}`}>
                                    <a>
                                        <AvartarCustom shape={"circle"} size={"default"} profileImageUrl={post.retweetPost.user.profileImageUrl} name={post.retweetPost.user.name}/>
                                    </a>
                                </Link>}
                            title={post.retweetPost.user.name}
                            description={<PostCardContent postData={post.retweetPost.content}/>}
                        />
                    </Card>
                    :
                    (
                        <Card.Meta
                            avatar={
                                <Link href={{pathname: '/user', query: {id: post.user.id}}} as={`/user/${post.user.id}`}>
                                    <a>
                                        <AvartarCustom shape={"circle"} size={"default"} profileImageUrl={post.user.profileImageUrl} name={post.user.name} />
                                    </a>
                                </Link>}
                            title={post.user.name}
                            description={isEditPost === true && editPostId === post.id ?
                                            <PostEditForm key={post.id} post={post} /> :<
                                            PostCardContent postData={post.content}/>}
                        />

                    )

                }


            </Card>

            {commentFormOpened && (
            <>
                <CommentForm post={post}/>
                <List
                    header={` 댓글`}
                    itemLayout = "horizontal"
                    dataSource={post.comments || []}
                    renderItem={ item => (

                      <Comment
                        author={item.user.name}
                        avatar={
                            <Link href={{pathname: '/user', query: {id : post.user.id}}} as={`/user/${post.user.id}`}>
                                <a>
                                    <Avatar shape="circle" icon={post.user.profileImageUrl && <img src={`http://localhost:8080/image/profile/${post.user.profileImageUrl}`} alt=""/>}>
                                        {post.user.name[0]}
                                    </Avatar>
                                </a>
                            </Link>
                        }
                        content={item.content}
                        datetime={
                            <Tooltip title={moment().format('YYYY-MM-DD HH:mm:ss')}>
                                <span>{moment(item.createAt).fromNow()}</span>
                            </Tooltip>
                        }
                      />
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
        createAt: PropTypes.string,
    }),
};

export default PostCards;
