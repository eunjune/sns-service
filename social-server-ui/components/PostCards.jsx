import React from 'react';
import PropTypes from 'prop-types';
import {Button, Card, Icon, Avatar} from 'antd';

const PostCards = ({post}) => {
    return (
        <Card
            key={+post.createdAt}
            cover={post.img && <img src="c.img" alt="example"/>}
            actions={[
                <Icon type="retweet" key="retweet"/>,
                <Icon type="heart" key="heart"/>,
                <Icon type="message" key="message"/>,
                <Icon type="ellipsis" key="ellipsis"/>,
            ]}
            extra = {<Button>팔로우</Button>}
        >
            <Card.Meta
                avatar={<Avatar>{post.User.name[0]}</Avatar>}
                title={post.User.name}
                description={post.content}
            >

            </Card.Meta>
        </Card>
    );
};

PostCards.propTypes = {
    post: PropTypes.shape({
        User: PropTypes.object,
        content: PropTypes.string,
        img: PropTypes.string,
        createdAt: PropTypes.object,
    }),
};

export default PostCards;