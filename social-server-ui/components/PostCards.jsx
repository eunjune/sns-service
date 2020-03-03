import React, { useState, useCallback, useEffect } from 'react';
import PropTypes from 'prop-types';
import { Button, Card, Icon, Avatar, Form, List,Comment,Input } from 'antd';
import { useSelector, useDispatch } from 'react-redux';
import { ADD_COMMENT_REQUEST } from '../reducers/post';
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
      setCommentFormOpened(prev => !prev);
    },[]);

    const onSubmitComment = useCallback((e) => {
      e.preventDefault();
      if(!me) {
        alert('로그인이 필요합니다.');
      }
      dispatch({
        type: ADD_COMMENT_REQUEST,
        data: {
          postId: post.id,
        }
      })
    },[me && me.seq]);

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
                  avatar={<Avatar>{post.user.name[0]}</Avatar>}
                  title={post.user.name}
                  description={(

                    <div>
                      {post.contents.split(/(#[^\s!@#$%^&*()+-=`~.;'"?<>,./]+)/g).map(v => {
                          if(v.match(/#[^\s!@#$%^&*()+-=`~.;'"?<>,./]+/)) {
                            return (
                              <Link href={`/hashtag/${v.slice(1)}`} key={v}><a>{v}</a></Link>
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
                 dataSource={post.commentList || []}
                renderItem={ item => (
                  <li>
                    <Comment
                      author={item.user.name}
                      avatar={<Avatar>{item.user.name[0]}</Avatar>}
                      content={item.contents}
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
        contents: PropTypes.string,
        // img: PropTypes.string,
        createAt: PropTypes.object,
    }),
};

export default PostCards;
