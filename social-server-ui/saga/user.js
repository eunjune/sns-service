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
    LOAD_USER_REQUEST, LOAD_USER_SUCCESS, LOAD_USER_FAILURE,
} from '../reducers/user';
import { call,fork,takeEvery,takeLatest,delay,put,all } from 'redux-saga/effects';

function emailCheckAPI(email) {
    return axios.post('user/exists', email)
}

function* emailCheck(action) {
    try {
        const result = yield call(emailCheckAPI, action.data);
        yield put({
            type: EMAIL_CHECK_SUCCESS,
            data: !result.data.response,
        });
    } catch (e) { // 실패
        console.error(e.message);
        yield put({
            type: EMAIL_CHECK_FAILURE,
            error: '이메일 형식이 맞지 않습니다.',
        });
    }
}

function* watchEmailCheck() {
    yield takeLatest(EMAIL_CHECK_REQUEST, emailCheck);
}


function signUpAPI(joinRequest) {
    const formData = new FormData();
    formData.append('name', joinRequest.name);
    formData.append('principal', joinRequest.principal);
    formData.append('credentials', joinRequest.credentials);
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
            error: e
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
            error: e,
        });
    }
}

function* watchLogin() {
    yield takeLatest(LOG_IN_REQUEST, login);
}

function loadUserAPI({userId, token}) {
    return axios.get(userId === null ? 'user/me' : `user/${userId}`, !userId && {
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

export default function* userSaga() {
    yield all([
        fork(watchEmailCheck),
        fork(watchLogin),
        fork(watchSignUp),
        fork(watchLoadUser),
    ]);
}
