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
    EDIT_PROFILE_REQUEST,
    EDIT_PROFILE_FAILURE,
    EDIT_PROFILE_SUCCESS,
    NAME_CHECK_SUCCESS,
    NAME_CHECK_FAILURE,
    NAME_CHECK_REQUEST,
    UPLOAD_IMAGE_REQUEST,
    UPLOAD_IMAGE_FAILURE,
    UPLOAD_IMAGE_SUCCESS,
    EMAIL_CERTIFICATION_REQUEST,
    EMAIL_CERTIFICATION_SUCCESS,
    EMAIL_CERTIFICATION_FAILURE,
    EMAIL_RESEND_REQUEST,
    EMAIL_RESEND_FAILURE,
    EMAIL_RESEND_SUCCESS,
    EMAIL_LOG_IN_REQUEST,
    EMAIL_LOG_IN_SUCCESS,
    EMAIL_LOG_IN_FAILURE,
    LOAD_NEW_NOTIFICATION_REQUEST,
    LOAD_NEW_NOTIFICATION_SUCCESS,
    LOAD_NEW_NOTIFICATION_FAILURE,
    LOAD_READ_NOTIFICATION_SUCCESS,
    LOAD_READ_NOTIFICATION_FAILURE,
    LOAD_READ_NOTIFICATION_REQUEST,
    READ_NOTIFICATION_REQUEST,
    READ_NOTIFICATION_SUCCESS,
    READ_NOTIFICATION_FAILURE,
    REMOVE_NOTIFICATION_REQUEST,
    REMOVE_NOTIFICATION_FAILURE, REMOVE_NOTIFICATION_SUCCESS
} from '../reducers/user';
import {call, fork, takeEvery, takeLatest, delay, put, all} from 'redux-saga/effects';

