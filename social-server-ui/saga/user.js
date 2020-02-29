import axios from 'axios';
import {
    LOG_IN_REQUEST,
    LOG_IN_SUCCESS,
    LOG_IN_FAILURE,
    SIGN_UP_REQUEST,
    SIGN_UP_SUCCESS,
    SIGN_UP_FAILURE, EMAIL_CHECK_REQUEST, EMAIL_CHECK_SUCCESS, EMAIL_CHECK_FAILURE,
} from '../reducers/user';
import { call,fork,takeEvery,delay,put,all } from 'redux-saga/effects';

axios.defaults.baseURL = 'http://localhost:8080/api/';

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
    yield takeEvery(EMAIL_CHECK_REQUEST, emailCheck);
}


function loginAPI(loginData) {
    return axios.post('login', loginData);
}

function* login(action) {
    try {
        yield call(loginAPI, action.data);
        //yield delay(2000);
        yield put({
            type: LOG_IN_SUCCESS
        });
    } catch (e) { // 실패
        console.error(e);
        yield put({
            type: LOG_IN_FAILURE
        });
    }
}

function* watchLogin() {
    yield takeEvery(LOG_IN_REQUEST, login);
}

function loginOutAPI(loginData) {
    return axios.post('login', loginData);
}

function* loginOut(action) {
    try {
        yield call(loginAPI, action.data);
        //yield delay(2000);
        yield put({
            type: LOG_IN_SUCCESS
        });
    } catch (e) { // 실패
        console.error(e);
        yield put({
            type: LOG_IN_FAILURE
        });
    }
}

function* watchLoginOut() {
    yield takeEvery(LOG_IN_REQUEST, login);
}

function signUpAPI() {

}


function* signUp() {

    console.log('signUp');
    try {
        //yield call(signUpAPI);
        yield delay(2000);
        //throw new Error('에러');
        yield put({
            type: SIGN_UP_SUCCESS
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
    yield takeEvery(SIGN_UP_REQUEST, signUp);
}



function loadUserAPI() {

}


function* loadUser() {

    console.log('signUp');
    try {
        //yield call(signUpAPI);
        yield delay(2000);
        //throw new Error('에러');
        yield put({
            type: SIGN_UP_SUCCESS
        });
    } catch (e) { // 실패
        console.error(e);
        yield put({
            type: SIGN_UP_FAILURE,
            error: e
        });
    }
}



function* watchLoadUser() {

}



export default function* userSaga() {
    yield all([
        fork(watchEmailCheck),
        fork(watchLogin),
        fork(watchSignUp),
        fork(watchLoadUser),
    ]);
}
