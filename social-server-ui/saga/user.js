import axios from 'axios';

import {
    LOG_IN_REQUEST,
    LOG_IN_SUCCESS,
    LOG_IN_FAILURE,
    SIGN_UP_REQUEST,
    SIGN_UP_SUCCESS,
    SIGN_UP_FAILURE,
    EMAIL_CHECK_REQUEST,
    EMAIL_CHECK_SUCCESS,
    EMAIL_CHECK_FAILURE,
    LOAD_USER_REQUEST,
    LOAD_USER_SUCCESS,
    LOAD_USER_FAILURE,
    LOAD_ME_REQUEST,
    LOAD_ME_SUCCESS,
    LOAD_ME_FAILURE,
    FOLLOW_USER_REQUEST,
    FOLLOW_USER_SUCCESS,
    FOLLOW_USER_FAILURE,
    UNFOLLOW_USER_REQUEST,
    UNFOLLOW_USER_SUCCESS,
    UNFOLLOW_USER_FAILURE,
    LOAD_FOLLOWER_REQUEST,
    LOAD_FOLLOWER_SUCCESS,
    LOAD_FOLLOWER_FAILURE,
    REMOVE_FOLLOWER_FAILURE,
    REMOVE_FOLLOWER_REQUEST,
    REMOVE_FOLLOWER_SUCCESS,
    LOAD_FOLLOWING_REQUEST,
    LOAD_FOLLOWING_FAILURE,
    LOAD_FOLLOWING_SUCCESS,
    EDIT_NAME_REQUEST,
    EDIT_NAME_FAILURE,
    EDIT_NAME_SUCCESS,
    NAME_CHECK_SUCCESS, NAME_CHECK_FAILURE, NAME_CHECK_REQUEST
} from '../reducers/user';
import { call,fork,takeEvery,takeLatest,delay,put,all } from 'redux-saga/effects';

function emailCheckAPI(address) {
    const formData = new FormData();
    formData.append('address', address);
    return axios.post('user/exists/email', formData);
}

function* emailCheck(action) {
    try {
        const result = yield call(emailCheckAPI, action.data);
        yield put({
            type: EMAIL_CHECK_SUCCESS,
            data: !result.data.response,
        });
    } catch (error) { // 실패
        yield put({
            type: EMAIL_CHECK_FAILURE,
            error: error.response.data.error.message,
        });
    }
}

function* watchEmailCheck() {
    yield takeLatest(EMAIL_CHECK_REQUEST, emailCheck);
}

function nameCheckAPI(name) {
    const formData = new FormData();
    formData.append('name', name);
    return axios.post('user/exists/name', formData);
}

function* nameCheck(action) {
    try {
        const result = yield call(nameCheckAPI, action.data);
        yield put({
            type: NAME_CHECK_SUCCESS,
            data: !result.data.response,
        });
    } catch (error) { // 실패
        yield put({
            type: NAME_CHECK_FAILURE,
            error: error.response.data.error.message,
        });
    }
}

function* watchNameCheck() {
    yield takeLatest(NAME_CHECK_REQUEST, nameCheck);
}


function signUpAPI({name,address,password}) {
    const formData = new FormData();
    formData.append('name', name);
    formData.append('address', address);
    formData.append('password', password);
//data.append('file', new Blob(['test payload'], { type: 'text/csv' }));

    return axios.post('user/join', formData);
}


function* signUp(action) {
    try {
        const result = yield call(signUpAPI,action.data);
        yield put({
            type: SIGN_UP_SUCCESS,
            data: result.data,
        });
    } catch (e) { // 실패
        console.error(e);
        yield put({
            type: SIGN_UP_FAILURE,
            error: e.response.data.error.message,
        });
    }
}

function* watchSignUp() {
    yield takeLatest(SIGN_UP_REQUEST, signUp);
}


function loginAPI(loginData) {
    return axios.post('auth', loginData);
}

function* login(action) {
    try {
        const result = yield call(loginAPI, action.data);
        yield put({
            type: LOG_IN_SUCCESS,
            data: result.data,
        });
    } catch (e) { // 실패
        console.error(e);
        yield put({
            type: LOG_IN_FAILURE,
            error: e.response.data.error.message,
        });
    }
}

function* watchLogin() {
    yield takeLatest(LOG_IN_REQUEST, login);
}