function loadMeAPI(token) {
    return axios.get('user/me', {
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}

function* loadMe(action) {

    try {
        const result = yield call(loadMeAPI, action.data);
        yield put({
            type: LOAD_ME_SUCCESS,
            data: result.data.response,
        });
    } catch (e) { // 실패
        yield put({
            type: LOAD_ME_FAILURE,
            error: {
                status: e.response.data.error.status,
                message: e.response.data.error.message,
            }
        });
    }
}

function* watchLoadMe() {
    yield takeLatest(LOAD_ME_REQUEST, loadMe);
}

function loadUserAPI({userId, token}) {
    return axios.get(`user/${userId}`, {
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
        yield put({
            type: LOAD_USER_FAILURE,
            error: {
                status: e.response.data.error.status,
                message: e.response.data.error.message,
            }
        });
    }
}

function* watchLoadUser() {
    yield takeLatest(LOAD_USER_REQUEST, loadUser);
}

function loadFollowingAPI({token, offset = 0}) {
    return axios.get(`user/followings?page=${offset}&size=3`, {
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}

function* loadFollowing(action) {

    try {
        const result = yield call(loadFollowingAPI, action.data);
        yield put({
            type: LOAD_FOLLOWING_SUCCESS,
            data: result.data.response,
        });
    } catch (e) { // 실패
        yield put({
            type: LOAD_FOLLOWING_FAILURE,
            error: {
                status: e.response.data.error.status,
                message: e.response.data.error.message,
            }
        });
    }
}

function* watchLoadfollowing() {
    yield takeLatest(LOAD_FOLLOWING_REQUEST, loadFollowing);
}

function loadFollowerAPI({token, offset = 0}) {

    return axios.get(`user/followers?page=${offset}&size=3`, {
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}

function* loadFollower(action) {

    try {
        const result = yield call(loadFollowerAPI, action.data);
        yield put({
            type: LOAD_FOLLOWER_SUCCESS,
            data: result.data.response,
        });
    } catch (e) { // 실패
        yield put({
            type: LOAD_FOLLOWER_FAILURE,
            error: {
                status: e.response.data.error.status,
                message: e.response.data.error.message,
            }
        });
    }
}

function* watchLoadfollower() {
    yield takeLatest(LOAD_FOLLOWER_REQUEST, loadFollower,);
}

function loadNewNotificationAPI(token) {

    return axios.get(`user/notification/new`, {
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}

function* loadNewNotification(action) {

    try {
        const result = yield call(loadNewNotificationAPI, action.data);
        yield put({
            type: LOAD_NEW_NOTIFICATION_SUCCESS,
            data: result.data.response,
        });
    } catch (e) { // 실패
        yield put({
            type: LOAD_NEW_NOTIFICATION_FAILURE,
            error: {
                status: e.response.data.error.status,
                message: e.response.data.error.message,
            }
        });
    }
}

function* watchLoadNewNotification() {
    yield takeLatest(LOAD_NEW_NOTIFICATION_REQUEST, loadNewNotification);
}

function loadReadNotificationAPI(token) {

    return axios.get(`user/notification/read`, {
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}

function* loadReadNotification(action) {

    try {
        const result = yield call(loadReadNotificationAPI, action.data);
        yield put({
            type: LOAD_READ_NOTIFICATION_SUCCESS,
            data: result.data.response,
        });
    } catch (e) { // 실패
        yield put({
            type: LOAD_READ_NOTIFICATION_FAILURE,
            error: {
                status: e.response.data.error.status,
                message: e.response.data.error.message,
            }
        });
    }
}

function* watchLoadReadNotification() {
    yield takeLatest(LOAD_READ_NOTIFICATION_REQUEST, loadReadNotification);
}


function resendEmailAPI(token) {
    return axios.get('user/resend-email', {
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}


function* resendEmail(action) {
    try {
        const result = yield call(resendEmailAPI, action.data);
        yield put({
            type: EMAIL_RESEND_SUCCESS,
            data: result.data.response,
        });
    } catch (e) { // 실패
        yield put({
            type: EMAIL_RESEND_FAILURE,
            error: {
                status: e.response.data.error.status,
                message: e.response.data.error.message,
            }
        });
    }
}

function* watchResendEmail() {
    yield takeLatest(EMAIL_RESEND_REQUEST, resendEmail);
}

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
    } catch (e) { // 실패
        yield put({
            type: EMAIL_CHECK_FAILURE,
            error: {
                status: e.response.data.error.status,
                message: e.response.data.error.message,
            }
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
    } catch (e) { // 실패
        yield put({
            type: NAME_CHECK_FAILURE,
            error: {
                status: e.response.data.error.status,
                message: e.response.data.error.message,
            }
        });
    }
}

function* watchNameCheck() {
    yield takeLatest(NAME_CHECK_REQUEST, nameCheck);
}

function emailCertificateAPI(data) {
    return axios.post('check-email-token', data);
}


function* emailCertificate(action) {
    try {
        const result = yield call(emailCertificateAPI, action.data);
        yield put({
            type: EMAIL_CERTIFICATION_SUCCESS,
            data: result.data.response,
        });
    } catch (e) { // 실패
        yield put({
            type: EMAIL_CERTIFICATION_FAILURE,
            error: {
                status: e.response.data.error.status,
                message: e.response.data.error.message,
            }
        });
    }
}

function* watchEmailCertificate() {
    yield takeLatest(EMAIL_CERTIFICATION_REQUEST, emailCertificate);
}

function signUpAPI({name, address, password}) {
    const formData = new FormData();
    formData.append('name', name);
    formData.append('address', address);
    formData.append('password', password);
//data.append('file', new Blob(['test payload'], { type: 'text/csv' }));

    return axios.post('user/join', formData);
}


function* signUp(action) {
    try {
        const result = yield call(signUpAPI, action.data);
        yield put({
            type: SIGN_UP_SUCCESS,
            data: result.data,
        });
    } catch (e) { // 실패
        yield put({
            type: SIGN_UP_FAILURE,
            error: {
                status: e.response.data.error.status,
                message: e.response.data.error.message,
            }
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
        yield put({
            type: LOG_IN_FAILURE,
            error: {
                status: e.response.data.error.status,
                message: e.response.data.error.message,
            }
        });
    }
}

function* watchLogin() {
    yield takeLatest(LOG_IN_REQUEST, login);
}

function emailLoginAPI(loginData) {
    return axios.post(`auth`, loginData);
}

function* emailLogin(action) {
    try {
        const result = yield call(emailLoginAPI, action.data);
        yield put({
            type: EMAIL_LOG_IN_SUCCESS,
            data: result.data,
        });
    } catch (e) { // 실패
        yield put({
            type: EMAIL_LOG_IN_FAILURE,
            error: {
                status: e.response.data.error.status,
                message: e.response.data.error.message,
            }
        });
    }
}

function* watchEmailLogin() {
    yield takeLatest(EMAIL_LOG_IN_REQUEST, emailLogin);
}


function followAPI({userId, token}) {
    return axios.post(`user/follow/${userId}`, {}, {
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
        yield put({
            type: FOLLOW_USER_FAILURE,
            error: {
                status: e.response.data.error.status,
                message: e.response.data.error.message,
            }
        });
    }
}

function* watchFollow() {
    yield takeLatest(FOLLOW_USER_REQUEST, follow);
}


function editProfileAPI({profileRequest, token}) {

    return axios.put(`user/profile`, profileRequest, {
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}

function* editProfile(action) {

    try {
        const result = yield call(editProfileAPI, action.data);
        yield put({
            type: EDIT_PROFILE_SUCCESS,
            data: result.data.response,
        });
    } catch (e) { // 실패
        yield put({
            type: EDIT_PROFILE_FAILURE,
            error: {
                status: e.response.data.error.status,
                message: e.response.data.error.message,
            }
        });
    }
}

function* watchEditProfile() {
    yield takeLatest(EDIT_PROFILE_REQUEST, editProfile);
}

function editProfileImageAPI({file, token}) {
    const formData = new FormData();

    formData.append('file', file);

    return axios.put(`user/profile-image`, formData, {
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}

function* editProfileImage(action) {

    try {
        const result = yield call(editProfileImageAPI, action.data);
        yield put({
            type: UPLOAD_IMAGE_SUCCESS,
            data: result.data.response,
        });
    } catch (e) { // 실패
        yield put({
            type: UPLOAD_IMAGE_FAILURE,
            error: {
                status: e.response.data.error.status,
                message: e.response.data.error.message,
            }
        });
    }
}

function* watchEditProfileImage() {
    yield takeLatest(UPLOAD_IMAGE_REQUEST, editProfileImage);
}

function readNotificationAPI({id, token}) {
    return axios.patch(`user/notification/${id}`, null,{
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}

function* readNotification(action) {

    try {
        const result = yield call(readNotificationAPI, action.data);
        yield put({
            type: READ_NOTIFICATION_SUCCESS,
            data: result.data.response,
        });
    } catch (e) { // 실패
        yield put({
            type: READ_NOTIFICATION_FAILURE,
            error: {
                status: e.response.data.error.status,
                message: e.response.data.error.message,
            }
        });
    }
}

function* watchReadNotification() {
    yield takeLatest(READ_NOTIFICATION_REQUEST, readNotification);
}


function unfollowAPI({userId, token}) {
    return axios.delete(`user/follow/${userId}`, {
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
        yield put({
            type: UNFOLLOW_USER_FAILURE,
            error: {
                status: e.response.data.error.status,
                message: e.response.data.error.message,
            }
        });
    }
}

function* watchUnfollow() {
    yield takeLatest(UNFOLLOW_USER_REQUEST, unfollow);
}
function removeNotificationAPI({id, token}) {
    return axios.delete(`user/notification/${id}`, {
        headers: {
            'api_key': 'Bearer ' + token,
        },
    });
}

function* removeNotification(action) {

    try {
        const result = yield call(removeNotificationAPI, action.data);
        yield put({
            type: REMOVE_NOTIFICATION_SUCCESS,
            data: result.data.response,
        });
    } catch (e) { // 실패
        yield put({
            type: REMOVE_NOTIFICATION_FAILURE,
            error: {
                status: e.response.data.error.status,
                message: e.response.data.error.message,
            }
        });
    }
}

function* watchRemoveNotification() {
    yield takeLatest(REMOVE_NOTIFICATION_REQUEST, removeNotification);
}

function removeFollowerAPI({userId, token}) {
    return axios.delete(`user/follower/${userId}`, {
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
        yield put({
            type: REMOVE_FOLLOWER_FAILURE,
            error: {
                status: e.response.data.error.status,
                message: e.response.data.error.message,
            }
        });
    }
}

function* watchRemovefollower() {
    yield takeLatest(REMOVE_FOLLOWER_REQUEST, removeFollower);
}

export default function* userSaga() {
    yield all([
        //GET
        fork(watchLoadMe),
        fork(watchLoadUser),
        fork(watchLoadfollowing),
        fork(watchLoadfollower),
        fork(watchResendEmail),
        fork(watchLoadNewNotification),
        fork(watchLoadReadNotification),

        //POST
        fork(watchEmailCheck),
        fork(watchNameCheck),
        fork(watchEmailCertificate),
        fork(watchSignUp),
        fork(watchLogin),
        fork(watchEmailLogin),
        fork(watchFollow),

        //PUT
        fork(watchEditProfile),
        fork(watchEditProfileImage),

        //PATCH
        fork(watchReadNotification),

        //DELETE
        fork(watchUnfollow),
        fork(watchRemovefollower),
        fork(watchRemoveNotification),
    ]);
}
