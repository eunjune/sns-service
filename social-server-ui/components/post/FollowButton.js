import React from 'react'
import PropTypes from 'prop-types';
import {Button} from "antd";
import {useSelector} from "react-redux";

const FollowButton = ({post, onFollow, onUnfollow}) => {

    const me = useSelector(state => state.user.me);

    return (
        !me || post.user.id === me.id
            ? null
            : me.followings && me.followings.find(id => id === post.user.id)
            ? <Button onClick={onUnfollow(post.user.id)}>언팔로우</Button>
            : <Button onClick={onFollow(post.user.id)}>팔로우</Button>
    )
};

FollowButton.propTypes = {
    post: PropTypes.object.isRequired,
    onUnfollow: PropTypes.func.isRequired,
    onFollow: PropTypes.func.isRequired,
};

FollowButton.defaultProps = {
    me: null,
};

export default FollowButton;