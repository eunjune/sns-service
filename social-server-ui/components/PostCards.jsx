import React, { useState, useCallback, useEffect } from 'react';
import PropTypes from 'prop-types';
import { Button, Card, Icon, Avatar, Form, List,Comment,Input } from 'antd';
import { useSelector, useDispatch } from 'react-redux';
import {ADD_COMMENT_REQUEST, LOAD_COMMENTS_REQUEST} from '../reducers/post';
import Link from 'next/link';

const PostCards = ({post}) => {
    const [commentFormOpened, setCommentFormOpened] = useState(false);
    const [commentText, setCommentText] = useState('');
    const { me } = useSelector(state => state.user);
    const { addedComment,isAddingComment } = useSelector(state => state.post);
    const dispatch = useDispatch();

    useEffect(() => {
      setCommentText('');
    }, [addedComment === true]);

    const onToggleComment = useCallback(() => {
        const token = sessionStorage.getItem("token");

        setCommentFormOpened(prev => !prev);


        if(!commentFormOpened) {
          dispatch({
              type: LOAD_COMMENTS_REQUEST,
              data: {
                  postId: post.id,
                  userId: me.id,
                  token: token,
              },
          })
        }

    },[]);

    const onSubmitComment = useCallback((e) => {

        const token = sessionStorage.getItem("token");

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
                token: token,
            }
            })
    },[me && me.id, commentText]);

    const onChangeCommentText = useCallback((e) => {
      setCommentText(e.target.value);
    },[]);

  return (
        <div>
          <Card
              key={+post.createAt}
              cover={/*post.img && */<img src="" alt="example"/>}
              actions={[
                  <Icon type="retweet" key="retweet"/>,
                  <Icon type="heart" key="heart"/>,
                  <Icon type="message" key="message" onClick={onToggleComment}/>,
                  <Icon type="ellipsis" key="ellipsis"/>,
              ]}
              extra = {<Button>팔로우</Button>}
          >
              <Card.Meta
                  avatar={<Link href={{pathname: '/user', query: {id:post.user.id}}} as={`/user/${post.user.id}`}><a><Avatar>{post.user.name[0]}</Avatar></a></Link>  }
                  title={post.user.name}
                  description={(

                    <div>
                      {post.content.split(/(#[^\s!@#$%^&*()+-=`~.;'"?<>,./]+)/g).map(v => {
                          if(v.match(/#[^\s!@#$%^&*()+-=`~.;'"?<>,./]+/)) {
                            return (
                              <Link href={{pathname: '/hashtag', query: { tag : v.slice(1) }}} as={`/hashtag/${v.slice(1)}`} key={v}><a>{v}</a></Link>
                            )
                          }
                          return v;
                      })}
                    </div>
                  )}
              />

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
        </div>
    );
};

PostCards.propTypes = {
    post: PropTypes.shape({
        user: PropTypes.object,
        content: PropTypes.string,
        // img: PropTypes.string,
        createAt: PropTypes.object,
    }),
};

export default PostCards;
