import {call, all, fork, takeLatest,delay,put,throttle} from 'redux-saga/effects'
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
    LOAD_USER_POSTS_SUCCESS, UPLOAD_IMAGES_FAILURE, UPLOAD_IMAGES_REQUEST, UPLOAD_IMAGES_SUCCESS, LIKE_POST_SUCCESS, LIKE_POST_FAILURE, LIKE_POST_REQUEST, UNLIKE_POST_FAILURE, UNLIKE_POST_SUCCESS, UNLIKE_POST_REQUEST, RETWEET_SUCCESS, RETWEET_FAILURE, RETWEET_REQUEST, REMOVE_POST_REQUEST, REMOVE_POST_SUCCESS, REMOVE_POST_FAILURE
} from '../reducers/post';
import axios from 'axios';
import { UNFOLLOW_me_SUCCESS } from '../reducers/user';

function addPostAPI({post, token}) {
    return axios.post('post', post, {
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}

function* addPost(action) {
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

function* watchAddPost() {
    
    yield takeLatest(ADD_POST_REQUEST, addPost);
}


function loadPostAPI(lastId=0) {

    return axios.get(`user/post/list?lastId=${lastId}&size=8`);
}

function* loadPost(action) {
    try {
        const result = yield call(loadPostAPI, action.lastId);
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
    yield throttle(1000,LOAD_MAIN_POSTS_REQUEST, loadPost);
}

function removePostAPI({postId,token}) {

    return axios.delete(`post/${postId}`,{
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}

function* removePost(action) {
    try {
        const result = yield call(removePostAPI, action.data);
        yield put({
            type: REMOVE_POST_SUCCESS,
            data : result.data.response,
        });
    } catch (e) {
        console.error(e);
        yield put({
            type: REMOVE_POST_FAILURE,
            error: e,
        })
    }
}

function* watchRemovePost() {
    yield takeLatest(REMOVE_POST_REQUEST, removePost);
}

function loadHashtagAPI({tag,lastId=0}) {
    return axios.get(`/post/${encodeURIComponent(tag)}/list?lastId=${lastId}&size=6`);
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


function loadUserPostAPI({userId,token,lastId=0}) {
    return axios.get(`user/${userId || 0}/post/list?lastId=${lastId}&size=6`, {
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



function addCommentAPI({userId,postId,comment,token}) {
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

function uploadImagesAPI({imageFormData,token}) {
    return axios.post(`post/images`, imageFormData, {
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}

function* uploadImages(action) {
    try {
        const result = yield call(uploadImagesAPI, action.data);
        yield put({
            type: UPLOAD_IMAGES_SUCCESS,
            data : result.data.response
        });
    } catch (e) {
        console.error(e);
        yield put({
            type: UPLOAD_IMAGES_FAILURE,
            error: e,
        })
    }
}

function* watchUploadImages() {
    yield takeLatest(UPLOAD_IMAGES_REQUEST, uploadImages);
}

function likePostAPI({postId,userId,token}) {
    console.log(token);
    return axios.patch(`user/${userId}/post/${postId}/like`, {}, {
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}

function* likePost(action) {
    try {
        const result = yield call(likePostAPI, action.data);
        yield put({
            type: LIKE_POST_SUCCESS,
            data : result.data.response
        });
    } catch (e) {
        yield put({
            type: LIKE_POST_FAILURE,
            error: e,
        })
    }
}

function* watchLikePost() {
    yield takeLatest(LIKE_POST_REQUEST, likePost);
}
function unlikePostAPI({postId,userId,token}) {
    return axios.delete(`user/${userId}/post/${postId}/unlike`, {
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}

function* unlikePost(action) {
    try {
        const result = yield call(unlikePostAPI, action.data);
        yield put({
            type: UNLIKE_POST_SUCCESS,
            data : result.data.response
        });
    } catch (e) {
        console.error(e);
        yield put({
            type: UNLIKE_POST_FAILURE,
            error: e,
        })
    }
}

function* watchUnLikePost() {
    yield takeLatest(UNLIKE_POST_REQUEST, unlikePost);
}

function retweetAPI({postId,token}) {
    return axios.post(`/post/${postId}/retweet`,{}, {
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}

function* retweet(action) {
    try {
        const result = yield call(retweetAPI, action.data);
        yield put({
            type: RETWEET_SUCCESS,
            data : result.data.response
        });
    } catch (e) {
        alert(e.response.data.error.message);
        yield put({
            type: RETWEET_FAILURE,
            error: e,
        });
        //console.log(e.response);
    }
}

function* watchRetweet() {
    yield takeLatest(RETWEET_REQUEST, retweet);
}

export default function* postSaga() {
    yield all([
        fork(watchAddPost),
        fork(watchRemovePost),
        fork(watchLoadPost),
        fork(watchAddComment),
        fork(watchLoadHashtagPosts),
        fork(watchLoadUserPosts),
        fork(watchLoadComments),
        fork(watchUploadImages),
        fork(watchLikePost),
        fork(watchUnLikePost),
        fork(watchRetweet),
    ]);
}
