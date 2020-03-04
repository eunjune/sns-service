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
};

export const EMAIL_CHECK_REQUEST = 'EMAIL_CHECK_REQUEST';
export const EMAIL_CHECK_SUCCESS = 'EMAIL_CHECK_SUCCESS';
export const EMAIL_CHECK_FAILURE = 'EMAIL_CHECK_FAILURE';

export const LOG_IN_REQUEST = 'LOG_IN_REQUEST';
export const LOG_IN_SUCCESS = 'LOG_IN_SUCCESS';
export const LOG_IN_FAILURE = 'LOG_IN_FAILURE';

export const LOG_OUT = 'LOG_OUT';
// export const LOG_OUT_REQUEST = 'LOG_OUT_REQUEST';
// export const LOG_OUT_SUCCESS = 'LOG_OUT_SUCCESS';
// export const LOG_OUT_FAILURE = 'LOG_OUT_FAILURE';

export const LOAD_USER_REQUEST = 'LOAD_USER_REQUEST';
export const LOAD_USER_SUCCESS = 'LOAD_USER_SUCCESS';
export const LOAD_USER_FAILURE = 'LOAD_USER_FAILURE';

export const LOAD_ME_REQUEST = 'LOAD_ME_REQUEST';
export const LOAD_ME_SUCCESS = 'LOAD_ME_SUCCESS';
export const LOAD_ME_FAILURE = 'LOAD_ME_FAILURE';

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
                isEmailOk: null,
                isEmailChecking: true,
                emailCheckingErrorReason: null
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

        case SIGN_UP_REQUEST: {
            return {
                ...state,
                isSigningUp: true,
                isSignedUp: false,
                isEmailOk: false,
                signUpErrorReason: null
            };
        }

        case SIGN_UP_SUCCESS: {
            const me = action.data.response.user;
            const token = action.data.response.token;

            sessionStorage.setItem('token',token);

            return {
                ...state,
                isSigningUp: false,
                isSignedUp: true,
                me: me,
            };
        }

        case SIGN_UP_FAILURE: {
            return {
                ...state,
                isSigningUp: false,
                signUpErrorReason: action.error,
            };
        }

        case LOG_IN_REQUEST: {
            return {
                ...state,
                isLoggingIn: true,
                loginErrorReason: null,
            };
        }

        case LOG_IN_SUCCESS: {
            const me = action.data.response.user;
            const token = action.data.response.token;

            sessionStorage.setItem('token',token);

            return {
                ...state,
                isLoggingIn: false,
                me: me,
            };
        }

        case LOG_IN_FAILURE: {
            return {
                ...state,
                isLoggingIn: false,

                loginErrorReason: action.error,
                me: null,
            };
        }

        case LOG_OUT: {
            sessionStorage.setItem('token',null);

            return {
                ...state,
                me: null,
            };
        }

        case LOAD_ME_REQUEST: {

            return {
                ...state,
            };
        }

        case LOAD_ME_SUCCESS: {
            return {
                ...state,
                me: action.data,
            };
        }

        case LOAD_ME_FAILURE: {
            return {
                ...state,
            };
        }

        case LOAD_USER_REQUEST: {

            return {
                ...state,
            };
        }

        case LOAD_USER_SUCCESS: {
            return {
                ...state,
                user: action.data,
            };
        }

        case LOAD_USER_FAILURE: {
            return {
                ...state,
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
