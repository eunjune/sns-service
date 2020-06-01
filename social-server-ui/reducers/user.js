import cookie from 'react-cookies';
import produce from 'immer';

export const initialState = {
    me: null,
    user: null,
    followings: [],
    followers: [],
    newNotifications: [],
    readNotifications: [],

    isEmailOk: false,
    isCertificated: false,
    isNameOk: false,
    isLoggingIn: false,
    isEmailLogInWaiting: false,
    isEmailLogin: false,
    isSigningUp: false,
    isEditing: false,
    hasMoreFollower: false,
    hasMoreFollowing: false,

    signUpError: null,
    loginError: null,
    editError: null,
    nameCheckingError: null,
    emailCheckingError: null,
};

export const LOAD_ME_REQUEST = 'LOAD_ME_REQUEST';
export const LOAD_ME_SUCCESS = 'LOAD_ME_SUCCESS';
export const LOAD_ME_FAILURE = 'LOAD_ME_FAILURE';

export const LOAD_USER_REQUEST = 'LOAD_USER_REQUEST';
export const LOAD_USER_SUCCESS = 'LOAD_USER_SUCCESS';
export const LOAD_USER_FAILURE = 'LOAD_USER_FAILURE';

export const LOAD_FOLLOWING_REQUEST = 'LOAD_FOLLOWING_REQUEST';
export const LOAD_FOLLOWING_SUCCESS = 'LOAD_FOLLOWING_SUCCESS';
export const LOAD_FOLLOWING_FAILURE = 'LOAD_FOLLOWING_FAILURE';

export const LOAD_FOLLOWER_REQUEST = 'LOAD_FOLLOWER_REQUEST';
export const LOAD_FOLLOWER_SUCCESS = 'LOAD_FOLLOWER_SUCCESS';
export const LOAD_FOLLOWER_FAILURE = 'LOAD_FOLLOWER_FAILURE';

export const LOAD_NOTIFICATION_REQUEST = 'LOAD_NOTIFICATION_REQUEST';
export const LOAD_NOTIFICATION_SUCCESS = 'LOAD_NOTIFICATION_SUCCESS';
export const LOAD_NOTIFICATION_FAILURE = 'LOAD_NOTIFICATION_FAILURE';

export const EMAIL_CHECK_REQUEST = 'EMAIL_CHECK_REQUEST';
export const EMAIL_CHECK_SUCCESS = 'EMAIL_CHECK_SUCCESS';
export const EMAIL_CHECK_FAILURE = 'EMAIL_CHECK_FAILURE';

export const NAME_CHECK_REQUEST = 'NAME_CHECK_REQUEST';
export const NAME_CHECK_SUCCESS = 'NAME_CHECK_SUCCESS';
export const NAME_CHECK_FAILURE = 'NAME_CHECK_FAILURE';

export const EMAIL_CERTIFICATION_REQUEST = 'EMAIL_CERTIFICATION_REQUEST';
export const EMAIL_CERTIFICATION_SUCCESS = 'EMAIL_CERTIFICATION_SUCCESS';
export const EMAIL_CERTIFICATION_FAILURE = 'EMAIL_CERTIFICATION_FAILURE';

export const EMAIL_RESEND_REQUEST = 'EMAIL_RESEND_REQUEST';
export const EMAIL_RESEND_SUCCESS = 'EMAIL_RESEND_SUCCESS';
export const EMAIL_RESEND_FAILURE = 'EMAIL_RESEND_FAILURE';

export const SIGN_UP_REQUEST = 'SIGN_UP_REQUEST';
export const SIGN_UP_SUCCESS = 'SIGN_UP_SUCCESS';
export const SIGN_UP_FAILURE = 'SIGN_UP_FAILURE';

export const LOG_IN_REQUEST = 'LOG_IN_REQUEST';
export const LOG_IN_SUCCESS = 'LOG_IN_SUCCESS';
export const LOG_IN_FAILURE = 'LOG_IN_FAILURE';

