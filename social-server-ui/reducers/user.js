const dumyme = {
    name: '이름',
    Post: [],
    Followings: [],
    Followers: [],
    id:1
};

export const initialState = {
    isEmailOk: undefined,
    isEmailChecking: false,
    emailCheckingErrorReason: '',
    isLogin: false,
    isLoggingOut: false,
    isLoggingIn: false,
    loginErrorReason: '',
    isSignedUp: false,
    isSigningUp: false,
    signUpErrorReason: '',
    me: null,
    followingList: [],
    followerList: [],
    meInfo: null,
};

export const EMAIL_CHECK_REQUEST = 'EMAIL_CHECK_REQUEST';
export const EMAIL_CHECK_SUCCESS = 'EMAIL_CHECK_SUCCESS';
export const EMAIL_CHECK_FAILURE = 'EMAIL_CHECK_FAILURE';

export const LOG_IN_REQUEST = 'LOG_IN_REQUEST';
export const LOG_IN_SUCCESS = 'LOG_IN_SUCCESS';
export const LOG_IN_FAILURE = 'LOG_IN_FAILURE';

export const LOG_OUT_REQUEST = 'LOG_OUT_REQUEST';
export const LOG_OUT_SUCCESS = 'LOG_OUT_SUCCESS';
export const LOG_OUT_FAILURE = 'LOG_OUT_FAILURE';

export const LOAD_FOLLOW_REQUEST = 'LOAD_FOLLOW_REQUEST';
export const LOAD_FOLLOW_SUCCESS = 'LOAD_FOLLOW_SUCCESS';
export const LOAD_FOLLOW_FAILURE = 'LOAD_FOLLOW_FAILURE';

export const FOLLOW_me_REQUEST = 'FOLLOW_me_REQUEST';
export const FOLLOW_me_SUCCESS = 'FOLLOW_me_SUCCESS';
export const FOLLOW_me_FAILURE = 'FOLLOW_me_FAILURE';

export const UNFOLLOW_me_REQUEST = 'UNFOLLOW_me_REQUEST';
export const UNFOLLOW_me_SUCCESS = 'UNFOLLOW_me_SUCCESS';
export const UNFOLLOW_me_FAILURE = 'UNFOLLOW_me_FAILURE';

export const REMOVE_FOLLOWER_REQUEST = 'REMOVE_FOLLOWER_REQUEST';
export const REMOVE_FOLLOWER_SUCCESS = 'REMOVE_FOLLOWER_SUCCESS';
export const REMOVE_FOLLOWER_FAILURE = 'REMOVE_FOLLOWER_FAILURE';

export const SIGN_UP_REQUEST = 'SIGN_UP_REQUEST';
export const SIGN_UP_SUCCESS = 'SIGN_UP_SUCCESS';
export const SIGN_UP_FAILURE = 'SIGN_UP_FAILURE';

export const INCREMENT_NUMBER = 'INCREMENT_NUMBER';

export const ADD_POST_TO_ME = 'ADD_POST_TO_ME';

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case EMAIL_CHECK_REQUEST: {
            return {
                ...state,
                isEmailOk: undefined,
                isEmailChecking: true,
                emailCheckingErrorReason: '',
            };
        }

        case EMAIL_CHECK_SUCCESS: {
            return {
                ...state,
                isEmailOk: action.data,
                isEmailChecking: false,
            };
        }

        case EMAIL_CHECK_FAILURE: {
            return {
                ...state,
                emailCheckingErrorReason: action.error,
                isEmailChecking: false,
            };
        }

        case LOG_IN_REQUEST: {
            return {
                ...state,
                isLoggingIn: true,
                loginErrorReason: '',
            };
        }

        case LOG_IN_SUCCESS: {
            return {
                ...state,
                isLoggingIn: false,
                isLogin: true,
                me: dumyme,
            };
        }

        case LOG_IN_FAILURE: {
            return {
                ...state,
                isLoggingIn: false,
                isLogin: false,
                loginErrorReason: action.error,
                me: null,
            };
        }

        case LOG_OUT_REQUEST: {
            return {
                ...state,
                isLogin: false,
                me: null,
            };
        }

        case LOG_OUT_SUCCESS: {
            return {
                ...state,
                isLogin: false,
                me: null,
            };
        }

        case LOG_OUT_FAILURE: {
            return {
                ...state,
                isLogin: false,
                me: null,
                loginErrorReason: '',
            };
        }

        case SIGN_UP_REQUEST: {
            console.log('요청');
            return {
                ...state,
                isSigningUp: true,
                isSignedUp: false,
                signUpErrorReason: ''
            };
        }

        case SIGN_UP_SUCCESS: {
            return {
                ...state,
                isSigningUp: false,
                isSignedUp: true,
            };
        }

        case SIGN_UP_FAILURE: {
            return {
                ...state,
                isSigningUp: false,
                signUpErrorReason: action.error,
            };
        }

        default: {
            return {
                ...state
            };
        }
    }
};

export default reducer;
