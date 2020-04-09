import React, {useEffect, useCallback, useRef} from 'react';
import MarkDown from 'react-mark';
import PropTypes from 'prop-types';
import { useDispatch, useSelector } from 'react-redux';
import {LOAD_SEARCH_POSTS_REQUEST} from '../reducers/post';
import PostCards from '../components/post/PostCards';
import CenterAlignment from "../components/CenterAlignment";

const Search = ({ keyword }) => {
    const dispatch = useDispatch();
    const { posts, hasMorePost} = useSelector((state) => state.post);
    const usedLastIds = useRef([]);

    const onScroll = useCallback(() => {
        console.log(hasMorePost);
        if(window.scrollY + document.documentElement.clientHeight > document.documentElement.scrollHeight - 200 && hasMorePost) {
            const lastId = posts[posts.length-1].id;
            if(!usedLastIds.current.includes(lastId)) {
                dispatch({
                    type: LOAD_SEARCH_POSTS_REQUEST,
                    data : {
                        keyword,
                        lastId: lastId,
                    }
                });
                usedLastIds.current.push(lastId);
            }
        }

    }, [posts, hasMorePost]);

    useEffect(() => {

        window.addEventListener('scroll', onScroll);
        return () => {
            window.removeEventListener('scroll', onScroll);
        }
    }, [posts]);

    return (
        <CenterAlignment children={<div className='index' style={{padding: 50}}>
            {posts.map((post) => {
                return <PostCards key={+post.id} post={post} keyword={keyword}/>;
            })}
        </div>}/>
    );
};

Search.propTypes = {
    keyword: PropTypes.string.isRequired,
};

Search.getInitialProps = async (context) => {
    const keyword = context.query.keyword;

    context.store.dispatch({
        type: LOAD_SEARCH_POSTS_REQUEST,
        data: {keyword},
    });

    return { keyword: context.query.keyword };
};

export default Search;