export const EMAIL_LOG_IN_REQUEST = 'EMAIL_LOG_IN_REQUEST';
export const EMAIL_LOG_IN_SUCCESS = 'EMAIL_LOG_IN_SUCCESS';
export const EMAIL_LOG_IN_FAILURE = 'EMAIL_LOG_IN_FAILURE';

export const FOLLOW_USER_REQUEST = 'FOLLOW_USER_REQUEST';
export const FOLLOW_USER_SUCCESS = 'FOLLOW_USER_SUCCESS';
export const FOLLOW_USER_FAILURE = 'FOLLOW_USER_FAILURE';

export const EDIT_PROFILE_REQUEST = 'EDIT_PROFILE_REQUEST';
export const EDIT_PROFILE_SUCCESS = 'EDIT_PROFILE_SUCCESS';
export const EDIT_PROFILE_FAILURE = 'EDIT_PROFILE_FAILURE';

export const UPLOAD_IMAGE_REQUEST = 'UPLOAD_IMAGE_REQUEST';
export const UPLOAD_IMAGE_SUCCESS = 'UPLOAD_IMAGE_SUCCESS';
export const UPLOAD_IMAGE_FAILURE = 'UPLOAD_IMAGE_FAILURE';

export const READ_NOTIFICATION_REQUEST = 'READ_NOTIFICATION_REQUEST';
export const READ_NOTIFICATION_SUCCESS = 'READ_NOTIFICATION_SUCCESS';
export const READ_NOTIFICATION_FAILURE = 'READ_NOTIFICATION_FAILURE';

export const UNFOLLOW_USER_REQUEST = 'UNFOLLOW_USER_REQUEST';
export const UNFOLLOW_USER_SUCCESS = 'UNFOLLOW_USER_SUCCESS';
export const UNFOLLOW_USER_FAILURE = 'UNFOLLOW_USER_FAILURE';

export const REMOVE_NOTIFICATION_REQUEST = 'REMOVE_NOTIFICATION_REQUEST';
export const REMOVE_NOTIFICATION_SUCCESS = 'REMOVE_NOTIFICATION_SUCCESS';
export const REMOVE_NOTIFICATION_FAILURE = 'REMOVE_NOTIFICATION_FAILURE';

export const REMOVE_FOLLOWER_REQUEST = 'REMOVE_FOLLOWER_REQUEST';
export const REMOVE_FOLLOWER_SUCCESS = 'REMOVE_FOLLOWER_SUCCESS';
export const REMOVE_FOLLOWER_FAILURE = 'REMOVE_FOLLOWER_FAILURE';

export const RESET = 'RESET';
export const EMAIL_LOGIN_FINISH = 'EMAIL_LOGIN_FINISH';
export const LOG_OUT = 'LOG_OUT';
export const ADD_POST = 'ADD_POST';
export const REMOVE_POST = 'REMOVE_POST';

