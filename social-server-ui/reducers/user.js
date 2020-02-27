const dumyme = {
    name: '이름',
    Post: [],
    Followings: [],
    Followers: [],
};

export const initialState = {
    isLogin: false,
    isLoggingOut: false,
    isLoggingIn: false,
    loginErrorReason: '',
    signedUp: false,
    isSigningUp: false,
    signUpErrorReason: '',
    me: null,
    followingList: [],
    followerList: [],
    meInfo: null,
};

export const LOG_IN_REQUEST = 'LOG_IN_REQUEST';
export const LOG_IN_SUCCESS = 'LOG_IN_SUCCESS';
export const LOG_IN_FAILURE = 'LOG_IN_FAILURE';

export const LOG_me_REQUEST = 'LOG_me_REQUEST';
export const LOG_me_SUCCESS = 'LOG_me_SUCCESS';
export const LOG_me_FAILURE = 'LOG_me_FAILURE';

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
            }
        }

        case LOG_IN_FAILURE: {
            return {
                ...state,
                isLoggingIn: false,
                isLogin: false,
                loginErrorReason: action.error,
                me: null,
            }
        }

        case LOG_OUT_REQUEST: {
            return {
                ...state,
                isLogin: false,
                me: null,
            }
        }

        case LOG_OUT_SUCCESS: {
            return {
                ...state,
                isLogin: false,
                me: null,
            }
        }

        case LOG_OUT_FAILURE: {
            return {
                ...state,
                isLogin: false,
                me: null,
            }
        }

        case SIGN_UP_REQUEST: {
            return {
                ...state,
                signUpData: action.data,
            }
        }

        case SIGN_UP_SUCCESS: {
            return {
                ...state,
                signUpData: action.data,
            }
        }

        case SIGN_UP_FAILURE: {
            return {
                ...state,
                signUpData: action.data,
            }
        }

        default: {
            return {
                ...state
            }
        }
    }
};

export default reducer;
