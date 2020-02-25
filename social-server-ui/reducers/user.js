const dummyUser = {
    name: '이름',
    Post: [],
    Followings: [],
    Followers: [],
    signUpData: {},
};

export const initialState = {
    isLogin: false,
    user: null,
};

export const LOG_IN = 'LOG_IN';
export const LOG_OUT = 'LOG_OUT';
export const SIGN_UP = 'SIGN_UP';

export const signUpAction = (data) => {
    return {
        type: SIGN_UP,
        data: data,
    }
};

export const loginAction = {
    type: LOG_IN,
    data: {
        name: '이름',
    },
};

export const logoutAction = {
    type: LOG_OUT,
};

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case LOG_IN: {
            return {
                ...state,
                isLogin: true,
                user: dummyUser,
            }
        }
        case LOG_OUT: {
            return {
                ...state,
                isLogin: false,
                user: null,
            }
        }

        case SIGN_UP: {
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