const reducer = (state = initialState, action) => {
    return produce(state, (draft) => {
        switch (action.type) {

            case LOAD_ME_REQUEST: {
                break;
            }

            case LOAD_ME_SUCCESS: {
                draft.me = action.data;
                break;
            }

            case LOAD_ME_FAILURE: {
                console.error(action.error);
                break;
            }

            case LOAD_USER_REQUEST: {
                break;
            }

            case LOAD_USER_SUCCESS: {
                draft.user = action.data;
                break;
            }

            case LOAD_USER_FAILURE: {
                console.error(action.error);
                break;
            }

            case LOAD_FOLLOWING_REQUEST: {
                draft.followings = !action.data.offset ? [] : draft.followings;
                draft.hasMoreFollowing = action.data.offset ? draft.hasMoreFollowing : true;
                break;
            }

            case LOAD_FOLLOWING_SUCCESS: {
                draft.followings = draft.followings.concat(action.data);
                draft.hasMoreFollowing = action.data.length === 3;
                break;
            }

            case LOAD_FOLLOWING_FAILURE: {
                console.error(action.error);
                break;
            }

            case LOAD_FOLLOWER_REQUEST: {
                draft.followers = !action.data.offset ? [] : draft.followers;
                draft.hasMoreFollower = action.data.offset ? draft.hasMoreFollower : true;
                break;
            }

            case LOAD_FOLLOWER_SUCCESS: {
                draft.followers = draft.followers.concat(action.data);
                draft.hasMoreFollower = action.data.length === 3;
                break;
            }

            case LOAD_FOLLOWER_FAILURE: {
                console.error(action.error);
                break;
            }

            case LOAD_NOTIFICATION_REQUEST: {
                draft.newNotifications = [];
                break;
            }

            case LOAD_NOTIFICATION_SUCCESS: {
                draft.newNotifications = action.data.filter(v => !v.readMark);
                draft.readNotifications = action.data.filter(v => v.readMark);
                break;
            }

            case LOAD_NOTIFICATION_FAILURE: {
                console.error(action.error);
                break;
            }

            case EMAIL_RESEND_REQUEST: {
                break;
            }

            case EMAIL_RESEND_SUCCESS: {
                alert('인증 이메일이 재전송 되었습니다.');
                break;
            }

            case EMAIL_RESEND_FAILURE: {
                console.error(action.error);
                break;
            }

            case EMAIL_CHECK_REQUEST: {
                draft.isEmailOk = false;
                draft.emailCheckingError = null;
                break;
            }

            case EMAIL_CHECK_SUCCESS: {
                draft.isEmailOk = true;
                break;
            }

            case EMAIL_CHECK_FAILURE: {
                console.error(action.error);

                draft.emailCheckingError = action.error;
                break;
            }


            case NAME_CHECK_REQUEST: {
                draft.isNameOk = false;
                draft.nameCheckingError = null;
                break;
            }

            case NAME_CHECK_SUCCESS: {
                draft.isNameOk = true;
                break;
            }

            case NAME_CHECK_FAILURE: {
                console.error(action.error);

                draft.nameCheckingError = action.error;
                break;
            }

            case EMAIL_CERTIFICATION_REQUEST: {
                break;
            }

            case EMAIL_CERTIFICATION_SUCCESS: {
                console.log(action.data);
                draft.me = action.data;
                break;
            }

            case EMAIL_CERTIFICATION_FAILURE: {
                console.error(action.error);

                break;
            }

            case SIGN_UP_REQUEST: {
                draft.isEmailOk = false;
                draft.signUpError = null;
                break;
            }

            case SIGN_UP_SUCCESS: {
                const me = action.data.response.user;
                const token = action.data.response.token;

                cookie.save('token', token, {path: '/'});

                draft.isSigningUp = false;
                draft.me = me;
                break;
            }

            case SIGN_UP_FAILURE: {
                console.error(action.error);

                draft.signUpError = action.error;
                draft.isSigningUp = false;
                break;
            }

            case LOG_IN_REQUEST: {
                cookie.remove('token', {path: '/'});
                draft.isLoggingIn = true;
                draft.loginError = null;
                break;
            }

            case LOG_IN_SUCCESS: {
                const me = action.data.response.user;
                const token = action.data.response.token;

                cookie.save('token', token, {path: '/'});

                draft.isLoggingIn = false;
                draft.me = me;
                break;
            }

            case LOG_IN_FAILURE: {
                console.error(action.error);

                draft.isLoggingIn = false;
                draft.loginError = action.error;
                draft.me = null;
                break;
            }

            case EMAIL_LOG_IN_REQUEST: {
                cookie.remove('token', {path: '/'});
                draft.isLoggingIn = true;
                draft.loginError = null;
                break;
            }

            case EMAIL_LOG_IN_SUCCESS: {
                draft.isLoggingIn = false;
                draft.isEmailLogInWaiting = true;
                draft.loginError = null;
                break;
            }

            case EMAIL_LOG_IN_FAILURE: {
                console.error(action.error);

                draft.isLoggingIn = false;
                draft.loginError = action.error;
                draft.me = null;
                break;
            }

            case FOLLOW_USER_REQUEST: {
                break;
            }

            case FOLLOW_USER_SUCCESS: {
                draft.me = action.data;
                break;
            }

            case FOLLOW_USER_FAILURE: {
                console.error(action.error);
                break;
            }

            case UPLOAD_IMAGE_REQUEST: {
                draft.isEditing = true;
                draft.editError = null;
                break;
            }

            case UPLOAD_IMAGE_SUCCESS: {
                draft.me = action.data;
                draft.isEditing = false;
                break;
            }

            case UPLOAD_IMAGE_FAILURE: {
                console.error(action.error);

                draft.isEditing = false;
                draft.editError = action.error;
                break;
            }

            case EDIT_PROFILE_REQUEST: {
                draft.isEditing = true;
                draft.editError = null;
                break;
            }

            case EDIT_PROFILE_SUCCESS: {
                alert('수정완료');

                draft.me = action.data;
                draft.isEditing = false;
                break;
            }

            case EDIT_PROFILE_FAILURE: {
                console.error(action.error);

                draft.isEditing = false;
                draft.editError = action.error;
                break;
            }

            case READ_NOTIFICATION_REQUEST: {
                break;
            }

            case READ_NOTIFICATION_SUCCESS: {
                --draft.me.notificationCount;
                break;
            }

            case READ_NOTIFICATION_FAILURE: {
                console.error(action.error);

                break;
            }

            case UNFOLLOW_USER_REQUEST: {
                break;
            }

            case UNFOLLOW_USER_SUCCESS: {
                if (draft.followings.length > 0) {
                    draft.followings = draft.followings.filter(v => v.id !== action.data);
                }

                draft.me.followings = draft.me.followings.filter(v => v !== action.data);
                break;
            }

            case UNFOLLOW_USER_FAILURE: {
                console.error(action.error);

                break;
            }

            case REMOVE_NOTIFICATION_REQUEST: {

                break;
            }

            case REMOVE_NOTIFICATION_SUCCESS: {
                draft.readNotifications = draft.readNotifications.filter(v => v.id !== action.data);
                draft.newNotifications = draft.newNotifications.filter(v => v.id !== action.data);
                break;
            }

            case REMOVE_NOTIFICATION_FAILURE: {
                console.error(action.error);
                break;
            }

            case REMOVE_FOLLOWER_REQUEST: {
                break;
            }

            case REMOVE_FOLLOWER_SUCCESS: {
                draft.followers = draft.followers.filter(v => v.id !== action.data);
                draft.me.followers = draft.me.followers.filter(v => v !== action.data);
                break;
            }

            case REMOVE_FOLLOWER_FAILURE: {
                console.error(action.error);

                break;
            }

            case EMAIL_LOGIN_FINISH: {
                draft.isEmailLogin = true;
                break;
            }

            case LOG_OUT: {
                cookie.remove('token', {path: '/'});
                draft.me = null;
                draft.followings = [];
                draft.followers = [];
                draft.isEmailLogin = false;

                break;
            }

            case RESET: {
                draft.isEmailOk= false,
                draft.isCertificated= false,
                draft.isNameOk= false,
                draft.isLoggingIn= false,
                draft.isEmailLogInWaiting= false,
                draft.isEmailLogin= false,
                draft.isSigningUp= false,
                draft.isEditing= false,
                draft.hasMoreFollower= false,
                draft.hasMoreFollowing= false,

                draft.signUpError= null,
                draft.loginError= null,
                draft.editError= null,
                draft.nameCheckingError= null,
                draft.emailCheckingError= null

                break;
            }

            default: {
                break;
            }
        }
    });

};

export default reducer;