function loadUserAPI({userId,token}) {
    return axios.get(`user/${userId}`,{
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}


function* loadUser(action) {

    try {
        const result = yield call(loadUserAPI, action.data);
        yield put({
            type: LOAD_USER_SUCCESS,
            data: result.data.response,
        });
    } catch (e) { // 실패
        console.error(e);
        yield put({
            type: LOAD_USER_FAILURE,
            error: e
        });
    }
}

function* watchLoadUser() {
    yield takeLatest(LOAD_USER_REQUEST, loadUser);
}

function loadMeAPI(token) {
    return axios.get('user/me',{
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}

function* loadMe(action) {

    try {
        const result = yield call(loadMeAPI,action.data);
        yield put({
            type: LOAD_ME_SUCCESS,
            data: result.data.response,
        });
    } catch (e) { // 실패
        console.error(e);
        yield put({
            type: LOAD_ME_FAILURE,
            error: e
        });
    }
}

function* watchLoadMe() {
    yield takeLatest(LOAD_ME_REQUEST,loadMe);
}

function followAPI({userId,token}) {
    console.log('followAPI');
    console.log(token);
    return axios.post(`user/${userId}/follow`,{},{
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}

function* follow(action) {

    try {
        const result = yield call(followAPI, action.data);
        yield put({
            type: FOLLOW_USER_SUCCESS,
            data: result.data.response,
        });
    } catch (e) { // 실패
        console.error(e);
        yield put({
            type: FOLLOW_USER_FAILURE,
            error: e
        });
    }
}

function* watchFollow() {
    yield takeLatest(FOLLOW_USER_REQUEST,follow);
}

function unfollowAPI({userId,token}) {
    return axios.delete(`user/${userId}/follow`,{
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}

function* unfollow(action) {

    try {
        const result = yield call(unfollowAPI, action.data);
        yield put({
            type: UNFOLLOW_USER_SUCCESS,
            data: result.data.response,
        });
    } catch (e) { // 실패
        console.error(e);
        yield put({
            type: UNFOLLOW_USER_FAILURE,
            error: e
        });
    }
}

function* watchUnfollow() {
    yield takeLatest(UNFOLLOW_USER_REQUEST,unfollow);
}

function loadFollowerAPI({token, offset=0}) {
    return axios.get(`user/followers?page=${offset}&size=3`,{
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}

function* loadFollower(action) {

    try {
        const result = yield call(loadFollowerAPI,action.data);
        yield put({
            type: LOAD_FOLLOWER_SUCCESS,
            data: result.data.response,
        });
    } catch (e) { // 실패
        console.error(e);
        yield put({
            type: LOAD_FOLLOWER_FAILURE,
            error: e
        });
    }
}

function* watchLoadfollower() {
    yield takeLatest(LOAD_FOLLOWER_REQUEST,loadFollower,);
}

function loadFollowingAPI({token, offset=0}) {
    return axios.get(`user/followings?page=${offset}&size=3`,{
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}

function* loadFollowing(action) {

    try {
        const result = yield call(loadFollowingAPI,action.data);
        yield put({
            type: LOAD_FOLLOWING_SUCCESS,
            data: result.data.response,
        });
    } catch (e) { // 실패
        console.error(e);
        yield put({
            type: LOAD_FOLLOWING_FAILURE,
            error: e
        });
    }
}

function* watchLoadfollowing() {
    yield takeLatest(LOAD_FOLLOWING_REQUEST,loadFollowing);
}

function removeFollowerAPI({userId,token}) {
    return axios.delete(`user/${userId}/follower`,{
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}

function* removeFollower(action) {

    try {
        const result = yield call(removeFollowerAPI, action.data);
        yield put({
            type: REMOVE_FOLLOWER_SUCCESS,
            data: result.data.response,
        });
    } catch (e) { // 실패
        console.error(e);
        yield put({
            type: REMOVE_FOLLOWER_FAILURE,
            error: e
        });
    }
}

function* watchRemovefollower() {
    yield takeLatest(REMOVE_FOLLOWER_REQUEST,removeFollower);
}

function editNameAPI({name, token}) {
    return axios.patch(`user/${name}`, null, {
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}

function* editName(action) {

    try {
        const result = yield call(editNameAPI, action.data);
        yield put({
            type: EDIT_NAME_SUCCESS,
            data: result.data.response,
        });
    } catch (e) { // 실패
        console.error(e);
        yield put({
            type: EDIT_NAME_FAILURE,
            error: e
        });
    }
}

function* watchEditName() {
    yield takeLatest(EDIT_NAME_REQUEST,editName);
}

export default function* userSaga() {
    yield all([
        fork(watchEmailCheck),
        fork(watchNameCheck),
        fork(watchLogin),
        fork(watchSignUp),
        fork(watchLoadUser),
        fork(watchLoadMe),
        fork(watchFollow),
        fork(watchUnfollow),
        fork(watchLoadfollower),
        fork(watchLoadfollowing),
        fork(watchRemovefollower),
        fork(watchEditName),
    ]);
}
