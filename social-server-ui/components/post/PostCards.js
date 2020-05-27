import React, {useState, useCallback, memo} from 'react';
import cookie from 'react-cookies';
import PropTypes from 'prop-types';
import {Button, Card, Icon, Avatar, Tooltip, List, Comment, Popover} from 'antd';
import {useSelector, useDispatch} from 'react-redux';
import {
    LOAD_COMMENTS_REQUEST,
    UNLIKE_POST_REQUEST,
    LIKE_POST_REQUEST,
    RETWEET_REQUEST,
    REMOVE_POST_REQUEST,
    SHOW_EDIT_POST
} from '../../reducers/post';
import {FOLLOW_USER_REQUEST, REMOVE_POST, UNFOLLOW_USER_REQUEST} from '../../reducers/user'
import Link from 'next/link';
import PostImages from './PostImages'
import PostCardContent from './PostCardContent'
import styled from 'styled-components';
import moment from "moment";
import CommentForm from "./CommentForm";
import FollowButton from "./FollowButton";
import PostEditForm from "./PostEditForm";
import AvartarCustom from "../profile/AvartarCustom";

moment.locale('ko');

const PostCards = memo(({post, keyword}) => {
    const {isEditPost, editPostId} = useSelector(state => state.post);
    const [commentFormOpened, setCommentFormOpened] = useState(false);
    const meId = useSelector(state => state.user.me && state.user.me.id);
    const dispatch = useDispatch();
    const token = cookie.load('token');


    const onToggleComment = useCallback(() => {

        setCommentFormOpened(prev => !prev);

        if (!commentFormOpened) {
            dispatch({
                type: LOAD_COMMENTS_REQUEST,
                data: {
                    postId: post.id,
                },
            })
        }

    }, [meId, post.id]);

    const onFollow = useCallback(userId => () => {

        dispatch({
            type: FOLLOW_USER_REQUEST,
            data: {
                userId,
                token
            }
        });
    }, []);

    const onUnfollow = useCallback(userId => () => {

        dispatch({
            type: UNFOLLOW_USER_REQUEST,
            data: {
                userId,
                token
            }
        });
    }, []);

    const onToggleLike = useCallback(() => {

        if (!meId) {
            return alert('로그인이 필요합니다.');
        }

        if (post.likesOfMe) {
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

    }, [meId, post]);

    const onRetweet = useCallback(() => {


        if (!meId) {
            return alert('로그인이 필요합니다.');
        }

        return dispatch({
            type: RETWEET_REQUEST,
            data: {
                postId: post.id,
                token,
            }
        });
    }, [meId, post.id]);


    const onRemovePost = useCallback(postId => () => {
        dispatch({
            type: REMOVE_POST_REQUEST,
            data: {
                postId,
                token
            }
        });

    }, []);

    const showEditPost = useCallback(() => {
        if (!post.isRetweet) {
            dispatch({
                type: SHOW_EDIT_POST,
                postId: post.id,
                images: post.images,
            });
        }

    }, [post]);


    return (
        <div style={{marginBottom: '100px'}}>
            <Card
                type="inner"
                cover={post.images[0] && <PostImages images={post.images}/>}
                actions={[
                    <Icon type="retweet" key="retweet" onClick={onRetweet}/>,
                    <div>
                        <Icon type="heart" key="heart" theme={post.likesOfMe ? 'twoTone' : 'outlined'}
                              twoToneColor="#eb2f96" onClick={onToggleLike}/>
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
                <div style={{float: 'right'}}>{moment(post.createdAt).fromNow()}</div>
                {post.isRetweet ?
                    <Card
                        cover={post.retweetPost.images[0] && <PostImages images={post.retweetPost.images}/>}
                    >
                        <Card.Meta

                            avatar={<AvartarCustom shape={"circle"} size={"default"} profileImageUrl={post.retweetPost.user.profileImageUrl}
                                                   id={post.retweetPost.user.id}/>}
                            title={post.retweetPost.user.name}
                            description={<PostCardContent postData={post.retweetPost.content} keyword={keyword}/>}
                        />
                    </Card>
                    :
                    (
                        <Card.Meta
                            avatar={<AvartarCustom shape={"circle"} size={"default"} profileImageUrl={post.user.profileImageUrl} id={post.user.id}/>}
                            title={post.user.name}
                            description={isEditPost === true && editPostId === post.id ?
                                <PostEditForm key={post.id} post={post}/> :
                                <PostCardContent postData={post.content} keyword={keyword}/>}
                        />

                    )

                }


            </Card>

            {commentFormOpened && (
                <>
                    {meId && <CommentForm post={post}/>}
                    <List
                        header={` 댓글`}
                        itemLayout="horizontal"
                        dataSource={post.comments || []}
                        renderItem={item => (

                            <Comment
                                author={item.user.name}
                                avatar={
                                    <AvartarCustom shape="circle" size={"default"} profileImageUrl={item.user.profileImageUrl} id={item.user.id}/>
                                }
                                content={item.content}
                                datetime={
                                    <Tooltip title={moment().format('YYYY-MM-DD HH:mm:ss')}>
                                        <span>{moment(item.createdAt).fromNow()}</span>
                                    </Tooltip>
                                }
                            />
                        )}
                    />
                </>
            )}
        </div>
    );
});

PostCards.propTypes = {
    post: PropTypes.shape({
        user: PropTypes.object,
        content: PropTypes.string,
        createdAt: PropTypes.string,
    }),
};

export default PostCards;
