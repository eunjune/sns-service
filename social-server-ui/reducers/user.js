import cookie from 'react-cookies';
import produce from 'immer';
import Router from "next/router";
import {UPLOAD_IMAGES_FAILURE, UPLOAD_IMAGES_REQUEST, UPLOAD_IMAGES_SUCCESS} from "./post";

export const initialState = {
    isEmailOk: null,
    isEmailChecking: false,
    isCertificated: false,
    certificateErrorReason: '',
    emailCheckingErrorReason: null,
    isNameOk: null,
    isNameChecking: false,
    nameCheckingErrorReason: null,
    isLoggingIn: false,
    loginErrorReason: '',
    isSignedUp: false,
    isSigningUp: false,
    signUpErrorReason: '',
    me: null,
    followings: [],
    followers: [],
    user: null,
    isEditing: false,
    editErrorReason: '',
    hasMoreFollower: false,
    hasMoreFollowing: false,
};

export const EMAIL_CHECK_REQUEST = 'EMAIL_CHECK_REQUEST';
export const EMAIL_CHECK_SUCCESS = 'EMAIL_CHECK_SUCCESS';
export const EMAIL_CHECK_FAILURE = 'EMAIL_CHECK_FAILURE';

export const EMAIL_CERTIFICATION_REQUEST = 'EMAIL_CERTIFICATION_REQUEST';
export const EMAIL_CERTIFICATION_SUCCESS = 'EMAIL_CERTIFICATION_SUCCESS';
export const EMAIL_CERTIFICATION_FAILURE = 'EMAIL_CERTIFICATION_FAILURE';

export const EMAIL_RESEND_REQUEST = 'EMAIL_RESEND_REQUEST';
export const EMAIL_RESEND_SUCCESS = 'EMAIL_RESEND_SUCCESS';
export const EMAIL_RESEND_FAILURE = 'EMAIL_RESEND_FAILURE';

export const NAME_CHECK_REQUEST = 'NAME_CHECK_REQUEST';
export const NAME_CHECK_SUCCESS = 'NAME_CHECK_SUCCESS';
export const NAME_CHECK_FAILURE = 'NAME_CHECK_FAILURE';

export const LOG_IN_REQUEST = 'LOG_IN_REQUEST';
export const LOG_IN_SUCCESS = 'LOG_IN_SUCCESS';
export const LOG_IN_FAILURE = 'LOG_IN_FAILURE';

export const LOG_OUT = 'LOG_OUT';

export const LOAD_USER_REQUEST = 'LOAD_USER_REQUEST';
export const LOAD_USER_SUCCESS = 'LOAD_USER_SUCCESS';
export const LOAD_USER_FAILURE = 'LOAD_USER_FAILURE';

export const LOAD_ME_REQUEST = 'LOAD_ME_REQUEST';
export const LOAD_ME_SUCCESS = 'LOAD_ME_SUCCESS';
export const LOAD_ME_FAILURE = 'LOAD_ME_FAILURE';

export const LOAD_FOLLOWER_REQUEST = 'LOAD_FOLLOWER_REQUEST';
export const LOAD_FOLLOWER_SUCCESS = 'LOAD_FOLLOWER_SUCCESS';
export const LOAD_FOLLOWER_FAILURE = 'LOAD_FOLLOWER_FAILURE';

export const LOAD_FOLLOWING_REQUEST = 'LOAD_FOLLOWING_REQUEST';
export const LOAD_FOLLOWING_SUCCESS = 'LOAD_FOLLOWING_SUCCESS';
export const LOAD_FOLLOWING_FAILURE = 'LOAD_FOLLOWING_FAILURE';

export const FOLLOW_USER_REQUEST = 'FOLLOW_USER_REQUEST';
export const FOLLOW_USER_SUCCESS = 'FOLLOW_USER_SUCCESS';
export const FOLLOW_USER_FAILURE = 'FOLLOW_USER_FAILURE';

export const UNFOLLOW_USER_REQUEST = 'UNFOLLOW_USER_REQUEST';
export const UNFOLLOW_USER_SUCCESS = 'UNFOLLOW_USER_SUCCESS';
export const UNFOLLOW_USER_FAILURE = 'UNFOLLOW_USER_FAILURE';

export const REMOVE_FOLLOWER_REQUEST = 'REMOVE_FOLLOWER_REQUEST';
export const REMOVE_FOLLOWER_SUCCESS = 'REMOVE_FOLLOWER_SUCCESS';
export const REMOVE_FOLLOWER_FAILURE = 'REMOVE_FOLLOWER_FAILURE';

export const SIGN_UP_REQUEST = 'SIGN_UP_REQUEST';
export const SIGN_UP_SUCCESS = 'SIGN_UP_SUCCESS';
export const SIGN_UP_FAILURE = 'SIGN_UP_FAILURE';

export const EDIT_PROFILE_REQUEST = 'EDIT_PROFILE_REQUEST';
export const EDIT_PROFILE_SUCCESS = 'EDIT_PROFILE_SUCCESS';
export const EDIT_PROFILE_FAILURE = 'EDIT_PROFILE_FAILURE';

export const UPLOAD_IMAGE_REQUEST = 'UPLOAD_IMAGE_REQUEST';
export const UPLOAD_IMAGE_SUCCESS = 'UPLOAD_IMAGE_SUCCESS';
export const UPLOAD_IMAGE_FAILURE = 'UPLOAD_IMAGE_FAILURE';

export const INCREMENT_NUMBER = 'INCREMENT_NUMBER';

export const ADD_POST_TO_ME = 'ADD_POST_TO_ME';

