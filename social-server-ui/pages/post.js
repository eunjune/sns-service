import React  from 'react';
import {useSelector} from "react-redux";
import {LOAD_POST_REQUEST} from "../reducers/post";
import PropTypes from 'prop-types';
import cookie from "react-cookies";
import {Button, Card, Comment, Descriptions, Icon, List, Popover, Statistic, Tooltip} from "antd";
import PostCardContent from "../components/post/PostCardContent";
import CenterAlignment from "../components/CenterAlignment";
import AvartarCustom from "../components/profile/AvartarCustom";
import PostEditForm from "../components/post/PostEditForm";
import PostImages from "../components/post/PostImages";
import FollowButton from "../components/post/FollowButton";
import moment from "moment";
import styled from "styled-components";
import LikeOutlined from "@ant-design/icons/lib/icons/LikeOutlined";
import CommentOutlined from "@ant-design/icons/lib/icons/CommentOutlined";
import RetweetOutlined from "@ant-design/icons/lib/icons/RetweetOutlined";
import Helmet from 'react-helmet';
import {baseUrl} from "../config/config";

moment.locale('ko');

const CardWrapper = styled.div`
  margin-top: 100px;
`;

const Post = ({postId,userId}) => {
    const {post} = useSelector(state => state.post);

    return (
        <>
            <Helmet
                title={`${post.user.name}님의 글`}
                description={post.content}
                meta={[{
                    name: 'description', content: post.content,
                }, {
                    property: 'og:title', content: `${post.user.name}님의 게시글`,
                }, {
                    property: 'og:description', content: post.content,
                }, {
                    property: 'og:image', content: post.images[0]
                }, {
                    property: 'og:url', content: process.env.NODE_ENV === 'production' ?
                    'http://ec2-15-165-223-244.ap-northeast-2.compute.amazonaws.com/' :
                    'http://localhost:3060/' +
                    `user/${userId}/post/${postId}`,
                }]}

            />
            <CenterAlignment children={
                <CardWrapper>
                    <Card
                        type="inner"
                        cover={post.images[0] && <PostImages images={post.images}/>}
                    >
                        <div style={{float: 'right'}}>{moment(post.createdAt).fromNow()}</div>

                        <Card.Meta
                            avatar={<AvartarCustom shape={"circle"} size={"default"} profileImageUrl={post.user.profileImageUrl} id={post.user.id}/>}
                            title={post.user.name}
                            description={<PostCardContent postData={post.content}/>}
                            style={{marginBottom: 50}}
                        />

                        <Statistic title="좋아요" value={post.likeCount} prefix={<LikeOutlined />} style={{marginBottom: 50}}/>

                        <Statistic title="리트윗" value={post.retweetCount} prefix={<RetweetOutlined />} style={{marginBottom: 50}}/>

                        <List
                            header={<Statistic title="댓글" value={post.comments.length} prefix={<CommentOutlined />} />}
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
                    </Card>

                </CardWrapper>



            }/>
        </>

    );
};

Post.getInitialProps = async (context) => {

    console.log(context.query.postId);
    console.log(context.query.userId);

    context.store.dispatch({
       type: LOAD_POST_REQUEST,
       data: {
           postId : context.query.postId,
           userId : context.query.userId,
       }
    });

    return {postId :parseInt(context.query.postId,10), userId: parseInt(context.query.userId,10)};
};

Post.propTypes = {
    postId: PropTypes.number.isRequired,
    userId: PropTypes.number.isRequired,
};

export default Post;