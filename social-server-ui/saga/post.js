import {call, all, fork, takeLatest,delay,put} from 'redux-saga/effects'
import {
    ADD_COMMENT_FAILURE,
    ADD_COMMENT_REQUEST,
    ADD_COMMENT_SUCCESS,
    ADD_POST_FAILURE,
    ADD_POST_REQUEST,
    ADD_POST_SUCCESS, LOAD_COMMENTS_FAILURE, LOAD_COMMENTS_REQUEST, LOAD_COMMENTS_SUCCESS,
    LOAD_HASHTAG_POSTS_FAILURE,
    LOAD_HASHTAG_POSTS_REQUEST,
    LOAD_HASHTAG_POSTS_SUCCESS,
    LOAD_MAIN_POSTS_FAILURE,
    LOAD_MAIN_POSTS_REQUEST,
    LOAD_MAIN_POSTS_SUCCESS,
    LOAD_USER_POSTS_FAILURE,
    LOAD_USER_POSTS_REQUEST,
    LOAD_USER_POSTS_SUCCESS
} from '../reducers/post';
import axios from 'axios';

function addPostAPI({post, token}) {

    return axios.post('post', post, {
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}

function* addPost(action) {
    console.log('add');
    try {
        const result = yield call(addPostAPI, action.data);
        yield put({
            type: ADD_POST_SUCCESS,
            data : result.data.response,
        });
    } catch (e) {
        console.error(e);
        yield put({
            type: ADD_POST_FAILURE,
            error: e,
        })
    }
}
function loadPostAPI({userId,token}) {
    return axios.get(`user/${userId}/post/list?page=0&size=4`, {
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}

function* loadPost(action) {
    try {
        const result = yield call(loadPostAPI, action.data);
        yield put({
            type: LOAD_MAIN_POSTS_SUCCESS,
            data : result.data.response,
        });
    } catch (e) {
        console.error(e);
        yield put({
            type: LOAD_MAIN_POSTS_FAILURE,
            error: e,
        })
    }
}

function* watchLoadPost() {
    yield takeLatest(LOAD_MAIN_POSTS_REQUEST, loadPost);
}

function loadHashtagAPI({tag,token}) {
    return axios.get(`/post/${tag}/list?page=0&size=4`, {
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}


function* loadHashtag(action) {
    try {
        const result = yield call(loadHashtagAPI, action.data);
        yield put({
            type: LOAD_HASHTAG_POSTS_SUCCESS,
            data : result.data.response,
        });
    } catch (e) {
        console.error(e);
        yield put({
            type: LOAD_HASHTAG_POSTS_FAILURE,
            error: e,
        })
    }
}


function* watchLoadHashtagPosts() {
    yield takeLatest(LOAD_HASHTAG_POSTS_REQUEST,loadHashtag);
}


function loadUserPostAPI({userId,token}) {
    return axios.get(`user/${userId}/post/list?page=0&size=4`, {
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}

function* loadUserPost(action) {
    try {
        const result = yield call(loadUserPostAPI, action.data);
        yield put({
            type: LOAD_USER_POSTS_SUCCESS,
            data : result.data.response,
        });
    } catch (e) {
        console.error(e);
        yield put({
            type: LOAD_USER_POSTS_FAILURE,
            error: e,
        })
    }
}

function* watchLoadUserPosts() {
    yield takeLatest(LOAD_USER_POSTS_REQUEST,loadUserPost);
}

function loadCommentsAPI({userId,postId,token}) {
    return axios.get(`user/${userId}/post/${postId}/comment/list`, {
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}

function* loadComments(action) {
    try {
        const result = yield call(loadCommentsAPI, action.data);
        yield put({
            type: LOAD_COMMENTS_SUCCESS,
            data : {
                postId: action.data.postId,
                comments: result.data.response,
            },
        });
    } catch (e) {
        console.error(e);
        yield put({
            type: LOAD_COMMENTS_FAILURE,
            error: e,
        })
    }
}

function* watchLoadComments() {
    yield takeLatest(LOAD_COMMENTS_REQUEST, loadComments);
}

function* watchAddPost() {

    yield takeLatest(ADD_POST_REQUEST, addPost);
}

function addCommentAPI({userId,postId,comment,token}) {
    console.log(token);
    return axios.post(`user/${userId}/post/${postId}/comment`, {content: comment}, {
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}

function* addComment(action) {
    try {
        const result = yield call(addCommentAPI, action.data);
        yield put({
            type: ADD_COMMENT_SUCCESS,
            data : {
                postId: action.data.postId,  //??
                comment: result.data.response,
            }
        });
    } catch (e) {
        console.error(e);
        yield put({
            type: ADD_COMMENT_FAILURE,
            error: e,
        })
    }
}

function* watchAddComment() {
    yield takeLatest(ADD_COMMENT_REQUEST, addComment);
}

export default function* postSaga() {
    yield all([
        fork(watchAddPost),
        fork(watchLoadPost),
        fork(watchAddComment),
        fork(watchLoadHashtagPosts),
        fork(watchLoadUserPosts),
        fork(watchLoadComments),
    ]);
}
