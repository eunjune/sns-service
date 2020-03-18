import cookie from 'react-cookies';
import produce from 'immer';

export const initialState = {
    isEmailOk: null,
    isEmailChecking: false,
    emailCheckingErrorReason: null,
    isLoggingIn: false,
    loginErrorReason: null,
    isSignedUp: false,
    isSigningUp: false,
    signUpErrorReason: null,
    me: null,
    followings: [],
    followers: [],
    user: null,
    isEditingName: false,
    editNameErrorReason: '',
    hasMoreFollower: false,
    hasMoreFollowing: false,
};

export const EMAIL_CHECK_REQUEST = 'EMAIL_CHECK_REQUEST';
export const EMAIL_CHECK_SUCCESS = 'EMAIL_CHECK_SUCCESS';
export const EMAIL_CHECK_FAILURE = 'EMAIL_CHECK_FAILURE';

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

export const EDIT_NAME_REQUEST = 'EDIT_NAME_REQUEST';
export const EDIT_NAME_SUCCESS = 'EDIT_NAME_SUCCESS';
export const EDIT_NAME_FAILURE = 'EDIT_NAME_FAILURE';

export const INCREMENT_NUMBER = 'INCREMENT_NUMBER';

export const ADD_POST_TO_ME = 'ADD_POST_TO_ME';

const reducer = (state = initialState, action) => {
    return produce(state, (draft) => {
        switch (action.type) {
            case EMAIL_CHECK_REQUEST: {
                draft.isEmailOk = null;
                draft.isEmailChecking = true;
                draft.emailCheckingErrorReason = null;
                break;
            }
    
            case EMAIL_CHECK_SUCCESS: {
                draft.isEmailOk = action.data;
                draft.isEmailChecking = false;
                break;
            }
    
            case EMAIL_CHECK_FAILURE: {
                draft.emailCheckingErrorReason = action.error;
                draft.isEmailChecking = false;
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
                draft.loginErrorReason = null;
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
    
            case EDIT_NAME_REQUEST: {
                draft.isEditingName = true;
                draft.editNameErrorReason = '';
                break;
            }
    
            case EDIT_NAME_SUCCESS: {
                draft.me = action.data;
                draft.isEditingName = false;
                break;
            }
    
            case EDIT_NAME_FAILURE: {
                draft.isEditingName = false;
                break;
            }
            
            default: {
                break;
            }
        }
    });

};

export default reducer;