const reducer = (state = initialState, action) => {
    return produce(state, (draft) => {
        switch (action.type) {
            case EMAIL_CHECK_REQUEST: {
                draft.isEmailOk = false;
                draft.isEmailChecking = true;
                draft.emailCheckingErrorReason = null;
                break;
            }
    
            case EMAIL_CHECK_SUCCESS: {
                draft.isEmailOk = true;
                draft.isEmailChecking = false;
                break;
            }
    
            case EMAIL_CHECK_FAILURE: {
                draft.emailCheckingErrorReason = action.error;
                draft.isEmailChecking = false;
                break;
            }

            case EMAIL_CERTIFICATION_REQUEST: {
                break;
            }

            case EMAIL_CERTIFICATION_SUCCESS: {
                draft.me = action.data;
                break;
            }

            case EMAIL_CERTIFICATION_FAILURE: {
                alert(action.error);
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
                alert(action.error);
                break;
            }

            case NAME_CHECK_REQUEST: {
                draft.isNameOk = false;
                draft.isNameChecking = true;
                draft.nameCheckingErrorReason = null;
                break;
            }

            case NAME_CHECK_SUCCESS: {
                draft.isNameOk = true;
                draft.isNameChecking = false;
                break;
            }

            case NAME_CHECK_FAILURE: {
                draft.nameCheckingErrorReason = action.error;
                draft.isNameChecking = false;
                break;
            }
    
            case SIGN_UP_REQUEST: {
                draft.isSignedUp = true;
                draft.isSignedUp = false;
                draft.isEmailOk = false;
                draft.signUpErrorReason = null;
                break;
            }
    
            case SIGN_UP_SUCCESS: {
                const me = action.data.response.user;
                const token = action.data.response.token;
    
                cookie.save('token',token, { path: '/' });

                draft.isSigningUp = false;
                draft.isSignedUp = true;
                draft.me = me;
                break;
            }
    
            case SIGN_UP_FAILURE: {
                draft.isSignedUp = false;
                draft.signUpErrorReason = action.error;
                break;
            }
    
            case LOG_IN_REQUEST: {
                cookie.remove('token', { path: '/' });
                draft.isLoggingIn = true;
                draft.loginErrorReason = '';
                break;
            }
    
            case LOG_IN_SUCCESS: {
                const me = action.data.response.user;
                const token = action.data.response.token;
    
                cookie.save('token',token, { path: '/' });

                draft.isLoggingIn = false;
                draft.me = me;
                break;
            }
    
            case LOG_IN_FAILURE: {
                draft.isLoggingIn = false;
                draft.loginErrorReason = action.error;
                draft.me = null;
                break;
            }
    
            case LOG_OUT: {
                cookie.remove('token', { path: '/' });
                draft.me = null;
                break;
            }
    
            case LOAD_ME_REQUEST: {
                break;
            }
    
            case LOAD_ME_SUCCESS: {
                draft.me = action.data;
                break;
            }
    
            case LOAD_ME_FAILURE: {
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
                break;
            }
    
            case UNFOLLOW_USER_REQUEST: {
                break;
            }
    
            case UNFOLLOW_USER_SUCCESS: {
                draft.me.followings = draft.me.followings.filter(v=>v !== action.data);
                break;
            }
    
            case UNFOLLOW_USER_FAILURE: {
                break;
            }
    
            case LOAD_FOLLOWING_REQUEST: {
                draft.followings = !action.data.offset ? [] : draft.followings;
                draft.hasMoreFollowing = action.data.offset ? draft.hasMoreFollowing : true;
                break;1
            }
    
            case LOAD_FOLLOWING_SUCCESS: {
                draft.followings = draft.followings.concat(action.data);
                draft.hasMoreFollowing = action.data.length === 3;
                break;
            }
    
            case LOAD_FOLLOWING_FAILURE: {
                break;
            }
    
            case LOAD_FOLLOWER_REQUEST: {
                draft.followers = draft.followers.length === 0 ? [] : draft.followers;
                draft.hasMoreFollower = action.data.offset ? draft.hasMoreFollower : true;
                break;
            }
    
            case LOAD_FOLLOWER_SUCCESS: {
                draft.followers = draft.followers.concat(action.data)
                draft.hasMoreFollower = action.data.length === 3;
                break;
            }
    
            case LOAD_FOLLOWER_FAILURE: {
                break;
            }
    
            case REMOVE_FOLLOWER_REQUEST: {
                break;
            }
    
            case REMOVE_FOLLOWER_SUCCESS: {
                draft.me.followers = draft.me.followers.filter(v=>v.id !== action.data);
                draft.followers = draft.followers.filter(v => v.id !== action.data);
                break;
            }
    
            case REMOVE_FOLLOWER_FAILURE: {
                break;
            }
    
            case EDIT_PROFILE_REQUEST: {
                draft.isEditing = true;
                draft.editErrorReason = '';
                break;
            }
    
            case EDIT_PROFILE_SUCCESS: {
                draft.me = action.data;
                draft.isEditing = false;
                break;
            }
    
            case EDIT_PROFILE_FAILURE: {
                draft.isEditing = false;
                draft.editErrorReason = action.error;
                break;
            }

            case UPLOAD_IMAGE_REQUEST: {
                draft.isEditing = true;
                draft.editErrorReason = '';
                break;
            }

            case UPLOAD_IMAGE_SUCCESS: {
                draft.me = action.data;
                draft.isEditing = false;
                break;
            }

            case UPLOAD_IMAGE_FAILURE: {
                draft.isEditing = false;
                draft.editErrorReason = action.error;
                break;
            }
            
            default: {
                break;
            }
        }
    });

};

export default reducer;
