import React from 'react';
import PostCards from "../components/PostCards";
import PostForm from "../components/PostForm";

const dummy = {
    isLogin: true,
    imagePaths: [],
    mainPosts: [{
        User: {
            id: 1,
            name: '이름'
        },
        content: '첫번째 게시글',
        img: '',
        createdAt: Date,
    }],
};

const Home = () => {
    return (
        <div>
            {dummy.isLogin && <PostForm />}
            {dummy.mainPosts.map((c) => {
                return <PostCards key={c} post={c}/>;
            })}
        </div>
    );
};

export default Home;