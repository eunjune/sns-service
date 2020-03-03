import {call, all, fork, takeLatest,delay,put} from 'redux-saga/effects'
import {
    ADD_COMMENT_FAILURE,
    ADD_COMMENT_REQUEST, ADD_COMMENT_SUCCESS,
    ADD_POST_FAILURE,
    ADD_POST_REQUEST,
    ADD_POST_SUCCESS, LOAD_MAIN_POSTS_FAILURE, LOAD_MAIN_POSTS_REQUEST, LOAD_MAIN_POSTS_SUCCESS
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
        yield put({
            type: ADD_POST_FAILURE,
            error: e,
        })
    }
}

function* watchAddPost() {

    yield takeLatest(ADD_POST_REQUEST, addPost);
}

function* addComment(action) {
    try {
        yield delay(2000);
        yield put({
            type: ADD_COMMENT_SUCCESS,
            data : {
                postId: action.data.postId,
            }
        });
    } catch (e) {
        yield put({
            type: ADD_COMMENT_FAILURE,
            error: e,
        })
    }
}

function* watchAddComment() {
    yield takeLatest(ADD_COMMENT_REQUEST, addComment);
}

function loadPostAPI({userId,token}) {
    return axios.get('user/' + userId + '/post/list?page=0&size=4', {
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}

function* loadPost(action) {
    console.log(action);
    try {
        const result = yield call(loadPostAPI, action);
        yield put({
            type: LOAD_MAIN_POSTS_SUCCESS,
            data : result.data.response,
        });
    } catch (e) {
        yield put({
            type: LOAD_MAIN_POSTS_FAILURE,
            error: e,
        })
    }
}

function* watchLoadPost() {
    yield takeLatest(LOAD_MAIN_POSTS_REQUEST, loadPost);
}

export default function* postSaga() {
    yield all([
        fork(watchAddPost),
        fork(watchLoadPost),
        fork(watchAddComment),
    ]);
}
